package simpleenrollment;
import java.util.*;

public class mainmodule {

	public mainmodule() {}	
	
	public static void main(String[] args) {
		
		int		selection 	= 0;
		Scanner sc 			= new Scanner(System.in);
		
		while (selection != 4) {
			System.out.println("=====================================================");
			System.out.println("Main Modules of the DB Application");
			System.out.println ("[1] Manage Student Records");
			System.out.println ("[2] Enroll Student in a Course");
			System.out.println ("[3] Generate a Report");
			System.out.println ("[4] Quit Application");
			System.out.println("=====================================================");
			System.out.println ("Enter function to perform");
			selection = sc.nextInt();
	
			if (selection==1) {
				students s = new students();
				while (s.function() != 5) {};
			} else if (selection==2) {
				enroll e = new enroll();
				int enrollselection = 0;
				if (e.noerror) {
					while (enrollselection != 1) {
						e.getStudent();
						if (e.noerror) {
							e.enrollCourse();
						}
						System.out.println("[0]-Enroll Again [1]-Exit Enrollment");
						enrollselection = sc.nextInt();
					}
				} 
			} else if (selection==3) {
				report_01 r = new report_01();
				int reportselection = 0;
				System.out.println("Running Report Generator");
				while (reportselection != 1) {
					r.generate();
					System.out.println("[0]-Generate Again [1]-Exit Report Generation");
					reportselection = sc.nextInt();
				}
			} else if (selection==4) {
				System.out.println("Application is terminated");
			}
		}
	}
}
