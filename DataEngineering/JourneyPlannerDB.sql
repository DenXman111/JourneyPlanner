-- noinspection SqlNoDataSourceInspectionForFile

-- noinspection SqlResolveForFile

-- email used in users table
-- email type, copied from stack overflow: "https://dba.stackexchange.com/questions/68266/what-is-the-best-way-to-store-an-email-address-in-postgresql"
CREATE EXTENSION citext;
CREATE DOMAIN email AS citext
  CHECK ( value ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$' );

-- day enum for departure_time
CREATE TYPE day as ENUM ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday');

-- reservation_type for reservation function

CREATE TYPE reservation_type AS (
    transit numeric,
    departure timestamp
);

-- casting day to int
CREATE OR REPLACE FUNCTION day_to_int(day) returns int as
$$
begin
    case
        when $1 = 'Monday'::day then return 1;
        when $1 = 'Tuesday'::day then return 2;
        when $1 = 'Wednesday'::day then return 3;
        when $1 = 'Thursday'::day then return 4;
        when $1 = 'Friday'::day then return 5;
        when $1 = 'Saturday'::day then return 6;
        else return 7; -- Sunday
    end case;
end;
$$ LANGUAGE plpgsql;
CREATE CAST ( day as integer ) with function day_to_int(day);

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
    rating numeric(3, 2)  NOT NULL,
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
    CONSTRAINT city_fk FOREIGN KEY (city) REFERENCES cities (id),
    constraint proper_name check (stop_name similar to '[A-Z][a-zA-z]*((: |-| |\. )[a-zA-Z][a-zA-Z]*)*')
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
    price numeric(6, 2)  NOT NULL CHECK (0 <= price AND price <= 10000),
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
    transit numeric  NOT NULL,
    CONSTRAINT spans_pk PRIMARY KEY (id),
    CONSTRAINT correct_span CHECK ( begin_date <= end_date )
);

-- Table: departure_time
CREATE TABLE departure_time (
    departure time  NOT NULL,
    time interval  NOT NULL,
    span int  NOT NULL,
    day_of_the_week day NOT NULL,
    CONSTRAINT departure_time_pk PRIMARY KEY (departure, span, day_of_the_week)
);

-- Table: breaks
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
    transit numeric  NOT NULL,
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

--Views
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
-- @author Łukasz Selwa
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
        return new;
    end;
    $remove_breaks_update$ LANGUAGE plpgsql;
CREATE TRIGGER remove_breaks_after_update AFTER UPDATE ON spans
    FOR EACH ROW EXECUTE PROCEDURE remove_breaks_after_update();

-- Trigger: departure_time

-- function returns number of given day of the week in the interval
CREATE OR REPLACE FUNCTION days_in_span(begin_date date, end_date date, weekday day) RETURNS integer as
    $$
    begin
        if begin_date > end_date then
            return 0;
        end if;
        return (
            select count(*)
            from (
                select begin_date + times * '1 day'::interval as d
                from generate_series(0, end_date - begin_date) times
                ) foo
            where extract(isodow from foo.d) = weekday::integer);
    end;
    $$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION count_breaks(begin_date date, end_date date, weekday day, span_id numeric) RETURNS INTEGER AS
    $$
    begin
        return (
            select count(*)
            from breaks br
            where br.span_id = span_id
              and extract(isodow from br.date) = weekday::integer
              and br.date between begin_date and end_date
            );
    end;
    $$ LANGUAGE plpgsql;

-- before insert or update check if there is no other bus leaving on the same time
CREATE OR REPLACE FUNCTION departure_time_check() RETURNS TRIGGER AS
    $departure_time_check$
    declare
        tr integer;
        span_begin date;
        span_end date;
        rec record;
    begin
        tr = (select sp.transit from spans sp where sp.id = new.span);
        span_begin = (select sp.begin_date from spans sp where sp.id = new.span);
        span_end = (select sp.end_date from spans sp where sp.id = new.span);

        for rec in select *
        from departure_time dt join spans sp on dt.span = sp.id
        where sp.transit = tr and dt.day_of_the_week = new.day_of_the_week and dt.departure = new.departure and
              GREATEST(sp.begin_date, span_begin) <= LEAST(sp.end_date, span_end) loop
            if days_in_span(GREATEST(rec.begin_date, span_begin), LEAST(rec.end_date, span_end), new.day_of_the_week)
                   - count_breaks(GREATEST(rec.begin_date, span_begin), LEAST(rec.end_date, span_end), new.day_of_the_week, rec.span)
                   > 0 then
                raise exception 'bus % already leaves at % (span %)', tr, new.departure, rec.span;
            end if;
        end loop;
        return new;
    end;
    $departure_time_check$ LANGUAGE plpgsql;
