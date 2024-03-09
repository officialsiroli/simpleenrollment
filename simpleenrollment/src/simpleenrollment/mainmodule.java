package simpleenrollment;
import java.util.Scanner;

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
				while (e.function() != 1) {}; 
			} else if (selection==3) {
				report_01 r = new report_01();
				while (r.function() != 1) {};
			} else if (selection==4) {
				System.out.println("Application is terminated");
			}
		}
	}
}
