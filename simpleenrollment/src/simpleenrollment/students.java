/**
 Course Code: CCINFOM
 Purpose:     Provide Sample of a Java-based Database Application
              Performing Basic Data Operations of
              a. Create New Record
              b. Update a Record
              c. Delete a Record
              d. View a Record
              
 Created:     March 2024
 Created By:  Malabanan, Oliver A.
 Disclaimer:  This sample focuses on the interaction of Java with Databases and not
              the implementation of object-orientation
 */
package simpleenrollment;
import java.util.*;
import java.sql.*;

public class students {

	/* Declare the necessary data items for students */
	
	public int		 studentid;						// mirrors the field studentid in the students table
	public String	 lastname, firstname;			// mirrors the fields lastname and firstname in the students table
	public boolean 	 norecord;						// a status variable if no record was found during operations

	
	public students () {
		/* initialize data item for students */
		studentid = 0;
		lastname  = "";
		firstname = "";
		norecord  = true;
	}

	public void addStudent() {
		
		/* Steps in creating a record in a table in the database
		 * 1. Establish a connection to the DB
		 * 2. Prepare the Statement that will be executed (in this case INSERT statement)
		 * 3. Put into the statement the values from variables
		 * 4. Execute the Statement
		 */
		
		Scanner 			sc = new Scanner(System.in);
		PreparedStatement 	pstmt;				
		try {
			System.out.println("Preparing to add the student record containing the following data");
			System.out.println("Student ID:  " + studentid);
			System.out.println("Last name:   " + lastname);
			System.out.println("First name:  " + firstname);
			System.out.println("Press enter key to save record to the Database");
			sc.nextLine();
				
			dbconnect db = new dbconnect();		/* Check the codes in dbconnect.java for connecting to the database
												 * dbconnect has an attribute conn that will contain the active
												 * connection to the database 
												 */
			
			pstmt = db.conn.prepareStatement("INSERT INTO students (studentid, firstname, lastname) VALUES (?,?,?)");
			/* each ? in the SQL statement will be replaced by the value of the field in succeeding
			 * program statements below. Each ? is designated sequentially as 1, 2, 3.... n 
			 */
			
			pstmt.setInt(1, studentid);
			pstmt.setString(2, firstname);
			pstmt.setString(3, lastname);
			
			pstmt.executeUpdate();				/* Executes the SQL Statement. Is is executeUpdate since it is not
												 * returning any result
												 */
			pstmt.close();						/* It is important to close the prepared statement to release the
												 * memory it is using
												 */
			System.out.println("Student recorded in the Database");			
			db.disconnect();					/* It is important to terminate the connection to the database.
												 * If the program will not terminate the connection, the connection
												 * will remain active and will waste the allowable connection
												 * in the database server
												 */
		} catch (Exception e) {
	        System.out.println("Error occured while adding a Student Record");
	        System.out.println(e.getMessage());	
		}
	}
	
	public void updateStudent() {
		/* Steps in updating a record in a table in the database
		 * 1. Establish a connection to the DB
		 * 2. Prepare the Statement that will be executed (in this case UPDATE statement)
		 * 3. Put into the statement the values from variables
		 * 4. Execute the Statement
		 */
		
		Scanner 			sc = new Scanner(System.in);
		PreparedStatement 	pstmt;
		try {
			System.out.println("Preparing to update the student record containing the following data");
			System.out.println("Student ID:  " + studentid);
			System.out.println("Last name:   " + lastname);
			System.out.println("First name:  " + firstname);
			System.out.println("Press enter key to update record to the Database");
			sc.nextLine();
			
			dbconnect db = new dbconnect();		/* Check the codes in dbconnect.java for connecting to the database
			 									 * dbconnect has an attribute conn that will contain the active
			 									 * connection to the database 
			 									 */
			pstmt = db.conn.prepareStatement("UPDATE students SET firstname=?, lastname=? WHERE studentid=?");
			/* each ? in the SQL statement will be replaced by the value of the field in succeeding
			 * program statements below. Each ? is designated sequentially as 1, 2, 3.... n 
			 */
			
			pstmt.setString(1, firstname);
			pstmt.setString(2, lastname);
			pstmt.setInt(3, studentid);
			
			pstmt.executeUpdate();				/* Executes the SQL Statement. Is is executeUpdate since it is not
			 									 * returning any result
			 									 */
			pstmt.close();						/* It is important to close the prepared statement to release the
			 									 * memory it is using
			 									 */
			System.out.println("Student record updated in the Database");			
			db.disconnect();					/* It is important to terminate the connection to the database.
			 									 * If the program will not terminate the connection, the connection
			 									 * will remain active and will waste the allowable connection
			 									 * in the database server
			 									 */
		} catch (Exception e) {
	        System.out.println("Error occured while updating a Student Record");
	        System.out.println(e.getMessage());	
		}
	}
	
