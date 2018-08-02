package rbccps.smartcity.IDEAM.APIs;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;

public class cdxapis {

	static DeploymentInfo servletBuilder;
	static DeploymentManager manager;
	
	public static void main(String... args) throws Exception {
		
		servletBuilder = Servlets.deployment().setClassLoader(cdxapis.class.getClassLoader());
		servletBuilder.setDeploymentName("cdx").setContextPath("/cdx");
		
		servletBuilder.addServlets(Servlets.servlet("redirect", new RequestRedirect().getClass()).addMapping("/redirect"));
		servletBuilder.addServlets(Servlets.servlet("follow", new RequestFollow().getClass()).addMapping("/follow"));
		servletBuilder.addServlets(Servlets.servlet("share", new RequestShare().getClass()).addMapping("/share"));
		servletBuilder.addServlets(Servlets.servlet("search", new RequestSearch().getClass()).addMapping("/search"));
		servletBuilder.addServlets(Servlets.servlet("register", new RequestRegister().getClass()).addMapping("/register"));
		
		manager = Servlets.defaultContainer().addDeployment(servletBuilder);
		manager.deploy();
		PathHandler path = Handlers.path(Handlers.redirect("/cdx")).addPrefixPath("/cdx", manager.start());
		Undertow server = Undertow.builder().addHttpListener(8080, "0.0.0.0").setHandler(path).build();
		server.start();
	}
}