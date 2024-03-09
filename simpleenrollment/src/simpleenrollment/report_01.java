package simpleenrollment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class report_01 {
	
	public report_01() {}
	
	public void generate() {
		int					recordcount  = 0;
		int					start_year   = 0;
		int					end_year     = 0;
		float				grade		 = 0;
		String				coursecode   = "";
		int					studentcount = 0;
		int					term         = 0;
		int					totalcount   = 0;
		float				totalgrade	 = 0;
		float				avegrade	 = 0;
		Scanner 			sc 		     = new Scanner(System.in);
		PreparedStatement 	pstmt;
		ResultSet			rs;						// ResultSet is a data type to store results coming
													// from database queries
		try {
			System.out.println("Provide parameters for the Report");
			System.out.println("Enter the course: ");
			coursecode = sc.nextLine();
			System.out.println("Enter the school year (YYYY): ");
			start_year = sc.nextInt();
			System.out.println("Enter the term: ");
			term = sc.nextInt();
			
			System.out.println("Preparing the Report...");
			dbconnect db = new dbconnect();			/* Check the codes in dbconnect.java for connecting to the database
				 									 * dbconnect has an attribute conn that will contain the active
				 									 * connection to the database 
				 									 */
			pstmt = db.conn.prepareStatement("SELECT 		t.start_year, t.end_year, t.term, co.coursecode, e.grade, "
										   + "				COUNT(DISTINCT e.studentid) as numstudents                "
										   + "FROM			enrollment e	JOIN  course_offering co	              "
										   + "                              ON	  ( e.coursecode=co.coursecode AND    "
										   + "										e.termcode  =co.termcode )        "
										   + "							    JOIN  ref_terms t				          "
										   + "								ON	co.termcode = t.termcode              "
										   + "WHERE			t.start_year  = ?										  "
										   + "AND			t.term        = ?										  "
										   + "AND			co.coursecode = ?										  "
										   + "GROUP BY		t.start_year, t.end_year, t.term, co.coursecode, e.grade  "
										   + "ORDER BY		t.start_year, t.end_year, t.term, co.coursecode, e.grade  ");
			/* each ? in the SQL statement will be replaced by the value of the field in succeeding
			 * program statements below. Each ? is designated sequentially as 1, 2, 3.... n 
			 */

			pstmt.setInt(1, start_year);
			pstmt.setInt(2, term);
			pstmt.setString(3, coursecode);
			
			rs = pstmt.executeQuery();				/* Executes the SQL Statement. Is is executeQuery since it is 
		     										 * expected to return a result. The result will be stored in a
		     										 * resultSet variable rs
		     										 */
			recordcount = 0;
			while (rs.next()) {						/* rs.next() will fetch one record of the result
				 									 * it returns false if it cannot fetch anything already
				 									 */	
				recordcount++;
				if (recordcount==1) {
					System.out.println("School Year  Term  Course Code  Grade  Student Count");
					System.out.println("-----------  ----  -----------  -----  -------------");
				}
				
				start_year   = rs.getInt("start_year");
				end_year     = rs.getInt("end_year");
				term         = rs.getInt("term");
				coursecode   = rs.getString("coursecode");
				grade	     = rs.getFloat("grade");
				studentcount = rs.getInt("numstudents");
				
				totalcount   = totalcount + studentcount;
				totalgrade   = totalgrade + (grade * studentcount);
								
				System.out.println (start_year + "-" + end_year + "     " + term + "      " + coursecode + "    " + grade + "        " + studentcount);
				
			}

			if (recordcount==0) {
				System.out.println("Report is empty");
			} else {
				System.out.println("Summary Information");
				System.out.println("Total number of students: " + totalcount);
				System.out.println("Average grade:            " + totalgrade/totalcount);
				System.out.println("------------------------------------------------------------");
				System.out.println("Report has been generated");
			}
			
			rs.close();								// it is important to close the resultset to release the memory it used
			pstmt.close();							// it is important to close the prepared statement to release the memorty it used
			db.disconnect();						/* It is important to terminate the connection to the database.
					 								 * If the program will not terminate the connection, the connection
					 								 * will remain active and will waste the allowable connection
					 	 							 * in the database server
					 	 							 */
		} catch (Exception e) {
			System.out.println("An error occurred in generating the report");
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		report_01 r = new report_01();
		r.generate();
	}

}
