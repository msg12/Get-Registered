/////**
//// * 
//// */
////package com.flipkart.dao;
////
////import java.sql.Connection;
////import java.sql.PreparedStatement;
////import java.sql.ResultSet;
////import java.sql.SQLException;
////import java.util.ArrayList;
////import java.util.List;
////import org.apache.log4j.Logger;
////
////import com.flipkart.bean.Course;
////import com.flipkart.bean.EnrolledStudent;
////import com.flipkart.constant.SQLQueriesConstants;
////import com.flipkart.utils.DBUtils;
////
/////**
//// * @author jedi08
//// *
//// */
////public class ProfessorDaoOperation implements ProfessorDaoInterface{
////
////	private static volatile ProfessorDaoOperation instance=null;
////	private static Logger logger = Logger.getLogger(UserDaoOperation.class);
////
////	/**
////	 * Default Constructor
////	 */
////	private ProfessorDaoOperation()
////	{
////
////	}
////
////	/**
////	 * Method to make ProfessorDaoOperation Singleton
////	 * @return
////	 */
////	public static ProfessorDaoOperation getInstance()
////	{
////		if(instance==null)
////		{
////			// This is a synchronized block, when multiple threads will access this instance
////			synchronized(ProfessorDaoOperation.class){
////				instance=new ProfessorDaoOperation();
////			}
////		}
////		return instance;
////	}
////
////	/**
////	 * Method to get Courses by Professor Id using SQL Commands
////	 * @param profId professor id of the professor
////	 * @return get the courses offered by the professor.
////	 */
////	@Override
////	public List<Course> getCoursesByProfessor(String profId) {
////		Connection connection=DBUtils.getConnection();
////		List<Course> courseList=new ArrayList<Course>();
////		try {
////			PreparedStatement statement = connection.prepareStatement(SQLQueriesConstants.GET_COURSES);
////			
////			statement.setString(1, profId);
////			
////			ResultSet results=statement.executeQuery();
////			while(results.next())
////			{
////				courseList.add(new Course(results.getString("courseId"),results.getString("courseName"),results.getString("instructorId"),results.getInt("seats")));
////			}
////		}
////		catch(SQLException e)
////		{
////			logger.error(e.getMessage());
////		}
////		return courseList;
////		
////	}
////
////	/**
////	 * Method to view list of enrolled Students using SQL Commands
////	 * @param: profId: professor id 
////	 * @param: courseCode: course code of the professor
////	 * @return: return the enrolled students for the corresponding professor and course code.
////	 */
////	@Override
////	public List<EnrolledStudent> getStudents(String profId) {
////		Connection connection=DBUtils.getConnection();
////		List<EnrolledStudent> enrolledStudents=new ArrayList<EnrolledStudent>();
////		try {
////			PreparedStatement statement = connection.prepareStatement(SQLQueriesConstants.GET_ENROLLED_STUDENTS);
////			statement.setString(1, profId);
////			
////			ResultSet results = statement.executeQuery();
////			while(results.next())
////			{
////				enrolledStudents.add(new EnrolledStudent(results.getString("courseId"),results.getString("courseName"),results.getString("studentId"),results.getInt("semester")));
////			}
////		}
////		catch(SQLException e)
////		{
////			logger.error(e.getMessage());
////		}
////		return enrolledStudents;
////	}
////	
////	/**
////	 * Method to Grade a student using SQL Commands
////	 * @param: profId: professor id 
////	 * @param: courseId: course code for the corresponding 
////	 * @param: semester: semester for the corresponding 
////	 * @return: returns the status after adding the grade
////	 */
////	public boolean addGrade(String studentId,String courseId,int semester,String grade) {
////		Connection connection=DBUtils.getConnection();
////		try {
////			PreparedStatement statement = connection.prepareStatement(SQLQueriesConstants.ADD_GRADE);
////			
////			logger.info(courseId + " ->  " + grade);
////			statement.setString(1, grade);
////			statement.setString(2, courseId);
////			statement.setString(3, studentId);
////			statement.setInt(4, semester);
////			int row = statement.executeUpdate();
////			
////			if(row==1)
////				return true;
////			else
////				return false;
////		}
////		catch(SQLException e)
////		{
////			logger.error(e.getMessage());
////		}
////		return false;
////	}
////	
////
////	
////}
///**
// * 
// */
//package com.flipkart.dao;
//
//import java.sql.SQLException;
//import java.util.List;
//
//import com.flipkart.bean.Course;
//import com.flipkart.bean.ReportCard;
//import com.flipkart.exception.PaymentNotFoundException;
//import com.flipkart.bean.Payment;
//
///**
// * jedi08
// *
// */
//public interface RegistrationDaoInterface {
//
//	public int numOfRegisteredCourses(String studentId, int semester) throws SQLException;
//	
//	public boolean isRegistered(String courseCode, String studentId, int semester) throws SQLException;
//	/**
//	 * Method to add course in database
//	 * 
//	 * @param courseCode
//	 * @param studentId
//	 * @return boolean indicating if the course is added successfully
//	 * @throws SQLException
//	 */
//	public boolean addCourse(String courseId, String studentId, int semester) throws SQLException;
//
//	/**
//	 * Drop Course selected by student
//	 * 
//	 * @param courseCode
//	 * @param studentId
//	 * @return boolean indicating if the course is dropped successfully
//	 * @throws CourseNotFoundException
//	 * @throws SQLException
//	 */
//	public boolean removeCourse(String courseId, String studentId, int semester) throws SQLException;
//
//	/**
//	 * Method to get the list of courses available from course catalog
//	 * 
//	 * @param studentId
//	 * @return list of Courses
//	 * @throws SQLException
//	 */
//	public List<Course> viewCourses(String studentId, int semester) throws SQLException;
//
//	/**
//	 * Method to View list of Registered Courses
//	 * 
//	 * @param studentId
//	 * @return list of Registered Courses
//	 * @throws SQLException
//	 */
//	public List<Course> viewRegisteredCourses(String studentId, int semester) throws SQLException;
//
//	/**
//	 * Method to view grade card of the student
//	 * 
//	 * @param studentId
//	 * @return Grade Card
//	 * @throws SQLException
//	 * @throws PaymentNotFoundException 
//	 */
//	public ReportCard viewReportCard(String studentId, int semester) throws SQLException, PaymentNotFoundException;
//
//	/**
//	 * Method to retrieve fee for the selected courses from the database and
//	 * calculate total fee
//	 * 
//	 * @param studentId
//	 * @return Fee Student has to pay
//	 * @throws SQLException
//	 */
//	public boolean payFee(Payment payment) throws SQLException,PaymentNotFoundException;
//	
//	/*
//	 * Method to check whether the given student has paid the fee or not
//	 * for given sem
//	 * */
//	public Payment viewFee(String studentId,int semester) throws SQLException;
//
//}
/**
 * 
 */
