import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class ExpenseTrackerDBWithCategories extends JFrame implements ActionListener {
    private JLabel amountLabel, dateLabel, categoryLabel;
    private JTextField amountTextField, dateTextField;
    private JComboBox<String> categoryComboBox;
    private JButton addButton, deleteButton;
    private JTable expensesTable;
    private DefaultTableModel tableModel;

    private Connection conn;
    private Statement stmt;

    private Vector<String> categories;

    public ExpenseTrackerDBWithCategories() {
        // Initialize database connection and statement
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:expenses.db");
            stmt = conn.createStatement();

            // Create expenses table if it doesn't exist
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS expenses (id INTEGER PRIMARY KEY, amount REAL, date TEXT, category TEXT)");
            // Add amount column if it doesn't exist
            stmt.executeUpdate("ALTER TABLE expenses ADD COLUMN IF NOT EXISTS amount REAL");

        } catch (ClassNotFoundException ex) {
            System.err.println("Failed to load SQLite JDBC driver");
        } catch (SQLException ex) {
            System.err.println("Database error: " + ex.getMessage());
        }
// maha part

        // Initialize UI components
        amountLabel = new JLabel("Amount:");
        dateLabel = new JLabel("Date (YYYY-MM-DD):");
        categoryLabel = new JLabel("Category:");

        amountTextField = new JTextField(10);
        dateTextField = new JTextField(10);

        categories = new Vector<>();
        categories.add("Food");
        categories.add("Transportation");
        categories.add("Entertainment");
        categories.add("Other");
        categoryComboBox = new JComboBox<>(categories);

        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");


