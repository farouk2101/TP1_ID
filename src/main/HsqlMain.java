package main;

import java.sql.*;

public class HsqlMain {

	// JDBC driver name and database URL
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:hsqldb:file:testdb;shutdown:true";

	// Database credentials
	static final String USER = "username";
	static final String PASS = "password";
	private static Statement stmt;
	private static Connection conn;

	public static void main(String[] args) throws Exception {

		try{
			// Création de la base de données (ne marchera que si elle n'existe pas)
			create_database();
		}catch(Exception e){
			// Connexion à la base de données si elle existe déjà
			connect_database();
		}
		
		
		// Création d'une table dans la base de données (ne marchera pas si elle existe déjà)
		try{
			create_table();
		}catch(Exception e){
			System.out.println("Table already exists.");
		}
		
		try{
			// Insertion de données dans la base
			insert_values();
		}catch(Exception e){
			System.out.println("Data already exists");
		}
		
		// Affichage de certaines données de la base
		display_values();

		// Modification de certaines valeurs
		update_values();

		// Affichage de certaines données pour voir les différences après
		// l'update
		display_values();
		
		// Suppression de certaines valeurs et affichage des changements
		delete_values();
		display_values();
		
		// Fermeture de la base de données et sauvegarde de ces données
		close_and_save_database();

	}

	private static void create_database() throws Exception {
		System.out.println("Creating databse...");
		Class.forName("org.hsqldb.jdbcDriver").newInstance();
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		System.out.println("Database created.");
	}
	
	private static void connect_database() throws Exception {
		System.out.println("Establishing connexion to database...");
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		System.out.println("Connexion established.");
	}

	private static void create_table() throws Exception {
		System.out.println("Creating table in given database...");
		stmt = conn.createStatement();

		String sql = "CREATE TABLE REGISTRATION " + "(id INTEGER not NULL, "
				+ " first VARCHAR(255), " + " last VARCHAR(255), "
				+ " age INTEGER, " + " PRIMARY KEY ( id ))";

		stmt.executeUpdate(sql);
		System.out.println("Table created in given database.");
	}

	private static void insert_values() throws Exception {
		System.out.println("Inserting records into the table...");
		stmt = conn.createStatement();

		String sql = "INSERT INTO Registration "
				+ "VALUES (100, 'Zara', 'Ali', 18)";
		stmt.executeUpdate(sql);
		sql = "INSERT INTO Registration "
				+ "VALUES (101, 'Mahnaz', 'Fatma', 25)";
		stmt.executeUpdate(sql);
		sql = "INSERT INTO Registration " + "VALUES (102, 'Zaid', 'Khan', 30)";
		stmt.executeUpdate(sql);
		sql = "INSERT INTO Registration "
				+ "VALUES(103, 'Sumit', 'Mittal', 28)";
		stmt.executeUpdate(sql);
		System.out.println("Inserted records into the table...");
	}

	private static void display_values() throws Exception {
		System.out.println("Displaying values...");
		stmt = conn.createStatement();

		String sql = "SELECT id, first, last, age FROM Registration";
		ResultSet rs = stmt.executeQuery(sql);

		while (rs.next()) {
			// Retrieve by column name
			int id = rs.getInt("id");
			int age = rs.getInt("age");
			String first = rs.getString("first");
			String last = rs.getString("last");

			// Display values
			System.out.print("ID: " + id);
			System.out.print(", Age: " + age);
			System.out.print(", First: " + first);
			System.out.println(", Last: " + last);
		}
		rs.close();
	}

	private static void update_values() throws Exception {
		System.out.println("Updating values...");
		stmt = conn.createStatement();
		String sql = "UPDATE Registration "
				+ "SET age = 30 WHERE id in (100, 101)";
		stmt.executeUpdate(sql);
		System.out.println("Values updated.");
	}

	private static void delete_values() throws Exception {
		System.out.println("Deleting some data...");
		stmt = conn.createStatement();
		String sql = "DELETE FROM Registration " + "WHERE id = 101";
		stmt.executeUpdate(sql);
		System.out.println("Data deleted.");
	}

	private static void close_and_save_database() throws Exception {
		// Close connexion to the database and save changes
		System.out.println("Closing database ...");
		Statement statement = conn.createStatement();
		statement.executeQuery("SHUTDOWN");
		statement.close();
		conn.close();
		System.out.println("Database closed and changes saved!");
	}

}
