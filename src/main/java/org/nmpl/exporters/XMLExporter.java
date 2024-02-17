package org.nmpl.exporters;

import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.nmpl.Exportable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;

public class XMLExporter implements Exportable {
    private final Connection connection;

    public XMLExporter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void export(String tableName, String fileName) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            Element rootElement = doc.createElement("tableData");
            doc.appendChild(rootElement);

            // Fetch the data from the database table
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Generate XML elements for table data
            while (resultSet.next()) {
                Element row = doc.createElement("row");
                rootElement.appendChild(row);
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    String columnValue = resultSet.getString(i);
                    Element column = doc.createElement(columnName);
                    column.appendChild(doc.createTextNode(columnValue));
                    row.appendChild(column);
                }
            }

            // Configure transformer to format the XML with new lines
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Save the XML content to the specified file
            try (FileOutputStream fileOutputStream = new FileOutputStream(fileName)) {
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(fileOutputStream);
                transformer.transform(source, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to export table to XML.");
        }
    }
}
