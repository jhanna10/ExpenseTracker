import java.io.IOException;
import java.util.InputMismatchException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class AddExpenseController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML
	private TextField expenseAmount;
	@FXML
	private TextField expenseDescription;
	
	@FXML 
	private ComboBox<String> expenseCategory;
	
	
	public void initialize() {
		
		expenseCategory.setItems(EntryData.getInstance().getExpenseCategories());
	}
	
	public void switchToMain(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void addExpense(ActionEvent e) throws IOException {
		double amount;
		String description;
		String category;
		Alert a;
	
		try {
			if(expenseAmount.getText().isEmpty()) throw new InputMismatchException();
			amount = Double.parseDouble(expenseAmount.getText());
			if(expenseDescription.getText().isEmpty()) throw new IllegalArgumentException();
			description = expenseDescription.getText();
			if(expenseCategory.getValue().isBlank()) throw new NullPointerException();
			category = expenseCategory.getValue();
			Expense expense = new Expense(amount, description, category);
			EntryData.getInstance().addEntry(expense);
			EntryData.getInstance().setExpenses(amount);
			EntryData.getInstance().setNet();
			switchToMain(e);
			
		}catch(InputMismatchException | NumberFormatException ex) {
			a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("Enter Amount as a number.");
			a.show();
		}catch(NullPointerException ex) {
			a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("Choose a Category");
			a.show();
		}catch(IllegalArgumentException ex) {
			a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("Enter a short Description");
			a.show();
		}catch(Exception ex) {
			a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("An Unkown Error Occured");
			a.show();
		}
		
	}
	
	public void addCategory(ActionEvent e) {
		TextInputDialog inputDialog = new TextInputDialog();
		inputDialog.setContentText("Category:");
		inputDialog.setHeaderText("Enter a new Category");
		
		inputDialog.showAndWait()
			.filter(response -> response != "")
			.ifPresent(response -> EntryData.getInstance().addExpenseCategory(inputDialog.getEditor().getText()));
	}
	
	public void close(ActionEvent e) {
		Platform.exit();
	}
}
