package com.sanjeeviraj.messenger.client;

import java.net.*; 
import java.io.*;
import java.util.*;

public class SignUp implements Constants
{
	private static boolean CLOSE_CONN = true;
	private String username, password, question, answer, hint, forgot_pass, name, status;
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
		SignUp su = new SignUp();
		try
		{
			su.startConnection();
			if(su.checkConnection())
				su.getChoice();
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
		System.out.println("  Connecting...");    
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
		char[] passwordArray, answerArray;
		try
		{
			Console console = System.console();
			in = new Scanner(System.in);
			
			System.out.println("\nEnter choice \n1-sign up\n2-Back");
			choice = in.nextInt();
			
			switch(choice)
			{
				case 1: break;
						
				case 2: goBack();
						break;
	
				default: System.out.println("\nIncorrect choice\nRestarting messenger...");
						 this.getChoice();
			}
			
			System.out.println("You can exit or go back by typing exit or back in small letters anywhere\n----------------------------------------------------------\n");
			System.out.println("Suggestion: Use your mail id, phone number. Because, username you want may be already used by others. \nEnter username : ");

			while(true)
			{
				username = in.nextLine();
		
				if(check_input(username))
				{
					if(!checkUsername(username))
						break; //if false(username available), move to next input
					else
						System.out.print("Enter different username : ");
				}
			}
			
			System.out.println("\nusername available ");
			System.out.print("Note: There is no rules for password. Password will be not shown. Enter password : ");
			
			while(true)
			{
				passwordArray = console.readPassword();
				password = new String(passwordArray);
			
				if(!check_input(password))
				{
					System.out.print("Password can't be empty\nTry another password : ");
					continue;
				}
				
				System.out.print("Retype password : ");
				passwordArray = console.readPassword();
				
				if(password.equals(new String(passwordArray)))
					break; //password matched, so move to next input
				else
					System.out.println("Not matched\nType again");
			}
			

			System.out.print("Enter a hint (This will be shown if you forget password) : ");
			hint = in.next();
			check_input(hint); //hint can be empty, so nothing to do with the return value

			System.out.print("\nEnter a private/personal question : ");
			question = in.next();			
			check_input(question); //question can be empty, so nothing to do with the return value
			
			System.out.print("\nEnter answer : ");
			answerArray = console.readPassword();
			answer = new String(answerArray); //answeralc can be empty, so nothing to do with the return value
			
			if(answer.equals(BACK))
				goBack();
			if(answer.equals(EXIT))
				exit();
			
			while(true)
			{
				System.out.print("\nEnter your actual name : ");
				name = in.next();
				if(name.equals(BACK))
					goBack();
				if(name.equals(EXIT))
					exit();
				if(!name.isEmpty())
					break;
			}
				
			while(true)
			{
				System.out.print("\nEnter status : ");
				status = in.next();
				if(status.equals(BACK))
					goBack();
				if(status.equals(EXIT))
					exit();
				if(!status.isEmpty())
					break;
			}
			signUp(username, password, hint, question, answer, name, status);			
		}
		catch(InputMismatchException ime)
		{
			System.out.println("\nwrong input \nRestarting messenger...");
			this.getChoice();
		}
		return;
	}
	
	
	private void signUp(String username, String password, String hint, String question, String answer, String name, String status)
	{
		try
		{
			if(checkConnection())
			{
				dos.writeUTF("SignUp");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response from server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("signUp");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response from server\nTry again later");
					System.exit(0);
				}
				
				String user_details = "\n"+username+"\n"+password+"\n"+hint+"\n"+question+"\n"+answer;
			
				dos.writeUTF(user_details);
				if(!(dis.readUTF().equals("true")))
				{
					System.out.println("Incorrect response in server\nTry again later");
					System.exit(0);
				}				
				
				dos.writeUTF("Profile");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response from server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("createProfile");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response from server\nTry again later");
					System.exit(0);
				}
				
				user_details = "\n"+name+"\n"+status;
				
				dos.writeUTF(user_details);
				if(!(dis.readUTF().equals("true")))
				{
					System.out.println("Incorrect response in server\nTry again later");
					System.exit(0);
				}				
			}
			else
			{
				System.out.println("Connection error\nTry again later");
				System.exit(0);
			}
			
			System.out.println("Account created");
			goBack();
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
	
	
	private boolean checkUsername(String username)
	{
		try
		{	
			if(checkConnection())
			{
				dos.writeUTF("SignUp");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response from server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("checkUsername");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response from server\nTry again later");
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
	
	
	private void goBack()
	{
		Messenger ms = new Messenger();
		ms.getChoice();	
	}
	
	private boolean check_input(String input_string)
	{
		boolean return_val = false;
		if(input_string.equals("back"))
			goBack();
		else if(input_string.equals("exit"))
			exit();
		else if(input_string.isEmpty())
			return_val = false;
		else
			return_val = true;
		return return_val;
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