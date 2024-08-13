# Networking-Java-App
A chat app built using Java Swing and sockets. Allows for 2 TCPClients to interact via a TCPServer and send messages in real time. Message history is provided in a text log format.

![](https://github.com/AD-May/Networking-Java-App/blob/main/giphy.gif)

# To run application:

1. Open a terminal window in the IDE.
2. Navigate to the main project directory if not already there.
3. Compile the Java files using the javac command:
 javac *.java
4. Run the server using the java command:
 java ChatServerStarter
and click 'Start' to start the server
5. Open a new terminal window (without closing the current one) and run the first client:
 java TCPClient
6. Open another new terminal window and run the second client:
 java TCPClient
