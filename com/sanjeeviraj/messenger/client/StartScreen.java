package com.sanjeeviraj.messenger.client;

import java.net.*; 
import java.io.*;
import java.util.*;

public class StartScreen implements Constants
{
	public StartScreen()
	{

	}
	
	public StartScreen(String usernameArg, String passwordArg, Socket socketObjectArg)
	{
		//System.out.println("StartScreen constructor");
		username = usernameArg;
		password = passwordArg;
		socketObject = socketObjectArg;
	}
	
	private String username, password;
	private boolean check_user, check_pass;
	Socket socketObject;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	Scanner in;
	StartScreen ss;
	Runnable updatetime;
	Thread thread;
	static boolean CLOSE_CONN = true;

	public void main(String usernameArg, String passwordArg, Socket socketObjectArg)
	{
		ss = new StartScreen(usernameArg, passwordArg, socketObjectArg);
		try
		{
			ss.startConnection();
			if(ss.checkConnection())
			{
				updatetime = new UpdateTime(usernameArg);
			  	thread = new Thread(updatetime);
			  	thread.setDaemon(true);
		 	 	thread.start();
				ss.getChoice();
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
			System.out.println("connection checked : "+check_connection);
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
			System.out.println("\nEnter choice \n1-Chats :"+this.getNoOfUnreadMessages()+"\n2-Contacts \n3-Groups \n4-Profile \n5-Exit");
			choice = in.nextInt();
			switch(choice)
			{
				case 1: Chats ch = new Chats();
						ch.main(username, password, socketObject);
						break;
						
				case 2: Contacts co = new Contacts();
						co.main(username, password, socketObject);
						break;
					
				case 3: Groups gr = new Groups();
						gr.main(username, password, socketObject);
						break;
						
				case 4: Profile pf = new Profile();
						pf.main(username, password, socketObject);
						break;
						
				case 5: exit();
						break;
						
				default: System.out.println("\nIncorrect choice\nRestarting messenger...");
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
	
	private int getNoOfUnreadMessages()
	{
		return getNoOfUnreadContactChats() + getNoOfUnreadGroupChats();
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
	
	private void exit()
	{
		try
		{
			if(CLOSE_CONN)
			{
				dos.writeUTF("exit");
				System.out.println("Disconnecting from server");
				socketObject.close();
				UpdateTime.CONDITION=false; //Used to close updatetime thread
				System.exit(0);
			}
		}
		catch(Exception e)
		{
			System.out.println("Error while closing the connection \nError details:\n");
			e.printStackTrace();
		}
	}
}