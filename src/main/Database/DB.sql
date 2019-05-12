drop table if exists cities cascade;

create table cities (
    id serial primary key,
    name varchar(30) unique not null,
    rating numeric(3,2) check (rating<=5 and rating>0),
    average_price numeric(5) not null,
    country varchar(30)
);

drop table if exists buses cascade;

create table buses(
    id serial primary key,
    start_city integer references cities(id) not null,
    end_city integer references cities(id) not null,
    price numeric(4) not null,
    departure date not null,
    arrival date not null,
    available_spaces numeric(2) not null,
    check(start_city!=end_city),
    check(departure<=arrival)
);

drop table if exists users cascade;

create table users(
    username varchar(30) primary key not null,
    password varchar(30) not null,
    email varchar(30) unique not null,
    name varchar(30) not null,
    surname varchar(30) not null
);

drop table if exists moderators cascade;

create table moderators(
    username varchar(30) primary key not null,
    password varchar(30) not null
);

drop table if exists trips;

create table trips(
    id numeric,
    bus_id integer references buses(id),
    traveler varchar(30) references users(username),
    primary key (id,bus_id,traveler)
);

insert into cities values
(1, 'Krakow', 4.27, 74, 'Poland'),
(2, 'Mozyr', 3.00, 30, 'Belarus'),
(3, 'Poznan', 3.20, 49, 'Poland'),
(4, 'Warszawa', 3.94, 88, 'Poland'),
(5, 'Lodz', 2.20, 52, 'Poland'),
(6, 'Gdansk', 3.20, 50, 'Poland'),
(7, 'Szczecin', 2.40, 44, 'Poland'),
(8, 'Bydgoszcz', 2.10, 44, 'Poland'),
(9, 'Lublin', 3.12, 46, 'Poland'),
(10, 'Bialystok', 2.4, 41, 'Poland'),
(11, 'Wroclaw', 3.03, 42, 'Poland'),
(12, 'Vienna', 4.12, 110, 'Austria'),
(13, 'Prague', 4.03, 100, 'Czech'),
(14, 'Berlin', 3.94, 118, 'Germany'),
(15, 'Budapest', 3.88, 88, 'Hungary'),
(16, 'Kiev', 3.31, 77, 'Ukraine'),
(17, 'Lvov', 3.10, 69, 'Ukraine'),
(18, 'Vilnius', 3.90, 85, 'Latvia'),
(19, 'Moscow', 4.44, 90, 'Russia'),
(20, 'Copenhagen', 4.55, 155, 'Denmark'),
(21, 'Paris', 4.59, 172, 'France'),
(22, 'Milan', 4.28, 121, 'Italy'),
(23, 'Zagreb', 4.10, 92, 'Croatia'),
(24, 'Munich', 3.59, 101, 'Germany'),
(25, 'Minsk', 4.33, 55, 'Belarus');

DROP SEQUENCE IF EXISTS cities_seq;
CREATE SEQUENCE cities_seq START 26;

DROP SEQUENCE IF EXISTS buses_seq;
CREATE SEQUENCE buses_seq START 1;

DROP SEQUENCE IF EXISTS res_id;
create sequence res_id increment by 1 start 1 ;

insert into users values
('admin', 'admin', 'denis_pivovarov@gmail.com', 'Denis', 'Pivovarov'),
('user', 'user', 'denis@gmail.com', 'Denis', 'Pivovarov');

insert into moderators values ('admin', 'admin');

insert into buses values
(nextval('buses_seq'), 1, 2, 44, '2019-05-01', '2019-05-02', 30),
(nextval('buses_seq'), 1, 2, 44, '2019-05-03', '2019-05-04', 30),
(nextval('buses_seq'), 1, 2, 44, '2019-05-05', '2019-05-06', 30),
(nextval('buses_seq'), 1, 2, 44, '2019-05-07', '2019-05-08', 30),
(nextval('buses_seq'), 1, 2, 44, '2019-05-09', '2019-05-10', 30),
(nextval('buses_seq'), 1, 2, 44, '2019-05-11', '2019-05-12', 30),

