/**
 * 
 */
package com.flipkart.restcontroller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.flipkart.bean.Customer;

@Path("/customer")
public class CustomerRestApi {
	
	@GET
	@Path("/customerdetails")
	@Produces(MediaType.APPLICATION_JSON)
	public Customer getCustomer() {
		Customer customer = new Customer();
		customer.setCustid(1);
		customer.setCustName("gjbuy");
		customer.setCustAddress("customer address");
		
		System.out.println("bhaisahab");
		
	   return customer;

	}

}
