import java.time.LocalDate;


public class Income extends Entry {

	public Income(double amount, String description, String category) {
		super(amount, description, "Income", category);
	}

	public Income(double amount, String description, LocalDate date, String category) {
		super(amount, description, date, "Income", category);
	}
	
}
