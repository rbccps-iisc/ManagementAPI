package rbccps.smartcity.IDEAM.registerapi.ldap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import rbccps.smartcity.IDEAM.urls.URLs;

public class LDAP {

	private static DirContext dirContext = null;
	static String url = URLs.getLDAPURL();
	static String conntype = "simple";
	static String AdminDn = "cn=admin,dc=smartcity";
	static String password = "";
	static Hashtable<String, String> environment = new Hashtable<String, String>();

	static String entryDN;
	static String brokerEntryDN;
	static String brokerExchangeEntryDN;
	static String brokerExchange_DeviceName_EntryDN;
	static String brokerExchange_DeviceName_Configure_EntryDN;
	static String brokerExchange_DeviceName_Private_EntryDN;
	static String brokerExchange_DeviceName_Public_EntryDN;
	static String brokerExchange_DeviceName_Protected_EntryDN;
	static String brokerExchange_DeviceName_Follow_EntryDN;
	static String brokerExchange_DeviceName_Notify_EntryDN;
	static String brokerQueueEntryDN;
	static String brokerQueue_Name_EntryDN;
	static String brokerQueue_Name_Follow_EntryDN;
	static String brokerQueue_Name_Notify_EntryDN;
	static String brokerShareEntryDN;
	static String brokerShare_Name_EntryDN;
	static String LDAPEntry;

	public static void readldappwd() {
		System.out.println("constructer to LDAP bind");
		
		try
		{
			BufferedReader br=new BufferedReader(new FileReader("/etc/pwd"));
			
			password=br.readLine();			
			br.close();
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		try {
			environment.put(Context.INITIAL_CONTEXT_FACTORY,
					"com.sun.jndi.ldap.LdapCtxFactory");
			environment.put(Context.PROVIDER_URL, url);
			environment.put(Context.SECURITY_AUTHENTICATION, conntype);
			environment.put(Context.SECURITY_PRINCIPAL, AdminDn);
			environment.put(Context.SECURITY_CREDENTIALS, password);
			dirContext = new InitialDirContext(environment);
			System.out.println("Bind successful");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	// Attributes to be set for new entry creation
	public boolean addRegistrationEntry(String providerId, String userId, String apiKey) {
		boolean flag = false;

		System.out.println(providerId + userId + apiKey);

		Attribute OWNER = new BasicAttribute("owner", providerId);
		Attribute PASSWORD = new BasicAttribute("userPassword", apiKey);
		Attribute BLOCK = new BasicAttribute("block", "false");

		// ObjectClass attributes
		Attribute oc = new BasicAttribute("objectClass");
		oc.add("broker");
		oc.add("exchange");
		oc.add("queue");
		oc.add("share");

		Attributes entry = new BasicAttributes();

		entry.put(OWNER);
		entry.put(PASSWORD);
		entry.put(BLOCK);
		entry.put(oc);

		entryDN = "uid=" + userId + ",cn=devices,dc=smartcity";
		System.out.println("entryDN :" + entryDN + " Entry :"
				+ entry.toString());

		// Broker Object
		// Broker Entry
		brokerEntryDN = "description=broker," + "uid=" + userId
				+ ",cn=devices,dc=smartcity";
		Attributes brokerEntry = new BasicAttributes();
		Attribute brokerread = new BasicAttribute("read", "true");
		Attribute brokerwrite = new BasicAttribute("write", "true");

		brokerEntry.put(brokerread);
		brokerEntry.put(brokerwrite);
		brokerEntry.put(oc);

		// Exchange

		brokerExchangeEntryDN = "description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchangeEntry = new BasicAttributes();
		// Attribute exchangeread = new BasicAttribute("read", "true");
		// Attribute exchangewrite = new BasicAttribute("write", "true");

		brokerExchangeEntry.put(oc);
		// brokerExchangeEntry.put(exchangeread);
		// brokerExchangeEntry.put(exchangewrite);

		// Exchange Name to which User can Read / Write

		brokerExchange_DeviceName_EntryDN = "description=" + userId
				+ ",description=exchange,description=broker," + "uid=" + userId
				+ ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_read = new BasicAttribute("read", "true");
		Attribute exchange_DeviceName_write = new BasicAttribute("write",
				"true");

		brokerExchange_DeviceName_Entry.put(oc);
		brokerExchange_DeviceName_Entry.put(exchange_DeviceName_read);
		brokerExchange_DeviceName_Entry.put(exchange_DeviceName_write);

		// Exchange Name (Configure) to which User can Read / Write

		brokerExchange_DeviceName_Configure_EntryDN = "description=" + userId
				+ ".configure,description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Configure_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_Configure_read = new BasicAttribute(
				"read", "true");
		Attribute exchange_DeviceName_Configure_write = new BasicAttribute(
				"write", "true");

		brokerExchange_DeviceName_Configure_Entry.put(oc);
		brokerExchange_DeviceName_Configure_Entry
				.put(exchange_DeviceName_Configure_read);
		brokerExchange_DeviceName_Configure_Entry
				.put(exchange_DeviceName_Configure_write);

		// Exchange Name (Protected) to which User can Read / Write

		brokerExchange_DeviceName_Protected_EntryDN = "description=" + userId
				+ ".protected,description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Protected_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_Protected_read = new BasicAttribute(
				"read", "true");
		Attribute exchange_DeviceName_Protected_write = new BasicAttribute(
				"write", "true");

		brokerExchange_DeviceName_Protected_Entry.put(oc);
		brokerExchange_DeviceName_Protected_Entry
				.put(exchange_DeviceName_Protected_read);
		brokerExchange_DeviceName_Protected_Entry
				.put(exchange_DeviceName_Protected_write);
		
		// Exchange Name (Private) to which User can Read / Write

		brokerExchange_DeviceName_Private_EntryDN = "description=" + userId
				+ ".private,description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Private_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_Private_read = new BasicAttribute(
				"read", "true");
		Attribute exchange_DeviceName_Private_write = new BasicAttribute(
				"write", "true");

		brokerExchange_DeviceName_Private_Entry.put(oc);
		brokerExchange_DeviceName_Private_Entry
				.put(exchange_DeviceName_Private_read);
		brokerExchange_DeviceName_Private_Entry
				.put(exchange_DeviceName_Private_write);

		// Exchange Name (Public) to which User can Read / Write

		brokerExchange_DeviceName_Public_EntryDN = "description=" + userId
				+ ".public,description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Public_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_Public_read = new BasicAttribute(
				"read", "true");
		Attribute exchange_DeviceName_Public_write = new BasicAttribute(
				"write", "true");

