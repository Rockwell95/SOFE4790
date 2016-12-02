import ast
import socket
import struct
import sys
from random import randint
import time
import threading
from ast import literal_eval as make_tuple

import select

clock = 0

MCAST_GRP = '224.1.1.1'
MCAST_PORT = 5007

STATES = ['RELEASED', 'WANTED', 'HELD']
critical_state = STATES[0]
state = STATES[0]

is_approved = False
expecting_approval = False
g_allowed = False


print "Hello!"


def clock_generator():
    global clock
    clock += 1


# ============= SEND STATE ===============
def broadcast_state(send_state):
    message = str((send_state, clock))
    multicast_group = ('224.1.1.1', 5007)

    # Create UDP Socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    # Set a timeout so the socket does not block indefinitely when trying to receive data
    sock.settimeout(0.2)

    ttl = struct.pack('b', 1)
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, ttl)

    # Sender
    print "Sending"
    try:
        # send data to multicast group
        print >> sys.stderr, 'sending "%s"' % message
        sent = sock.sendto(message, multicast_group)

        # Look for responses from all recipients
        while True:
            print >> sys.stderr, 'waiting to receive'
            try:
                data, server = sock.recvfrom(16)
            except socket.timeout:
                print >> sys.stderr, 'timed out; no more responses'
                break
            else:
                print >> sys.stderr, 'received "%s" from %s' % (data, server)
                if send_state == STATES[1]:
                    global g_allowed
                    g_allowed = to_bool(data)

    finally:
        print >> sys.stderr, 'closing socket'
        sock.close()
# =============== END SEND STATE =============


def critical_section():
    global state, g_allowed
    state = STATES[1]
    # print str(g_allowed and (critical_state != (STATES[2])))
    if g_allowed and (critical_state != (STATES[2])):
        state = STATES[2]
        broadcast_state(state)
        print "*******************"
        print "IN CRITICAL SECTION"
        print "*******************"
        i = 0
        time.sleep(3)
        state = STATES[0]
        broadcast_state(state)
        g_allowed = False
    else:
        print "Request denied, will try again in a bit"
        broadcast_state(state)


def send_approval(approved):
    global critical_state
    if approved:
        critical_state = STATES[2]
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 2)
    sock.sendto(str(approved), (MCAST_GRP, MCAST_PORT))


def to_bool(message):
    return True if message == "True" else False


# Thread for checking for messages (running continuously)
def check_for_messages():
    multicast_group = '224.1.1.1'
    server_address = ('', 5007)

    # Create the socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEPORT, 1)
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

    # Bind to the server address
    sock.bind(server_address)

    # Tell the operating system to add the socket to the multicast group
    # on all interfaces.
    group = socket.inet_aton(multicast_group)
    mreq = struct.pack('4sL', group, socket.INADDR_ANY)
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)

    # Receive/respond loop
    while True:
        print >> sys.stderr, '\nwaiting to receive message'
        data, address = sock.recvfrom(1024)

        print >> sys.stderr, 'received %s bytes from %s' % (len(data), address)
        print >> sys.stderr, data

        print >> sys.stderr, 'sending approval status acknowledgement to', address
        global critical_state
        t_data = make_tuple(data)
        if t_data[0] != 'WANTED':
            critical_state = data[0]

        if t_data[0] == 'WANTED' and state != 'HELD':
            reply = str(clock < t_data[1])
        else:
            reply = 'False'

        print >> sys.stderr, 'sending', reply

        sock.sendto(reply, address)


def worker():
    while True:
        check_for_messages()


def loop():
    while True:
        random_num = randint(0, 1)
        # print random_num
        if random_num > 0:
            clock_generator()
            # print clock
        else:
            # print "Attempting Critical Section"
            # print "Allowed: " + str(g_allowed)
            print "Critical Section Status: " + critical_state
            critical_section()

#  Set up threads
general_loop = threading.Thread(target=loop)
general_loop.start()
message_checker = threading.Thread(target=worker)
message_checker.start()
