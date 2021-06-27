
package com.flipkart.client;

import com.flipkart.constant.RoleConstants;
import com.flipkart.exception.StudentNotRegisteredException;
import com.flipkart.exception.UserAlreadyExistException;
import com.flipkart.exception.UserNotFoundException;
import com.flipkart.service.StudentInterface;
import com.flipkart.service.StudentOperation;
import com.flipkart.service.UserInterface;
import com.flipkart.service.UserOperation;

import java.util.Scanner;

import static com.flipkart.constant.RoleConstants.STUDENT;

public class CRSApplicationClient {

	public static void main(String[] args) throws StudentNotRegisteredException {
		Scanner sc = new Scanner(System.in);
		boolean loggedIn = false;
		System.out.println(" ");
		System.out.println("++++++++  COURSE MANAGEMENT SYSTEM  +++++++++");
		//System.out.println("-------------------------------------------------------------");

		printMenu();
		int Input=sc.nextInt();
		
		while(Input!=3)
		{
			switch(Input)
			{	
				case 1:
					registerStudent();
					break;
				case 2:
					loginUser();
					break;
				case 3:
					break;
				default:
					System.out.println("Enter a valid input");
			}
			printMenu();
			Input = sc.nextInt();
			if(Input==3) {
				break;
			}
		}
		//System.out.println("---------------- GOODBYE --------------------------");
		
		sc.close();
	}

	public static void printMenu() {
		//System.out.println("------------------------------------------");
		System.out.println("\n\nMENU ---------->");
		System.out.println();
		System.out.println("Press 1 for Student Registration ");
		System.out.println("Press 2 to Login ");
		System.out.println("Press 3 to Exit");
		System.out.println();
		System.out.println("Input Choice:");
		System.out.println();
	}
	
	private static void registerStudent() {
		Scanner sc = new Scanner(System.in);

		String userId, name, password, address, branch, mobileNumber, rollNumber;

		try {
			// input all the student details
			//System.out.println("------------------------------------------");
			System.out.println("        STUDENT REGISTRATION PORTAL        ");
			System.out.println();

			System.out.println("Enter Name:");
			name = sc.nextLine();
			System.out.println("Enter User ID:");
			rollNumber = sc.nextLine();
			System.out.println("Enter Email:");
			userId = sc.nextLine();
			System.out.println("Enter Password:");
			password = sc.nextLine();
			System.out.println("Enter Batch:");
			mobileNumber = sc.nextLine();
			System.out.println("Enter Branch:");
			branch = sc.nextLine();
			System.out.println("Enter Address:");
			address = sc.nextLine();

			StudentOperation studentOperation = new StudentOperation();
			studentOperation.registerStudent(name, mobileNumber, address, userId, password, String.valueOf(STUDENT),
					rollNumber, branch, false);

		} catch (UserAlreadyExistException ex) {
			ex.getMessage();
		}
	}

	private static void loginUser() throws StudentNotRegisteredException {
		Scanner sc = new Scanner(System.in);

		String userId, password;
		try {
			//System.out.println("------------------------------------------");
			System.out.println("                LOGIN PORTAL        ");
			System.out.println();

			System.out.println("Enter Email:");
			userId = sc.next();
			System.out.println("Enter Password:");
			password = sc.next();
			UserInterface userInterface = new UserOperation();
			boolean loggedin = userInterface.verifyCredentials(userId, password);
			// 2 cases
			// true->role->student->approved
			if (loggedin) {
				String role = userInterface.getRole(userId);
//				Role userRole = Role.(role);
				RoleConstants userRole = RoleConstants.stringToName(role);
				switch (userRole) {
				case ADMIN:
					System.out.println("\nAdmin Login Successful!");
					AdminMenuCRS adminMenu = new AdminMenuCRS();
					adminMenu.createMenu();
					break;
				case PROFESSOR:
					System.out.println("\nProfessor Login Successful!");
					ProfessorMenuCRS professorMenu = new ProfessorMenuCRS();
					professorMenu.professorMenu(userId);

					break;
				case STUDENT:
					StudentInterface studentInterface = new StudentOperation();
					int isApproved = studentInterface.checkIsVerified(userId);
					if (isApproved == 1) {
						System.out.println(" \nStudent Login Successful!");
						StudentMenuCRS studentMenu = new StudentMenuCRS();
						studentMenu.create_menu(userId);

					} else {
						System.out.println("\nLogin fail,Approval required from admin!");
						loggedin = false;
					}
					break;
				}

			} else {
				System.out.println("Invalid Credentials!");
			}

		} catch (UserNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
