package com.sanjeeviraj.messenger.server;

import java.net.*; 
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

public class Groups implements Choice
{
	public Groups() {	}
	
	public Groups(ServerSocket serverSocketArg, Socket socketObjectArg)
	{
		//System.out.println("Signup constructor");
		serverSocket = serverSocketArg;
		socketObject = socketObjectArg;
	}
	
	private static String PATH = "com\\sanjeeviraj\\messenger\\server\\Accounts.config";
	public static final String CHECKUSERNAME = "checkUsername";
	
	public static final String ADDCONTACT = "addContact";
	public static final String REMOVECONTACT = "removeContact";
	public static final String GETCONTACTID = "getContactID";
	public static final String GETCONTACTNAME = "getContactName";
	public static final String GETCONTACTSTATUS = "getContactStatus";
	public static final String GETCONTACTLASTSEEN = "getContactLastSeen";
	public static final String GETNOOFCONTACTS = "getNoOfContacts";
	public static final String CHECKCONTACT = "checkContact";


	public static final String GETNOOFGROUPS = "getNoOfGroups";
	public static final String GETGROUPID = "getGroupID";
	public static final String SETREAD = "setRead";
	public static final String GETGROUPNAME = "getGroupName";
	public static final String GETNOOFGROUPMEMBERS = "getNoOfGroupMembers";
	public static final String GETGROUPTOTALNOOFMESSAGES = "getGroupTotalNoOfMessages";
	public static final String GETNOOFUNREADMESSAGESGROUP = "getNoOfUnreadMessagesGroup";
	public static final String CHECKGROUP = "checkGroup";
	public static final String CHECKGROUPMEMBER = "checkGroupMember";
	public static final String CREATEGROUP = "createGroup";
	public static final String DELETEGROUP = "deleteGroup";


	public static final String CREATEPROFILE = "createProfile";
	public static final String GETPROFILE = "getProfile";
	public static final String CHANGENAME = "changeName";
	public static final String CHANGESTATUS = "changeStatus";	
	public static final String SIGNUP = "signUp";
	public static final int PORT_NUMBER = 6666;   
	private static boolean CLOSE_CONN = false;
    private String user_details;	 

    String username, contact;
	Socket socketObject;
	OutputStream os;
	DataOutputStream dos;
	InputStream is;
	DataInputStream dis;
	Profile pf;
	Contacts co;
	Groups gr;
	ServerSocket serverSocket;
	LineNumberReader lnr;
	Calendar rightNow = Calendar.getInstance();
	Date date_obj;
	DateFormat df_obj = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
	
