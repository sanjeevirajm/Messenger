package com.sanjeeviraj.messenger.server;

import java.net.*; 
import java.io.*;

public class SignUp implements Choice
{
	public SignUp() {	}
	
	public SignUp(ServerSocket serverSocketArg, Socket socketObjectArg)
	{
		serverSocket = serverSocketArg;
		socketObject = socketObjectArg;
	}
	
	private static String PATH = "com\\sanjeeviraj\\messenger\\server\\Accounts.config";
	private static String FOLDER_PATH = "com\\sanjeeviraj\\messenger\\server\\";
	public static final String CHECKUSERNAME = "checkUsername";
	public static final String SIGNUP = "signUp";
	public static final int PORT_NUMBER = 6666;   
	private static boolean CLOSE_CONN = false;
    private String user_details;	
	Socket socketObject;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	SignUp su;
	ServerSocket serverSocket;
	LineNumberReader lnr;
	
	public void main(ServerSocket serverSocketArg, Socket socketObjectArg)
	{
		try
		{
			su = new SignUp(serverSocketArg, socketObjectArg);
			su.startConnection();
			su.getChoice();					
		}
		catch(Exception e)
		{
			System.out.println("Error occured\nError Details : ");
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(CLOSE_CONN)
				{	
					System.out.println("Disconnecting from client");
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
		try
		{
			os = socketObject.getOutputStream();
			dos = new DataOutputStream(os);
			is = socketObject.getInputStream();
			dis	= new DataInputStream(is);	
		}
		catch(ConnectException ce)
		{
			System.out.println("Connection error\nTry again later.");
		}
		catch(Exception e)
		{
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
		}
	}
	
   public void getChoice()
   {	
	   try
	   {
		String input = dis.readUTF();
		switch(input)
		{
			case CHECKCONNECTION : //System.out.println("input check connection");		
								   dos.writeUTF("connected");
								   //System.out.println("connection checked by "+socketObject.getRemoteSocketAddress());
								   break;
								   
			case SIGNINCLASS : //System.out.println("input : Signin class");
							   dos.writeUTF("connected");
							   //System.out.println("signin by "+socketObject.getRemoteSocketAddress());
								   break;
								   
			case SIGNUPCLASS : //System.out.println("input : Signin class");
							   dos.writeUTF("connected");
							   getChoice();
							   //System.out.println("signin by "+socketObject.getRemoteSocketAddress());
								   break;

			case CHECKUSERNAME : //System.out.println("input : check username ");
								 dos.writeUTF("connected");
								 //System.out.println("username checked by "+socketObject.getRemoteSocketAddress());
								 String username = dis.readUTF();
								 //System.out.println("input :"+username);
								 if(checkUsername(username))
									  dos.writeUTF("true");
								 else
									  dos.writeUTF("false");
								 //System.out.println("username checked by "+socketObject.getRemoteSocketAddress());
								 break;
								   
			case SIGNUP : 		//System.out.println("input: Signup method");
								dos.writeUTF("connected");
								//System.out.println("signup details sent by "+socketObject.getRemoteSocketAddress());
								String user_details = dis.readUTF();
								signUp(user_details);
							    //System.out.println("signup details sent by "+socketObject.getRemoteSocketAddress());
								break;
								   
			case PROFILECLASS : //System.out.println("input : profile class");
							    dos.writeUTF("connected");
							    Profile pf = new Profile();
								pf.main(serverSocket, socketObject);
							    //System.out.println("profile class "+socketObject.getRemoteSocketAddress());
								break;
								
			case EXIT : 		//System.out.println("input : exit");
								CLOSE_CONN = true;
								break;						
								   
			default : //System.out.println("default in getchoice");
						break;	   
		}
	   }
	   catch(Exception e)
	   {
	    	System.out.println("Error occured\nError details : ");
			e.printStackTrace();
	   }   
    }
	
	
	private boolean checkUsername(String username)
	{
		String existing_user;
		boolean user_exist = false;
		try
		{
			lnr = new LineNumberReader(new FileReader(PATH));
			
			//System.out.println("usernames\n");
			while(true)
		 	{
				while((existing_user = lnr.readLine()) != null) 
				{
					//System.out.println("1 checking "+username+" with "+existing_user);
					if(existing_user.isEmpty())
					{
						//System.out.println("2 checking "+username+" with "+existing_user);
						if((existing_user = lnr.readLine()) != null) 	
						{
							//System.out.println("3 checking "+username+" with "+existing_user);
							if(existing_user.equals(username))
							{
								//System.out.println("4 checking "+username+" with "+existing_user);
								user_exist = true;
								break;
							}
						}
					}
				}
				break;
			}
		 //System.out.println("5 checking "+username+" with "+existing_user+" "+user_exist);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return user_exist;
	}
	
	private void signUp(String user_details)
	{
		String username = user_details.substring(1, user_details.indexOf('\n', 1));
		user_details = "\n"+user_details;
		//System.out.println("Signup(user_details) : "+user_details);
		//System.out.println("username : "+username);
		File config_file;
		File folder;
		FileWriter fwr = null;
		boolean file_closed = false;
		try
		{
			//user_details = dis.readUTF();
			config_file = new File(PATH);
			folder = new File(FOLDER_PATH);

			if(!folder.exists())
				folder.mkdirs();

			if(config_file.exists())
				fwr = new FileWriter(config_file, true);
			else
				fwr = new FileWriter(config_file);
			fwr.write(user_details);
			//System.out.println("file edited");
			fwr.close();
			file_closed = true;
			
			//System.out.println("check user : "+username);
			if(checkUsername(username))
			{
				//System.out.println("username found: file modified");
				dos.writeUTF("true");
			}
			else
			{
				//System.out.println("username not found: file not modified");
				dos.writeUTF("false");
			}			  			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(!file_closed)
					fwr.close();
			}
			catch(IOException ioe)
			{
				//System.out.println("\nError occured while closing the file");
				ioe.printStackTrace();
			}
		}
	}
}