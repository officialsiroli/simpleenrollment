package simpleenrollment;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class courseofferings {

	public String	termcode;
	public String	coursecode;
	public String	section;
	public int		offering_slots;
	public int		taken_slots;
	public boolean 	norecord;						// a status variable if no record was found during operations
	public boolean	noerror;
	
	public courseofferings() {
		termcode   		= "";
		coursecode 		= "";
		section   		= "";
		offering_slots 	= 0;
		taken_slots 	= 0;
		norecord		= false;
		noerror			= true;
	}
	
	public void viewCourseOffering() {
		Scanner 			sc 		 = new Scanner(System.in);
		PreparedStatement 	pstmt;
		ResultSet			rs;						
		try {
			norecord = true;
			System.out.println("Retrieving the course offering ");
		
			dbconnect db = new dbconnect();			
			pstmt = db.conn.prepareStatement("SELECT termcode, coursecode, section, offering_slots, taken_slots FROM course_offering WHERE termcode=? AND coursecode=? AND section=? AND (offering_slots-taken_slots)>0");
			pstmt.setString(1, termcode);
			pstmt.setString(2, coursecode);
			pstmt.setString(3, section);
			rs = pstmt.executeQuery();
			while (rs.next()) {		
				norecord = false;
				offering_slots = rs.getInt("offering_slots");
				taken_slots    = rs.getInt("taken_slots");
			}
			
			rs.close();		
			pstmt.close();	
			db.disconnect();
			
			if (norecord) {
				System.out.println("No offering found.");
				noerror=false;
			} else {
				System.out.println("Offering found.");
				noerror=true;
			}
		} catch (Exception e) {
	        System.out.println("Error occured while retrieving a offering");
	        System.out.println(e.getMessage());			
	        noerror=false;
		}			
	}
	
	public void updateCourseOfferingCount() {
		Scanner 			sc 		 = new Scanner(System.in);
		PreparedStatement 	pstmt;
		try {
			System.out.println("Updating the course offering ");
			dbconnect db = new dbconnect();			
			pstmt = db.conn.prepareStatement("UPDATE course_offering SET taken_slots = ? WHERE termcode=? AND coursecode=? AND section=?");
			pstmt.setInt(1, taken_slots);
			pstmt.setString(2, termcode);
			pstmt.setString(3, coursecode);
			pstmt.setString(4, section);
			pstmt.executeUpdate();
			pstmt.close();	
			db.disconnect();	
			noerror=true;
		} catch (Exception e) {
		    System.out.println("Error occured while updating an offering");
		    System.out.println(e.getMessage());	
		    noerror=false;
		}			
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
