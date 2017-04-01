# Client application:
  Client/user need to run Messenger class to start application. It shows two options: 
  *Sign in -> will run SignIn class
  *Sign up -> will run SignUp class

# SignIn:
  This class will get user id and password as input. If these are entered correctly, it will run StartScreen class
  If password is entered wrong continuously three times, it will display "password hint"
  If password is entered wrong continuously three times,it will display "forgot password" and ask to enter yes or no
  If yes is entered, user can recover account(change to new password) by entering personal question answer
  
# SignUp:
  This class will get user id as input. If user id available it moves to next input, else it again asks for user id
  password, hint, personal question, answer, profile name, status are entered and send it to server
  Messenger Class will be opened
  
  # StartScreen
  It will display 5 static options:
  1.Chats [number]-> open Chats class. number represents number of unread chats
  2.Contacts -> open contacts class
  3.Groups -> open groups class
  4.Profile -> open profile class
  5.Exit -> close messenger

  # Chats
  It will display 4 static options:
  1.ChatContact -> ask for contact id -> if correct & available in contacts-> open ChatContact class
  2.ChatGroup -> ask for group id -> if correct & user is a member of group id -> open ChatGroup class
  3.Back -> open startscreen class
  4.Exit -> close messenger
  
  Dynamic options starts from 5
  5.unread contact chat 1 -> open ChatContact class
  6.unread contact chat 2 -> open ChatContact class
  ...
  8.unread group chat 1 -> open ChatGroup class

# Contacts
  It will display 4 static options:
  1.AddContact -> ask for contact id -> add to contacts-> open Contacts class
  2.RemoveContact -> ask for contact id -> remove from contacts-> open Contacts class
  3.Back -> open startscreen class
  4.Exit -> close messenger
  
  Dynamic options starts from 5
  5.contact 1 -> open ChatContact class
  6.contact 2 -> open ChatContact class

# ChatContact
  It will display 4 static options:
  1.send message -> enter message -> send message to contact-> open ChatContacts class
  2.Next page -> show next 10 messages
  3.previous page -> show previous 10 messages
  4.Back -> open Chats class
  5.Exit -> close messenger
  
  10 messages will be shown here

# Groups
  It will display 4 static options:
  1.Create group -> ask for group id, name -> create group -> open Groups class
  2.Delete group -> ask for group id -> remove user from group, delete chats file and corresponding folder inside user folder-> open Groups class
  3.Back -> open startscreen class
  4.Exit -> close messenger
  
  Dynamic options starts from 5
  5.Group 1 -> open ChatGroup class
  6.Group 2 -> open ChatGroup class
  ...
  
# ChatGroup
  It will display 6 static options:
  1.send message -> enter message -> send message to all members-> open ChatGroup class
  2.Next page -> show next 10 messages
  3.previous page -> show previous 10 messages
  4.Manage group -> show managegroup options
  5.Back -> open startscreen class
  6.Exit -> close messenger
  
  10 messages will be shown here

# ChatGroup - ManageGroup
  It will display 6 static options:
  1.add a member -> check permission -> enter user id -> check user -> add member -> call manageGroup()
  2.remove a member -> check permission -> enter member id -> check member -> remove member -> call manageGroup()
  3.add admin permission to a member-> check user permission -> enter member id -> check member permission -> add admin permission to member -> call manageGroup()
  4.remove admin permission to a member-> check user permission -> enter member id -> check member permission -> remove admin permission from member -> call manageGroup()
  5.Back -> open ChatGroup class
  6.Exit -> close messenger
  
  Group members and their details will be shown here
  
  # Profile
  It will display 7 static options:
  1.change profile name -> enter new name -> change name -> open Profile class
  2.change status -> enter new status -> change status -> open Profile class
  3.change password -> enter old password -> verify -> enter new password -> change password -> open Profile class
  4.change secret question & answer -> show question -> enter old answer -> verify -> enter new question & answer -> change secret question & answer -> open Profile class
  5.change hint -> enter new hint -> change hint -> open Profile class
  6.Back -> open startscreen class
  7.Exit -> close messenger
  
Profile details will be shown here
