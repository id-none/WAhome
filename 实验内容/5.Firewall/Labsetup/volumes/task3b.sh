# Outside hosts can only access the telnet server on 192.168.60.5
iptables -A FORWARD -o eth1+ -p tcp --dport 23 -d 192.168.60.5 -j ACCEPT
iptables -A FORWARD -i eth1+ -p tcp --sport 23 -j ACCEPT

# allow TCP packets of connections that have been made
iptables -A FORWARD -o eth1+ -p tcp -m conntrack \
--ctstate ESTABLISHED,RELATED -j ACCEPT
iptables -A FORWARD -i eth1+ -p tcp -m conntrack \
--ctstate ESTABLISHED,RELATED -j ACCEPT

# allow internal hosts to make TCP connection
iptables -A FORWARD -p tcp -i eth1+ --syn \
-m conntrack --ctstate NEW -j ACCEPT

iptables -P FORWARD DROP
