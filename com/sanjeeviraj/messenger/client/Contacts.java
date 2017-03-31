package com.sanjeeviraj.messenger.client;

import java.net.*; 
import java.io.*;
import java.util.*;

public class Contacts implements Constants
{
	public Contacts()
	{

	}
	public Contacts(String usernameArg, String passwordArg, Socket socketObjectArg)
	{
		//System.out.println("Contacts constructor");
		username = usernameArg;
		password = passwordArg;
		socketObject = socketObjectArg;
	}
	
	public static final String SERVER_NAME = "127.0.0.1";
	public static final int PORT_NUMBER = 6666;
	private static boolean CLOSE_CONN = true;
	private String username, password;
	private boolean check_user, check_pass;
	Socket socketObject;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	Scanner in;
	
	public void main(String usernameArg, String passwordArg, Socket socketObjectArg)
	{
		Contacts co = new Contacts(usernameArg, passwordArg, socketObjectArg);
		try
		{
			co.startConnection();
			if(co.checkConnection())
			{
				co.getChoice();
			}
			else
			{
				System.out.println("Connection error.\n Try again later.");
				System.exit(0);
			}
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
		finally
		{
			try
			{
				if(CLOSE_CONN)
				{
					dos.writeUTF("exit");
					System.out.println("Disconnecting from server");
					socketObject.close();
				}
			}
			catch(Exception e)
			{
				System.out.println("Error while closing the connection \nError details:\n");
				e.printStackTrace();
			}
		}		
	}
	
	private void startConnection()
	{
		//System.out.println("Connecting...");    
		try
		{
			os = socketObject.getOutputStream();
			dos = new DataOutputStream(os);
			is = socketObject.getInputStream();
			dis	= new DataInputStream(is);		 
		}
		catch(ConnectException ce)
		{
			System.out.println("Connection error\nPossible reasons: server not available\nserver refused to connect\nserver busy\nTry again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private boolean checkConnection()
	{    
		boolean check_connection = true;
		try
		{
			//System.out.print("checking connection...");
		 	dos.writeUTF("check_connection");
			if(!(dis.readUTF().equals("connected")))
				check_connection = false;
			//System.out.println("connection checked : "+check_connection);
		}
		catch(ConnectException ce)
		{
			CLOSE_CONN = true;
			System.out.println("Connection error\nTry again later");
			ce.printStackTrace();
			System.exit(0);
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
		return check_connection;
	}
	
	private void getChoice()
	{
		int choice;
		try
		{
			in = new Scanner(System.in);
			System.out.println("\nEnter choice \n1-Add contact \n2-Remove contact \n3-Back \n4-Exit \n ------contacts-----");
			this.viewContacts();
			choice = in.nextInt();
			switch(choice)
			{
					
				case 1: this.addContact();
						break;
						
				case 2: this.removeContact();
						break;
				
				case 3: goBack();
						break;

				case 4: exit();
						break;
						
				default: this.handleChoice(choice);
			}
		}
		catch(InputMismatchException ime)
		{
			System.out.println("\nwrong input \nRestarting messenger...");
			this.getChoice();
		}
		return;
	}
	
	private void addContact()
	{
		String contact_name;
		System.out.println("Enter contact name(id) : ");
		try
		{
			contact_name = in.next();
			if(!(this.checkUsername(contact_name))) //checking contact_name exists or not
			{
				System.out.println("user not found...moving back\n");
				this.getChoice();
			}
			else if(this.checkContact(contact_name))
			{
				System.out.println("user exists...moving back\n");
				this.getChoice();	
			}
			else
			{
				if(checkConnection() == true)
				{
					dos.writeUTF("Contacts");
					if(!(dis.readUTF().equals("connected")))
					{
						System.out.println("No response in server\nTry again later");
						System.exit(0);
					}
					
					dos.writeUTF("addContact");
					if(!(dis.readUTF().equals("connected")))
					{
						System.out.println("No response in server\nTry again later");
						System.exit(0);
					}

					dos.writeUTF(username);
					if(!(dis.readUTF().equals("connected")))
					{
						System.out.println("No response in server\nTry again later");
						System.exit(0);
					}

					dos.writeUTF(contact_name);
					if(!(dis.readUTF().equals("true")))
					{
						System.out.println("No response in server\nTry again later");
						System.exit(0);
					}

					System.out.println("contact *"+contact_name+"* added...");
					this.getChoice();
				}
			}	
		}			
		catch(ConnectException ce)
		{
			System.out.println("Connection error. Try again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void removeContact()
	{
		String contact_name;
		System.out.println("Enter contact name(id) : ");
		try
		{
			contact_name = in.next();
			if(!(this.checkUsername(contact_name))) //checking contact_name exists or not
			{
				System.out.println("user not found...moving back\n");
				this.getChoice();
			}
			else
			{
				if(checkConnection() == true)
				{
					dos.writeUTF("Contacts");
					if(!(dis.readUTF().equals("connected")))
					{
						System.out.println("No response in server\nTry again later");
						System.exit(0);
					}
					
					dos.writeUTF("removeContact");
					if(!(dis.readUTF().equals("connected")))
					{
						System.out.println("No response in server\nTry again later");
						System.exit(0);
					}

					dos.writeUTF(username);
					if(!(dis.readUTF().equals("connected")))
					{
						System.out.println("No response in server\nTry again later");
						System.exit(0);
					}

					dos.writeUTF(contact_name);
					if(!(dis.readUTF().equals("true")))
					{
						System.out.println("No response in server\nTry again later");
						System.exit(0);
					}

					System.out.println("contact *"+contact_name+"* removed...");
					this.getChoice();
				}
			}	
		}			
		catch(ConnectException ce)
		{
			System.out.println("Connection error. Try again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}	

	private void handleChoice(int choice)
	{
		int no_of_contacts;
		String id;
		try
		{
			no_of_contacts = this.getNoOfContacts()+4;
			if(!(choice <= no_of_contacts) || (choice < 1))
			{
				System.out.println("Incorrect choice\nRestarting messenger...");
				this.getChoice();
			}
			else
			{
				id = getContactID((choice - 4));
				ChatContact cc = new ChatContact();
				cc.main(username, password, socketObject, id);
			}
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}			
	}
	
	private void viewContacts()
	{
		int no_of_contacts;
		String contact_name, id, status, last_seen;
		//System.out.println("Enter contact name(id) : ");
		try
		{
			no_of_contacts = this.getNoOfContacts()+4;
			//System.out.println("*-"+no_of_contacts+"-*");
			//System.out.println("*-"+(no_of_contacts-4)+"-*");
			for(int i = 5; i <= no_of_contacts; i++)
			{
				id = this.getContactID(i-4);

				System.out.print("\n**-["+i+"]-**");
				System.out.print(" || user id: "+id);
				System.out.println(" || Name: "+this.getContactName(id));
				System.out.print("Status: "+this.getContactStatus(id));
				System.out.println(" || Last seen: "+this.getContactLastSeen(id));
				System.out.print("Total no of messages: "+this.getContactTotalNoOfMessages(id));
				System.out.println(" || Unread messages: "+this.getNoOfUnreadMessagesContact(id));
			}
		}			
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private String getContactID(int position)
	{
		String response = null;
		try
		{
			if(checkConnection() == true)
			{
				dos.writeUTF("Contacts");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("getContactID");
				if(!(dis.readUTF().equals("connected")))
				{	
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(username);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
					
				dos.writeUTF(""+position);
				response = dis.readUTF();
			}	
			
		}			
		catch(ConnectException ce)
		{
			System.out.println("Connection error. Try again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
		return response;
	}

	private String getContactName(String id)
	{
		String response = null;
		try
		{
			if(checkConnection() == true)
			{
				dos.writeUTF("Contacts");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("getContactName");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(id);
				response = dis.readUTF();

			}	
			
		}			
		catch(ConnectException ce)
		{
			System.out.println("Connection error. Try again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
		return response;
	}

	private String getContactStatus(String id)
	{
		String response = null;
		try
		{
			if(checkConnection() == true)
			{
				dos.writeUTF("Contacts");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
			
				dos.writeUTF("getContactStatus");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(id);
				response = dis.readUTF();
			}	
		}			
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
		return response;
	}

	private String getContactLastSeen(String id)
	{
		String response = null;
		try
		{
			if(checkConnection() == true)
			{
				dos.writeUTF("Contacts");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
					
				dos.writeUTF("getContactLastSeen");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(id);
				response = dis.readUTF();
			}	
			
		}			
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
		return response;
	}

	private int getContactTotalNoOfMessages(String contact_id)
	{
		String response = "0";
		int nmsg = 0;
		try
		{
			if(checkConnection() == true)
			{
				dos.writeUTF("ChatContact");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF("getNoOfMessages");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(username);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}				
					
				dos.writeUTF(contact_id);
				response = dis.readUTF();
				nmsg = Integer.parseInt(response);
			}	
			
		}			
		catch(ConnectException ce)
		{
			System.out.println("Connection error. Try again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
		return nmsg;
	}

	private String getNoOfUnreadMessagesContact(String contact_id)
	{
		String response = "0";
		//int unread_messages = 0;
		try
		{
			if(checkConnection() == true)
			{
				dos.writeUTF("Chats");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("getNoOfUnreadMessagesContact");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
					
				dos.writeUTF(username);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(contact_id);
				response = dis.readUTF();
		//		unread_messages = Integer.parseInt(response);
			}
		}			
		catch(ConnectException ce)
		{
			System.out.println("Connection error. Try again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
		//return unread_messages;
		return response;
	}

	private int getNoOfContacts()
	{
		String response = null;
		int no_of_contacts = 0;
		try
		{
			if(checkConnection() == true)
			{
				dos.writeUTF("Contacts");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("getNoOfContacts");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
					
				dos.writeUTF(username);
				response = dis.readUTF();
				no_of_contacts = Integer.parseInt(response);
			}
			
		}			
		catch(ConnectException ce)
		{
			System.out.println("Connection error. Try again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
		return no_of_contacts;
	}
	
	private boolean checkUsername(String name)
	{
		try
		{
			if(checkConnection())
			{
				dos.writeUTF("SignUp");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
			
				dos.writeUTF("checkUsername");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF(name);
				if(dis.readUTF().equals("true"))
					check_user = true;
				else
					check_user = false;
			}
		}
		catch(ConnectException ce)
		{
			System.out.println("Connection error. Try again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
		return check_user;
	}

	private boolean checkContact(String contact_name)
	{
		try
		{
			if(checkConnection())
			{
				dos.writeUTF("Contacts");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
			
				dos.writeUTF("checkContact");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF(username);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(contact_name);
				if(dis.readUTF().equals("true"))
					check_user = true;
				else
					check_user = false;
			}
		}
		catch(ConnectException ce)
		{
			System.out.println("Connection error. Try again later.");
			System.exit(0);
			
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
		return check_user;
	}

	private void exit()
	{
		try
		{
			if(CLOSE_CONN)
			{
				dos.writeUTF("exit");
				System.out.println("Disconnecting from server");
				socketObject.close();
				System.exit(0);
			}
		}
		catch(Exception e)
		{
			System.out.println("Error while closing the connection \nError details:\n");
			e.printStackTrace();
		}
	}

	private void goBack()
	{
		try
		{
			StartScreen sc = new StartScreen();
			sc.main(username, password, socketObject);
		}
		catch(Exception e)
		{
			System.out.println("Error occured \nError details:\n Trying to close connection");
			exit();
			e.printStackTrace();
		}
	}
}