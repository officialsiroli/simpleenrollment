/**
 Course Code: CCINFOM
 Purpose:     Provide Sample of a Java-based Database Application
 Created:     March 2024
 Created By:  Malabanan, Oliver A.
 Disclaimer:  This sample focuses on the interaction of Java with Databases and not
              the implementation of object-orientation
 */
package simpleenrollment;
import java.sql.*;

public class dbconnect {

	/* Declare the necessary variables for database connection */
	public Connection conn;
	
	public dbconnect() {
		try {
		/* Prepare Connection to the Database */
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbenrollment?useTimezone=true&serverTimezone=UTC&user=admin&password=DLSU1234");
        System.out.println("DB Connection to MYSQL established. Connected DB: dbenrollment");
		} catch (Exception e) {
	        System.out.println("An error happened connecting to the DB");
			System.out.println(e.getMessage());
		}
	}
	
	public void disconnect() {
		try {
			conn.close();
			System.out.println("Closing connection to the DB");
		} catch (Exception e) {
	        System.out.println("An error happened closing the connection to the DB");
			System.out.println(e.getMessage());			
		}
	}
	
	public static void main(String[] args) {
		dbconnect db = new dbconnect();
	}

}
