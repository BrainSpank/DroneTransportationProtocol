print "Start simulator (SITL)"
import sys
from dronekit_sitl import SITL
import bluetooth
from PyBluez import *

if len(sys.argv) == 0 or sys.argv[0] != "client" or sys.argv[0] != "server":
    print " use command 'python flightWithConnection.py client [or] server' to run"

connectionType = sys.argv[0]
if connectionType == "client":
    home = '--home=51.01,-3.01,60,180'
else:
    home = '--home=51.00,-3.00,60,180'

sitl = SITL(path="/home/pi/Documents/ardupilot/ArduCopter/ArduCopter.elf")
# sitl.download('copter', 'stable', verbose=True)
sitl_args = ['-I0', '--model', 'quad', home]
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

time.sleep(3)

"""
Arms vehicle and fly to aTargetAltitude.
"""

print "Basic pre-arm checks"
# Don't try to arm until autopilot is ready
while not vehicle.is_armable:
    print " Waiting for vehicle to initialise..."
    time.sleep(1)


print "Arming motors"
# Copter should arm in GUIDED mode
vehicle.mode    = VehicleMode("GUIDED")
vehicle.armed   = True

if connectionType == "client":
    socket = client()
else:
    server()

# Confirm vehicle armed before attempting to take off
while not vehicle.armed:
    print " Is Armable?: %s" % vehicle.is_armable
    print " Waiting for arming..."
    if connectionType == "client":
        socket.send(vehicle.location)