CREATE TRIGGER departure_time_check BEFORE INSERT OR UPDATE ON departure_time
    FOR EACH ROW EXECUTE PROCEDURE departure_time_check();

-- Trigger: breaks
-- returns null if given break is not contained in the indicated span
-- @author Łukasz Selwa
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


CREATE OR REPLACE FUNCTION reservations_delete() RETURNS TRIGGER AS
    $reservations_delete$
    begin
        delete from transit_reservation where reservation = old.id;
        return old;
    end;
    $reservations_delete$ LANGUAGE plpgsql;
CREATE TRIGGER reservations_delete BEFORE DELETE ON reservations
    FOR EACH ROW EXECUTE PROCEDURE reservations_delete();


CREATE OR REPLACE function transit_reservation_delete() RETURNS TRIGGER AS
    $transit_reservation_delete$
    begin
        delete from seat_reservation st where st.transit_reservation_id = old.id;
        return old;
    end;
    $transit_reservation_delete$ LANGUAGE plpgsql;
CREATE TRIGGER transit_reservation_delete BEFORE DELETE ON transit_reservation
    FOR EACH ROW EXECUTE PROCEDURE transit_reservation_delete();

-- trigger on transit_reservation checks if given transit exists (there is a bus in database which leaves on given time)
-- @author Łukasz Selwa + Denis Pivovarov
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
                and new.departure_date::date between s.begin_date and s.end_date
                and dt.day_of_the_week::integer = extract(isodow from new.departure_date)
                and dt.departure = cast(new.departure_date as time)
                and (select count(*) from breaks br where br.span_id = s.id and br.date = new.departure_date::date) = 0
            ) = 0 then
            raise exception 'There is no transit % departing on %', new.transit, new.departure_date;
        end if;

        return new;
    end;
    $trasit_reservation_check$ LANGUAGE plpgsql;
CREATE TRIGGER transit_reservation_check BEFORE INSERT OR UPDATE ON transit_reservation
    FOR EACH ROW EXECUTE PROCEDURE transit_reservation_check();

--trigger on reservations checks [date_reservation before all buses]
-- @author Denis Pivovarov

--

/*CREATE OR REPLACE FUNCTION date_reservation_check() RETURNS trigger AS $date_reservation_check$
BEGIN
    IF (SELECT min(departure_date) FROM transit_reservation WHERE reservation = NEW.id) < NEW.date_reservation THEN
        RAISE EXCEPTION 'some departure_date in reservation is before date_reservation';
    END IF;
    return NEW;
END;
$date_reservation_check$ LANGUAGE plpgsql;


CREATE TRIGGER date_reservation_check BEFORE INSERT OR UPDATE ON reservations
    FOR EACH ROW EXECUTE PROCEDURE date_reservation_check();*/


--trigger on seat_reservation that checks if seat number < all seats when somebody tries to make a bus reservation
-- @author Denis Pivovarov
CREATE OR REPLACE FUNCTION have_free_seat_check() RETURNS trigger AS $have_free_seat_check$
BEGIN
    IF (SELECT bm.seats FROM transit_reservation tr JOIN transits t ON t.id_transit = tr.transit JOIN buses_models bm ON bm.id = t.bus_model WHERE NEW.transit_reservation_id = tr.id)
        <= (NEW.seat)
    THEN
        RAISE EXCEPTION 'No free places in bus';
    END IF;
    return NEW;
END;
$have_free_seat_check$ LANGUAGE plpgsql;

CREATE TRIGGER have_free_seat_check BEFORE INSERT OR UPDATE ON seat_reservation
    FOR EACH ROW EXECUTE PROCEDURE have_free_seat_check();


create or replace function add_bus(dep_stop int, arr_stop int, price int, bus_model int,bg_dt date, ed_dt date, dep time, leg interval, weekday day) returns numeric as
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

-- function buses_in_span returns all buses in span span_id and dates in interval
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
    -- author @Łukasz {
    L_date = GREATEST(begin_date, (select sp.begin_date from spans sp where sp.id = span_id));
    R_date = LEAST(end_date, (select sp.end_date from spans sp where sp.id = span_id));
    -- }
    FOR r IN
        SELECT * FROM departure_time WHERE departure_time.span = span_id
        LOOP
            now = L_date;
            LOOP
                EXIT  WHEN  r.day_of_the_week::integer = FLOOR(extract(isodow from now));
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
        WHERE  GREATEST(L, sp.begin_date) <= LEAST(R, sp.end_date)
    ;
