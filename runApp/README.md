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