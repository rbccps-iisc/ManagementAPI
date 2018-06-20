package rbccps.smartcity.IDEAM.APIs;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

@Path("/share")
public class RequestShare extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@GET
	public void doGet(@Context HttpServletResponse response) throws IOException{
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++");
		System.out.println("In RequestRedirect");
		System.out.println("++++++++++++++++++++++++++++++++++++++++++");
		response.sendRedirect("http://rbccps.org/smartcity/");
		return;
}
}