end;
$$
language plpgsql;


create or replace function buses_from_city( city_id numeric, L date, R date)
returns TABLE(id_transit numeric, end_city numeric, price numeric(6, 2), departure timestamp, arrival timestamp) as
    $$
    begin
        return QUERY
        SELECT tr.id_transit, bsa.city, tr.price, bii.departure, bii.arrival
        FROM transits tr
            JOIN bus_stops bsd ON tr.departure_stop = bsd.id
            JOIN bus_stops bsa ON tr.arrival_stop = bsa.id
            JOIN spans sp ON tr.id_transit = sp.transit
            join cities c on bsa.city = c.id
            JOIN buses_in_span(sp.id, L, R) bii ON sp.id = bii.span
        WHERE bsd.city = city_id and GREATEST(L, sp.begin_date) <= LEAST(R, sp.end_date)
        ORDER BY tr.price desc limit 10
    ;
    end;
    $$
language plpgsql;

create or replace function optional_visits(start_city numeric, start_time timestamp, end_city numeric, end_time timestamp)
returns TABLE(id_tr1 numeric, tr1_str_city numeric, tr1_end_city numeric, tr1_price numeric(6, 2), tr1_departure timestamp, tr1_arrival timestamp,
              id_tr2 numeric, tr2_str_city numeric, tr2_end_city numeric, tr2_price numeric(6, 2), tr2_departure timestamp, tr2_arrival timestamp)
as
    $$
    begin
        return query
        select tr1.id_transit, tr1_bsd.city, tr1_bsa.city, tr1.price, bii1.departure, bii1.arrival,
               tr2.id_transit, tr2_bsd.city, tr2_bsa.city, tr2.price, bii2.departure, bii2.arrival
        from transits tr1
            join bus_stops tr1_bsd on tr1.departure_stop = tr1_bsd.id
            join bus_stops tr1_bsa on tr1.arrival_stop = tr1_bsd.id
            join bus_stops tr2_bsd on tr2_bsd.city = tr1_bsa.city
            join transits tr2 on tr2.departure_stop = tr2_bsd.id
            join bus_stops tr2_bsa on tr2_bsa.id = tr2.arrival_stop
            join spans sp1 on sp1.transit = tr1.id_transit
            join spans sp2 on sp2.transit = tr2.id_transit
            join buses_in_span(sp1.id, start_time::date, end_time::date) bii1 ON bii1.span = sp1.id
            join buses_in_span(sp2.id, start_time::date, end_time::date) bii2 ON bii2.span = sp2.id
        where tr1_bsd.city = start_city
            and tr2_bsa.city = end_city
            and GREATEST(start_time::date, sp1.begin_date) <= LEAST(end_time::date, sp1.end_date)
            and GREATEST(start_time::date, sp2.begin_date) <= LEAST(end_time::date, sp2.end_date)
            and bii1.arrival <= bii2.departure
            and start_time <= bii1.departure
            and bii2.arrival <= end_time
        ;
    end;
    $$
language plpgsql;

create table moderators(
    username varchar(50) references users(username)
);

INSERT INTO moderators(username) values ('admin1');


create or replace function loginUser(given_username varchar(50), user_password varchar(50)) returns boolean as
    $$
    begin
        return (select count(*) from users u where u.username = given_username and u.password = ENCODE(DIGEST(user_password, 'sha1'), 'hex')) <> 0;
    end;
    $$language plpgsql;

create or replace function loginModerator(given_username varchar(50), user_password varchar(50)) returns boolean as
    $$
    begin
        return loginUser(given_username, user_password) and (select count(*) from moderators where username = given_username) <> 0;
    end;
    $$ language plpgsql;


create or replace function enough_seats(transit_id integer, departure timestamp, seats integer) returns boolean as
    $$
    begin
        return
            -- capacity
            (
                select bm.seats
                from transits t join buses_models bm on t.bus_model = bm.id
                where t.id_transit = transit_id limit 1
            )
            >=
            -- reserved seats
            (
                select count(*)
                from seat_reservation st join transit_reservation tr on st.transit_reservation_id = tr.id
                where tr.transit = transit_id and tr.departure_date = departure
            )
            + seats
            ;

    end;
    $$ language plpgsql;


