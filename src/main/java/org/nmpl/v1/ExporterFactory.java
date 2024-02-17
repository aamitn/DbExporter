package org.nmpl.v1;

import org.reflections.Reflections;

import java.sql.Connection;
import java.util.*;

public class ExporterFactory {
    private final Map<String, String> exporterClassNames;
    private final Connection connection;

    public ExporterFactory(Connection connection) {
        this.connection = connection;
        exporterClassNames =  loadExporterConfig();
    }

    private Map<String, String>  loadExporterConfig() {
        Map<String, String> exporterClassNames = new HashMap<>();
        try {
            String exporterPackage = "org.nmpl.v1.exporters";
            Reflections reflections = new Reflections(exporterPackage);
            Set<Class<? extends Exportable>> exporterClasses = reflections.getSubTypesOf(Exportable.class);

            for (Class<? extends Exportable> exporterClass : exporterClasses) {
                if (Configurable.class.isAssignableFrom(exporterClass)) {
                    String format = ((Configurable) exporterClass.getDeclaredConstructor().newInstance()).getExportType();
                    if (format != null) {
                        exporterClassNames.put(format, exporterClass.getName());
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load exporter configuration: " + e.getMessage(), e);
        }
        return exporterClassNames;
    }


    public Exportable getExporter(String format) throws Exception {
        String className = exporterClassNames.get(format);
        if (className == null) {
            throw new IllegalArgumentException("Unsupported export format: " + format);
        }
        return (Exportable) Class.forName(className).getDeclaredConstructor(Connection.class).newInstance(connection);
    }

    public List<String> getSupportedFormats() {
        List<String> arl = new ArrayList<>(exporterClassNames.keySet());
        Collections.sort(arl);
        return arl;
    }
}