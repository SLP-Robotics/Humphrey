import threading
from networktables import NetworkTables
import logging

logging.basicConfig(level=logging.DEBUG)

cond = threading.Condition()
notified = [False]

def connection_listener(connected, info):
    print(info, '; Connected=%s' % connected)
    with cond:
        notified[0] = True
        cond.notify()


NetworkTables.initialize(server='10.82.34.2')
NetworkTables.addConnectionListener(connection_listener, immediateNotify=True)

with cond:
    print("Waiting")
    if not notified[0]:
        cond.wait()
    else:
        print('NetworkTables is not connected')

table = NetworkTables.getTable('SmartDashboard')

table.putNumber('bruh', 5)
value = table.getNumber('bruh', 0)

print(value)
