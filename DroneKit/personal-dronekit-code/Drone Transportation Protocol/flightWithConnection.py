# Import DroneKit-Python
from dronekit import connect, VehicleMode, LocationGlobalRelative
import time
import sys
import bluetooth

print "Start simulator (SITL)"
from dronekit_sitl import SITL

# TODO:  Check that there is an internet connection before moving on

#sitl = SITL()
sitl = SITL(path="/home/pi/Documents/ardupilot/ArduCopter/ArduCopter.elf")
#sitl.download('copter', 'stable', verbose=True)
sitl_args = ['-I0', '--model', 'quad',
             '--home=51.01,-3.01,60,180', '--speedup=1']  # Set home location (lat,lng,alt,yaw) to 180 Rhymeny Street

sitl.launch(sitl_args, await_ready=True, restart=True, verbose=True)

# Connect to the Vehicle.
print "Connecting to vehicle on: 'tcp:127.0.0.1:5760'"
vehicle = connect('tcp:127.0.0.1:5760', wait_ready=True)

# Get all vehicle attributes (state)
print "\nGet all vehicle attribute values:"
print " Global Location: %s" % vehicle.location.global_frame
print " Global Location (relative altitude): %s" % vehicle.location.global_relative_frame
print " Local Location: %s" % vehicle.location.local_frame
print " Attitude: %s" % vehicle.attitude
print " Velocity: %s" % vehicle.velocity
print " GPS: %s" % vehicle.gps_0
print " Gimbal status: %s" % vehicle.gimbal
print " Battery: %s" % vehicle.battery
print " EKF OK?: %s" % vehicle.ekf_ok
print " Last Heartbeat: %s" % vehicle.last_heartbeat
print " Rangefinder: %s" % vehicle.rangefinder
print " Rangefinder distance: %s" % vehicle.rangefinder.distance
print " Rangefinder voltage: %s" % vehicle.rangefinder.voltage
print " Heading: %s" % vehicle.heading
print " Is Armable?: %s" % vehicle.is_armable
print " System status: %s" % vehicle.system_status.state
print " Groundspeed: %s" % vehicle.groundspeed  # settable
print " Airspeed: %s" % vehicle.airspeed  # settable
print " Mode: %s" % vehicle.mode.name  # settable
print " Armed: %s" % vehicle.armed  # settable

def arm_and_takeoff(aTargetAltitude):
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

#    # Confirm vehicle armed before attempting to take off
#    while not vehicle.armed:
#        print " Is Armable?: %s" % vehicle.is_armable
#  #      print " Waiting for arming..."
#        vehicle.armed = True
#        time.sleep(5)
#
#    print "Taking off!"
#    vehicle.simple_takeoff(aTargetAltitude) # Take off to target altitude
#
#    # Wait until the vehicle reaches a safe height before processing the goto (otherwise the command
#    #  after Vehicle.simple_takeoff will execute immediately).
#    while True:
#        print " Altitude: ", vehicle.location.global_relative_frame.alt
#        #Break and return from function just below target altitude.
#        if vehicle.location.global_relative_frame.alt>=aTargetAltitude*0.95:
#            print "Reached target altitude"
#            break
#        time.sleep(1)




# Get Vehicle Home location - will be `None` until first set by autopilot
while not vehicle.home_location:
    cmds = vehicle.commands
    cmds.download()
    cmds.wait_ready()
    if not vehicle.home_location:
        print " Waiting for home location ..."
# We have a home location, so print it!
print "\n Home location: %s" % vehicle.home_location


# Have a string that is used to store the log of the drone flight
strFileOut = ''
start = time.time()


def addLogEntry():
    strFileOut.join(string(vehicle.location.global_frame.lat) + "\t" + string(vehicle.location.global_frame.lon) + "\t" + string(vehicle.location.global_frame.alt) + "\t" + string(time.time() - start + "\n"))

#addLogEntry()

print " Global Location: %s" % vehicle.location.global_frame
print " Global Location (relative altitude): %s" % vehicle.location.global_relative_frame
print " Local Location: %s" % vehicle.location.local_frame

print " Setting ARMING_CHECK to 0"
ARMING_CHECK = 0
RC3_MIN = 1101

# Arm and takeoff vehicle
takeOffAltitude = 20 # in meters
arm_and_takeoff(takeOffAltitude)


print " Connecting via Bluetooth"
s = bluetooth.BluetoothSocket(bluetooth.RFCOMM)

if sys.argv[0] == "client":
    serverMACAddress = 'B8:27:EB:FD:16:4B'
    port = 3
    s.connect((serverMACAddress, port))

else:
    hostMACAddress = 'B8:27:EB:FD:16:4B' # The MAC address of a Bluetooth adapter on the server. The server might have multiple Bluetooth adapters.
    port = 3
    backlog = 1
    size = 1024
    s.bind((hostMACAddress, port))
    s.listen(backlog)
    try:
        client, clientInfo = s.accept()
        print "Connected successfully"
        while 1:
            data = client.recv(size)
            if data:
                print(data)
                client.send(data) # Echo back to client
    except:
        print("Closing socket")
        client.close()
    s.close()


s.send(" Global Location: %s" % vehicle.location.global_frame)
s.send(" Global Location (relative altitude): %s" % vehicle.location.global_relative_frame)
s.send(" Local Location: %s" % vehicle.location.local_frame)

s.send("\nGet all vehicle attribute values:")
s.send(" Global Location: %s" % vehicle.location.global_frame)
s.send(" Global Location (relative altitude): %s" % vehicle.location.global_relative_frame)
s.send(" Local Location: %s" % vehicle.location.local_frame)
s.send(" Attitude: %s" % vehicle.attitude)
s.send(" Velocity: %s" % vehicle.velocity)
s.send(" GPS: %s" % vehicle.gps_0)
s.send(" Gimbal status: %s" % vehicle.gimbal)
s.send(" Battery: %s" % vehicle.battery)
s.send(" EKF OK?: %s" % vehicle.ekf_ok)
s.send(" Last Heartbeat: %s" % vehicle.last_heartbeat)
s.send(" Rangefinder: %s" % vehicle.rangefinder)
s.send(" Rangefinder distance: %s" % vehicle.rangefinder.distance)
s.send(" Rangefinder voltage: %s" % vehicle.rangefinder.voltage)
s.send(" Heading: %s" % vehicle.heading)
s.send(" Is Armable?: %s" % vehicle.is_armable)
s.send(" System status: %s" % vehicle.system_status.state)
s.send(" Groundspeed: %s" % vehicle.groundspeed)  # settable)
s.send(" Airspeed: %s" % vehicle.airspeed)  # settable)
s.send(" Mode: %s" % vehicle.mode.name)  # settable)
s.send(" Armed: %s" % vehicle.armed)  # settables.send()

s.close()

# Close vehicle object before exiting script
vehicle.close()

# Shut down simulator
sitl.stop()
print("Completed")