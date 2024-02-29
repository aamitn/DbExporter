package org.nmpl.v0.exporters;

import org.nmpl.v0.DbExporterException;
import org.nmpl.v0.Exportable;

import java.sql.Connection;
public class TestExporter implements Exportable {
    private final Connection connection;

    public TestExporter(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void export(String tableName, String fileName) {
        try {
        System.out.println("TEST EXPORTER\n DB NAME: "+connection.getCatalog());
        } catch (Exception e) {
            throw new DbExporterException("Failed to export table to HTML.",e);
        }
    }
}