# packets going to the 192.168.60.0/24 network should be routed to the TUN interface

# No need to execute if using client2+

ip route del 192.168.60.0/24
ip route add 192.168.60.0/24 dev tun0 # via 192.168.53.99

# To router directly, then VPN is of no use
# ip route add 192.168.60.0/24 via 10.9.0.11