(nextval('buses_seq'), 2, 1, 44, '2019-05-02', '2019-05-03', 30),
(nextval('buses_seq'), 2, 1, 44, '2019-05-04', '2019-05-05', 30),
(nextval('buses_seq'), 2, 1, 44, '2019-05-06', '2019-05-07', 30),
(nextval('buses_seq'), 2, 1, 44, '2019-05-08', '2019-05-09', 30),
(nextval('buses_seq'), 2, 1, 44, '2019-05-10', '2019-05-11', 30),
(nextval('buses_seq'), 2, 1, 44, '2019-05-12', '2019-05-13', 30),

(nextval('buses_seq'), 1, 3, 20, '2019-05-01', '2019-05-01', 30),
(nextval('buses_seq'), 1, 3, 20, '2019-05-05', '2019-05-05', 30),
(nextval('buses_seq'), 1, 3, 20, '2019-05-08', '2019-05-08', 30),
(nextval('buses_seq'), 1, 3, 20, '2019-05-10', '2019-05-10', 30),
(nextval('buses_seq'), 1, 3, 20, '2019-05-15', '2019-05-15', 30),
(nextval('buses_seq'), 1, 3, 20, '2019-05-19', '2019-05-19', 30),

(nextval('buses_seq'), 3, 1, 20, '2019-05-02', '2019-05-02', 30),
(nextval('buses_seq'), 3, 1, 20, '2019-05-06', '2019-05-06', 30),
(nextval('buses_seq'), 3, 1, 20, '2019-05-08', '2019-05-08', 30),
(nextval('buses_seq'), 3, 1, 20, '2019-05-10', '2019-05-10', 30),
(nextval('buses_seq'), 3, 1, 20, '2019-05-16', '2019-05-16', 30),
(nextval('buses_seq'), 3, 1, 20, '2019-05-19', '2019-05-19', 30),

(nextval('buses_seq'), 1, 5, 20, '2019-05-01', '2019-05-01', 30),
(nextval('buses_seq'), 1, 5, 20, '2019-05-07', '2019-05-07', 30),
(nextval('buses_seq'), 1, 5, 20, '2019-05-10', '2019-05-10', 30),

(nextval('buses_seq'), 5, 1, 20, '2019-05-03', '2019-05-03', 30),
(nextval('buses_seq'), 5, 1, 20, '2019-05-08', '2019-05-08', 30),
(nextval('buses_seq'), 5, 1, 20, '2019-05-12', '2019-05-12', 30),

(nextval('buses_seq'), 1, 7, 25, '2019-05-01', '2019-05-01', 30),
(nextval('buses_seq'), 1, 7, 25, '2019-05-04', '2019-05-04', 30),
(nextval('buses_seq'), 1, 7, 25, '2019-05-05', '2019-05-05', 30),
(nextval('buses_seq'), 1, 7, 25, '2019-05-10', '2019-05-10', 30),

(nextval('buses_seq'), 7, 1, 25, '2019-05-02', '2019-05-02', 30),
(nextval('buses_seq'), 7, 1, 25, '2019-05-04', '2019-05-04', 30),
(nextval('buses_seq'), 7, 1, 25, '2019-05-07', '2019-05-07', 30),
(nextval('buses_seq'), 7, 1, 25, '2019-05-12', '2019-05-12', 30),

(nextval('buses_seq'), 3, 6, 18, '2019-05-01', '2019-05-01', 30),
(nextval('buses_seq'), 3, 6, 18, '2019-05-03', '2019-05-03', 30),
(nextval('buses_seq'), 3, 6, 18, '2019-05-05', '2019-05-05', 30),
(nextval('buses_seq'), 3, 6, 18, '2019-05-08', '2019-05-08', 30),
(nextval('buses_seq'), 3, 6, 18, '2019-05-11', '2019-05-11', 30),

(nextval('buses_seq'), 6, 3, 18, '2019-05-02', '2019-05-02', 30),
(nextval('buses_seq'), 6, 3, 18, '2019-05-04', '2019-05-04', 30),
(nextval('buses_seq'), 6, 3, 18, '2019-05-06', '2019-05-06', 30),
(nextval('buses_seq'), 6, 3, 18, '2019-05-09', '2019-05-09', 30),
(nextval('buses_seq'), 6, 3, 18, '2019-05-12', '2019-05-12', 30),

