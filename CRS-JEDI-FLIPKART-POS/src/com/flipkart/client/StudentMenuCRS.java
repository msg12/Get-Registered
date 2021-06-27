package com.flipkart.client;

import java.sql.SQLException;
import java.util.*;
import java.util.Scanner;

import com.flipkart.bean.Course;
import com.flipkart.bean.Payment;
import com.flipkart.bean.ReportCard;
import com.flipkart.exception.AddCourseException;
import com.flipkart.exception.CourseLimitReachedException;
import com.flipkart.exception.CourseNotDeletedException;
import com.flipkart.exception.CourseNotFoundException;
import com.flipkart.exception.PaymentNotFoundException;
import com.flipkart.exception.SeatNotAvailableException;
import com.flipkart.service.RegistrationInterface;
import com.flipkart.service.RegistrationOperation;

/**
 *
 * @author GP-08 The class displays the menu for student client
 *
 */
public class StudentMenuCRS {

	Scanner sc = new Scanner(System.in);

	RegistrationInterface registrationInterface = new RegistrationOperation();
	private int semester = 1;

	private boolean is_registered;

	/**
	 * Method to generate Student Menu for course registration, addition, removal
	 * and fee payment
	 * 
	 * @param studentId student id
	 */
	public void create_menu(String studentId) {

		is_registered = getRegistrationStatus(studentId);
		while (is_registered) {
			//System.out.println("------------------------------------------");
			System.out.println();
			System.out.println("               STUDENT PORTAL          ");
			System.out.println();
			//System.out.println("------------------------------------------");
			System.out.println("1. Course Registration");
			System.out.println("2. Add Course");
			System.out.println("3. Drop Course");
			System.out.println("4. View Course");
			System.out.println("5. View Registered Courses");
			System.out.println("6. View grade card");
			System.out.println("7. Make Payment");
			System.out.println("8. Logout");
			//System.out.println("------------------------------------------");
			System.out.println();
			System.out.println("  Input Choice");
			System.out.println();

			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				registerCourses(studentId);
				break;

			case 2:
				addCourse(studentId);
				break;

			case 3:

				dropCourse(studentId);
				break;

			case 4:
				viewCourse(studentId);
				break;

			case 5:
				viewRegisteredCourse(studentId);
				break;

			case 6:
				viewGradeCard(studentId);
				break;

			case 7:
				make_payment(studentId);
				break;

			case 8:
				return;

			default:
				System.out.println("Invalid Entry,Enter Again");
			}
		}
	}

	/**
	 * Select course for registration
	 * 
	 * @param studentId
	 */
	private void registerCourses(String studentId) {
		System.out.println("Enter Semester : ");
		semester = sc.nextInt();
		sc.nextLine();
		int count = 0;
		while (count < 6) {
			try {
				List<Course> courseList = viewCourse(studentId);

				if (courseList == null)
					return;

				System.out.println("Enter Course Code : " + (count + 1));
				String courseId = sc.nextLine();

				if (registrationInterface.addCourse(courseId, studentId, semester)) {
					System.out.println("Course " + courseId + " registered sucessfully.");
					count++;
				} else {
					System.out.println(" You have already registered for Course : " + courseId);
				}
			} catch (AddCourseException | CourseNotFoundException | CourseLimitReachedException | SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		System.out.println("Registration Successful");
		is_registered = true;

	}

	/**
	 * Add course for registration
	 * 
	 * @param studentId
	 * @throws AddCourseException
	 */
	private void addCourse(String studentId) {
		if (is_registered) {
			List<Course> availableCourseList = viewCourse(studentId);

			if (availableCourseList == null)
				return;

			try {
				System.out.println("Enter Course Code : ");
				String courseCode = sc.next();
				if (registrationInterface.addCourse(courseCode, studentId, semester)) {
					System.out.println(" You have successfully registered for Course : " + courseCode);
				} else {
					System.out.println(" You have already registered for Course : " + courseCode);
				}
			} catch (AddCourseException | CourseNotFoundException | CourseLimitReachedException | SQLException e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("Please complete registration");
		}

	}

	/**
	 * Method to check if student is already registered
	 * 
	 * @param studentId
	 * @return Registration Status
	 */
	private boolean getRegistrationStatus(String studentId) {
		return true;
	}

	/**
	 * Drop Course
	 * 
	 * @param studentId
	 */
	private void dropCourse(String studentId) {
		sc.nextLine();
		if (is_registered) {
			List<Course> registeredCourseList = viewRegisteredCourse(studentId);

			if (registeredCourseList == null)
				return;

			System.out.println("Enter the Course Code : ");
			String courseCode = sc.nextLine();

			try {
				registrationInterface.dropCourse(courseCode, studentId, semester);
				System.out.println("You have successfully dropped Course : " + courseCode);

			} catch (CourseNotFoundException | SQLException | CourseNotDeletedException e) {

				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("Please complete registration");
		}

	}

	/**
	 * View all available Courses
	 * 
	 * @param studentId
	 * @return List of available Courses
	 */
	private List<Course> viewCourse(String studentId) {
		List<Course> course_available = null;
		try {
			course_available = registrationInterface.viewCourses(studentId, semester);
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}

		if (course_available.isEmpty()) {
			System.out.println("NO COURSE AVAILABLE");
			return null;
		}

		System.out
				.println(String.format("%-20s %-20s %-20s %-20s", "COURSE CODE", "COURSE NAME", "INSTRUCTOR", "SEATS"));
		System.out.println("-----------------------------------------------------------------------------------------");
		for (Course obj : course_available) {
			System.out.println(String.format("%-20s %-20s %-20s %-20s", obj.getCourseId(), obj.getCourseName(),
					obj.getInstructorId(), obj.getSeats()));
		}
		System.out.println("-----------------------------------------------------------------------------------------");


		return course_available;
		// TODO
	}

	/**
	 * View Registered Courses
	 * 
	 * @param studentId
	 * @return List of Registered Courses
	 */
	private List<Course> viewRegisteredCourse(String studentId) {
		List<Course> course_registered = null;
		try {
			course_registered = registrationInterface.viewRegisteredCourses(studentId, semester);
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}

		if (course_registered.isEmpty()) {
			System.out.println("You haven't registered for any course");
			return null;
		}
		System.out.println("-----------------------------------------------------------------------------------------");

		System.out.println(String.format("%-20s %-20s %-20s", "COURSE CODE", "COURSE NAME", "INSTRUCTOR"));

		for (Course obj : course_registered) {

			System.out.println(
					String.format("%-20s %-20s %-20s ", obj.getCourseId(), obj.getCourseName(), obj.getInstructorId()));
		}
		System.out.println("-----------------------------------------------------------------------------------------");

		return course_registered;
		// TODO
	}

	/**
	 * View grade card for particular student
	 * 
	 * @param studentId
	 */
	private void viewGradeCard(String studentId) {
		ReportCard grade_card = null;
		try {
			grade_card = registrationInterface.viewReportCard(studentId, semester);
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}


		
		System.out.println("-----------------------------------------------------------------------------------------");

		if (grade_card == null) {
			System.out.println("You haven't registered for any course!");
			return;
		}
		System.out.println("Student_ID : " + grade_card.getStudentId());
		System.out.println("Semester : " + grade_card.getSem());
		System.out.println("Grade : " + grade_card.getgrade());
		
		System.out.println(String.format("%-20s %-20s", "COURSE CODE", "GRADE"));

		for (HashMap.Entry<String, String> obj : grade_card.getGrades().entrySet()) {
			System.out.println(String.format("%-20s %-20s", obj.getKey(), obj.getValue()));
		}
		System.out.println("-----------------------------------------------------------------------------------------");
	}

	/**
	 * Make Payment for selected courses. Student is provided with an option to pay
	 * the fees and select the mode of payment.
	 * 
	 * @param studentId
	 */
	private void make_payment(String studentId) {
		// TODO
		
		
		List<Course> course_registered = null;
		try {
			course_registered = registrationInterface.viewRegisteredCourses(studentId, semester);
			if (course_registered.isEmpty()) {
				System.out.println("You haven't registered for any course");
				return ;
			}
		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}		
		try {
		Payment viewPayment = registrationInterface.viewFee(studentId, semester);
		if(viewPayment != null) {
			System.out.println("------------------------------------------");
			System.out.println("              Notification");
			System.out.println("------------------------------------------");
			System.out.print("PaymentId :\t\t" + viewPayment.getPaymentId());
			System.out.println("");
			System.out.print("Student Id :\t\t" + viewPayment.getStudentId());
			System.out.println("");
			System.out.print("Semester :\t\t" + viewPayment.getSemester());
			System.out.println("");
			System.out.print("Status :\t\t" + viewPayment.getStatus());
			System.out.println("");
			System.out.print("Amount Paid :\t\t" + viewPayment.getAmount());
			System.out.println("");
			System.out.println("------------------------------------------\n\n");
			return;
		}
		System.out.println("------------------------------------------");
		System.out.println("              Payment Option");
		System.out.println("------------------------------------------");
		System.out.println("1. Net Banking");
		System.out.println("2. Card");
		System.out.println("3. Scholarship\n\n");
		
		System.out.println("Choose any option from the above:\n");
		
		int choice = sc.nextInt();
		sc.nextLine();
		String BankName = "";
		String CardNumber ="";
		String paymentId ="";
		String ifsc="";
		int amount = course_registered.size() * 1000;
		String status ="success";
		System.out.print("Amount to be Paid :\t\tRs. " + amount);
		System.out.println("");
		switch(choice) {
		case 1:
			System.out.print("Enter Bank Name:\t");
			BankName = sc.nextLine();
			System.out.println("");
			System.out.print("Enter IFSC Code:\t");
			ifsc =  sc.nextLine();
			System.out.println("");
			System.out.print("Enter Account Number:\t");
			String account =  sc.nextLine();
			paymentId = BankName + account;
			System.out.println("");
			break;
		case 2:
			System.out.print("Enter Bank Name:\t");
			BankName = sc.nextLine();
			System.out.println("");
			System.out.print("Enter IFSC Code:\t");
			ifsc =  sc.nextLine();
			System.out.println("");
			System.out.print("Enter Card Number:\t");
			String card =  sc.nextLine();
			System.out.println("");
			paymentId = BankName + card;
			break;
		case 3:
			break;
		default : System.out.println("Payment Denied\nTry Again!");
			return;
		}
		String notificationId = studentId + semester+status;
		Payment pay = new Payment(paymentId,studentId,amount,status,notificationId,semester);
		
		
			if(registrationInterface.payFee(pay)) {
				
				System.out.println("------------------------------------------");
				System.out.println("              Notification");
				System.out.println("------------------------------------------");
				System.out.print("PaymentId :\t\t" + paymentId);
				System.out.println("");
				if(BankName != "") {
				System.out.print("Bank Name :\t\t" + BankName);
				System.out.println("");
				}
				if(ifsc != "") {
				System.out.print("PaymentID :\t\t" + paymentId);
				System.out.println("");
				}
				System.out.print("Status :\t\t" + status);
				System.out.println("");
				System.out.print("Amount Paid :\t\t" + amount);
				System.out.println("");
				System.out.println("------------------------------------------\n\n");
			}
			else {
				System.out.println("Payment not completed. try again.");
			}
			
		}
		catch( PaymentNotFoundException |SQLException ex) {
			System.out.println(ex.getMessage());
		}
		
	}

}