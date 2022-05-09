import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ExpenseChartController {

	private Stage stage;
	private Scene scene;
	private Parent root;


	private ObservableList<PieChart.Data> pieData;
	private ObservableList<Entry> entries;
	private Map<String, Double> map;
	private ObservableList<String> categories;
	private ObservableList<String> amount;
	
	@FXML 
	private PieChart pieChart;
	@FXML
	private ListView<String> lbxCategories;
	@FXML
	private ListView<String> lbxAmount;
	
	public void initialize() {
		entries = FXCollections.observableArrayList(EntryData.getInstance().getList());
		pieData = FXCollections.observableArrayList();
		map = new TreeMap<>();
		
		categories = FXCollections.observableArrayList();
		amount = FXCollections.observableArrayList();
		
		for(Entry entry: entries) {
			if(entry instanceof Expense) {
				String cat = entry.getCategory();
				if(map.containsKey(cat)) {
					double value = map.get(cat);
					value += Double.parseDouble(entry.getAmount().substring(1));
					map.put(cat, value);
				}else {
					map.put(cat, Double.parseDouble(entry.getAmount().substring(1)));
				}
			}
		}
		
		map.forEach((c, a) -> {
			pieData.add(new PieChart.Data(c, a));
			categories.add(c);
			amount.add(String.format("$%.2f", a));
		});
		
		
		pieChart.setData(pieData);
		lbxCategories.setItems(categories);
		lbxAmount.setItems(amount);
		lbxAmount.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override public ListCell<String> call(ListView<String> list) {
				return new RightAlign();
			}
		});
	}
	
	public void switchToMain(ActionEvent e) throws IOException {
		root = FXMLLoader.load(getClass().getResource("Main.fxml"));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}

class RightAlign extends ListCell<String> {
	
	public RightAlign() { }
	
	@Override 
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		setText(item);
		setAlignment(Pos.CENTER_RIGHT);
	}
}
