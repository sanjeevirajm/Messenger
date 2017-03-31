package com.sanjeeviraj.messenger.server;

import java.net.*; 
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

public class ChatGroup implements Choice
{
	public ChatGroup() {	}
	
	public ChatGroup(ServerSocket serverSocketArg, Socket socketObjectArg)
	{
		System.out.println("Signup constructor");
		serverSocket = serverSocketArg;
		socketObject = socketObjectArg;
	}
	
	private static String PATH = "com\\sanjeeviraj\\messenger\\server\\Accounts.config";
	public static final String CHECKUSERNAME = "checkUsername";
	

	public static final String SENDMESSAGE = "sendMessage";
	public static final String GETMEMBER = "getMember";
	public static final String SETREAD = "setRead";

	public static final String ADDMEMBER = "addMember";
	public static final String REMOVEMEMBER = "removeMember";
	public static final String ADDADMINPERMISSION = "addAdminPermission";
	public static final String REMOVEADMINPERMISSION = "removeAdminPermission";
	public static final String GETMEMBERID = "getMemberID";
	public static final String GETNOOFMESSAGES = "getNoOfMessages";
	public static final String GETMESSAGETIME = "getMessageTime";
	public static final String GETMESSAGESENDER = "getMessageSender";
	public static final String GETMESSAGE = "getMessage";
	public static final String CHECKADMIN = "checkAdmin";
	public static final String CHECKMEMBER = "checkMember";
	public static final String GETNOOFMEMBERS = "getNoOfMembers";
	public static final String DELETEGROUP = "deleteGroup";

	public static final String ADDCONTACT = "addContact";
	public static final String REMOVECONTACT = "removeContact";
	public static final String GETCONTACTID = "getContactID";
	public static final String GETCONTACTNAME = "getContactName";
	public static final String GETCONTACTSTATUS = "getContactStatus";
	public static final String GETCONTACTLASTSEEN = "getContactLastSeen";
	public static final String GETNOOFCONTACTS = "getNoOfContacts";
	public static final String CHECKCONTACT = "checkContact";

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
	ChatGroup cg;
	ServerSocket serverSocket;
	LineNumberReader lnr;
	Calendar rightNow = Calendar.getInstance();
	Date date_obj;
	DateFormat df_obj = DateFormat.getDateTimeInstance(DateFormat.MEDIUM,DateFormat.MEDIUM);
	
