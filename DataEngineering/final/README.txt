Projekt „Journey Planner”  autorzy:
Łukasz Selwa,
Dzianis Pivavarau,
Krzysztof Mrzigod

Projekt bazy danych dla przewoźnika autobusowego.
Baza danych umożliwia przechowywanie przejazdów autobusowych (tranists) odbywających się w ustalonych
przedziałach czasowych (spans) regularnie co tydzień. Przejazdy te są między miastami europy,
z których każde posiada indywidualną ocenę (rating) co pozwoli naszej aplikacji na znajdowanie
podróży z najlepszą oceną. Dodatkowo baza umożliwia rezerwowanie miejsc w autobusach
dla zarejestrowanych użytkowników.

Bardziej szczegółowe elementy projektu i wraz z autorem:

Łukasz Selwa:

1. Widok ‚new_users’ nic nie przechowuje ale po wstawieniu wszystkich danych nowego użytkownika
 oblicza hash jego hasła i tworzy nową krotkę w tabeli users.

2. Po zmodyfikowaniu lub usunięciu przedziału czasowego (span) wszystkie jego przerwy (breaks),
które już nie będą w nim zawarte zostaną usunięte.

3. Wyzwalacz check_span_overlap zapewnia, że wszystkie przedziały czasowe w ramach jednego przejazdu
nigdy nie będą się nachodzić. (prawdopodobnie jego funkcjonalność zostanie zmieniona)

4. Wyzwalacz break_check nie dodaje przerwy jeżeli nie jest zawarta w przedziale.

5. Wyzwalacz transit_reservation_check przed wstawieniem rezerwacji na przejazd sprawdza czy dany przejazd
faktycznie odbędzie się w podanym terminie.

6. Przykładowe dane dla tabel cities, bus_stops, new_users, buses_models, transits, spans,
 departure_time, reservations, transit_reservation, seat_reservation

Łukasz Selwa + Denis Pivovarov:

1. Wyzwalacz seat_reservation_departure_date_check zapewnia, że nigdy nie będą dwie rezerwacje na to samo
miejsce w jednyn autobusie.

Denis Pivovarov:

1. Wyzwalacz date_reservation_check na tabeli 'reservations' sprawdza czy data rezerwacji jest przed czasem wyjazdu wszystkich busów tej
rezerwacji.

2. Wyzwalacz have_free_seat_check na tabeli 'seat_reservation' sprawdza czy jest wolne miejsce w autobusie.

3. Funkcja buses_in_span zwraca tabel z informacją o pojedynczych przejazdach na jednym interwale.

4. Funkcja get_buses zwraca tabel z informacją o pojedynczych przejazdach w zadanym przedziale dat.

5. Ograniczenia na tabelach buses_models, transits, spans, seat_reservation.

Krzysztof Mrzigod

1. Constrainty spojnosciowe na tabelach cities, bus_stops (rating, cena, nazwa)

2. Funkcja add_bus ktora dodaje potrzebne krotki do tabel transits, intervals i departure dates by reprezentowac nowa linie autobusowa

3. Sekwencje transit_id i bus_id wykorzystywane przez powyzsza funkcje

