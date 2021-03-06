package rbccps.smartcity.IDEAM.APIs;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.stream.Collectors;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.StreamingOutput;

import org.json.simple.JSONObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.AMQP.Basic.Return;

import rbccps.smartcity.IDEAM.registerapi.broker.Pool;
import rbccps.smartcity.IDEAM.registerapi.ldap.LDAP;

public class RequestShare extends HttpServlet
{
	
	public static final long serialVersionUID = 1L;
	static JSONObject responseObj;
	static String response;
	static String authorization;
	static String X_Consumer_Custom_ID;
	static String X_Consumer_Username;
	static String apikey;
	static String X_Consumer_Groups;
	static String body;
	
	static JsonParser parser;
	static JsonElement jsonTree;
	static JsonObject jsonObject;
	
	static JsonElement entityID;
	static JsonElement requestorID;
	static String _entityID = null;
	static String _requestorID = null;
	
	static String share_entityID = null;
	static String [] share_entityIDs = null;
	
	static JsonElement permission;
	static String _permission = null;
	static String _read = null;
	static String _write = null;
	
	static JsonElement validity;
	static String _validity = null;
	static String _validityUnits = null;
	static String _expiryTime = null;
	static LocalDate _expireDate = null;
	static LocalTime _expiretime = null;
	static LocalDateTime _expiry = null; 
	static ZoneId zoneId = null;
	static long epoch;
	
	static String temp = null;
	static String rmq_pwd;
	static String ldap_pwd;
	
	static String[] decoded_authorization_datas = new String[2];
	static boolean isOwner = false;
	static boolean isdefault = false;
	static String share_exchange = null;
	static String _exchange = null;
	static boolean invalid_access_request = false;
	static boolean public_access_request = false;
	
