-- noinspection SqlResolveForFile

-- email used in users table
-- email type, copied from stack overflow: "https://dba.stackexchange.com/questions/68266/what-is-the-best-way-to-store-an-email-address-in-postgresql"
CREATE EXTENSION citext;
CREATE DOMAIN email AS citext
  CHECK ( value ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$' );

-- password
CREATE EXTENSION pgcrypto;

-- sequences
-- Sequence: reservation_id
CREATE SEQUENCE reservation_id
      INCREMENT BY 1
      MINVALUE 1
      START WITH 1
;

-- Sequence: generates ids for cities
CREATE SEQUENCE cities_id_seq
    INCREMENT BY 1
    MINVALUE 100
    START WITH 100
;

-- Sequence: generates ids for bus_stops
CREATE SEQUENCE stops_id_seq
    INCREMENT BY 1
    MINVALUE 100
    START WITH 100
;

Create sequence transit_id
    increment by 1
    minvalue 100
    start with 100
;

Create sequence span_id
    increment by 1
    start 1
;

Create sequence reservation_id_seq
    increment by 1
    start 1
;

Create sequence transit_reservation_id_seq
    increment by 1
    start 1
;

-- tables
-- Table: cities
CREATE TABLE cities (
    id NUMERIC  NOT NULL DEFAULT NEXTVAL('cities_id_seq'),
    name varchar(63)  NOT NULL,
    rating int  NOT NULL,
    average_price int  NOT NULL,
    country varchar(63)  NOT NULL,
    CONSTRAINT cities_pk PRIMARY KEY (id),
    constraint rating_range check (rating>=0 and rating<=5),
    constraint minimum_price check (average_price>=0 and average_price <= 10000),
    constraint proper_name check (name similar to '[A-Z][a-z]*((-| |\. )[A-Z][a-z]*)*'),
    constraint proper_country check (country similar to '[A-Z][a-z]*((-| |\. )[A-Z][a-z]*)*')
);

-- Table: bus_stops
CREATE TABLE bus_stops (
    id NUMERIC  NOT NULL DEFAULT  NEXTVAL('stops_id_seq'),
    stop_name varchar(127)  NOT NULL,
    city numeric  NOT NULL,
    CONSTRAINT bus_stops_pk PRIMARY KEY (id),
    CONSTRAINT city_fk FOREIGN KEY (city) REFERENCES cities (id)
    --constraint proper_name check (stop_name similar to '[A-Z][a-z]*((: |-| |\. )[A-Z][a-z]*)*')
);

-- Table: buses_models
CREATE TABLE buses_models (
    id numeric  NOT NULL,
    seats numeric(3)  NOT NULL CHECK (1 <= seats AND seats <= 300),
    CONSTRAINT buses_models_pk PRIMARY KEY (id)
);

-- Table: transits
CREATE TABLE transits (
    id_transit numeric  NOT NULL DEFAULT NEXTVAL('transit_id'),
    departure_stop numeric  NOT NULL,
    arrival_stop numeric  NOT NULL,
    price numeric(4, 2)  NOT NULL CHECK (0 <= price AND price <= 10000),
    bus_model numeric  NOT NULL,
    CONSTRAINT transits_pk PRIMARY KEY (id_transit),
    CONSTRAINT bus_model_fk FOREIGN KEY (bus_model) REFERENCES buses_models (id),
    CONSTRAINT arrival_stop_fk FOREIGN KEY (arrival_stop) REFERENCES bus_stops (id),
    CONSTRAINT departure_stop_fk FOREIGN KEY (departure_stop) REFERENCES bus_stops (id)
);

-- Table: spans
CREATE TABLE spans(
    id int  NOT NULL DEFAULT NEXTVAL('span_id'),
    begin_date date  NOT NULL,
    end_date date  NOT NULL,
    transit int  NOT NULL,
    CONSTRAINT spans_pk PRIMARY KEY (id),
    CONSTRAINT correct_span CHECK ( begin_date <= end_date )
);

-- Table: departure_time
CREATE TABLE departure_time (
    departure time  NOT NULL,
    time interval  NOT NULL,
    span int  NOT NULL,
    day_of_the_week int NOT NULL CHECK ( day_of_the_week BETWEEN 1 and 7),
    CONSTRAINT departure_time_pk PRIMARY KEY (departure, span, day_of_the_week)
);

-- Table: exceptions
CREATE TABLE breaks (
    date date  NOT NULL,
    span_id int  NOT NULL,
    CONSTRAINT break_pk PRIMARY KEY (date, span_id)
);

-- Table: users
CREATE TABLE  users (
    username varchar(50)  NOT NULL,
    email_address email NOT NULL UNIQUE,
    password text NOT NULL,
    name varchar(63)  NOT NULL,
    surname varchar(63)  NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (username),
    constraint unique_email UNIQUE (email_address)
);

-- Table: reservations
CREATE TABLE reservations (
    id int  NOT NULL DEFAULT NEXTVAL('reservation_id_seq'),
    "user" varchar(50)  NOT NULL,
    date_reservation timestamp  NOT NULL,
    CONSTRAINT reservations_pk PRIMARY KEY (id)
);

-- Table: transit_reservation
CREATE TABLE transit_reservation (
    id int  NOT NULL DEFAULT NEXTVAL('transit_reservation_id_seq'),
    transit int  NOT NULL,
    departure_date timestamp  NOT NULL,
    reservation int  NOT NULL,
    CONSTRAINT transit_reservation_pk PRIMARY KEY (id)
);

-- Table: seat_reservation
-- Table: seat_reservation
CREATE TABLE seat_reservation (
    seat int  NOT NULL CHECK (1 <= seat AND seat <= 300),
    transit_reservation_id int  NOT NULL,
    CONSTRAINT seat_reservation_pk PRIMARY KEY (seat,transit_reservation_id)
);

-- tough view to write, left for later
-- -- views
-- -- View: buses
-- CREATE VIEW buses AS
-- select res.id_transit, res.start_city, res.end_city, res.price, res.departure, res.arrival
-- from buses_reservation res;

-- View: countries
CREATE VIEW countries AS
select country as name from cities group by country;

-- View: lines
CREATE VIEW lines AS
select departure_stop, arrival_stop from transits group by departure_stop, arrival_stop;

-- View: new_users
-- empty view used for adding new users with not hashed password
-- author Łukasz Selwa
CREATE VIEW new_users AS
SELECT
       varchar(50) 'new username' as username,
       email 'john.smith@example.com' as email_address,
       varchar(50) 'not hashed password' as password,
       varchar(63) 'John' as name,
       varchar(63) 'Smith' as surname;

-- Rule: new_users
-- adds user to users table and hashes his password
-- author Łukasz Selwa
CREATE RULE create_new_user AS ON INSERT TO new_users
    DO INSTEAD
    INSERT INTO users(username, email_address, password, name, surname)
    VALUES(new.username, new.email_address, ENCODE(DIGEST(new.password, 'sha1'), 'hex'), new.name, new.surname);

-- foreign keys
-- Reference: departure_time_spans (table: departure_time)
ALTER TABLE departure_time ADD CONSTRAINT departure_time_spans
    FOREIGN KEY (span)
    REFERENCES spans (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: breaks_spans (table: exceptions)
ALTER TABLE breaks ADD CONSTRAINT breaks_spans
    FOREIGN KEY (span_id)
    REFERENCES spans (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: spans_transits (table: spans)
ALTER TABLE spans ADD CONSTRAINT spans_transits
    FOREIGN KEY (transit)
    REFERENCES transits (id_transit)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: transit_reservations (table: transit_reservation)
ALTER TABLE transit_reservation ADD CONSTRAINT transit_reservations
    FOREIGN KEY (reservation)
    REFERENCES reservations (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: transit_reservation_ref (table: transit_reservation)
ALTER TABLE transit_reservation ADD CONSTRAINT transit_reservation_ref
    FOREIGN KEY (transit)
    REFERENCES transits (id_transit)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: reservations_users (table: reservations)
ALTER TABLE reservations ADD CONSTRAINT reservations_users
    FOREIGN KEY ("user")
    REFERENCES users (username)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;

-- Reference: seat_transit_reservation (table: seat_reservation)
ALTER TABLE seat_reservation ADD CONSTRAINT seat_transit_reservation
    FOREIGN KEY (transit_reservation_id)
    REFERENCES transit_reservation (id)
    NOT DEFERRABLE
    INITIALLY IMMEDIATE
;



-- Trigger: spans
-- after modifying or deleting spans remove all breaks that will not be contained
CREATE OR REPLACE FUNCTION remove_breaks_after_delete() RETURNS TRIGGER AS
    $remove_breaks$
    begin
        DELETE FROM breaks WHERE spans_id = OLD.id AND date BETWEEN OLD.begin_date AND OLD.end_date;
    end;
    $remove_breaks$ LANGUAGE plpgsql;
CREATE TRIGGER remove_breaks_after_delete AFTER DELETE ON spans
    FOR EACH ROW EXECUTE PROCEDURE remove_breaks_after_delete();

CREATE OR REPLACE FUNCTION remove_breaks_after_update() RETURNS TRIGGER AS
    $remove_breaks_update$
    begin
        DELETE FROM breaks WHERE span_id = NEW.id AND date NOT BETWEEN NEW.begin_date AND NEW.end_date;
    end;
    $remove_breaks_update$ LANGUAGE plpgsql;
CREATE TRIGGER remove_breaks_after_update AFTER UPDATE ON spans
    FOR EACH ROW EXECUTE PROCEDURE remove_breaks_after_update();

-- before inserting span check if it does not overlap other span with the same transit_id
CREATE OR REPLACE FUNCTION check_span_overlap() RETURNS TRIGGER AS
    $check_span_overlap$
    declare
        rec record;
    begin
        for rec in select id ,begin_date, end_date from spans where transit = new.transit loop
            if GREATEST(rec.begin_date, new.begin_date) <= LEAST(rec.end_date, new.end_date) then
                raise notice 'Value: %, %,    %, %', new.begin_date, new.end_date, rec.begin_date, rec.end_date;
                raise exception 'new span overlaps with other span';
            end if;
        end loop;
        return new;
    end;
    $check_span_overlap$ LANGUAGE plpgsql;
CREATE TRIGGER check_span_overlap BEFORE INSERT ON spans
    FOR EACH ROW EXECUTE PROCEDURE check_span_overlap();

-- Trigger: breaks
-- returns null if break is not contained in indicated span
CREATE OR REPLACE FUNCTION break_check() RETURNS TRIGGER AS
    $break_check$
    declare
        rec record;
    begin
        if NEW.date is null or new.span_id is null then
            return null;
        end if;
        for rec in SELECT begin_date, end_date from spans WHERE id = NEW.span_id loop
            if new.date between begin_date and end_date then
                return new;
            end if;
        end loop;
        return null;
    end;
    $break_check$ LANGUAGE plpgsql;
CREATE TRIGGER break_check BEFORE INSERT OR UPDATE ON breaks
    FOR EACH ROW EXECUTE PROCEDURE break_check();

-- triggers

-- trigger on seat_reservation checks if seat has not been taken already
-- Old version didn't work (I think Denis was the author), I wrote it from scratch
-- @author Łukasz Selwa
CREATE OR REPLACE FUNCTION seat_reservation_departure_date_check() RETURNS trigger AS $seat_reservation_departure_date_check$
DECLARE
    my_transit int;
    my_departure_date timestamp;
BEGIN
    if new.transit_reservation_id is null or new.seat is null then
        raise exception 'incorrect seat data';
    end if;
    my_transit = (select max(tr.transit) from transit_reservation tr where tr.id = new.transit_reservation_id);
    my_departure_date = (select max(tr.departure_date) from transit_reservation tr where tr.id = new.transit_reservation_id);
    if (
        select count(*)
        from seat_reservation st join transit_reservation res on st.transit_reservation_id = res.id
        where res.transit = my_transit and res.departure_date = my_departure_date and st.seat = new.seat
        ) <> 0 then
        raise exception 'seat % is already taken in transit % on % (transit_reservation: %)', new.seat, my_transit, my_departure_date, new.transit_reservation_id;
    end if;
    return NEW;
END;
$seat_reservation_departure_date_check$ LANGUAGE plpgsql;

CREATE TRIGGER seat_reservation_departure_date_check BEFORE INSERT OR UPDATE ON seat_reservation
    FOR EACH ROW EXECUTE PROCEDURE seat_reservation_departure_date_check();


-- trigger on transit_reservation checks if such transit exists (the is a bus in database which leaves on given time)
CREATE OR REPLACE FUNCTION transit_reservation_check() RETURNS TRIGGER AS
    $trasit_reservation_check$
    begin
        if new.transit is null or new.departure_date is null then
            raise exception 'null values in transit_reservation';
        end if;

        if (
            select count(*)
            from departure_time dt join spans s on dt.span = s.id
            where
                s.transit = new.transit
                and new.departure_date between s.begin_date and (s.end_date + '1 day'::interval)
                and dt.day_of_the_week = extract(isodow from new.departure_date)
                and dt.departure = cast(new.departure_date as time)
            ) = 0 then
            raise exception 'There is no transit % departing on %', new.transit, new.departure_date;
        end if;

        return new;
    end;
    $trasit_reservation_check$ LANGUAGE plpgsql;
CREATE TRIGGER transit_reservation_check BEFORE INSERT OR UPDATE ON transit_reservation
    FOR EACH ROW EXECUTE PROCEDURE transit_reservation_check();

/*
-- After minor changes in database structure this trigger has not work correctly anymore and by 'not correctly' I mean at all.
-- Fixed it if you have time.
-- Sincerely,
-- Łukasz

--trigger on reservations checks [date_reservation before all buses]
CREATE OR REPLACE FUNCTION date_reservation_check() RETURNS trigger AS $date_reservation_check$
BEGIN
    IF (SELECT min(departure_date) FROM seat_reservation WHERE transit = NEW.id) < NEW.date_reservation THEN
        RAISE EXCEPTION 'some departure_date in reservation is before date_reservation';
    END IF;
    return NEW;
END;
$date_reservation_check$ LANGUAGE plpgsql;


DROP TRIGGER IF EXISTS date_reservation_check ON reservations;
CREATE TRIGGER date_reservation_check BEFORE INSERT OR UPDATE ON reservations
    FOR EACH ROW EXECUTE PROCEDURE date_reservation_check();
*/

/*
-- This trigger does not work because it uses relation 'buses' which does not exist.
-- We agreed not to include view 'buses' in final version, please write it from scratch without this relation.
-- Sincerely,
-- Łukasz

--trigger on seat_reservation checks that number of reserved seats < all seats when somebody try make bus reservation
CREATE OR REPLACE FUNCTION have_free_seat_check() RETURNS trigger AS $have_free_seat_check$
BEGIN
    IF (SELECT min(seats) FROM buses b JOIN transits t ON t.id_transit = b.id_transit JOIN buses_models bm ON t.bus_model = bm.id WHERE NEW.depatrure_date = b.departure AND NEW.transit = b.id_transit)
        = (SELECT count(*) FROM seat_reservation WHERE NEW.transit = transit AND NEW.departure_date = departure_date)
    THEN
        RAISE EXCEPTION 'No free places in bus';
    END IF;
    return NEW;
END;
$have_free_seat_check$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS have_free_seat_check ON seat_reservation;
CREATE TRIGGER have_free_seat_check BEFORE INSERT OR UPDATE ON seat_reservation
    FOR EACH ROW EXECUTE PROCEDURE have_free_seat_check();
*/

create or replace function add_bus(dep_stop int, arr_stop int, price int, bus_model int,bg_dt date, ed_dt date, dep time, leg interval, weekday int) returns numeric as
$$
begin
if ((select count(*)from bus_stops where dep_stop=id)=0 or (select count(*)from bus_stops where arr_stop=id)=0 or price<=0
        or (select count(*) from buses_models where id=bus_model)=0 or bg_dt>ed_dt
        or weekday not between 1 and 7)
then
raise exception 'Incorrect input';
end if;
insert into transits values (nextval('transit_id'),dep_stop,arr_stop,price,bus_model);
insert into intervals values (nextval('span_id'),bg_dt,ed_dt,currval('transit_id'));
insert into departure_time values (dep,leg,currval('span_id'),weekday);
return 1;
end;
$$
language plpgsql;

-- function buses_in_span returns all buses in span span_id and dates in interval L..R
-- @author Denis Pivovarov

-- fixed bug, @Łukasz Selwa
create or replace function  buses_in_span(span_id numeric, begin_date date, end_date date)
    returns TABLE(span numeric, departure timestamp, arrival timestamp) as
$$
declare
    now date;
    r record;
    L_date date;
    R_date date;
begin
    -- author @Łukasz
    L_date = GREATEST(begin_date, (select sp.begin_date from spans sp where sp.id = span_id));
    R_date = LEAST(end_date, (select sp.end_date from spans sp where sp.id = span_id));
    --
    FOR r IN
        SELECT * FROM departure_time WHERE departure_time.span = span_id
        LOOP
            now = L_date;
            LOOP
                EXIT  WHEN  r.day_of_the_week = FLOOR(extract(isodow from now));
                now = now + '1 day' :: interval;
            END LOOP;
            LOOP
                EXIT WHEN now > R_date;
                IF (SELECT count(*) FROM breaks WHERE span = breaks.span_id AND date = now) > 0 THEN
                    now = now + '7 days' :: interval;
                    CONTINUE;
                end if;
                span = span_id;
                departure = now + r.departure;
                arrival = departure + r.time;
                now = now + '7 days' :: interval;
                RETURN NEXT;
            END LOOP;

        END LOOP;
end;
$$
language plpgsql;

-- function get_buses returns all buses in dates in interval L..R
-- @author Denis Pivovarov

create or replace function  get_buses(L date, R date)
    returns TABLE(id_transit numeric, start_city numeric, end_city numeric, price numeric(6, 2), departure timestamp, arrival timestamp) as
$$
begin
    return QUERY
        SELECT tr.id_transit, bsd.city, bsa.city, tr.price, bii.departure, bii.arrival
        FROM transits tr
            JOIN bus_stops bsd ON tr.departure_stop = bsd.id
            JOIN bus_stops bsa ON tr.arrival_stop = bsa.id
            JOIN spans sp ON tr.id_transit = sp.transit
            JOIN buses_in_span(sp.id, L, R) bii ON sp.id = bii.span
    ;
end;
$$
language plpgsql;



-- End of file.

