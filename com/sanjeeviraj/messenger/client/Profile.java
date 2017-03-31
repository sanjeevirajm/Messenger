package com.sanjeeviraj.messenger.client;

import java.net.*; 
import java.io.*;
import java.util.*;

public class Profile implements Constants
{
	public Profile()
	{
	}

	public Profile(String usernameArg, String passwordArg, Socket socketObjectArg)
	{
		username = usernameArg;
		password = passwordArg;
		socketObject = socketObjectArg;
	}
	
	private static boolean CLOSE_CONN = true;
	private String username, password;
	private boolean check_user, check_pass;
	
	private Socket socketObject;
	private OutputStream os;
	private DataOutputStream dos;
	private InputStream is;
	private DataInputStream dis;
	private Scanner in;
	public Profile pf;

	public void main(String usernameArg, String passwordArg, Socket socketObjectArg)
	{
		pf = new Profile(usernameArg, passwordArg, socketObjectArg);
		try
		{
			pf.startConnection();
			if(pf.checkConnection())
			{
				pf.getChoice();
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
			System.out.println("\nEnter choice \n1-Change Name \n2-Change Status \n3-Change password \n4-Change secret question & answer \n5-Change hint \n6-Back \n7-Exit \n*----- profile-----*");
			this.printProfile();
			choice = in.nextInt();
			switch(choice)
			{
				case 1: this.changeName();
						break;
						
				case 2: this.changeStatus();
						break;
					
				case 3: this.changePassword();
						break;
						
				case 4: this.changeRecoveryDetails();
						break;
						
				case 5: this.changeHint();
						break;
						
				case 6: this.back();
						break;

				case 7: this.exit();
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
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void changeName()
	{
		String response = "0";
		String new_name;
		try
		{
			new_name = in.nextLine();
			if(this.checkConnection())
			{
				dos.writeUTF("Profile");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("changeName");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF(username);
			
					response = dis.readUTF();
					if(!response.equals("connected"))
					{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
					}

					System.out.print("Enter new name: ");
					new_name = in.nextLine();
					dos.writeUTF(new_name);
					response = dis.readUTF();
					if(!response.equals("changed"))
					{
						System.out.println("Server problem\nTry again...");
						socketObject.close();
						CLOSE_CONN = false;
						SignIn si = new SignIn();
						si.main();
					}	
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

	private void changeStatus()
	{
		String response = "0";
		String new_status;
		try
		{
			new_status = in.nextLine();
			if(this.checkConnection())
			{
				dos.writeUTF("Profile");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("changeStatus");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF(username);
			
					response = dis.readUTF();
					if(!response.equals("connected"))
					{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
					}

					System.out.print("Enter new status: ");
					new_status = in.nextLine();
					dos.writeUTF(new_status);
					response = dis.readUTF();
					if(!response.equals("changed"))
					{
						System.out.println("Server problem\nTry again...");
						socketObject.close();
						CLOSE_CONN = false;
						SignIn si = new SignIn();
						si.main();
					}	
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

	private void changePassword()
	{
		String question, answer;
		boolean check_answer = false;
		char[] passwordArray;
		char[] answerArray;
		Console console = System.console();
		try
		{
			if(this.checkConnection())
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
			
							if(password.equals("EXIT") || password.equals("exit"))
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
		
	private void changeHint()
	{
		String hint, new_hint;
		Console console = System.console();
		try
		{
			if(this.checkConnection())
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

				System.out.println("old hint: "+hint);

				dos.writeUTF("SignIn");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				System.out.print("Enter hint : ");
				new_hint = in.nextLine();
					while(new_hint.equals(""))
						new_hint = in.nextLine();
					
					dos.writeUTF("changeHint");
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
					dos.writeUTF(new_hint);

					if(dis.readUTF().equals("true"))
					{
						System.out.println("Hint changed");
						this.getChoice();
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
	
	private void changeRecoveryDetails()
	{
		String question, answer;
		String new_question, new_answer;
		boolean check_answer = false;
		char[] passwordArray;
		char[] answerArray;
		Console console = System.console();
		try
		{
			if(this.checkConnection())
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
				
				System.out.print("\nPersonal question: "+question+"\nEnter old answer : ");
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
					System.out.print("Enter new Personal question : ");
					new_question = in.nextLine();
					while(new_question.equals(""))
						new_question = in.nextLine();

					System.out.print("Enter answer, answer will be not shown : ");
						while(true)
						{
							answerArray = console.readPassword();
							new_answer = new String(answerArray);
			
							if(password.equals("EXIT") || password.equals("exit"))
								exit();
				
							System.out.print("Retype answer : ");
				
							answerArray = console.readPassword();
				
							if(new_answer.equals(new String(answerArray)))
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
					dos.writeUTF("changeRecoveryDetails");
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

					dos.writeUTF(new_question);
					dos.writeUTF(new_answer);

					if(dis.readUTF().equals("true"))
					{
						System.out.println("Password changed");
						this.getChoice();
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

	private void back()
	{
		try
		{
			StartScreen sc = new StartScreen();
			sc.main(username, password, socketObject);
		}
		catch(Exception e)
		{
			System.out.println("Error while closing the connection \nError details:\n");
			e.printStackTrace();
		}
	}

	private void printProfile()
	{
		System.out.println("--------your profile---------");
		String userid, name, status;
		name = status = null;
		userid = username;
		String response = "0";
		try
		{
			if(this.checkConnection() == true)
			{
				dos.writeUTF("Profile");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF("getProfile");
				if(!(dis.readUTF().equals("connected")))
				{
					System.out.println("No response in server\nTry again later");
					System.exit(0);
				}
				
				dos.writeUTF(username);
				response = dis.readUTF();
				
				name = response.substring(0,response.indexOf('\n'));
				status = response.substring((response.indexOf('\n')+1), response.length());
				}
				else
				{
					System.out.println("unable to connect\nTry again later");
					System.exit(0);
				}
			System.out.println("your name: "+name);
			System.out.println("your userid: "+userid);
			System.out.println("your status: "+status);
		}
		catch(ConnectException ce)
		{
			System.out.println("Connection error. Try again later.");
			System.exit(0);
		}
		catch(Exception e)
		{
			CLOSE_CONN = true;
			System.out.println("print profile ..Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
			System.exit(0);
		}
	}
}