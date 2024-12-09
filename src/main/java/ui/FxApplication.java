package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxApplication extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Lade die FXML-Datei für die MainView
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Mainview.fxml"));
            Parent root = loader.load();

            // Konfiguriere die Hauptszene
            Scene scene = new Scene(root);
            primaryStage.setTitle("PrivateBank Management");
            primaryStage.setScene(scene);

            // Zeige das Hauptfenster
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Fehler beim Laden der MainView!");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
