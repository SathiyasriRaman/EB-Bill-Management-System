import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class EBillGUI extends JFrame {

    JTextField idField, nameField, unitsField, dateField, searchField;
    JButton addButton, viewButton, searchButton, exportButton;

    public EBillGUI() {
        setTitle("EB Bill System");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set layout
        setLayout(new GridLayout(8, 2, 10, 10));

        // Create input fields
        idField = new JTextField(15);
        nameField = new JTextField(15);
        unitsField = new JTextField(15);
        dateField = new JTextField(15);
        searchField = new JTextField(15);

        // Create buttons
        addButton = new JButton("Add Bill");
        viewButton = new JButton("View Bills");
        searchButton = new JButton("Search Bill");
        exportButton = new JButton("Export Bill");

        // Add components to frame
        add(new JLabel("Consumer ID:")); add(idField);
        add(new JLabel("Name:"));        add(nameField);
        add(new JLabel("Units:"));       add(unitsField);
        add(new JLabel("Date (YYYY-MM-DD):")); add(dateField);
        add(addButton); add(viewButton);
        add(new JLabel("Search by Name:")); add(searchField);
        add(searchButton); add(exportButton);

        // Add actions
        addButton.addActionListener(e -> addBill());
        viewButton.addActionListener(e -> viewBills());
        searchButton.addActionListener(e -> searchBill());
        exportButton.addActionListener(e -> exportBill());

        setVisible(true);
    }

    void addBill() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("../ebills.txt", true))) {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            int units = Integer.parseInt(unitsField.getText().trim());
            String date = dateField.getText().trim();
            double amount = calculateBill(units);

            bw.write(id + "|" + name + "|" + units + "|" + amount + "|" + date + "\n");
            JOptionPane.showMessageDialog(this, "Bill Added Successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    double calculateBill(int units) {
        if (units <= 100) return units * 1.5;
        else if (units <= 300) return 100 * 1.5 + (units - 100) * 2.5;
        else if (units <= 500) return 100 * 1.5 + 200 * 2.5 + (units - 300) * 4;
        else return 100 * 1.5 + 200 * 2.5 + 200 * 4 + (units - 500) * 6;
    }

    void viewBills() {
        try (BufferedReader br = new BufferedReader(new FileReader("../ebills.txt"))) {
            JTextArea area = new JTextArea(15, 50);
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            area.append("ID\tName\tUnits\tAmount\tDate\n");
            area.append("----------------------------------------------------------\n");

            String line;
            while ((line = br.readLine()) != null) {
                area.append(line.replace('|', '\t') + "\n");
            }

            JOptionPane.showMessageDialog(this, new JScrollPane(area), "View Bills", JOptionPane.PLAIN_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
        }
    }

    void searchBill() {
        String searchName = searchField.getText().trim().toLowerCase();
        if (searchName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name to search.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader("../ebills.txt"))) {
            JTextArea area = new JTextArea(10, 40);
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            area.append("ID\tName\tUnits\tAmount\tDate\n");
            area.append("----------------------------------------------------------\n");

            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                if (line.toLowerCase().contains(searchName)) {
                    area.append(line.replace('|', '\t') + "\n");
                    found = true;
                }
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "No records found for: " + searchName);
            } else {
                JOptionPane.showMessageDialog(this, new JScrollPane(area), "Search Result", JOptionPane.PLAIN_MESSAGE);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + ex.getMessage());
        }
    }

    void exportBill() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String unitsText = unitsField.getText().trim();
        String date = dateField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || unitsText.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields to export.");
            return;
        }

        try {
            int units = Integer.parseInt(unitsText);
            double amount = calculateBill(units);

            String filename = "bill_" + id + ".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            writer.write("Electricity Bill\n");
            writer.write("====================\n");
            writer.write("Consumer ID: " + id + "\n");
            writer.write("Name       : " + name + "\n");
            writer.write("Units      : " + units + "\n");
            writer.write("Amount     : ₹" + amount + "\n");
            writer.write("Date       : " + date + "\n");

            writer.close();
            JOptionPane.showMessageDialog(this, "Bill exported successfully to " + filename);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error exporting bill: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new EBillGUI();
    }
}
