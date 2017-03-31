package com.sanjeeviraj.messenger.server;

import java.net.*; 
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;

public class ChatContact implements Choice
{
	public ChatContact() {	}
	
	public ChatContact(ServerSocket serverSocketArg, Socket socketObjectArg)
	{
		System.out.println("Signup constructor");
		serverSocket = serverSocketArg;
		socketObject = socketObjectArg;
	}
	
	private static String PATH = "com\\sanjeeviraj\\messenger\\server\\Accounts.config";
	public static final String CHECKUSERNAME = "checkUsername";

	public static final String SENDMESSAGE = "sendMessage";
	public static final String GETNOOFMESSAGES = "getNoOfMessages";
	public static final String GETMESSAGETIME = "getMessageTime";
	public static final String GETMESSAGE = "getMessage";
	public static final String SETREAD = "setRead";

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
	ChatContact co;
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
			co = new ChatContact(serverSocketArg, socketObjectArg);
			System.out.println("Signup object created");
			co.startConnection();
			System.out.println("after connecting to the client");
			co.getChoice();					
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
   	String contact_id = "";
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
								 contact_id = dis.readUTF();
								 dos.writeUTF("connected");
								 String message = dis.readUTF();
								 dos.writeUTF(""+sendMessage(username, contact_id, message));
								 break;									 							 

			case GETNOOFMESSAGES :	 dos.writeUTF("connected");
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 contact_id = dis.readUTF();
								 dos.writeUTF(""+getNoOfMessages(username, contact_id));
								 break;									 							 

			case GETMESSAGE :	 dos.writeUTF("connected");
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 contact_id = dis.readUTF();
								 dos.writeUTF("connected");
								 int position = Integer.parseInt(dis.readUTF());
								 dos.writeUTF(""+getMessage(username, contact_id, position));
								 break;	 

			case GETMESSAGETIME :	 dos.writeUTF("connected");
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 contact_id = dis.readUTF();
								 dos.writeUTF("connected");
								 position = Integer.parseInt(dis.readUTF());
								 dos.writeUTF(""+getMessageTime(username, contact_id, position));
								 break;									 							 								 								 								

			case SETREAD :	 	 dos.writeUTF("connected");
								 username = dis.readUTF();
								 dos.writeUTF("connected");
								 contact_id = dis.readUTF();
								 dos.writeUTF(""+setRead(username, contact_id));
								 break;									 							 								 								 								

			case EXIT : 		CLOSE_CONN = true;
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
    
	private boolean sendMessage(String user_id, String contact_id, String message)
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

		File user_chat_file;
		File contact_chat_file;
		File contact_unread_file;
		FileWriter fwr = null;
		
		int unread_no = 0;
		boolean file_closed = false;
		boolean response = false;
		boolean unread_exist = false;
		try
		{			
			System.out.println("**sendmessage 1");
			
			user_chat = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\contacts\\"+contact_id+"\\Chats.msgr";
			contact_chat = "com\\sanjeeviraj\\messenger\\server\\users\\"+contact_id+"\\contacts\\"+user_id+"\\Chats.msgr";
			contact_unread = "com\\sanjeeviraj\\messenger\\server\\users\\"+contact_id+"\\Unread.msgr";

			System.out.println("**sendmessage 2");
			user_chat_file = new File(user_chat);
			System.out.println("**sendmessage 3");
			contact_chat_file = new File(contact_chat);
			System.out.println("**sendmessage 4");
			contact_unread_file = new File(contact_unread);
			System.out.println("**sendmessage 5");
			
			if(!(user_chat_file.exists()) || !(contact_chat_file.exists()) || !(contact_unread_file.exists()))
			{
				System.out.println("**sendmessage 6");
				response = false;
			}
			else
			{
				System.out.println("**sendmessage 7");

			lnr = new LineNumberReader(new FileReader(user_chat_file));

			while((data = lnr.readLine()) != null)
					data1 = data1 + data + "\n";
			
			message_details = "\n"+"/time"+"\n"+getTime()+"\n"+"/message"+message+"\n";
			System.out.println("Message : "+message_details);

			lnr.close();
			//data1 = data1.trim();
			data1 = message_details + data1;

			fwr = new FileWriter(user_chat_file);
			fwr.write(data1);
			System.out.println("file edited");
			fwr.close();

			if(!user_id.equals(contact_id))
			{
				data1 = "";
				lnr = new LineNumberReader(new FileReader(contact_chat_file));

				while((data = lnr.readLine()) != null)
						data1 = data1 + data + "\n";

				lnr.close();
				data1 = data1.trim();
				data1 = message_details + data1;

			
				fwr = new FileWriter(contact_chat_file);
				fwr.write(data1);
				System.out.println("file edited");
				fwr.close();
			

				data1 = "";
				lnr = new LineNumberReader(new FileReader(contact_unread_file));

				while((cur_line = lnr.readLine()) != null)
				{
					if(cur_line.equals("/type") && !(unread_exist))
					{
						temp_data = cur_line;
						cur_line = lnr.readLine();
						if(cur_line.equals("person"))
						{
							temp_data += "\n" + cur_line;
							cur_line = lnr.readLine();
							if(cur_line.equals("/name"))
							{
								temp_data += "\n" + cur_line;
								cur_line = lnr.readLine();
								if(cur_line.equals(user_id))
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
					data = "/type\nperson\n/name\n"+user_id+"\n/unread\n"+unread_no;
					old_data = data + "\n" + old_data;
				}
				else
				{
					old_data = user_data + "\n" + old_data;
				}

				fwr = new FileWriter(contact_unread);
				fwr.write(old_data);
				System.out.println("file edited");
				fwr.close();
				response = true;
				file_closed = true;
			}
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

	
	private boolean setRead(String user_id, String contact_id)
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
						if(cur_line.equals("person"))
						{
							temp_data += "\n" + cur_line;
							cur_line = lnr.readLine();
							if(cur_line.equals("/name"))
							{
								temp_data += "\n" + cur_line;
								cur_line = lnr.readLine();
								if(cur_line.equals(contact_id))
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

	private int getNoOfMessages(String user_id, String contact_id)
	{
		int nmessage = 0;
		String data = "";
		String user_chat;
		File user_chat_file;
		try
		{
			System.out.print("username : "+user_id);
			System.out.println("	conact : "+contact_id);
			user_chat = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\contacts\\"+contact_id+"\\Chats.msgr";
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

	private String getMessageTime(String user_id, String contact_id, int position)
	{		
		String time = "";
		int nmessage = 0;
		String user_chat;
		String data = "";
		File user_chat_file;
		try
		{
			System.out.print("username : "+user_id);
			System.out.println("	conact : "+contact_id);
			user_chat = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\contacts\\"+contact_id+"\\Chats.msgr";
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

	private String getMessage(String user_id, String contact_id, int position)
	{
		String message = "";
		int nmessage = 0;
		String user_chat;
		String data = "";
		File user_chat_file;
		try
		{			
			System.out.print("username : "+user_id);
			System.out.println("	conact : "+contact_id);

			user_chat = "com\\sanjeeviraj\\messenger\\server\\users\\"+user_id+"\\contacts\\"+contact_id+"\\Chats.msgr";
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

	private String getTime()
	{
		date_obj = rightNow.getTime();
		return df_obj.format(date_obj);
	}
	
}