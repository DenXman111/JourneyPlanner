drop table if exists cities cascade;
create table cities (
id serial primary key,
name varchar(30) unique not null,
rating numeric(3,2) check (rating<=5 and rating>0),
average_price numeric(5,2) not null,
country varchar(30)
);
drop table if exists buses cascade;
create table buses(
id serial primary key,
start_city integer references cities(id) not null,
end_city integer references cities(id) not null,
price numeric(3,2) not null,
departure date not null,
arrival date not null,
available_spaces numeric(2) not null,
check(start_city!=end_city),
check(departure<arrival)
);
drop table if exists users cascade;
create table users(
username varchar(30) primary key not null,
password varchar(30),
name varchar(30),
surname varchar(30)
);
drop table if exists trips;
create table trips(
id numeric,
bus_id integer references buses(id),
traveler varchar(30) references users(username),
primary key (id,bus_id,traveler)
);
insert into cities values
(1,'Krakow',5.00,250.00,'Poland'),
(2,'Wroclaw',3.00,210.00,'Poland'),
(3,'Poznan',3.20,195.00,'Poland'),
(4,'Warszawa',3.10,235.00,'Poland'),
(5,'Lodz',2.20,183.00,'Poland'),
(6,'Gdansk',4.30,265.00,'Poland'),
(7,'Szczecin',3.60,197.00,'Poland'),
(8,'Bydgoszcz',2.80,196.00,'Poland'),
(9,'Lublin',4.1,194.00,'Poland'),
(10,'Bialystok',2.4,162.00,'Poland');


