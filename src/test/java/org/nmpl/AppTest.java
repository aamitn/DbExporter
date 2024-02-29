package org.nmpl;

import org.fest.swing.fixture.FrameFixture;
import org.junit.jupiter.api.Test;
import org.nmpl.v0.DbExporter;

import javax.swing.*;
import java.sql.SQLException;

import static org.fest.swing.core.matcher.JButtonMatcher.withText;
import static org.junit.jupiter.api.Assertions.assertTrue;


class AppTest {

    private FrameFixture frame;

    @Test
    void setUp()  {
        final String dbName = "testDb";
        final String username = "root";
        final String password = "password";
        SwingUtilities.invokeLater(() -> {
            DbExporter app = null;
            try {
                 app = new DbExporter(dbName, username, password);
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            finally{
                frame = new FrameFixture(app);
                frame.show();
                frame.button(withText("Show Tables")).click();
                assertTrue(frame.table().rowCount() >= 2); // Assuming tables are populated
            }
        });
    }

}
