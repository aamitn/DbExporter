package org.nmpl.v2.exporters;


import org.nmpl.v2.ExportType;
import org.nmpl.v2.Exportable;

import java.sql.Connection;

@ExportType("TEST")
public class TestExporter implements Exportable {
    private final Connection connection;

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
}