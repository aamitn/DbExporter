package org.nmpl.v0;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExporterFactory {
    private final Map<String, String> exporterClassNames;
    private final Connection connection;

    public ExporterFactory(Connection connection) {
        this.connection = connection;
        exporterClassNames = new HashMap<>();
        loadExporterConfig();
    }

    private void loadExporterConfig() {
        try {
            // Parse the XML configuration file
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(getClass().getClassLoader().getResourceAsStream("config.xml"));

            doc.getDocumentElement().normalize();

            // Read exporter elements from the XML
            NodeList nodeList = doc.getElementsByTagName("exporter");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String type = element.getElementsByTagName("type").item(0).getTextContent();
                    String className = element.getElementsByTagName("class").item(0).getTextContent();
                    exporterClassNames.put(type, className);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load exporter configuration: " + e.getMessage(), e);
        }
    }

    public Exportable getExporter(String format) {
        String className = exporterClassNames.get(format);
        if (className == null) {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }
        try {
            return (Exportable) Class.forName(className).getDeclaredConstructor(Connection.class).newInstance(connection);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create exporter instance", e);
        }
    }

    public List<String> getSupportedFormats() {
        return new ArrayList<>(exporterClassNames.keySet());
    }
}