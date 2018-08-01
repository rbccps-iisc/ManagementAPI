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
		
		manager = Servlets.defaultContainer().addDeployment(servletBuilder);
		manager.deploy();
		PathHandler path = Handlers.path(Handlers.redirect("/cdx")).addPrefixPath("/cdx", manager.start());
		Undertow server = Undertow.builder().addHttpListener(8888, "localhost").setHandler(path).build();
		server.start();
	}
}