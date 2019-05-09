-- noinspection SqlResolveForFile

-- tables
-- Table: bus_stops
CREATE TABLE bus_stops (
    id int  NOT NULL,
    stop_name varchar(127)  NOT NULL,
    city int  NOT NULL,
    CONSTRAINT bus_stops_pk PRIMARY KEY (id)
);

-- Table: buses_models
CREATE TABLE buses_models (
    id int  NOT NULL,
    seats int  NOT NULL,
    CONSTRAINT buses_models_pk PRIMARY KEY (id)
);

-- Table: cities
CREATE TABLE cities (
    id int  NOT NULL,
    name varchar(63)  NOT NULL CHECK (not null),
    rating int  NOT NULL,
    average_price int  NOT NULL,
    country varchar(63)  NOT NULL,
    CONSTRAINT cities_pk PRIMARY KEY (id)
);

-- Table: departure_time
CREATE TABLE departure_time (
    departure time  NOT NULL,
    time interval  NOT NULL,
    interval int  NOT NULL,
    day_of_the_week int  NULL,
    CONSTRAINT departure_time_pk PRIMARY KEY (departure,interval)
);

-- Table: exceptions
CREATE TABLE breaks (
    date date  NOT NULL,
    interval_id int  NOT NULL,
    CONSTRAINT exceptions_pk PRIMARY KEY (date,interval_id)
);

-- Trigger: breaks
-- returns null if break is not contained in indicated interval
CREATE OR REPLACE FUNCTION exception_check() RETURNS TRIGGER AS
    $exception_check$
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
    $exception_check$ LANGUAGE plpgsql;
CREATE TRIGGER exception_check BEFORE INSERT OR UPDATE ON breaks
    FOR EACH ROW EXECUTE PROCEDURE exception_check();

-- Table: intervals
CREATE TABLE intervals (
    id int  NOT NULL,
    begin_date date  NOT NULL,
    end_date date  NOT NULL,
    transit int  NOT NULL,
    CONSTRAINT intervals_pk PRIMARY KEY (id),
    CONSTRAINT correct_interval CHECK ( begin_date <= end_date )
);

-- Table: reservations
CREATE TABLE reservations (
    id int  NOT NULL,
    "user" varchar(20)  NOT NULL,
    date_reservation timestamp  NOT NULL,
    CONSTRAINT reservations_pk PRIMARY KEY (id)
);

-- Table: seat_reservation
CREATE TABLE seat_reservation (
    seat int  NOT NULL,
    reservation int  NOT NULL,
    transit int  NOT NULL,
    departure_date timestamp  NOT NULL,
    CONSTRAINT seat_reservation_pk PRIMARY KEY (seat,transit)
);

-- Table: transits
CREATE TABLE transits (
    id_transit int  NOT NULL,
    departure_stop int  NOT NULL,
    arrival_stop int  NOT NULL,
    price int  NOT NULL,
    bus_model int  NOT NULL,
    CONSTRAINT transits_pk PRIMARY KEY (id_transit)
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
select res.start_city, res.end_city, res.price, res.departure, res.arrival
from buses_reservation res;

-- View: countries
CREATE VIEW countries AS
select country as name from cities group by country;

-- View: lines
CREATE VIEW lines AS
select departure_stop, arrival_stop from transits group by departure_stop, arrival_stop;

-- foreign keys
-- Reference: buses_reservation_buses_model (table: transits)
ALTER TABLE transits ADD CONSTRAINT buses_reservation_buses_model
    FOREIGN KEY (bus_model)
    REFERENCES buses_models (id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

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

-- Reference: reservations_users (table: reservations)
ALTER TABLE reservations ADD CONSTRAINT reservations_users
    FOREIGN KEY ("user")
    REFERENCES users (username)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: stations_cities (table: bus_stops)
ALTER TABLE bus_stops ADD CONSTRAINT stations_cities
    FOREIGN KEY (city)
    REFERENCES cities (id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: transits_bus_stops (table: transits)
ALTER TABLE transits ADD CONSTRAINT transits_bus_stops
    FOREIGN KEY (arrival_stop)
    REFERENCES bus_stops (id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

-- Reference: transits_departure_stops (table: transits)
ALTER TABLE transits ADD CONSTRAINT transits_departure_stops
    FOREIGN KEY (departure_stop)
    REFERENCES bus_stops (id)  
    NOT DEFERRABLE 
    INITIALLY IMMEDIATE
;

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

