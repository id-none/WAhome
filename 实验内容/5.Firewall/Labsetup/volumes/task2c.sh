# Outside hosts can only access the telnet server on 192.168.60.5
iptables -A FORWARD -o eth1+ -p tcp --dport 23 -d 192.168.60.5 -j ACCEPT
iptables -A FORWARD -i eth1+ -p tcp --sport 23 -j ACCEPT

iptables -P FORWARD DROP
