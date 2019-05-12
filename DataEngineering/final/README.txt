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

5. Wyzwalacz seat_reservation_departure_date_check zapewnia, że nigdy nie będą dwie rezerwacje na to samo
miejsce w jednyn autobusie.

6. Wyzwalacz transit_reservation_check przed wstawieniem rezerwacji na przejazd sprawdza czy dany przejazd
faktycznie odbędzie się w podanym terminie.

7. Przykładowe dane dla tabel cities, bus_stops, new_users, buses_models, transits, spans,
 departure_time, reservations, transit_reservation, seat_reservation



