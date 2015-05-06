In the first Window, assignment pdf auto fills the IP and Port text field and makes it uneditable unless checkbox is clicked.  I found it 
unpractical so left it editable for the user if they will be hosting on a specific IP or port.

For custom file, sending vector of strings over the server.

For network gameplay, it should be running fine.  Error that used to occur was a game freeze.  Timer frozen and buttons unclickable.
But this error only happened once in a while, meaning it was probably a thread issue.  Made some changes and haven't ran into that error 
again but if it occurs, it probably will not happen in a new game. 

Was not sure how to check for loss of internet connection, but it should be correctly implemented.  
In CheckConnectionThread class, run method writes to server/host and on the receiving end, the server/host writes back the same message.  
Once a player looses internet, it will be caught in the write to exception where it will check for internet connection.  If the player has
internet connection then it means the other player lost connection.
 - Server checks for this in the method sendMessage();
 