package com.flipkart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.flipkart.bean.Course;
import com.flipkart.bean.EnrolledStudent;
import com.flipkart.constant.SQLQueriesConstants;
import com.flipkart.utils.DBUtils;

/**
 * jedi08
 *
 */
public class ProfessorDaoOperation implements ProfessorDaoInterface{
	

	/**
	 * Method to get Courses by Professor Id using SQL Commands
	 * @param userId, professor id of the professor
	 * @return get the courses offered by the professor.
	 */
	
	private static volatile ProfessorDaoOperation instance=null;

/**
 * Default Constructor
 */
public ProfessorDaoOperation()
{

}

/**
 * Method to make ProfessorDaoOperation Singleton
 * @return
 */
public static ProfessorDaoOperation getInstance()
{
	if(instance==null)
	{
		// This is a synchronized block, when multiple threads will access this instance
		synchronized(ProfessorDaoOperation.class){
			instance=new ProfessorDaoOperation();
		}
	}
	return instance;
}
	@Override
	public List<Course> getCoursesByProfessor(String profId) {
		Connection connection=DBUtils.getConnection();
		List<Course> courseList=new ArrayList<Course>();
		try {
			PreparedStatement statement = connection.prepareStatement(SQLQueriesConstants.GET_COURSES);
			
			statement.setString(1, profId);
			
			ResultSet results=statement.executeQuery();
			while(results.next())
			{
				courseList.add(new Course(results.getString("courseId"),results.getString("courseName"),results.getString("instructorId"),results.getInt("seats")));
			}
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return courseList;
		
	}

	/**
	 * Method to view list of enrolled Students using SQL Commands
	 * @param: profId: professor id 
	 * @param: courseCode: course code of the professor
	 * @return: return the enrolled students for the corresponding professor and course code.
	 */
	@Override
	public List<EnrolledStudent> getStudents(String profId) {
		Connection connection=DBUtils.getConnection();
		List<EnrolledStudent> enrolledStudents=new ArrayList<EnrolledStudent>();
		try {
			PreparedStatement statement = connection.prepareStatement(SQLQueriesConstants.GET_ENROLLED_STUDENTS);
			statement.setString(1, profId);
			
			ResultSet results = statement.executeQuery();
			while(results.next())
			{
				enrolledStudents.add(new EnrolledStudent(results.getString("courseId"),results.getString("courseName"),results.getString("studentId"),results.getInt("semester")));
			}
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return enrolledStudents;
	}
	
	/**
	 * Method to Grade a student using SQL Commands
	 * @param: profId: professor id 
	 * @param: courseId: course code for the corresponding 
	 * @param: semester: semester for the corresponding 
	 * @return: returns the status after adding the grade
	 */
	public boolean addGrade(String studentId,String courseId,int semester,String grade) {
		Connection connection=DBUtils.getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(SQLQueriesConstants.ADD_GRADE);
//			PreparedStatement statement = connection.prepareStatement(SQLQueriesConstants.ADD_REPORT_CARD);
			
			
			System.out.println(grade + "   " + courseId);
			statement.setString(1, grade);
			statement.setString(2, courseId);
			statement.setString(3, studentId);
			statement.setInt(4, semester);
			int row = statement.executeUpdate();
			
			if(row==1)return true;
			
			return false;
			
//			if(row==1) {
////				public static final String ADD_REPORT_CARD=" insert into reportcard(studentId_report , cpi,semester) values (? , ? , ?);";
//
//				PreparedStatement stat = connection.prepareStatement(SQLQueriesConstants.ADD_REPORT_CARD);
//				
//				stat.setString(1, studentId);
//				stat.setString(2, courseId);
//				stat.setInt(3, semester);
//				stat.setString(4, grade);
//				
//				int ch = stat.executeUpdate();
//				if(ch==1)return true;
//				
//				return false;
//				
//				
//			}
//			else
//				return false;
		}
		catch(SQLException e)
		{
			System.out.println(e.getMessage());
		}
		return false;
	}
	

	
}

