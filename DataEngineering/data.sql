-- noinspection SqlResolveForFile

-- exemplary data
INSERT INTO cities (id, name, rating, average_price, country) VALUES
(1, 'Krakow', 4.27, 74, 'Poland'), --
(2, 'Mozyr', 3.00, 30, 'Belarus'), --
(3, 'Poznan', 3.20, 49, 'Poland'), --
(4, 'Warszawa', 3.94, 88, 'Poland'),--
(5, 'Lodz', 2.20, 52, 'Poland'), --
(6, 'Gdansk', 3.20, 50, 'Poland'),--
(7, 'Szczecin', 2.40, 44, 'Poland'),--
(8, 'Bydgoszcz', 2.10, 44, 'Poland'),--
(9, 'Lublin', 3.12, 46, 'Poland'), --
(10, 'Bialystok', 2.4, 41, 'Poland'),--
(11, 'Wroclaw', 3.03, 42, 'Poland'), --
(12, 'Vienna', 4.12, 110, 'Austria'), --
(13, 'Prague', 4.03, 100, 'Czech'), --
(14, 'Berlin', 3.94, 118, 'Germany'),--
(15, 'Budapest', 3.88, 88, 'Hungary'), --
(16, 'Kiev', 3.31, 77, 'Ukraine'), --
(17, 'Lvov', 3.10, 69, 'Ukraine'), --
(18, 'Vilnius', 3.90, 85, 'Latvia'), --
(19, 'Moscow', 4.44, 90, 'Russia'), --
(20, 'Copenhagen', 4.55, 155, 'Denmark'), --
(21, 'Paris', 4.59, 172, 'France'), --
(22, 'Milan', 4.28, 121, 'Italy'), --
(23, 'Zagreb', 4.10, 92, 'Croatia'), --
(24, 'Munich', 3.59, 101, 'Germany'),--
(25, 'Minsk', 4.33, 55, 'Belarus'),
(26, 'Katowice', 4.22, 74, 'Poland'), --
(27, 'Kielce', 2.78, 50, 'Poland'), --
(28, 'Olsztyn', 3.9, 64, 'Poland'), --
(29, 'Rzeszow', 4.1, 48, 'Poland'), --
(30, 'Torun', 4.3, 51, 'Poland'); --

INSERT INTO bus_stops(id, stop_name, city) VALUES
(1, 'Dworzec autobusowy w Białymstoku', 10),
(2, 'Dworzec autobusowy w Bydgoszczy', 8),
(3, 'Dworzec PKS Gdańsk', 6),
(4, 'Dworzec autobusowy w Katowicach', 26),
(5, 'Dworzec PKS w Kielcach', 27),
(6, 'Dworzec MDA w Krakowie', 1),
(7, 'Dworzec autobusowy w Lublinie', 9),
(8, 'Dworzec autobusowy Centralny w Łodzi', 5),
(9, 'Dworzec autobusowy Łódź Kaliska', 5),
(10, 'Dworzec autobusowy w Olsztynie', 28),
(11, 'Dworzec autobusowy w Poznaniu', 3),
(12, 'Dworzec autobusowy w Rzeszowie', 29),
(13, 'Dworzec autobusowy w Szczecinie', 7),
(14, 'Dworzec autobusowy w Toruniu', 30), --
(15, 'Dworzec PKS w Toruniu', 30), --
(16, 'Dworzec autobusowy Warszawa Stadion', 4), --
(17, 'Dworzec autobusowy Warszawa Zachodnia', 4), --
(18, 'Dworzec Wrocław', 11), --
(19, 'Kiev Central Bus Station', 16), --
(20, 'Lviv bus station', 17), --
(21, 'Zentraler Omnibusbahnhof Berlin', 14), --
(22, 'Zentraler Omnibusbahnhof München', 24), --
(23, 'Avtovokzal Mazyr', 2), --
(24, 'Vienna International Busterminal', 12), --
(25, 'Vienna bu station', 12), --
(26, 'Praha Florenc', 13), --
(27, 'Budapest bus station', 15), --
(28, 'Vilnius Bus Station', 18), --
(29, 'Moscow-Butyrskaya Station', 19), --
(30, 'Kurskiy Railway Station', 20), --
(31, 'Copenhagen Central Station', 20), --
(32, 'Porte Maillot', 21), --
(33, 'BUS Milan central station', 22), --
(34, 'Zagreb Bus Station', 23), --
(35, 'Central Bus Terminals', 25);


