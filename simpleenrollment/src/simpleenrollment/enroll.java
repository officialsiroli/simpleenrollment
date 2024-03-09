package simpleenrollment;
import java.sql.*;
import java.util.*;

public class enroll {
	
	public	String			termcode;
	public	int				term;
	public	int 			start_year;
	public	int				end_year;
	public  boolean 		noerror=true;
	public  students 		s;
	public  courseofferings	co;
	
	public enroll() {
		getCurrentTerm();
	}
	
	private void getCurrentTerm() {
		Scanner 			sc 		    = new Scanner(System.in);
		PreparedStatement 	pstmt;
		ResultSet			rs;		
		int 				recordcount = 0;
		try {
			System.out.println("Retrieving current term and school year" );
			dbconnect db = new dbconnect();	
			pstmt = db.conn.prepareStatement("SELECT termcode, term, start_year, end_year FROM	ref_terms WHERE	current = 1 ORDER BY start_year, term DESC;");
			rs = pstmt.executeQuery();
			while (rs.next()) {
				recordcount++;
				termcode 	= rs.getString("termcode");
				term     	= rs.getInt("term");
				start_year 	= rs.getInt("start_year");
				end_year	= rs.getInt("end_year");
			}
			if (recordcount==0) {
				System.out.println("No current term on record. Contact System Administrator");
				noerror=false;
			} else {
				System.out.println("Term Code:   " + termcode);
				System.out.println("School Year: " + start_year + "-"+ end_year);
				System.out.println("Term:        " + term);
				noerror=true;
			}
		} catch (Exception e) {
	        System.out.println("Error occured while retrieving current term and school year");
	        System.out.println(e.getMessage());					
	        noerror=false;
		}	
	}
	
	public void getStudent() {
		Scanner sc = new Scanner(System.in);
		
		s = new students();
		System.out.println("Enter Student ID of student enrolling: ");
		s.studentid = sc.nextInt();
		s.viewStudent();
		if (s.norecord) {
			System.out.println("Student is not on record");
			noerror=false;
		} else noerror=true;
	}
	
	public void enrollCourse() {
		
		Scanner sc = new Scanner(System.in);
		co = new courseofferings();

		System.out.println("Enter Course to enroll in: ");
		co.coursecode = sc.nextLine();
		System.out.println("Enter Section of the course: ");
		co.section  = sc.nextLine();		
		co.termcode = termcode;
		co.viewCourseOffering();
		if (co.norecord) {
			System.out.println(co.coursecode + " " + co.section + " is currently not available");
			noerror=false;
		} else {
			System.out.println("Course and section valid for enrollment");
			System.out.println("Press any key to enroll");
			sc.nextLine();	
			
			enrollment e = new enrollment();
			e.co 		= (courseofferings)co;
			e.studentid = s.studentid;
			if (e.checkEnrollment()==1) {
				e.addEnrollment();
				if (e.noerror) {
					System.out.println("Enrollment record created");
					co.taken_slots++;
					co.updateCourseOfferingCount();
					noerror=true;
				} else {
					System.out.println("Enrollment was unsuccessful");
					noerror=false;
				}
			} else {
				System.out.println("Student is already enrolled in the course. Cannot continue with enrollment");
				noerror=false;
			}
		}
	}

	public static void main(String[] args) {
		enroll e = new enroll();
		e.getCurrentTerm();
		if (e.noerror) {
			e.getStudent();
			if (e.noerror) {
				e.enrollCourse();
			}
		} 
	}

}