		brokerExchange_DeviceName_Public_Entry.put(oc);
		brokerExchange_DeviceName_Public_Entry
				.put(exchange_DeviceName_Public_read);
		brokerExchange_DeviceName_Public_Entry
				.put(exchange_DeviceName_Public_write);

		
		// Exchange Name (Protected) to which User can Read / Write

		brokerExchange_DeviceName_Follow_EntryDN = "description=" + userId
				+ ".follow,description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Follow_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_Follow_read = new BasicAttribute(
				"read", "true");
		Attribute exchange_DeviceName_Follow_write = new BasicAttribute(
				"write", "true");

		brokerExchange_DeviceName_Follow_Entry.put(oc);
		brokerExchange_DeviceName_Follow_Entry
				.put(exchange_DeviceName_Follow_read);
		brokerExchange_DeviceName_Follow_Entry
				.put(exchange_DeviceName_Follow_write);

		
		// Exchange Name (Notify) to which User can Read / Write

		brokerExchange_DeviceName_Notify_EntryDN = "description=" + userId
				+ ".notify,description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Notify_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_Notify_read = new BasicAttribute(
				"read", "true");
		Attribute exchange_DeviceName_Notify_write = new BasicAttribute(
				"write", "true");

		brokerExchange_DeviceName_Notify_Entry.put(oc);
		brokerExchange_DeviceName_Notify_Entry
				.put(exchange_DeviceName_Notify_read);
		brokerExchange_DeviceName_Notify_Entry
				.put(exchange_DeviceName_Notify_write);
	
		// Queue

		brokerQueueEntryDN = "description=queue,description=broker," + "uid="
				+ userId + ",cn=devices,dc=smartcity";

		Attributes brokerQueueEntry = new BasicAttributes();
		Attribute queueread = new BasicAttribute("read", "true");
		Attribute queuewrite = new BasicAttribute("write", "true");

		brokerQueueEntry.put(oc);
		brokerQueueEntry.put(queueread);
		brokerQueueEntry.put(queuewrite);

		// Queue (Name or ID) from which User can Read

		brokerQueue_Name_EntryDN = "description=" + userId
				+ ",description=queue,description=broker," + "uid=" + userId
				+ ",cn=devices,dc=smartcity";

		Attributes brokerQueue_Name_Entry = new BasicAttributes();
		Attribute queue_Name_read = new BasicAttribute("read", "true");
		Attribute queue_Name_write = new BasicAttribute("write", "true");

		brokerQueue_Name_Entry.put(oc);
		brokerQueue_Name_Entry.put(queue_Name_read);
		brokerQueue_Name_Entry.put(queue_Name_write);

		// Queue Follow (Name or ID) from which User can Read

		brokerQueue_Name_Follow_EntryDN = "description=" + userId + ".follow"
				+ ",description=queue,description=broker," + "uid=" + userId
				+ ",cn=devices,dc=smartcity";

		Attributes brokerQueue_Follow_Name_Entry = new BasicAttributes();
		Attribute queue_Follow_Name_read = new BasicAttribute("read", "true");
		Attribute queue_Follow_Name_write = new BasicAttribute("write", "true");

		brokerQueue_Follow_Name_Entry.put(oc);
		brokerQueue_Follow_Name_Entry.put(queue_Follow_Name_read);
		brokerQueue_Follow_Name_Entry.put(queue_Follow_Name_write);
		
		// Queue Notify (Name or ID) from which User can Read

		brokerQueue_Name_Notify_EntryDN = "description=" + userId + ".notify"
				+ ",description=queue,description=broker," + "uid=" + userId
				+ ",cn=devices,dc=smartcity";

		Attributes brokerQueue_Notify_Name_Entry = new BasicAttributes();
		Attribute queue_Notify_Name_read = new BasicAttribute("read", "true");
		Attribute queue_Notify_Name_write = new BasicAttribute("write", "true");

		brokerQueue_Notify_Name_Entry.put(oc);
		brokerQueue_Notify_Name_Entry.put(queue_Notify_Name_read);
		brokerQueue_Notify_Name_Entry.put(queue_Notify_Name_write);
		
		// Share

		brokerShareEntryDN = "description=share,description=broker," + "uid="
				+ userId + ",cn=devices,dc=smartcity";

		Attributes brokerShareEntry = new BasicAttributes();
		Attribute shareread = new BasicAttribute("read", "true");
		Attribute sharewrite = new BasicAttribute("write", "true");

		brokerShareEntry.put(oc);
		brokerShareEntry.put(shareread);
		brokerShareEntry.put(sharewrite);

		// Share (Name or ID) from which User can Read

		brokerShare_Name_EntryDN = "description=" + userId
				+ ",description=share,description=broker," + "uid=" + userId
				+ ",cn=devices,dc=smartcity";

		Attributes brokerShare_Name_Entry = new BasicAttributes();
		Attribute Share_Name_read = new BasicAttribute("read", "true");
		Attribute Share_Name_write = new BasicAttribute("write", "true");

		brokerShare_Name_Entry.put(oc);
		brokerShare_Name_Entry.put(Share_Name_read);
		brokerShare_Name_Entry.put(Share_Name_write);


		try {
			dirContext.createSubcontext(entryDN, entry);
			dirContext.createSubcontext(brokerEntryDN, brokerEntry);
			dirContext.createSubcontext(brokerExchangeEntryDN,
					brokerExchangeEntry);
			dirContext.createSubcontext(brokerExchange_DeviceName_EntryDN,
					brokerExchange_DeviceName_Entry);
			dirContext.createSubcontext(
					brokerExchange_DeviceName_Configure_EntryDN,
					brokerExchange_DeviceName_Configure_Entry);
			dirContext.createSubcontext(
					brokerExchange_DeviceName_Private_EntryDN,
					brokerExchange_DeviceName_Private_Entry);
			dirContext.createSubcontext(
					brokerExchange_DeviceName_Public_EntryDN,
					brokerExchange_DeviceName_Public_Entry);
			dirContext.createSubcontext(
					brokerExchange_DeviceName_Protected_EntryDN,
					brokerExchange_DeviceName_Protected_Entry);
			dirContext.createSubcontext(
					brokerExchange_DeviceName_Follow_EntryDN,
					brokerExchange_DeviceName_Follow_Entry);
			dirContext.createSubcontext(
					brokerExchange_DeviceName_Notify_EntryDN,
					brokerExchange_DeviceName_Notify_Entry);
			dirContext.createSubcontext(brokerQueueEntryDN, brokerQueueEntry);
			dirContext.createSubcontext(brokerQueue_Name_EntryDN,
					brokerQueue_Name_Entry);
			dirContext.createSubcontext(brokerQueue_Name_Follow_EntryDN,
					brokerQueue_Follow_Name_Entry);
			dirContext.createSubcontext(brokerQueue_Name_Notify_EntryDN,
					brokerQueue_Notify_Name_Entry);
			dirContext.createSubcontext(brokerShareEntryDN, brokerShareEntry);
			dirContext.createSubcontext(brokerShare_Name_EntryDN,
					brokerShare_Name_Entry);

			flag = true;

			System.out.println("entryDN : "+entryDN );
			System.out.println("brokerEntryDN : "+brokerEntryDN);
			System.out.println("brokerExchangeEntryDN : "+brokerExchangeEntryDN);
			System.out.println("brokerExchange_DeviceName_EntryDN : "+brokerExchange_DeviceName_EntryDN);
			System.out.println("brokerExchange_DeviceName_Configure_EntryDN : "+brokerExchange_DeviceName_Configure_EntryDN);
			System.out.println("brokerExchange_DeviceName_Private_EntryDN : "+brokerExchange_DeviceName_Private_EntryDN);
			System.out.println("brokerExchange_DeviceName_Protected_EntryDN : "+brokerExchange_DeviceName_Protected_EntryDN);
			System.out.println("brokerExchange_DeviceName_Public_EntryDN : "+brokerExchange_DeviceName_Public_EntryDN);
			System.out.println("brokerExchange_DeviceName_Follow_EntryDN : "+brokerExchange_DeviceName_Follow_EntryDN);
			System.out.println("brokerExchange_DeviceName_Notify_EntryDN : "+brokerExchange_DeviceName_Notify_EntryDN);
			System.out.println("brokerQueueEntryDN : "+brokerQueueEntryDN);
			System.out.println("brokerQueue_Name_EntryDN : "+brokerQueue_Name_EntryDN);
			System.out.println("brokerQueue_Name_Follow_EntryDN : "+brokerQueue_Name_Follow_EntryDN);
			System.out.println("brokerQueue_Name_Notify_EntryDN : "+brokerQueue_Name_Notify_EntryDN);
			System.out.println("brokerShareEntryDN : "+brokerShareEntryDN);
			System.out.println("brokerShare_Name_EntryDN : "+brokerShare_Name_EntryDN);

		} catch (Exception e) {
			System.out.println("error: " + e.getMessage());
			return flag;
		}
		return flag;
	}
	
	// Attributes to be set for new entry creation
		public boolean addsubscriberRegistrationEntry(String providerId, String userId, String apiKey) {
			boolean flag = false;

			System.out.println(providerId + userId + apiKey);

			Attribute OWNER = new BasicAttribute("owner", providerId);
			Attribute PASSWORD = new BasicAttribute("userPassword", apiKey);
			Attribute BLOCK = new BasicAttribute("block", "false");

			// ObjectClass attributes
			Attribute oc = new BasicAttribute("objectClass");
			oc.add("broker");
			oc.add("exchange");
			oc.add("queue");
			oc.add("share");

			Attributes entry = new BasicAttributes();

			entry.put(OWNER);
			entry.put(PASSWORD);
			entry.put(BLOCK);
			entry.put(oc);

			entryDN = "uid=" + userId + ",cn=devices,dc=smartcity";
			System.out.println("entryDN :" + entryDN + " Entry :"
					+ entry.toString());

			// Broker Object
			// Broker Entry
			brokerEntryDN = "description=broker," + "uid=" + userId
					+ ",cn=devices,dc=smartcity";
			Attributes brokerEntry = new BasicAttributes();
			Attribute brokerread = new BasicAttribute("read", "true");
			Attribute brokerwrite = new BasicAttribute("write", "true");

			brokerEntry.put(brokerread);
			brokerEntry.put(brokerwrite);
			brokerEntry.put(oc);

			// Exchange

			brokerExchangeEntryDN = "description=exchange,description=broker,"
					+ "uid=" + userId + ",cn=devices,dc=smartcity";

			Attributes brokerExchangeEntry = new BasicAttributes();
	
			brokerExchangeEntry.put(oc);
			
			// Exchange Name (Notify) to which User can Read / Write

			brokerExchange_DeviceName_Notify_EntryDN = "description=" + userId
					+ ".notify,description=exchange,description=broker,"
					+ "uid=" + userId + ",cn=devices,dc=smartcity";

			Attributes brokerExchange_DeviceName_Notify_Entry = new BasicAttributes();
			Attribute exchange_DeviceName_Notify_read = new BasicAttribute(
					"read", "true");
			Attribute exchange_DeviceName_Notify_write = new BasicAttribute(
					"write", "true");

			brokerExchange_DeviceName_Notify_Entry.put(oc);
			brokerExchange_DeviceName_Notify_Entry
					.put(exchange_DeviceName_Notify_read);
			brokerExchange_DeviceName_Notify_Entry
					.put(exchange_DeviceName_Notify_write);

			
			// Queue

			brokerQueueEntryDN = "description=queue,description=broker," + "uid="
					+ userId + ",cn=devices,dc=smartcity";

			Attributes brokerQueueEntry = new BasicAttributes();
			Attribute queueread = new BasicAttribute("read", "true");
			Attribute queuewrite = new BasicAttribute("write", "true");

			brokerQueueEntry.put(oc);
			brokerQueueEntry.put(queueread);
			brokerQueueEntry.put(queuewrite);

			// Queue (Name or ID) from which User can Read

			brokerQueue_Name_EntryDN = "description=" + userId
					+ ",description=queue,description=broker," + "uid=" + userId
					+ ",cn=devices,dc=smartcity";

			Attributes brokerQueue_Name_Entry = new BasicAttributes();
			Attribute queue_Name_read = new BasicAttribute("read", "true");
			Attribute queue_Name_write = new BasicAttribute("write", "true");

			brokerQueue_Name_Entry.put(oc);
			brokerQueue_Name_Entry.put(queue_Name_read);
			brokerQueue_Name_Entry.put(queue_Name_write);

			// Queue Notify (Name or ID) from which User can Read

			brokerQueue_Name_Notify_EntryDN = "description=" + userId + ".notify"
					+ ",description=queue,description=broker," + "uid=" + userId
					+ ",cn=devices,dc=smartcity";

			Attributes brokerQueue_Notify_Name_Entry = new BasicAttributes();
			Attribute queue_Notify_Name_read = new BasicAttribute("read", "true");
			Attribute queue_Notify_Name_write = new BasicAttribute("write", "true");

			brokerQueue_Notify_Name_Entry.put(oc);
			brokerQueue_Notify_Name_Entry.put(queue_Notify_Name_read);
			brokerQueue_Notify_Name_Entry.put(queue_Notify_Name_write);
			
			// Share

			brokerShareEntryDN = "description=share,description=broker," + "uid="
					+ userId + ",cn=devices,dc=smartcity";

			Attributes brokerShareEntry = new BasicAttributes();
			Attribute shareread = new BasicAttribute("read", "true");
			Attribute sharewrite = new BasicAttribute("write", "true");

			brokerShareEntry.put(oc);
			brokerShareEntry.put(shareread);
			brokerShareEntry.put(sharewrite);

			// Share (Name or ID) from which User can Read

			brokerShare_Name_EntryDN = "description=" + userId
					+ ",description=share,description=broker," + "uid=" + userId
					+ ",cn=devices,dc=smartcity";

			Attributes brokerShare_Name_Entry = new BasicAttributes();
			Attribute Share_Name_read = new BasicAttribute("read", "true");
			Attribute Share_Name_write = new BasicAttribute("write", "true");

			brokerShare_Name_Entry.put(oc);
			brokerShare_Name_Entry.put(Share_Name_read);
			brokerShare_Name_Entry.put(Share_Name_write);

			try {
				dirContext.createSubcontext(entryDN, entry);
				dirContext.createSubcontext(brokerEntryDN, brokerEntry);
				dirContext.createSubcontext(brokerExchangeEntryDN,
						brokerExchangeEntry);
				dirContext.createSubcontext(
						brokerExchange_DeviceName_Notify_EntryDN,
						brokerExchange_DeviceName_Notify_Entry);
				dirContext.createSubcontext(brokerQueueEntryDN, brokerQueueEntry);
				dirContext.createSubcontext(brokerQueue_Name_EntryDN,
						brokerQueue_Name_Entry);
				dirContext.createSubcontext(brokerQueue_Name_Notify_EntryDN,
						brokerQueue_Notify_Name_Entry);
				dirContext.createSubcontext(brokerShareEntryDN, brokerShareEntry);
				dirContext.createSubcontext(brokerShare_Name_EntryDN,
						brokerShare_Name_Entry);

				flag = true;

				System.out.println("entryDN : "+entryDN );
				System.out.println("brokerEntryDN : "+brokerEntryDN);
				System.out.println("brokerExchangeEntryDN : "+brokerExchangeEntryDN);
				System.out.println("brokerExchange_DeviceName_EntryDN : "+brokerExchange_DeviceName_EntryDN);
				System.out.println("brokerExchange_DeviceName_Notify_EntryDN : "+brokerExchange_DeviceName_Notify_EntryDN);
				System.out.println("brokerQueueEntryDN : "+brokerQueueEntryDN);
				System.out.println("brokerQueue_Name_EntryDN : "+brokerQueue_Name_EntryDN);
				System.out.println("brokerShareEntryDN : "+brokerShareEntryDN);
				System.out.println("brokerShare_Name_EntryDN : "+brokerShare_Name_EntryDN);

			} catch (Exception e) {
				System.out.println("error: " + e.getMessage());
				return flag;
			}
			return flag;
		}
	
	// Attributes to be set for new entry creation
	public boolean addVideoEntry(String providerId, String userId, String apiKey) {
		boolean flag = false;
		System.out.println(providerId + userId + apiKey);

		Attribute OWNER = new BasicAttribute("owner", providerId);
		Attribute PASSWORD = new BasicAttribute("userPassword", apiKey);
		Attribute BLOCK = new BasicAttribute("block", "false");
		
		// ObjectClass attributes
		Attribute oc = new BasicAttribute("objectClass");
		oc.add("video");
		oc.add("share");

		Attributes entry = new BasicAttributes();

		entry.put(OWNER);
		entry.put(PASSWORD);
		entry.put(BLOCK);
		entry.put(oc);

		String entryDN = "uid=" + userId + ",cn=devices,dc=smartcity";
		System.out.println("entryDN :" + entryDN + " Entry :"
				+ entry.toString());

		// video Object
		// video Entry
		String videoEntryDN = "description=video," + "uid=" + userId
				+ ",cn=devices,dc=smartcity";
		Attributes videoEntry = new BasicAttributes();
		Attribute videoread = new BasicAttribute("read", "true");
		Attribute videowrite = new BasicAttribute("write", "true");

		videoEntry.put(videoread);
		videoEntry.put(videowrite);
		videoEntry.put(oc);

		try {
			dirContext.createSubcontext(entryDN, entry);
			dirContext.createSubcontext(videoEntryDN, videoEntry);
			flag = true;

		} catch (Exception e) {
			System.out.println("error: " + e.getMessage());
			return flag;
		}
		return flag;
	}
	
	// Attributes to be set for new entry creation
	public boolean addShareEntry(String providerId, String userId, String read, String write, String validity) {
		boolean flag = false;
		System.out.println(providerId + " : " + userId + " : " + read + " : " + write  + " : " + validity);

		readldappwd();
		
		// ObjectClass attributes
		Attribute oc = new BasicAttribute("objectClass");
		oc.add("broker");
		oc.add("exchange");
		oc.add("queue");
		oc.add("share");
		
		// Share
		brokerShareEntryDN = "description=share,description=broker," + "uid="
				+ providerId + ",cn=devices,dc=smartcity";

		Attributes brokerShareEntry = new BasicAttributes();
		Attribute shareread = new BasicAttribute("read", read);
		Attribute sharewrite = new BasicAttribute("write", write);
		Attribute sharevalidity = new BasicAttribute("validity", validity);
	
		brokerShareEntry.put(shareread);
		brokerShareEntry.put(sharewrite);
		brokerShareEntry.put(sharevalidity);

		// Share (Name or ID) of requested user

		brokerShare_Name_EntryDN = "description=" + userId
				+ ",description=share,description=broker," + "uid=" + providerId
				+ ",cn=devices,dc=smartcity";

		Attributes brokerShare_Name_Entry = new BasicAttributes();
		Attribute Share_Name_read = new BasicAttribute("read", read);
		Attribute Share_Name_write = new BasicAttribute("write", write);
		Attribute Share_Name_validity = new BasicAttribute("validity", validity);

		brokerShare_Name_Entry.put(oc);
		brokerShare_Name_Entry.put(Share_Name_read);
		brokerShare_Name_Entry.put(Share_Name_write);
		brokerShare_Name_Entry.put(Share_Name_validity);
		
		System.out.println("brokerShare_Name_EntryDN : "+brokerShare_Name_EntryDN);
		
		try {
			dirContext.createSubcontext(brokerShare_Name_EntryDN,
					brokerShare_Name_Entry);
			flag = true;

		} catch (Exception e) {
			System.out.println("error: " + e.getMessage());
			return flag;
		}
		
		System.out.println("brokerShare_Name_EntryDN : "+brokerShare_Name_EntryDN);
		return flag;
	}

	public static boolean deleteEntry(String providerId, String userId, String apiKey) {
		System.out.println("In Delete LDAP");

		boolean flag = false;
		
		System.out.println(providerId + "=====" + userId + "=====" + apiKey);

		Attribute OWNER = new BasicAttribute("owner", providerId);
		Attribute PASSWORD = new BasicAttribute("userPassword", apiKey);
		Attribute BLOCK = new BasicAttribute("block", "false");

		// ObjectClass attributes
		Attribute oc = new BasicAttribute("objectClass");
		oc.add("broker");
		oc.add("exchange");
		oc.add("queue");
		oc.add("share");

		Attributes entry = new BasicAttributes();

		entry.put(OWNER);
		entry.put(PASSWORD);
		entry.put(BLOCK);
		entry.put(oc);

		entryDN = "uid=" + userId + ",cn=devices,dc=smartcity";
		System.out.println("entryDN :" + entryDN + " Entry :"
				+ entry.toString());

		// Broker Object
		// Broker Entry
		brokerEntryDN = "description=broker," + "uid=" + userId
				+ ",cn=devices,dc=smartcity";
		Attributes brokerEntry = new BasicAttributes();
		Attribute brokerread = new BasicAttribute("read", "true");
		Attribute brokerwrite = new BasicAttribute("write", "true");

		brokerEntry.put(brokerread);
		brokerEntry.put(brokerwrite);
		brokerEntry.put(oc);

		// Exchange

		brokerExchangeEntryDN = "description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchangeEntry = new BasicAttributes();
		// Attribute exchangeread = new BasicAttribute("read", "true");
		// Attribute exchangewrite = new BasicAttribute("write", "true");

		brokerExchangeEntry.put(oc);
		// brokerExchangeEntry.put(exchangeread);
		// brokerExchangeEntry.put(exchangewrite);

		// Exchange Name to which User can Read / Write

		brokerExchange_DeviceName_EntryDN = "description=" + userId
				+ ",description=exchange,description=broker," + "uid=" + userId
				+ ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_read = new BasicAttribute("read", "true");
		Attribute exchange_DeviceName_write = new BasicAttribute("write",
				"true");

		brokerExchange_DeviceName_Entry.put(oc);
		brokerExchange_DeviceName_Entry.put(exchange_DeviceName_read);
		brokerExchange_DeviceName_Entry.put(exchange_DeviceName_write);

		// Exchange Name (Configure) to which User can Read / Write

		brokerExchange_DeviceName_Configure_EntryDN = "description=" + userId
				+ ".configure,description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Configure_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_Configure_read = new BasicAttribute(
				"read", "true");
		Attribute exchange_DeviceName_Configure_write = new BasicAttribute(
				"write", "true");

		brokerExchange_DeviceName_Configure_Entry.put(oc);
		brokerExchange_DeviceName_Configure_Entry
				.put(exchange_DeviceName_Configure_read);
		brokerExchange_DeviceName_Configure_Entry
				.put(exchange_DeviceName_Configure_write);

		// Exchange Name (Protected) to which User can Read / Write

		brokerExchange_DeviceName_Protected_EntryDN = "description=" + userId
				+ ".protected,description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Protected_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_Protected_read = new BasicAttribute(
				"read", "true");
		Attribute exchange_DeviceName_Protected_write = new BasicAttribute(
				"write", "true");

		brokerExchange_DeviceName_Protected_Entry.put(oc);
		brokerExchange_DeviceName_Protected_Entry
				.put(exchange_DeviceName_Protected_read);
		brokerExchange_DeviceName_Protected_Entry
				.put(exchange_DeviceName_Protected_write);
		
		// Exchange Name (Private) to which User can Read / Write

		brokerExchange_DeviceName_Private_EntryDN = "description=" + userId
				+ ".private,description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Private_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_Private_read = new BasicAttribute(
				"read", "true");
		Attribute exchange_DeviceName_Private_write = new BasicAttribute(
				"write", "true");

		brokerExchange_DeviceName_Private_Entry.put(oc);
		brokerExchange_DeviceName_Private_Entry
				.put(exchange_DeviceName_Private_read);
		brokerExchange_DeviceName_Private_Entry
				.put(exchange_DeviceName_Private_write);

		// Exchange Name (Public) to which User can Read / Write

		brokerExchange_DeviceName_Public_EntryDN = "description=" + userId
				+ ".public,description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Public_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_Public_read = new BasicAttribute(
				"read", "true");
		Attribute exchange_DeviceName_Public_write = new BasicAttribute(
				"write", "true");

		brokerExchange_DeviceName_Public_Entry.put(oc);
		brokerExchange_DeviceName_Public_Entry
				.put(exchange_DeviceName_Public_read);
		brokerExchange_DeviceName_Public_Entry
				.put(exchange_DeviceName_Public_write);

		
		// Exchange Name (Protected) to which User can Read / Write

		brokerExchange_DeviceName_Follow_EntryDN = "description=" + userId
				+ ".follow,description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Follow_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_Follow_read = new BasicAttribute(
				"read", "true");
		Attribute exchange_DeviceName_Follow_write = new BasicAttribute(
				"write", "true");

		brokerExchange_DeviceName_Follow_Entry.put(oc);
		brokerExchange_DeviceName_Follow_Entry
				.put(exchange_DeviceName_Follow_read);
		brokerExchange_DeviceName_Follow_Entry
				.put(exchange_DeviceName_Follow_write);


		// Exchange Name (Notify) to which User can Read / Write

		brokerExchange_DeviceName_Notify_EntryDN = "description=" + userId
				+ ".notify,description=exchange,description=broker,"
				+ "uid=" + userId + ",cn=devices,dc=smartcity";

		Attributes brokerExchange_DeviceName_Notify_Entry = new BasicAttributes();
		Attribute exchange_DeviceName_Notify_read = new BasicAttribute(
				"read", "true");
		Attribute exchange_DeviceName_Notify_write = new BasicAttribute(
				"write", "true");

		brokerExchange_DeviceName_Notify_Entry.put(oc);
		brokerExchange_DeviceName_Notify_Entry
				.put(exchange_DeviceName_Notify_read);
		brokerExchange_DeviceName_Notify_Entry
				.put(exchange_DeviceName_Notify_write);
	
		
		// Queue

		brokerQueueEntryDN = "description=queue,description=broker," + "uid="
				+ userId + ",cn=devices,dc=smartcity";

		Attributes brokerQueueEntry = new BasicAttributes();
		Attribute queueread = new BasicAttribute("read", "true");
		Attribute queuewrite = new BasicAttribute("write", "true");

		brokerQueueEntry.put(oc);
		brokerQueueEntry.put(queueread);
		brokerQueueEntry.put(queuewrite);

		// Queue (Name or ID) from which User can Read

		brokerQueue_Name_EntryDN = "description=" + userId
				+ ",description=queue,description=broker," + "uid=" + userId
				+ ",cn=devices,dc=smartcity";

		Attributes brokerQueue_Name_Entry = new BasicAttributes();
		Attribute queue_Name_read = new BasicAttribute("read", "true");
		Attribute queue_Name_write = new BasicAttribute("write", "true");

		brokerQueue_Name_Entry.put(oc);
		brokerQueue_Name_Entry.put(queue_Name_read);
		brokerQueue_Name_Entry.put(queue_Name_write);

		// Queue Follow (Name or ID) from which User can Read

		brokerQueue_Name_Follow_EntryDN = "description=" + userId + ".follow"
				+ ",description=queue,description=broker," + "uid=" + userId
				+ ",cn=devices,dc=smartcity";

		Attributes brokerQueue_Follow_Name_Entry = new BasicAttributes();
		Attribute queue_Follow_Name_read = new BasicAttribute("read", "true");
		Attribute queue_Follow_Name_write = new BasicAttribute("write", "true");

		brokerQueue_Follow_Name_Entry.put(oc);
		brokerQueue_Follow_Name_Entry.put(queue_Follow_Name_read);
		brokerQueue_Follow_Name_Entry.put(queue_Follow_Name_write);
		
		
		// Queue Notify (Name or ID) from which User can Read

		brokerQueue_Name_Notify_EntryDN = "description=" + userId + ".notify"
				+ ",description=queue,description=broker," + "uid=" + userId
				+ ",cn=devices,dc=smartcity";

		Attributes brokerQueue_Notify_Name_Entry = new BasicAttributes();
		Attribute queue_Notify_Name_read = new BasicAttribute("read", "true");
		Attribute queue_Notify_Name_write = new BasicAttribute("write", "true");

		brokerQueue_Notify_Name_Entry.put(oc);
		brokerQueue_Notify_Name_Entry.put(queue_Notify_Name_read);
		brokerQueue_Notify_Name_Entry.put(queue_Notify_Name_write);
		
		// Share

		brokerShareEntryDN = "description=share,description=broker," + "uid="
				+ userId + ",cn=devices,dc=smartcity";

		Attributes brokerShareEntry = new BasicAttributes();
		Attribute shareread = new BasicAttribute("read", "true");
		Attribute sharewrite = new BasicAttribute("write", "true");

		brokerShareEntry.put(oc);
		brokerShareEntry.put(shareread);
		brokerShareEntry.put(sharewrite);

		// Share (Name or ID) from which User can Read

		brokerShare_Name_EntryDN = "description=" + userId
				+ ",description=share,description=broker," + "uid=" + userId
				+ ",cn=devices,dc=smartcity";

		Attributes brokerShare_Name_Entry = new BasicAttributes();
		Attribute Share_Name_read = new BasicAttribute("read", "true");
		Attribute Share_Name_write = new BasicAttribute("write", "true");

		brokerShare_Name_Entry.put(oc);
		brokerShare_Name_Entry.put(Share_Name_read);
		brokerShare_Name_Entry.put(Share_Name_write);


		try {
			
			dirContext.destroySubcontext(brokerShare_Name_EntryDN);
			dirContext.destroySubcontext(brokerShareEntryDN);
			dirContext.destroySubcontext(brokerQueue_Name_Follow_EntryDN);
			dirContext.destroySubcontext(brokerQueue_Name_Notify_EntryDN);
			dirContext.destroySubcontext(brokerQueue_Name_EntryDN);
			dirContext.destroySubcontext(brokerQueueEntryDN);
			dirContext.destroySubcontext(
					brokerExchange_DeviceName_Follow_EntryDN);
			dirContext.destroySubcontext(
					brokerExchange_DeviceName_Protected_EntryDN);
			dirContext.destroySubcontext(
					brokerExchange_DeviceName_Public_EntryDN);
			dirContext.destroySubcontext(
					brokerExchange_DeviceName_Private_EntryDN);
			dirContext.destroySubcontext(
					brokerExchange_DeviceName_Configure_EntryDN);
			dirContext.destroySubcontext(brokerExchange_DeviceName_EntryDN);
			dirContext.destroySubcontext(brokerExchange_DeviceName_Notify_EntryDN);
			dirContext.destroySubcontext(brokerExchangeEntryDN);
			dirContext.destroySubcontext(brokerEntryDN);
			dirContext.destroySubcontext(entryDN);
					
			System.out.println("Deleted Entry");

			flag = true;

			System.out.println("Deleted entryDN : "+entryDN );
			System.out.println("Deleted brokerEntryDN : "+brokerEntryDN);
			System.out.println("Deleted brokerExchangeEntryDN : "+brokerExchangeEntryDN);
			System.out.println("Deleted brokerExchange_DeviceName_EntryDN : "+brokerExchange_DeviceName_EntryDN);
			System.out.println("Deleted brokerExchange_DeviceName_Configure_EntryDN : "+brokerExchange_DeviceName_Configure_EntryDN);
			System.out.println("Deleted brokerExchange_DeviceName_Private_EntryDN : "+brokerExchange_DeviceName_Private_EntryDN);
			System.out.println("Deleted brokerExchange_DeviceName_Protected_EntryDN : "+brokerExchange_DeviceName_Protected_EntryDN);
			System.out.println("Deleted brokerExchange_DeviceName_Public_EntryDN : "+brokerExchange_DeviceName_Public_EntryDN);
			System.out.println("Deleted brokerExchange_DeviceName_Follow_EntryDN : "+brokerExchange_DeviceName_Follow_EntryDN);
			System.out.println("Deleted brokerQueueEntryDN : "+brokerQueueEntryDN);
			System.out.println("Deleted brokerQueue_Name_EntryDN : "+brokerQueue_Name_EntryDN);
			System.out.println("Deleted brokerQueue_Name_Follow_EntryDN : "+brokerQueue_Name_Follow_EntryDN);
			System.out.println("Deleted brokerShareEntryDN : "+brokerShareEntryDN);
			System.out.println("Deleted brokerShare_Name_EntryDN : "+brokerShare_Name_EntryDN);
	
	} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public boolean verifyProvider(String userId,
			String[] decoded_authorization_data) {

		boolean flag = false;
		entryDN = "uid=" + userId + ",cn=devices,dc=smartcity";

		try {

			LDAPEntry = dirContext.getAttributes(entryDN).toString();
			System.out.println(LDAPEntry);

			String[] LDAPEntry_Split = LDAPEntry.split(",");

			String[] providerName = LDAPEntry_Split[0].split("=");
			String[] provider = providerName[1].split(":");
			provider[1] = provider[1].replaceAll("\\s+", "");

			String submitted_providerName = decoded_authorization_data[0];

			if (provider[1].contains(submitted_providerName)) {
				System.out.println("Valid Device of the User");
				flag = true;
			} else {
				System.out.println("Invalid Device of the User");
				flag = false;
			}

		} catch (NamingException e) {
			// TODO Auto-generated catch block
			flag = false;
			return flag;
			// e.printStackTrace();
		}
		return flag;
	}

}