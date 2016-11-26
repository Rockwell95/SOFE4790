from __future__ import print_function

from random import randint
import Pyro4

uri = input("What is the Pyro uri of the greeting object? ").strip()
name = input("What is your name? ").strip()

server = Pyro4.Proxy(uri)         # get a Pyro proxy to the greeting object
print(server.get_fortune(name))   # call method normally

LOOP_COUNT = 2000
i = 0
logical_clock = 0


def increment_client_clock():
    global logical_clock
    logical_clock += 1


while i < LOOP_COUNT:
    rand_num = randint(0, 4)
    if rand_num == 0:
        attempt = 1
        print("Accessing critical section")
        access_success = server.critical_section()
        while not access_success:
            print ("Critical Section in use. Attempt #: " + str(attempt) + " failed.", end='\r')
            access_success = server.critical_section()
            attempt += 1
        print ("\nSuccess after " + str(attempt) + " attempts.")
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
