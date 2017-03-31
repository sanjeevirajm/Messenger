package com.sanjeeviraj.messenger.client;

import java.net.*; 
import java.io.*;
import java.util.*;

public class ChatContact implements Constants
{
	public ChatContact()
	{

	}
	
	public ChatContact(String usernameArg, String passwordArg, Socket socketObjectArg, String contact_id_arg)
	{
		//System.out.println("ChatContact constructor");
		username = usernameArg;
		password = passwordArg;
		socketObject = socketObjectArg;
		contact_id = contact_id_arg;
	}
	
	public static final String SERVER_NAME = "127.0.0.1";
	public static final int PORT_NUMBER = 6666;
	private static boolean CLOSE_CONN = true;
	private String username, password, contact_id;
	private boolean check_user, check_pass;
	Socket socketObject;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	Scanner in;

	int page_no = 1;
	
	public void main(String usernameArg, String passwordArg, Socket socketObjectArg, String contact_id_arg)
	{
		ChatContact cc = new ChatContact(usernameArg, passwordArg, socketObjectArg, contact_id_arg);
		try
		{
			cc.startConnection();
			if(cc.checkConnection())
			{
				cc.getChoice();
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
			System.out.println("\nEnter choice \n1-Send Message \n2-Show next 10 messages(if available) \n3-Show previous 10 messages(if available) \n4-Back \n5-Exit \n*----Chats----*");
			//this.unreadChatContact();
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
						
				case 4: this.goBack();
						break;

				case 5: this.exit();
						break;						
						
				default:  System.out.println("\nwrong input \nRestarting messenger...");
						this.getChoice();
						//this.handleChat(choice);
			}
		}
		catch(InputMismatchException ime)
		{
			System.out.println("\nwrong input \nRestarting messenger...");
			this.getChoice();
		}
		return;
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
				dos.writeUTF("ChatContact");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF("sendMessage");
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
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(message);
				if(!(dis.readUTF().equals("true")))
				{
					System.out.println("server problem...message not sent...\nTry again later");
					this.getChoice();
				}
				System.out.println("\nMessage sent..");
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

	private void showMessages(int page_no)
	{
		int total_messages;
		String message, time;
		int nmessages = page_no * 10;
		try
		{
			//i = nmessages - 9;
			total_messages = this.getNoOfMessages();
			//System.out.println("*-"+no_of_contacts+"-*");
			//System.out.println("*-"+(no_of_contacts-4)+"-*");
			//System.out.println("Total messages : "+total_messages);
			for(int i = nmessages - 9; i <= total_messages && i <= nmessages; i++)
			{
				System.out.print("\n**-["+i+"]-**");
				System.out.print(" || Time: "+this.getMessageTime(i));
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

	private int getNoOfMessages()
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

	private String getMessageTime(int n)
	{
		String response = "";
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

				dos.writeUTF("getMessageTime");
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
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(""+n);
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

	private String getMessage(int n)
	{
		String response = "";
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

				dos.writeUTF("getMessage");
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
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(""+n);
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

	private void setRead()
	{
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
				
				dos.writeUTF(contact_id);
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

	private void chatContact(String id)
	{
		ChatContact cc = new ChatContact();
		cc.main(username, password, socketObject, id);
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
				dos.writeUTF("ChatContact");
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
					
				dos.writeUTF("getContactName");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(username+"\n"+password);
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
				
				dos.writeUTF(username+"\n"+password);
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("username or password is wrong\nSign in again");
					socketObject.close();
					CLOSE_CONN = false;
					SignIn si = new SignIn();
					si.main();
				}
					
				dos.writeUTF("getContactLastSeen");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}

				dos.writeUTF(username+"\n"+password);
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
			Chats ch = new Chats();
			ch.main(username, password, socketObject);
		}
		catch(Exception e)
		{
			System.out.println("Error occured \nError details:\n Trying to close connection");
			exit();
			e.printStackTrace();
		}
	}

}