#Uruchamianie aplikacji:

1. Aplikacja wymaga podania danych do połączenia z bazą danych w systemie Postgresql. 
Taki system można pobrać ze strony: „https://www.postgresql.org/download/” i uruchomić lokalnie. 

    W pierwszym oknie, które pojawi się po uruchomieniu aplikacji należy podać kolejno dane dla dostępnej bazy danych:
	
	nazwę hosta 
	
	port
	
	nazwę użytkownika
	
	hasło dla tego użytkownika
	
	nazwę bazy danych, która zostanie wypełniona
	
    Na wejściu pola będą wypełnione danymi bazy, która jest zwykle automatycznie tworzona wraz z zainstalowaniem systemu Postgresql.

    Uwaga! Aplikacja usunie i nadpisze wszystkie potrKzebne tabele itd. Dlatego na podana instancja bazy nie powinna przechowywać ważnych dla was danych.
Dodatkowo użytkownik, który zostanie podany musi mieć wszelkie potrzebne uprawnienia do tworzenia, usuwania, modyfikowania i wyświetlania tabel, funkcji, itp. 
**oraz rozszerzeń (extensions)**

    Wykorzystujemy rozszerzenia do haszowania haseł i niestandardowych typów tekstowych (citext
, pgcrypto).

2. Aplikacja w pliku jar wymaga zainstalowanego środowiska Java JDK 11 i pobrania JavaFX SDK:

    https://jdk.java.net/11/

    https://gluonhq.com/products/javafx/

3. Aplikacje jar należy uruchamiać z wiersza poleceń z flagami 

    --module-path [PATH_TO_FX] —add-modules=javafx.controls,javafx.web,javafx.fxml

    Gdzie za [PATH_TO_FX] trzeba wstawić ścieżkę do biblioteki JavaFX.

    Przykładowa komenda:

    **java --module-path [PATH_TO_FX] --add-modules=javafx.controls,javafx.web,javafx.fxml -jar JourneyPlanner.jar**

    Jeżeli uruchamiacie aplikację na systemie Linux/macOS skrypt run.sh przeprowadzi was przez uruchamianie aplikacji.
    
#Instrukcja obsługi:

**Przy pierwszym uruchomieniu, po uzupełnieniu danych, należy nacisnąć przycisk *Create* w celu wypełnienia bazy danych.**


Korzystanie z aplikacji powinno być w miarę możliwości intuicyjne. 
Kilka rzeczy, których nie jest tak łatwo się domyślić:

Aby móc zarezerwować wycieczkę trzeba być zalogowanym użytkownikiem aplikacji. 
( sign up -> log in -> find trips)

Czasami mapa nie chce się poprawnie załadować (co wynika z biblioteki, z której korzystamy w aplikacji) najlepiej wtedy zamknąć okno z mapą i włączyć ponownie.

Aby móc modyfikować dostępne busy, listę miast i przystanków trzeba w oknie logowania wpisać dane użytkownika ( username: admin1, password: admin1) i nacisnąć napis ‚login as a moderator’


Opis przyciskow w menu admina:
add new line dodaje linie autobusowa miedzy dwoma przystankami, ktorych podajemy id. Id przystankow mozna sprawdzic w menu 'manage cities and bus stops'
bus type jest od 0 do 4, rozne typy busow maja rozne ilosci miejsc
price=cena za przejazd

Po dodaniu linii mozemy sprawdzic jaki dostala numer klikajac na show lines

Assign span to line: Przyporzadkowywuje dany przedzial czasowy do konkretnej linii, biorac jej numer
Zeby sprawdzic jakie spany sa przypisane do danej linii i jakie one maja id, mamy nizej przycisk Show spans from line

Add departure time: bierze godzine odjazdu busa, czas przejazdu, dzien tygodnia oraz id przedzialu czasowego by ustawic cotygodniowego busa w danym przedziale czasowym

Add break: dodaje do danej linii dzien, w ktorym autobusy maja nie jezdzic

Delete break: usuwa z danej linii wyjatek, jezeli istnieje

delete departure time: analogicznie do add, jesli taki departure time istnieje, a takze usuwa wszystkie rezerwacje wykorzystujace ten konkretny departure time

delete span from line: analogidznie do assign, jesli taki span istnieje, a takze usuwa wszystkie rezerwacje wykorzystujace ten konkretny span

delete line: usuwa linie, a takze wszystkie rezerwacje wykorzystujace te linie

manage cities and bus stops: miasta i przystanki mozna edytowac tylko gdy nie maja one zadnych przypisanych do nich linii