	public void main(ServerSocket serverSocketArg, Socket socketObjectArg)
	{
		try
		{
			System.out.println("Signup main(sa,soa)");
			cg = new ChatGroup(serverSocketArg, socketObjectArg);
			System.out.println("Signup object created");
			cg.startConnection();
			System.out.println("after connecting to the client");
			cg.getChoice();					
			System.out.println("last in main(sa,soa)");
		}
		catch(Exception e)
		{
			System.out.println("Error occured\nError Details : ");
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
		System.out.println("starting...");    
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
		System.out.println("waiting for input... ");   
		String input = dis.readUTF();
		switch(input)
		{
			case CHECKCONNECTION : System.out.println("input check connection");		
								   dos.writeUTF("connected");
								   System.out.println("connection checked by "+socketObject.getRemoteSocketAddress());
								   break;
								   
			case SENDMESSAGE :	 dos.writeUTF("connected");
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 String group_id = dis.readUTF();
								 dos.writeUTF("connected");
								 String message = dis.readUTF();
								 dos.writeUTF(""+sendMessage(username, group_id, message));
								 break;									 							 

			case GETNOOFMESSAGES :	 dos.writeUTF("connected");
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF(""+getNoOfMessages(username, group_id));
								 break;									 							 

	
			case SETREAD :	 	 dos.writeUTF("connected");
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF(""+setRead(username, group_id));
								 break;									 							 								 								 								

			case GETMEMBER :	 dos.writeUTF("connected");
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF("connected");
								 int position = Integer.parseInt(dis.readUTF());
								 dos.writeUTF(""+getMember(username, group_id, position));
								 break;			

			case ADDMEMBER :	 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF("connected");
								 String member_id = dis.readUTF();
								 dos.writeUTF(""+addMember(member_id, group_id));
								 break;

			case REMOVEMEMBER :	 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF("connected");
								 member_id = dis.readUTF();
								 dos.writeUTF(""+removeMember(member_id, group_id));
								 break;


			case ADDADMINPERMISSION :	 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF("connected");
								 member_id = dis.readUTF();
								 dos.writeUTF(""+addAdminPermission(member_id, group_id));
								 break;			

			case REMOVEADMINPERMISSION :	 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF("connected");
								 member_id = dis.readUTF();
								 dos.writeUTF(""+removeAdminPermission(member_id, group_id));
								 break;			

			case GETMEMBERID :	 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF("connected");
								 position = Integer.parseInt(dis.readUTF());
								 dos.writeUTF(""+getMember("", group_id, position));
								 break;

			case GETMESSAGETIME :	 dos.writeUTF("connected");
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF("connected");
								position = Integer.parseInt(dis.readUTF());
								 dos.writeUTF(""+getMessageTime(username, group_id, position));
								 break;

			case GETMESSAGESENDER :	 dos.writeUTF("connected");
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF("connected");
								 position = Integer.parseInt(dis.readUTF());
								 dos.writeUTF(""+getMessageSender(username, group_id, position));
								 break;

			case GETMESSAGE :	 dos.writeUTF("connected");
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF("connected");
								 position = Integer.parseInt(dis.readUTF());
								 dos.writeUTF(""+getMessage(username, group_id, position));
								 break;								 

			case CHECKADMIN :	 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF("connected");
								 member_id = dis.readUTF();
								 dos.writeUTF(""+checkAdmin( member_id, group_id));
								 break;

			case CHECKMEMBER :	 dos.writeUTF("connected");
								 member_id = dis.readUTF();
								 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF(""+checkMember( member_id, group_id));
								 break;								 								 								 

			case GETNOOFMEMBERS :	 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF(""+getNoOfMembers("", group_id));
								 break;

			case DELETEGROUP :	 dos.writeUTF("connected");
								 group_id = dis.readUTF();
								 dos.writeUTF("connected");
								 member_id = dis.readUTF();
								 removeMember(member_id, group_id);
								 dos.writeUTF(""+deleteGroup(member_id, group_id));
								 break;								 

			case EXIT : 		
								CLOSE_CONN = true;
								break;						
								   
			default : System.out.println("default in getchoice");
						break;	   
		}
	   }
	   catch(Exception e)
	   {
	    	System.out.println("Error occured\nError details : ");
			e.printStackTrace();
	   }   
    }

