package com.sanjeeviraj.messenger.server;

import java.net.*; 
import java.io.*;

public class Messenger extends Thread implements Choice, Constants
{
	public Messenger() {	}
	
	public Messenger(ServerSocket serverSocketArg, Socket socketObjectArg)
	{
		serverSocket = serverSocketArg;
		socketObject = socketObjectArg;
	}
	
	public static final int PORT_NUMBER = 6666;      
	Socket socketObject;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	Messenger ms;
	ServerSocket serverSocket;
	boolean conn = true;

	public void run()
	{
		try
		{
			ms = new Messenger(serverSocket, socketObject);
			ms.startConnection();
			while(ms.conn)
			{
				ms.getChoice();			
			}	
		}
		catch(Exception e)
		{
			//System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			ms.conn = false;
		}
		finally
		{
			try
			{
				System.out.println("Disconnecting from client");
				//socketObject.close();
				ms.conn = false;
			}
			catch(Exception e)
			{
				System.out.println("Error while closing the connection \nError details:\n");
				e.printStackTrace();
				ms.conn = false;
			}
		}
	}
	
	private void startConnection()
	{
		//System.out.println("starting...");    
		try
		{
			os = socketObject.getOutputStream();
			dos = new DataOutputStream(os);
			is = socketObject.getInputStream();
			dis	= new DataInputStream(is);	
			//serverSocket.setSoTimeout(1000000);   
		}
		catch(ConnectException ce)
		{
			//System.out.println("Connection error\nTry again later.");
			ms.conn = false;
		}
		catch(Exception e)
		{
			//System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			ms.conn = false;
		}
	}
	
   public void getChoice()
   {
	   try
	   {
		switch(dis.readUTF())
		{
			case CHECKCONNECTION :	dos.writeUTF("connected");
									//System.out.println("Messenger class: connection checked by "+socketObject.getRemoteSocketAddress());
									break;
								   
			case SIGNINCLASS :		dos.writeUTF("connected");
									//System.out.println("signin by "+socketObject.getRemoteSocketAddress());
									SignIn si = new SignIn();
									si.main(serverSocket, socketObject);
									break;
								   
			case SIGNUPCLASS : 		dos.writeUTF("connected");
									//System.out.println("signup by "+socketObject.getRemoteSocketAddress());
									SignUp su = new SignUp();
									su.main(serverSocket, socketObject);
									break;
									
			case PROFILECLASS : 	////System.out.println("input : profile class");
							    	dos.writeUTF("connected");
							    	Profile pf = new Profile();
									pf.main(serverSocket, socketObject);
							    	//System.out.println("profile class "+socketObject.getRemoteSocketAddress());
									break;

			case CONTACTS :    		//System.out.println("input : Signin class");
							   		dos.writeUTF("connected");
							   		Contacts co = new Contacts();
							   		co.main(serverSocket, socketObject);
							   		//System.out.println("signin by "+socketObject.getRemoteSocketAddress());
							   		break;

			case GROUPS :    		//System.out.println("input : Signin class");
							   		dos.writeUTF("connected");
							   		Groups gr = new Groups();
							   		gr.main(serverSocket, socketObject);
							   		//System.out.println("signin by "+socketObject.getRemoteSocketAddress());
							   		break;				   		

			case CHATS :    		//System.out.println("input : Signin class");
							   		dos.writeUTF("connected");
							   		Chats ch = new Chats();
							   		ch.main(serverSocket, socketObject);
							   		//System.out.println("signin by "+socketObject.getRemoteSocketAddress());
							   		break;							   									  					

			case CHATCONTACT :    	//System.out.println("input : Signin class");
							   		dos.writeUTF("connected");
							   		ChatContact cc = new ChatContact();
							   		cc.main(serverSocket, socketObject);
							   		//System.out.println("signin by "+socketObject.getRemoteSocketAddress());
							   		break;		

			case CHATGROUP :    	//System.out.println("input : Signin class");
							   		dos.writeUTF("connected");
							   		ChatGroup cg = new ChatGroup();
							   		cg.main(serverSocket, socketObject);
							   		//System.out.println("signin by "+socketObject.getRemoteSocketAddress());
							   		break;							  					

			case EXIT : 	ms.conn = false;
							break;						
			default : break;	   
		}
	   }
	   catch(java.net.SocketException se)
	   {
	    	//System.out.println("SocketException occured\nError details : ");
			se.printStackTrace();
			//System.out.println("Disconnecting from server");
			try
			{
				//socketObject.close();
				ms.conn = false;
			}
			catch(Exception e)
			{
				System.out.println("Error occured while closing the connection");
				e.printStackTrace();
				ms.conn = false;
			}
	   }
	   catch(IOException ioe)
	   {
	    	//System.out.println("IOException occured\nError details : ");
			ioe.printStackTrace();
			//System.out.println("Disconnecting from server");
			try
			{
				//socketObject.close();
				ms.conn = false;
			}
			catch(Exception e)
			{
				System.out.println("Error occured while closing the connection");
				e.printStackTrace();
				ms.conn = false;
			}		   
	   }
   }
}