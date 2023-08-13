import socket
from scapy.all import *
from scapy.all import SNMP, UDP
# Replace with the path to the PCAP file you want to read
print("Please enter the number of packets: ")
num_of_packets = int(input())
print("Please enter 1 if you want to sleep between packets and 0 otherwise: ")
time_sleep = int(input())

pcap_file = "./pcap-sim/capture26032023_2h.pcap"

# Replace with the IP address and port number of the destination  192.168.24.47  192.168.27.227
dest_ip = "192.168.24.47"
dest_port = 1625
num = 0
# Read the packets from the PCAP file
packets = rdpcap(pcap_file)
last = packets[0].time
# Loop through each packet
for packet in packets:
    x = packet.time - last
    last = packet.time
    if time_sleep == 1:
      time.sleep(x)

    snmp_packet = packet.getlayer(UDP).payload.load
    snmp_data = SNMP(snmp_packet)

    # Extract the raw bytes of the packet
    raw_bytes = bytes(packet)

    # Create a UDP socket
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

    # Send the raw bytes to the destination
    sock.sendto(snmp_packet, (dest_ip, dest_port))

    num += 1
    # Close the socket
    sock.close()

    print("Sent packet number ",num)
    if num == num_of_packets:
      break

print("Total number of packets sent is ",num);
