/**
 * 
 */
package com.flipkart.restcontroller;
import static com.flipkart.constant.RoleConstants.STUDENT;

import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.Email;

import com.flipkart.bean.Student;
import com.flipkart.constant.RoleConstants;
import com.flipkart.exception.StudentNotRegisteredException;
import com.flipkart.exception.UserNotFoundException;
import com.flipkart.service.StudentInterface;
import com.flipkart.service.StudentOperation;
import com.flipkart.service.UserInterface;
import com.flipkart.service.UserOperation;

@Path("/user")
public class UserRestAPI {

	StudentInterface studentInterface=StudentOperation.getInstance();
	UserInterface userInterface =UserOperation.getInstance();
	
	
	
	@POST
	@Path("/login")
	public Response loginUser(
			@QueryParam("userId") String userId,
			@QueryParam("password") String password) throws ValidationException {
		try {
			
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
					return Response.status(200).entity("Login successful as admin with id "+userId).build();
					
				case PROFESSOR:
					return Response.status(200).entity("Login successful as professor with id "+userId).build();
					
				case STUDENT:
					StudentInterface studentInterface = new StudentOperation();
					int isApproved=0;
					try {
						isApproved = studentInterface.checkIsVerified(userId);
					} catch (StudentNotRegisteredException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (isApproved == 1) {
						return Response.status(200).entity("Login successful as student with id "+userId).build();

					} else {
						loggedin = false;
						return Response.status(200).entity("You have not been approved by the admin! , your id is "+userId).build();
						
					}
				}

			} else {
				return Response.status(500).entity("Invalid credentials!").build();
			}

		} catch (UserNotFoundException ex) {
			return Response.status(500).entity(ex.getMessage()).build();
		}
		return null;
		
		
}
	
	
	
	@POST
	@Path("/studentRegistration")
	public Response registerStudent(@QueryParam("stuname") String stuname,
			@QueryParam("stumob") String stumob,
			@QueryParam("stuadd") String stuadd,
			@QueryParam("stuid") String stuid,
			@QueryParam("stupass") String stupass,
			@QueryParam("sturoll") String sturoll,
			@QueryParam("stubr") String stubr)
	{
		
		System.out.println(stuname+" "+stubr);
		
		try
		{
			StudentOperation studentOperation = new StudentOperation();
			
			studentOperation.registerStudent(stuname, stumob, stuadd, stuid, stupass, String.valueOf(STUDENT),
					sturoll, stubr, false);

		}
		catch(Exception ex)
		{
			return Response.status(500).entity("Something went wrong! Please try again.").build(); 
		}
		
		
		return Response.status(201).entity("Registration Successful for "+stuid).build(); 
	}
	
}
