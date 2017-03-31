package com.sanjeeviraj.messenger.server;
 
interface Choice
{
	public static final String SIGNINCLASS = "SignIn";
	public static final String SIGNUPCLASS = "SignUp";
	public static final String CHATS = "Chats";
	public static final String CHATCONTACT = "ChatContact";
	public static final String CHATGROUP = "ChatGroup";
	public static final String PROFILECLASS = "Profile";
	public static final String CONTACTS = "Contacts";
	public static final String GROUPS = "Groups";
	public static final String CHECKCONNECTION = "check_connection";
	public static final String EXIT = "exit";
	
	public void getChoice();
}