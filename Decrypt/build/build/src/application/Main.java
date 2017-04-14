package application;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Main extends Application {

	private Stage primaryStage;
	private BorderPane baseLayout;
	
	@Override
	public void start(Stage primaryStage) {
		
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Ransomware Sample");
		
		initBaseLayout();
		initMainLayout();
		
		System.out.println("메인 윈도우 열림");
	}

	public void initBaseLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("BaseLayout.fxml"));
			baseLayout = (BorderPane) loader.load();
			
			Scene scene = new Scene(baseLayout);
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			
		}
	}
	
	public void initMainLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("MainLayout.fxml"));
			AnchorPane overview = (AnchorPane) loader.load();
			
			baseLayout.setCenter(overview);
		} catch (IOException e) {
			
		}
	}
		
	public static void main(String[] args) {
		launch(args);
	}

	public Window getPrimaryStage() {
		// TODO Auto-generated method stub
		return this.primaryStage;
	}
}
