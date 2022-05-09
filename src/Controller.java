import java.io.IOException;
import java.text.NumberFormat;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class Controller {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	private final NumberFormat CURRENCY_FORMATTER = NumberFormat.getCurrencyInstance();
	private String incomeFormatted;
	private String expensesFormatted;
	private String netFormatted;
	
	@FXML
	private Label totalIncome;
	@FXML
	private Label totalExpenses;
	@FXML 
	private Label net;
	
	@FXML
	private Button deleteBTN;
	@FXML
	private Button cancelBTN;
	@FXML
	private Button addIncomeBTN;
	@FXML
	private Button addExpenseBTN;
	@FXML
	private Button chartBTN;
	
	@FXML
	private TextField expenseAmount;
	@FXML
	private TextField expenseDescription;
	@FXML
	private TextField incomeAmount;
	@FXML 
	private TextField incomeDescription;
	
	@FXML
	private TableColumn<Entry, String> displayAmount;
	@FXML
	private TableColumn<Entry, String> displayCategory;
	@FXML
	private TableColumn<Entry, String> displayDescription;
	@FXML
	private TableColumn<Entry, String> displayDate;
	@FXML
	private TableColumn<Entry, String> displayType;
	@FXML
	private TableView<Entry> displayTable;
	
	
	public void initialize() {
		
		incomeFormatted = CURRENCY_FORMATTER.format(EntryData.getInstance().getIncome());
		expensesFormatted = CURRENCY_FORMATTER.format(EntryData.getInstance().getExpenses());
		netFormatted = CURRENCY_FORMATTER.format(EntryData.getInstance().getNetAmount());
		
		
		if(EntryData.getInstance().getNetAmount() < 0) {
			net.setTextFill(Paint.valueOf("Red"));
		} else {
			net.setTextFill(Paint.valueOf("Green"));
		}
		
		totalIncome.setText(incomeFormatted);
		totalExpenses.setText(expensesFormatted);
		net.setText(netFormatted);
		
		displayAmount.setCellValueFactory(new PropertyValueFactory<Entry, String>("amount"));
		displayCategory.setCellValueFactory(new PropertyValueFactory<Entry, String>("category"));
		displayDescription.setCellValueFactory(new PropertyValueFactory<Entry, String>("description"));
		displayDate.setCellValueFactory(new PropertyValueFactory<Entry, String>("dateFormatted"));
		displayType.setCellValueFactory(new PropertyValueFactory<Entry, String>("type"));

		displayTable.setItems(EntryData.getInstance().getList());
	}
		
	public void switchToAddIncome(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("AddIncome.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToAddExpense(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("AddExpense.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToExpenseChart(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("ExpenseChart.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void allowEntryEdit(ActionEvent e) {
		deleteBTN.setVisible(true);
		cancelBTN.setVisible(true);
		addIncomeBTN.setDisable(true);
		addExpenseBTN.setDisable(true);
		chartBTN.setDisable(true);
		deleteBTN.requestFocus();
	}
	
	public void deleteEntry(ActionEvent e) {
		int row = displayTable.getSelectionModel().getSelectedIndex();
		ObservableList<Entry> list = EntryData.getInstance().getList();
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.setHeaderText("Are you sure you want to delete entry?");
		
		if(row >= 0) {
			confirm.showAndWait()
				.filter(response -> response == ButtonType.OK)
				.ifPresent(response -> { 
					Entry delete = list.get(row);
					double amount = Double.parseDouble(delete.getAmount().substring(1));
					if(delete instanceof Expense) EntryData.getInstance().setExpenses(-amount);
					else EntryData.getInstance().setIncome(-amount);
					EntryData.getInstance().setNet();
					list.remove(row);
					initialize();
					});
		}
	}
	
	public void editCancel(ActionEvent e) {
		deleteBTN.setVisible(false);
		cancelBTN.setVisible(false);
		addIncomeBTN.setDisable(false);
		addExpenseBTN.setDisable(false);
		chartBTN.setDisable(false);
	}
	
	public void close(ActionEvent e) {
		Platform.exit();
	}
	
}