    private boolean sendMessage(String user_id, String group_id, String message)
	{	
		String temp;
		String user_chat;
		String contact_chat;
		String contact_unread;
		String message_details;

		String temp_data = "";
		String data = "";
		String cur_line = "";
		String user_data = "";
		String old_data = "";
		String data1 = "";

		String group_file = "com\\sanjeeviraj\\messenger\\server\\groups\\Groups.config";
		String group_folder = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id;
		String group_chats_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Chats.msgr";
		String group_members_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Members.config";
		String group_profile_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Profile.config";

		String user_group_file = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\Groups.msgr";
		String user_group_folder = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\groups\\"+group_id;
		String user_group_chats_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\groups\\"+group_id+"\\Chats.msgr";

		FileWriter fwr = null;
		File user_chat_file;
		File group_chat_file;
		File contact_chat_file;
		File contact_unread_file;
		
		int unread_no = 0;
		boolean file_closed = false;
		boolean response = false;
		boolean unread_exist = false;
		try
		{			
			message_details = "\n"+"/name"+"\n"+user_id+"\n"+"/time"+"\n"+getTime()+"\n"+"/message"+message+"\n";
				System.out.println("**sendmessage 1");
			
				System.out.println("**sendmessage 2");
				group_chat_file = new File(group_chats_path);
				//System.out.println("**sendmessage 3");
				//contact_chat_file = new File(contact_chat);
				System.out.println("**sendmessage 7");

				group_chat_file = new File(group_chats_path);
				lnr = new LineNumberReader(new FileReader(group_chat_file));

				while((data = lnr.readLine()) != null)
						data1 = data1 + data + "\n";
				
				
				System.out.println("Message : "+message_details);

				lnr.close();
				//data1 = data1.trim();
				data1 = message_details + data1;

				fwr = new FileWriter(group_chat_file);
				fwr.write(data1);
				System.out.println("file edited");
				fwr.close();

			for(int i = 1; i <= getNoOfMembers(user_id, group_id); i++)
			{
				System.out.println("\n***********************\nsendmessage loop: "+i+"\n**********************");
				String member_name = getMember(user_id, group_id, i);

				String member_chat = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_name+"\\groups\\"+group_id+"\\Chats.msgr";
				//contact_chat = "com\\sanjeeviraj\\messenger\\server\\users\\"+contact_id+"\\contacts\\"+user_id+"\\Chats.msgr";
				String member_unread = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_name+"\\Unread.msgr";

				System.out.println("**sendmessage 2");
				File member_chat_file = new File(member_chat);
				//System.out.println("**sendmessage 3");
				//contact_chat_file = new File(contact_chat);
				System.out.println("**sendmessage 4");
				File member_unread_file = new File(member_unread);
				System.out.println("**sendmessage 5");
				
				System.out.println("**sendmessage 7");

				lnr = new LineNumberReader(new FileReader(member_chat_file));

				data1 = "";
				while((data = lnr.readLine()) != null)
						data1 = data1 + data + "\n";
				
				System.out.println("Message : "+message_details);

				lnr.close();
				//data1 = data1.trim();
				data1 = message_details + data1;

				fwr = new FileWriter(member_chat_file);
				fwr.write(data1);
				System.out.println("file edited");
				fwr.close();



					data1 = "";
					lnr = new LineNumberReader(new FileReader(member_unread_file));

					while((cur_line = lnr.readLine()) != null)
					{
						if(cur_line.equals("/type") && !(unread_exist))
						{
							temp_data = cur_line;
							cur_line = lnr.readLine();
							if(cur_line.equals("group"))
							{
								temp_data += "\n" + cur_line;
								cur_line = lnr.readLine();
								if(cur_line.equals("/name"))
								{
									temp_data += "\n" + cur_line;
									cur_line = lnr.readLine();
									if(cur_line.equals(group_id))
									{
										temp_data += "\n" + cur_line + "\n" + lnr.readLine();
										unread_exist = true;
										unread_no = Integer.parseInt(lnr.readLine()) + 1;
										user_data = temp_data + "\n" + unread_no;
									}
									else
									{
										old_data += "\n" + temp_data + "\n" + cur_line;
									}
								}
								else
								{
									old_data += "\n" + temp_data + "\n" + cur_line;
								}		
							}
							else
							{
								old_data += "\n" + temp_data + "\n" + cur_line;
							}
						}
						else
						{
							old_data += "\n" + cur_line;
						}
					}
					lnr.close();

					if(!unread_exist)
					{
						unread_no++;
						data = "/type\ngroup\n/name\n"+group_id+"\n/unread\n"+unread_no;
						old_data = data + "\n" + old_data;
					}
					else
					{
						old_data = user_data + "\n" + old_data;
					}

					 unread_exist = false;
					fwr = new FileWriter(member_unread_file);
					fwr.write(old_data);
					System.out.println("file edited");
					fwr.close();
					response = true;
					file_closed = true;
				}
			}  
		catch(java.io.FileNotFoundException fno)
		{
			response = false;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(response)
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

	private boolean addMember(String member_id, String group_id)
	{
		String group_members_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Members.config";

		String group_chats_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Chats.msgr";
		String member_group_file = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\Groups.msgr";
		String member_group_folder = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\groups\\"+group_id;
		String member_group_chats_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\groups\\"+group_id+"\\Chats.msgr";

		String line = "";
		String data = "";

		FileWriter fwr = null;
		LineNumberReader lnr = null;
		File group_members_file;
		String member_details;
		boolean file_closed = false;
		boolean response = false;
		try
		{		
			new File(member_group_folder).mkdirs();

			data = "";
			lnr = new LineNumberReader(new FileReader(new File(group_chats_path)));
			while((line = lnr.readLine()) != null)
			{
				data += line + "\n";
			}
			lnr.close();

			fwr = new FileWriter(new File(member_group_chats_path), true);
			fwr.write(data);
			fwr.close();

			fwr = new FileWriter(new File(member_group_file), true);
			fwr.write(group_id+"\n");
			fwr.close();

			member_details = "\n/type\nnormal\n/name\n"+member_id;
			group_members_file = new File(group_members_path);
			fwr = new FileWriter(group_members_file, true);
			fwr.write(member_details);
			System.out.println("file edited");
			fwr.close();
			response = true;
		}		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}

	private boolean addAdminPermission(String member_id, String group_id)
	{
		String group_members_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Members.config";
		String line = "";
		String data = "";

		String group_chats_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Chats.msgr";
		String member_group_file = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\Groups.msgr";
		String member_group_folder = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\groups\\"+group_id;
		String member_group_chats_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\groups\\"+group_id+"\\Chats.msgr";

		FileWriter fwr = null;
		LineNumberReader lnr = null;
		File group_members_file;
		String member_details;
		String new_member_details;
		boolean file_closed = false;
		boolean response = false;
		try
		{		
			group_members_file = new File(group_members_path);

			lnr = new LineNumberReader(new FileReader(new File(group_members_path)));
			while((line = lnr.readLine()) != null) 
			{
				data += data + "\n";
			}
			lnr.close();

			member_details = "/type\nnormal\n/name\n"+member_id;
			new_member_details = "/type\nadmin\n/name\n"+member_id;

			data = data.replace(member_details, new_member_details);

			fwr = new FileWriter(group_members_file);
			fwr.write(data);
			System.out.println("file edited");
			fwr.close();
			response = true;
		}		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}

	private boolean removeAdminPermission(String member_id, String group_id)
	{
		String group_members_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Members.config";
		String line = "";
		String data = "";

		String group_chats_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Chats.msgr";
		String member_group_file = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\Groups.msgr";
		String member_group_folder = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\groups\\"+group_id;
		String member_group_chats_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\groups\\"+group_id+"\\Chats.msgr";

		FileWriter fwr = null;
		LineNumberReader lnr = null;
		File group_members_file;
		String member_details;
		String new_member_details;
		boolean file_closed = false;
		boolean response = false;
		try
		{		
			group_members_file = new File(group_members_path);

			lnr = new LineNumberReader(new FileReader(new File(group_members_path)));
			while((line = lnr.readLine()) != null) 
			{
				data += data + "\n";
			}
			lnr.close();

			member_details = "/type\nadmin\n/name\n"+member_id;
			new_member_details = "/type\nnormal\n/name\n"+member_id;

			data = data.replace(member_details, new_member_details);

			fwr = new FileWriter(group_members_file);
			fwr.write(data);
			System.out.println("file edited");
			fwr.close();
			response = true;
		}		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}

	private boolean checkAdmin(String member_id, String group_id)
	{
		String group_members_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Members.config";
		String line = "";
	
		LineNumberReader lnr = null;
		File group_members_file;

		boolean response = false;
		try
		{		
			lnr = new LineNumberReader(new FileReader(new File(group_members_path)));
			while(((line = lnr.readLine()) != null) && (!response)) 
			{System.out.println("\ncheck admin: "+line);
				if(line.equals("/type"))
				{System.out.println("\ncheck admin: "+line);
					line = lnr.readLine();
					if(line.equals("admin"))
					{System.out.println("\ncheck admin: "+line);
						lnr.readLine();
						line = lnr.readLine();
						if(line.equals(member_id))
						{System.out.println("\ncheck admin: "+line);
							response = true;
							break;
						}
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

	private boolean checkMember(String member_id, String group_id)
	{
		String group_members_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Members.config";
		String line = "";
	
		LineNumberReader lnr = null;
		File group_members_file;

		boolean response = false;
		try
		{		
			lnr = new LineNumberReader(new FileReader(new File(group_members_path)));
			while(((line = lnr.readLine()) != null) && (!response)) 
			{
				if(line.equals("/name"))
				{
					line = lnr.readLine();
					if(line.equals(member_id))
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

	private boolean removeMember(String member_id, String group_id)
	{
		String group_members_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Members.config";
		String line = "";
		String data = "";

		String group_chats_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Chats.msgr";
		String member_group_file = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\Groups.msgr";
		String member_group_folder = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\groups\\"+group_id;
		String member_group_chats_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\groups\\"+group_id+"\\Chats.msgr";

		FileWriter fwr = null;
		LineNumberReader lnr = null;
		File group_members_file;
		String member_details;
		boolean file_closed = false;
		boolean response = false;
		try
		{		
			group_members_file = new File(group_members_path);

			lnr = new LineNumberReader(new FileReader(new File(group_members_path)));
			while((line = lnr.readLine()) != null) 
			{
				data += line + "\n";
			}
			lnr.close();

			if(checkAdmin(member_id, group_id))
				member_details = "/type\nadmin\n/name\n"+member_id;
			else
				member_details = "/type\nnormal\n/name\n"+member_id;
			data = data.replace(member_details, "");

			fwr = new FileWriter(group_members_file);
			fwr.write(data);
			System.out.println("file edited");
			fwr.close();
			response = true;
		}		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}


	private boolean deleteGroup(String member_id, String group_id)
	{
		String line = "";
		String data = "";

		String member_group_file_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\Groups.msgr";
		String member_group_folder_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\groups\\"+group_id;
		String member_group_chats_path = "com\\sanjeeviraj\\messenger\\server\\users\\"+member_id+"\\groups\\"+group_id+"\\Chats.msgr";


		String group_config_path = "com\\sanjeeviraj\\messenger\\server\\groups\\Groups.config";
		String group_folder_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\";
		String group_chats_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Chats.msgr";
		String group_profile_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Profile.config";
		String group_members_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Members.config";

		FileWriter fwr = null;
		LineNumberReader lnr = null;

		File member_group_file;
		File member_group_folder;
		File member_group_chats_file;

		File group_config_file;
		File group_folder_file;
		File group_chats_file;
		File group_profile_file;
		File group_members_file;		
		
		String member_details;
		boolean file_closed = false;
		boolean response = false;
		try
		{		
			member_group_file = new File(member_group_file_path);

			lnr = new LineNumberReader(new FileReader(member_group_file));
			while((line = lnr.readLine()) != null) 
			{
				if(!line.equals(group_id))
					data += line + "\n";
			}
			lnr.close();

			fwr = new FileWriter(member_group_file);
			fwr.write(data);
			System.out.println("file edited");
			fwr.close();

			 member_group_folder = new File(member_group_folder_path);
			 member_group_chats_file = new File(member_group_chats_path);

			 member_group_chats_file.delete();
			 member_group_folder.delete();
			response = true;

			if(getNoOfMembers("", group_id) == 0)
			{
				group_config_file = new File(group_config_path);
				group_folder_file = new File(group_folder_path);
				group_chats_file = new File(group_chats_path);
				group_profile_file = new File(group_profile_path);
				group_members_file = new File(group_members_path);	

				data = "";
				lnr = new LineNumberReader(new FileReader(group_config_file));
				while((line = lnr.readLine()) != null) 
				{
					if(!line.equals(group_id))
						data += line + "\n";
				}
				lnr.close();

				fwr = new FileWriter(group_config_file);
				fwr.write(data);
				System.out.println("file edited");
				fwr.close();


				group_folder_file.delete();
				group_chats_file.delete();
				group_profile_file.delete();
				group_members_file.delete();
			}
		}		
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return response;
	}

	private String getMember(String user_id, String group_id, int position)
	{
		String response = "";
		int nmembers = 0;
		String group_members_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Members.config";
		String data = "";
		File members_file;
		try
		{			
			members_file = new File(group_members_path);
	
			lnr = new LineNumberReader(new FileReader(members_file));
			while((data = lnr.readLine()) != null)
			{
					if(data.equals("/type"))
						nmembers++;

					if(nmembers == position)
					{
						lnr.readLine();
						lnr.readLine();
						response = lnr.readLine();
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

	private int getNoOfMembers(String user_id, String group_id)
	{
		int nmembers = 0;
		String group_members_path = "com\\sanjeeviraj\\messenger\\server\\groups\\"+group_id+"\\Members.config";
		String data = "";
		File members_file;
		try
		{			
			members_file = new File(group_members_path);
	
			lnr = new LineNumberReader(new FileReader(members_file));
			while((data = lnr.readLine()) != null)
			{
				if(data.equals("/type"))
				{
					lnr.readLine();
					lnr.readLine();
					lnr.readLine();
					nmembers++;
				}
			}
			lnr.close();
		}	
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return nmembers;
	}	

	private int getNoOfMessages(String user_id, String group_id)
	{
		int nmessage = 0;
		String data = "";
		String user_chat;
		File user_chat_file;
		try
		{
			System.out.print("username : "+user_id);
			System.out.println("	group : "+group_id);
			user_chat = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\groups\\"+group_id+"\\Chats.msgr";
			user_chat_file = new File(user_chat);
			lnr = new LineNumberReader(new FileReader(user_chat_file));

			while((data = lnr.readLine()) != null)
					if(data.equals("/time"))
						nmessage++;
		
			lnr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return nmessage;
	}

	private String getMessageTime(String user_id, String group_id, int position)
	{		
		String time = "";
		int nmessage = 0;
		String user_chat;
		String data = "";
		File user_chat_file;
		try
		{
			System.out.print("username : "+user_id);
			System.out.println("	group : "+group_id);
			user_chat = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\groups\\"+group_id+"\\Chats.msgr";
			user_chat_file = new File(user_chat);

			lnr = new LineNumberReader(new FileReader(user_chat_file));
			while((data = lnr.readLine()) != null)
			{
					if(data.equals("/time"))
						nmessage++;
					if(nmessage == position)
					{
						time = lnr.readLine();
						break;
					}
			}
			lnr.close(); 			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return time;
	}

	private String getMessageSender(String user_id, String group_id, int position)
	{		
		String name = "";
		int nmessage = 0;
		String user_chat;
		String data = "";
		File user_chat_file;
		try
		{
			System.out.print("username : "+user_id);
			System.out.println("	group : "+group_id);
			user_chat = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\groups\\"+group_id+"\\Chats.msgr";
			user_chat_file = new File(user_chat);

			lnr = new LineNumberReader(new FileReader(user_chat_file));
			while((data = lnr.readLine()) != null)
			{
					if(data.equals("/name"))
						nmessage++;
					if(nmessage == position)
					{
						name = lnr.readLine();
						break;
					}
			}
			lnr.close(); 			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return name;
	}
	

	private String getMessage(String user_id, String group_id, int position)
	{
		String message = "";
		int nmessage = 0;
		String user_chat;
		String data = "";
		File user_chat_file;
		try
		{			
			System.out.print("username : "+user_id);
			System.out.println("	group : "+group_id);

			user_chat = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\groups\\"+group_id+"\\Chats.msgr";
			user_chat_file = new File(user_chat);
	
			lnr = new LineNumberReader(new FileReader(user_chat_file));
			while((data = lnr.readLine()) != null)
			{
					if(data.equals("/message"))
						nmessage++;

					if(nmessage == position)
					{
						while((data = lnr.readLine()) != null && !(data.equals("/time")))
						{
							message += "\n" + data;
						}
						break;
					}
			}
			lnr.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return message;
	}	

	private boolean setRead(String user_id, String group_id)
	{	
		String temp_data = "";
		String cur_line = "";
		String old_data = "";
		String user_unread = "";

		File user_unread_file;
		FileWriter fwr = null;
		
		boolean file_closed = false;
		boolean response = false;
		boolean unread_exist = false;
		try
		{						
			user_unread = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\Unread.msgr";

			user_unread_file = new File(user_unread);
			
				lnr = new LineNumberReader(new FileReader(user_unread_file));

				while((cur_line = lnr.readLine()) != null)
				{
					if(cur_line.equals("/type") && !(unread_exist))
					{
						temp_data = cur_line;
						cur_line = lnr.readLine();
						if(cur_line.equals("group"))
						{
							temp_data += "\n" + cur_line;
							cur_line = lnr.readLine();
							if(cur_line.equals("/name"))
							{
								temp_data += "\n" + cur_line;
								cur_line = lnr.readLine();
								if(cur_line.equals(group_id))
								{
									lnr.readLine();
									lnr.readLine();
									unread_exist = true;
								}
								else
								{
									old_data += "\n" + temp_data + "\n" + cur_line;
								}
							}
							else
							{
								old_data += "\n" + temp_data + "\n" + cur_line;
							}		
						}
						else
						{
							old_data += "\n" + temp_data + "\n" + cur_line;
						}
					}
					else
					{
						old_data += "\n" + cur_line;
					}
				}
				lnr.close();

				if(unread_exist)
				{
					fwr = new FileWriter(user_unread_file);
					fwr.write(old_data);
					System.out.println("file edited");
					fwr.close();
				}
				file_closed = true;
			response = true;		
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
				System.out.println("\nError occured while closing the file");
				ioe.printStackTrace();
			}
		}
		return response;
	}
	
	private String getTime()
	{
		date_obj = rightNow.getTime();
		return df_obj.format(date_obj);
	}
}