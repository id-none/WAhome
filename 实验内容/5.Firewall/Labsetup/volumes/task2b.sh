# Outside hosts cannot ping internal hosts on eth1(192.168.60.0/24)
iptables -A FORWARD -p icmp --icmp-type echo-request -o eth1+ -j DROP
iptables -A FORWARD -p icmp --icmp-type echo-reply -i eth1+ -j DROP

# Outside hosts can ping the router
iptables -A INPUT -p icmp -j ACCEPT

# Internal hosts in eth1(192.168.60.0/24) can ping outside hosts
iptables -A FORWARD -p icmp --icmp-type echo-request -i eth1+ -j ACCEPT
iptables -A FORWARD -p icmp --icmp-type echo-reply -o eth1+ -j ACCEPT

# block all other packets between internal and external networks
iptables -P FORWARD DROP
