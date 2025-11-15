package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
	private static final String DEFAULT_USER = "root";
	// private static final String DEFAULT_URL = "jdbc:mysql://localhost:3307/convert_file?useSSL=false&serverTimezone=UTC";
	// private static final String DEFAULT_PASS = "123456789";
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/convert_file?useSSL=false&serverTimezone=UTC";
	private static final String DEFAULT_PASS = "";

	public static Connection getConnection() throws SQLException {
		String url = System.getenv().getOrDefault("DB_URL", DEFAULT_URL);
		String user = System.getenv().getOrDefault("DB_USER", DEFAULT_USER);
		String pass = System.getenv().getOrDefault("DB_PASS", DEFAULT_PASS);

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException ignored) {
			// Driver may be provided by the container; ignore if not found here.
		}

		return DriverManager.getConnection(url, user, pass);
	}
}
