import java.time.LocalDate;

public class Expense extends Entry { 
	
	public Expense(double amount, String description, String category) {
		super(amount, description, "Expense", category);
	}

	public Expense(double amount, String description, LocalDate date, String category) {
		super(amount, description, date, "Expense", category);
	}

}