	public void viewStudent() {
		
		/* Steps in retrieving a record in a table in the database
		 * 1. Establish a connection to the DB
		 * 2. Prepare the Statement that will be executed (in this case SELECT statement)
		 * 3. Put into the statement the values from variables
		 * 4. Execute the Statement and Retrieve the results
		 * 5. Put the results data into their respective variables
		 */
		
		Scanner 			sc 		 = new Scanner(System.in);
		PreparedStatement 	pstmt;
		ResultSet			rs;						// ResultSet is a data type to store results coming
													// from database queries
		try {
			norecord = true;
			System.out.println("Retrieving the record of student " + studentid);
		
			dbconnect db = new dbconnect();			/* Check the codes in dbconnect.java for connecting to the database
				 									 * dbconnect has an attribute conn that will contain the active
				 									 * connection to the database 
				 									 */
			pstmt = db.conn.prepareStatement("SELECT studentid, firstname, lastname FROM students WHERE studentid = ?");
			/* each ? in the SQL statement will be replaced by the value of the field in succeeding
			 * program statements below. Each ? is designated sequentially as 1, 2, 3.... n 
			 */
			
			pstmt.setInt(1, studentid);
			rs = pstmt.executeQuery();				/* Executes the SQL Statement. Is is executeQuery since it is 
												     * expected to return a result. The result will be stored in a
												     * resultSet variable rs
												     */
			while (rs.next()) {						/* rs.next() will fetch one record of the result
													 * it returns false if it cannot fetch anything already
													 */			
				norecord = false;
				firstname = rs.getString("firstname");	// Transfers the data from the field "firstname" to the variable
				lastname  = rs.getString("lastname");	// Transfers the data from the field "lastname" to the variable
			}
			
			rs.close();							// it is important to close the resultset to release the memory it used
			pstmt.close();						// it is important to close the prepared statement to release the memorty it used
			db.disconnect();					/* It is important to terminate the connection to the database.
				 								 * If the program will not terminate the connection, the connection
				 								 * will remain active and will waste the allowable connection
				 								 * in the database server
				 								 */
			if (norecord) {
				System.out.println("No student record found.");
			} else {
				System.out.println("Student ID:  " + studentid);
				System.out.println("Last name:   " + lastname);
				System.out.println("First name:  " + firstname);
				System.out.println("---------------------------------------------");
				System.out.println("Record Retrieved");
			}
		} catch (Exception e) {
	        System.out.println("Error occured while retrieving a Student Record");
	        System.out.println(e.getMessage());					
		}	
	}
	
	public void deleteStudent() {
		/* Steps in deleting a record in a table in the database
		 * 1. Establish a connection to the DB
		 * 2. Prepare the Statement that will be executed (in this case DELETE statement)
		 * 3. Put into the statement the values from variables
		 * 4. Execute the Statement
		 */
		Scanner 			sc = new Scanner(System.in);
		PreparedStatement 	pstmt;
		try {
			System.out.println("Preparing to delete the student record containing the following data");
			System.out.println("Student ID:  " + studentid);
			System.out.println("Last name:   " + lastname);
			System.out.println("First name:  " + firstname);
			System.out.println("Press enter key to delete record to the Database");
			sc.nextLine();
		
			dbconnect db = new dbconnect();			/* Check the codes in dbconnect.java for connecting to the database
				 									 * dbconnect has an attribute conn that will contain the active
				 									 * connection to the database 
				 									 */
			pstmt = db.conn.prepareStatement("DELETE FROM students WHERE studentid=?");
			/* each ? in the SQL statement will be replaced by the value of the field in succeeding
			 * program statements below. Each ? is designated sequentially as 1, 2, 3.... n 
			 */
			pstmt.setInt(1, studentid);
			pstmt.executeUpdate();					/* Executes the SQL Statement. Is is executeUpdate since it is not
				 									 * returning any result
				 									 */
			pstmt.close();							/* It is important to close the prepared statement to release the
				 									 * memory it is using
				 									 */
			System.out.println("Student record deleted in the Database");			
			db.disconnect();						/* It is important to terminate the connection to the database.
				 									 * If the program will not terminate the connection, the connection
				 									 * will remain active and will waste the allowable connection
				 									 * in the database server
				 									 */
		} catch (Exception e) {
	        System.out.println("Error occured while deleting a Student Record. It may be being used by other records and cannot be deleted");
	        System.out.println(e.getMessage());	
		}
	}
	
