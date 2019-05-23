# JourneyPlanner

to work on this project:

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
    
    --module-path ${PATH_TO_FX} --add-modules=javafx.controls,javafx.fxml,javafx.web
    
    
4. If you want use elements from .jar in Scene Builder you should add .jar to Scene Builder.