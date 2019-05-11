-- noinspection SqlResolveForFile

-- script used for removing all data created after running file JourneyPlannerDB.sql
DROP TABLE IF EXISTS breaks CASCADE;
DROP TABLE IF EXISTS bus_stops CASCADE;
DROP TABLE IF EXISTS buses_models CASCADE;
DROP TABLE IF EXISTS cities CASCADE;
DROP TABLE IF EXISTS departure_time CASCADE;
DROP TABLE IF EXISTS spans CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS seat_reservation CASCADE;
DROP TABLE IF EXISTS transit_reservation CASCADE;
DROP TABLE IF EXISTS transits CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP VIEW IF EXISTS countries;
DROP VIEW IF EXISTS lines;
DROP VIEW IF EXISTS new_users;

DROP SEQUENCE IF EXISTS reservation_id;
DROP SEQUENCE IF EXISTS cities_id_seq;
drop sequence if exists transit_id;
drop sequence if exists span_id;
DROP SEQUENCE IF EXISTS reservation_id_seq;
DROP SEQUENCE IF EXISTS stops_id_seq;
DROP SEQUENCE IF EXISTS transit_reservation_id_seq;

DROP FUNCTION IF EXISTS break_check();
DROP FUNCTION IF EXISTS date_reservation_check();
DROP FUNCTION IF EXISTS have_free_seat_check();
DROP FUNCTION IF EXISTS remove_breaks_after_delete();
DROP FUNCTION IF EXISTS remove_breaks_after_update();
DROP FUNCTION IF EXISTS seat_reservation_departure_date_check();
DROP FUNCTION IF EXISTS seat_reservation_departure_date_check();
DROP FUNCTION IF EXISTS check_span_overlap();
drop function if exists add_bus(int,int,int,int,date,date,time,interval,day);

DROP TYPE IF EXISTS day;
DROP DOMAIN IF EXISTS email;
DROP EXTENSION IF EXISTS citext;
DROP EXTENSION IF EXISTS pgcrypto;