iptables -A INPUT -p icmp --icmp-type echo-request -j ACCEPT
iptables -A OUTPUT -p icmp --icmp-type echo-reply -j ACCEPT
iptables -P OUTPUT DROP  # Set default rule for OUTPUT
iptables -P INPUT DROP   # Set default rule for INPUT