	// the main is only created to test the CRUD functionalities of the class
	// C - Create Record
	// R - Read Record
	// U - Update Record
	// D - Delete Record
	
	public int function() {
		/* this function is to provide an I/O module to facilitate the different record functions
		 * of the class. The function can be called by the MAIN MODULE of the DB Application.
		 */
		Scanner sc 	= new Scanner(System.in);		
		System.out.println ("------------------------------------------------------");
		System.out.println ("Functions available for Student Record");
		System.out.println ("[1] - Create a new Student Record");
		System.out.println ("[2] - Retrieve a student Record");
		System.out.println ("[3] - Update a student Record");
		System.out.println ("[4] - Delete a student Record");
		System.out.println ("[5] - Exit");
		System.out.println ("Enter function to perform:");
		int selection = sc.nextInt();
		
		if (selection == 1) {
			/* Mechanics for Creating a Student Record 
			 * 1. Ask user for the data about the student
			 * 2. Create the new record
			 */
			System.out.println ("Creating new record of a Student");
			System.out.println ("Enter Student ID: ");
			studentid = sc.nextInt();
			sc.nextLine();
			System.out.println ("Enter Last Name: ");
			lastname = sc.nextLine();
			System.out.println ("Enter First Name: ");
			firstname = sc.nextLine();
			addStudent();
		} else if (selection == 2) {
			/* Mechanics for Retrieving Student Record 
			 * 1. Ask user for student ID
			 * 2. Retrieve the record with the student ID provided
			 */
			System.out.println("Enter student ID to retrieve :");
			studentid = sc.nextInt();
			viewStudent();			
		} else if (selection == 3) {
			/* Mechanics for Updating a Record
			 * 1. Ask for the identifier of the record
			 * 2. Retrieve the old data of the record
			 * 3. Ask for the new set of data of the record. 
			 *    if user did not put any value then old data will be used.
			 * 4. Call the update function
			 */			

			System.out.println("Enter student ID to update :");
			studentid = sc.nextInt();
			sc.nextLine();
			viewStudent();
			
			if (norecord) {
				System.out.println("No record to update.");
			} else {
				String newlastname  = new String();
				String newfirstname = new String();
				System.out.println ("Enter updated Last Name: ");
				newlastname  = sc.nextLine();
				System.out.println ("Enter updated First Name: ");
				newfirstname = sc.nextLine();
	
				/* Check if the field was left not to be updated
				 * If so, then use the old value of the data
				 */
				if (newlastname.isEmpty()  == false) lastname = newlastname;
				if (newfirstname.isEmpty() == false) firstname = newfirstname;
				updateStudent();
			}
		} else if (selection == 4) {
			/* Mechanics for Deleting a Record
			 * 1. Ask for the identifier of the record
			 * 2. Retrieve the old data of the record
			 * 3. Call the delete function
			 */			
			
			System.out.println("Enter student ID to delete :");
			studentid = sc.nextInt();
			sc.nextLine();
			viewStudent();
			
			if (norecord) {
				System.out.println("No record to delete.");
			} else {
				deleteStudent();
			}
		} else if (selection == 5) {
			System.out.println("Exiting Student Function selected");
			System.out.println("Function terminated");
			return selection;
		} else {
			System.out.println("Selection not valid");
		}
		
		System.out.println ("Press any key to return to Student Functions");
		sc.nextLine();
		sc.nextLine();
		
		return selection;
	}
	
	public static void main(String[] args) {
		students s = new students();
		while (s.function() != 5) {
		};
	}
}