	public static void readldappwd() 
	{	
		try
		{
			BufferedReader br=new BufferedReader(new FileReader("/etc/pwd"));
			ldap_pwd=br.readLine();			
			br.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try 
		{
			body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
			
			boolean flag = getshareinfo(body);
			
			checkentityID();
			
			jsonObject = new JsonObject();
			
			if(invalid_access_request) {
				invalid_access_request = false;
				response.setStatus(401);
				return;
			} else if (public_access_request) {
				public_access_request = false;
				jsonObject.addProperty("status", "failure");
				jsonObject.addProperty("reason", "No permission is required to access Public exchange.");
				response.setStatus(401);
				response.getWriter().println(jsonObject);
				return;
			}
			
			decoded_authorization_datas[0] = request.getHeader("X-Consumer-Username");
			decoded_authorization_datas[1] = request.getHeader("apikey");
			
			if ((LDAP.verifyProvider(share_entityID, decoded_authorization_datas))) {
				System.out.println("Device belongs to owner");
				isOwner = true;
			}

			if (!isOwner)
				if(!request.getHeader("X-Consumer-Username").equals(share_entityID))
					{
						response.setStatus(401);
						return;
					}
			
			isOwner = false;
			
			if(!flag)
			{
				response.setStatus(400);
				jsonObject.addProperty("status", "Failure");
				jsonObject.addProperty("reason", "Possible missing fields");
				response.getWriter().println(jsonObject);
				return;
			}
			
			String resp=sendsharerequest();
			
			if(resp.contains("Failed"))
			{
				response.setStatus(502);
			}
			else if(resp.contains("Request already shared"))
			{
				response.setStatus(409);
			}
			
			response.getWriter().println(resp);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			response.setStatus(502);
			return;
		}
	}
	
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		readldappwd();
		
		body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
		
		boolean flag = getshareinfo(body);
		
		checkentityID();
		
		jsonObject = new JsonObject();
		
		if(invalid_access_request) {
			invalid_access_request = false;
			response.setStatus(401);
			return;
		} else if (public_access_request) {
			public_access_request = false;
			jsonObject.addProperty("status", "failure");
			jsonObject.addProperty("reason", "No permission is required to access Public exchange.");
			response.setStatus(401);
			response.getWriter().println(jsonObject);
			return;
		}
		
		decoded_authorization_datas[0] = request.getHeader("X-Consumer-Username");
		decoded_authorization_datas[1] = request.getHeader("apikey");
		
		if ((LDAP.verifyProvider(share_entityID, decoded_authorization_datas))) {
			System.out.println("Device belongs to owner");
			isOwner = true;
		}

		if (!isOwner)
			if(!request.getHeader("X-Consumer-Username").equals(share_entityID))
			{
				response.setStatus(401);
				return;
			}
		
		isOwner = false;
		
		if(!flag)
		{
			response.setStatus(400);
			jsonObject.addProperty("status", "Failure");
			jsonObject.addProperty("reason", "Possible missing fields");
			response.getWriter().println(jsonObject);
			return;
		}
		
		Hashtable<String, Object> env = new Hashtable<String, Object>();
		
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://ldapd:8389");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "cn=admin,dc=smartcity");
		env.put(Context.SECURITY_CREDENTIALS, ldap_pwd);
		
		DirContext ctx=null;
		
		try 
		{
			ctx = new InitialDirContext(env);
		} 
		catch (NamingException e1) 
		{
			e1.printStackTrace();
		}
		
		try 
		{
			if(_permission.equals("read"))
			{ 
				ctx.destroySubcontext("description="+share_entityID+"."+_exchange+",description=read,description=share,description=broker,uid="+_requestorID+",cn=devices,dc=smartcity");
				boolean unbind=unbind();	
				
				if(!unbind)
				{
					response.setStatus(502);
					jsonObject.addProperty("status", "Failure");
					jsonObject.addProperty("reason", "Unable to unbind queue");
					response.getWriter().println(jsonObject);
					
				}
			}
			else if(_permission.equals("write"))
			{
				ctx.destroySubcontext("description="+share_entityID+".configure,description=write,description=share,description=broker,uid="+_requestorID+",cn=devices,dc=smartcity");

			}
			else if(_permission.equals("read-write"))
			{
				ctx.destroySubcontext("description="+share_entityID+"."+_exchange+",description=read,description=share,description=broker,uid="+_requestorID+",cn=devices,dc=smartcity");
				ctx.destroySubcontext("description="+share_entityID+".configure,description=write,description=share,description=broker,uid="+_requestorID+",cn=devices,dc=smartcity");
				
				boolean unbind=unbind();
				
				if(!unbind)
				{
					response.setStatus(502);
					jsonObject.addProperty("status", "Failure");
					jsonObject.addProperty("reason", "Unable to unbind queue");
					response.getWriter().println(jsonObject);
					
				}
			}
		}
		
		catch (NamingException e1) 
		{
			jsonObject.addProperty("status", "Failure");
			jsonObject.addProperty("reason", "Share entry does not exist");
			response.setStatus(404);
			response.getWriter().println(jsonObject);
			return;
		}
		
		jsonObject.addProperty("status", "success");
		jsonObject.addProperty("reason", "Successfully unshared");
		jsonObject.addProperty("entityID", _requestorID);
		jsonObject.addProperty("access", _exchange);
		response.getWriter().println(jsonObject);
	}
	
	public boolean getshareinfo(String json) 
	{	
		
		try
		{
			parser = new JsonParser();
			jsonTree = parser.parse(json);
			jsonObject = jsonTree.getAsJsonObject();
			
			System.out.println(jsonObject.toString());
			
			entityID = jsonObject.get("entityID");
			_entityID = entityID.toString().replace("\"", "");
			
			System.out.println(_entityID);
			
			requestorID = jsonObject.get("requestorID");
			_requestorID = requestorID.toString().replace("\"", "");
			
			System.out.println(_requestorID);
						
			permission = jsonObject.get("permission");
			_permission= permission.toString().replace("\"", "");
			
			if((!(_permission.equalsIgnoreCase("read")))&&(!(_permission.equalsIgnoreCase("write")))&&(!(_permission.equalsIgnoreCase("read-write"))))	
			return false;
			
			System.out.println(_permission);
			
			validity = jsonObject.get("validity");
			_validity= validity.toString().replace("\"", "");
			
			if((_validity.charAt(_validity.length()-1)!='M')&&(_validity.charAt(_validity.length()-1)!='Y')&&(_validity.charAt(_validity.length()-1)!='D'))
			return false;
			
			
			System.out.println(_validity);
			
			return true;
		}
		catch(Exception e)
		{
			return false;

		}
	}
	
