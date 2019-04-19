drop table if exists cities;
create table cities (
id serial primary key,
name varchar(30) unique not null,
rating numeric(2,2) check (rating<5 and rating>0),
average_price numeric(3,2)
);
drop table if exists buses cascade;
create table buses(
id serial primary key,
start_city varchar(30) references cities(name),
end_city varchar(30) references cities(name),
price numeric(3,2),
trip_date date,
available_spaces numeric(2),
check(start_city!=end_city)
);
