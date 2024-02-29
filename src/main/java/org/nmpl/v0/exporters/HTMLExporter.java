package org.nmpl.v0.exporters;

import org.nmpl.v0.Exportable;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class HTMLExporter implements Exportable {
    private final Connection connection;

    public HTMLExporter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void export(String tableName, String fileName) {
        try {
            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<table border=\"1\">"); // Add border attribute to create table borders
            htmlContent.append(System.lineSeparator());

            // Fetch the data from the database table
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Generate table header
            htmlContent.append("<tr>").append(System.lineSeparator());
            for (int i = 1; i <= columnCount; i++) {
                htmlContent.append("  <th>").append(metaData.getColumnName(i)).append("</th>").append(System.lineSeparator());
            }
            htmlContent.append("</tr>").append(System.lineSeparator());

            // Generate table data
            while (resultSet.next()) {
                htmlContent.append("<tr>").append(System.lineSeparator());
                for (int i = 1; i <= columnCount; i++) {
                    htmlContent.append("  <td>").append(resultSet.getString(i)).append("</td>").append(System.lineSeparator());
                }
                htmlContent.append("</tr>").append(System.lineSeparator());
            }

            htmlContent.append("</table");

            // Save HTML content to the specified file
            try (FileWriter fileWriter = new FileWriter(fileName)) {
                fileWriter.write(htmlContent.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to export table to HTML.");
        }
    }
}