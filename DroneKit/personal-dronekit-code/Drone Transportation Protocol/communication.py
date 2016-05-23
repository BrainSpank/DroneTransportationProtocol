print "Start simulator (SITL)"
from dronekit_sitl import SITL
# Import DroneKit-Python
from dronekit import connect, VehicleMode, LocationGlobalRelative
import os
import platform
import sys
import bluetooth
import time


if (len(sys.argv) > 1):
    startLocation = sys.argv[1]
else:
    # Co ordinates for cardiff
    startLocation = "-35.363261,149.165230,584,353"

platform = platform.system()

if(platform == "Linux"):
    sitl = SITL(path="/home/pi/Documents/ardupilot/ArduCopter/ArduCopter.elf")
elif(platform == "Darwin"):
    sitl = SITL()
    sitl.download('copter', 'stable', verbose=True)

sitl_args = ['-I0', '--model', 'quad', '--home=' + startLocation]
sitl.launch(sitl_args, await_ready=True, restart=True)

# Import DroneKit-Python
from dronekit import connect, VehicleMode

# Connect to the Vehicle.
print "Connecting to vehicle on: 'tcp:127.0.0.1:5760'"
vehicle = connect('tcp:127.0.0.1:5760', wait_ready=True)

# Get some vehicle attributes (state)
print "Get some vehicle attribute values:"
print " GPS: %s" % vehicle.gps_0
print " Location: %s" % vehicle.location.global_frame
print " Battery: %s" % vehicle.battery
print " Last Heartbeat: %s" % vehicle.last_heartbeat
print " Is Armable?: %s" % vehicle.is_armable
print " System status: %s" % vehicle.system_status.state
print " Mode: %s" % vehicle.mode.name    # settable

# plan route
waypoints = []
point1 = LocationGlobalRelative(vehicle.location.global_frame.lat, vehicle.location.global_frame.lon, 50)
point2 = LocationGlobalRelative(vehicle.location.global_frame.lat + 0.2, vehicle.location.global_frame.lon + 0.2, 50)
point3 = LocationGlobalRelative(vehicle.location.global_frame.lat + 0.2, vehicle.location.global_frame.lon + 0.2, 0)

waypoints.append(point1)
waypoints.append(point2)
waypoints.append(point3)

# deterct and onnect

print " Connecting via Bluetooth"
s = bluetooth.BluetoothSocket(bluetooth.RFCOMM)

if sys.argv[2] == "client":
    serverMACAddress = '00:11:67:C1:4B:76'
    port = 3
    s.connect((serverMACAddress, port))

    print "Sending Data:"
    s.send(" Global Location: %s" % vehicle.location.global_frame)
    s.send(" Global Location (relative altitude): %s" % vehicle.location.global_relative_frame)
    s.send(" Local Location: %s" % vehicle.location.local_frame)

    s.close()

else:
    hostMACAddress = '00:11:67:C1:4B:76' # The MAC address of a Bluetooth adapter on the server. The server might have multiple Bluetooth adapters.
    port = 3
    backlog = 1
    size = 1024
    s.bind((hostMACAddress, port))
    s.listen(backlog)
    try:
        client, clientInfo = s.accept()
        print "Connected successfully"
        data = client.recv(size)
        while 1:
            data = client.recv(size)
            if data:
                print "Incoming bluetooth data:"
                print(data)
    except:
        print("Closing socket")
        client.close()
    s.close()



# re plan route


# set off





# Close vehicle object before exiting script
vehicle.close()

# Shut down simulator
sitl.stop()
print("Completed")