	public static void checkentityID() {
		share_entityID = _entityID;
		if(share_entityID.contains("."))
		{
			isdefault = false;
			System.out.println(_entityID);
			System.out.println(_entityID.split("."));
			
			share_entityIDs = new String[2];
			share_entityIDs = _entityID.split("\\.");
			share_entityID = share_entityIDs[0];
			share_exchange = share_entityIDs[1];
			System.out.println(share_entityID);
			System.out.println(share_exchange);
			_exchange = share_exchange;
			
			if( ! ( _exchange.contains("private") || _exchange.contains("heartbeat") || _exchange.contains("public") || _exchange.contains("protected"))) {
				invalid_access_request = true;
			}
			
			if ( _exchange.contains("public") ) {
				public_access_request = true;
			} 
		} else {
			isdefault = true;
			_exchange = "protected";
		}
		
	}

	
	public static String sendsharerequest() 
	{
		
		readldappwd();
		
		boolean ldap=false,pub=false;
			
			
	    if(_validity.charAt(_validity.length()-1)=='Y') 
		{
			_validityUnits = "Year";
		} 
		else if(_validity.charAt(_validity.length()-1)=='M') 
		{
			_validityUnits = "Month";
		} 
		else if(_validity.charAt(_validity.length()-1)=='D') 
		{
			_validityUnits = "Day";
		} 
			
		System.out.println(_validityUnits);
			
		temp = _validity.substring(0, _validity.length()-1);
			
		System.out.println(temp);
			

		if(_validityUnits.equalsIgnoreCase("Year")) 
		{
			_expireDate = LocalDate.now().plusYears(Long.parseLong(temp));
		} 
		else if(_validityUnits.equalsIgnoreCase("Month")) 
		{
			_expireDate = LocalDate.now().plusMonths(Long.parseLong(temp));
		}  
		else if(_validityUnits.equalsIgnoreCase("Day")) 
		{
			_expireDate = LocalDate.now().plusDays(Long.parseLong(temp));	
		} 
			
		_expiretime = LocalTime.now();
			
		System.out.println("Expiry Date is : "+_expireDate.toString());
		System.out.println("Expiry Time is : "+_expiretime.toString()); 
			 
		_expiry = LocalDateTime.of(_expireDate, _expiretime);
			 
		System.out.println("Expiry is : "+_expiry.toString());
		zoneId = ZoneId.systemDefault();
		epoch = _expiry.atZone(zoneId).toInstant().toEpochMilli();
			 
		System.out.println("Epoch is : "+epoch);
		_validity = epoch+"";
			
		LDAP addShareEntryToLdap = new LDAP();
		ldap=addShareEntryToLdap.addShareEntry(share_entityID, _requestorID, _permission, _validity, _exchange);
		pub=publish(share_entityID, _requestorID, _exchange);
		
		JsonObject response=new JsonObject();
		if(ldap&&pub)
		{	
			response.addProperty("status","success");
			response.addProperty("info","Share request approved");
			response.addProperty("entityID",_requestorID);
			response.addProperty("access",_exchange);
			response.addProperty("permission",_permission);
			response.addProperty("validity",_expiry.toString());
		
		}
		else if (LDAP.entryexists){
			LDAP.entryexists = false;
			response.addProperty("status","failure");
			response.addProperty("reason", "Request already shared");
		}
		else
		{
			response.addProperty("status","failure");
			response.addProperty("reason", "Failed to approve share request");
		}
		
		return response.toString();
	}
	
	public static boolean publish(String entityID, String requestorID, String _exchange)
	{
			
		try
		{	
			JsonObject object=new JsonObject();
			
			object.addProperty("status","Approved");
			object.addProperty("info","You can now bind to "+entityID+"."+_exchange);
		
			
			Pool.getAdminChannel().basicPublish(requestorID+".notify", entityID+"."+_exchange, null, object.toString().getBytes("UTF-8"));
			
			return true;
		}
			
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	
	public static boolean unbind()
	{		
		try 
		{
			Map<String, Object> args=new HashMap<String, Object>();
			args.put("durable", "true");
			Pool.getAdminChannel().queueUnbind(_requestorID,_entityID+"."+_exchange,"#",args);
			
			return true;
			
		}
			
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
}
