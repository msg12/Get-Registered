package com.flipkart.service;

import java.util.ArrayList;

import java.util.List;

import com.flipkart.bean.Course;
import com.flipkart.bean.EnrolledStudent;
import com.flipkart.exception.GradeNotAddedException;
import com.flipkart.exception.StudentNotFoundForVerificationException;
import com.flipkart.exception.StudentNotRegisteredException;
import com.flipkart.dao.ProfessorDaoInterface;
import com.flipkart.dao.ProfessorDaoOperation;
/**
 * jedi08
 *
 */
public class ProfessorOperation implements ProfessorInterface {

	ProfessorDaoInterface professorobj=new ProfessorDaoOperation();
	
	
	private static volatile ProfessorOperation instance=null;
	ProfessorDaoInterface professorDaoInterface=ProfessorDaoOperation.getInstance();
	private ProfessorOperation()
	{

	}

	/**
	 * Method to make ProfessorOperation Singleton
	 * @return
	 */
	public static ProfessorOperation getInstance()
	{
		if(instance==null)
		{
			// This is a synchronized block, when multiple threads will access this instance
			synchronized(ProfessorOperation.class){
				instance=new ProfessorOperation();
			}
		}
		return instance;
	}

	
	@Override
	public List<Course> getCourses(String professorId) {
		//call the DAO class
		//get the courses for the professor
		List<Course> coursesOffered=new ArrayList<Course>();
		try
		{
			coursesOffered=professorobj.getCoursesByProfessor(professorId);
		}
		catch(Exception ex)
		{
			throw ex;
		}
		return coursesOffered;
	}

	@Override
	public List<EnrolledStudent> viewStudents(String professorId){
		// TODO Auto-generated method stub
		List<EnrolledStudent> enrolledStudents=new ArrayList<EnrolledStudent>();
		try
		{
			enrolledStudents=professorobj.getStudents(professorId);
		}
		catch(Exception ex)
		{
			throw ex;
		}
		return enrolledStudents;
	}

	@Override
	public boolean addGrade(String studentId, String courseId,int semester, String grade)
			throws StudentNotRegisteredException, StudentNotFoundForVerificationException, GradeNotAddedException {
		// TODO Auto-generated method stub
		try
		{
			professorobj.addGrade(studentId, courseId,semester, grade);
		}
		catch(Exception ex)
		{
			throw new GradeNotAddedException(studentId);
		}
		return true;
	}

}
