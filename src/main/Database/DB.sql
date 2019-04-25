drop table if exists cities cascade;
create table cities (
id serial primary key,
name varchar(30) unique not null,
rating numeric(2,2) check (rating<5 and rating>0),
average_price numeric(3,2) not null
);
drop table if exists buses cascade;
create table buses(
id serial primary key,
start_city integer references cities(id) not null,
end_city integer references cities(id) not null,
price numeric(3,2) not null,
trip_date date not null,
available_spaces numeric(2) not null,
check(start_city!=end_city)
);
drop table if exists users cascade;
create table users(
username varchar(30) primary key not null,
password varchar(30)
);
drop table if exists trips;
create table trips(
bus_id integer references buses(id),
traveler varchar(30) references users(username)
);