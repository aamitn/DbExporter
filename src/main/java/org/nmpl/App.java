package org.nmpl;

import javax.swing.*;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) {
         final String dbName = "shortener";
         final String username = "root";
         final String pasword = "1234qwer";

        SwingUtilities.invokeLater(() -> {

            DbExporter manager;
            try {
                manager = new DbExporter(dbName,username,pasword);
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
            manager.setVisible(true);
        });
    }
}
