package org.nmpl;

import javax.swing.*;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DbExporter manager;
            try {
                manager = new DbExporter();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
            manager.setVisible(true);
        });
    }
}
