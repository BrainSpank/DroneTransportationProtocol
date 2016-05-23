print "Start simulator (SITL)"
from dronekit_sitl import SITL
import bluetooth
import platform
from PyBluez import *

def search():
    devices = bluetooth.discover_devices(duration=10, lookup_names = True)
    return devices

def getDevices():
    while True:
        results = search()
        if (results!=None):
            for addr, name in results:
                print "{0} - {1}".format(addr, name)
                return results
        time.sleep(10)

platform = platform.system()

if(platform == "Linux"):
    sitl = SITL(path="/home/pi/Documents/ardupilot/ArduCopter/ArduCopter.elf")
elif(platform == "Darwin"):
    sitl = SITL()
    sitl.download('copter', 'stable', verbose=True)

sitl_args = ['-I0', '--model', 'quad', '--home=-35.363261,149.165230,584,353']
sitl.launch(sitl_args, await_ready=True, restart=True)

# Import DroneKit-Python
from dronekit import connect, VehicleMode
import time

# Connect to the Vehicle.
print "Connecting to vehicle on: 'tcp:127.0.0.1:5760'"
vehicle = connect('tcp:127.0.0.1:5760', wait_ready=True)

# Get some vehicle attributes (state)
print "Get some vehicle attribute values:"
print " GPS: %s" % vehicle.gps_0
print " Battery: %s" % vehicle.battery
print " Last Heartbeat: %s" % vehicle.last_heartbeat
print " Is Armable?: %s" % vehicle.is_armable
print " System status: %s" % vehicle.system_status.state
print " Mode: %s" % vehicle.mode.name    # settable


"""
Arms vehicle and fly to aTargetAltitude.
"""

print "Basic pre-arm checks"
# Don't try to arm until autopilot is ready
while not vehicle.is_armable:
    print " Waiting for vehicle to initialise..."
    time.sleep(1)


hostMACAddress = 'B8:27:EB:FD:16:4B' # The MAC address of a Bluetooth adapter on the server. The server might have multiple Bluetooth adapters.
port = 3
backlog = 1
size = 1024
s = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
s.bind((hostMACAddress, port))
s.listen(backlog)
try:
    client, clientInfo = s.accept()
    while 1:
        data = client.recv(size)
        if data:
            print "Receiving data: "
            print(data)
            client.send(data) # Echo back to client
except:
    print("Closing socket")
    client.close()
s.close()


"""
print "Arming motors"
# Copter should arm in GUIDED mode
vehicle.mode    = VehicleMode("GUIDED")
vehicle.armed   = True

server()


print " Setting ARMING_CHECK to 0"
ARMING_CHECK = 0

# Confirm vehicle armed before attempting to take off
while not vehicle.armed:
    print " Is Armable?: %s" % vehicle.is_armable
    print " Waiting for arming..."
    time.sleep(5)
"""