package org.nmpl.v1.exporters;


import org.nmpl.v1.Configurable;
import org.nmpl.v1.Exportable;

import java.sql.Connection;


public class TestExporter implements Exportable, Configurable {
    private final Connection connection;
    public TestExporter() {
        this.connection = null;
    }
    public TestExporter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void export(String tableName, String fileName) {
        try {
        System.out.println("TEST EXPORTER");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to export table to HTML.");
        }
    }
    @Override
    public String getExportType() {
        return "TEST";
    }
}