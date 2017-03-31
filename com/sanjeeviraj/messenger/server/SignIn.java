package com.sanjeeviraj.messenger.server;

import java.net.*; 
import java.io.*;

public class SignIn implements Choice
{
	public SignIn() {	}
	
	public SignIn(ServerSocket serverSocketArg, Socket socketObjectArg)
	{
		//System.out.println("SignIn constructor");
		serverSocket = serverSocketArg;
		socketObject = socketObjectArg;
	}
	
	private static String PATH = "com\\sanjeeviraj\\messenger\\server\\Accounts.config";
	public static final String CHECKUSERNAME = "checkUsername";
	public static final String RECOVERACCOUNT = "recoverAcount";
	public static final String SIGNIN = "signIn";
	public static final String GETPASSWORD = "getPassword";
	public static final String CHANGEPASSWORD = "changePassword";
	public static final String GETHINT = "getHint";
	public static final String CHANGEHINT = "changeHint";
	public static final String CHANGERECOVERYDETAILS = "changeRecoveryDetails";
	public static final String GETQUESTION = "getQuestion";
	public static final String GETANSWER = "getAnswer";
	public static final int PORT_NUMBER = 6666;   
	private static boolean CLOSE_CONN = false;
    private String user_details, username;	
	Socket socketObject;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	SignIn si;
	ServerSocket serverSocket;
	LineNumberReader lnr;
	
