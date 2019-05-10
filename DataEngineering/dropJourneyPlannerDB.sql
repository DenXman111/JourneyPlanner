-- script used for removing all data created after running file JourneyPlannerDB.sql
DROP TABLE IF EXISTS breaks CASCADE;
DROP TABLE IF EXISTS bus_stops CASCADE;
DROP TABLE IF EXISTS buses_models CASCADE;
DROP TABLE IF EXISTS cities CASCADE;
DROP TABLE IF EXISTS departure_time CASCADE;
DROP TABLE IF EXISTS intervals CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS seat_reservation CASCADE;
DROP TABLE IF EXISTS transits CASCADE;
DROP TABLE IF EXISTS users CASCADE;

DROP VIEW IF EXISTS countries;
DROP VIEW IF EXISTS lines;

DROP SEQUENCE IF EXISTS reservation_id;

DROP TYPE IF EXISTS day;
DROP DOMAIN IF EXISTS email;
DROP EXTENSION IF EXISTS citext;

DROP FUNCTION IF EXISTS break_check();
DROP FUNCTION IF EXISTS date_reservation_check();
DROP FUNCTION IF EXISTS have_free_seat_check();
DROP FUNCTION IF EXISTS remove_breaks_after_delete();
DROP FUNCTION IF EXISTS remove_breaks_after_update();
DROP FUNCTION IF EXISTS seat_reservation_departure_date_check();