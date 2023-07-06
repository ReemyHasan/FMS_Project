import socket
from scapy.all import *
from scapy.all import SNMP, UDP
# Replace with the path to the PCAP file you want to read

pcap_file = ".\pcap-sim\capture26032023_2h.pcap"

# Replace with the IP address and port number of the destination
dest_ip = "127.0.0.1"
dest_port = 1625
num = 0
# Read the packets from the PCAP file
packets = rdpcap(pcap_file)
last = 1679781009.668890
# Loop through each packet
for packet in packets:
    x = packet.time - last
    last = packet.time
    #print(x)
    #time.sleep(x)
    # Check if the packet is an SNMP packet
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
print("Total number of packets sent is ",num);