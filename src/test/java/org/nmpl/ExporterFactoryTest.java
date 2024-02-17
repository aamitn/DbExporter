package org.nmpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.nmpl.exporters.XMLExporter;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class ExporterFactoryTest {

    private Connection connection;

    @Mock
    private Connection mockConnection;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Assuming you have a method in ExporterFactory to set the Connection
        // You may need to modify ExporterFactory to accept Connection as a parameter
        ExporterFactory exporterFactory = new ExporterFactory(mockConnection);
    }

    @Test
    void testGetExporter() {
        ExporterFactory exporterFactory = new ExporterFactory(connection);

        try {
            // Assuming 'XML' is a supported export format in your config.xml
            Exportable exporter = exporterFactory.getExporter("XML");
            assertNotNull(exporter);

            System.out.println("Actual class of exporter: " + exporter.getClass().getName());
            assertTrue(exporter instanceof XMLExporter); // Adjust based on your actual exporter class

        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception occurred while getting exporter");
        }
    }

    @Test
    void testGetSupportedFormats() {
        ExporterFactory exporterFactory = new ExporterFactory(connection);

        List<String> supportedFormats = exporterFactory.getSupportedFormats();

        assertNotNull(supportedFormats);
        assertFalse(supportedFormats.isEmpty());
        assertTrue(supportedFormats.contains("XML")); // Adjust based on your actual supported formats
    }
}
