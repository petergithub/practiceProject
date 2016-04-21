package org.pu.utils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * get all table names 
 * --- If you are using Microsoft SQL Server you can use one of these queries.
 * SELECT * FROM sys.tables or SELECT * FROM sysobjects WHERE xtype = 'U' 
 * --- If you are using Oracle, you can use one of these: 
 * SELECT * FROM all_tables; or SELECT * FROM all_objects WHERE object_type = 'TABLE';
 * 
 * @author Shang Pu
 * @version Date: Dec 28, 2012 5:13:09 PM
 */
public class JdbcUtils {
	private final static Logger log = LoggerFactory.getLogger(JdbcUtils.class);

	
	@SuppressWarnings("unused")
	public void testDbconnection() {
		Connection conn = null;
		ResultSet rs = null;
		Statement stmt = null;
		try {
			// oracle
			// jdbc:oracle:driver_type:[username/password]@database_specifier
			String oracleDriver = "oracle.jdbc.driver.OracleDriver";
			String oracleUrl = "jdbc:oracle:thin:@127.0.0.1:1521:oracle";

			// mysql
			String mysqlDriver = "com.mysql.jdbc.Driver";
			String mysqlUrl = "jdbc:mysql://localhost:3306/<DatabaseName>?&characterEncoding=GBK";

			// SQL server 2000
			String sqlServer2000Driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
			String sqlServer2000Url = "jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=<DatabaseName>";

			// SQL server 2005
			String sqlServer2005Driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
			String sqlServer2005Url = "jdbc:sqlserver://localhost:1433;DatabaseName=<DatabaseName>";

			// Microsoft Access
			String accessDriver = "sun.jdbc.odbc.JdbcOdbcDriver";
			String accessUrl = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=<file.mdb>";
			// e.g.
			// "jdbc:odbc:driver={Microsoft Access Driver (*.mdb)};DBQ=<file.mdb>C:\\count.mdb";

			// hsqldb: in-memory and disk-based tables
			String hsqldbDriver = "org.hsqldb.jdbcDriver";
			String hsqldbUrl = "jdbc:hsqldb:<DatabaseName>";

			// h2 db: in-memory and disk-based tables
			String h2Driver = "org.h2.Driver";
			String h2Url = "jdbc:h2:~/test";
			// String h2Url = "jdbc:h2:mem::testh2";

			String driver = hsqldbDriver;
			String url = "jdbc:hsqldb:testhsql";
			String userName = "sa";
			String password = "";
			// 1. 注册驱动
			// 2. 获取数据库的连接
			conn = JdbcUtils.getConnection(driver, url, userName, password);

			// 3. 获取表达式
			stmt = conn.createStatement();

			// 4. 执行 SQL
			displayDbProperties(conn);
			// String sql = "show databases";
			String sql = "select username from T_USER";
			rs = stmt.executeQuery(sql);

			// 5. 显示结果集里面的数据
			while (rs.next()) {
				// log.info("SCHEMA_NAME: " + rs.getString("SCHEMA_NAME"));
				log.info("username: " + rs.getString("username"));
			}
		} catch (SQLException e) {
			log.error("SQLException in testDbconnection()", e);
		} finally {
			// 6. 释放资源
			JdbcUtils.close(conn, stmt, rs);
		}
	}

	/*
	 * Display the driver properties, database details
	 */
	public static void displayDbProperties(Connection conn) {
		DatabaseMetaData dm = null;
		ResultSet rs = null;
		try {
			if (conn != null) {
				dm = conn.getMetaData();
				log.info("Driver Information");
				log.info("\tDriver Name: " + dm.getDriverName());
				log.info("\tDriver Version: " + dm.getDriverVersion());
				log.info("Database Information ");
				log.info("\tDatabase Name: " + dm.getDatabaseProductName());
				log.info("\tDatabase Version: " + dm.getDatabaseProductVersion());
				log.info("Available Catalogs ");
				rs = dm.getCatalogs();
				while (rs.next()) {
					log.info("\tcatalog: " + rs.getString(1));
				}
			} else
				log.info("Error: No active Connection");
		} catch (SQLException e) {
			log.error("Exception in displayDbProperties()", e);
		}
	}

	public static Connection getConnection(Properties prop) {
		return getConnection(prop.getProperty("jdbc.driver"), prop.getProperty("jdbc.url"),
				prop.getProperty("jdbc.username"), prop.getProperty("jdbc.password"));
	}

	public static Connection getConnection(String url, String userName, String password, String driver) {
		Connection conn = null;
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, password);
			if (conn != null) log.info("Connect {} Successful!", url);
		} catch (SQLException e) {
			log.error("SQLException in getConnection()", e);
		} catch (ClassNotFoundException e) {
			log.error("ClassNotFoundException in getConnection()", e);
		}
		return conn;
	}

	/**
	 * check to see if the connection is closed
	 */
	public static boolean isClosed(Connection oConn) {
		boolean bIsClosed = false;
		try {
			if (null == oConn)
				bIsClosed = true;
			else
				bIsClosed = oConn.isClosed();
		} catch (SQLException e) {
			close(oConn, null, null);
			bIsClosed = true;
		}
		return bIsClosed;
	}

	/**
	 * Close the connection, statement and resultset objects. To close any one of them individually,
	 * just pass in the other objects as null object.
	 */
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		log.debug("Enter close()");
		close(rs);
		close(stmt);
		close(conn);
		log.debug("Exit close()");
	}

	public static void close(Connection conn) {
		try {
			if (null != conn) conn.close();
		} catch (SQLException e) {
			log.error("Exception in close Connection", e);
		}
	}

	public static void close(Statement stmt) {
		try {
			if (null != stmt) stmt.close();
		} catch (SQLException e) {
			log.error("Exception in close Statement", e);
		}
	}

	public static void close(ResultSet rs) {
		try {
			if (null != rs) rs.close();
		} catch (SQLException e) {
			log.error("Exception in close ResultSet", e);
		}
	}

	public void testLdapConn() {
		Connection conn;
		String url = "jdbc:oracle:thin:@ldap://hostname1:389/mdatadev,cn=OracleContext,dc=company,dc=com "
				+ "ldap://hostname2:389/mdatadev,cn=OracleContext,dc=company,dc=com "
				+ "ldap://hostname3:389/mdatadev,cn=OracleContext,dc=company,dc=com";
		conn = getConnection(url, "docbasecomp", "dev#1234", "oracle.jdbc.driver.OracleDriver");
		displayDbProperties(conn);
		close(conn);
	}
	
	@Test
	public void testTnsConn() throws SQLException {
		String path = "C:\\Oracle\\Ora92\\network\\ADMIN\\";
		System.setProperty("oracle.net.tns_admin", path);
		// MDATADEV=(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=hostname)(PORT=1557)))(CONNECT_DATA=(SID=MDATADEV)))
//		String url = "jdbc:oracle:thin:@MDATADEV";
		String url = "jdbc:oracle:thin:@MDATADEV";
		Connection conn = getConnection(url, "projectCOMP", "password", "oracle.jdbc.driver.OracleDriver");
		displayDbProperties(conn);
		close(conn);
	}

	public void testGetDirectConn() throws SQLException {
		String url = "jdbc:oracle:thin:@hostname:1623:projectp2";
		Connection conn = getConnection(url, "project", "company07", "oracle.jdbc.driver.OracleDriver");
		displayDbProperties(conn);
		close(conn);
	}
}
