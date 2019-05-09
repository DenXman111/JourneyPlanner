-- noinspection SqlResolveForFile

-- tables
-- Table: bus_stops
CREATE TABLE bus_stops (
    id numeric  NOT NULL,
    stop_name varchar(127)  NOT NULL,
    city numeric  NOT NULL,
    CONSTRAINT bus_stops_pk PRIMARY KEY (id),
    CONSTRAINT city_fk FOREIGN KEY (city) REFERENCES cities (id)
);

-- Table: buses_models
CREATE TABLE buses_models (
    id numeric  NOT NULL,
    seats numeric(3)  NOT NULL CHECK (1 <= seats AND seats <= 300),
    CONSTRAINT buses_models_pk PRIMARY KEY (id)
);

-- Table: cities
CREATE TABLE cities (
    id int  NOT NULL,
    name varchar(63)  NOT NULL,
    rating int  NOT NULL,
    average_price int  NOT NULL,
    country varchar(63)  NOT NULL,
    CONSTRAINT cities_pk PRIMARY KEY (id)
);

CREATE TYPE day AS ENUM ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday', 'Everyday');

-- Table: departure_time
CREATE TABLE departure_time (
    departure time  NOT NULL,
    time interval  NOT NULL,
    interval int  NOT NULL,
    day_of_the_week day  NOT NULL,
    CONSTRAINT departure_time_pk PRIMARY KEY (departure,interval)
);

-- Table: exceptions
CREATE TABLE breaks (
    date date  NOT NULL,
    interval_id int  NOT NULL,
    CONSTRAINT break_pk PRIMARY KEY (date,interval_id)
);

-- Trigger: breaks
-- returns null if break is not contained in indicated interval
CREATE OR REPLACE FUNCTION break_check() RETURNS TRIGGER AS
    $break_check$
    declare
        rec record;
    begin
        if NEW.date is null or new.interval_id is null then
            return null;
        end if;
        for rec in SELECT begin_date, end_date from intervals WHERE id = NEW.interval_id loop
            if new.date between begin_date and end_date then
                return new;
            end if;
        end loop;
        return null;
    end;
    $break_check$ LANGUAGE plpgsql;
CREATE TRIGGER break_check BEFORE INSERT OR UPDATE ON breaks
    FOR EACH ROW EXECUTE PROCEDURE break_check();

-- Table: intervals
CREATE TABLE intervals (
    id int  NOT NULL,
    begin_date date  NOT NULL,
    end_date date  NOT NULL,
    transit int  NOT NULL,
    CONSTRAINT intervals_pk PRIMARY KEY (id),
    CONSTRAINT correct_interval CHECK ( begin_date <= end_date )
);

-- Trigger: intervals
-- after modifying or deleting interval remove all breaks that will not be contained
CREATE OR REPLACE FUNCTION remove_breaks_after_delete() RETURNS TRIGGER AS
    $remove_breaks$
    begin
        DELETE FROM breaks WHERE interval_id = OLD.id AND date BETWEEN OLD.begin_date AND OLD.end_date;
    end;
    $remove_breaks$ LANGUAGE plpgsql;
CREATE TRIGGER remove_breaks_after_delete AFTER DELETE ON intervals
    FOR EACH ROW EXECUTE PROCEDURE remove_breaks_after_delete();

CREATE OR REPLACE FUNCTION remove_breaks_after_update() RETURNS TRIGGER AS
    $remove_breaks_update$
    begin
        DELETE FROM breaks WHERE interval_id = NEW.id AND date NOT BETWEEN NEW.begin_date AND NEW.end_date;
    end;
    $remove_breaks_update$ LANGUAGE plpgsql;
CREATE TRIGGER remove_breaks_after_update AFTER UPDATE ON intervals
    FOR EACH ROW EXECUTE PROCEDURE remove_breaks_after_update();

-- Table: reservations
CREATE TABLE reservations (
    id numeric  NOT NULL,
    "user" varchar(20)  NOT NULL,
    date_reservation timestamp  NOT NULL,
    CONSTRAINT reservations_pk PRIMARY KEY (id),
    CONSTRAINT user_fk FOREIGN KEY ("user") REFERENCES users (username)

);

-- Table: seat_reservation
CREATE TABLE seat_reservation (
    seat numeric(3)  NOT NULL CHECK (1 <= seats AND seats <= 300),
    reservation numeric  NOT NULL,
    transit numeric  NOT NULL,
    departure_date timestamp  NOT NULL,
    CONSTRAINT seat_reservation_pk PRIMARY KEY (seat,transit)
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

-- email type, copied from stack overflow: "https://dba.stackexchange.com/questions/68266/what-is-the-best-way-to-store-an-email-address-in-postgresql"
CREATE EXTENSION citext;
CREATE DOMAIN email AS citext
  CHECK ( value ~ '^[a-zA-Z0-9.!#$%&''*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$' );

-- Table: users
CREATE TABLE  users (
    username varchar(20)  NOT NULL,
    email email NOT NULL,
    password bigint  NOT NULL,
    name varchar(63)  NOT NULL,
    surname varchar(63)  NOT NULL,
    CONSTRAINT users_pk PRIMARY KEY (username),
    constraint unique_email UNIQUE (email)
);

-- views
-- View: buses
CREATE VIEW buses AS
select res.id_transit, res.start_city, res.end_city, res.price, res.departure, res.arrival
from buses_reservation res;

-- View: countries
CREATE VIEW countries AS
select country as name from cities group by country;

-- View: lines
CREATE VIEW lines AS
select departure_stop, arrival_stop from transits group by departure_stop, arrival_stop;

-- foreign keys
-- Reference: departure_time_intervals (table: departure_time)
ALTER TABLE departure_time ADD CONSTRAINT departure_time_intervals
    FOREIGN KEY (interval)
    REFERENCES intervals (id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: exceptions_intervals (table: exceptions)
ALTER TABLE exceptions ADD CONSTRAINT exceptions_intervals
    FOREIGN KEY (intervals_id)
    REFERENCES intervals (id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: intervals_transits (table: intervals)
ALTER TABLE intervals ADD CONSTRAINT intervals_transits
    FOREIGN KEY (transit)
    REFERENCES transits (id_transit)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: reservation_buses_reservation (table: seat_reservation)
ALTER TABLE seat_reservation ADD CONSTRAINT reservation_buses_reservation
    FOREIGN KEY (transit)
    REFERENCES transits (id_transit)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: reservation_users_reservations (table: seat_reservation)
ALTER TABLE seat_reservation ADD CONSTRAINT reservation_users_reservations
    FOREIGN KEY (reservation)
    REFERENCES reservations (id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

--triggers
--trigger on seat_reservation checks [have bus with true departure_date]
CREATE OR REPLACE FUNCTION seat_reservation_departure_date_check() RETURNS trigger AS $seat_reservation_departure_date_check$
BEGIN
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
CREATE TRIGGER have_free_seat_check BEFORE INSERT OR UPDATE ON seat_reservation;
    FOR EACH ROW EXECUTE PROCEDURE have_free_seat_check();

-- sequences
-- Sequence: reservation_id
CREATE SEQUENCE reservation_id
      INCREMENT BY 1
      MINVALUE 1
      MAXVALUE 1000000000
      START WITH 1
      CYCLE
;

-- End of file.

