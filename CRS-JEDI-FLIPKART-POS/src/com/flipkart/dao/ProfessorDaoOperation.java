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
 * @author GP-08
 *
 */
public class ProfessorDaoOperation implements ProfessorDaoInterface{
	

	/**
	 * Method to get Courses by Professor Id using SQL Commands
	 * @param userId, professor id of the professor
	 * @return get the courses offered by the professor.
	 */
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
