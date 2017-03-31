package com.sanjeeviraj.messenger.server;

import java.net.*; 
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

public class Profile implements Choice
{
	public Profile() {	}
	
	public Profile(ServerSocket serverSocketArg, Socket socketObjectArg)
	{
		//System.out.println("Profile constructor");
		serverSocket = serverSocketArg;
		socketObject = socketObjectArg;
	}
	
	private static String PATH = "com\\sanjeeviraj\\messenger\\server\\Accounts.config";
	public static final String CHECKUSERNAME = "checkUsername";
	public static final String CREATEPROFILE = "createProfile";
	public static final String GETPROFILE = "getProfile";
	public static final String CHANGENAME = "changeName";
	public static final String CHANGESTATUS = "changeStatus";	
	public static final String SIGNUP = "signUp";
	public static final String UPDATETIME = "updateTime";	
	public static final int PORT_NUMBER = 6666;   
	private static boolean CLOSE_CONN = false;
    private String user_details;	
	Socket socketObject;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	Profile pf;
	ServerSocket serverSocket;
	LineNumberReader lnr;
	Calendar rightNow = Calendar.getInstance();
	Date date_obj;
	DateFormat df_obj = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
	
	public void main(ServerSocket serverSocketArg, Socket socketObjectArg)
	{
		try
		{
			//System.out.println("Profile main(sa,soa)");
			pf = new Profile(serverSocketArg, socketObjectArg);
			//System.out.println("Profile object created");
			pf.startConnection();
			//System.out.println("after connecting to the client");
			pf.getChoice();					
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
			//System.out.println("Connection error\nTry again later.");
		}
		catch(Exception e)
		{
			//System.out.println("Error occured\nExiting messenger...\nError Details : ");
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
								   
			case PROFILECLASS : //System.out.println("input : profile class");
							    dos.writeUTF("connected");
							    getChoice();
							    //System.out.println("profile class "+socketObject.getRemoteSocketAddress());
								break;
							
			case CREATEPROFILE : //System.out.println("input : profile method");
							     dos.writeUTF("connected");
								 createProfile();
								 //System.out.println("Create profile by "+socketObject.getRemoteSocketAddress());
								 break;							

			case GETPROFILE : 	 //System.out.println("input : profile method");
							     dos.writeUTF("connected");
							     String userid = dis.readUTF();
								 getProfile(userid);
								 //System.out.println("Get profile by "+socketObject.getRemoteSocketAddress());
								 break;

			case CHANGENAME : 	 //System.out.println("input : profile change name method");
							     dos.writeUTF("connected");
							     userid = dis.readUTF();
								 changeName(userid);
								 //System.out.println("Get profile by "+socketObject.getRemoteSocketAddress());
								 break;								 												 

			case CHANGESTATUS :  //System.out.println("input : profile change name method");
							     dos.writeUTF("connected");
							     userid = dis.readUTF();
								 changeStatus(userid);
								 //System.out.println("Get profile by "+socketObject.getRemoteSocketAddress());
								 break;								 												 								 
								
			case SIGNINCLASS :  //System.out.println("input : Signin class");
							    dos.writeUTF("connected");
							    //System.out.println("signin by "+socketObject.getRemoteSocketAddress());
								break;
								   
			case SIGNUPCLASS : //System.out.println("input : Signin class");
							   dos.writeUTF("connected");
							   SignUp su = new SignUp();
							   su.main(serverSocket, socketObject);
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

			case UPDATETIME : 
								 dos.writeUTF("connected");
								// //System.out.println("update time called by "+socketObject.getRemoteSocketAddress());
								 username = dis.readUTF();
								// //System.out.println("input :"+username);
								 setTime(username);
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
	    	//System.out.println("Error occured\nError details : ");
			e.printStackTrace();
	   }   
    }
	
	
	private boolean checkUsername(String username)
	{
		String existing_user;
		boolean user_exist = false;
		try
		{
			lnr = new LineNumberReader(new FileReader("com\\sanjeeviraj\\messenger\\server\\users\\"+"\\"+username+"\\Profile.config"));
			
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
	
	private void createProfile()
	{
		String user_details;
		String username;
		
		String folder_path;
		String file_path;
		File profile_dir;
		File profile_file;
		FileWriter fwr = null;
		boolean file_closed = false;
		try
		{
			user_details = dis.readUTF();
			username = user_details.substring(1, user_details.indexOf('\n', 1));
			
			//System.out.println("createProfile(user_details) : "+user_details);
			//System.out.println("username : "+username);
			
			folder_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+"\\"+username+"\\contacts";
			file_path =  "com\\sanjeeviraj\\messenger\\server\\users\\"+"\\"+username+"\\Profile.config";
			
			profile_dir = new File(folder_path);
			profile_dir.mkdirs();
			
			folder_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+"\\"+username+"\\groups";
			profile_dir = new File(folder_path);
			profile_dir.mkdir();
			
			//user_details = dis.readUTF();
			profile_file = new File(file_path);
			
			fwr = new FileWriter(profile_file, true);
			fwr.write(user_details+"\n"+getTime());
			//System.out.println("file edited");
			fwr.close();
			file_closed = true;
			
			file_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+username+"\\Contacts.msgr";
			
			fwr = new FileWriter(new File(file_path), true);
			fwr.write("");
			//System.out.println("file edited");
			fwr.close();

			file_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+username+"\\Groups.msgr";
			
			fwr = new FileWriter(new File(file_path), true);
			fwr.write("");
			//System.out.println("file edited");
			fwr.close();

			file_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+username+"\\Unread.msgr";
			
			fwr = new FileWriter(new File(file_path), true);
			fwr.write("");
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

	private void getProfile(String userid)
	{
		String name, status, time, user_details;
		String file_path;
		LineNumberReader lnr;
		try
		{
			file_path =  "com\\sanjeeviraj\\messenger\\server\\users\\"+"\\"+userid+"\\Profile.config";
			lnr = new LineNumberReader(new FileReader(file_path));
			lnr.readLine();
			name = lnr.readLine();
			status = lnr.readLine();
			time = lnr.readLine();
			user_details = name+"\n"+status;
			//System.out.println("getProfile\nname: "+name+"\nstatus: "+status+"\ntime: "+time);
			//System.out.println("getProfile\nname: "+user_details);
			dos.writeUTF(user_details);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void changeName(String userid)
	{
		String name, status, time, user_details;
		String file_path;
		LineNumberReader lnr;

		String new_name;
		
		File profile_file;
		FileWriter fwr = null;
		boolean file_closed = false;
		try
		{
			dos.writeUTF("connected");
			new_name = dis.readUTF();
			file_path =  "com\\sanjeeviraj\\messenger\\server\\users\\"+"\\"+userid+"\\Profile.config";
			lnr = new LineNumberReader(new FileReader(file_path));
			lnr.readLine();
			name = lnr.readLine();
			status = lnr.readLine();
			lnr.close();
			user_details = "\n"+new_name+"\n"+status;
			
			//System.out.println("getProfile\nname: "+user_details);
			
			profile_file = new File(file_path);
			
			fwr = new FileWriter(profile_file);
			user_details = user_details + "\n" +getTime();
			fwr.write(user_details);
			//System.out.println("file edited");
			fwr.close();
			file_closed = true;
			dos.writeUTF("changed");			  			
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

	private void changeStatus(String userid)
	{
		String name, status, time, user_details;
		String file_path;
		LineNumberReader lnr;

		String new_status;
		
		File profile_file;
		FileWriter fwr = null;
		boolean file_closed = false;
		try
		{
			dos.writeUTF("connected");
			new_status = dis.readUTF();
			file_path =  "com\\sanjeeviraj\\messenger\\server\\users\\"+"\\"+userid+"\\Profile.config";
			lnr = new LineNumberReader(new FileReader(file_path));
			lnr.readLine();
			name = lnr.readLine();
			status = lnr.readLine();
			lnr.close();
			user_details = "\n"+name+"\n"+new_status;
			
			//System.out.println("getProfile\nname: "+user_details);
			
			profile_file = new File(file_path);
			
			fwr = new FileWriter(profile_file);
			user_details = user_details + "\n" +getTime();
			fwr.write(user_details);
			//System.out.println("file edited");
			fwr.close();
			file_closed = true;
			dos.writeUTF("changed");			  			
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

	private void setTime(String userid)
	{
		String name, status, time, user_details;
		String file_path;
		LineNumberReader lnr;

		String new_status;
		
		File profile_file;
		FileWriter fwr = null;
		boolean file_closed = false;
		try
		{
			//dos.writeUTF("connected");
			file_path =  "com\\sanjeeviraj\\messenger\\server\\users\\"+"\\"+userid+"\\Profile.config";
			lnr = new LineNumberReader(new FileReader(file_path));
			lnr.readLine();
			name = lnr.readLine();
			status = lnr.readLine();
			lnr.close();
			user_details = "\n"+name+"\n"+status;
			
			//System.out.println("getProfile\nname: "+user_details);
			
			profile_file = new File(file_path);
			
			fwr = new FileWriter(profile_file);
			user_details = user_details + "\n" +getTime();
			fwr.write(user_details);
			//System.out.println("file edited");
			fwr.close();
			file_closed = true;
			dos.writeUTF("changed");			  			
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
	
	private String getTime()
	{
		date_obj = rightNow.getTime();
		return df_obj.format(date_obj);
	}
}