	public void main(ServerSocket serverSocketArg, Socket socketObjectArg)
	{
		try
		{
			//System.out.println("Signup main(sa,soa)");
			gr = new Groups(serverSocketArg, socketObjectArg);
			//System.out.println("Signup object created");
			gr.startConnection();
			//System.out.println("after connecting to the client");
			gr.getChoice();					
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
					//System.out.println("Disconnecting from client");
					socketObject.close();
				}
			}
			catch(Exception e)
			{
				//System.out.println("Error while closing the connection \nError details:\n");
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

			case CONTACTS :    //System.out.println("input : Signin class");
							   dos.writeUTF("connected");
							   getChoice();
							   //System.out.println("signin by "+socketObject.getRemoteSocketAddress());
							   break;

			case ADDCONTACT :  //System.out.println("input : add contact class");
							   dos.writeUTF("connected");
							   //System.out.println("username checked by "+socketObject.getRemoteSocketAddress());
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 contact = dis.readUTF();
								 //System.out.println("input :"+username);
								 if(addContact(contact))
									  dos.writeUTF("true");
								 else
									  dos.writeUTF("false");
							   //System.out.println("add contact by "+socketObject.getRemoteSocketAddress());
							   break;

			case REMOVECONTACT :  //System.out.println("input : remove contact class");
							   		dos.writeUTF("connected");
							  		 //System.out.println("username checked by "+socketObject.getRemoteSocketAddress());
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 contact = dis.readUTF();
								 //System.out.println("input :"+username);
								 if(removeContact(contact))
									  dos.writeUTF("true");
								 else
									  dos.writeUTF("false");
							   //System.out.println("add contact by "+socketObject.getRemoteSocketAddress());
							   break;							   							   
								   
			case CHECKCONTACT : //System.out.println("input : check username ");
								 dos.writeUTF("connected");
								 //System.out.println("username checked by "+socketObject.getRemoteSocketAddress());
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 contact = dis.readUTF();
								 //System.out.println("input :"+username);
								 if(checkContact(contact))
									  dos.writeUTF("true");
								 else
									  dos.writeUTF("false");
								 //System.out.println("username checked by "+socketObject.getRemoteSocketAddress());
								 break;

			case GETNOOFGROUPS : //System.out.println("input : check username ");
								 dos.writeUTF("connected");
								 //System.out.println("username checked by "+socketObject.getRemoteSocketAddress());
								 username = dis.readUTF();
								 //dos.writeUTF("connected");
								 dos.writeUTF(""+getNoOfGroups(username));
								 //System.out.println("username checked by "+socketObject.getRemoteSocketAddress());
								 break;								 

			case GETCONTACTID :  dos.writeUTF("connected");
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 int position = Integer.parseInt(dis.readUTF());
								 //System.out.println("input :"+username);
								 dos.writeUTF(""+getContactID(position));
								 break;

			case GETGROUPID :  	 dos.writeUTF("connected");
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 position = Integer.parseInt(dis.readUTF());
								 //System.out.println("input :"+username);
								 dos.writeUTF(""+getGroupID(username, position));
								 break;								 								 

			case GETGROUPNAME :	 dos.writeUTF("connected");
								 String id = dis.readUTF();
								 dos.writeUTF(getGroupName(id));
								 break;								 

			case GETNOOFGROUPMEMBERS :	 dos.writeUTF("connected");
								 id = dis.readUTF();
								 dos.writeUTF(""+getNoOfGroupMembers(id));
								 break;								 								 

			case GETGROUPTOTALNOOFMESSAGES :	dos.writeUTF("connected");
								 userid = dis.readUTF();
								 dos.writeUTF("connected");
								 String groupid = dis.readUTF();
								 dos.writeUTF(""+getGroupTotalNoOfMessages(groupid, userid));
								 break;								 								 


			case GETCONTACTSTATUS :dos.writeUTF("connected");
								 id = dis.readUTF();
								 //System.out.println("\n***********id: "+id+" :*********  : "+getContactStatus(id)+"\n");
								 dos.writeUTF(getContactStatus(id));
								 break;								 
								 
			case GETCONTACTLASTSEEN : dos.writeUTF("connected");
								 id = dis.readUTF();
								 //System.out.println("\n***********id: "+id+" :*********  : "+getContactLastSeen(id)+"\n");
								 dos.writeUTF(getContactLastSeen(id));
								 break;		

			case CHECKGROUP : 	 dos.writeUTF("connected");
								 id = dis.readUTF();
								 //System.out.println("\n***********id: "+id+" :*********  : "+getContactLastSeen(id)+"\n");
								 dos.writeUTF(""+checkGroup(id));
								 break;		

			case CHECKGROUPMEMBER : 	 dos.writeUTF("connected");
								 id = dis.readUTF();

								 dos.writeUTF("connected");
								 username = dis.readUTF();
								 //System.out.println("\n***********id: "+id+" :*********  : "+getContactLastSeen(id)+"\n");
								 dos.writeUTF(""+checkGroupMember(id, username));
								 break;		

			case CREATEGROUP :	dos.writeUTF("connected");
								 String group_id = dis.readUTF();
								 dos.writeUTF("connected");
								 String group_name = dis.readUTF();
								 dos.writeUTF("connected");
								 String user_id = dis.readUTF();
								 dos.writeUTF(""+createGroup(group_id, group_name, user_id));
								 break;								 								 
								 
			case EXIT : 		
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
	
	private String getGroupName(String group_id)
	{
		String group_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Profile.config";
		int n = 0;
		String response = "";
		String data = "";
		try
		{
			lnr = new LineNumberReader(new FileReader(group_path));
			response = lnr.readLine();		
			lnr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}	

	private boolean checkGroup(String group_id)
	{
		String group_path = "com\\sanjeeviraj\\messenger\\server\\groups\\Groups.config";
		boolean response = false;
		String data = "";
		try
		{
			lnr = new LineNumberReader(new FileReader(group_path));
			while((data = lnr.readLine())!=null)
			{
				if(data.equals(group_id))
				{	
					response = true;
					break;
				}
			}		
			lnr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}		

	private boolean checkGroupMember(String group_id, String user_id)
	{
		String group_members_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Members.config";
		boolean response = false;
		String data = "";
		try
		{
			lnr = new LineNumberReader(new FileReader(group_members_path));
			while(((data = lnr.readLine())!=null) && (!response))
			{
				if(data.equals("/name"))
				{	
					if(lnr.readLine().equals(user_id))
					{
						response = true;
						break;
					}
				}
			}		
			lnr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}

	private int getNoOfGroupMembers(String group_id)
	{
		String group_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Members.config";
		int n = 0;
		String data = "";
		try
		{
			lnr = new LineNumberReader(new FileReader(group_path));

			while((data = lnr.readLine()) != null)
			{
					if(data.equals("/type"))
					{
						lnr.readLine();
						lnr.readLine();
						lnr.readLine();
						n++;
					}
			}
			lnr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return n;
	}

	private boolean createGroup(String group_id, String group_name, String user_id)
	{
		String group_file = "com\\sanjeeviraj\\messenger\\server\\groups\\Groups.config";
		String new_group_folder = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id;
		String new_group_chats_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Chats.msgr";
		String new_group_members_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Members.config";
		String new_group_profile_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Profile.config";

		String user_group_file = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\Groups.msgr";
		String user_group_folder = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\groups\\"+group_id;
		String user_group_chats_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\groups\\"+group_id+"\\Chats.msgr";

		FileWriter fwr = null;
		boolean file_closed = false;
		boolean response = false;
		try
		{
			//user_details = dis.readUTF();
			//username = user_details.substring(1, user_details.indexOf('\n', 1));
						
			new File(new_group_folder).mkdirs();
			new File(user_group_folder).mkdirs();
			
			//System.out.println("\nfolder created...");

			fwr = new FileWriter(new File(new_group_chats_path), true);
			fwr.write("");
			fwr.close();

			System.out.println("\n1 file created...");
			fwr = new FileWriter(new File(user_group_chats_path), true);
			fwr.write("");
			fwr.close();

			//System.out.println("\n2 file created...");
			fwr = new FileWriter(new File(group_file), true);
			fwr.write(group_id+"\n");
			fwr.close();

			//System.out.println("\n3 file created...");
			fwr = new FileWriter(new File(new_group_members_path), true);
			String member_details = "\n/type\nadmin\n/name\n"+user_id;
			fwr.write(member_details);
			fwr.close();

			//System.out.println("\n4 file created...");
			fwr = new FileWriter(new File(new_group_profile_path), true);
			fwr.write(group_name+"\n");
			fwr.close();

			//System.out.println("\n5 file created...");
			fwr = new FileWriter(new File(user_group_file), true);
			fwr.write(group_id+"\n");
			fwr.close();
			file_closed = true;
			
			if(checkGroup(group_id))
			{
				System.out.println("username found: file modified");
				response = true;
			}
			else
			{
				System.out.println("username not found: file not modified");
				response = false;
			}			  			
		}
		catch(Exception e)
		{
			System.out.println("\nError occured: ");
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
				System.out.println("\nError occured while closing the file");
				ioe.printStackTrace();
			}
		}
		return response;
	}	

	private int getGroupTotalNoOfMessages(String group_id, String user_id)
	{
		String group_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\groups\\"+group_id+"\\Chats.msgr";
		int n = 0;
		String data = "";
		try
		{
			lnr = new LineNumberReader(new FileReader(group_path));

			while((data = lnr.readLine()) != null)
			{
					if(data.equals("/time"))
					{
						n++;
					}
			}
			lnr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return n;
	}	

	
	private boolean checkContact(String contact_name)
	{
		String existing_user;
		boolean user_exist = false;
		try
		{
			lnr = new LineNumberReader(new FileReader("com\\sanjeeviraj\\messenger\\server\\users\\"+username+"\\Contacts.msgr"));
			
			//System.out.println("usernames\n");
			while(true)
		 	{
				//System.out.println("while true");
					//System.out.println("if 1");
					if((existing_user = lnr.readLine()) != null) 	
					{
						//System.out.println("if 2");
						if(existing_user.equals(contact_name))
						{
							//System.out.println("if 3");
							//System.out.println("checking "+contact_name+" with "+existing_user);
							user_exist = true;
							break;
						}
					}
					else
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
	
	private boolean addContact(String contact_name)
	{
		String user_details;
		//String username;
		
		String folder_path;
		String file_path;
		String chats_path;

		File contact_dir;
		File contact_file;
		File chats_file;
		FileWriter fwr = null;
		boolean file_closed = false;
		boolean response = false;
		try
		{
			//user_details = dis.readUTF();
			//username = user_details.substring(1, user_details.indexOf('\n', 1));
			
			//System.out.println("username : "+username);
			
			folder_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+username+"\\contacts\\"+contact_name;
			chats_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+username+"\\contacts\\"+contact_name+"\\Chats.msgr";
			file_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+username+"\\Contacts.msgr";			
			contact_dir = new File(folder_path);
			contact_dir.mkdirs();
			
			//user_details = dis.readUTF();
			contact_file = new File(file_path);
			chats_file = new File(chats_path);
			
			fwr = new FileWriter(contact_file, true);
			fwr.write("\n"+contact_name);
			//System.out.println("file edited");
			fwr.close();
			file_closed = true;

			fwr = new FileWriter(chats_file, true);
			fwr.write("");
			//System.out.println("file edited");
			fwr.close();
			file_closed = true;			
			
			//System.out.println("check user : "+username);
			if(checkContact(contact_name))
			{
				//System.out.println("username found: file modified");
				response = true;
			}
			else
			{
				//System.out.println("username not found: file not modified");
				response = false;
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
		return response;
	}


	private boolean removeContact(String contact_name)
	{
		String user_details;
		//String username;
		LineNumberReader lnr;
		String folder_path;
		String chats_path;
		String file_path;
		String data, data1;
		data1 = data = "";
		File contact_dir;
		File contact_file;
		File chats_file;
		FileWriter fwr = null;
		boolean file_closed = false;
		boolean response = false;
		try
		{
			//user_details = dis.readUTF();
			//username = user_details.substring(1, user_details.indexOf('\n', 1));
			
			
			//System.out.println("username : "+username);
			
			folder_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+username+"\\contacts\\"+contact_name;
			chats_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+username+"\\contacts\\"+contact_name+"\\Chats.msgr";
			file_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+username+"\\Contacts.msgr";			
			
			lnr = new LineNumberReader(new FileReader(file_path));

			while((data = lnr.readLine()) != null)
				if(!data.equals(contact_name))
					data1 = data1 + data + "\n";
				//System.out.println("Data : "+data);

			lnr.close();
			data1 = data1.trim();

			chats_file = new File(chats_path);
			chats_file.delete();

			contact_dir = new File(folder_path);
			contact_dir.delete();
			
			//user_details = dis.readUTF();
			contact_file = new File(file_path);
			
			fwr = new FileWriter(contact_file);
			fwr.write(data1);
			//System.out.println("file edited");
			fwr.close();
			file_closed = true;
			
			//System.out.println("check user : "+username);
			if(checkContact(contact_name))
			{
				//System.out.println("username found: file modified");
				response = false;
			}
			else
			{
				//System.out.println("username not found: file not modified");
				response = true;
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
		return response;
	}

	private int getNoOfGroups(String user_id)
	{
		LineNumberReader lnr;
		String folder_path;
		String chats_path;
		String file_path;
		String data, data1;
		data1 = data = "";
		File contact_dir;
		File contact_file;
		File chats_file;
		FileWriter fwr = null;
		boolean file_closed = false;
		boolean response = false;
		int no_of_groups = 0;
		try
		{
			//user_details = dis.readUTF();
			//username = user_details.substring(1, user_details.indexOf('\n', 1));
			
			
			//System.out.println("username : "+user_id);
			
			file_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\Groups.msgr";			
			File groups_file = new File(file_path);

			lnr = new LineNumberReader(new FileReader(groups_file));

			while((data = lnr.readLine()) != null)
				if(!data.isEmpty() || !data.equals(""))
					no_of_groups++;
				//System.out.println("Data : "+data);

			lnr.close();
						  			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return no_of_groups;
	}

	private String getGroupID(String user_id, int position)
	{
		LineNumberReader lnr;
		String folder_path;
		String chats_path;
		String file_path;
		String data, data1;
		data1 = data = "";
		File contact_dir;
		File contact_file;
		File chats_file;
		FileWriter fwr = null;
		boolean file_closed = false;
		String response = "";
		int no_of_groups = 0;
		try
		{
						
			file_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\Groups.msgr";			
			File groups_file = new File(file_path);

			lnr = new LineNumberReader(new FileReader(groups_file));

			while((data = lnr.readLine()) != null)
			{
				if(!data.isEmpty() || !data.equals(""))
					no_of_groups++;

				if(no_of_groups == position)
				{
					response = data;
					break;
				}
			}	//System.out.println("Data : "+data);

			lnr.close();
						  			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}	

	private String getContactID(int position)
	{
		LineNumberReader lnr;
		String folder_path;
		String chats_path;
		String file_path;
		String data, data1;
		data1 = data = "";
		File contact_dir;
		File contact_file;
		File chats_file;
		FileWriter fwr = null;
		boolean file_closed = false;
		boolean response = false;
		int no_of_contacts = 0;
		int i = 1;
		try
		{
			
			file_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+username+"\\Contacts.msgr";			
			
			lnr = new LineNumberReader(new FileReader(file_path));

			while((data = lnr.readLine()) != null)
			{
				if(!data.isEmpty())
				{
					if(i == position)
						break;
					i++;	
				}	
				
			}
				no_of_contacts++;
				//System.out.println("Data : "+data);

			lnr.close();
						  			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return data;
	}	

	private void getProfile(String userid)
	{
		String name, status, time, user_details;
		String file_path;
		LineNumberReader lnr;
		try
		{
			file_path =  "com\\sanjeeviraj\\messenger\\server\\users\\"+userid+"\\Profile.config";
			lnr = new LineNumberReader(new FileReader(file_path));
			lnr.readLine();
			name = lnr.readLine();
			status = lnr.readLine();
			time = lnr.readLine();
			user_details = name+"\n"+status+"\n"+time;
			////System.out.println("getProfile\nname: "+name+"\nstatus: "+status+"\ntime: "+time);
			//System.out.println("getProfile\nname: "+user_details);
			dos.writeUTF(user_details);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private String getContactName(String userid)
	{
		String name = "";
		String file_path;
		LineNumberReader lnr;
		try
		{
			file_path =  "com\\sanjeeviraj\\messenger\\server\\users\\"+userid+"\\Profile.config";
			lnr = new LineNumberReader(new FileReader(file_path));
			lnr.readLine();
			name = lnr.readLine();
			//System.out.println("getProfile\nname: "+name+", id : "+userid);
			lnr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return name;
	}

	private String getContactStatus(String userid)
	{
		String status = "";
		String file_path;
		LineNumberReader lnr;
		try
		{
			file_path =  "com\\sanjeeviraj\\messenger\\server\\users\\"+userid+"\\Profile.config";
			lnr = new LineNumberReader(new FileReader(file_path));
			lnr.readLine();
			lnr.readLine();
			status = lnr.readLine();
			////System.out.println("getProfile\nname: "+name+"\nstatus: "+status+"\ntime: "+time);
			//System.out.println("getProfile\nstatus, id: "+status+" : "+userid);
			lnr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return status;
	}


	private String getContactLastSeen(String userid)
	{
		String time = "";
		String file_path;
		LineNumberReader lnr;
		try
		{
			file_path =  "com\\sanjeeviraj\\messenger\\server\\users\\"+userid+"\\Profile.config";
			lnr = new LineNumberReader(new FileReader(file_path));
			lnr.readLine();
			lnr.readLine();
			lnr.readLine();
			time = lnr.readLine();
			////System.out.println("getProfile\nname: "+name+"\nstatus: "+status+"\ntime: "+time);
			//System.out.println("getProfile\ntime: "+time+ "  id : "+userid);
			lnr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return time;
	}

	private void checkUsername(String userid)
	{
		String name, status, time, user_details;
		String file_path;
		LineNumberReader lnr;
		try
		{
			file_path =  "com\\sanjeeviraj\\messenger\\server\\users\\"+userid+"\\Profile.config";
			lnr = new LineNumberReader(new FileReader(file_path));
			while(true)
			{
				lnr.readLine();
				name = lnr.readLine();
				status = lnr.readLine();
				time = lnr.readLine();
				user_details = name+"\n"+status;
				////System.out.println("getProfile\nname: "+name+"\nstatus: "+status+"\ntime: "+time);
				//System.out.println("getProfile\nname: "+user_details);
				dos.writeUTF(user_details);
			}
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
			file_path =  "com\\sanjeeviraj\\messenger\\server\\users\\"+userid+"\\Profile.config";
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
			file_path =  "com\\sanjeeviraj\\messenger\\server\\users\\"+userid+"\\Profile.config";
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
	
	private String getTime()
	{
		date_obj = rightNow.getTime();
		return df_obj.format(date_obj);
	}
}