import select
import socket
import sys


#For terminal colours
class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


def prompt():
    sys.stdout.write(bcolors.OKGREEN + bcolors.BOLD + '<You> ' + bcolors.ENDC)
    sys.stdout.flush()


# main function
if __name__ == "__main__":

    if len(sys.argv) < 3:
        print 'Usage : python Client.py hostname port'
        sys.exit()

    print "Welcome to the Python-Powered Chat Client."
    # username = sys.stdin.readline().rstrip()
    # print "Welcome, %s" % username

    host = sys.argv[1]
    port = int(sys.argv[2])

    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(2)

    # connect to remote host
    try:
        s.connect((host, port))
    except:
        print 'Unable to connect'
        sys.exit()

    print 'Connecting to remote host, please wait...'
    uname_prompt = s.recv(4096)
    sys.stdout.write(uname_prompt)
    sys.stdout.flush()
    uname = sys.stdin.readline().rstrip()
    s.send(uname)
    # prompt()

    while 1:
        socket_list = [sys.stdin, s]

        # Get the list sockets which are readable
        read_sockets, write_sockets, error_sockets = select.select(socket_list, [], [])

        for sock in read_sockets:
            # incoming message from remote server
            if sock == s:
                data = sock.recv(4096)
                if not data:
                    print '\nDisconnected from chat server'
                    sys.exit()
                else:
                    # print data
                    sys.stdout.write(data)
                    if bcolors.WARNING not in data:
                        prompt()
                    else:
                        sys.stdout.flush()

            # user entered a message
            else:
                msg = sys.stdin.readline()
                s.send(msg)
                prompt()
