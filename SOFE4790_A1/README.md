# TCP Chat Application
### For Unix Operating Systems (Written in Python)

#### By: Dominick Mancini -- 100517944 ####
#### Course: SOFE 4790U - Distributed Systems ####

## Usage
##### To Run the Client From Source #####
* Open a Terminal Window
* Navigate to the source directory
* Run
    ```
     python Client.py <ip of server> <port of server>
    ```
* The program will not launch if the server cannot be conected to

##### To Run the Server From Source #####
* Open a Terminal Window
* Navigate to the source directory
* Run
    ```
    python Server.py
    ```

##### Executing Without Python #####
* The server can still be run using precompiled binaries provided with the submission
* Open a terminal and navigate to the source directory
* For the client, run
    ```
    dist/Client/Client <ip of server> <port of server>
    ```
* For the server, run
    ```
    dist/Server/Server
    ```

##### Using the Client #####
* Once the client is loaded, it will automatically try to connect to the server.
* Once the server is connected, you will be prompted to provide a username (no spaces are allowed)
* After entering a username, you will be brought into a general chat room.
* Any messages you send here without appending the proper prefix will be sent to all members of the chat room.
* To send a private message, type ```<username> >> <message>```
    * For example, to send a private message "Hello World!" to Foo, use ```Foo >> Hello World!```
    * Messages will show up to the recipiten in the format ```<username> [private] message```
    * For example, if your username is Bar, and we sent the above message, Foo, would receive it as ```<Bar> [private] Hello World!```
* The server also provides to keyword commands to the client
    * ```leave``` - Disconnects you from the server and terminates the program
    * ```list``` - Lists the usernames and IP Addresses of all clients connected to the chat. Useful for finding the name of someone you wish to message privately.
* The prompt identifies messages you sent with ```"<You>"``` higlighted in green
* Messages from other users are identified with their username as ```<username>``` highlighted in blue

##### Terminating the Client and Server #####
* **Server**
    * Ctrl+C will terminate the server, all connected clients will have their connections terminated as well
* **Client**
    * use the "leave" command, the server will properly acknowledge your departure, remove you from the connection list, and notify other users
    * **NOT RECOMMENDED:** Ctrl+C can also terminate the client, however, the server will be unaware of the departure, and will not remove you from the connection list or notify other users; trying to sign back in with the same username will result in an error from the server saying that username is in use and you must provide another.
