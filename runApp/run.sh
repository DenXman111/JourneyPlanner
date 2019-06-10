!#/bin/bash
echo "The application requires Java JDK 11 and JavaFX SDK installed on your machine."
echo
echo "Download link for Java JDK 11:"
echo "https://jdk.java.net/11/"
echo
echo "Download link for JavaFX SDK:"
echo "https://gluonhq.com/products/javafx/"
echo
echo "Specify the path for the JavaFX SDK library:"
read PATH_TO_FX
echo
echo
java --module-path "$PATH_TO_FX" --add-modules=javafx.controls,javafx.web,javafx.fxml -jar JourneyPlanner.jar
