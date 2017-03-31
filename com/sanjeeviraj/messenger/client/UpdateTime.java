package com.sanjeeviraj.messenger.client;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.net.*; 
import java.io.*;
import java.util.*;

public class UpdateTime implements Runnable, Constants
{
	static boolean CONDITION = true;
	
	Socket socketObject;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	String username;

	public UpdateTime(String userid)
	{
		username = userid;
	}

	private void setTime()
	{    
		boolean check_connection = true;
		try
		{	
		 	dos.writeUTF("Profile");
			if(!(dis.readUTF().equals("connected")))
			{
				System.out.println("No response in server( in thread UpdateTime...)\nTry again later");
			}
				
			dos.writeUTF("updateTime");
			if(!(dis.readUTF().equals("connected")))
			{
				System.out.println("No response in server( in thread UpdateTime...)\nTry again later");
			}

			dos.writeUTF(username);
			if(!(dis.readUTF().equals("changed")))
			{
				System.out.println("No response in server( in thread UpdateTime...)\nTry again later");
			}
		}
		catch(ConnectException ce)
		{
			System.out.println("Connection error in thread UpdateTime...\nTry again later");
			ce.printStackTrace();
		}
		catch(Exception e)
		{
			System.out.println("Error occured in thread UpdateTime...\nError Details : ");
			e.printStackTrace();
		}
	}

	public void run()
	{
		String old_time, new_time;
		Calendar rightNow;
		Date date_obj;
		DateFormat df_obj;
		int difference;
		
		long i = 9;
		try
		{
			socketObject = new Socket(SERVER_NAME, PORT_NUMBER); 
			os = socketObject.getOutputStream();
			dos = new DataOutputStream(os);
			is = socketObject.getInputStream();
			dis	= new DataInputStream(is);		 

			rightNow = Calendar.getInstance();
			df_obj = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
			date_obj = rightNow.getTime();
			old_time = df_obj.format(date_obj);
			new_time = old_time;
			while(CONDITION)
			{
				rightNow = Calendar.getInstance();
				date_obj = rightNow.getTime();
				new_time = df_obj.format(date_obj);
				difference = old_time.compareTo(new_time);
				if(difference == -1)
				{
					i++;
					rightNow = Calendar.getInstance();
					date_obj = rightNow.getTime();
					old_time = df_obj.format(date_obj);
					if(i==10)
					{
						i = 0;
						setTime();
					}
					if(!CONDITION) //Condition variable will be set to false by main program when user closes the program
						close();
				}	
			}
		}
		catch(ConnectException ce)
		{
			System.out.println("Connection error( in thread UpdateTime...)\nPossible reasons: server not available\nserver refused to connect\nserver busy\nTry again later.");
			ce.printStackTrace();
		}
		catch(Exception e)
		{
			System.out.println("Error occured in thread updateTime...\nError Details : ");
			e.printStackTrace();
		}
	}

	private void close()
	{
		try
		{
			dos.writeUTF("exit");
			System.out.println("Disconnecting from server");
			socketObject.close();
		}
		catch(Exception e)
		{
			System.out.println("Error while closing the connection \nError details:\n");
			e.printStackTrace();
		}
	}
}