package org.solq.dht.test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.h2.tools.Csv;
import org.h2.tools.Server;
import org.h2.tools.SimpleResultSet;
import org.junit.Test;

//http://topmanopensource.iteye.com/category/58113
public class H2Demo {
	private Server server;

	private static String port = "8082";
	// 内存模式 关闭连接后数据丢失
	private static String sourceURL1 = "jdbc:h2:mem:h2db";
	// 远程内存模式
	private static String sourceURL2 = "jdbc:h2:tcp://localhost:8082/mem:h2db";

	private static String sourceURL3 = "jdbc:h2:tcp://localhost:8082/~/test";

	private String user = "root";
	private String password = "123456";

	@Test
	public void startServer() throws InterruptedException {
		try {
			System.out.println("正在启动h2...");
			server = Server.createTcpServer(new String[] { "-tcpPort", port }).start();
			System.out.println("启动h2成功.");
		} catch (SQLException e) {
			System.out.println("启动h2出错：" + e.toString());
			e.printStackTrace();
			stopServer();
			throw new RuntimeException(e);
		}
		Thread.sleep(500000);
	}

	void stopServer() {
		if (server != null) {
			System.out.println("正在关闭h2...");
			server.stop();
			System.out.println("关闭成功.");
		}
	}

	@Test
	public void men() {
		try {
			Class.forName("org.h2.Driver");
			Connection conn = DriverManager.getConnection(sourceURL1, user, password);
			Statement stat = conn.createStatement();
			// insert data
			stat.execute("CREATE MEMORY Table TEST(NAME VARCHAR)");
			stat.execute("INSERT INTO TEST VALUES('Hello World')");
			// stat.execute("delete mappedURL");

			// use data
			ResultSet result = stat.executeQuery("select name from test ");
			int i = 1;
			while (result.next()) {
				System.out.println(i++ + ":" + result.getString("name"));
			}
			result.close();
			stat.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void client() throws SQLException, ClassNotFoundException {

		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(sourceURL3, user, password);
		Statement stat = conn.createStatement();
		stat.execute("CREATE MEMORY Table TEST(NAME VARCHAR)");
		stat.execute("INSERT INTO TEST VALUES('Hello World')");
		// use data
		ResultSet result = stat.executeQuery("select name from test");
		int i = 1;
		while (result.next()) {
			System.out.println(i++ + ":" + result.getString("name"));
		}
		result.close();
		stat.close();
		conn.close();

	}

	@Test
	public void clientSelect() throws SQLException, ClassNotFoundException {

		Class.forName("org.h2.Driver");
		Connection conn = DriverManager.getConnection(sourceURL3, user, password);
		Statement stat = conn.createStatement();
		stat.execute("INSERT INTO TEST VALUES('Hello World')");

		// use data
		ResultSet result = stat.executeQuery("select name from test");
		int i = 1;
		while (result.next()) {
			System.out.println(i++ + ":" + result.getString("name"));
		}
		result.close();
		stat.close();
		conn.close();

	}

	@Test
	public void pressure() throws ClassNotFoundException, SQLException, InterruptedException {
		int count = 1000;
		Connection[] connections = new Connection[count];

		long start = System.currentTimeMillis();

		for (int i = 0; i < count; i++) {
			connections[i] = DriverManager.getConnection(sourceURL3, user, password);
		}
		long end = System.currentTimeMillis();
		System.out.println((end - start));

		Thread.sleep(10000);
		start = System.currentTimeMillis();
		for (int i = 0; i < count; i++) {
			connections[i].close();
		}
		end = System.currentTimeMillis();
		System.out.println((end - start));

	}

	@Test
	public void writeCsv() throws SQLException, ClassNotFoundException {

		SimpleResultSet rs = new SimpleResultSet();
		rs.addColumn("NAME", Types.VARCHAR, 255, 0);
		rs.addColumn("EMAIL", Types.VARCHAR, 255, 0);

		rs.addRow("Bob Meier", "bob.meier@abcde.abc");
		rs.addRow("John Jones", "john.jones@abcde.abc");
		new Csv().write("data/test.csv", rs, null);

	}

	@Test
	public void readCsv() throws Exception {
		ResultSet rs = new Csv().read("data/test.csv", null, null);
		ResultSetMetaData meta = rs.getMetaData();
		while (rs.next()) {
			for (int i = 0; i < meta.getColumnCount(); i++) {
				System.out.println(meta.getColumnLabel(i + 1) + ": " + rs.getString(i + 1));
			}
			System.out.println();
		}
		rs.close();
	}
}