package com.flipkart.dao;

import java.util.HashMap;

import com.flipkart.bean.Course;
import com.flipkart.bean.Professor;
import com.flipkart.bean.ReportCard;
import com.flipkart.bean.User;
import com.flipkart.exception.AddCourseException;
import com.flipkart.exception.CourseNotDeletedException;
import com.flipkart.exception.CourseNotFoundException;
import com.flipkart.exception.ProfessorNotAddedException;
import com.flipkart.exception.ProfessorNotDeletedException;
import com.flipkart.exception.StudentNotFoundForVerificationException;
import com.flipkart.exception.StudentNotRegisteredException;
import com.flipkart.exception.UserAlreadyExistException;
import com.flipkart.exception.UserNotApprovedExecption;
import com.flipkart.exception.UserNotFoundException;

/**
 * @author jedi08
 *
 */
public interface AdminDaoInterface {

	/**
	 * Delete Course using SQL commands
	 * @param courseCode
	 * @throws CourseNotFoundException
	 * @throws CourseNotDeletedException 
	 */
	public void deleteCourse(String courseId) throws CourseNotFoundException, CourseNotDeletedException;

	/**
	 * Add Course using SQL commands
	 * @param course
	 * @throws AddCourseException
	 */
	public void addCourse(Course course) throws AddCourseException;
	
	/**
	 * Approve Student using SQL commands
	 * @param studentId
	 * @throws StudentNotFoundForVerificationException
	 */
	public void approveStudent(String studentId) throws StudentNotFoundForVerificationException;
	
	/**
	 * Add professor using SQL commands
	 * @param professor
	 * @throws ProfessorNotAddedException
	 * @throws UserAlreadyExistException 
	 */
	public void addProfessor(Professor professor) throws ProfessorNotAddedException, UserAlreadyExistException;
	
	/**
	 * Method to add user using SQL commands
	 * @param user
	 * @throws UserNotApprovedExecption
	 * @throws UserAlreadyExistException 
	 */
	public void addUser(Professor user) throws UserNotApprovedExecption, UserAlreadyExistException;
	
	/**
	 * Method to delete Professor from DB
	 * 
	 * @param professorId
	 * @throws ProfessorNotAddedException
	 * @throws UserNotFoundException
	 */
	public void removeProfessor(String prefessorId) throws ProfessorNotAddedException,ProfessorNotDeletedException;

	/**
	 * Function to generate report
	 * 
	 * @param reportCard
	 * @throws StudentNotRegisteredException
	 */
	public void generateReport(int semester,String StudentId,String CPI,String courseid) throws StudentNotRegisteredException;
	public HashMap<String, String> fetchGrades(String StudentId, int semester);
}
