package org.nmpl;

import org.reflections.Reflections;

import java.sql.Connection;
import java.util.*;

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
            String exporterPackage = "org.nmpl.exporters";
            Reflections reflections = new Reflections(exporterPackage);
            Set<Class<? extends Exportable>> exporterClasses = reflections.getSubTypesOf(Exportable.class);

            for (Class<? extends Exportable> exporterClass : exporterClasses) {
                String format = getExportFormat(exporterClass);
                if (format != null) {
                    exporterClassNames.put(format, exporterClass.getName());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load exporter configuration: " + e.getMessage(), e);
        }
    }

    private String getExportFormat(Class<? extends Exportable> exporterClass) {
        ExportType annotation = exporterClass.getAnnotation(ExportType.class);
        if (annotation != null) {
            return annotation.value();
        }
        return null;
    }

    public Exportable getExporter(String format) throws Exception {
        String className = exporterClassNames.get(format);
        if (className == null) {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }
        return (Exportable)  Class.forName(className).getDeclaredConstructor(Connection.class).newInstance(connection);
    }

    public List<String> getSupportedFormats() {
        return new ArrayList<>(exporterClassNames.keySet());
    }
}