package jp.kotmw.core;

import org.bukkit.Location;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jp.kotmw.core.nms.particle.magicsquare.Magic_square;

public class FXMain extends Application {

	public static void main(String[] args) {
		Magic_square square = new Magic_square(new Location(null, 0, 0, 0), "{\"particle\":\"flame\",\"color\":\"#000000\",\"shapes\":[{\"type\":\"star\",\"radius\":4,\"quantity\":12}]}");
		square.draw();
		System.out.println("出力完了");
		System.exit(0);
		//launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Json Editor");
		primaryStage.setScene(new Scene(FXMLLoader.load(ClassLoader.getSystemResource("Main.fxml"))));
		primaryStage.show();
	}
}