(nextval('buses_seq'), 4, 10 ,16, '2019-05-03', '2019-05-03', 30),
(nextval('buses_seq'), 4, 10 ,16, '2019-05-04', '2019-05-04', 30),
(nextval('buses_seq'), 4, 10 ,16, '2019-05-06', '2019-05-06', 30),
(nextval('buses_seq'), 4, 10 ,16, '2019-05-08', '2019-05-08', 30),
(nextval('buses_seq'), 4, 10 ,16, '2019-05-09', '2019-05-09', 30),
(nextval('buses_seq'), 4, 10 ,16, '2019-05-11', '2019-05-11', 30),

(nextval('buses_seq'), 10, 4, 16, '2019-05-04', '2019-05-04', 30),
(nextval('buses_seq'), 10, 4, 16, '2019-05-05', '2019-05-05', 30),
(nextval('buses_seq'), 10, 4, 16, '2019-05-07', '2019-05-07', 30),
(nextval('buses_seq'), 10, 4, 16, '2019-05-09', '2019-05-09', 30),
(nextval('buses_seq'), 10, 4, 16, '2019-05-10', '2019-05-10', 30),
(nextval('buses_seq'), 10, 4, 16, '2019-05-12', '2019-05-12', 30),

(nextval('buses_seq'), 3, 4, 14, '2019-05-02', '2019-05-02', 30),
(nextval('buses_seq'), 3, 4, 14, '2019-05-04', '2019-05-04', 30),
(nextval('buses_seq'), 3, 4, 14, '2019-05-06', '2019-05-06', 30),
(nextval('buses_seq'), 3, 4, 14, '2019-05-08', '2019-05-08', 30),
(nextval('buses_seq'), 3, 4, 14, '2019-05-10', '2019-05-10', 30),

(nextval('buses_seq'), 4, 3, 14, '2019-05-03', '2019-05-03', 30),
(nextval('buses_seq'), 4, 3, 14, '2019-05-05', '2019-05-05', 30),
(nextval('buses_seq'), 4, 3, 14, '2019-05-07', '2019-05-07', 30),
(nextval('buses_seq'), 4, 3, 14, '2019-05-09', '2019-05-09', 30),
(nextval('buses_seq'), 4, 3, 14, '2019-05-11', '2019-05-11', 30),

(nextval('buses_seq'), 5, 8, 19, '2019-05-03', '2019-05-03', 30),
(nextval('buses_seq'), 5, 8, 19, '2019-05-08', '2019-05-08', 30),
(nextval('buses_seq'), 5, 8, 19, '2019-05-10', '2019-05-10', 30),

(nextval('buses_seq'), 8, 5, 19, '2019-05-04', '2019-05-04', 30),
(nextval('buses_seq'), 8, 5, 19, '2019-05-09', '2019-05-09', 30),
(nextval('buses_seq'), 8, 5, 19, '2019-05-11', '2019-05-11', 30),

(nextval('buses_seq'), 5, 6, 22, '2019-05-02', '2019-05-02', 30),
(nextval('buses_seq'), 5, 6, 22, '2019-05-04', '2019-05-04', 30),
(nextval('buses_seq'), 5, 6, 22, '2019-05-06', '2019-05-06', 30),
(nextval('buses_seq'), 5, 6, 22, '2019-05-09', '2019-05-09', 30),

(nextval('buses_seq'), 6, 5, 22, '2019-05-03', '2019-05-03', 30),
(nextval('buses_seq'), 6, 5, 22, '2019-05-05', '2019-05-05', 30),
(nextval('buses_seq'), 6, 5, 22, '2019-05-07', '2019-05-07', 30),
(nextval('buses_seq'), 6, 5, 22, '2019-05-10', '2019-05-10', 30),

(nextval('buses_seq'), 6, 9, 24, '2019-05-04', '2019-05-04', 30),
(nextval('buses_seq'), 6, 9, 24, '2019-05-06', '2019-05-06', 30),
(nextval('buses_seq'), 6, 9, 24, '2019-05-08', '2019-05-08', 30),
(nextval('buses_seq'), 6, 9, 24, '2019-05-10', '2019-05-10', 30),

