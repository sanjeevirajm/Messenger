package com.sanjeeviraj.messenger.client;

import java.net.*; 
import java.io.*;
import java.util.*;

public class Chats implements Constants
{
	public Chats()
	{

	}
	
	public Chats(String usernameArg, String passwordArg, Socket socketObjectArg)
	{
		//System.out.println("Chats constructor");
		username = usernameArg;
		password = passwordArg;
		socketObject = socketObjectArg;
	}
	
	private static boolean CLOSE_CONN = true;
	private String username, password;
	private boolean check_user, check_pass;
	Socket socketObject;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	Scanner in;
	int no_of_contacts = 0;

	public void main(String usernameArg, String passwordArg, Socket socketObjectArg)
	{
		Chats ch = new Chats(usernameArg, passwordArg, socketObjectArg);
		try
		{
			ch.startConnection();
			if(ch.checkConnection())
			{
				ch.getChoice();
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
			System.out.println("\nEnter choice \n1-Chat Contact \n2-Chat Group \n3-Back \n4-Exit \n*----Unread chats----*");
			this.unreadChats();
			choice = in.nextInt();
			switch(choice)
			{
				case 1: this.chatContact();
						break;
						
				case 2: this.chatGroup();
						break;
					
				case 3: this.goBack();
						break;
						
				case 4: this.exit();
						break;
						
				default: this.handleChat(choice);
			}
		}
		catch(InputMismatchException ime)
		{
			System.out.println("\nwrong input \nRestarting messenger...");
			this.getChoice();
		}
		return;
	}
	
	private void chatContact()
	{
		String contact_id = "";
		System.out.println("Enter contact id: ");
		while(true)
		{
			contact_id = in.next();
			if(checkContact(contact_id))
				{
					ChatContact cc = new ChatContact();
					cc.main(username, password, socketObject, contact_id);
					break;
				}
			System.out.println("Enter correct contact id: ");
		}
	}

	private void chatContact(String id)
	{
		ChatContact cc = new ChatContact();
		cc.main(username, password, socketObject, id);
	}

	private void chatGroup()
	{
		String group_id = "";
		System.out.println("Enter group id: ");
		while(true)
		{
			group_id = in.next();
			if(checkMember(username, group_id))
			{
				ChatGroup cg = new ChatGroup();
				cg.main(username, password, socketObject, group_id);
			}
				System.out.println("Enter correct group id: ");
		}
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

	private boolean checkMember(String member_id, String group_id)
	{
		String response = "false";
		boolean check_user = false;
		try
		{
			if(checkConnection() == true)
			{
				//System.out.println("\nsend message 1...");
				dos.writeUTF("ChatGroup");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				//System.out.println("\nsend message 2...");
				dos.writeUTF("checkMember");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				//System.out.println("\nsend message 3...");
				dos.writeUTF(member_id);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}				
					
				//System.out.println("\nsend message 4...");					
				dos.writeUTF(group_id);
				response = dis.readUTF();

				if(response.equals("true"))
					check_user = true;
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


	private void chatGroup(String id)
	{
		ChatGroup cg = new ChatGroup();
		cg.main(username, password, socketObject, id);
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

	private void handleChat(int choice)
	{
		if(choice <= (getNoOfUnreadContactChats()+4))
		{
			chatContact(getUnreadContactID(choice-4));
		}
		else if(choice <= (getNoOfUnreadGroupChats()+getNoOfUnreadContactChats()+4))
		{
			chatGroup(getUnreadGroupID(choice-getNoOfUnreadContactChats()-4));
		}
		else
		{
			System.out.println("Incorrect choice\nRestarting messenger...\n");
			this.getChoice();
		}
	}

	private void unreadChats()
	{
		try
		{
			this.unreadContactChats();
			this.unreadGroupChats();
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void unreadContactChats()
	{
		System.out.println("\n[*----Contact chats----*]");
		try
		{
			int nmsg = 0;
			String contact_name, id, last_seen;
		
			this.no_of_contacts = this.getNoOfUnreadContactChats()+4;
			for(int i = 5; i <= no_of_contacts; i++)
			{
				id = this.getUnreadContactID(i-4);
				contact_name = this.getContactName(id);
				last_seen = this.getContactLastSeen(id);
				nmsg = this.getNoOfUnreadMessagesContact(id);
				
				System.out.print("\n*-["+i+"]-*");
				System.out.print(" || ID: "+id);
				System.out.print(" || Name: "+contact_name);
				System.out.print(" || Last seen: "+last_seen);
				System.out.println(" || Unread messages : "+nmsg);
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

	private int getNoOfUnreadContactChats()
	{
		String response = null;
		int no_of_contacts = 0;
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
				
				dos.writeUTF("getNoOfUnreadContactChats");
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

	private int getNoOfUnreadGroupChats()
	{
		String response = null;
		int no_of_groups = 0;
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
				
				dos.writeUTF("getNoOfUnreadGroupChats");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
					
				dos.writeUTF(username);
				response = dis.readUTF();
				no_of_groups = Integer.parseInt(response);
				//System.out.println("NO OF UNREAD GROUP CHATS: "+no_of_groups);
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
		return no_of_groups;
	}

	private String getUnreadContactID(int position)
	{
		String response = null;
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
				
				dos.writeUTF("getUnreadContactID");
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

	private String getUnreadGroupID(int position)
	{
		String response = null;
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
				
				dos.writeUTF("getUnreadGroupID");
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

	private int getNoOfUnreadMessagesContact(String id)
	{
		String response = null;
		int nmessages = 0;
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

				dos.writeUTF(id);
					
				response = dis.readUTF();
				nmessages = Integer.parseInt(response);
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
		return nmessages;
	}

	private int getNoOfUnreadMessagesGroup(String id)
	{
		String response = null;
		int nmessages = 0;
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
				
				dos.writeUTF("getNoOfUnreadMessagesGroup");
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
					
				dos.writeUTF(id);	
				
				response = dis.readUTF();
				nmessages = Integer.parseInt(response);
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
		return nmessages;
	}

	private void unreadGroupChats()
	{
		System.out.println("\n[*----Group chats----*]");
		try
		{
			int no_of_groups;
			int nmsg = 0;
			int nmembers = 0;
			String group_name, id;

			no_of_groups = this.getNoOfUnreadGroupChats()+this.no_of_contacts;
			//System.out.println("NO OF GROUPS VARIABLE : "+no_of_groups);
			//System.out.println("NO OF CONTACTS VARIABLE : "+this.no_of_contacts);
			for(int i = (this.no_of_contacts + 1); i <= no_of_groups; i++)
			{
				id = this.getUnreadGroupID(no_of_contacts-4);
				
				System.out.print("\n*-["+i+"]-*");
				System.out.print(" || Group id: "+id);
				System.out.print("  ||  Name: "+this.getGroupName(id));
				System.out.println("  ||  No of Members: "+this.getNoOfGroupMembers(id));
				System.out.print("Total no of messages: "+this.getGroupTotalNoOfMessages(id));
				System.out.println("  ||  Unread messages: "+this.getNoOfUnreadMessagesGroup(id));
			}
			System.out.print("\nEnter group/conact temporary id : ");
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private String getGroupName(String id)
	{
		String response = null;
		try
		{
			if(checkConnection() == true)
			{
				dos.writeUTF("Groups");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("getGroupName");
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

	private int getNoOfGroupMembers(String id)
	{
		String response = "0";
		try
		{
			if(checkConnection() == true)
			{
				dos.writeUTF("Groups");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
			
				dos.writeUTF("getNoOfGroupMembers");
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
		return Integer.parseInt(response);
	}

	private int getGroupTotalNoOfMessages(String id)
	{
		String response = "0";
		try
		{
			if(checkConnection() == true)
			{
				dos.writeUTF("Groups");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
					
				dos.writeUTF("getGroupTotalNoOfMessages");
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
		return Integer.parseInt(response);
	}	

	private int getNoOfContactChats()
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
				
				dos.writeUTF(username+"\n"+password);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("username or password is wrong\nSign in again");
					socketObject.close();
					CLOSE_CONN = false;
					SignIn si = new SignIn();
					si.main();
				}
					
				dos.writeUTF("getNoOfGroupChats");
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

	private int getNoOfGroupChats()
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
				
				dos.writeUTF(username+"\n"+password);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("username or password is wrong\nSign in again");
					socketObject.close();
					CLOSE_CONN = false;
					SignIn si = new SignIn();
					si.main();
				}
					
				dos.writeUTF("getNoOfGroupChats");
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
				
				dos.writeUTF(username+"\n"+password);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("username or password is wrong\nSign in again");
					socketObject.close();
					CLOSE_CONN = false;
					SignIn si = new SignIn();
					si.main();
				}
					
				dos.writeUTF("getContactStatus");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(username+"\n"+password);
				response = dis.readUTF();
			}	
			return response;
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