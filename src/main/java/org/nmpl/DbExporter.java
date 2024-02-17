package org.nmpl;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DbExporter extends JFrame {
    protected final JComboBox<String> exportFormatComboBox;
    protected final JList<String> tableList;
    private final DefaultListModel<String> tableListModel;
    private final Connection connection;

    public DbExporter(String dbName,String username,String pasword) throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName, username, pasword);
        setTitle("Database Manager");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create components
        tableListModel = new DefaultListModel<>();
        tableList = new JList<>(tableListModel);
        JButton showButton = new JButton("Show Tables");

        ExporterFactory ef;
        try {
            ef = new ExporterFactory(connection);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<String> formatList = new ArrayList<>(ef.getSupportedFormats());

        exportFormatComboBox = new JComboBox<>(formatList.toArray(new String[0]));
        JButton exportButton = new JButton("Export Table");

        // Create panels for layout
        JPanel topPanel = new JPanel();
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Set a vertical BoxLayout for the top panel
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Add components to the top panel
        topPanel.add(showButton);
        topPanel.add(exportFormatComboBox);
        topPanel.add(exportButton);

        mainPanel.add(new JScrollPane(tableList), BorderLayout.CENTER);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Add mainPanel to the frame
        add(mainPanel);

        // Event handlers
        showButton.addActionListener(e -> showTables());
        exportButton.addActionListener(e -> exportTable());
    }

    protected void showTables() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            String catalog = connection.getCatalog();
            ResultSet tables = metaData.getTables(catalog, null, null, new String[]{"TABLE"});

            tableListModel.clear();

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                tableListModel.addElement(tableName);
            }

            tables.close();
        } catch (SQLException e) {
            String error = "Failed to connect to the database or fetch tables.";
            JOptionPane.showMessageDialog(this, error);
            throw new DbExporterException(error, e);
        }
    }

    protected void exportTable() {
        String selectedTable = tableList.getSelectedValue();
        if (selectedTable == null) {
            JOptionPane.showMessageDialog(this, "Please select a table to export.");
            return;
        }

        String exportFormat = Objects.requireNonNull(exportFormatComboBox.getSelectedItem()).toString();
        String fileName = selectedTable + "." + exportFormat.toLowerCase();

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save " + exportFormat + " File");
        fileChooser.setSelectedFile(new File(fileName));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            fileName = selectedFile.getAbsolutePath();

            try {
                ExporterFactory exportFactory = new ExporterFactory(connection);
                Exportable exporter = exportFactory.getExporter(exportFormat);
                exporter.export(selectedTable, fileName);
                JOptionPane.showMessageDialog(this, "Table data exported to " + fileName);
            } catch (Exception e) {
                String error = "Failed to export table.";
                JOptionPane.showMessageDialog(this, error);
                throw new DbExporterException(error, e);
            }
        }
    }
}
