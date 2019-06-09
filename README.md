# JourneyPlanner

Uruchamianie aplikacji przez plik jar (linux i macOs):

1.  Proszę pobrać i zainstalować, jeżeli nie ma na komputerze, java JDK 11 i JavaFX SDK

	https://jdk.java.net/11/

	https://gluonhq.com/products/javafx/


2.  Proszę rozpakować src.tar i przejść do folderu jarApp

3.  Proszę utworzyć zmienną globalną  w powłoce, w której będzie uruchomiona aplikacja wskazującą na plik jpConnection.json,
    który znajduje się w tym folderze (bez tego aplikacja nie połączy się z bazą na google cloud)

	    export GOOGLE_APPLICATION_CREDENTIALS="[PATH]"

    Gdzie za [PATH] należy podstawić bezwzględną ścieżkę do pliku jpConnection.json

4.  Uruchomić plik  komendą


    java -jar JourneyPlanner.jar --module-path [PATH_TO_FX] --add-modules=javafx.controls,javafx.web,javafx.fxml
    Gdzie za [PATH_TO_FX] trzeba podstawić ścieżkę do javafx (folder lib z pobranego pliku javafx-sdk)

To work on this project:

1. Download the appropriate JDK 11 and JavaFX SDK for your operating system.

https://jdk.java.net/11/

https://gluonhq.com/products/javafx/

Download PostgreSQL JDBC 4.2 Driver, 42.2.5 from
https://jdbc.postgresql.org/download.html 

2.  Go to **File -> Project Structure -> Libraries** and add the JavaFX 11 SDK as a library to the project.
    
    Also add postgresql-42.2.5.jar to Libraries.

3. 
    In **Preferences (File -> Settings) -> Appearance & Behavior -> Path Variables**
     define the name of new global variable as **PATH_TO_FX** and browse to the lib folder of the JavaFX SDK to set its value,
      and click apply.
    Then click on **Run -> Edit Configurations...** and add these VM options:
    
    --module-path ${PATH_TO_FX} --add-modules=javafx.controls,javafx.web,javafx.fxml
   
4. Create environmental variable **GOOGLE_APPLICATION_CREDENTIALS** pointing to credentials json file (message us to get it)

5. (optional) If you work on IntelliJ install cloud code plugin by google
