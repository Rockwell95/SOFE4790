# TCP Chat Server
# SOFE 4790U - Distributed Systems - Fall 2016 - Dominick Mancini - 100517944
# Based on and adopted from guide at http://www.binarytides.com/code-chat-application-server-client-sockets-python/
# NOTE: This code will not operate on a Windows Based Machine due to the fact that it uses some Unix system-level code


import select
import socket
from time import strftime, gmtime


# Function to broadcast chat messages to all connected clients
def broadcast_data(sock, message):
    # Do not send the message to master socket and the client who has send us the message
    for socket in CONNECTION_LIST:
        if socket != server_socket and socket != sock:
            try:
                socket.send(message)
            except:
                # broken socket connection
                socket.close()
                CONNECTION_LIST.remove(socket)


# Function to find username given ip and pid
def get_username(ip_pid_tuple):
    obj = next((client for client in CLIENTS if client.ip == ip_pid_tuple[0] and client.pid == ip_pid_tuple[1]), None)
    return obj.username


def uname_exists(name):
    obj = next((client for client in CLIENTS if client.username == name), None)
    return obj is not None


def get_obj_by_uname(uname):
    return next((client for client in CLIENTS if client.username == uname), None)


def get_recipient_sock_by_peername(peer_tuple):
    return next((client for client in CLIENTS if client.socket.getpeername() == peer_tuple), None).socket


def list_clients():
    cli_list = "\r"
    count = 1
    for client in CLIENTS:
        num_of_spaces = 20 - len(client.username)
        i = 0
        padding = ""
        while i < num_of_spaces:
            padding += " "
            i += 1

        cli_list += "%d. %s%s%s:%s\n" % (count, client.username, padding, client.ip, client.pid)
        count += 1
    cli_list += "\n"
    return cli_list


class Client(object):
    ip = "0.0.0.0"
    pid = "0000"
    username = ""
    socket = None

    def __init__(self, ip, pid, uname, socket):
        self.ip = ip
        self.pid = pid
        self.username = uname
        self.socket = socket


# For terminal colours
class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


if __name__ == "__main__":
    # List to keep track of socket descriptors
    CONNECTION_LIST = []
    # List to keep track of connected users.
    CLIENTS = []
    RECV_BUFFER = 4096  # Advisable to keep this as exponent of 2
    PORT = 5000

    server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # This has no effect, why?
    server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    server_socket.bind(("127.0.0.1", PORT))
    server_socket.listen(10)

    # Add server socket to the list of readable connections
    CONNECTION_LIST.append(server_socket)
    print bcolors.OKBLUE + "[" + strftime("%Y-%m-%d %H:%M:%S GMT", gmtime()) + "] " + bcolors.ENDC + "Chat server started on port " + str(PORT) + ". press ctrl-c to stop server"

    while 1:
        # Get the list sockets which are ready to be read through select
        read_sockets, write_sockets, error_sockets = select.select(CONNECTION_LIST, [], [])
        for sock in read_sockets:
            # New connection
            if sock == server_socket:
                # Handle the case in which there is a new connection received through server_socket
                sockfd, addr = server_socket.accept()
                CONNECTION_LIST.append(sockfd)
                # Prompt for username at client end
                username_request = "Connected! Welcome, please provide your username (no spaces are permitted)\n> "
                sockfd.send(username_request)
                uname = sockfd.recv(RECV_BUFFER)
                while " " in uname:
                    sockfd.send(
                        bcolors.WARNING + "\rThis username has spaces. Choose another > " + bcolors.ENDC)
                    uname = sockfd.recv(RECV_BUFFER)[:-1]
                # Checks if the name already exists
                while uname_exists(uname):
                    sockfd.send(
                        bcolors.WARNING + "\rThis username is already in use, please choose another > " + bcolors.ENDC)
                    uname = sockfd.recv(RECV_BUFFER).rstrip()

                CLIENTS.append(Client(addr[0], addr[1], uname, sockfd))
                last_index = len(CLIENTS) - 1
                latest_joiner = (CLIENTS[last_index].username, CLIENTS[last_index].ip, CLIENTS[last_index].pid)
                # Send Welcome Message to Client
                sockfd.send(
                    "\r\nWelcome %s!\nThe following commands are available:\n"
                    "1. leave   # Leave the chat\n"
                    "2. list    # List all currently connected members"
                    "\nTo send a direct message use "
                    "<name_of_recipient> >> <message> format\n"
                    "Otherwise, your message will be broadcast to everyone\n\n" % uname)

                print bcolors.OKBLUE + "[" + strftime("%Y-%m-%d %H:%M:%S GMT", gmtime()) + "] " + bcolors.ENDC + "Client \"%s\" (%s, %s) connected." % latest_joiner

                broadcast_data(sockfd, "\r\"%s\" [%s:%s] entered room\n" % latest_joiner)

            # Some incoming message from client
            else:
                # Data received from client,
                try:
                    # In Windows, sometimes when a TCP program closes abruptly,
                    # a "Connection reset by peer" exception will be thrown
                    data = sock.recv(RECV_BUFFER)
                    if data:
                        # For leaving the chat
                        if data == 'leave\n':
                            raise Exception("Disconnect")
                        # For listing connected users
                        elif data == "list\n":
                            sock.send(list_clients())
                        # Private messaging
                        elif ">>" in data:
                            message = data.split(" >> ")
                            recipient_name = message[0].rstrip()
                            private_message = bcolors.OKBLUE + "\r" + '<' + get_username(
                                sock.getpeername()) + '> [private] ' + bcolors.ENDC + message[1]
                            # Verify destination username actually exists
                            if uname_exists(recipient_name):
                                recipient = get_obj_by_uname(recipient_name)
                                recipient_peername = (recipient.ip, recipient.pid)
                                recipient_socket = get_recipient_sock_by_peername(recipient_peername)
                                recipient_socket.send(private_message)
                            else:
                                sock.send(
                                    bcolors.FAIL + "\r<SERVER> I'm sorry, %s was not found to be connected to this chat.\n" % recipient_name)
                        # For public messaging
                        else:
                            broadcast_data(sock, bcolors.OKBLUE + "\r" + '<' + get_username(
                                sock.getpeername()) + '> ' + bcolors.ENDC + data)
                # Loss of client connection
                except:
                    username = get_username(sock.getpeername())
                    user = get_obj_by_uname(username)
                    broadcast_data(sock, "\rClient \"%s\" (%s, %s) has left the chat\n" % (username, user.ip, user.pid))
                    print bcolors.OKBLUE + "[" + strftime("%Y-%m-%d %H:%M:%S GMT", gmtime()) + "] " + bcolors.ENDC + "Client \"%s\" (%s, %s) has left the chat" % (username, user.ip, user.pid)
                    sock.close()
                    CONNECTION_LIST.remove(sock)
                    CLIENTS.remove(get_obj_by_uname(username))
                    continue

    server_socket.close()