(nextval('buses_seq'), 9, 6, 24, '2019-05-05', '2019-05-05', 30),
(nextval('buses_seq'), 9, 6, 24, '2019-05-07', '2019-05-07', 30),
(nextval('buses_seq'), 9, 6, 24, '2019-05-09', '2019-05-09', 30),
(nextval('buses_seq'), 9, 6, 24, '2019-05-11', '2019-05-11', 30),

(nextval('buses_seq'), 1, 9, 17, '2019-05-02', '2019-05-02', 30),
(nextval('buses_seq'), 1, 9, 17, '2019-05-06', '2019-05-06', 30),
(nextval('buses_seq'), 1, 9, 17, '2019-05-07', '2019-05-07', 30),
(nextval('buses_seq'), 1, 9, 17, '2019-05-09', '2019-05-09', 30),
(nextval('buses_seq'), 1, 9, 17, '2019-05-12', '2019-05-12', 30),

(nextval('buses_seq'), 9, 1, 17, '2019-05-03', '2019-05-03', 30),
(nextval('buses_seq'), 9, 1, 17, '2019-05-07', '2019-05-07', 30),
(nextval('buses_seq'), 9, 1, 17, '2019-05-08', '2019-05-08', 30),
(nextval('buses_seq'), 9, 1, 17, '2019-05-10', '2019-05-10', 30),
(nextval('buses_seq'), 9, 1, 17, '2019-05-13', '2019-05-13', 30),

(nextval('buses_seq'), 7, 9, 30, '2019-05-01', '2019-05-02', 30),
(nextval('buses_seq'), 7, 9, 30, '2019-05-03', '2019-05-04', 30),
(nextval('buses_seq'), 7, 9, 30, '2019-05-06', '2019-05-07', 30),
(nextval('buses_seq'), 7, 9, 30, '2019-05-09', '2019-05-10', 30),

(nextval('buses_seq'), 9, 7, 30, '2019-05-03', '2019-05-03', 30),
(nextval('buses_seq'), 9, 7, 30, '2019-05-05', '2019-05-05', 30),
(nextval('buses_seq'), 9, 7, 30, '2019-05-08', '2019-05-08', 30),
(nextval('buses_seq'), 9, 7, 30, '2019-05-11', '2019-05-11', 30),

(nextval('buses_seq'), 8, 10, 18, '2019-05-02', '2019-05-02', 30),
(nextval('buses_seq'), 8, 10, 18, '2019-05-04', '2019-05-04', 30),
(nextval('buses_seq'), 8, 10, 18, '2019-05-06', '2019-05-06', 30),
(nextval('buses_seq'), 8, 10, 18, '2019-05-08', '2019-05-08', 30),

(nextval('buses_seq'), 10, 8, 18, '2019-05-03', '2019-05-03', 30),
(nextval('buses_seq'), 10, 8, 18, '2019-05-05', '2019-05-05', 30),
(nextval('buses_seq'), 10, 8, 18, '2019-05-07', '2019-05-07', 30),
(nextval('buses_seq'), 10, 8, 18, '2019-05-09', '2019-05-09', 30),

(nextval('buses_seq'), 6, 8, 13, '2019-05-01', '2019-05-01', 30),
(nextval('buses_seq'), 6, 8, 13, '2019-05-03', '2019-05-03', 30),
(nextval('buses_seq'), 6, 8, 13, '2019-05-06', '2019-05-06', 30),
(nextval('buses_seq'), 6, 8, 13, '2019-05-09', '2019-05-09', 30),

(nextval('buses_seq'), 8, 6, 13, '2019-05-02', '2019-05-02', 30),
(nextval('buses_seq'), 8, 6, 13, '2019-05-04', '2019-05-04', 30),
(nextval('buses_seq'), 8, 6, 13, '2019-05-07', '2019-05-07', 30),
(nextval('buses_seq'), 8, 6, 13, '2019-05-10', '2019-05-10', 30),

