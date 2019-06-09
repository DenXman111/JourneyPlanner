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
PATH_TO_JSON="$PWD/jpConnection.json"
if [ ! -f "jpConnection.json" ]; then
	ech "Specify the path for the 'jpConnection.json' file:"
	read PATH_TO_JSON;
fi
echo
echo
export GOOGLE_APPLICATION_CREDENTIALS="$PATH_TO_JSON"
java --module-path "$PATH_TO_FX" --add-modules=javafx.controls,javafx.web,javafx.fxml -jar JourneyPlanner.jar
