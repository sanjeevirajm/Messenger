package com.sanjeeviraj.messenger.client;

import java.net.*; 
import java.io.*;
import java.util.*;

public class Groups implements Constants
{
	public Groups()
	{

	}
	public Groups(String usernameArg, String passwordArg, Socket socketObjectArg)
	{
		username = usernameArg;
		password = passwordArg;
		socketObject = socketObjectArg;
	}
	
	private static boolean CLOSE_CONN = true;
	private String username, password;
	private int no_of_Groups = 0;
	private boolean check_user, check_pass;
	Socket socketObject;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	Scanner in;

	
	public void main(String usernameArg, String passwordArg, Socket socketObjectArg)
	{
		Groups gr = new Groups(usernameArg, passwordArg, socketObjectArg);
		try
		{
			gr.startConnection();
			if(gr.checkConnection())
			{
				gr.getChoice();
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
			System.out.println("\nEnter choice \n1-Create group \n2-Delete group \n3-Back \n4-Exit \n5-Chat group \n ------Groups-----");
			this.viewGroups();
			choice = in.nextInt();
			switch(choice)
			{
					
				case 1: this.createGroup();
						break;
						
				case 2: this.deleteGroup();
						break;
				
				case 3: goBack();
						break;

				case 4: exit();
						break;

				case 5: System.out.println("group id: ");
						String id = in.next();
						this.chatGroup(id, username);
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

	private void handleChoice(int choice)
	{
		String id;
		int no_of_groups = this.getNoOfGroups()+5;
		try
		{
			if(!(choice < no_of_groups) && (choice < 1))
			{
				System.out.println("Incorrect choice\nRestarting messenger...");
				this.getChoice();
			}
			else
			{
				id = this.getGroupID((choice - 5));
				ChatGroup cg = new ChatGroup();
				cg.main(username, password, socketObject, id);
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
	
	private void createGroup()
	{
		String group_id, group_name;
		System.out.println("Enter group id(must be unique) : ");
		try
		{
			while(true)
			{
				group_id = in.next();
				if(!this.checkGroup(group_id))
					break;
				else
					System.out.println(group_id+" exists, Enter different id(must be unique) : ");
			}

			System.out.println("Enter group name : ");
			group_name = in.next();

			System.out.println("After creating group, you will be switched to manage group menu");
			System.out.println("Creating...");

			this.createGroup(group_id, group_name, username);
			this.chatGroup(group_id, username);
		}			
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void chatGroup(String group_id, String user_id)
	{
		ChatGroup cg = new ChatGroup();
		cg.main(username, password, socketObject, group_id);
	}

	private boolean checkGroup(String group_id)
	{
		boolean response = false;
		try
		{
			if(checkConnection())
			{
				dos.writeUTF("Groups");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("checkGroup");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
	
				dos.writeUTF(group_id);
				if(dis.readUTF().equals("true"))
					response = true;
				else
					response = false;
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

	private void createGroup(String group_id, String group_name, String name)
	{
		try
		{
			if(checkConnection())
			{
				dos.writeUTF("Groups");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("createGroup");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
	
				dos.writeUTF(group_id);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
	
				dos.writeUTF(group_name);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(name);
				if(!(dis.readUTF().equals("true")))
				{
					System.out.println("Incorrect response, check entered details and try again later");
					this.getChoice();
				}
				else
				{
					System.out.print("user *"+name+"* added as admin to ");
					System.out.println("group *"+group_id+"* ...");
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

	private void deleteGroup()
	{
		String group_id, group_name;
		System.out.println("Group will not deleted. You will be removed from the group, group details and messages will not be shown to you ");
		System.out.println("Enter group id : ");
		try
		{
			group_id = in.next();
			System.out.println("Deleting...");
			this.deleteGroup(group_id, username);
			this.getChoice();
		}			
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void deleteGroup(String group_id, String name)
	{
		try
		{
			if(checkConnection())
			{
				dos.writeUTF("ChatGroup");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("deleteGroup");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
	
				dos.writeUTF(group_id);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
	
				dos.writeUTF(name);
				if(!(dis.readUTF().equals("true")))
				{
					System.out.println("Incorrect response, check entered details and try again later");
					this.getChoice();
				}
				else
				{
					System.out.println("user *"+name+"* removed...");
					System.out.println("group *"+group_id+"* deleted...");
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

	private void viewGroups()
	{
		String contact_name, id, status, last_seen;
		//System.out.println("Enter contact name(id) : ");
		try
		{
			this.no_of_Groups = this.getNoOfGroups()+5;
			System.out.println("*-"+this.no_of_Groups+"-*");
			System.out.println("*-"+(this.no_of_Groups-5)+"-*");
			for(int i = 6; i <= this.no_of_Groups; i++)
			{
				id = this.getGroupID(i-5);

				System.out.print("\n**-["+i+"]-**");
				System.out.print(" || Group id: "+id);
				System.out.print("  ||  Name: "+this.getGroupName(id));
				System.out.println("  ||  No of Members: "+this.getNoOfGroupMembers(id));
				System.out.print("Total no of messages: "+this.getGroupTotalNoOfMessages(id));
				System.out.println("  ||  Unread messages: "+this.getNoOfUnreadMessagesGroup(id));
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

	private String getGroupID(int position)
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
				
				dos.writeUTF("getGroupID");
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

	private int getNoOfUnreadMessagesGroup(String group_id)
	{
		String response = "0";
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

				dos.writeUTF(group_id);
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
		return Integer.parseInt(response);
	}	

	private int getNoOfGroups()
	{
		String response = null;
		int no_of_Groups = 0;
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
				
				dos.writeUTF("getNoOfGroups");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
					
				dos.writeUTF(username);
				response = dis.readUTF();
				no_of_Groups = Integer.parseInt(response);
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
		return no_of_Groups;
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