(nextval('buses_seq'), 2, 19, 56, '2019-05-03', '2019-05-04', 30),
(nextval('buses_seq'), 2, 19, 56, '2019-05-04', '2019-05-05', 30),
(nextval('buses_seq'), 2, 19, 56, '2019-05-06', '2019-05-07', 30),
(nextval('buses_seq'), 2, 19, 56, '2019-05-07', '2019-05-08', 30),
(nextval('buses_seq'), 2, 19, 56, '2019-05-09', '2019-05-10', 30),
(nextval('buses_seq'), 2, 19, 56, '2019-05-10', '2019-05-11', 30),
(nextval('buses_seq'), 2, 19, 56, '2019-05-12', '2019-05-13', 30),

(nextval('buses_seq'), 19, 2, 56, '2019-05-03', '2019-05-04', 30),
(nextval('buses_seq'), 19, 2, 56, '2019-05-05', '2019-05-06', 30),
(nextval('buses_seq'), 19, 2, 56, '2019-05-06', '2019-05-07', 30),
(nextval('buses_seq'), 19, 2, 56, '2019-05-08', '2019-05-09', 30),
(nextval('buses_seq'), 19, 2, 56, '2019-05-10', '2019-05-11', 30),
(nextval('buses_seq'), 19, 2, 56, '2019-05-11', '2019-05-12', 30),
(nextval('buses_seq'), 19, 2, 56, '2019-05-13', '2019-05-14', 30),

(nextval('buses_seq'), 1, 12, 44, '2019-05-01', '2019-05-02', 30),
(nextval('buses_seq'), 1, 12, 44, '2019-05-03', '2019-05-04', 30),
(nextval('buses_seq'), 1, 12, 44, '2019-05-05', '2019-05-06', 30),
(nextval('buses_seq'), 1, 12, 44, '2019-05-07', '2019-05-08', 30),
(nextval('buses_seq'), 1, 12, 44, '2019-05-09', '2019-05-10', 30),
(nextval('buses_seq'), 1, 12, 44, '2019-05-11', '2019-05-12', 30),

(nextval('buses_seq'), 12, 1, 44, '2019-05-03', '2019-05-04', 30),
(nextval('buses_seq'), 12, 1, 44, '2019-05-05', '2019-05-06', 30),
(nextval('buses_seq'), 12, 1, 44, '2019-05-07', '2019-05-08', 30),
(nextval('buses_seq'), 12, 1, 44, '2019-05-09', '2019-05-10', 30),
(nextval('buses_seq'), 12, 1, 44, '2019-05-11', '2019-05-12', 30),
(nextval('buses_seq'), 12, 1, 44, '2019-05-13', '2019-05-14', 30),

(nextval('buses_seq'), 12, 22, 59, '2019-05-02', '2019-05-03', 30),
(nextval('buses_seq'), 12, 22, 59, '2019-05-04', '2019-05-05', 30),
(nextval('buses_seq'), 12, 22, 59, '2019-05-06', '2019-05-07', 30),
(nextval('buses_seq'), 12, 22, 59, '2019-05-08', '2019-05-09', 30),
(nextval('buses_seq'), 12, 22, 59, '2019-05-10', '2019-05-11', 30),
(nextval('buses_seq'), 12, 22, 59, '2019-05-12', '2019-05-13', 30),

(nextval('buses_seq'), 22, 12, 59, '2019-05-04', '2019-05-05', 30),
(nextval('buses_seq'), 22, 12, 59, '2019-05-06', '2019-05-07', 30),
(nextval('buses_seq'), 22, 12, 59, '2019-05-08', '2019-05-09', 30),
(nextval('buses_seq'), 22, 12, 59, '2019-05-10', '2019-05-11', 30),
(nextval('buses_seq'), 22, 12, 59, '2019-05-12', '2019-05-13', 30),

