"""
A simple Python script to send and receive messages to a device over Bluetooth
using PyBluez (with Python 2).
"""

import bluetooth

def client():

    serverMACAddress = 'B8:27:EB:FD:16:4B'
    port = 3
    s = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
    s.connect((serverMACAddress, port))
    while 1:
        text = raw_input() # Note change to the old (Python 2) raw_input
        if text == "quit":
            break
        s.send(text)
    s.close()

    #return s


def server():

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
                print(data)
                client.send(data) # Echo back to client
    except:
        print("Closing socket")
        client.close()
    s.close()