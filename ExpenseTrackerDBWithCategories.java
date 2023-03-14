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


        // Hana's part
        // Initialize table

        tableModel = new DefaultTableModel(new Object[]{"ID", "Amount", "Date", "Category"}, 0);
        try {
            ResultSet rs = stmt.executeQuery("SELECT * FROM expenses");

            while (rs.next()) {
                int id = rs.getInt("id");
                double amount = rs.getDouble("amount");
                String date = rs.getString("date");
                String category = rs.getString("category");
                tableModel.addRow(new Object[]{id, amount, date, category});
            }

            rs.close();
        } catch (SQLException ex) {
            System.err.println("Database error: " + ex.getMessage());
        }

        expensesTable = new JTable(tableModel);
        
        //Imene
              // Add UI components to container
        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(amountLabel);
        inputPanel.add(amountTextField);
        inputPanel.add(dateLabel);
        inputPanel.add(dateTextField);
        inputPanel.add(categoryLabel);
        inputPanel.add(categoryComboBox);
        inputPanel.add(addButton);
        inputPanel.add(deleteButton);

        c.add(inputPanel, BorderLayout.NORTH);
        c.add(new JScrollPane(expensesTable), BorderLayout.CENTER);

        // Register event listeners
        addButton.addActionListener(this);
        deleteButton.addActionListener(this);

        // Set window properties
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Expense Tracker");
        setVisible(true);
    }


    // Abdenour's part 
    // ActionListener method
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            // Get input values
            double amount = Double.parseDouble(amountTextField.getText());
            String date = dateTextField.getText();
            String category = (String) categoryComboBox.getSelectedItem();

            // Insert into database
           
            try {
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO expenses (amount, date, category) VALUES (?, ?, ?)");
                stmt.setDouble(1, amount);
                stmt.setString(2, date);
                stmt.setString(3, category);
                stmt.executeUpdate();
                            // Add to table
            int id = getLastInsertedId();
            tableModel.addRow(new Object[]{id, amount, date, category});

            // Clear input fields
            amountTextField.setText("");
            dateTextField.setText("");
        } catch (SQLException ex) {
            System.err.println("Database error: " + ex.getMessage());
        }
    } else if (e.getSource() == deleteButton) {
        // Get selected row index
        int rowIndex = expensesTable.getSelectedRow();

        if (rowIndex != -1) {
            // Get expense ID from table model
            int id = (int) tableModel.getValueAt(rowIndex, 0);

            // Delete from database
            try {
                PreparedStatement stmt = conn.prepareStatement("DELETE FROM expenses WHERE id = ?");
                stmt.setInt(1, id);
                stmt.executeUpdate();

                // Remove from table
                tableModel.removeRow(rowIndex);
            } catch (SQLException ex) {
                System.err.println("Database error: " + ex.getMessage());
            }
        }
    }
}
}


       