(nextval('buses_seq'), 2, 25, 6, '2019-05-01', '2019-05-01', 30),
(nextval('buses_seq'), 2, 25, 6, '2019-05-02', '2019-05-02', 30),
(nextval('buses_seq'), 2, 25, 6, '2019-05-03', '2019-05-03', 30),
(nextval('buses_seq'), 2, 25, 6, '2019-05-04', '2019-05-04', 30),
(nextval('buses_seq'), 2, 25, 6, '2019-05-05', '2019-05-05', 30),
(nextval('buses_seq'), 2, 25, 6, '2019-05-06', '2019-05-06', 30),
(nextval('buses_seq'), 2, 25, 6, '2019-05-07', '2019-05-07', 30),
(nextval('buses_seq'), 2, 25, 6, '2019-05-08', '2019-05-08', 30),
(nextval('buses_seq'), 2, 25, 6, '2019-05-09', '2019-05-09', 30),
(nextval('buses_seq'), 2, 25, 6, '2019-05-10', '2019-05-10', 30),
(nextval('buses_seq'), 2, 25, 6, '2019-05-11', '2019-05-11', 30),

(nextval('buses_seq'), 25, 2, 6, '2019-05-01', '2019-05-01', 30),
(nextval('buses_seq'), 25, 2, 6, '2019-05-02', '2019-05-02', 30),
(nextval('buses_seq'), 25, 2, 6, '2019-05-03', '2019-05-03', 30),
(nextval('buses_seq'), 25, 2, 6, '2019-05-04', '2019-05-04', 30),
(nextval('buses_seq'), 25, 2, 6, '2019-05-05', '2019-05-05', 30),
(nextval('buses_seq'), 25, 2, 6, '2019-05-06', '2019-05-06', 30),
(nextval('buses_seq'), 25, 2, 6, '2019-05-07', '2019-05-07', 30),
(nextval('buses_seq'), 25, 2, 6, '2019-05-08', '2019-05-08', 30),
(nextval('buses_seq'), 25, 2, 6, '2019-05-09', '2019-05-09', 30),
(nextval('buses_seq'), 25, 2, 6, '2019-05-10', '2019-05-10', 30),
(nextval('buses_seq'), 25, 2, 6, '2019-05-11', '2019-05-11', 30),

(nextval('buses_seq'), 19, 25, 57, '2019-05-01', '2019-05-02', 30),
(nextval('buses_seq'), 19, 25, 57, '2019-05-03', '2019-05-04', 30),
(nextval('buses_seq'), 19, 25, 57, '2019-05-05', '2019-05-06', 30),
(nextval('buses_seq'), 19, 25, 57, '2019-05-07', '2019-05-08', 30),
(nextval('buses_seq'), 19, 25, 57, '2019-05-09', '2019-05-10', 30),
(nextval('buses_seq'), 19, 25, 57, '2019-05-11', '2019-05-12', 30),

(nextval('buses_seq'), 25, 19, 57, '2019-05-02', '2019-05-03', 30),
(nextval('buses_seq'), 25, 19, 57, '2019-05-04', '2019-05-05', 30),
(nextval('buses_seq'), 25, 19, 57, '2019-05-06', '2019-05-07', 30),
(nextval('buses_seq'), 25, 19, 57, '2019-05-08', '2019-05-09', 30),
(nextval('buses_seq'), 25, 19, 57, '2019-05-10', '2019-05-11', 30),
(nextval('buses_seq'), 25, 19, 57, '2019-05-12', '2019-05-13', 30),

(nextval('buses_seq'), 1, 19, 69, '2019-05-01', '2019-05-02', 30),
(nextval('buses_seq'), 1, 19, 69, '2019-05-03', '2019-05-04', 30),
(nextval('buses_seq'), 1, 19, 69, '2019-05-06', '2019-05-07', 30),
(nextval('buses_seq'), 1, 19, 69, '2019-05-09', '2019-05-10', 30),
(nextval('buses_seq'), 1, 19, 69, '2019-05-12', '2019-05-13', 30),

(nextval('buses_seq'), 19, 1, 69, '2019-05-01', '2019-05-02', 30),
(nextval('buses_seq'), 19, 1, 69, '2019-05-03', '2019-05-04', 30),
(nextval('buses_seq'), 19, 1, 69, '2019-05-06', '2019-05-07', 30),
(nextval('buses_seq'), 19, 1, 69, '2019-05-09', '2019-05-10', 30),
(nextval('buses_seq'), 19, 1, 69, '2019-05-12', '2019-05-13', 30);
