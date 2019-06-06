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

---- Etap 1 ----

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



---- Etap 2 ----


https://github.com/DenXman111/JourneyPlanner





Krzysztof Mrzigod

1. Wiekszosc funkcji do komunikacji ze stara baza danych (nie ta z 1szego etapu, tylko mniejsza ktora byla dla niej pierwowzorem)

2. Ekran Historii (dla starej bazy danych)

3. Ekran Moderacji
    3.1 Dodawanie linii autobusowych, przedialow czasowych, czasow odjazdu dla busuw w danym przedziale oraz dni specjalnych
    3.2 Usuwanie powyzszych
    3.3 Usuwanie rezerwacji zwiazanych z usuwanymi elementami
    3.4 Wyswietlanie wszystkich linii autobusowych obecnie w bazie
    3.5 Wyswietlanie wszystkich przedzialow czasowych przypisanych do danej linii


Denis Pivovarov:
1. Całkiem mapa
2. Log in
3. Sign up
4. Inne rzeczy (widać w historii commit'ów na github)


Łukasz Selwa

Mechanizm rezerwowania znalezionych podróży
	1.1 Znajdowanie i rezerwowanie pierwszych k wolnych miejsc
dla każdego wypranego przejazdu lub rzucanie wyjątkiem jeśli się nie da.
	1.2 Mechanizm anulowania rezerwacji
2. poprawiony system logowania (w tym logowania jako moderator)
3. Strona zarządzania miastami i przystankami
	3.1 Zmienianie nazw miast, kraju, oceny miast i średniej ceny za noc w hotelu
	3.2 Usuwanie miast, które jest dozwolone tylko jeśli nie ma przystanku autobusowego w bazie wskazującego na to miast
	3.3 Zmienianie nazw przystanku
	3.4 Zmienianie miasta, do którego dany przystanek należy i usuwanie przystanku.
		Podobnie dozwolone tylko jeśli nie ma przejazdu (transits) wskazującego na ten przystanek
	3.5 dodawanie przystanków i miast (uwaga pojawiają się na końcu tablicy)
4. Komunikacja z bazą danych na google cloud
5. Wygląd stron welcome, form, login, signUp i citeisModerator