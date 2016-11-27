from __future__ import print_function

import ast
import socket
from random import randint
import Pyro4
import struct

import select

uri = input("What is the Pyro uri of the greeting object? ").strip() # This is a large string in the format "PYRO:<large string>@localhost:<port>
name = input("What is your name? ").strip()

server = Pyro4.Proxy(uri)         # get a Pyro proxy to the server object
print(server.get_fortune(name))   # call method normally

LOOP_COUNT = 2000
i = 0
logical_clock = 0

MCAST_GRP = '224.1.1.1'
MCAST_PORT = 5007

STATES = ['RELEASED', 'WANTED', 'HELD']

STATE = STATES[0]
expecting_approval = False

is_approved = False


def increment_client_clock():
    global logical_clock
    logical_clock += 1


def multicast_request(state):
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 2)
    sock.sendto(str({'state': state, 'time': logical_clock}), (MCAST_GRP, MCAST_PORT))
    global expecting_approval
    expecting_approval = True


def send_approval(is_approved):
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
    sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 2)
    sock.sendto(str(is_approved), (MCAST_GRP, MCAST_PORT))


def to_bool(message):
    return True if message == "True" else False


def check_for_messages():
    global is_approved, expecting_approval
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
    sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    sock.bind(('', MCAST_PORT))  # use MCAST_GRP instead of '' to listen only
    # to MCAST_GRP, not all groups on MCAST_PORT
    mreq = struct.pack("4sl", socket.inet_aton(MCAST_GRP), socket.INADDR_ANY)

    sock.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)

    ready = select.select([sock], [], [], 1)
    if ready[0]:
        message = sock.recv(10240)
        remote_name = str(sock.recvfrom(1024))
        if isinstance(ast.literal_eval(message), dict):
            message_proper = ast.literal_eval(message)
            print("Process '" + remote_name + "' changing state to " + str(message_proper['state']))
            if STATE != STATES[1] or STATE != STATES[2]:
                send_approval(True)
            else:
                send_approval(False)
        else:
            is_approved = to_bool(message)
            expecting_approval = False


while i < LOOP_COUNT:
    check_for_messages()
    rand_num = randint(0, 4)
    if rand_num == 0:  # Access critical section
        attempt = 1
        STATE = STATES[1]
        multicast_request(STATE)
        print("Accessing critical section")
        access_success = server.critical_section()
        while not access_success:
            STATE = STATES[1]
            check_for_messages()
            print ("Critical Section in use. Attempt #: " + str(attempt) + " failed.", end='\r')
            access_success = server.critical_section()
            STATE = STATES[2]
            attempt += 1
        print ("\nSuccess after " + str(attempt) + " attempts.")
        STATE = STATES[0]
    elif rand_num == 1:
        server_clock_count = server.send_message_to_client()
        print("Server clock count: " + str(server_clock_count))
        logical_clock = max(logical_clock, server_clock_count)
        print("Client clock synced with server: " + str(logical_clock))
    elif rand_num == 2:
        increment_client_clock()
        print ("Incremented client clock: " + str(logical_clock))
    elif rand_num == 3:
        print("Sending time to server")
        server.sync_clock(logical_clock)
    else:
        print ("Incrementing logical clock count on server")
        server.logical_clock_increment()
    i += 1
