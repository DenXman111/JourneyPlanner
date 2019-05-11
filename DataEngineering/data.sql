-- noinspection SpellCheckingInspectionForFile

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

-- stops data
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

-- users data
INSERT INTO new_users(username, email_address, password, name, surname) VALUES
('admin1', 'admin1@example.com', 'admin1', 'Łukasz', 'Selwa'),
('Thomas_Mathis0', 'thomas.mathis@example.com', '111111', 'Thomas', 'Mathis'),
('Piers_King1', 'piers.king@example.com', '2000', 'Piers', 'King'),
('Sonia_Allan2', 'sonia.allan@example.com', '159753', 'Sonia', 'Allan'),
('Phil_Arnold3', 'phil.arnold@example.com', '112233', 'Phil', 'Arnold'),
('Una_Wright4', 'una.wright@example.com', '000000', 'Una', 'Wright'),
('Andrea_Fraser5', 'andrea.fraser@example.com', 'maggie', 'Andrea', 'Fraser'),
('Julian_Davies6', 'julian.davies@example.com', 'andrew', 'Julian', 'Davies'),
('Isaac_Lee7', 'isaac.lee@example.com', 'cheese', 'Isaac', 'Lee'),
('Julia_White8', 'julia.white@example.com', '1234567890', 'Julia', 'White'),
('Isaac_Dickens9', 'isaac.dickens@example.com', 'dragon', 'Isaac', 'Dickens'),
('Wanda_Butler10', 'wanda.butler@example.com', 'buster', 'Wanda', 'Butler'),
('Max_Bailey11', 'max.bailey@example.com', 'zxcvbn', 'Max', 'Bailey'),
('Ella_Skinner12', 'ella.skinner@example.com', 'austin', 'Ella', 'Skinner'),
('Peter_Blake13', 'peter.blake@example.com', 'harley', 'Peter', 'Blake'),
('Sean_Mills14', 'sean.mills@example.com', 'matthew', 'Sean', 'Mills'),
('Adam_Thomson15', 'adam.thomson@example.com', 'qwerty', 'Adam', 'Thomson'),
('Amanda_Fraser16', 'amanda.fraser@example.com', '1qaz2wsx', 'Amanda', 'Fraser'),
('Faith_Wright17', 'faith.wright@example.com', '121212', 'Faith', 'Wright'),
('Christian_Alsop18', 'christian.alsop@example.com', 'buster', 'Christian', 'Alsop'),
('Michael_Fraser19', 'michael.fraser@example.com', 'abc123', 'Michael', 'Fraser'),
('Gavin_Metcalfe20', 'gavin.metcalfe@example.com', 'qwerty', 'Gavin', 'Metcalfe'),
('Madeleine_Thomson21', 'madeleine.thomson@example.com', 'freedom', 'Madeleine', 'Thomson'),
('Keith_Vance22', 'keith.vance@example.com', 'dragon', 'Keith', 'Vance'),
('Boris_North23', 'boris.north@example.com', 'klaster', 'Boris', 'North'),
('Nicola_Marshall24', 'nicola.marshall@example.com', 'computer', 'Nicola', 'Marshall'),
('Una_MacLeod25', 'una.macleod@example.com', 'killer', 'Una', 'MacLeod'),
('Penelope_Russell26', 'penelope.russell@example.com', 'martin', 'Penelope', 'Russell'),
('Jason_Greene27', 'jason.greene@example.com', 'cheese', 'Jason', 'Greene'),
('Nicola_Churchill28', 'nicola.churchill@example.com', '696969', 'Nicola', 'Churchill'),
('Tim_Kelly29', 'tim.kelly@example.com', 'jordan', 'Tim', 'Kelly'),
('Joseph_Black30', 'joseph.black@example.com', 'corvette', 'Joseph', 'Black'),
('Felicity_Avery31', 'felicity.avery@example.com', 'superman', 'Felicity', 'Avery'),
('Julia_Welch32', 'julia.welch@example.com', 'abc123', 'Julia', 'Welch'),
('Sally_Jackson33', 'sally.jackson@example.com', '11111111', 'Sally', 'Jackson'),
('Tracey_MacLeod34', 'tracey.macleod@example.com', '123123', 'Tracey', 'MacLeod'),
('Stewart_Bell35', 'stewart.bell@example.com', 'killer', 'Stewart', 'Bell'),
('Kylie_Henderson36', 'kylie.henderson@example.com', 'amanda', 'Kylie', 'Henderson'),
('Bella_Ball37', 'bella.ball@example.com', '2000', 'Bella', 'Ball'),
('Grace_Oliver38', 'grace.oliver@example.com', 'amanda', 'Grace', 'Oliver'),
('Carolyn_Hughes39', 'carolyn.hughes@example.com', '123321', 'Carolyn', 'Hughes'),
('Madeleine_Gray40', 'madeleine.gray@example.com', '11111111', 'Madeleine', 'Gray'),
('Kimberly_Ogden41', 'kimberly.ogden@example.com', 'maggie', 'Kimberly', 'Ogden'),
('Madeleine_Peake42', 'madeleine.peake@example.com', 'thunder', 'Madeleine', 'Peake'),
('Leah_Clarkson43', 'leah.clarkson@example.com', 'jennifer', 'Leah', 'Clarkson'),
('Adrian_Gill44', 'adrian.gill@example.com', 'chelsea', 'Adrian', 'Gill'),
('Christian_Gibson45', 'christian.gibson@example.com', 'tigger', 'Christian', 'Gibson'),
('Donna_Lawrence46', 'donna.lawrence@example.com', '7777777', 'Donna', 'Lawrence'),
('Mary_Martin47', 'mary.martin@example.com', 'dallas', 'Mary', 'Martin'),
('Andrew_Jackson48', 'andrew.jackson@example.com', 'taylor', 'Andrew', 'Jackson'),
('Una_Campbell49', 'una.campbell@example.com', 'klaster', 'Una', 'Campbell'),
('Amanda_Sanderson50', 'amanda.sanderson@example.com', 'password', 'Amanda', 'Sanderson'),
('Claire_Marshall51', 'claire.marshall@example.com', 'hockey', 'Claire', 'Marshall'),
('Ruth_Taylor52', 'ruth.taylor@example.com', '1234567', 'Ruth', 'Taylor'),
('Carol_Edmunds53', 'carol.edmunds@example.com', 'andrew', 'Carol', 'Edmunds'),
('Bella_Anderson54', 'bella.anderson@example.com', '131313', 'Bella', 'Anderson'),
('Luke_Henderson55', 'luke.henderson@example.com', '987654321', 'Luke', 'Henderson'),
('Amelia_Parr56', 'amelia.parr@example.com', 'michael', 'Amelia', 'Parr'),
('Lisa_Chapman57', 'lisa.chapman@example.com', 'william', 'Lisa', 'Chapman'),
('Stephanie_Rees58', 'stephanie.rees@example.com', '000000', 'Stephanie', 'Rees'),
('Emma_Simpson59', 'emma.simpson@example.com', 'qwerty', 'Emma', 'Simpson'),
('Lily_Johnston60', 'lily.johnston@example.com', 'mustang', 'Lily', 'Johnston'),
('Nathan_Dyer61', 'nathan.dyer@example.com', '555555', 'Nathan', 'Dyer'),
('Isaac_Allan62', 'isaac.allan@example.com', '2000', 'Isaac', 'Allan'),
('Keith_Davidson63', 'keith.davidson@example.com', 'michael', 'Keith', 'Davidson'),
('Mary_Morgan64', 'mary.morgan@example.com', 'password', 'Mary', 'Morgan'),
('Liam_Springer65', 'liam.springer@example.com', 'hockey', 'Liam', 'Springer'),
('Zoe_Jones66', 'zoe.jones@example.com', 'joshua', 'Zoe', 'Jones'),
('Leah_Allan67', 'leah.allan@example.com', '1234567', 'Leah', 'Allan'),
('Lucas_Wright68', 'lucas.wright@example.com', '7777777', 'Lucas', 'Wright'),
('Jennifer_Coleman69', 'jennifer.coleman@example.com', 'killer', 'Jennifer', 'Coleman'),
('Ella_Smith70', 'ella.smith@example.com', 'hello', 'Ella', 'Smith'),
('Rebecca_King71', 'rebecca.king@example.com', 'love', 'Rebecca', 'King'),
('Anna_Gibson72', 'anna.gibson@example.com', 'jordan', 'Anna', 'Gibson'),
('Natalie_Simpson73', 'natalie.simpson@example.com', 'football', 'Natalie', 'Simpson'),
('Trevor_Campbell74', 'trevor.campbell@example.com', 'zxcvbn', 'Trevor', 'Campbell'),
('Felicity_Rees75', 'felicity.rees@example.com', 'monkey', 'Felicity', 'Rees'),
('Benjamin_Alsop76', 'benjamin.alsop@example.com', 'pepper', 'Benjamin', 'Alsop'),
('Piers_Churchill77', 'piers.churchill@example.com', 'jennifer', 'Piers', 'Churchill'),
('John_Clark78', 'john.clark@example.com', '654321', 'John', 'Clark'),
('Alan_Alsop79', 'alan.alsop@example.com', 'freedom', 'Alan', 'Alsop'),
('Joan_Sharp80', 'joan.sharp@example.com', 'letmein', 'Joan', 'Sharp'),
('Anthony_Mackenzie81', 'anthony.mackenzie@example.com', 'taylor', 'Anthony', 'Mackenzie'),
('Isaac_Clarkson82', 'isaac.clarkson@example.com', '121212', 'Isaac', 'Clarkson'),
('Christopher_Berry83', 'christopher.berry@example.com', 'password', 'Christopher', 'Berry'),
('Rose_Randall84', 'rose.randall@example.com', 'shadow', 'Rose', 'Randall'),
('Alison_Taylor85', 'alison.taylor@example.com', 'matrix', 'Alison', 'Taylor'),
('Joseph_Fraser86', 'joseph.fraser@example.com', '12345', 'Joseph', 'Fraser'),
('Audrey_Sharp87', 'audrey.sharp@example.com', '123456789', 'Audrey', 'Sharp'),
('Blake_Wright88', 'blake.wright@example.com', 'robert', 'Blake', 'Wright'),
('Mary_Randall89', 'mary.randall@example.com', 'hello', 'Mary', 'Randall'),
('Luke_Forsyth90', 'luke.forsyth@example.com', 'access', 'Luke', 'Forsyth'),
('Jessica_Parr91', 'jessica.parr@example.com', '1234567890', 'Jessica', 'Parr'),
('Jacob_Roberts92', 'jacob.roberts@example.com', '12345', 'Jacob', 'Roberts'),
('Boris_Dickens93', 'boris.dickens@example.com', 'ashley', 'Boris', 'Dickens'),
('Frank_Parr94', 'frank.parr@example.com', 'thomas', 'Frank', 'Parr'),
('Claire_Graham95', 'claire.graham@example.com', '555555', 'Claire', 'Graham'),
('Andrew_McGrath96', 'andrew.mcgrath@example.com', '696969', 'Andrew', 'McGrath'),
('Paul_Gill97', 'paul.gill@example.com', 'zxcvbnm', 'Paul', 'Gill'),
('Boris_Blake98', 'boris.blake@example.com', 'asdfgh', 'Boris', 'Blake'),
('Liam_McGrath99', 'liam.mcgrath@example.com', 'andrew', 'Liam', 'McGrath'),
('Faith_Lyman100', 'faith.lyman@example.com', 'password', 'Faith', 'Lyman'),
('Audrey_Mitchell101', 'audrey.mitchell@example.com', 'corvette', 'Audrey', 'Mitchell'),
('Pippa_Morrison102', 'pippa.morrison@example.com', 'pass', 'Pippa', 'Morrison'),
('Joe_Taylor103', 'joe.taylor@example.com', '7777777', 'Joe', 'Taylor'),
('Megan_Welch104', 'megan.welch@example.com', 'zxcvbnm', 'Megan', 'Welch'),
('Jonathan_Duncan105', 'jonathan.duncan@example.com', '112233', 'Jonathan', 'Duncan'),
('Una_Robertson106', 'una.robertson@example.com', 'joshua', 'Una', 'Robertson'),
('Robert_Marshall107', 'robert.marshall@example.com', 'corvette', 'Robert', 'Marshall'),
('Pippa_North108', 'pippa.north@example.com', 'taylor', 'Pippa', 'North'),
('Lisa_Poole109', 'lisa.poole@example.com', '123456789', 'Lisa', 'Poole'),
('Zoe_Mackenzie110', 'zoe.mackenzie@example.com', 'dragon', 'Zoe', 'Mackenzie'),
('Joshua_Mathis111', 'joshua.mathis@example.com', 'qazwsx', 'Joshua', 'Mathis'),
('Amelia_Morrison112', 'amelia.morrison@example.com', 'tigger', 'Amelia', 'Morrison'),
('Grace_Miller113', 'grace.miller@example.com', 'ginger', 'Grace', 'Miller'),
('Donna_Knox114', 'donna.knox@example.com', 'jordan', 'Donna', 'Knox'),
('Joanne_Mills115', 'joanne.mills@example.com', 'hello', 'Joanne', 'Mills'),
('Joseph_Ross116', 'joseph.ross@example.com', 'harley', 'Joseph', 'Ross'),
('David_Short117', 'david.short@example.com', 'martin', 'David', 'Short'),
('Carl_Mackay118', 'carl.mackay@example.com', '131313', 'Carl', 'Mackay'),
('Tracey_Gibson119', 'tracey.gibson@example.com', 'abc123', 'Tracey', 'Gibson'),
('Lily_Metcalfe120', 'lily.metcalfe@example.com', 'computer', 'Lily', 'Metcalfe'),
('Lucas_Russell121', 'lucas.russell@example.com', 'klaster', 'Lucas', 'Russell'),
('Alan_Newman122', 'alan.newman@example.com', 'taylor', 'Alan', 'Newman'),
('Paul_May123', 'paul.may@example.com', '1234567', 'Paul', 'May'),
('Jennifer_Sharp124', 'jennifer.sharp@example.com', 'matrix', 'Jennifer', 'Sharp'),
('Fiona_Harris125', 'fiona.harris@example.com', '131313', 'Fiona', 'Harris'),
('Piers_Underwood126', 'piers.underwood@example.com', 'football', 'Piers', 'Underwood'),
('Maria_May127', 'maria.may@example.com', '000000', 'Maria', 'May'),
('James_Welch128', 'james.welch@example.com', '1234', 'James', 'Welch'),
('Sebastian_Cameron129', 'sebastian.cameron@example.com', 'jessica', 'Sebastian', 'Cameron'),
('Christopher_Short130', 'christopher.short@example.com', 'harley', 'Christopher', 'Short'),
('Stephanie_Lee131', 'stephanie.lee@example.com', 'zxcvbn', 'Stephanie', 'Lee'),
('Steven_Lambert132', 'steven.lambert@example.com', 'ashley', 'Steven', 'Lambert'),
('Claire_Grant133', 'claire.grant@example.com', 'thomas', 'Claire', 'Grant'),
('Colin_Ross134', 'colin.ross@example.com', '11111111', 'Colin', 'Ross'),
('Michelle_Tucker135', 'michelle.tucker@example.com', '123321', 'Michelle', 'Tucker'),
('Adrian_Lawrence136', 'adrian.lawrence@example.com', 'matrix', 'Adrian', 'Lawrence'),
('Kimberly_Glover137', 'kimberly.glover@example.com', 'abc123', 'Kimberly', 'Glover'),
('Jason_Wilson138', 'jason.wilson@example.com', 'pass', 'Jason', 'Wilson'),
('Sean_Dickens139', 'sean.dickens@example.com', 'password', 'Sean', 'Dickens'),
('Christian_Randall140', 'christian.randall@example.com', '987654321', 'Christian', 'Randall'),
('Rachel_Underwood141', 'rachel.underwood@example.com', 'qazwsx', 'Rachel', 'Underwood'),
('Donna_Hodges142', 'donna.hodges@example.com', 'asdfgh', 'Donna', 'Hodges'),
('Jennifer_Hemmings143', 'jennifer.hemmings@example.com', 'klaster', 'Jennifer', 'Hemmings'),
('Jason_Sutherland144', 'jason.sutherland@example.com', '696969', 'Jason', 'Sutherland'),
('Jan_Short145', 'jan.short@example.com', '987654321', 'Jan', 'Short'),
('Leonard_Young146', 'leonard.young@example.com', 'buster', 'Leonard', 'Young'),
('Vanessa_Sharp147', 'vanessa.sharp@example.com', 'amanda', 'Vanessa', 'Sharp'),
('Diana_Miller148', 'diana.miller@example.com', 'jordan', 'Diana', 'Miller'),
('Yvonne_Anderson149', 'yvonne.anderson@example.com', '1234', 'Yvonne', 'Anderson'),
('Keith_Metcalfe150', 'keith.metcalfe@example.com', 'maggie', 'Keith', 'Metcalfe'),
('Felicity_Vance151', 'felicity.vance@example.com', 'hello', 'Felicity', 'Vance'),
('Dan_Manning152', 'dan.manning@example.com', 'matrix', 'Dan', 'Manning'),
('Anne_Quinn153', 'anne.quinn@example.com', '666666', 'Anne', 'Quinn'),
('Abigail_Ellison154', 'abigail.ellison@example.com', 'ranger', 'Abigail', 'Ellison'),
('Zoe_Davies155', 'zoe.davies@example.com', '1234567890', 'Zoe', 'Davies'),
('Theresa_Chapman156', 'theresa.chapman@example.com', 'yankees', 'Theresa', 'Chapman'),
('James_Mills157', 'james.mills@example.com', 'computer', 'James', 'Mills'),
('Deirdre_Metcalfe158', 'deirdre.metcalfe@example.com', '12345', 'Deirdre', 'Metcalfe'),
('Cameron_Davidson159', 'cameron.davidson@example.com', 'superman', 'Cameron', 'Davidson'),
('Keith_Springer160', 'keith.springer@example.com', '1234567890', 'Keith', 'Springer'),
('Joseph_Sutherland161', 'joseph.sutherland@example.com', '1234', 'Joseph', 'Sutherland'),
('David_Abraham162', 'david.abraham@example.com', 'football', 'David', 'Abraham'),
('Caroline_Knox163', 'caroline.knox@example.com', '777777', 'Caroline', 'Knox'),
('Kylie_Lewis164', 'kylie.lewis@example.com', 'asdfgh', 'Kylie', 'Lewis'),
('Sarah_Ellison165', 'sarah.ellison@example.com', 'taylor', 'Sarah', 'Ellison'),
('Carol_Dyer166', 'carol.dyer@example.com', 'qwertyuiop', 'Carol', 'Dyer'),
('Neil_Pullman167', 'neil.pullman@example.com', 'andrew', 'Neil', 'Pullman'),
('Theresa_Terry168', 'theresa.terry@example.com', '555555', 'Theresa', 'Terry'),
('Heather_Lewis169', 'heather.lewis@example.com', 'trustno1', 'Heather', 'Lewis'),
('Leonard_Berry170', 'leonard.berry@example.com', 'superman', 'Leonard', 'Berry'),
('Elizabeth_Lyman171', 'elizabeth.lyman@example.com', 'taylor', 'Elizabeth', 'Lyman'),
('Max_Walsh172', 'max.walsh@example.com', 'pepper', 'Max', 'Walsh'),
('Frank_Poole173', 'frank.poole@example.com', 'asdfgh', 'Frank', 'Poole'),
('Owen_May174', 'owen.may@example.com', 'andrew', 'Owen', 'May'),
('Carl_Nash175', 'carl.nash@example.com', '987654321', 'Carl', 'Nash'),
('James_Payne176', 'james.payne@example.com', 'charlie', 'James', 'Payne'),
('Sarah_Peters177', 'sarah.peters@example.com', 'george', 'Sarah', 'Peters'),
('Gavin_Carr178', 'gavin.carr@example.com', '1234567890', 'Gavin', 'Carr'),
('Felicity_Lawrence179', 'felicity.lawrence@example.com', 'iloveyou', 'Felicity', 'Lawrence'),
('Brandon_Pullman180', 'brandon.pullman@example.com', 'chelsea', 'Brandon', 'Pullman'),
('Dorothy_King181', 'dorothy.king@example.com', 'shadow', 'Dorothy', 'King'),
('Charles_Harris182', 'charles.harris@example.com', 'maggie', 'Charles', 'Harris'),
('Phil_Wright183', 'phil.wright@example.com', 'hunter', 'Phil', 'Wright'),
('Rebecca_Wallace184', 'rebecca.wallace@example.com', 'iloveyou', 'Rebecca', 'Wallace'),
('Olivia_Berry185', 'olivia.berry@example.com', 'chelsea', 'Olivia', 'Berry'),
('Katherine_Ince186', 'katherine.ince@example.com', 'william', 'Katherine', 'Ince'),
('Anthony_Greene187', 'anthony.greene@example.com', 'chelsea', 'Anthony', 'Greene'),
('Thomas_Henderson188', 'thomas.henderson@example.com', '1234567890', 'Thomas', 'Henderson'),
('Joshua_Walker189', 'joshua.walker@example.com', 'qwertyuiop', 'Joshua', 'Walker'),
('Audrey_Mackay190', 'audrey.mackay@example.com', 'andrew', 'Audrey', 'Mackay'),
('Brian_Kelly191', 'brian.kelly@example.com', '131313', 'Brian', 'Kelly'),
('Blake_Howard192', 'blake.howard@example.com', 'jordan', 'Blake', 'Howard'),
('Emma_Young193', 'emma.young@example.com', 'letmein', 'Emma', 'Young'),
('Diana_Watson194', 'diana.watson@example.com', '654321', 'Diana', 'Watson'),
('Isaac_Dowd195', 'isaac.dowd@example.com', 'trustno1', 'Isaac', 'Dowd'),
('Jonathan_Churchill196', 'jonathan.churchill@example.com', 'harley', 'Jonathan', 'Churchill'),
('Andrea_Hughes197', 'andrea.hughes@example.com', 'baseball', 'Andrea', 'Hughes'),
('Andrew_Randall198', 'andrew.randall@example.com', 'amanda', 'Andrew', 'Randall'),
('Stephen_Mills199', 'stephen.mills@example.com', 'thomas', 'Stephen', 'Mills');


