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
    name varchar(30) not null,
    surname varchar(30) not null
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

insert into buses values
(1, 1, 2, 44, '2019-05-01', '2019-05-02', 30),
(2, 1, 3, 20, '2019-05-01', '2019-05-02', 30),
(3, 1, 5, 20, '2019-05-01', '2019-05-02', 30),
(4, 1, 7, 25, '2019-05-04', '2019-05-04', 30),
(5, 2, 19, 56, '2019-05-06', '2019-05-07', 30),
(6, 3, 1, 19, '2019-05-01', '2019-05-02', 30),
(7, 3, 6, 18, '2019-05-01', '2019-05-02', 30),
(8, 4, 10 ,18, '2019-05-01', '2019-05-02', 30),
(9, 4, 3, 14, '2019-05-01', '2019-05-02', 30),
(10, 5, 8, 19, '2019-05-01', '2019-05-02', 30),
(11, 5, 6, 22, '2019-05-01', '2019-05-02', 30),
(12, 2, 1, 42, '2019-05-01', '2019-05-02', 30),
(13, 6, 9, 17, '2019-05-01', '2019-05-02', 30),
(14, 9, 1, 37, '2019-05-06', '2019-05-06', 30),
(15, 7, 9, 10, '2019-05-01', '2019-05-02', 30),
(16, 8, 10, 13, '2019-05-01', '2019-05-02', 30),
(17, 6, 8, 21, '2019-05-01', '2019-05-02', 30),
(18, 1, 12, 44, '2019-05-01', '2019-05-02', 30),
(19, 12, 22, 59, '2019-05-01', '2019-05-02', 30),
(20, 2, 25, 6, '2019-05-01', '2019-05-02', 30),
(21, 25, 2, 6, '2019-05-01', '2019-05-02', 30),
(22, 25, 19, 57, '2019-05-01', '2019-05-02', 30),
(23, 19, 1, 69, '2019-05-10', '2019-05-11', 30);