	public void main(ServerSocket serverSocketArg, Socket socketObjectArg)
	{
		try
		{
			////System.out.println("signin main(sa,soa)");
			si = new SignIn(serverSocketArg, socketObjectArg);
			////System.out.println("signin object created");
			si.startConnection();
			//System.out.println("after connecting to the client");
			si.getChoice();					
			//System.out.println("last in main(sa,soa)");
		}
		catch(Exception e)
		{
			//System.out.println("Error occured\nError Details : ");
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
		//System.out.println("starting...");    
		try
		{
			//socketObject = new Socket(PORT_NUMBER); 
			os = socketObject.getOutputStream();
			dos = new DataOutputStream(os);
			is = socketObject.getInputStream();
			dis	= new DataInputStream(is);	
			//serverSocket.setSoTimeout(1000000);   
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
		//System.out.println("waiting for input... ");   
		String input = dis.readUTF();
		switch(input)
		{
			case CHECKCONNECTION : //System.out.println("input check connection");		
								   dos.writeUTF("connected");
								   //System.out.println("connection checked by "+socketObject.getRemoteSocketAddress());
								   break;
								   
			case SIGNINCLASS :	   //System.out.println("input : Signin class");
								   dos.writeUTF("connected");
								   //System.out.println("signin by "+socketObject.getRemoteSocketAddress());
								   si.getChoice();
								   break;
								   							   
			case CHECKUSERNAME :   //System.out.println("input : check username ");
								   dos.writeUTF("connected");
								   //System.out.println("username checked by "+socketObject.getRemoteSocketAddress());
								   username = dis.readUTF();
								   if(checkUsername(username))
									  dos.writeUTF("true");
								   else
									  dos.writeUTF("false");
								   //System.out.println("username checked by "+socketObject.getRemoteSocketAddress());
								   break;
								   
			case SIGNIN : 		   //System.out.println("input: signin method");
								   dos.writeUTF("connected");
								   //System.out.println("signin details sent by "+socketObject.getRemoteSocketAddress());
								   String user_details = dis.readUTF();
								   signIn(user_details);
								   //System.out.println("signin details sent by "+socketObject.getRemoteSocketAddress());
								   break;
								   
			case CHANGEPASSWORD :  //System.out.println("input: recoverAcount method");
								   dos.writeUTF("connected");
								   username = dis.readUTF();
								   String password = dis.readUTF();
								   changePassword(username, password);
								   break;					   
								   
			case GETPASSWORD :     //System.out.println("input: recoverAcount method");
								   dos.writeUTF("connected");
								   username = dis.readUTF();
								   dos.writeUTF(getPassword(username));
								   break;					   

			case GETHINT :  	   //System.out.println("input: get hint method");
								   dos.writeUTF("connected");
								   username = dis.readUTF();
								   dos.writeUTF(getHint(username));
								   break;					   
			
			case CHANGEHINT :  	   //System.out.println("input: change hint method");
								   dos.writeUTF("connected");
								   username = dis.readUTF();
								   changeHint(username);
								   break;	

			case CHANGERECOVERYDETAILS : //System.out.println("input: changeRecoveryDetails method");
								   dos.writeUTF("connected");
								   username = dis.readUTF();
								   changeRecoveryDetails(username);
								   break;						   

			case GETQUESTION :     //System.out.println("input: recoverAcount method");
								   dos.writeUTF("connected");
								   username = dis.readUTF();
								   dos.writeUTF(getQuestion(username));
								   break;					   
								 
			case GETANSWER :  	   //System.out.println("input: recoverAcount method");
								   dos.writeUTF("connected");
								   username = dis.readUTF();
								   dos.writeUTF(getAnswer(username));
								   break;					   								 
								   
			case EXIT : 		   //System.out.println("input : exit");
								   CLOSE_CONN = true;
								   break;						
								   
			default : //System.out.println("default in getchoice");
						break;	   
		}
	   }
	   catch(Exception e)
	   {
	    	//System.out.println("Error occured\nError details : ");
			e.printStackTrace();
	   }   
    }
	
	private boolean checkUsername(String username)
	{
		String existing_user;
		boolean check_password = false;
		try
		{
			lnr = new LineNumberReader(new FileReader(PATH));
			
			//System.out.println("usernames\n");
			while(true)
		 	{
				//System.out.println("while true");
				//System.out.println("if out");
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
								check_password = true;
								break;
							}
						}
					}
				}
				break;
			}
		 //System.out.println("5 checking "+username+" with "+existing_user+" "+check_password);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return check_password;
	}
	
	private void signIn(String user_details)
	{	
		//System.out.println("signin(user_details) : "+user_details);
		username = user_details.substring(0, user_details.indexOf('\n'));
		String password = user_details.substring((user_details.indexOf('\n')+1), user_details.length());
		//System.out.println("username : "+username);
		String existing_user;
		String existing_pass;
		boolean check_password = false;
		try
		{
			lnr = new LineNumberReader(new FileReader(PATH));
			
			//System.out.println("usernames\n");
			while(true)
		 	{
				//System.out.println("while true");
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
								existing_pass = lnr.readLine();
								//System.out.println("4 checking "+username+" "+password+" with "+existing_user+" " +existing_pass);
								if(password.equals(existing_pass))
								{
									//System.out.println("check pass : true");
									check_password = true;
									break;									
								}
								else
								{
									//System.out.println("check pass : false");
									check_password = false;
									break;									
								}
							}
						}
					}
				}
				break;
			}
			dos.writeUTF(""+check_password);
		 //System.out.println("5 checking "+username+" with "+existing_user+" "+check_password);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void changePassword(String username, String password)
	{	
		String data1 = "";//to store data in file before username
		String data2 = username + "\n" + password;
		String data3 = "";//to store data in file after password
		char old_details[] = (username + "\n" + getPassword(username)).toCharArray();
		char new_details[] = (username + "\n" + password).toCharArray();
		String file_data = "";
		String data;
		File config_file;
		FileWriter fwr = null;
		boolean file_closed = false;
		try
		{
			lnr = new LineNumberReader(new FileReader(PATH));
			
			while((data = lnr.readLine()) != null)
			{
				if(data.equals(username))
				{
					lnr.readLine();
					while((data = lnr.readLine()) != null)
					{
						data3 = data3 + data + "\n";
					}
					break;
				}
				//System.out.println("Data : "+data);
				//System.out.println("Filedata : "+file_data);
				data1 = data1 + data + "\n";
			}
			lnr.close();
			data3 = data3.trim();
			//file_data = file_data.replace(old_details[old_details.length-1], new_details[new_details.length-1]);
			file_data = data1 + data2 + "\n" + data3;
			//System.out.println("Filedata : "+file_data);
			
			config_file = new File(PATH);
			
			fwr = new FileWriter(config_file);
			fwr.write(file_data);
			//System.out.println("file edited");
			fwr.close();
			file_closed = true;
			
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

	private String getPassword(String username)
	{	
		String password = null;
		//System.out.println("username : "+username);
		String existing_user;
		try
		{
			lnr = new LineNumberReader(new FileReader(PATH));
						
			//System.out.println("usernames\n");
			while(true)
		 	{
				//System.out.println("while true");
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
								password = lnr.readLine();
								break;									
							}
						}
					}
				}
				break;
			}
		 //System.out.println("5 checking "+username+" with "+existing_user+" "+password);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return password;
	}	
	
	private String getHint(String username)
	{	
		String hint = null;
		//System.out.println("username : "+username);
		String existing_user;
		try
		{
			lnr = new LineNumberReader(new FileReader(PATH));
						
			//System.out.println("usernames\n");
			while(true)
		 	{
				//System.out.println("while true");
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
								lnr.readLine();
								hint = lnr.readLine();
								break;									
							}
						}
					}
				}
				break;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return hint;
	}	

	private void changeHint(String username)
	{	
		String data1 = "";//to store data in file before username
		
		String data3 = "";//to store data in file after hint
		
		String file_data = "";
		String data, new_hint, hint;
		File config_file;
		FileWriter fwr = null;
		boolean file_closed = false;
		try
		{
			dos.writeUTF("connected");
			new_hint = dis.readUTF();
			hint = getHint(username);
			String data2 = username + "\n" + getPassword(username) + "\n" + new_hint;
			
			char old_details[] = (username + "\n" + getPassword(username) +"\n"+ hint).toCharArray();
		char new_details[] = (username + "\n" + getPassword(username) + "\n" + hint).toCharArray();
			lnr = new LineNumberReader(new FileReader(PATH));
			
			while((data = lnr.readLine()) != null)
			{
				if(data.equals(username))
				{
					lnr.readLine();
					lnr.readLine();
					while((data = lnr.readLine()) != null)
					{
						data3 = data3 + data + "\n";
					}
					break;
				}
				//System.out.println("Data : "+data);
				//System.out.println("Filedata : "+file_data);
				data1 = data1 + data + "\n";
			}
			lnr.close();
			data3 = data3.trim();
			//file_data = file_data.replace(old_details[old_details.length-1], new_details[new_details.length-1]);
			file_data = data1 + data2 + "\n" + data3;
			//System.out.println("Filedata : "+file_data);
			
			config_file = new File(PATH);
			
			fwr = new FileWriter(config_file);
			fwr.write(file_data);
			//System.out.println("file edited");
			fwr.close();
			file_closed = true;
			
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

	private void changeRecoveryDetails(String username)
	{	
		String new_question, new_answer;
		String question = getQuestion(username);
		String answer = getAnswer(username);
		String hint = getHint(username);

		String data1 = "";//to store data in file before username
		
		String data3 = "";//to store data in file after answer
		
		String file_data = "";
		String data;
		File config_file;
		FileWriter fwr = null;
		boolean file_closed = false;
		try
		{
			dos.writeUTF("connected");
			new_question = dis.readUTF();
			new_answer = dis.readUTF();

			String data2 = username + "\n" + getPassword(username) + "\n" + hint + "\n" + new_question + "\n" + new_answer;
			//System.out.println("\n\n\n*--------*\nmodified data : \n*-----------*\n"+data2);
			char old_details[] = (username + "\n" + getPassword(username) +"\n"+ hint + "\n" + question + "\n" + answer).toCharArray();
		char new_details[] = (username + "\n" + getPassword(username) + "\n" + hint + "\n" + new_question + "\n" + new_answer).toCharArray();
			lnr = new LineNumberReader(new FileReader(PATH));
			
			while((data = lnr.readLine()) != null)
			{
				if(data.equals(username))
				{
					lnr.readLine();
					lnr.readLine();
					lnr.readLine();
					lnr.readLine();
					while((data = lnr.readLine()) != null)
					{
						data3 = data3 + data + "\n";
					}
					break;
				}
				else
				{
					////System.out.println("Data : "+data);
					////System.out.println("Filedata : "+file_data);
					data1 = data1 + data + "\n";
				}	

			}
			//System.out.println("modified data : "+data2);
			lnr.close();
			data3 = data3.trim();
			//file_data = file_data.replace(old_details[old_details.length-1], new_details[new_details.length-1]);
			file_data = data1 + data2 + "\n" + data3;
			////System.out.println("Filedata : "+file_data);
			
			config_file = new File(PATH);
			
			fwr = new FileWriter(config_file);
			fwr.write(file_data);
			//System.out.println("file edited");
			fwr.close();
			file_closed = true;
			
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

	private String getQuestion(String username)
	{	
		String question = null;
		//System.out.println("username : "+username);
		String existing_user;
		try
		{
			lnr = new LineNumberReader(new FileReader(PATH));
						
			//System.out.println("usernames\n");
			while(true)
		 	{
				//System.out.println("while true");
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
								lnr.readLine();
								lnr.readLine();
								question = lnr.readLine();
								break;									
							}
						}
					}
				}
				break;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return question;
	}	

	private String getAnswer(String username)
	{	
		String answer = null;
		//System.out.println("username : "+username);
		String existing_user;
		try
		{
			lnr = new LineNumberReader(new FileReader(PATH));
						
			//System.out.println("usernames\n");
			while(true)
		 	{
				//System.out.println("while true");
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
								lnr.readLine();
								lnr.readLine();
								lnr.readLine();
								answer = lnr.readLine();
								break;									
							}
						}
					}
				}
				break;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return answer;
	}		
}