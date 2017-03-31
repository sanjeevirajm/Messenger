package com.sanjeeviraj.messenger.server;

import java.util.Scanner;
import java.net.*; 
import java.io.*;

public class Interrupt extends Thread
{
	private Socket socketObject;
	public static final String SERVER_NAME = "127.0.0.1";
	public static final int PORT_NUMBER = 6666;
	public void run()
	{
		try
		{
			String input;
			Scanner in = new Scanner(System.in);
			
			while(true)
			{
				input = in.next();
				if(input.equalsIgnoreCase("exit"))
				{
					System.out.println("Confirm exit? Type yes or no");
					input = in.next();
					if(input.equalsIgnoreCase("yes"))
						break;
				}
			}
			Start.EXIT_CODE = true;
			startConnection();
		}
		catch(Exception e)
		{
			System.out.println("Error occurred\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}

	private void startConnection()
	{
		System.out.println("trying to exit...");    
		try
		{
			socketObject = new Socket(SERVER_NAME, PORT_NUMBER); 		 
		}
		catch(ConnectException ce)
		{
			System.out.println("Connection error\nPossible reasons: server not available\nserver refused to connect\nserver busy\nTry again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}
}