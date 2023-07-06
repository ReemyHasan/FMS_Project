import argparse

from scapy.all import *
from scapy.layers.inet import IP, UDP


class SNMPTrapGenerator(object):
    def __init__(self, infile, dst_ip, src_ip=None):
        try:
            self.rd = rdpcap(infile)
            self.dst_ip = dst_ip
            self.src_ip = src_ip
        except IOError:
            print("Failed reading file %s contents" % infile)
            sys.exit(1)

    def send(self, count=100):
        pkt_cnt = 0
        p_out = []

        for p in self.rd:
            pkt_cnt += 1
            np = p.getlayer(UDP).payload.load
            if IP in np and UDP in np:
                np[IP].dst = self.dst_ip
                if self.src_ip is not None:
                    np[IP].src = self.src_ip
                print(np)
                p_out.append(np)
                if pkt_cnt % count == 0:
                    send(PacketList(p_out))
                    p_out = []
                break

        # Send remianing in final batch
        #send(PacketList(p_out))
        print("Total packets sent %d" % pkt_cnt)


def main():
    # parser = argparse.ArgumentParser(prog="snmptrapgen", add_help=True)
    # parser.add_argument("-f", "--file", type=str,
    #                     required=True, help="Specify .pcap file.")
    # parser.add_argument("-d", "--dst_ip", type=str,
    #                     required=True, help="Sepcify destination ip address.")
    # parser.add_argument("-s", "--src_ip", type=str,
    #                     required=False, help="Specify source ip address.p")
    # args = parser.parse_args()

    pcap_file = ".\pcap-sim\capture26032023_2h.pcap"

    # Replace with the IP address and port number of the destination
    dest_ip = "127.0.0.1"
    dest_port = 1625

    stg = SNMPTrapGenerator(pcap_file, dest_ip,"127.0.0.1")
    stg.send(1)


if __name__ == "__main__":
    main()
