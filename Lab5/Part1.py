import sys
import Pyro4

import time

print 'Number of arguments:', len(sys.argv), 'arguments.'
print 'Argument List:', str(sys.argv)

locked = False
pid = sys.argv[1]
step_count = 0


@Pyro4.expose
class Server(object):
    def __init__(self):
        print "PID:" + pid

    def get_fortune(self, name):
        return "Hello, {0}. Here is your fortune message:\n" \
               "Behold the warranty -- the bold print giveth and the fine print taketh away.".format(name)

    def critical_section(self):
        if not locked:
            self.lock_section(True)
            print "IN CRITICAL SECTION"
            time.sleep(3)
            self.lock_section(False)
            return True
        else:
            #print "CRITICAL SECTION INACCESSIBLE"
            return False

    def logical_clock_increment(self):
        global step_count
        print "incrementing Clock: " + str(step_count)
        step_count += 1

    def lock_section(self, is_lock):
        global locked
        locked = is_lock

    def send_message_to_client(self):
        print "Sending Clock to Client: " + str(step_count)
        return step_count

    def sync_clock(self, client_time):
        global step_count
        step_count = max(step_count, client_time)
        print "Synced clock with client: " + str(step_count)

daemon = Pyro4.Daemon()                # make a Pyro daemon
uri = daemon.register(Server)   # register the server as a Pyro object

print("Ready. Object uri =", uri)      # print the uri so we can use it in the client later
daemon.requestLoop()                   # start the event loop of the server to wait for calls