-- function returns first unreserved seats in given bus or throws exception if there are not enough seats left
create or replace function first_free_seats(transit_id integer, departure timestamp, seats integer)
returns table(seat_number integer) as
    $$
    declare
        capacity integer;
        reserved_seats integer;
    begin
        capacity =
            (
                select bm.seats
                from transits t join buses_models bm on t.bus_model = bm.id
                where t.id_transit = transit_id
            );
        reserved_seats =
            (
                select count(*)
                from seat_reservation st join transit_reservation tr on st.transit_reservation_id = tr.id
                where tr.transit = transit_id and tr.departure_date = departure
            );
        if capacity < seats + reserved_seats then
            raise exception 'no enough seats left';
        end if;
        return query
            with reserved as (
                select st.seat
                from seat_reservation st join transit_reservation tr on st.transit_reservation_id = tr.id
                where tr.transit = transit_id and tr.departure_date = departure
                )
            select * from generate_series(1, capacity, 1) num where num not in (select * from reserved) limit seats;
    end;
    $$ language plpgsql;


create or replace function get_buses_with_seats_left(L date, R date, seats integer)
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
        WHERE  GREATEST(L, sp.begin_date) <= LEAST(R, sp.end_date)
            and enough_seats(tr.id_transit::integer, bii.departure, seats)
    ;
end;
$$
language plpgsql;

select *
from get_buses('2019-06-01'::date, '2019-06-07'::date);



create or replace function reserve(user_id varchar(30), seats integer, res variadic reservation_type array) returns
    table (transit_id integer, departure_time timestamp, departure_stop varchar(127), arrival_stop varchar(127), reserved_seats integer array )as
    $$
    declare
        my_reservation numeric = null;
        my_transit_id numeric = null;
        my_seat integer;
        trans record;
    begin
        my_reservation = NEXTVAL('reservation_id_seq');
        insert into reservations(id, "user", date_reservation) VALUES (my_reservation, user_id, current_timestamp);
        for trans in select * from unnest(res) loop
            my_transit_id =  NEXTVAL('transit_reservation_id_seq');
            insert into transit_reservation(id, transit, departure_date, reservation)
            VALUES(my_transit_id, trans.transit, trans.departure, my_reservation);
            transit_id = trans.transit;
            departure_time = trans.departure;
            departure_stop = (select bs.stop_name from transits t join bus_stops bs on t.departure_stop = bs.id where t.id_transit = trans.transit);
            arrival_stop = (select bs.stop_name from transits t join bus_stops bs on t.arrival_stop = bs.id where t.id_transit = trans.transit);
            reserved_seats = array(select seat_number from first_free_seats(trans.transit::integer, trans.departure, seats));

            for my_seat in select * from unnest(reserved_seats) rs loop

                insert into seat_reservation(seat, transit_reservation_id) VALUES (my_seat, my_transit_id);

            end loop;

            return next;

        end loop;
    exception
        when others then
            if my_reservation is not null then
                delete from reservations where id = my_reservation;
            end if;
            raise exception 'reservation fail';
    end;
    $$ language plpgsql;


create or replace function user_reservations(given_username varchar(50))
returns table(
    reservation_id integer, reservation_date timestamp, seat_number integer, transit_id numeric, transit_price numeric(6,2),
    departure timestamp, departure_stop varchar(127), departure_city numeric,
    arrival timestamp, arrival_stop varchar(127), arrival_city numeric
    ) as
    $$
    begin
        return query
            select r.id, r.date_reservation, sr.seat, t.id_transit, t.price,
                   tres.departure_date, dbs.stop_name, dbs.city,
                   tres.departure_date::timestamp + dt.time::interval, abs.stop_name, abs.city
            from seat_reservation sr
                join transit_reservation tres on sr.transit_reservation_id = tres.id
                join reservations r on r.id = tres.reservation
                join transits t on tres.transit = t.id_transit
                join spans s on s.transit = t.id_transit
                join departure_time dt on dt.span = s.id and extract(isodow from tres.departure_date) = dt.day_of_the_week::integer
                join bus_stops dbs on t.departure_stop = dbs.id
                join bus_stops abs on t.arrival_stop = abs.id
            where r."user" = given_username
                and tres.departure_date::time = dt.departure
                and tres.departure_date::date not in (select b.date from breaks b where b.span_id = s.id)
            order by r.date_reservation, tres.departure_date
            ;
    end;
    $$ language plpgsql;

create or replace function reserved_seats(reservation_id integer) returns integer as
    $$
    declare
        trans_res integer;
    begin
        trans_res = (select tr.id from transit_reservation tr where tr.reservation = reservation_id limit 1);
        return (select count(*) from seat_reservation sr where sr.transit_reservation_id = trans_res);
    end;
    $$ language plpgsql;

-- End of file

