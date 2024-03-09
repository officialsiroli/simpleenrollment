package simpleenrollment;

import java.sql.*;
import java.util.*;

public class enrollment {
	
	public int				studentid;
	public courseofferings 	co;
	public float			grade=0;
	public boolean			noerror=true;

	public enrollment() {
		co 		  = new courseofferings();
		studentid = 0;
		grade     = 0;
		noerror   = true;
	}
	
	public int checkEnrollment() {
		Scanner 			sc = new Scanner(System.in);
		PreparedStatement 	pstmt;		
		ResultSet			rs;
		try {				
			dbconnect db = new dbconnect();
			pstmt = db.conn.prepareStatement("SELECT * FROM enrollment WHERE studentid = ? AND termcode=? AND coursecode=?");			
			pstmt.setInt(1, studentid);
			pstmt.setString(2, co.termcode);
			pstmt.setString(3, co.coursecode);
			rs = pstmt.executeQuery();		
			while (rs.next()) {
				rs.close();
				pstmt.close();
				db.disconnect();
				return 0;
			}
			return 1;
		} catch (Exception e) {
	        System.out.println("Error occured while checking enrollment record");
	        System.out.println(e.getMessage());	
	        noerror=false;
	        return 0;
		}				
		
	}
	
	public void addEnrollment() {
		Scanner 			sc = new Scanner(System.in);
		PreparedStatement 	pstmt;				
		try {				
			dbconnect db = new dbconnect();
			pstmt = db.conn.prepareStatement("INSERT INTO enrollment (studentid, termcode, coursecode, section) VALUES (?,?,?,?)");			
			pstmt.setInt(1, studentid);
			pstmt.setString(2, co.termcode);
			pstmt.setString(3, co.coursecode);
			pstmt.setString(4, co.section);
			pstmt.executeUpdate();			
			pstmt.close();					
			System.out.println("Enrollment recorded in the Database");	
			noerror=true;
			db.disconnect();
		} catch (Exception e) {
	        System.out.println("Error occured while adding a Student Record");
	        System.out.println(e.getMessage());	
	        noerror=false;
		}		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
