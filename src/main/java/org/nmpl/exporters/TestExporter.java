package org.nmpl.exporters;
import org.nmpl.ExportType;
import org.nmpl.Exportable;


@ExportType("TEST")
public class TestExporter implements Exportable {
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