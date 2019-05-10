-- noinspection SqlResolveForFile

-- types
-- day used in departure_time table
CREATE TYPE day AS ENUM ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday', 'Everyday');

-- email used in users table
-- email type, copied from stack overflow: "https://dba.stackexchange.com/questions/68266/what-is-the-best-way-to-store-an-email-address-in-postgresql"
CREATE EXTENSION citext;
CREATE DOMAIN email AS citext
  CHECK ( value ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$' );


-- tables
-- Table: cities
CREATE TABLE cities (
    id NUMERIC  NOT NULL,
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
    id NUMERIC  NOT NULL,
    stop_name varchar(127)  NOT NULL,
    city numeric  NOT NULL,
    CONSTRAINT bus_stops_pk PRIMARY KEY (id),
    CONSTRAINT city_fk FOREIGN KEY (city) REFERENCES cities (id),
    constraint proper_name check (stop_name similar to '[A-Z][a-z]*((: |-| |\. )[A-Z][a-z]*)*')
);

-- Table: buses_models
CREATE TABLE buses_models (
    id numeric  NOT NULL,
    seats numeric(3)  NOT NULL CHECK (1 <= seats AND seats <= 300),
    CONSTRAINT buses_models_pk PRIMARY KEY (id)
);

-- Table: transits
CREATE TABLE transits (
    id_transit numeric  NOT NULL,
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
CREATE TABLE spans (
    id int  NOT NULL,
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
    day_of_the_week day  NOT NULL,
    CONSTRAINT departure_time_pk PRIMARY KEY (departure, span)
);

-- Table: exceptions
CREATE TABLE breaks (
    date date  NOT NULL,
    span_id int  NOT NULL,
    CONSTRAINT break_pk PRIMARY KEY (date, span_id)
);

-- Table: users
CREATE TABLE  users (
    username varchar(20)  NOT NULL,
    email email NOT NULL UNIQUE,
    password bigint  NOT NULL,
    name varchar(63)  NOT NULL,
    surname varchar(63)  NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (username),
    constraint unique_email UNIQUE (email)
);

-- Table: reservations
CREATE TABLE reservations (
    id int  NOT NULL,
    "user" varchar(20)  NOT NULL,
    date_reservation timestamp  NOT NULL,
    CONSTRAINT reservations_pk PRIMARY KEY (id)
);

-- Table: transit_reservation
CREATE TABLE transit_reservation (
    id int  NOT NULL,
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
            if GREATEST(rec.begin_date, rec.end_date) <= LEAST(rec.end_date, new.end_date) then
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

--triggers
--trigger on seat_reservation checks [have bus with true departure_date]
-- modification, check if seat is not already reserved, @author Łukasz Selwa
CREATE OR REPLACE FUNCTION seat_reservation_departure_date_check() RETURNS trigger AS $seat_reservation_departure_date_check$
DECLARE
    my_transit int;
BEGIN
    my_transit = (select max(transit) from transit_reservation where id = new.transit_reservation_id);
    if (
        select count(*)
        from seat_reservation seat join transit_reservation res on seat.transit_reservation_id = res.id
        where res.transit = my_transit
        ) <> 0 then
        raise exception 'seat is already taken';
    end if;
    IF (SELECT count(*) FROM buses WHERE departure = NEW.departure_date AND id_transit = NEW.transit) = 0 THEN
        RAISE EXCEPTION 'Have not bus in this departure_date';
    END IF;
    return NEW;
END;
$seat_reservation_departure_date_check$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS seat_reservation_departure_date_check ON seat_reservation;
CREATE TRIGGER seat_reservation_departure_date_check BEFORE INSERT OR UPDATE ON seat_reservation
    FOR EACH ROW EXECUTE PROCEDURE seat_reservation_departure_date_check();


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

create or replace function add_bus(dep_stop int, arr_stop int, price int, bus_model int,bg_dt date, ed_dt date, dep time, leg interval, weekday day) returns numeric as
$$
begin
if ((select count(*)from bus_stops where dep_stop=id)=0 or (select count(*)from bus_stops where arr_stop=id)=0 or price<=0 or (select count(*) from buses_models where id=bus_model)=0 or bg_dt>ed_dt)
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

-- sequences
-- Sequence: reservation_id
CREATE SEQUENCE reservation_id
      INCREMENT BY 1
      MINVALUE 1
      MAXVALUE 1000000000
      START WITH 1
      CYCLE
;

Create sequence transit_id
    increment by 1
    start 1
;

Create sequence span_id
    increment by 1
    start 1
;
-- End of file.

