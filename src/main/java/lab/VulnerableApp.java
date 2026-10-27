package lab;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/** Intentionally insecure scanner fixture. Use only in an isolated test labs. */
public class VulnerableApp {
    private static final Logger LOG = LogManager.getLogger(VulnerableApp.class);

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println("Usage: VulnerableApp <jdbc-url> <db-user> <db-password> <username>");
            return;
        }
        String username = args[3]; // Externally controlled input.
        LOG.error("Looking up user: {}", username); // Untrusted text reaches old Log4j.
        try (Connection conn = DriverManager.getConnection(args[0], args[1], args[2])) {
            findUser(conn, username);
        }
    }

    public static void findUser(Connection conn, String username) throws Exception {
        // CWE-89: user input concatenated directly into executable SQL.
        String sql = "SELECT id, username FROM users WHERE username = '" + username + "'";
        try (Statement stmt = conn.createStatement();
             ResultSet rows = stmt.executeQuery(sql)) {
            while (rows.next()) {
                System.out.println(rows.getInt("id") + ": " + rows.getString("username"));
            }
        }
    }
}
