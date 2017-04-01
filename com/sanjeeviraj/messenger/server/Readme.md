# server application
server needs to open Start class
It will wait for connection request
When a client requests for connection, it will open a new thread named Messenger
Messenger will handle that socket
Each thread is connected with a client
Interupt thread will always run in background
When Exit is entered, it will ask another time to confirm
If confirmed, new connection requests will not accepted
When all client closes the conncetion, the application will be closed.
