/**
 * 
 */
package com.flipkart.restcontroller;


import javax.validation.ValidationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import java.sql.SQLException;
import java.util.*;

import com.flipkart.service.ProfessorInterface;
import com.flipkart.service.ProfessorOperation;
import org.apache.log4j.Logger;
import com.flipkart.bean.Course;
import com.flipkart.bean.Payment;
import com.flipkart.bean.ReportCard;
import com.flipkart.exception.AddCourseException;
import com.flipkart.exception.CourseAlreadyRegisteredException;
import com.flipkart.exception.CourseLimitReachedException;
import com.flipkart.exception.CourseNotDeletedException;
import com.flipkart.exception.CourseNotFoundException;
import com.flipkart.exception.PaymentNotFoundException;
import com.flipkart.exception.SeatNotAvailableException;
import com.flipkart.service.RegistrationInterface;
import com.flipkart.service.RegistrationOperation;




@Path("/student")
public class StudentRestAPI {
	
	RegistrationInterface registrationInterface = RegistrationOperation.getInstance();
	ProfessorInterface professorInterface = ProfessorOperation.getInstance();
	
	private static Logger logger = Logger.getLogger(StudentRestAPI.class);
	
	
	
	@POST
	@Path("/registerCourses")
	@Consumes("application/json")
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerCourses(List<String> courseList, 
			@NotNull
			@QueryParam("studentId") String studentId,
			@NotNull
			
			@QueryParam("semester") int semester)	throws ValidationException{
						
		try
		{
			List<Course> availableCourseList = registrationInterface.viewCourses(studentId,1);
			Set<String> hash_set = new HashSet<String>();
			
			for(String courseCode:courseList) {
				
					if(!hash_set.add(courseCode))
						return Response.status(500).entity("Duplicate value  : "+courseCode).build();
			}

			for(String courseCode:courseList)
				registrationInterface.addCourse(courseCode, studentId,semester);
		}
		catch (AddCourseException | CourseNotFoundException | CourseLimitReachedException | SQLException e) {
			System.out.println(e.getMessage());
			return Response.status(500).entity(e.getMessage()).build();
		}			
		
		return Response.status(201).entity( "Registration Successful").build();
		
}
	


	
	
	@PUT
	@Path("/addCourse")
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCourse(
			@NotNull
			@QueryParam("courseCode") String courseId,
			@NotNull
			@QueryParam("studentId") String studentId,
			@NotNull
			@QueryParam("semester") int semester) throws ValidationException{
	
		try{
			
			List<Course> availCourseList = registrationInterface.viewCourses(studentId,semester);
				registrationInterface.addCourse(courseId, studentId,semester);
			
			return Response.status(201).entity( "You have successfully added Course : " + courseId).build();
			
		}
		catch (AddCourseException | CourseNotFoundException | CourseLimitReachedException | SQLException e) {
			System.out.println(e.getMessage());
			return Response.status(500).entity(e.getMessage()).build();

		}

	}

	
	
	@DELETE
	@Path("/dropCourse")
	@Produces(MediaType.APPLICATION_JSON)
	public Response dropCourse(
			@NotNull
			@QueryParam("courseCode") String courseId,
			@NotNull
			@QueryParam("studentId") String studentId,
			@NotNull
			@QueryParam("semester") int semester) throws ValidationException{
		
		try{
			
			List<Course>registeredCourseList = registrationInterface.viewRegisteredCourses(studentId,semester);
			registrationInterface.dropCourse(courseId, studentId, semester);
			return Response.status(201).entity( "Successfully dropped Course : " + courseId).build();
		}
		catch(CourseNotFoundException | SQLException | CourseNotDeletedException e)
		{	
			logger.info(e.getMessage());
			return Response.status(501).entity(e.getMessage()).build();
		} 
		
	}
	
	
	
	@GET
	@Path("/viewAvailableCourses")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> viewCourse(
			@NotNull
			@QueryParam("studentId") String studentId,
			@NotNull
	
			@QueryParam("semester") int semester) throws ValidationException{
		
		try {
			return registrationInterface.viewCourses(studentId,semester);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	@GET
	@Path("/viewRegisteredCourses")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Course> viewRegisteredCourse(
			@NotNull
			@QueryParam("studentId") String studentId,
			@NotNull
			@QueryParam("semester") int semester) throws ValidationException{
		
			try {
				return registrationInterface.viewRegisteredCourses(studentId,semester);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}
	
	
	
	//paymentId,studentId,amount,status,notificationId,semester
	@POST
	@Path("/make_payment")
	@Produces(MediaType.APPLICATION_JSON)
	public Response make_payment(
			@NotNull
			@QueryParam("studentId") String studentId , 
			@NotNull
			@QueryParam("semester") int semester) throws ValidationException{
		
			List<Course> course_registered = null;
			try {
				course_registered = registrationInterface.viewRegisteredCourses(studentId, semester);
				if (course_registered.isEmpty()) {
					return Response.status(201).entity( "You haven't registered for any course").build();
				}
			} catch (SQLException e) {
	
				logger.info(e.getMessage());
				return Response.status(501).entity(e.getMessage()).build();
				
			}
			String notificationId;
			int fee= 0;
			try {
			Payment viewPayment = registrationInterface.viewFee(studentId, semester);
			
			if(viewPayment != null) {
				return Response.status(201).entity( "You have already completed the payment.").build();
			}
			fee = course_registered.size() * 1000;

			logger.info("Your total fee  = " + fee);
			String paymentId = studentId + Integer.toString(semester) + Integer.toString(fee);
			StringBuilder in = new StringBuilder();
			in.append(paymentId);
			in.reverse();
			notificationId = in.toString() ;
			String status = "success";
			Payment payment = new Payment(paymentId,studentId,fee,status,notificationId,semester);
			
				registrationInterface.payFee(payment);
			} catch( PaymentNotFoundException |SQLException ex) {
				logger.error(ex.getMessage());
				return Response.status(201).entity( "You have already completed the payment.").build();
			}

			
			logger.info("Your Payment is successful");
			logger.info("Your transaction id : " + notificationId);
			
			return Response.status(501).entity("Your total fee  = " + fee+"\n"+"Your Payment is successful\n"+"Your transaction id : " + notificationId).build();
		
	}
	
	
	
	@GET
	@Path("/viewGradeCard")
	@Produces(MediaType.APPLICATION_JSON)
	public ReportCard viewGradeCard(
			@NotNull
			@QueryParam("studentId") String studentId,
			@NotNull
			@QueryParam("semester") int semester) throws ValidationException{
		
		
			ReportCard grade_card =null;
			try {
				grade_card = registrationInterface.viewReportCard(studentId,semester);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return grade_card;
		
	}
	
}