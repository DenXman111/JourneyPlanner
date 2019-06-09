# JourneyPlanner

1. full description tba.

# Starting the application

1. If you're using Linux/macOS you can simply download folder runApp and run the Bash script run.sh. The script will guide you with all the necessary downloads and then it will start the application.

2. Windows tba.

# Using the JourneyPlanner application 

1. To log in as a moderator go to the 'LogIn' window and fill text areas:
   
   username: **admin1**
   
   password: **admin1**
   
   Then press 'login as a moderator'.

# IDE preparations 

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
    
    --module-path ${PATH_TO_FX} --add-modules=javafx.controls,javafx.web,javafx.fxml
   
4. Create environmental variable **GOOGLE_APPLICATION_CREDENTIALS** pointing to credentials json file (message us to get it)

5. (optional) If you work on IntelliJ install cloud code plugin by google
