import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class EntryData {

	private static EntryData instance = new EntryData();
	private static String filename = "Resources/EntryList.txt";
	
	private ObservableList<Entry> entryItems;
	private ObservableList<String> incomeCategories;
	private ObservableList<String> expenseCategories;
	
	private final DateTimeFormatter FORMATTER;
	private double income;
	private double expense;
	private double netAmount;
	
	
	
	private EntryData() {
		FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy");
	}
	
	public void addEntry(Entry entry) {
		entryItems.add(0, entry);
	}
	
	public void addIncomeCategory(String cat) {
		incomeCategories.add(incomeCategories.size() - 1, cat);
	}
	
	public void addExpenseCategory(String cat) {
		expenseCategories.add(expenseCategories.size() - 1, cat);
	}
	
	public static EntryData getInstance() {
		return instance;
	}
	
	public ObservableList<Entry> getList() {
		return entryItems;
	}
	
	public ObservableList<String> getIncomeCategories() {
		return incomeCategories;
	}
	
	public ObservableList<String> getExpenseCategories() {
		return expenseCategories;
	}
	
	public double getNetAmount() {
		return netAmount;
	}
	
	public double getIncome() {
		return income;
	}
	
	public double getExpenses() {
		return expense;
	}
	
	public void setIncome(double value) {
		if(income <= 0)
			income = value; 
		else 
			income += value;
	}
	
	public void setExpenses(double value) {
		if(expense <= 0) 
			expense = value;
		else 
			expense += value;
	}
	
	public void setNet() {
		netAmount = income - expense;
	}
	
	public void saveEntries() throws IOException {
		Path path = Paths.get(filename);
		BufferedWriter bw = Files.newBufferedWriter(path);
		
		try {
			Iterator<String> incomeCatIter = incomeCategories.iterator();
			while(incomeCatIter.hasNext()) {
				String cat = incomeCatIter.next();
				
				bw.write(String.format("%s\t", cat));
			}
			bw.newLine();
			
			Iterator<String> expenseCatIter = expenseCategories.iterator();
			while(expenseCatIter.hasNext()) {
				String cat = expenseCatIter.next();
				
				bw.write(String.format("%s\t", cat));
			}
			bw.newLine();
			
			Iterator<Entry> iter = entryItems.iterator();
			while(iter.hasNext()) {
				Entry item = iter.next();
				
				bw.write(String.format("%s\t%s\t%s\t%s\t%s",
						item.getAmount().substring(1),
						item.getDescription(),
						item.getDate().format(FORMATTER),
						item.getClass().getName(),
						item.getCategory()));
				bw.newLine();
			}
			
		}finally {
			if(bw != null) {
				bw.close();
			}
		}
	}
	
	public void loadEntries() throws IOException {
		entryItems = FXCollections.observableArrayList();
		incomeCategories = FXCollections.observableArrayList();
		expenseCategories = FXCollections.observableArrayList();
		income = 0;
		expense = 0;
		Path path = Paths.get(filename);
		BufferedReader br = null; 
		String input;
		
		try {
			br = Files.newBufferedReader(path);
			
			input = br.readLine();
			String[] inccomeCats = input.split("\t");
			for(String cat : inccomeCats) {
				incomeCategories.add(cat);
			}
			
			input = br.readLine();
			String[] expenseCats = input.split("\t");
			for(String cat : expenseCats) {
				expenseCategories.add(cat);
			}
			
			while((input = br.readLine()) != null) {
				String[] entries = input.split("\t");
				
				double amount = Double.parseDouble(entries[0]);
				String description = entries[1];
				String dateString = entries[2];
				String entryType = entries[3];
				String category = entries[4];
				
				LocalDate date = LocalDate.parse(dateString, FORMATTER);
				
				if(entryType.equals("Expense")) { 
					entryItems.add(new Expense(amount, description, date, category));
					expense += amount;
				}else {
					entryItems.add(new Income(amount, description, date, category));
					income += amount;
				}
			}
			
		}catch(NoSuchFileException e) {
			defaultCategories();
		}finally {
			setNet();
			if(br != null) {
				br.close();
			}
		}
	}
	
	private void defaultCategories() {
		incomeCategories.addAll("Wages", "Gift", "Bonus", "Other");

		expenseCategories.addAll("Groceries", "Rent", "Transportation", "Insurance",
				"Restaurants", "Bills", "Other");

	}
}
