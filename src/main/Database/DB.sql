drop table if exists cities cascade;
create table cities (
id serial primary key,
name varchar(30) unique not null,
rating numeric(2,2) check (rating<5 and rating>0),
average_price numeric(3,2) not null,
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