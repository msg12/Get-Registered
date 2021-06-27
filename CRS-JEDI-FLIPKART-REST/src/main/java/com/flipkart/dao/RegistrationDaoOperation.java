package com.flipkart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.flipkart.bean.Course;
import com.flipkart.bean.ReportCard;
import com.flipkart.constant.SQLQueriesConstants;
import com.flipkart.exception.PaymentNotFoundException;
import com.flipkart.bean.Payment;
import com.flipkart.utils.DBUtils;

/**
 * jedi08
 *
 */
public class RegistrationDaoOperation implements RegistrationDaoInterface {

	private PreparedStatement stmt = null;
	
	private static volatile RegistrationDaoOperation instance=null;

	/**
	 * Default Constructor
	 */
	public RegistrationDaoOperation()
	{}

	/**
	 * Method to make RegistrationDaoOperation Singleton
	 * @return
	 */
	public static RegistrationDaoOperation getInstance()
	{
		if(instance==null)
		{
			synchronized(RegistrationDaoOperation.class){
				instance=new RegistrationDaoOperation();
			}
		}
		return instance;
	}
	

	@Override
	public int numOfRegisteredCourses(String studentId, int semester) throws SQLException{
		
		Connection conn = DBUtils.getConnection();
		
		int count = 0;
		try {

			stmt = conn.prepareStatement(SQLQueriesConstants.NUMBER_OF_REGISTERED_COURSES);
			stmt.setString(1, studentId);
			stmt.setInt(2,  semester);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				count++;
			}
			return count;

		}
		catch (SQLException e) 
		{

			System.out.println(e.getMessage());

		} 
		catch (Exception e)
		{

			System.out.println(e.getMessage());
		}
		
		return count;
	}
	
	@Override
	public boolean addCourse(String courseId, String studentId, int semester) throws SQLException {

		Connection conn = DBUtils.getConnection();

		try {
			stmt = conn.prepareStatement(SQLQueriesConstants.ADD_COURSE);
			stmt.setString(1, studentId);
			stmt.setString(2, courseId);
			stmt.setInt(3, semester);
			stmt.setString(4, null);

			stmt.executeUpdate();
			stmt = conn.prepareStatement(SQLQueriesConstants.DECREMENT_COURSE_SEATS);
			stmt.setString(1, courseId);
			stmt.executeUpdate();

			return true;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} 
		return false;
	}
	
	@Override
	public boolean isRegistered(String courseCode, String studentId, int semetser) throws SQLException{
		
		Connection conn = DBUtils.getConnection();
		
		boolean check = false;
		try
		{
			stmt = conn.prepareStatement(SQLQueriesConstants.IS_REGISTERED);
			stmt.setString(1, courseCode);
			stmt.setString(2, studentId);
			stmt.setInt(3, semetser);
			ResultSet rs = stmt.executeQuery();
			
			while(rs.next())
			{
				check = true;
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		
		return check;
		
	}


	@Override
	public boolean removeCourse(String courseId, String studentId, int semester) throws SQLException {
		Connection conn = DBUtils.getConnection();

		try {
			stmt = conn.prepareStatement(SQLQueriesConstants.DROP_COURSE_QUERY);
			stmt.setString(1, courseId);
			stmt.setString(2, studentId);
			stmt.setInt(3, semester);
			stmt.execute();

			stmt = conn.prepareStatement(SQLQueriesConstants.INCREMENT_SEAT_QUERY);
			stmt.setString(1, courseId);
			stmt.execute();

			stmt.close();

			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 

		return true;
	}

	@Override
	public List<Course> viewCourses(String studentId, int semester) throws SQLException {
		// TODO Auto-generated method stub
		List<Course> availableCourseList = new ArrayList<>();
		Connection conn = DBUtils.getConnection();

		try {
			stmt = conn.prepareStatement(SQLQueriesConstants.VIEW_AVAILABLE_COURSES);
			stmt.setString(1, studentId);
			stmt.setInt(2, semester);
			ResultSet queryResult = stmt.executeQuery();

			while (queryResult.next()) {
				availableCourseList
						.add(new Course(queryResult.getString("courseId"), queryResult.getString("courseName"),
								queryResult.getString("instructorId"), queryResult.getInt("seats")));
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 

		return availableCourseList;
	}

	@Override
	public List<Course> viewRegisteredCourses(String studentId, int semester) throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = DBUtils.getConnection();
		List<Course> registeredCourseList = new ArrayList<>();
		try {
			stmt = conn.prepareStatement(SQLQueriesConstants.VIEW_REGISTERED_COURSES);
			stmt.setString(1, studentId);
			stmt.setInt(2, semester);

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				registeredCourseList.add(new Course(rs.getString("courseId"), rs.getString("courseName"),
						rs.getString("instructorId"), rs.getInt("seats")));

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 

		return registeredCourseList;
	}

	@Override
	public ReportCard viewReportCard(String studentId, int semester) throws SQLException,PaymentNotFoundException {
		// TODO Auto-generated method stub
		Connection conn = DBUtils.getConnection();
		ReportCard reportCard = null;
		try {
			stmt = conn.prepareStatement(SQLQueriesConstants.VIEW_REPORT_CARD);
			stmt.setString(1, studentId);
			stmt.setInt(2, semester);
			ResultSet queryResult = stmt.executeQuery();
			
			stmt = conn.prepareStatement(SQLQueriesConstants.VIEW_REGISTERED_COURSES);
			stmt.setString(1, studentId);
			stmt.setInt(2, semester);
			ResultSet queryResult2 = stmt.executeQuery();
			
			HashMap<String, String> grades = new HashMap<String, String>();
			while(queryResult2.next()) {
				grades.put(queryResult2.getString("course.courseId"), queryResult2.getString("semesterregistration.grade"));
			}

			while(queryResult.next()) {
				String ee=queryResult.getString("grade");
				reportCard = new ReportCard(studentId,grades, semester, ee);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
		return reportCard;
	}

	@Override
	public boolean payFee(Payment payment) throws SQLException {
		// TODO Auto-generated method stub
		Connection conn = DBUtils.getConnection();
		try {
			stmt = conn.prepareStatement(SQLQueriesConstants.INSERT_PAYMENT);
			stmt.setString(1, payment.getStudentId());
			stmt.setString(2, payment.getPaymentId());
			stmt.setString(3, payment.getStatus());
			stmt.setInt(4, payment.getAmount());
			stmt.setString(5, payment.getNotificationId());
			stmt.setInt(6, payment.getSemester());
			stmt.execute();
			return true;

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
		
		return false;
	}
	
	@Override
	public Payment viewFee(String studentId,int semester) {
		Connection conn = DBUtils.getConnection();
		try {
			stmt = conn.prepareStatement(SQLQueriesConstants.VIEW_PAYMENT);
			stmt.setString(1, studentId);
			stmt.setInt(2, semester);
			
			
			ResultSet q = stmt.executeQuery();
			
			while(q.next())
				return new Payment(q.getString("paymentId"),q.getString("studentId_payment"),q.getInt("amount"),q.getString("status"),q.getString("notificationId"),q.getInt("semester"));
		}
		catch(SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

}

