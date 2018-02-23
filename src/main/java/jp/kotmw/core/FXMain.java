package jp.kotmw.core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMain extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Json Editor");
		primaryStage.setScene(new Scene(FXMLLoader.load(ClassLoader.getSystemResource("Main.fxml"))));
		primaryStage.show();
	}
}
