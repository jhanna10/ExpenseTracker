import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Start extends Application {

	public static void main(String[] args) {
		launch(args);

	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		
		Scene scene = new Scene(root);
		primaryStage.setTitle("Expense Tracker");
		primaryStage.setScene(scene);
		primaryStage.show();
		
	}
	
	@Override
	public void stop() {
		try {
			EntryData.getInstance().saveEntries();
		
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}
	@Override
	public void init() {
		try {
			EntryData.getInstance().loadEntries();
			
		}catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

}


