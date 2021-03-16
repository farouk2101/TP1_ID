package main;

import com.sun.org.apache.xalan.internal.xsltc.trax.XSLTCSource;

import java.sql.*;
import java.util.Scanner;

public class Main2 {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:hsqldb:file:testdb;shutdown:true";

    // Database credentials
    static final String USER = "username";
    static final String PASS = "password";
    private static Statement stmt;
    private static Connection conn;
    private static Wrapper wrapper = new Wrapper();

    public static void main(String[] args) throws Exception {
        wrapper.parse("velib.csv");

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
//        diplay_columnsName();

        // Fermeture de la base de données et sauvegarde de ces données
        close_and_save_database();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Appuyez sur entrer");
        String word = scanner.nextLine();

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

        String sql = "CREATE TABLE VELIB (";
        for(int i = 0; i < wrapper.getColumns().size(); i++){
            sql = sql + createStringDBColumn(i);
        }

//        System.out.println(sql);

        stmt.executeUpdate(sql);
        System.out.println("Table created in given database.");
    }

    private static void insert_values() throws Exception {
        System.out.println("Inserting records into the table...");
        stmt = conn.createStatement();


        for(int j = 0; j < wrapper.getColumns().get(1).getItems().size(); j++){
//            System.out.println("Numero de la ligne : " + wrapper.getColumns().get(1).getItems().size() + " ====================================================================");
            String sql = "INSERT INTO VELIB ";
            sql = sql + "VALUES (" + wrapper.getColumns().get(0).getItems().get(j) + ", " +
                    "'" + wrapper.getColumns().get(1).getItems().get(j) + "'" + ", "  +
                    "'" +wrapper.getColumns().get(2).getItems().get(j) + "'" + ", " +
                    wrapper.getColumns().get(3).getItems().get(j) + ", " +
                    wrapper.getColumns().get(4).getItems().get(j) + ", " +
                    "'" +wrapper.getColumns().get(5).getItems().get(j) + "'"  + ", " +
                    wrapper.getColumns().get(6).getItems().get(j)+ ", " +
                    wrapper.getColumns().get(7).getItems().get(j) + ", " +
                    "'" +wrapper.getColumns().get(8).getItems().get(j) + "'" +
                    ")";
            System.out.println(sql); // verification
            stmt.executeUpdate(sql);
        }


        System.out.println("Inserted records into the table...");
    }

    private static void diplay_columnsName() throws Exception{
        String sql = "SELECT * FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME =  N'VELIB'";
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()){
            System.out.printf(rs.getString("TABLE_NAME"));
        }
    }

    private static void display_values() throws Exception {
        int compteur= 0;
        System.out.println("Displaying values...");
        stmt = conn.createStatement();

        String sql = "SELECT number, name, address, dept, cp, ville, latitude, longitude, wgs84 FROM VELIB";
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println();

        while (rs.next()) {

            // Retrieve by column name
            String number = rs.getString("number");
            String name = rs.getString("name");
            String address = rs.getString("address");
            String dept = rs.getString("dept");
            String cp = rs.getString("cp");
            String ville = rs.getString("ville");
            String latitude = rs.getString("latitude");
            String longitude = rs.getString("longitude");
            String wgs84 = rs.getString("wgs84");

            // Display values
            System.out.println("number : " + number);
            System.out.println("name : " + name);
            System.out.println("address : " + address);
            System.out.println("dept : " + dept);
            System.out.println("cp : " + cp);
            System.out.println("ville : " + ville);
            System.out.println("latitude : " + latitude) ;
            System.out.println("longitude : " + longitude);
            System.out.println("wgs84 : " + wgs84);
            System.out.println();

        }

        rs.close();
    }

    /*private static void update_values() throws Exception {
        System.out.println("Updating values...");
        stmt = conn.createStatement();
        String sql = "UPDATE Registration "
                + "SET age = 30 WHERE id in (100, 101)";
        stmt.executeUpdate(sql);
        System.out.println("Values updated.");
    }*/

    /*private static void delete_values() throws Exception {
        System.out.println("Deleting some data...");
        stmt = conn.createStatement();
        String sql = "DELETE FROM Registration " + "WHERE id = 101";
        stmt.executeUpdate(sql);
        System.out.println("Data deleted.");
    }*/

    private static void close_and_save_database() throws Exception {
        // Close connexion to the database and save changes
        System.out.println("Closing database ...");
        Statement statement = conn.createStatement();
        statement.executeQuery("SHUTDOWN");
        statement.close();
        conn.close();
        System.out.println("Database closed and changes saved!");
    }


    private static String createStringDBColumn(int i){
        String columString = "";
        if(wrapper.getColumns().get(i).getType() .equals(Type.INTEGER)) {
            columString = wrapper.getColumns().get(i).getName() + " " + wrapper.getColumns().get(i).getType();
        }
        else if(wrapper.getColumns().get(i).getType() .equals(Type.DECIMAL)){
            columString = wrapper.getColumns().get(i).getName() + " " + wrapper.getColumns().get(i).getType() + "(" + wrapper.getColumns().get(i).getMaxSize() + ", " + wrapper.getColumns().get(i).getMaxSize() + ")";
        }
        else{
            columString = wrapper.getColumns().get(i).getName() + " " + wrapper.getColumns().get(i).getType() + "(" + wrapper.getColumns().get(i).getMaxSize() + ")";
        }

        if(wrapper.getColumns().get(i).isNotNull()){
            columString = columString + " NOT NULL";
        }
        if(wrapper.getColumns().get(i).isPrimaryKey()){
            columString = columString + " PRIMARY KEY";
        }
//        System.out.printf("i = " + i + ", wrapper.getColumns().size() = " + wrapper.getColumns().size());
        if(i < wrapper.getColumns().size()-1){
            columString = columString + ", ";
        }
        else{
            columString = columString + ")";
        }
        return columString;
    }

}
