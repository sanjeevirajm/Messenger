package com.sanjeeviraj.messenger.client;

import java.net.*; 
import java.io.*;
import java.util.*;

public class SignIn implements Constants
{
	private static boolean CLOSE_CONN = true;
	private String username, password, question, answer, forgot_pass;
	private int choice, ntimes = 0;
	private boolean check_user, check_pass;

	private Socket socketObject;
	private OutputStream os;
	private DataOutputStream dos;
	private InputStream is;
	private DataInputStream dis;
	private Scanner in;
	
	public void main()
	{
		SignIn si = new SignIn();
		try
		{
			si.startConnection();
			
			if(si.checkConnection())
				si.getChoice();
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
					os.close();
					dos.close();
					is.close();
					dis.close();
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
			socketObject = new Socket(SERVER_NAME, PORT_NUMBER); 
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
			//System.out.print("  checking connection...");
		 	
			dos.writeUTF("check_connection");
			if(!(dis.readUTF().equals("connected")))
				check_connection = false;
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
	
	
	public void getChoice()
	{
		char[] passwordArray;
		Console console = System.console();
		in = new Scanner(System.in);
		try
		{
			System.out.println("\nEnter choice \n1-sign in\n2-Back");
			choice = in.nextInt();
			
			switch(choice)
			{
				case 1: break;
						
				case 2: Messenger ms = new Messenger();
						ms.getChoice();
						break;
	
				default: System.out.println("\nIncorrect choice\nRestarting messenger...");
						 this.getChoice();
			}
			
			System.out.println("Enter username : ");
			while(true)
			{
				username = in.next();	
				
				if(username.equals(BACK))
					goBack();
				if(username.equals(EXIT))
					exit();
				if(checkUsername(username))
					break; //checkUsername(string) function returns true when the user exists
				else
					System.out.print("Enter correct username : ");
			}
			
			System.out.print("Enter password (Password will not be shown) : ");
			passwordArray = console.readPassword();
			password = new String(passwordArray);
			
			if(password.equals(BACK))
				goBack();
			if(password.equals(EXIT))
				exit();			
			
			while(true)
			{
				if(checkPassword(username, password))
				{
					check_pass = true;
					break;
				}
			
				System.out.print("Enter correct password : ");
				
				passwordArray = console.readPassword();
				password = new String(passwordArray);
				
				if(password.equals(BACK))
					goBack();
				if(password.equals(EXIT))
					exit();
				
				ntimes++;
				if(ntimes > 1)
					System.out.println("Password hint : "+getHint(username));
				
				if(ntimes > 3)
				{
					ntimes = 0;
					System.out.print("Did you forgot password (Type yes or no) : ");
					forgot_pass = in.next();
					
					if(forgot_pass.equals("yes"))
					{
						recoverAccount(username);
						break;
					}
				}
			}
			if(check_pass)
			{
				System.out.println("signed in as "+username);
							
				StartScreen sc = new StartScreen();
				sc.main(username, password, socketObject);		
			}
		}
		catch(InputMismatchException ime)
		{
			System.out.println("\nwrong input \nRestarting messenger...");
			this.getChoice();
		}
		return;
	}
	
	
	private boolean checkUsername(String username)
	{
		try
		{
			if(checkConnection())
			{
				dos.writeUTF("SignUp");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
			
				dos.writeUTF("checkUsername");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF(username);
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
	
	
	private boolean checkPassword(String username, String password)
	{	
		try
		{
			if(checkConnection())
			{		
				dos.writeUTF("SignIn");	
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("signIn");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF(username+"\n"+password);
				if(dis.readUTF().equals("true"))
					check_pass = true;
				else
					check_pass = false;
			}
		} 
		catch(ConnectException ce)
		{
			System.out.println("Connection error. Try again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
		return check_pass;
	}
	
	
	private void recoverAccount(String username)
	{
		boolean check_answer = false;
		char[] passwordArray;
		char[] answerArray;
		Console console = System.console();
		try
		{
			if(checkConnection())
			{
				dos.writeUTF("SignIn");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("getQuestion");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF(username);
				question = dis.readUTF();
				
				dos.writeUTF("SignIn");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("getAnswer");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF(username);
				answer = dis.readUTF();
				
				System.out.print("\nPersonal question: "+question+"\nEnter answer : ");
				while(true)
				{
					answerArray = console.readPassword();
					if(answer.equals(new String(answerArray)))
					{
						check_answer = true;
						break;
					}
					else
					{
						System.out.print("\nEnter correct answer : ");
					}
				}
				
				if(check_answer)
				{
					System.out.print("Note: There is no rules for password. Password can be any number of characters. Password will be not shown. Enter password : ");
						while(true)
						{
							passwordArray = console.readPassword();
							password = new String(passwordArray);
			
							if(password.equals(BACK))
								goBack();
							if(password.equals(EXIT))
								exit();
				
							System.out.print("Retype password : ");
				
							passwordArray = console.readPassword();
				
							if(password.equals(new String(passwordArray)))
								break;
							else
								System.out.println("Not matched\nstart again");
						}
					dos.writeUTF("SignIn");
					if(!(dis.readUTF().equals("connected")))
					{
						System.out.println("No response in server\nTry again later");
						System.exit(0);
					}
					dos.writeUTF("changePassword");
					if(!(dis.readUTF().equals("connected")))
					{
						System.out.println("No response in server\nTry again later");
						System.exit(0);
					}
					dos.writeUTF(username);
					dos.writeUTF(password);
				
					if(dis.readUTF().equals("true"))
					{
						System.out.println("Password changed");
						Messenger ms = new Messenger();
						ms.getChoice();
					}	
					
				}
			}
			else
			{
				System.out.println("Connection error\nTry again later");
				System.exit(0);
			}
		}
		catch(ConnectException ce)
		{
			System.out.println("Connection error. Try again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private String getHint(String username)
	{
		String hint = "";
		try
		{
			if(checkConnection())
			{
				dos.writeUTF("SignIn");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				dos.writeUTF("getHint");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				dos.writeUTF(username);
				hint = dis.readUTF();
			}
			else
			{
				System.out.println("Connection error\nTry again later");
				System.exit(0);
			}
		}
		catch(ConnectException ce)
		{
			System.out.println("Connection error. Try again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
		return hint;
	}
	
	private void goBack()
	{
		Messenger ms = new Messenger();
		ms.getChoice();	
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
}	