import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Entry {

	private double amount;
	private String description;
	private String category;
	private LocalDate date;
	private String dateFormatted;
	private String type;
	
	private final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy");

	
	public Entry(double amount, String description, String type, String category) {
		this.amount = amount;
		this.description = description;
		this.type = type;
		this.category = category;
		date = LocalDate.now();
		dateFormatted = date.format(FORMATTER);
	}

	public Entry(double amount, String description, LocalDate date, String type, String category) {
		this.amount = amount;
		this.description = description;
		this.type = type;
		this.date = date;
		this.category = category;
		dateFormatted = date.format(FORMATTER);
	}
	public String getAmount() {
		return String.format("$%.2f", amount);
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public DateTimeFormatter getFormatter() {
		return FORMATTER;
	}
	
	public String getDateFormatted() {
		return dateFormatted;
	}
	
	public String getCategory() {
		return category;
	}
	
	public String getType() {
		return type;
	}
}
