package com.sanjeeviraj.messenger.server;

import java.net.*; 
import java.util.Scanner;

public class Start extends Thread implements Constants
{
	public static boolean EXIT_CODE = false;      
	
	public static void main(String[] args)
	{
		try
		{
			String input;
			Scanner in = new Scanner(System.in);
			ServerSocket serverSocket = new ServerSocket(PORT_NUMBER); 

			Thread ir = new Interrupt();
			ir.start();
			while(true)
			{
				if(EXIT_CODE)
					break;

				System.out.println("Waiting for connection request...");
				Socket socketObject = serverSocket.accept();
				if(EXIT_CODE)
					break;
				Thread ms = new Messenger(serverSocket, socketObject);
				if(EXIT_CODE)
					break;
				ms.start();
				if(EXIT_CODE)
					break;
			}
		}
		catch(SocketTimeoutException s)
		{
			System.out.println("Socket timed out!");
		}
		catch(Exception e)
		{
			System.out.println("Error occurred\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}
}