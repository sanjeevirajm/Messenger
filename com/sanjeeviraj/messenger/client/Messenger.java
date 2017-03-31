package com.sanjeeviraj.messenger.client;

import java.util.*;

public class Messenger implements Choice
{	
	public static void main(String args[])
	{
		try
		{
			Messenger ms = new Messenger();
			ms.getChoice();
		}
		catch(Exception e)
		{
			System.out.println("Error occured\nExiting messenger...\nError Details : ");
			e.printStackTrace();
		}

	}
	
	public void getChoice()
	{
		int choice;
		System.out.println("You can exit or go back by typing exit or back in small letters anywhere\n----------------------------------------------------------\n");
		System.out.println("\nEnter choice:\n1-Sign in\n2-Sign up\n3-Exit\n");
		try
		{
			Scanner in = new Scanner(System.in);
			choice = in.nextInt();
			switch(choice)
			{
				case 1: SignIn si = new SignIn();
						si.main();
						break;
						
				case 2: SignUp su = new SignUp();
						su.main();
						break;
						
				case 3: System.exit(0);
						break;
						
				default: System.out.println("\nIncorrect choice\nRestarting messenger...");
						 this.getChoice();
			}
		}
		catch(InputMismatchException ime)
		{
			System.out.println("\nwrong input entered\nRestarting messenger...");
			this.getChoice();
		}
		return;
	}
}