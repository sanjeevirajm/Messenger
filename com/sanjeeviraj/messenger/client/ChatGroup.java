package com.sanjeeviraj.messenger.client;

import java.net.*; 
import java.io.*;
import java.util.*;

public class ChatGroup implements Constants
{
	public ChatGroup()
	{

	}

	public ChatGroup(String usernameArg, String passwordArg, Socket socketObjectArg, String group_id_arg)
	{
		//System.out.println("ChatGroup constructor");
		username = usernameArg;
		password = passwordArg;
		socketObject = socketObjectArg;
		group_id = group_id_arg;
	}
	
	public static final String SERVER_NAME = "127.0.0.1";
	public static final int PORT_NUMBER = 6666;
	private static boolean CLOSE_CONN = true;
	private String username, password, group_id;
	private boolean check_user, check_pass;
	Socket socketObject;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	Scanner in;
	int page_no = 0;
	
	public void main(String usernameArg, String passwordArg, Socket socketObjectArg, String group_id_arg)
	{
		ChatGroup cg = new ChatGroup(usernameArg, passwordArg, socketObjectArg, group_id_arg);
		try
		{
			cg.startConnection();
			if(cg.checkConnection())
			{
				cg.getChoice();
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
			System.out.println("\nEnter choice \n1-Send Message \n2-Show next 10 messages(if available) \n3-Show previous 10 messages(if available) \n4-Manage group(change name), members(add, remove), change admin permission(normal <-> admin) \n5-Back \n6-Exit \n[*----Chats----*]");
			this.showMessages(page_no);
			choice = in.nextInt();

			switch(choice)
			{
				case 1: this.sendMessage();
						break;
						
				case 2: if((page_no*10) < this.getNoOfMessages())
							++page_no;
						else
							System.out.println("\n!! --Page doesn't exist, showing same page-- !!");
							this.getChoice();
						break;
								
				case 3: if(page_no > 1)
							--page_no;
						else
							System.out.println("\n!! --Page doesn't exist, showing same page-- !!");
							this.getChoice();
						break;
				
				case 4: this.manageGroup();
						break;

				case 5: this.goBack();
						break;

				case 6: this.exit();
						break;						
						
				default:  System.out.println("\nwrong input \nRestarting messenger...");
						this.getChoice();
			}
		}
		catch(InputMismatchException ime)
		{
			System.out.println("\nwrong input \nRestarting messenger...");
			this.getChoice();
		}
		return;
	}

	private void showMessages(int page_no)
	{
		int total_messages;
		String sender_id;
		String message, time;
		int nmessages = page_no * 10;
		try
		{
			if(page_no <=1)
				nmessages = 10;

			total_messages = this.getNoOfMessages();

			for(int i = nmessages - 9; i <= total_messages && i <= nmessages; i++)
			{
				sender_id = this.getMessageSender(i);
				System.out.print("\n**-["+i+"]-**");
				System.out.print(" ||sender ID: "+sender_id);
				System.out.print(" ||Name: "+this.getContactName(sender_id));
				System.out.println(" || Time: "+this.getMessageTime(i));
				System.out.print(" || ----Message--- || "+this.getMessage(i));
			}
			this.setRead();
		}			
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void manageGroup()
	{
		int choice;
		try
		{
			in = new Scanner(System.in);
			System.out.println("\nEnter choice \n1-Add a member \n2-Remove a member \n3-convert normal member -> admin \n4-convert admin -> normal member \n5-Back \n6-Exit \n[*]----Members----[*]");
			
			this.showMembers();
			choice = in.nextInt();

			switch(choice)
			{
				case 1: this.addMember();
						break;
						
				case 2: this.removeMember();
						break;
								
				case 3: this.addAdminPermission();
						break;
				
				case 4: this.removeAdminPermission();
						break;

				case 5: this.goBack();
						break;

				case 6: this.exit();
						break;						
						
				default: this.handleMembersChoice(choice);
			}
		}
		catch(InputMismatchException ime)
		{
			System.out.println("\nwrong input \nRestarting messenger...");
			this.getChoice();
		}
		return;
	}

	private void manageGroup(String member_id)
	{
		int choice;
		try
		{
			in = new Scanner(System.in);
			System.out.println("\nEnter choice \n1-Remove member \n2-convert normal member -> admin \n3-convert admin -> normal member \n4-Back \n5-Exit \n[*]----Member Details----[*]");
			
			this.showMemberDetails(member_id);
			choice = in.nextInt();

			switch(choice)
			{						
				case 1: this.removeMember(member_id);																				
						break;
								
				case 2: this.addAdminPermission(member_id);
						break;
				
				case 3: this.removeAdminPermission(member_id);
						break;

				case 4: this.getChoice();
						break;

				case 5: this.exit();
						break;						
						
				default:  System.out.println("\nwrong input \nRestarting messenger...");
						this.manageGroup();
			}
		}
		catch(InputMismatchException ime)
		{
			System.out.println("\nwrong input \nRestarting messenger...");
			this.manageGroup();
		}
		return;
	}
	
	private void sendMessage()
	{
		String message = "";
		String line;
		System.out.println("To send message press enter, type /send, and press enter");
		System.out.println("Enter message: ");
		try
		{
			while(true)
			{
				line = in.nextLine();
				if(line.equals("/send"))
					break;
				if(line.equals("/time") || line.equals("/message"))
				{
					System.out.println("Your message contains a string which is not allowed. That word is automatically removed. (Tip: use space or other character before that word)");
				}
				else
				{
					message += line + "\n";
				}
			}
			sendMessage(message);
		}			
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void sendMessage(String message)
	{
		int nmsg = 0;
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
				dos.writeUTF("sendMessage");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				//System.out.println("\nsend message 3...");
				dos.writeUTF(username);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}				
					
				//System.out.println("\nsend message 4...");					
				dos.writeUTF(group_id);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				//System.out.println("\nsend message 5...");
				dos.writeUTF(message);
				if(!(dis.readUTF().equals("true")))
				{
					System.out.println("server problem...message not sent...\nTry again later");
					this.getChoice();
				}
				//System.out.println("\nMessage sent..");
				this.getChoice();
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

	private void addMember()
	{
		String member_id = "";
		System.out.println("Enter user id: ");
		try
		{
			member_id = in.nextLine();
			this.addMember(member_id);
		}			
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void addMember(String member_id)
	{
		int nmsg = 0;
		try
		{
			if(this.checkAdmin(username))
			while(true)
			{
				member_id = in.next();
				if(!this.checkMember(member_id))
					break;
				else
					System.out.println("member exist in group, enter correct user id: ");
			}
			else
			{
				System.out.println("Permission denied ");
				this.manageGroup(username);
			}
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
				dos.writeUTF("addMember");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}			
					
				//System.out.println("\nsend message 4...");					
				dos.writeUTF(group_id);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				//System.out.println("\nsend message 5...");
				dos.writeUTF(member_id);
				if(!(dis.readUTF().equals("true")))
				{
					System.out.println("server problem...\nTry again later");
					this.getChoice();
				}
				else
				{
					System.out.println("\nMember added..");
					this.manageGroup();	
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

	private void removeMember()
	{
		String member_id = "";
		System.out.println("Enter user id: ");;
		try
		{
			member_id = in.nextLine();
			this.removeMember(member_id);
		}			
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void removeMember(String member_id)
	{
		int nmsg = 0;
		try
		{
			if(this.checkAdmin(username))
			while(true)
			{
				if(this.checkMember(member_id))
					break;
				else
				{
					member_id = in.next();
					System.out.println("user not exist, enter correct user id: ");
				}
			}
			else
			{
				System.out.println("Permission denied ");
				this.manageGroup();
			}
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
				dos.writeUTF("removeMember");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}			
					
				//System.out.println("\nsend message 4...");					
				dos.writeUTF(group_id);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				//System.out.println("\nsend message 5...");
				dos.writeUTF(member_id);
				if(!(dis.readUTF().equals("true")))
				{
					System.out.println("server problem...\nTry again later");
					this.getChoice();
				}
				else
				{
					System.out.println("\nMember removed..");
					this.manageGroup();	
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

	private void addAdminPermission()
	{
		String member_id = "";
		System.out.println("Enter user id: ");
		try
		{
			member_id = in.nextLine();
			this.addAdminPermission(member_id);
		}			
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void addAdminPermission(String member_id)
	{
		int nmsg = 0;
		try
		{
			if(this.checkAdmin(member_id))
			{
				System.out.println("Admin permission is already added");
				this.manageGroup(username);
			}

			if(this.checkAdmin(username))
			while(true)
			{
				if(this.checkMember(member_id))
					break;
				else
				{
					member_id = in.next();
					System.out.println("user not exist, enter correct user id: ");
				}
			}
			else
			{
				System.out.println("Permission denied ");
				this.manageGroup();
			}
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
				dos.writeUTF("addAdminPermission");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}			
					
				//System.out.println("\nsend message 4...");					
				dos.writeUTF(group_id);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				//System.out.println("\nsend message 5...");
				dos.writeUTF(member_id);
				if(!(dis.readUTF().equals("true")))
				{
					System.out.println("server problem...\nTry again later");
					this.getChoice();
				}
				else
				{
					System.out.println("Admin permission added to member "+member_id);
					this.manageGroup();	
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

	private void removeAdminPermission()
	{
		String member_id = "";
		System.out.println("Enter user id: ");
		try
		{
			member_id = in.nextLine();
			this.removeAdminPermission(member_id);
		}			
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void removeAdminPermission(String member_id)
	{
		int nmsg = 0;
		try
		{
			if(!this.checkAdmin(member_id))
			{
				System.out.println("Admin permission is already removed ");
				this.manageGroup(username);
			}

			if(this.checkAdmin(username))
			while(true)
			{
				if(this.checkMember(member_id))
					break;
				else
				{
					member_id = in.next();
					System.out.println("user not exist, enter correct user id: ");
				}
			}
			else
			{
				System.out.println("Permission denied ");
				this.manageGroup();
			}
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
				dos.writeUTF("removeAdminPermission");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}			
					
				//System.out.println("\nsend message 4...");					
				dos.writeUTF(group_id);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				//System.out.println("\nsend message 5...");
				dos.writeUTF(member_id);
				if(!(dis.readUTF().equals("true")))
				{
					System.out.println("server problem...\nTry again later");
					this.getChoice();
				}
				else
				{
					System.out.println("Admin permission removed to member "+member_id);
					this.manageGroup();	
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

	private void setRead()
	{
		try
		{
			if(checkConnection() == true)
			{
				dos.writeUTF("ChatGroup");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF("setRead");
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
				if(!(dis.readUTF().equals("true")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
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

	private void showMembers()
	{
		int no_of_members;
		String contact_name, id, status, last_seen;
		try
		{
			no_of_members = this.getNoOfMembers()+6;
			for(int i = 7; i <= no_of_members; i++)
			{
				id = this.getMemberID(i-6);
				System.out.print("\n**-["+i+"]-**");
				this.showMemberDetails(id);
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

	private String getMemberID(int position)
	{
		String response = null;
		try
		{
			if(checkConnection() == true)
			{
				dos.writeUTF("ChatGroup");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("getMemberID");
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

	private void handleMembersChoice(int choice)
	{
		if(choice <= (this.getNoOfMembers()+6))
		{
			this.manageGroup(this.getMemberID(choice-6));
		}
		else
		{
			System.out.println("Incorrect choice\nRestarting messenger...\n");
			this.manageGroup();
		}
	}

	private void showMemberDetails(String id)
	{
		try
		{
				System.out.print(" || member id: "+id);
				System.out.print(" || Name: "+this.getContactName(id));
				System.out.println(" || Admin: "+this.checkAdmin(id));
				System.out.print("Status: "+this.getContactStatus(id));
				System.out.println(" || Last seen: "+this.getContactLastSeen(id));
		}			
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private int getNoOfMessages()
	{
		int nmsg = 0;
		String response = "0";
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
				dos.writeUTF("getNoOfMessages");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				//System.out.println("\nsend message 3...");
				dos.writeUTF(username);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}				
					
				//System.out.println("\nsend message 4...");					
				dos.writeUTF(group_id);
				
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

	private String getMessageTime(int pos)
	{
		String response = "";
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
				dos.writeUTF("getMessageTime");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				//System.out.println("\nsend message 3...");
				dos.writeUTF(username);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}				
					
				//System.out.println("\nsend message 4...");					
				dos.writeUTF(group_id);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}						

				dos.writeUTF(""+pos);		
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

	private String getMessageSender(int pos)
	{
		String response = "";
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
				dos.writeUTF("getMessageSender");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				//System.out.println("\nsend message 3...");
				dos.writeUTF(username);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}				
					
				//System.out.println("\nsend message 4...");					
				dos.writeUTF(group_id);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}						

				dos.writeUTF(""+pos);		
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

	private String getMessage(int pos)
	{
		String response = "";
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
				dos.writeUTF("getMessage");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				//System.out.println("\nsend message 3...");
				dos.writeUTF(username);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}				
					
				//System.out.println("\nsend message 4...");					
				dos.writeUTF(group_id);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}						

				dos.writeUTF(""+pos);		
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

	private boolean checkAdmin(String member_id)
	{
		String response = "false";
		boolean check_admin = false;
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
				dos.writeUTF("checkAdmin");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				//System.out.println("\ncheck admin: "+member_id);
				
				dos.writeUTF(group_id);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}				
					
				//System.out.println("\nsend message 4...");					
				dos.writeUTF(member_id);
				response = dis.readUTF();

				if(response.equals("true"))
					check_admin = true;
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
		return check_admin;
	}

	private boolean checkMember(String member_id)
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

	private int getNoOfMembers()
	{
		String response = "0";
		int result = 0;
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
				dos.writeUTF("getNoOfMembers");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF(group_id);
				response = dis.readUTF();

				result = Integer.parseInt(response);
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
		return result;
	}

	private void ChatGroup(String id)
	{
		ChatGroup cc = new ChatGroup();
		cc.main(username, password, socketObject, id);
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