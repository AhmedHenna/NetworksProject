# NetworksProject

## General
- In the project we tested over multiple networks, but only 4 networks are available in this project as a preset. You can view them below in this file. Of course you can create your own netowrk and it can be adjusted as you would like with as many routers, switches and clients. Which means it can adjust to any network topology 
  - Keep in mind that evrey router only has 1 switch connected to it. 
  - Evrey switch can have as many devices connected to it (clients, switches), but only 1 router
- You can follow the examples provided in code to check how you can add the different devices

## Technical Limitations

- All clients are directly connected to their default gateway (routers)
  - They of course should have been broadcasting an ARP request to get to their respective router's MAC Address and then sent everything through the connected switch, but this was not implemented as we realized this problem at the very end

- Events are taken from Queue one at a time, which isnt an accurate representation of a netowkr
  - However, processing of an event is not single threaded, the queue is the only single threaded aspect

- The window size changes randomaly based on a random number to simplify the matter

- Checksum validation step is based on a random generated value, but it does not use the actual implementation
  - The actual implemetnation is commented out for easier testing
  
- Dropping of packet segments during network transfer is also based on a random generated value which can be adjusted

- New packet sending takes priorty over resending packet
  - So sending consumes the window size, and the remiaing is given to the resending process
 
- When recieivng an out of order data segement, the reciever ignores this segement and the old ack number is resent, compared to a real network which should maintian all receieved packets even if out of order
  
- Resending always occur after a timeout, so there is no immediate resend on failed checksum or packet drop

- A simplified version congestion control was implemented due to time constraints on the project delievery time



## Example 1
This example is just a very simple and easy network. Where we only have 1 switch and 2 clients. This is LAN only network, so no need for a router.

<p align="center">
<img src="https://user-images.githubusercontent.com/71960514/236643455-702c69c7-b7fe-49d8-8ef8-f19735575d38.png" width=400/>

</p>

<details>
  <summary>Output</summary>
  
 ````
 
| 21:29:02.198 | Client A   | Sent     | arp.ArpRequestEvent            | To   | Switch A   |
| 21:29:02.263 | Switch A   | Received | arp.ArpRequestEvent            | From | Client A   |
| 21:29:02.263 | Switch A   | Sent     | arp.ArpRequestEvent            | To   | Client B   |
| 21:29:02.482 | Client B   | Received | arp.ArpRequestEvent            | From | Switch A   |
| 21:29:02.543 | Client B   | Sent     | arp.ArpResponseEvent           | To   | Switch A   |
| 21:29:02.685 | Switch A   | Received | arp.ArpResponseEvent           | From | Client B   |
| 21:29:02.746 | Switch A   | Sent     | arp.ArpResponseEvent           | To   | Client A   |
| 21:29:02.886 | Client A   | Received | arp.ArpResponseEvent           | From | Switch A   |
| 21:29:02.949 | Client A   | Sent     | tcp.TcpSynEvent                | To   | Switch A   |
| 21:29:03.089 | Switch A   | Received | tcp.TcpSynEvent                | From | Client A   |
| 21:29:03.140 | Switch A   | Sent     | tcp.TcpSynEvent                | To   | Client B   |
| 21:29:03.294 | Client B   | Received | tcp.TcpSynEvent                | From | Switch A   |
| 21:29:03.357 | Client B   | Sent     | tcp.TcpSynAckEvent             | To   | Switch A   |
| 21:29:03.501 | Switch A   | Received | tcp.TcpSynAckEvent             | From | Client B   |
| 21:29:03.563 | Switch A   | Sent     | tcp.TcpSynAckEvent             | To   | Client A   |
| 21:29:03.703 | Client A   | Received | tcp.TcpSynAckEvent             | From | Switch A   |
| 21:29:03.767 | Client A   | Sent     | tcp.TcpAckEvent                | To   | Switch A   |
| 21:29:03.830 | Client A   | Sent     | tcp.TcpSendDataEvent           | To   | Switch A   |
| 21:29:03.894 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:1   | DATA: Just testing ou  |
| 21:29:03.956 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:16  | DATA: t this long mes  |
| 21:29:04.019 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:31  | DATA: sage to make su  |
| 21:29:04.115 | Switch A   | Received | tcp.TcpAckEvent                | From | Client A   |
| 21:29:04.179 | Switch A   | Sent     | tcp.TcpAckEvent                | To   | Client B   |
| 21:29:04.322 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:1   | DATA: Just testing ou  |
| 21:29:04.385 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:1   | DATA: Just testing ou  |
| 21:29:04.527 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:16  | DATA: t this long mes  |
| 21:29:04.590 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:16  | DATA: t this long mes  |
| 21:29:04.731 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:31  | DATA: sage to make su  |
| 21:29:04.794 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:31  | DATA: sage to make su  |
| 21:29:04.935 | Client B   | Received | tcp.TcpAckEvent                | From | Switch A   |
| 21:29:05.142 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:1   | DATA: Just testing ou  |
| 21:29:05.205 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:16  |
| 21:29:05.349 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:16  | DATA: t this long mes  |
| 21:29:05.413 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:29:05.558 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:31  | DATA: sage to make su  |
| 21:29:05.622 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:46  |
| 21:29:05.765 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:16  |
| 21:29:05.829 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:16  |
| 21:29:05.969 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:31  |
| 21:29:06.032 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:29:06.174 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:46  |
| 21:29:06.237 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:46  |
| 21:29:06.379 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:16  |
| 21:29:06.380 | Client A   | Received new window size for | Client B | 4 |
| 21:29:06.443 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:46  | DATA: re that evreyth  |
| 21:29:06.506 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:61  | DATA: ing works        |
| 21:29:06.585 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:29:06.585 | Client A   | Received new window size for | Client B | 2 |
| 21:29:06.792 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:46  |
| 21:29:06.792 | Client A   | Received new window size for | Client B | 2 |
| 21:29:06.996 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:46  | DATA: re that evreyth  |
| 21:29:07.059 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:46  | DATA: re that evreyth  |
| 21:29:07.203 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:61  | DATA: ing works        |
| 21:29:07.265 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:61  | DATA: ing works        |
| 21:29:07.409 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:46  | DATA: re that evreyth  |
| 21:29:07.473 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:61  |
| 21:29:07.616 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:61  | DATA: ing works        |
| 21:29:07.679 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:70  |
| 21:29:07.822 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:61  |
| 21:29:07.883 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:61  |
| 21:29:08.026 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:70  |
| 21:29:08.089 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:70  |
| 21:29:08.230 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:61  |
| 21:29:08.230 | Client A   | Received new window size for | Client B | 4 |
| 21:29:08.436 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:70  |
| 21:29:08.436 | Client A   | Received new window size for | Client B | 3 |
| 21:29:08.500 | Client A   | Sent     | tcp.TcpFinEvent                | To   | Switch A   |
| 21:29:08.642 | Switch A   | Received | tcp.TcpFinEvent                | From | Client A   |
| 21:29:08.705 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Client B   |
| 21:29:08.850 | Client B   | Received | tcp.TcpFinEvent                | From | Switch A   |
| 21:29:08.912 | Client B   | Sent     | tcp.TcpFinAckEvent             | To   | Switch A   |
| 21:29:08.977 | Client B   | Sent     | tcp.TcpFinEvent                | To   | Switch A   |
| 21:29:09.056 | Switch A   | Received | tcp.TcpFinAckEvent             | From | Client B   |
| 21:29:09.121 | Switch A   | Sent     | tcp.TcpFinAckEvent             | To   | Client A   |
| 21:29:09.262 | Switch A   | Received | tcp.TcpFinEvent                | From | Client B   |
| 21:29:09.324 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Client A   |
| 21:29:09.468 | Client A   | Received | tcp.TcpFinAckEvent             | From | Switch A   |
| 21:29:09.672 | Client A   | Received | tcp.TcpFinEvent                | From | Switch A   |
| 21:29:09.735 | Client A   | Sent     | tcp.TcpFinAckEvent             | To   | Switch A   |
| 21:29:09.797 | Client A   | Sent     | tcp.TcpFinEvent                | To   | Switch A   |
| 21:29:09.875 | Switch A   | Received | tcp.TcpFinAckEvent             | From | Client A   |
| 21:29:09.939 | Switch A   | Sent     | tcp.TcpFinAckEvent             | To   | Client B   |
| 21:29:10.083 | Switch A   | Received | tcp.TcpFinEvent                | From | Client A   |
| 21:29:10.146 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Client B   |
| 21:29:10.287 | Client B   | Received | tcp.TcpFinAckEvent             | From | Switch A   |
| 21:29:10.288 | Client B   | Received Data: Just testing out this long message to make sure that evreything works |
| 21:29:10.491 | Client B   | Received | tcp.TcpFinEvent                | From | Switch A   |

  ````
</details>


## Example 2
For example 2 we decided to make it a bit more complex by making each of the clients on their own different network (router).

<p align="center">
<img src="https://user-images.githubusercontent.com/71960514/236643852-0ebaba4c-54a5-4688-865b-a4894186f052.png" width=400/>

</p>
<details>
  <summary>Output</summary>
  
  ````
| 21:27:00.131 | Client A   | Sent     | tcp.TcpSynEvent                | To   | Router A   |
| 21:27:00.193 | Router A   | Received | tcp.TcpSynEvent                | From | Client A   |
| 21:27:00.256 | Router A   | Sent     | tcp.TcpSynEvent                | To   | Router B   |
| 21:27:00.414 | Router B   | Received | tcp.TcpSynEvent                | From | Router A   |
| 21:27:00.477 | Router B   | Sent     | tcp.TcpSynEvent                | To   | Switch B   |
| 21:27:00.620 | Switch B   | Received | tcp.TcpSynEvent                | From | Router B   |
| 21:27:00.682 | Switch B   | Sent     | tcp.TcpSynEvent                | To   | Client B   |
| 21:27:00.821 | Client B   | Received | tcp.TcpSynEvent                | From | Switch B   |
| 21:27:00.882 | Client B   | Sent     | tcp.TcpSynAckEvent             | To   | Router B   |
| 21:27:01.037 | Router B   | Received | tcp.TcpSynAckEvent             | From | Client B   |
| 21:27:01.100 | Router B   | Sent     | tcp.TcpSynAckEvent             | To   | Router A   |
| 21:27:01.244 | Router A   | Received | tcp.TcpSynAckEvent             | From | Router B   |
| 21:27:01.308 | Router A   | Sent     | tcp.TcpSynAckEvent             | To   | Switch A   |
| 21:27:01.449 | Switch A   | Received | tcp.TcpSynAckEvent             | From | Router A   |
| 21:27:01.513 | Switch A   | Sent     | tcp.TcpSynAckEvent             | To   | Client A   |
| 21:27:01.658 | Client A   | Received | tcp.TcpSynAckEvent             | From | Switch A   |
| 21:27:01.722 | Client A   | Sent     | tcp.TcpAckEvent                | To   | Router A   |
| 21:27:01.787 | Client A   | Sent     | tcp.TcpSendDataEvent           | To   | Router A   |
| 21:27:01.854 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:1   | DATA: This is some da  |
| 21:27:01.915 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:16  | DATA: ta that I am se  |
| 21:27:01.979 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:31  | DATA: nding through t  |
| 21:27:02.075 | Router A   | Received | tcp.TcpAckEvent                | From | Client A   |
| 21:27:02.138 | Router A   | Sent     | tcp.TcpAckEvent                | To   | Router B   |
| 21:27:02.277 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:1   | DATA: This is some da  |
| 21:27:02.342 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:1   | DATA: This is some da  |
| 21:27:02.486 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:16  | DATA: ta that I am se  |
| 21:27:02.550 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:16  | DATA: ta that I am se  |
| 21:27:02.693 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:31  | DATA: nding through t  |
| 21:27:02.755 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:31  | DATA: nding through t  |
| 21:27:02.898 | Router B   | Received | tcp.TcpAckEvent                | From | Router A   |
| 21:27:02.962 | Router B   | Sent     | tcp.TcpAckEvent                | To   | Switch B   |
| 21:27:03.106 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:1   | DATA: This is some da  |
| 21:27:03.169 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:1   | DATA: This is some da  |
| 21:27:03.313 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:16  | DATA: ta that I am se  |
| 21:27:03.375 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:16  | DATA: ta that I am se  |
| 21:27:03.517 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:31  | DATA: nding through t  |
| 21:27:03.581 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:31  | DATA: nding through t  |
| 21:27:03.724 | Switch B   | Received | tcp.TcpAckEvent                | From | Router B   |
| 21:27:03.788 | Switch B   | Sent     | tcp.TcpAckEvent                | To   | Client B   |
| 21:27:03.929 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:1   | DATA: This is some da  |
| 21:27:03.993 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:1   | DATA: This is some da  |
| 21:27:04.135 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:16  | DATA: ta that I am se  |
| 21:27:04.199 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:16  | DATA: ta that I am se  |
| 21:27:04.341 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:31  | DATA: nding through t  |
| 21:27:04.404 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:31  | DATA: nding through t  |
| 21:27:04.547 | Client B   | Received | tcp.TcpAckEvent                | From | Switch B   |
| 21:27:04.753 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:1   | DATA: This is some da  |
| 21:27:04.818 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:16  |
| 21:27:04.963 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:16  | DATA: ta that I am se  |
| 21:27:05.026 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:31  |
| 21:27:05.173 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:31  | DATA: nding through t  |
| 21:27:05.173 | Client B   | Simulated segment dropped, not acknowledging |
| 21:27:05.382 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:16  |
| 21:27:05.445 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:16  |
| 21:27:05.591 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:31  |
| 21:27:05.653 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:31  |
| 21:27:05.798 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:16  |
| 21:27:05.862 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:16  |
| 21:27:06.005 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:31  |
| 21:27:06.067 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:27:06.213 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:16  |
| 21:27:06.275 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:16  |
| 21:27:06.418 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:31  |
| 21:27:06.482 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:27:06.629 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:16  |
| 21:27:06.630 | Client A   | Received new window size for | Client B | 2 |
| 21:27:06.690 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:46  | DATA: his very compli  |
| 21:27:06.755 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:61  | DATA: cated network s  |
| 21:27:06.834 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:27:06.834 | Client A   | Received new window size for | Client B | 4 |
| 21:27:06.897 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:76  | DATA: imulation that   |
| 21:27:06.961 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:91  | DATA: I made           |
| 21:27:07.040 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:46  | DATA: his very compli  |
| 21:27:07.103 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:46  | DATA: his very compli  |
| 21:27:07.242 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:61  | DATA: cated network s  |
| 21:27:07.306 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:61  | DATA: cated network s  |
| 21:27:07.444 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:76  | DATA: imulation that   |
| 21:27:07.507 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:76  | DATA: imulation that   |
| 21:27:07.649 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:91  | DATA: I made           |
| 21:27:07.710 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:91  | DATA: I made           |
| 21:27:07.850 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:46  | DATA: his very compli  |
| 21:27:07.913 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:46  | DATA: his very compli  |
| 21:27:08.055 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:61  | DATA: cated network s  |
| 21:27:08.118 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:61  | DATA: cated network s  |
| 21:27:08.260 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:76  | DATA: imulation that   |
| 21:27:08.323 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:76  | DATA: imulation that   |
| 21:27:08.470 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:91  | DATA: I made           |
| 21:27:08.533 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:91  | DATA: I made           |
| 21:27:08.677 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:46  | DATA: his very compli  |
| 21:27:08.741 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:46  | DATA: his very compli  |
| 21:27:08.883 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:61  | DATA: cated network s  |
| 21:27:08.945 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:61  | DATA: cated network s  |
| 21:27:09.088 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:76  | DATA: imulation that   |
| 21:27:09.151 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:76  | DATA: imulation that   |
| 21:27:09.292 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:91  | DATA: I made           |
| 21:27:09.355 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:91  | DATA: I made           |
| 21:27:09.496 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:46  | DATA: his very compli  |
| 21:27:09.559 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:31  |
| 21:27:09.702 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:61  | DATA: cated network s  |
| 21:27:09.763 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:31  |
| 21:27:09.906 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:76  | DATA: imulation that   |
| 21:27:09.969 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:31  |
| 21:27:10.109 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:91  | DATA: I made           |
| 21:27:10.171 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:31  |
| 21:27:10.312 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:31  |
| 21:27:10.376 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:31  |
| 21:27:10.516 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:31  |
| 21:27:10.580 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:31  |
| 21:27:10.721 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:31  |
| 21:27:10.784 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:31  |
| 21:27:10.929 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:31  |
| 21:27:10.994 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:31  |
| 21:27:11.137 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:31  |
| 21:27:11.202 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:27:11.346 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:31  |
| 21:27:11.412 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:27:11.555 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:31  |
| 21:27:11.619 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:27:11.761 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:31  |
| 21:27:11.825 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:27:11.968 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:31  |
| 21:27:12.033 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:27:12.174 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:31  |
| 21:27:12.238 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:27:12.379 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:31  |
| 21:27:12.441 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:27:12.583 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:31  |
| 21:27:12.647 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:27:12.789 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:27:12.789 | Client A   | Received new window size for | Client B | 1 |
| 21:27:12.995 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:27:12.995 | Client A   | Received new window size for | Client B | 4 |
| 21:27:13.200 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:27:13.200 | Client A   | Received new window size for | Client B | 3 |
| 21:27:13.402 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:27:13.402 | Client A   | Received new window size for | Client B | 3 |
| 21:27:17.138 | Client A   | Resending event |
| 21:27:17.202 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:31  | DATA: nding through t  |
| 21:27:17.202 | Client A   | Resending event |
| 21:27:17.267 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:46  | DATA: his very compli  |
| 21:27:17.267 | Client A   | Resending event |
| 21:27:17.331 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:61  | DATA: cated network s  |
| 21:27:17.347 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:31  | DATA: nding through t  |
| 21:27:17.409 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:31  | DATA: nding through t  |
| 21:27:17.555 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:46  | DATA: his very compli  |
| 21:27:17.619 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:46  | DATA: his very compli  |
| 21:27:17.765 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:61  | DATA: cated network s  |
| 21:27:17.827 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:61  | DATA: cated network s  |
| 21:27:17.972 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:31  | DATA: nding through t  |
| 21:27:18.034 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:31  | DATA: nding through t  |
| 21:27:18.181 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:46  | DATA: his very compli  |
| 21:27:18.244 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:46  | DATA: his very compli  |
| 21:27:18.340 | Client A   | Resending event |
| 21:27:18.386 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:61  | DATA: cated network s  |
| 21:27:18.402 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:76  | DATA: imulation that   |
| 21:27:18.402 | Client A   | Resending event |
| 21:27:18.449 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:61  | DATA: cated network s  |
| 21:27:18.465 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:91  | DATA: I made           |
| 21:27:18.591 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:31  | DATA: nding through t  |
| 21:27:18.651 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:31  | DATA: nding through t  |
| 21:27:18.791 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:46  | DATA: his very compli  |
| 21:27:18.853 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:46  | DATA: his very compli  |
| 21:27:18.997 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:76  | DATA: imulation that   |
| 21:27:19.060 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:76  | DATA: imulation that   |
| 21:27:19.201 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:61  | DATA: cated network s  |
| 21:27:19.265 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:61  | DATA: cated network s  |
| 21:27:19.407 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:91  | DATA: I made           |
| 21:27:19.468 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:91  | DATA: I made           |
| 21:27:19.607 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:31  | DATA: nding through t  |
| 21:27:19.607 | Client B   | Invalid checksum detected |
| 21:27:19.670 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:31  |
| 21:27:19.809 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:46  | DATA: his very compli  |
| 21:27:19.869 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:31  |
| 21:27:20.011 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:76  | DATA: imulation that   |
| 21:27:20.075 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:76  | DATA: imulation that   |
| 21:27:20.216 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:61  | DATA: cated network s  |
| 21:27:20.216 | Client B   | Invalid checksum detected |
| 21:27:20.279 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:31  |
| 21:27:20.419 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:91  | DATA: I made           |
| 21:27:20.483 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:91  | DATA: I made           |
| 21:27:20.621 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:31  |
| 21:27:20.683 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:31  |
| 21:27:20.824 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:31  |
| 21:27:20.887 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:31  |
| 21:27:21.027 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:76  | DATA: imulation that   |
| 21:27:21.089 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:76  | DATA: imulation that   |
| 21:27:21.230 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:31  |
| 21:27:21.293 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:31  |
| 21:27:21.436 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:91  | DATA: I made           |
| 21:27:21.500 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:91  | DATA: I made           |
| 21:27:21.639 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:31  |
| 21:27:21.701 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:27:21.844 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:31  |
| 21:27:21.908 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:27:22.048 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:76  | DATA: imulation that   |
| 21:27:22.113 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:31  |
| 21:27:22.254 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:31  |
| 21:27:22.319 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:27:22.459 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:91  | DATA: I made           |
| 21:27:22.521 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:31  |
| 21:27:22.663 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:31  |
| 21:27:22.727 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:27:22.867 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:31  |
| 21:27:22.928 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:27:23.071 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:31  |
| 21:27:23.133 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:31  |
| 21:27:23.277 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:31  |
| 21:27:23.339 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:27:23.481 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:31  |
| 21:27:23.545 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:31  |
| 21:27:23.685 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:27:23.685 | Client A   | Received new window size for | Client B | 1 |
| 21:27:23.888 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:27:23.888 | Client A   | Received new window size for | Client B | 2 |
| 21:27:24.094 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:31  |
| 21:27:24.157 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:27:24.298 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:27:24.298 | Client A   | Received new window size for | Client B | 2 |
| 21:27:24.501 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:31  |
| 21:27:24.564 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:27:24.703 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:31  |
| 21:27:24.765 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:27:24.905 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:31  |
| 21:27:24.965 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:27:25.123 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:27:25.123 | Client A   | Received new window size for | Client B | 2 |
| 21:27:25.330 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:27:25.330 | Client A   | Received new window size for | Client B | 3 |
| 21:27:32.552 | Client A   | Resending event |
| 21:27:32.615 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:31  | DATA: nding through t  |
| 21:27:32.615 | Client A   | Resending event |
| 21:27:32.677 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:46  | DATA: his very compli  |
| 21:27:32.677 | Client A   | Resending event |
| 21:27:32.693 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:31  | DATA: nding through t  |
| 21:27:32.739 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:61  | DATA: cated network s  |
| 21:27:32.755 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:31  | DATA: nding through t  |
| 21:27:32.896 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:46  | DATA: his very compli  |
| 21:27:32.959 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:46  | DATA: his very compli  |
| 21:27:33.102 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:61  | DATA: cated network s  |
| 21:27:33.165 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:61  | DATA: cated network s  |
| 21:27:33.307 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:31  | DATA: nding through t  |
| 21:27:33.370 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:31  | DATA: nding through t  |
| 21:27:33.512 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:46  | DATA: his very compli  |
| 21:27:33.575 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:46  | DATA: his very compli  |
| 21:27:33.714 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:61  | DATA: cated network s  |
| 21:27:33.746 | Client A   | Resending event |
| 21:27:33.777 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:61  | DATA: cated network s  |
| 21:27:33.808 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:76  | DATA: imulation that   |
| 21:27:33.808 | Client A   | Resending event |
| 21:27:33.869 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:91  | DATA: I made           |
| 21:27:33.916 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:31  | DATA: nding through t  |
| 21:27:33.977 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:31  | DATA: nding through t  |
| 21:27:34.118 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:46  | DATA: his very compli  |
| 21:27:34.180 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:46  | DATA: his very compli  |
| 21:27:34.318 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:61  | DATA: cated network s  |
| 21:27:34.379 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:61  | DATA: cated network s  |
| 21:27:34.535 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:76  | DATA: imulation that   |
| 21:27:34.599 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:76  | DATA: imulation that   |
| 21:27:34.740 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:91  | DATA: I made           |
| 21:27:34.806 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:91  | DATA: I made           |
| 21:27:34.947 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:31  | DATA: nding through t  |
| 21:27:35.011 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:46  |
| 21:27:35.152 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:46  | DATA: his very compli  |
| 21:27:35.215 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:61  |
| 21:27:35.356 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:61  | DATA: cated network s  |
| 21:27:35.420 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:76  |
| 21:27:35.560 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:76  | DATA: imulation that   |
| 21:27:35.624 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:76  | DATA: imulation that   |
| 21:27:35.764 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:91  | DATA: I made           |
| 21:27:35.827 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:91  | DATA: I made           |
| 21:27:35.968 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:46  |
| 21:27:36.032 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:46  |
| 21:27:36.176 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:61  |
| 21:27:36.238 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:61  |
| 21:27:36.381 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:76  |
| 21:27:36.443 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:76  |
| 21:27:36.584 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:76  | DATA: imulation that   |
| 21:27:36.648 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:76  | DATA: imulation that   |
| 21:27:36.789 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:91  | DATA: I made           |
| 21:27:36.852 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:91  | DATA: I made           |
| 21:27:36.993 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:46  |
| 21:27:37.056 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:46  |
| 21:27:37.196 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:61  |
| 21:27:37.259 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:61  |
| 21:27:37.398 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:76  |
| 21:27:37.460 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:76  |
| 21:27:37.598 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:76  | DATA: imulation that   |
| 21:27:37.659 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:91  |
| 21:27:37.801 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:91  | DATA: I made           |
| 21:27:37.801 | Client B   | Simulated segment dropped, not acknowledging |
| 21:27:38.005 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:46  |
| 21:27:38.069 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:46  |
| 21:27:38.211 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:61  |
| 21:27:38.273 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:61  |
| 21:27:38.412 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:76  |
| 21:27:38.476 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:76  |
| 21:27:38.615 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:91  |
| 21:27:38.677 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:91  |
| 21:27:38.818 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:46  |
| 21:27:38.818 | Client A   | Received new window size for | Client B | 3 |
| 21:27:39.025 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:61  |
| 21:27:39.025 | Client A   | Received new window size for | Client B | 3 |
| 21:27:39.227 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:76  |
| 21:27:39.227 | Client A   | Received new window size for | Client B | 2 |
| 21:27:39.430 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:91  |
| 21:27:39.494 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:91  |
| 21:27:39.631 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:91  |
| 21:27:39.694 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:91  |
| 21:27:39.838 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:91  |
| 21:27:39.838 | Client A   | Received new window size for | Client B | 2 |
| 21:27:48.990 | Client A   | Resending event |
| 21:27:49.053 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:91  | DATA: I made           |
| 21:27:49.207 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:91  | DATA: I made           |
| 21:27:49.270 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:91  | DATA: I made           |
| 21:27:49.413 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:91  | DATA: I made           |
| 21:27:49.476 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:91  | DATA: I made           |
| 21:27:49.613 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:91  | DATA: I made           |
| 21:27:49.675 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:91  | DATA: I made           |
| 21:27:49.817 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:91  | DATA: I made           |
| 21:27:49.879 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:97  |
| 21:27:50.017 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:97  |
| 21:27:50.079 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:97  |
| 21:27:50.232 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:97  |
| 21:27:50.293 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:97  |
| 21:27:50.433 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:97  |
| 21:27:50.495 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:97  |
| 21:27:50.636 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:97  |
| 21:27:50.636 | Client A   | Received new window size for | Client B | 2 |
| 21:27:50.698 | Client A   | Sent     | tcp.TcpFinEvent                | To   | Router A   |
| 21:27:50.836 | Router A   | Received | tcp.TcpFinEvent                | From | Client A   |
| 21:27:50.898 | Router A   | Sent     | tcp.TcpFinEvent                | To   | Router B   |
| 21:27:51.040 | Router B   | Received | tcp.TcpFinEvent                | From | Router A   |
| 21:27:51.101 | Router B   | Sent     | tcp.TcpFinEvent                | To   | Switch B   |
| 21:27:51.241 | Switch B   | Received | tcp.TcpFinEvent                | From | Router B   |
| 21:27:51.303 | Switch B   | Sent     | tcp.TcpFinEvent                | To   | Client B   |
| 21:27:51.442 | Client B   | Received | tcp.TcpFinEvent                | From | Switch B   |
| 21:27:51.506 | Client B   | Sent     | tcp.TcpFinAckEvent             | To   | Router B   |
| 21:27:51.567 | Client B   | Sent     | tcp.TcpFinEvent                | To   | Router B   |
| 21:27:51.644 | Router B   | Received | tcp.TcpFinAckEvent             | From | Client B   |
| 21:27:51.707 | Router B   | Sent     | tcp.TcpFinAckEvent             | To   | Router A   |
| 21:27:51.847 | Router B   | Received | tcp.TcpFinEvent                | From | Client B   |
| 21:27:51.910 | Router B   | Sent     | tcp.TcpFinEvent                | To   | Router A   |
| 21:27:52.052 | Router A   | Received | tcp.TcpFinAckEvent             | From | Router B   |
| 21:27:52.115 | Router A   | Sent     | tcp.TcpFinAckEvent             | To   | Switch A   |
| 21:27:52.253 | Router A   | Received | tcp.TcpFinEvent                | From | Router B   |
| 21:27:52.315 | Router A   | Sent     | tcp.TcpFinEvent                | To   | Switch A   |
| 21:27:52.455 | Switch A   | Received | tcp.TcpFinAckEvent             | From | Router A   |
| 21:27:52.519 | Switch A   | Sent     | tcp.TcpFinAckEvent             | To   | Client A   |
| 21:27:52.659 | Switch A   | Received | tcp.TcpFinEvent                | From | Router A   |
| 21:27:52.721 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Client A   |
| 21:27:52.861 | Client A   | Received | tcp.TcpFinAckEvent             | From | Switch A   |
| 21:27:53.064 | Client A   | Received | tcp.TcpFinEvent                | From | Switch A   |
| 21:27:53.126 | Client A   | Sent     | tcp.TcpFinAckEvent             | To   | Router A   |
| 21:27:53.189 | Client A   | Sent     | tcp.TcpFinEvent                | To   | Router A   |
| 21:27:53.265 | Router A   | Received | tcp.TcpFinAckEvent             | From | Client A   |
| 21:27:53.326 | Router A   | Sent     | tcp.TcpFinAckEvent             | To   | Router B   |
| 21:27:53.467 | Router A   | Received | tcp.TcpFinEvent                | From | Client A   |
| 21:27:53.530 | Router A   | Sent     | tcp.TcpFinEvent                | To   | Router B   |
| 21:27:53.670 | Router B   | Received | tcp.TcpFinAckEvent             | From | Router A   |
| 21:27:53.732 | Router B   | Sent     | tcp.TcpFinAckEvent             | To   | Switch B   |
| 21:27:53.874 | Router B   | Received | tcp.TcpFinEvent                | From | Router A   |
| 21:27:53.937 | Router B   | Sent     | tcp.TcpFinEvent                | To   | Switch B   |
| 21:27:54.078 | Switch B   | Received | tcp.TcpFinAckEvent             | From | Router B   |
| 21:27:54.141 | Switch B   | Sent     | tcp.TcpFinAckEvent             | To   | Client B   |
| 21:27:54.282 | Switch B   | Received | tcp.TcpFinEvent                | From | Router B   |
| 21:27:54.345 | Switch B   | Sent     | tcp.TcpFinEvent                | To   | Client B   |
| 21:27:54.484 | Client B   | Received | tcp.TcpFinAckEvent             | From | Switch B   |
| 21:27:54.484 | Client B   | Received Data: This is some data that I am sending through this very complicated network simulation that I made |
| 21:27:54.684 | Client B   | Received | tcp.TcpFinEvent                | From | Switch B   |

  
  ````
 
</details>

## Example 3

For this example we wanted to make an even more complex netowrk with multiple diffferet routers. This is where routing algoirthms will be used since there are multiple paths from one router to another. Both Dijkstra and Bellman Ford can be used. But for this example Dijkstra was used, but you can swap out the algoirthm with the setRoutingStrategy() method on each router

<p align="center">
<img src="https://user-images.githubusercontent.com/71960514/236643925-0ce5ef92-36ae-416f-9307-58732db977e7.png" width=400/>

</p>

<details>
  <summary>Output</summary>
  
  ````
| 21:25:02.276 | Client A   | Sent     | tcp.TcpSynEvent                | To   | Router A   |
| 21:25:02.341 | Router A   | Received | tcp.TcpSynEvent                | From | Client A   |
| 21:25:02.404 | Router A   | Sent     | tcp.TcpSynEvent                | To   | Router C   |
| 21:25:02.554 | Router C   | Received | tcp.TcpSynEvent                | From | Router A   |
| 21:25:02.617 | Router C   | Sent     | tcp.TcpSynEvent                | To   | Router B   |
| 21:25:02.759 | Router B   | Received | tcp.TcpSynEvent                | From | Router C   |
| 21:25:02.821 | Router B   | Sent     | tcp.TcpSynEvent                | To   | Router D   |
| 21:25:02.951 | Router D   | Received | tcp.TcpSynEvent                | From | Router B   |
| 21:25:03.002 | Router D   | Sent     | tcp.TcpSynEvent                | To   | Switch D   |
| 21:25:03.176 | Switch D   | Received | tcp.TcpSynEvent                | From | Router D   |
| 21:25:03.238 | Switch D   | Sent     | tcp.TcpSynEvent                | To   | Client D   |
| 21:25:03.382 | Client D   | Received | tcp.TcpSynEvent                | From | Switch D   |
| 21:25:03.445 | Client D   | Sent     | tcp.TcpSynAckEvent             | To   | Router D   |
| 21:25:03.589 | Router D   | Received | tcp.TcpSynAckEvent             | From | Client D   |
| 21:25:03.653 | Router D   | Sent     | tcp.TcpSynAckEvent             | To   | Router B   |
| 21:25:03.796 | Router B   | Received | tcp.TcpSynAckEvent             | From | Router D   |
| 21:25:03.859 | Router B   | Sent     | tcp.TcpSynAckEvent             | To   | Router C   |
| 21:25:03.999 | Router C   | Received | tcp.TcpSynAckEvent             | From | Router B   |
| 21:25:04.062 | Router C   | Sent     | tcp.TcpSynAckEvent             | To   | Router A   |
| 21:25:04.206 | Router A   | Received | tcp.TcpSynAckEvent             | From | Router C   |
| 21:25:04.269 | Router A   | Sent     | tcp.TcpSynAckEvent             | To   | Switch A   |
| 21:25:04.412 | Switch A   | Received | tcp.TcpSynAckEvent             | From | Router A   |
| 21:25:04.476 | Switch A   | Sent     | tcp.TcpSynAckEvent             | To   | Client A   |
| 21:25:04.624 | Client A   | Received | tcp.TcpSynAckEvent             | From | Switch A   |
| 21:25:04.689 | Client A   | Sent     | tcp.TcpAckEvent                | To   | Router A   |
| 21:25:04.751 | Client A   | Sent     | tcp.TcpSendDataEvent           | To   | Router A   |
| 21:25:04.815 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:1   | DATA: This is some da  |
| 21:25:04.879 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:16  | DATA: ta that I am se  |
| 21:25:04.945 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:31  | DATA: nding through t  |
| 21:25:05.034 | Router A   | Received | tcp.TcpAckEvent                | From | Client A   |
| 21:25:05.098 | Router A   | Sent     | tcp.TcpAckEvent                | To   | Router C   |
| 21:25:05.243 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:1   | DATA: This is some da  |
| 21:25:05.308 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:1   | DATA: This is some da  |
| 21:25:05.451 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:16  | DATA: ta that I am se  |
| 21:25:05.511 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:16  | DATA: ta that I am se  |
| 21:25:05.655 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:31  | DATA: nding through t  |
| 21:25:05.718 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:31  | DATA: nding through t  |
| 21:25:05.862 | Router C   | Received | tcp.TcpAckEvent                | From | Router A   |
| 21:25:05.926 | Router C   | Sent     | tcp.TcpAckEvent                | To   | Router B   |
| 21:25:06.062 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:1   | DATA: This is some da  |
| 21:25:06.128 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:1   | DATA: This is some da  |
| 21:25:06.272 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:16  | DATA: ta that I am se  |
| 21:25:06.336 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:16  | DATA: ta that I am se  |
| 21:25:06.477 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:31  | DATA: nding through t  |
| 21:25:06.535 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:31  | DATA: nding through t  |
| 21:25:06.695 | Router B   | Received | tcp.TcpAckEvent                | From | Router C   |
| 21:25:06.758 | Router B   | Sent     | tcp.TcpAckEvent                | To   | Router D   |
| 21:25:06.901 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:1   | DATA: This is some da  |
| 21:25:06.964 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:1   | DATA: This is some da  |
| 21:25:07.103 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:16  | DATA: ta that I am se  |
| 21:25:07.167 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:07.311 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:31  | DATA: nding through t  |
| 21:25:07.374 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:31  | DATA: nding through t  |
| 21:25:07.519 | Router D   | Received | tcp.TcpAckEvent                | From | Router B   |
| 21:25:07.570 | Router D   | Sent     | tcp.TcpAckEvent                | To   | Switch D   |
| 21:25:07.730 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:1   | DATA: This is some da  |
| 21:25:07.793 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:1   | DATA: This is some da  |
| 21:25:07.938 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:16  | DATA: ta that I am se  |
| 21:25:08.003 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:08.141 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:31  | DATA: nding through t  |
| 21:25:08.204 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:31  | DATA: nding through t  |
| 21:25:08.347 | Switch D   | Received | tcp.TcpAckEvent                | From | Router D   |
| 21:25:08.412 | Switch D   | Sent     | tcp.TcpAckEvent                | To   | Client D   |
| 21:25:08.544 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:1   | DATA: This is some da  |
| 21:25:08.598 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:1   | DATA: This is some da  |
| 21:25:08.759 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:08.823 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:08.968 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:31  | DATA: nding through t  |
| 21:25:09.033 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:31  | DATA: nding through t  |
| 21:25:09.173 | Client D   | Received | tcp.TcpAckEvent                | From | Switch D   |
| 21:25:09.381 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:1   | DATA: This is some da  |
| 21:25:09.446 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:16  |
| 21:25:09.582 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:09.582 | Client D   | Invalid checksum detected |
| 21:25:09.646 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:16  |
| 21:25:09.788 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:31  | DATA: nding through t  |
| 21:25:09.852 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:16  |
| 21:25:09.996 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:16  |
| 21:25:10.060 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:16  |
| 21:25:10.197 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:16  |
| 21:25:10.261 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:16  |
| 21:25:10.404 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:16  |
| 21:25:10.468 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:16  |
| 21:25:10.605 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:16  |
| 21:25:10.669 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:16  |
| 21:25:10.812 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:16  |
| 21:25:10.876 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:16  |
| 21:25:11.019 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:16  |
| 21:25:11.071 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:16  |
| 21:25:11.234 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:16  |
| 21:25:11.297 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:16  |
| 21:25:11.443 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:16  |
| 21:25:11.507 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:16  |
| 21:25:11.630 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:16  |
| 21:25:11.694 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:16  |
| 21:25:11.853 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:16  |
| 21:25:11.915 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:16  |
| 21:25:12.061 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:16  |
| 21:25:12.117 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:16  |
| 21:25:12.262 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:16  |
| 21:25:12.326 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:16  |
| 21:25:12.471 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:16  |
| 21:25:12.534 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:16  |
| 21:25:12.671 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:16  |
| 21:25:12.734 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:16  |
| 21:25:12.877 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:16  |
| 21:25:12.941 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:16  |
| 21:25:13.084 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:16  |
| 21:25:13.084 | Client A   | Received new window size for | Client D | 4 |
| 21:25:13.138 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:46  | DATA: his very compli  |
| 21:25:13.201 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:61  | DATA: cated network s  |
| 21:25:13.265 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:76  | DATA: imulation that   |
| 21:25:13.316 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:91  | DATA: I made           |
| 21:25:13.379 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:16  |
| 21:25:13.379 | Client A   | Received new window size for | Client D | 3 |
| 21:25:13.586 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:16  |
| 21:25:13.586 | Client A   | Received new window size for | Client D | 2 |
| 21:25:13.801 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:46  | DATA: his very compli  |
| 21:25:13.865 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:46  | DATA: his very compli  |
| 21:25:14.007 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:61  | DATA: cated network s  |
| 21:25:14.072 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:61  | DATA: cated network s  |
| 21:25:14.209 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:76  | DATA: imulation that   |
| 21:25:14.260 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:76  | DATA: imulation that   |
| 21:25:14.419 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:91  | DATA: I made           |
| 21:25:14.483 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:91  | DATA: I made           |
| 21:25:14.619 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:46  | DATA: his very compli  |
| 21:25:14.684 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:46  | DATA: his very compli  |
| 21:25:14.826 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:61  | DATA: cated network s  |
| 21:25:14.890 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:61  | DATA: cated network s  |
| 21:25:15.032 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:76  | DATA: imulation that   |
| 21:25:15.097 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:76  | DATA: imulation that   |
| 21:25:15.249 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:91  | DATA: I made           |
| 21:25:15.312 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:91  | DATA: I made           |
| 21:25:15.455 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:46  | DATA: his very compli  |
| 21:25:15.519 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:46  | DATA: his very compli  |
| 21:25:15.655 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:61  | DATA: cated network s  |
| 21:25:15.720 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:61  | DATA: cated network s  |
| 21:25:15.863 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:76  | DATA: imulation that   |
| 21:25:15.927 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:76  | DATA: imulation that   |
| 21:25:16.070 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:91  | DATA: I made           |
| 21:25:16.126 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:91  | DATA: I made           |
| 21:25:16.285 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:46  | DATA: his very compli  |
| 21:25:16.349 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:46  | DATA: his very compli  |
| 21:25:16.494 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:61  | DATA: cated network s  |
| 21:25:16.557 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:61  | DATA: cated network s  |
| 21:25:16.709 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:76  | DATA: imulation that   |
| 21:25:16.772 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:76  | DATA: imulation that   |
| 21:25:16.917 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:91  | DATA: I made           |
| 21:25:16.983 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:91  | DATA: I made           |
| 21:25:17.114 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:46  | DATA: his very compli  |
| 21:25:17.167 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:46  | DATA: his very compli  |
| 21:25:17.325 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:61  | DATA: cated network s  |
| 21:25:17.390 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:61  | DATA: cated network s  |
| 21:25:17.532 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:76  | DATA: imulation that   |
| 21:25:17.596 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:76  | DATA: imulation that   |
| 21:25:17.735 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:91  | DATA: I made           |
| 21:25:17.799 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:91  | DATA: I made           |
| 21:25:17.942 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:46  | DATA: his very compli  |
| 21:25:18.007 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:16  |
| 21:25:18.145 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:61  | DATA: cated network s  |
| 21:25:18.209 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:16  |
| 21:25:18.357 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:76  | DATA: imulation that   |
| 21:25:18.421 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:16  |
| 21:25:18.564 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:91  | DATA: I made           |
| 21:25:18.628 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:16  |
| 21:25:18.775 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:16  |
| 21:25:18.837 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:16  |
| 21:25:18.981 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:16  |
| 21:25:19.044 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:16  |
| 21:25:19.181 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:16  |
| 21:25:19.247 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:16  |
| 21:25:19.389 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:16  |
| 21:25:19.453 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:16  |
| 21:25:19.596 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:16  |
| 21:25:19.648 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:16  |
| 21:25:19.810 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:16  |
| 21:25:19.874 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:16  |
| 21:25:20.015 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:16  |
| 21:25:20.079 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:16  |
| 21:25:20.207 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:16  |
| 21:25:20.269 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:16  |
| 21:25:20.316 | Client A   | Resending event |
| 21:25:20.380 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:16  | DATA: ta that I am se  |
| 21:25:20.380 | Client A   | Resending event |
| 21:25:20.425 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:16  |
| 21:25:20.440 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:31  | DATA: nding through t  |
| 21:25:20.487 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:16  |
| 21:25:20.644 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:16  |
| 21:25:20.708 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:16  |
| 21:25:20.848 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:16  |
| 21:25:20.912 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:16  |
| 21:25:21.052 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:16  |
| 21:25:21.115 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:16  |
| 21:25:21.256 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:16  | DATA: ta that I am se  |
| 21:25:21.321 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:16  | DATA: ta that I am se  |
| 21:25:21.446 | Client A   | Resending event |
| 21:25:21.462 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:31  | DATA: nding through t  |
| 21:25:21.509 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:46  | DATA: his very compli  |
| 21:25:21.509 | Client A   | Resending event |
| 21:25:21.525 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:31  | DATA: nding through t  |
| 21:25:21.572 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:61  | DATA: cated network s  |
| 21:25:21.664 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:16  |
| 21:25:21.726 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:16  |
| 21:25:21.867 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:16  |
| 21:25:21.927 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:16  |
| 21:25:22.066 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:16  |
| 21:25:22.127 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:16  |
| 21:25:22.280 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:16  |
| 21:25:22.341 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:16  |
| 21:25:22.496 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:16  | DATA: ta that I am se  |
| 21:25:22.558 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:16  | DATA: ta that I am se  |
| 21:25:22.574 | Client A   | Resending event |
| 21:25:22.635 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:76  | DATA: imulation that   |
| 21:25:22.635 | Client A   | Resending event |
| 21:25:22.696 | Router A   | 21:25:22.696 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:91  | DATA: I made           | Received | tcp.TcpSendDataSegmentEvent    | From |
| Client A   | SEQ:46  | DATA: his very compli  |
| 21:25:22.759 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:46  | DATA: his very compli  |
| 21:25:22.900 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:31  | DATA: nding through t  |
| 21:25:22.963 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:31  | DATA: nding through t  |
| 21:25:23.104 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:61  | DATA: cated network s  |
| 21:25:23.165 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:61  | DATA: cated network s  |
| 21:25:23.321 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:16  |
| 21:25:23.385 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:16  |
| 21:25:23.525 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:16  |
| 21:25:23.587 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:16  |
| 21:25:23.727 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:16  |
| 21:25:23.788 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:16  |
| 21:25:23.928 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:16  |
| 21:25:23.989 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:16  |
| 21:25:24.131 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:16  | DATA: ta that I am se  |
| 21:25:24.194 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:24.333 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:76  | DATA: imulation that   |
| 21:25:24.396 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:76  | DATA: imulation that   |
| 21:25:24.536 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:91  | DATA: I made           |
| 21:25:24.599 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:91  | DATA: I made           |
| 21:25:24.736 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:46  | DATA: his very compli  |
| 21:25:24.797 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:46  | DATA: his very compli  |
| 21:25:24.954 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:31  | DATA: nding through t  |
| 21:25:25.016 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:31  | DATA: nding through t  |
| 21:25:25.153 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:61  | DATA: cated network s  |
| 21:25:25.215 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:61  | DATA: cated network s  |
| 21:25:25.353 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:16  |
| 21:25:25.354 | Client A   | Received new window size for | Client D | 3 |
| 21:25:25.556 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:16  |
| 21:25:25.556 | Client A   | Received new window size for | Client D | 2 |
| 21:25:25.756 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:16  |
| 21:25:25.756 | Client A   | Received new window size for | Client D | 4 |
| 21:25:25.961 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:16  |
| 21:25:25.961 | Client A   | Received new window size for | Client D | 3 |
| 21:25:26.166 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:16  | DATA: ta that I am se  |
| 21:25:26.229 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:26.368 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:76  | DATA: imulation that   |
| 21:25:26.431 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:76  | DATA: imulation that   |
| 21:25:26.571 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:91  | DATA: I made           |
| 21:25:26.632 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:91  | DATA: I made           |
| 21:25:26.774 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:46  | DATA: his very compli  |
| 21:25:26.836 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:46  | DATA: his very compli  |
| 21:25:26.976 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:31  | DATA: nding through t  |
| 21:25:27.040 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:31  | DATA: nding through t  |
| 21:25:27.183 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:61  | DATA: cated network s  |
| 21:25:27.245 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:61  | DATA: cated network s  |
| 21:25:27.387 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:27.450 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:27.592 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:76  | DATA: imulation that   |
| 21:25:27.655 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:76  | DATA: imulation that   |
| 21:25:27.797 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:91  | DATA: I made           |
| 21:25:27.857 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:91  | DATA: I made           |
| 21:25:27.999 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:46  | DATA: his very compli  |
| 21:25:28.061 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:46  | DATA: his very compli  |
| 21:25:28.200 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:31  | DATA: nding through t  |
| 21:25:28.264 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:31  | DATA: nding through t  |
| 21:25:28.405 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:61  | DATA: cated network s  |
| 21:25:28.468 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:61  | DATA: cated network s  |
| 21:25:28.608 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:28.671 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:31  |
| 21:25:28.812 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:76  | DATA: imulation that   |
| 21:25:28.875 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:76  | DATA: imulation that   |
| 21:25:29.016 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:91  | DATA: I made           |
| 21:25:29.080 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:91  | DATA: I made           |
| 21:25:29.222 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:46  | DATA: his very compli  |
| 21:25:29.285 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:46  | DATA: his very compli  |
| 21:25:29.427 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:31  | DATA: nding through t  |
| 21:25:29.489 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:46  |
| 21:25:29.631 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:61  | DATA: cated network s  |
| 21:25:29.694 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:61  | DATA: cated network s  |
| 21:25:29.834 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:31  |
| 21:25:29.896 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:31  |
| 21:25:30.038 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:76  | DATA: imulation that   |
| 21:25:30.101 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:76  | DATA: imulation that   |
| 21:25:30.243 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:91  | DATA: I made           |
| 21:25:30.306 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:91  | DATA: I made           |
| 21:25:30.445 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:46  | DATA: his very compli  |
| 21:25:30.508 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:61  |
| 21:25:30.649 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:46  |
| 21:25:30.710 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:46  |
| 21:25:30.853 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:61  | DATA: cated network s  |
| 21:25:30.915 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:76  |
| 21:25:31.057 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:31  |
| 21:25:31.119 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:31  |
| 21:25:31.262 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:76  | DATA: imulation that   |
| 21:25:31.324 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:91  |
| 21:25:31.466 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:91  | DATA: I made           |
| 21:25:31.529 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:97  |
| 21:25:31.672 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:61  |
| 21:25:31.735 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:61  |
| 21:25:31.876 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:46  |
| 21:25:31.937 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:46  |
| 21:25:32.079 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:76  |
| 21:25:32.144 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:76  |
| 21:25:32.286 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:31  |
| 21:25:32.348 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:31  |
| 21:25:32.488 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:91  |
| 21:25:32.550 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:91  |
| 21:25:32.692 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:97  |
| 21:25:32.755 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:97  |
| 21:25:32.896 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:61  |
| 21:25:32.959 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:61  |
| 21:25:33.099 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:46  |
| 21:25:33.161 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:46  |
| 21:25:33.304 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:76  |
| 21:25:33.368 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:76  |
| 21:25:33.510 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:31  |
| 21:25:33.574 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:25:33.717 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:91  |
| 21:25:33.780 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:91  |
| 21:25:33.922 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:97  |
| 21:25:33.984 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:97  |
| 21:25:34.125 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:61  |
| 21:25:34.188 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:61  |
| 21:25:34.327 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:46  |
| 21:25:34.390 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:46  |
| 21:25:34.531 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:76  |
| 21:25:34.592 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:76  |
| 21:25:34.732 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:31  |
| 21:25:34.794 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:25:34.935 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:91  |
| 21:25:34.999 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:91  |
| 21:25:35.140 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:97  |
| 21:25:35.203 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:97  |
| 21:25:35.343 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:61  |
| 21:25:35.405 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:61  |
| 21:25:35.545 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:46  |
| 21:25:35.608 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:46  |
| 21:25:35.747 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:76  |
| 21:25:35.794 | Client A   | Resending event |
| 21:25:35.810 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:76  |
| 21:25:35.857 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:16  | DATA: ta that I am se  |
| 21:25:35.857 | Client A   | Resending event |
| 21:25:35.920 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:31  | DATA: nding through t  |
| 21:25:35.952 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:25:35.952 | Client A   | Received new window size for | Client D | 4 |
| 21:25:36.155 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:91  |
| 21:25:36.216 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:91  |
| 21:25:36.357 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:97  |
| 21:25:36.419 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:97  |
| 21:25:36.557 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:61  |
| 21:25:36.620 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:61  |
| 21:25:36.759 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:46  |
| 21:25:36.759 | Client A   | Received new window size for | Client D | 2 |
| 21:25:36.932 | Client A   | Resending event |
| 21:25:36.963 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:76  |
| 21:25:36.995 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:46  | DATA: his very compli  |
| 21:25:36.995 | Client A   | Resending event |
| 21:25:37.027 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:76  |
| 21:25:37.058 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:61  | DATA: cated network s  |
| 21:25:37.167 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:16  | DATA: ta that I am se  |
| 21:25:37.229 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:16  | DATA: ta that I am se  |
| 21:25:37.384 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:31  | DATA: nding through t  |
| 21:25:37.447 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:31  | DATA: nding through t  |
| 21:25:37.586 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:91  |
| 21:25:37.647 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:91  |
| 21:25:37.803 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:97  |
| 21:25:37.865 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:97  |
| 21:25:38.004 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:61  |
| 21:25:38.004 | Client A   | Received new window size for | Client D | 4 |
| 21:25:38.066 | Client A   | Resending event |
| 21:25:38.127 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:76  | DATA: imulation that   |
| 21:25:38.127 | Client A   | Resending event |
| 21:25:38.189 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router A   | SEQ:91  | DATA: I made           |
| 21:25:38.221 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:46  | DATA: his very compli  |
| 21:25:38.283 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:46  | DATA: his very compli  |
| 21:25:38.425 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:76  |
| 21:25:38.425 | Client A   | Received new window size for | Client D | 3 |
| 21:25:38.627 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:61  | DATA: cated network s  |
| 21:25:38.689 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:61  | DATA: cated network s  |
| 21:25:38.828 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:16  | DATA: ta that I am se  |
| 21:25:38.890 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:16  | DATA: ta that I am se  |
| 21:25:39.028 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:31  | DATA: nding through t  |
| 21:25:39.090 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:31  | DATA: nding through t  |
| 21:25:39.230 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:91  |
| 21:25:39.230 | Client A   | Received new window size for | Client D | 3 |
| 21:25:39.432 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:97  |
| 21:25:39.432 | Client A   | Received new window size for | Client D | 2 |
| 21:25:39.494 | Client A   | Sent     | tcp.TcpFinEvent                | To   | Router A   |
| 21:25:39.634 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:76  | DATA: imulation that   |
| 21:25:39.696 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:76  | DATA: imulation that   |
| 21:25:39.837 | Router A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:91  | DATA: I made           |
| 21:25:39.899 | Router A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router C   | SEQ:91  | DATA: I made           |
| 21:25:40.042 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:46  | DATA: his very compli  |
| 21:25:40.104 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:46  | DATA: his very compli  |
| 21:25:40.243 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:61  | DATA: cated network s  |
| 21:25:40.305 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:61  | DATA: cated network s  |
| 21:25:40.446 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:16  | DATA: ta that I am se  |
| 21:25:40.508 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:40.648 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:31  | DATA: nding through t  |
| 21:25:40.711 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:31  | DATA: nding through t  |
| 21:25:40.851 | Router A   | Received | tcp.TcpFinEvent                | From | Client A   |
| 21:25:40.914 | Router A   | Sent     | tcp.TcpFinEvent                | To   | Router C   |
| 21:25:41.054 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:76  | DATA: imulation that   |
| 21:25:41.118 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:76  | DATA: imulation that   |
| 21:25:41.259 | Router C   | Received | tcp.TcpSendDataSegmentEvent    | From | Router A   | SEQ:91  | DATA: I made           |
| 21:25:41.321 | Router C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router B   | SEQ:91  | DATA: I made           |
| 21:25:41.464 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:46  | DATA: his very compli  |
| 21:25:41.526 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:46  | DATA: his very compli  |
| 21:25:41.666 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:61  | DATA: cated network s  |
| 21:25:41.728 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:61  | DATA: cated network s  |
| 21:25:41.869 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:16  | DATA: ta that I am se  |
| 21:25:41.932 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:42.074 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:31  | DATA: nding through t  |
| 21:25:42.135 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:31  | DATA: nding through t  |
| 21:25:42.276 | Router C   | Received | tcp.TcpFinEvent                | From | Router A   |
| 21:25:42.341 | Router C   | Sent     | tcp.TcpFinEvent                | To   | Router B   |
| 21:25:42.483 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:76  | DATA: imulation that   |
| 21:25:42.545 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:76  | DATA: imulation that   |
| 21:25:42.687 | Router B   | Received | tcp.TcpSendDataSegmentEvent    | From | Router C   | SEQ:91  | DATA: I made           |
| 21:25:42.750 | Router B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Router D   | SEQ:91  | DATA: I made           |
| 21:25:42.890 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:46  | DATA: his very compli  |
| 21:25:42.953 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:46  | DATA: his very compli  |
| 21:25:43.092 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:61  | DATA: cated network s  |
| 21:25:43.155 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:61  | DATA: cated network s  |
| 21:25:43.297 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:43.359 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:43.501 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:31  | DATA: nding through t  |
| 21:25:43.563 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:31  | DATA: nding through t  |
| 21:25:43.705 | Router B   | Received | tcp.TcpFinEvent                | From | Router C   |
| 21:25:43.767 | Router B   | Sent     | tcp.TcpFinEvent                | To   | Router D   |
| 21:25:43.908 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:76  | DATA: imulation that   |
| 21:25:43.970 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:76  | DATA: imulation that   |
| 21:25:44.110 | Router D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router B   | SEQ:91  | DATA: I made           |
| 21:25:44.173 | Router D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch D   | SEQ:91  | DATA: I made           |
| 21:25:44.314 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:46  | DATA: his very compli  |
| 21:25:44.376 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:46  | DATA: his very compli  |
| 21:25:44.516 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:61  | DATA: cated network s  |
| 21:25:44.577 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:61  | DATA: cated network s  |
| 21:25:44.718 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:16  | DATA: ta that I am se  |
| 21:25:44.718 | Client D   | Simulated segment dropped, not acknowledging |
| 21:25:44.921 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:31  | DATA: nding through t  |
| 21:25:44.985 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:97  |
| 21:25:45.123 | Router D   | Received | tcp.TcpFinEvent                | From | Router B   |
| 21:25:45.186 | Router D   | Sent     | tcp.TcpFinEvent                | To   | Switch D   |
| 21:25:45.327 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:76  | DATA: imulation that   |
| 21:25:45.389 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:76  | DATA: imulation that   |
| 21:25:45.529 | Switch D   | Received | tcp.TcpSendDataSegmentEvent    | From | Router D   | SEQ:91  | DATA: I made           |
| 21:25:45.593 | Switch D   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:91  | DATA: I made           |
| 21:25:45.733 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:46  | DATA: his very compli  |
| 21:25:45.795 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:97  |
| 21:25:45.936 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:61  | DATA: cated network s  |
| 21:25:46.000 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:97  |
| 21:25:46.141 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:97  |
| 21:25:46.203 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:97  |
| 21:25:46.345 | Switch D   | Received | tcp.TcpFinEvent                | From | Router D   |
| 21:25:46.407 | Switch D   | Sent     | tcp.TcpFinEvent                | To   | Client D   |
| 21:25:46.548 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:76  | DATA: imulation that   |
| 21:25:46.611 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:97  |
| 21:25:46.752 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch D   | SEQ:91  | DATA: I made           |
| 21:25:46.814 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router D   | ACK:97  |
| 21:25:46.954 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:97  |
| 21:25:47.017 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:97  |
| 21:25:47.156 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:97  |
| 21:25:47.218 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:97  |
| 21:25:47.362 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:97  |
| 21:25:47.425 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:97  |
| 21:25:47.564 | Client D   | Received | tcp.TcpFinEvent                | From | Switch D   |
| 21:25:47.628 | Client D   | Sent     | tcp.TcpFinAckEvent             | To   | Router D   |
| 21:25:47.691 | Client D   | Sent     | tcp.TcpFinEvent                | To   | Router D   |
| 21:25:47.769 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:97  |
| 21:25:47.834 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:97  |
| 21:25:47.974 | Router D   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:97  |
| 21:25:48.037 | Router D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router B   | ACK:97  |
| 21:25:48.177 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:97  |
| 21:25:48.239 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:97  |
| 21:25:48.379 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:97  |
| 21:25:48.441 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:97  |
| 21:25:48.582 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:97  |
| 21:25:48.643 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:97  |
| 21:25:48.784 | Router D   | Received | tcp.TcpFinAckEvent             | From | Client D   |
| 21:25:48.847 | Router D   | Sent     | tcp.TcpFinAckEvent             | To   | Router B   |
| 21:25:48.988 | Router D   | Received | tcp.TcpFinEvent                | From | Client D   |
| 21:25:49.048 | Router D   | Sent     | tcp.TcpFinEvent                | To   | Router B   |
| 21:25:49.190 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:97  |
| 21:25:49.252 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:97  |
| 21:25:49.393 | Router B   | Received | tcp.TcpAckDataSegmentEvent     | From | Router D   | ACK:97  |
| 21:25:49.455 | Router B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router C   | ACK:97  |
| 21:25:49.595 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:97  |
| 21:25:49.657 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:97  |
| 21:25:49.796 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:97  |
| 21:25:49.859 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:97  |
| 21:25:50.000 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:97  |
| 21:25:50.062 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:97  |
| 21:25:50.203 | Router B   | Received | tcp.TcpFinAckEvent             | From | Router D   |
| 21:25:50.266 | Router B   | Sent     | tcp.TcpFinAckEvent             | To   | Router C   |
| 21:25:50.405 | Router B   | Received | tcp.TcpFinEvent                | From | Router D   |
| 21:25:50.467 | Router B   | Sent     | tcp.TcpFinEvent                | To   | Router C   |
| 21:25:50.606 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:97  |
| 21:25:50.668 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:97  |
| 21:25:50.821 | Router C   | Received | tcp.TcpAckDataSegmentEvent     | From | Router B   | ACK:97  |
| 21:25:50.883 | Router C   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Router A   | ACK:97  |
| 21:25:51.023 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:97  |
| 21:25:51.085 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:97  |
| 21:25:51.227 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:97  |
| 21:25:51.288 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:97  |
| 21:25:51.428 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:97  |
| 21:25:51.492 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:97  |
| 21:25:51.633 | Router C   | Received | tcp.TcpFinAckEvent             | From | Router B   |
| 21:25:51.696 | Router C   | Sent     | tcp.TcpFinAckEvent             | To   | Router A   |
| 21:25:51.839 | Router C   | Received | tcp.TcpFinEvent                | From | Router B   |
| 21:25:51.903 | Router C   | Sent     | tcp.TcpFinEvent                | To   | Router A   |
| 21:25:52.044 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:97  |
| 21:25:52.106 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:97  |
| 21:25:52.245 | Router A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router C   | ACK:97  |
| 21:25:52.307 | Router A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:97  |
| 21:25:52.445 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:97  |
| 21:25:52.507 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:97  |
| 21:25:52.660 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:97  |
| 21:25:52.722 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:97  |
| 21:25:52.875 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:97  |
| 21:25:53.077 | Router A   | Received | tcp.TcpFinAckEvent             | From | Router C   |
| 21:25:53.140 | Router A   | Sent     | tcp.TcpFinAckEvent             | To   | Switch A   |
| 21:25:53.278 | Router A   | Received | tcp.TcpFinEvent                | From | Router C   |
| 21:25:53.342 | Router A   | Sent     | tcp.TcpFinEvent                | To   | Switch A   |
| 21:25:53.483 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:97  |
| 21:25:53.545 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:97  |
| 21:25:53.684 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Router A   | ACK:97  |
| 21:25:53.747 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:97  |
| 21:25:53.890 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:97  |
| 21:25:54.092 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:97  |
| 21:25:54.294 | Switch A   | Received | tcp.TcpFinAckEvent             | From | Router A   |
| 21:25:54.355 | Switch A   | Sent     | tcp.TcpFinAckEvent             | To   | Client A   |
| 21:25:54.496 | Switch A   | Received | tcp.TcpFinEvent                | From | Router A   |
| 21:25:54.557 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Client A   |
| 21:25:54.696 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:97  |
| 21:25:54.902 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:97  |
| 21:25:55.106 | Client A   | Received | tcp.TcpFinAckEvent             | From | Switch A   |
| 21:25:55.313 | Client A   | Received | tcp.TcpFinEvent                | From | Switch A   |
| 21:25:55.377 | Client A   | Sent     | tcp.TcpFinAckEvent             | To   | Router A   |
| 21:25:55.440 | Client A   | Sent     | tcp.TcpFinEvent                | To   | Router A   |
| 21:25:55.519 | Router A   | Received | tcp.TcpFinAckEvent             | From | Client A   |
| 21:25:55.581 | Router A   | Sent     | tcp.TcpFinAckEvent             | To   | Router C   |
| 21:25:55.724 | Router A   | Received | tcp.TcpFinEvent                | From | Client A   |
| 21:25:55.786 | Router A   | Sent     | tcp.TcpFinEvent                | To   | Router C   |
| 21:25:55.926 | Router C   | Received | tcp.TcpFinAckEvent             | From | Router A   |
| 21:25:55.990 | Router C   | Sent     | tcp.TcpFinAckEvent             | To   | Router B   |
| 21:25:56.132 | Router C   | Received | tcp.TcpFinEvent                | From | Router A   |
| 21:25:56.196 | Router C   | Sent     | tcp.TcpFinEvent                | To   | Router B   |
| 21:25:56.337 | Router B   | Received | tcp.TcpFinAckEvent             | From | Router C   |
| 21:25:56.400 | Router B   | Sent     | tcp.TcpFinAckEvent             | To   | Router D   |
| 21:25:56.542 | Router B   | Received | tcp.TcpFinEvent                | From | Router C   |
| 21:25:56.604 | Router B   | Sent     | tcp.TcpFinEvent                | To   | Router D   |
| 21:25:56.744 | Router D   | Received | tcp.TcpFinAckEvent             | From | Router B   |
| 21:25:56.805 | Router D   | Sent     | tcp.TcpFinAckEvent             | To   | Switch D   |
| 21:25:56.943 | Router D   | Received | tcp.TcpFinEvent                | From | Router B   |
| 21:25:57.006 | Router D   | Sent     | tcp.TcpFinEvent                | To   | Switch D   |
| 21:25:57.149 | Switch D   | Received | tcp.TcpFinAckEvent             | From | Router D   |
| 21:25:57.211 | Switch D   | Sent     | tcp.TcpFinAckEvent             | To   | Client D   |
| 21:25:57.350 | Switch D   | Received | tcp.TcpFinEvent                | From | Router D   |
| 21:25:57.412 | Switch D   | Sent     | tcp.TcpFinEvent                | To   | Client D   |
| 21:25:57.554 | Client D   | Received | tcp.TcpFinAckEvent             | From | Switch D   |
| 21:25:57.554 | Client D   | Received Data: This is some data that I am sending through this very complicated network simulation that I made |
| 21:25:57.756 | Client D   | Received | tcp.TcpFinEvent                | From | Switch D   |

  ````

</details>


## Example 4

For example 4 we wanted to highlight the fact that we can start multiple different connections at once. In this example Client A is communicating to Client D, and at the same time Client C is communicating to Client B.

<p align="center">
<img src="https://user-images.githubusercontent.com/71960514/236643967-037b0d04-0e7f-4805-a6f3-6f9ae8cbe0b4.png" width=400/>

</p>

<details>
  <summary>Output</summary>
  
  ````
| 21:20:46.092 | Client A   | Sent     | arp.ArpRequestEvent            | To   | Switch A   |
| 21:20:46.153 | Client C   | Sent     | arp.ArpRequestEvent            | To   | Switch B   |
| 21:20:46.216 | Switch A   | Received | arp.ArpRequestEvent            | From | Client A   |
| 21:20:46.216 | Switch A   | Sent     | arp.ArpRequestEvent            | To   | Client B   |
| 21:20:46.216 | Switch A   | Sent     | arp.ArpRequestEvent            | To   | Switch B   |
| 21:20:46.622 | Switch B   | Received | arp.ArpRequestEvent            | From | Switch A   |
| 21:20:46.622 | Switch B   | Sent     | arp.ArpRequestEvent            | To   | Client C   |
| 21:20:46.622 | Switch B   | Sent     | arp.ArpRequestEvent            | To   | Client D   |
| 21:20:47.041 | Client D   | Received | arp.ArpRequestEvent            | From | Switch B   |
| 21:20:47.105 | Client D   | Sent     | arp.ArpResponseEvent           | To   | Switch B   |
| 21:20:47.249 | Switch B   | Received | arp.ArpRequestEvent            | From | Client C   |
| 21:20:47.249 | Switch B   | Sent     | arp.ArpRequestEvent            | To   | Client D   |
| 21:20:47.249 | Switch B   | Sent     | arp.ArpRequestEvent            | To   | Switch A   |
| 21:20:47.655 | Switch A   | Received | arp.ArpRequestEvent            | From | Switch B   |
| 21:20:47.655 | Switch A   | Sent     | arp.ArpRequestEvent            | To   | Client A   |
| 21:20:47.655 | Switch A   | Sent     | arp.ArpRequestEvent            | To   | Client B   |
| 21:20:48.076 | Client B   | Received | arp.ArpRequestEvent            | From | Switch A   |
| 21:20:48.141 | Client B   | Sent     | arp.ArpResponseEvent           | To   | Switch A   |
| 21:20:48.283 | Switch B   | Received | arp.ArpResponseEvent           | From | Client D   |
| 21:20:48.338 | Switch B   | Sent     | arp.ArpResponseEvent           | To   | Client C   |
| 21:20:48.338 | Switch B   | Sent     | arp.ArpResponseEvent           | To   | Switch A   |
| 21:20:48.499 | Switch A   | Received | arp.ArpResponseEvent           | From | Client B   |
| 21:20:48.565 | Switch A   | Sent     | arp.ArpResponseEvent           | To   | Client A   |
| 21:20:48.565 | Switch A   | Sent     | arp.ArpResponseEvent           | To   | Switch B   |
| 21:20:48.708 | Switch A   | Received | arp.ArpResponseEvent           | From | Switch B   |
| 21:20:48.773 | Switch A   | Sent     | arp.ArpResponseEvent           | To   | Client A   |
| 21:20:49.132 | Switch B   | Received | arp.ArpResponseEvent           | From | Switch A   |
| 21:20:49.195 | Switch B   | Sent     | arp.ArpResponseEvent           | To   | Client C   |
| 21:20:49.552 | Client A   | Received | arp.ArpResponseEvent           | From | Switch A   |
| 21:20:49.615 | Client A   | Sent     | tcp.TcpSynEvent                | To   | Switch A   |
| 21:20:49.759 | Client C   | Received | arp.ArpResponseEvent           | From | Switch B   |
| 21:20:49.825 | Client C   | Sent     | tcp.TcpSynEvent                | To   | Switch B   |
| 21:20:49.960 | Switch A   | Received | tcp.TcpSynEvent                | From | Client A   |
| 21:20:50.023 | Switch A   | Sent     | tcp.TcpSynEvent                | To   | Client B   |
| 21:20:50.023 | Switch A   | Sent     | tcp.TcpSynEvent                | To   | Switch B   |
| 21:20:50.167 | Switch B   | Received | tcp.TcpSynEvent                | From | Client C   |
| 21:20:50.230 | Switch B   | Sent     | tcp.TcpSynEvent                | To   | Client D   |
| 21:20:50.230 | Switch B   | Sent     | tcp.TcpSynEvent                | To   | Switch A   |
| 21:20:50.362 | Switch B   | Received | tcp.TcpSynEvent                | From | Switch A   |
| 21:20:50.428 | Switch B   | Sent     | tcp.TcpSynEvent                | To   | Client D   |
| 21:20:50.794 | Switch A   | Received | tcp.TcpSynEvent                | From | Switch B   |
| 21:20:50.845 | Switch A   | Sent     | tcp.TcpSynEvent                | To   | Client B   |
| 21:20:51.217 | Client D   | Received | tcp.TcpSynEvent                | From | Switch B   |
| 21:20:51.281 | Client D   | Sent     | tcp.TcpSynAckEvent             | To   | Switch B   |
| 21:20:51.433 | Client B   | Received | tcp.TcpSynEvent                | From | Switch A   |
| 21:20:51.497 | Client B   | Sent     | tcp.TcpSynAckEvent             | To   | Switch A   |
| 21:20:51.641 | Switch B   | Received | tcp.TcpSynAckEvent             | From | Client D   |
| 21:20:51.706 | Switch B   | Sent     | tcp.TcpSynAckEvent             | To   | Client C   |
| 21:20:51.706 | Switch B   | Sent     | tcp.TcpSynAckEvent             | To   | Switch A   |
| 21:20:51.849 | Switch A   | Received | tcp.TcpSynAckEvent             | From | Client B   |
| 21:20:51.907 | Switch A   | Sent     | tcp.TcpSynAckEvent             | To   | Client A   |
| 21:20:51.907 | Switch A   | Sent     | tcp.TcpSynAckEvent             | To   | Switch B   |
| 21:20:52.050 | Switch A   | Received | tcp.TcpSynAckEvent             | From | Switch B   |
| 21:20:52.115 | Switch A   | Sent     | tcp.TcpSynAckEvent             | To   | Client A   |
| 21:20:52.474 | Switch B   | Received | tcp.TcpSynAckEvent             | From | Switch A   |
| 21:20:52.538 | Switch B   | Sent     | tcp.TcpSynAckEvent             | To   | Client C   |
| 21:20:52.880 | Client A   | Received | tcp.TcpSynAckEvent             | From | Switch A   |
| 21:20:52.943 | Client A   | Sent     | tcp.TcpAckEvent                | To   | Switch A   |
| 21:20:53.007 | Client A   | Sent     | tcp.TcpSendDataEvent           | To   | Switch A   |
| 21:20:53.071 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:1   | DATA: Hi Client D thi  |
| 21:20:53.102 | Client C   | Received | tcp.TcpSynAckEvent             | From | Switch B   |
| 21:20:53.133 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:16  | DATA: s is Client A a  |
| 21:20:53.166 | Client C   | Sent     | tcp.TcpAckEvent                | To   | Switch B   |
| 21:20:53.197 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:31  | DATA: nd i am sending  |
| 21:20:53.228 | Client C   | Sent     | tcp.TcpSendDataEvent           | To   | Switch B   |
| 21:20:53.292 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:1   | DATA: Hi Client B thi  |
| 21:20:53.308 | Switch A   | Received | tcp.TcpAckEvent                | From | Client A   |
| 21:20:53.358 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:16  | DATA: s is Client C,   |
| 21:20:53.360 | Switch A   | Sent     | tcp.TcpAckEvent                | To   | Client B   |
| 21:20:53.360 | Switch A   | Sent     | tcp.TcpAckEvent                | To   | Switch B   |
| 21:20:53.418 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:31  | DATA: I am sending yo  |
| 21:20:53.513 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:1   | DATA: Hi Client D thi  |
| 21:20:53.577 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:1   | DATA: Hi Client D thi  |
| 21:20:53.577 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:1   | DATA: Hi Client D thi  |
| 21:20:53.720 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:16  | DATA: s is Client A a  |
| 21:20:53.784 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:16  | DATA: s is Client A a  |
| 21:20:53.784 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:16  | DATA: s is Client A a  |
| 21:20:53.918 | Switch B   | Received | tcp.TcpAckEvent                | From | Client C   |
| 21:20:53.983 | Switch B   | Sent     | tcp.TcpAckEvent                | To   | Client D   |
| 21:20:53.983 | Switch B   | Sent     | tcp.TcpAckEvent                | To   | Switch A   |
| 21:20:54.127 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:31  | DATA: nd i am sending  |
| 21:20:54.191 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:31  | DATA: nd i am sending  |
| 21:20:54.191 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:31  | DATA: nd i am sending  |
| 21:20:54.334 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:1   | DATA: Hi Client B thi  |
| 21:20:54.393 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:1   | DATA: Hi Client B thi  |
| 21:20:54.393 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:1   | DATA: Hi Client B thi  |
| 21:20:54.537 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:16  | DATA: s is Client C,   |
| 21:20:54.602 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:16  | DATA: s is Client C,   |
| 21:20:54.602 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:16  | DATA: s is Client C,   |
| 21:20:54.956 | Switch B   | Received | tcp.TcpAckEvent                | From | Switch A   |
| 21:20:55.019 | Switch B   | Sent     | tcp.TcpAckEvent                | To   | Client D   |
| 21:20:55.163 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:31  | DATA: I am sending yo  |
| 21:20:55.226 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:31  | DATA: I am sending yo  |
| 21:20:55.226 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:31  | DATA: I am sending yo  |
| 21:20:55.371 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:1   | DATA: Hi Client D thi  |
| 21:20:55.434 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:1   | DATA: Hi Client D thi  |
| 21:20:55.999 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:16  | DATA: s is Client A a  |
| 21:20:56.061 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:16  | DATA: s is Client A a  |
| 21:20:56.205 | Switch A   | Received | tcp.TcpAckEvent                | From | Switch B   |
| 21:20:56.268 | Switch A   | Sent     | tcp.TcpAckEvent                | To   | Client B   |
| 21:20:56.622 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:31  | DATA: nd i am sending  |
| 21:20:56.687 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:31  | DATA: nd i am sending  |
| 21:20:57.250 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:1   | DATA: Hi Client B thi  |
| 21:20:57.314 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:1   | DATA: Hi Client B thi  |
| 21:20:57.672 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:16  | DATA: s is Client C,   |
| 21:20:57.736 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:16  | DATA: s is Client C,   |
| 21:20:57.880 | Client D   | Received | tcp.TcpAckEvent                | From | Switch B   |
| 21:20:58.082 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:31  | DATA: I am sending yo  |
| 21:20:58.145 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:31  | DATA: I am sending yo  |
| 21:20:58.485 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:1   | DATA: Hi Client D thi  |
| 21:20:58.550 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:16  |
| 21:20:58.709 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:16  | DATA: s is Client A a  |
| 21:20:58.771 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:31  |
| 21:20:58.915 | Client B   | Received | tcp.TcpAckEvent                | From | Switch A   |
| 21:20:59.131 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:31  | DATA: nd i am sending  |
| 21:20:59.194 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:46  |
| 21:20:59.337 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:1   | DATA: Hi Client B thi  |
| 21:20:59.400 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:16  |
| 21:20:59.550 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:16  | DATA: s is Client C,   |
| 21:20:59.615 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:20:59.757 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:31  | DATA: I am sending yo  |
| 21:20:59.821 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:46  |
| 21:20:59.953 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:16  |
| 21:21:00.005 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:16  |
| 21:21:00.005 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:16  |
| 21:21:00.179 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:31  |
| 21:21:00.243 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:31  |
| 21:21:00.243 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:31  |
| 21:21:00.386 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:46  |
| 21:21:00.449 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:46  |
| 21:21:00.449 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:46  |
| 21:21:00.588 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:16  |
| 21:21:00.652 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:16  |
| 21:21:00.652 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:16  |
| 21:21:00.795 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:31  |
| 21:21:00.859 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:21:00.859 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:31  |
| 21:21:00.994 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:46  |
| 21:21:01.059 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:46  |
| 21:21:01.059 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:46  |
| 21:21:01.219 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:16  |
| 21:21:01.282 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:16  |
| 21:21:01.641 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:31  |
| 21:21:01.704 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:31  |
| 21:21:02.253 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:46  |
| 21:21:02.316 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:46  |
| 21:21:02.673 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:16  |
| 21:21:02.737 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:16  |
| 21:21:02.879 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:21:02.944 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:31  |
| 21:21:03.482 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:46  |
| 21:21:03.535 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:46  |
| 21:21:03.693 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:16  |
| 21:21:03.693 | Client A   | Received new window size for | Client D | 2 |
| 21:21:03.758 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:46  | DATA:  you this data   |
| 21:21:03.821 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:61  | DATA: just to say tha  |
| 21:21:03.902 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:31  |
| 21:21:03.902 | Client A   | Received new window size for | Client D | 3 |
| 21:21:03.966 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:76  | DATA: t you are an am  |
| 21:21:04.023 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:91  | DATA: azing person     |
| 21:21:04.102 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:46  |
| 21:21:04.102 | Client A   | Received new window size for | Client D | 4 |
| 21:21:04.310 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:16  |
| 21:21:04.310 | Client C   | Received new window size for | Client B | 4 |
| 21:21:04.374 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:46  | DATA: u this to tell   |
| 21:21:04.438 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:61  | DATA: you how much I   |
| 21:21:04.502 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:76  | DATA: love your cooki  |
| 21:21:04.555 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:91  | DATA: es               |
| 21:21:04.618 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:31  |
| 21:21:04.618 | Client C   | Received new window size for | Client B | 3 |
| 21:21:04.826 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:46  |
| 21:21:04.826 | Client C   | Received new window size for | Client B | 3 |
| 21:21:05.044 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:46  | DATA:  you this data   |
| 21:21:05.107 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:46  | DATA:  you this data   |
| 21:21:05.107 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:46  | DATA:  you this data   |
| 21:21:05.234 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:61  | DATA: just to say tha  |
| 21:21:05.298 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:61  | DATA: just to say tha  |
| 21:21:05.298 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:61  | DATA: just to say tha  |
| 21:21:05.441 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:76  | DATA: t you are an am  |
| 21:21:05.504 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:76  | DATA: t you are an am  |
| 21:21:05.504 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:76  | DATA: t you are an am  |
| 21:21:05.655 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:91  | DATA: azing person     |
| 21:21:05.719 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:91  | DATA: azing person     |
| 21:21:05.719 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:91  | DATA: azing person     |
| 21:21:05.864 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:46  | DATA: u this to tell   |
| 21:21:05.928 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:46  | DATA: u this to tell   |
| 21:21:05.928 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:46  | DATA: u this to tell   |
| 21:21:06.063 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:61  | DATA: you how much I   |
| 21:21:06.128 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:61  | DATA: you how much I   |
| 21:21:06.128 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:61  | DATA: you how much I   |
| 21:21:06.285 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:76  | DATA: love your cooki  |
| 21:21:06.348 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:76  | DATA: love your cooki  |
| 21:21:06.348 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:76  | DATA: love your cooki  |
| 21:21:06.493 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:91  | DATA: es               |
| 21:21:06.546 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:91  | DATA: es               |
| 21:21:06.546 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:91  | DATA: es               |
| 21:21:06.911 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:46  | DATA:  you this data   |
| 21:21:06.974 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:46  | DATA:  you this data   |
| 21:21:07.116 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:61  | DATA: just to say tha  |
| 21:21:07.180 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:61  | DATA: just to say tha  |
| 21:21:07.739 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:76  | DATA: t you are an am  |
| 21:21:07.803 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:76  | DATA: t you are an am  |
| 21:21:07.946 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:91  | DATA: azing person     |
| 21:21:08.009 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:91  | DATA: azing person     |
| 21:21:08.141 | Client A   | Resending event |
| 21:21:08.204 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:46  | DATA:  you this data   |
| 21:21:08.204 | Client A   | Resending event |
| 21:21:08.267 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:61  | DATA: just to say tha  |
| 21:21:08.267 | Client A   | Resending event |
| 21:21:08.331 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:76  | DATA: t you are an am  |
| 21:21:08.331 | Client A   | Resending event |
| 21:21:08.395 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:91  | DATA: azing person     |
| 21:21:08.570 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:46  | DATA: u this to tell   |
| 21:21:08.623 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:46  | DATA: u this to tell   |
| 21:21:08.986 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:61  | DATA: you how much I   |
| 21:21:09.051 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:61  | DATA: you how much I   |
| 21:21:09.151 | Client C   | Resending event |
| 21:21:09.198 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:76  | DATA: love your cooki  |
| 21:21:09.214 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:46  | DATA: u this to tell   |
| 21:21:09.214 | Client C   | Resending event |
| 21:21:09.262 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:76  | DATA: love your cooki  |
| 21:21:09.278 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:61  | DATA: you how much I   |
| 21:21:09.278 | Client C   | Resending event |
| 21:21:09.341 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:76  | DATA: love your cooki  |
| 21:21:09.819 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:91  | DATA: es               |
| 21:21:09.884 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:91  | DATA: es               |
| 21:21:10.026 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:46  | DATA:  you this data   |
| 21:21:10.090 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:61  |
| 21:21:10.241 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:46  | DATA:  you this data   |
| 21:21:10.305 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:46  | DATA:  you this data   |
| 21:21:10.305 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:46  | DATA:  you this data   |
| 21:21:10.353 | Client C   | Resending event |
| 21:21:10.416 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:91  | DATA: es               |
| 21:21:10.448 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:61  | DATA: just to say tha  |
| 21:21:10.512 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:76  |
| 21:21:10.648 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:61  | DATA: just to say tha  |
| 21:21:10.714 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:61  | DATA: just to say tha  |
| 21:21:10.714 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:61  | DATA: just to say tha  |
| 21:21:10.857 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:76  | DATA: t you are an am  |
| 21:21:10.921 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:76  | DATA: t you are an am  |
| 21:21:10.921 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:76  | DATA: t you are an am  |
| 21:21:11.063 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:76  | DATA: t you are an am  |
| 21:21:11.115 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:91  |
| 21:21:11.278 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:91  | DATA: azing person     |
| 21:21:11.340 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:91  | DATA: azing person     |
| 21:21:11.340 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:91  | DATA: azing person     |
| 21:21:11.485 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:91  | DATA: azing person     |
| 21:21:11.547 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:103 |
| 21:21:11.684 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:46  | DATA: u this to tell   |
| 21:21:11.747 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:61  |
| 21:21:11.907 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:46  | DATA: u this to tell   |
| 21:21:11.970 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:46  | DATA: u this to tell   |
| 21:21:11.970 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:46  | DATA: u this to tell   |
| 21:21:12.115 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:61  | DATA: you how much I   |
| 21:21:12.173 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:61  | DATA: you how much I   |
| 21:21:12.173 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:61  | DATA: you how much I   |
| 21:21:12.317 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:61  | DATA: you how much I   |
| 21:21:12.383 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:76  |
| 21:21:12.528 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:76  | DATA: love your cooki  |
| 21:21:12.528 | Client B   | Invalid checksum detected |
| 21:21:12.593 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:76  |
| 21:21:12.727 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:76  | DATA: love your cooki  |
| 21:21:12.791 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:76  | DATA: love your cooki  |
| 21:21:12.791 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:76  | DATA: love your cooki  |
| 21:21:12.935 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:61  |
| 21:21:12.999 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:61  |
| 21:21:12.999 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:61  |
| 21:21:13.141 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:46  | DATA:  you this data   |
| 21:21:13.205 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:46  | DATA:  you this data   |
| 21:21:13.557 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:91  | DATA: es               |
| 21:21:13.608 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:91  | DATA: es               |
| 21:21:13.608 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:91  | DATA: es               |
| 21:21:13.768 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:91  | DATA: es               |
| 21:21:13.831 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:76  |
| 21:21:13.975 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:76  |
| 21:21:14.038 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:76  |
| 21:21:14.038 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:76  |
| 21:21:14.396 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:61  | DATA: just to say tha  |
| 21:21:14.459 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:61  | DATA: just to say tha  |
| 21:21:14.603 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:76  | DATA: t you are an am  |
| 21:21:14.665 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:76  | DATA: t you are an am  |
| 21:21:15.012 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:91  |
| 21:21:15.075 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:91  |
| 21:21:15.075 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:91  |
| 21:21:15.435 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:91  | DATA: azing person     |
| 21:21:15.498 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:91  | DATA: azing person     |
| 21:21:15.640 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:103 |
| 21:21:15.694 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:103 |
| 21:21:15.694 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:103 |
| 21:21:15.855 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:61  |
| 21:21:15.919 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:61  |
| 21:21:15.919 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:61  |
| 21:21:16.276 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:46  | DATA: u this to tell   |
| 21:21:16.340 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:46  | DATA: u this to tell   |
| 21:21:16.693 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:61  | DATA: you how much I   |
| 21:21:16.748 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:61  | DATA: you how much I   |
| 21:21:16.891 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:76  |
| 21:21:16.956 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:76  |
| 21:21:16.956 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:76  |
| 21:21:17.102 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:76  |
| 21:21:17.165 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:76  |
| 21:21:17.165 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:76  |
| 21:21:17.509 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:76  | DATA: love your cooki  |
| 21:21:17.574 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:76  | DATA: love your cooki  |
| 21:21:17.708 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:61  |
| 21:21:17.773 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:61  |
| 21:21:18.140 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:46  | DATA:  you this data   |
| 21:21:18.204 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:103 |
| 21:21:18.560 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:91  | DATA: es               |
| 21:21:18.624 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:91  | DATA: es               |
| 21:21:18.760 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:76  |
| 21:21:18.812 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:76  |
| 21:21:18.812 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:76  |
| 21:21:19.179 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:76  |
| 21:21:19.235 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:76  |
| 21:21:19.394 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:61  | DATA: just to say tha  |
| 21:21:19.394 | Client D   | Simulated segment dropped, not acknowledging |
| 21:21:19.603 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:76  | DATA: t you are an am  |
| 21:21:19.603 | Client D   | Invalid checksum detected |
| 21:21:19.657 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:103 |
| 21:21:19.807 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:91  |
| 21:21:19.858 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:91  |
| 21:21:20.225 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:91  | DATA: azing person     |
| 21:21:20.225 | Client D   | Invalid checksum detected |
| 21:21:20.281 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:103 |
| 21:21:20.648 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:103 |
| 21:21:20.711 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:103 |
| 21:21:21.070 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:61  |
| 21:21:21.135 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:61  |
| 21:21:21.273 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:46  | DATA: u this to tell   |
| 21:21:21.337 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:76  |
| 21:21:21.481 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:61  | DATA: you how much I   |
| 21:21:21.545 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:76  |
| 21:21:21.902 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:76  |
| 21:21:21.966 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:76  |
| 21:21:22.324 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:76  |
| 21:21:22.389 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:76  |
| 21:21:22.531 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:76  | DATA: love your cooki  |
| 21:21:22.593 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:91  |
| 21:21:22.736 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:61  |
| 21:21:22.736 | Client A   | Received new window size for | Client D | 3 |
| 21:21:22.943 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:103 |
| 21:21:23.005 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:103 |
| 21:21:23.005 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:103 |
| 21:21:23.149 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:91  | DATA: es               |
| 21:21:23.212 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:93  |
| 21:21:23.365 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:76  |
| 21:21:23.427 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:76  |
| 21:21:23.522 | Client A   | Resending event |
| 21:21:23.586 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:61  | DATA: just to say tha  |
| 21:21:23.586 | Client A   | Resending event |
| 21:21:23.651 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:76  | DATA: t you are an am  |
| 21:21:23.651 | Client A   | Resending event |
| 21:21:23.715 | Client A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:91  | DATA: azing person     |
| 21:21:23.771 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:76  |
| 21:21:23.772 | Client A   | Received new window size for | Client D | 3 |
| 21:21:23.994 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:103 |
| 21:21:24.059 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:103 |
| 21:21:24.059 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:103 |
| 21:21:24.201 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:91  |
| 21:21:24.201 | Client A   | Received new window size for | Client D | 1 |
| 21:21:24.416 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:103 |
| 21:21:24.480 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:103 |
| 21:21:24.480 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:103 |
| 21:21:24.527 | Client C   | Resending event |
| 21:21:24.591 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:46  | DATA: u this to tell   |
| 21:21:24.591 | Client C   | Resending event |
| 21:21:24.622 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:103 |
| 21:21:24.622 | Client A   | Received new window size for | Client D | 4 |
| 21:21:24.654 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:61  | DATA: you how much I   |
| 21:21:24.654 | Client C   | Resending event |
| 21:21:24.686 | Client A   | Sent     | tcp.TcpFinEvent                | To   | Switch A   |
| 21:21:24.717 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:76  | DATA: love your cooki  |
| 21:21:24.821 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:61  |
| 21:21:24.821 | Client C   | Received new window size for | Client B | 1 |
| 21:21:25.029 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:76  |
| 21:21:25.093 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:76  |
| 21:21:25.093 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:76  |
| 21:21:25.237 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:76  |
| 21:21:25.289 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:76  |
| 21:21:25.289 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:76  |
| 21:21:25.439 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:76  |
| 21:21:25.439 | Client C   | Received new window size for | Client B | 4 |
| 21:21:25.649 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:76  |
| 21:21:25.649 | Client C   | Received new window size for | Client B | 3 |
| 21:21:25.729 | Client C   | Resending event |
| 21:21:25.794 | Client C   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:91  | DATA: es               |
| 21:21:25.850 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:91  |
| 21:21:25.915 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:91  |
| 21:21:25.915 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:91  |
| 21:21:26.075 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:103 |
| 21:21:26.137 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:103 |
| 21:21:26.483 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:93  |
| 21:21:26.547 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:93  |
| 21:21:26.547 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:93  |
| 21:21:26.690 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:76  |
| 21:21:26.690 | Client C   | Received new window size for | Client B | 3 |
| 21:21:26.892 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:61  | DATA: just to say tha  |
| 21:21:26.956 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:61  | DATA: just to say tha  |
| 21:21:26.956 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:61  | DATA: just to say tha  |
| 21:21:27.100 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:76  | DATA: t you are an am  |
| 21:21:27.165 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:76  | DATA: t you are an am  |
| 21:21:27.165 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:76  | DATA: t you are an am  |
| 21:21:27.310 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Client A   | SEQ:91  | DATA: azing person     |
| 21:21:27.365 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:91  | DATA: azing person     |
| 21:21:27.365 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch B   | SEQ:91  | DATA: azing person     |
| 21:21:27.526 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:103 |
| 21:21:27.590 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:103 |
| 21:21:28.155 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:103 |
| 21:21:28.220 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:103 |
| 21:21:28.355 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:46  | DATA: u this to tell   |
| 21:21:28.419 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:46  | DATA: u this to tell   |
| 21:21:28.419 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:46  | DATA: u this to tell   |
| 21:21:28.565 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:61  | DATA: you how much I   |
| 21:21:28.630 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:61  | DATA: you how much I   |
| 21:21:28.630 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:61  | DATA: you how much I   |
| 21:21:28.773 | Switch A   | Received | tcp.TcpFinEvent                | From | Client A   |
| 21:21:28.829 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Client B   |
| 21:21:28.829 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Switch B   |
| 21:21:28.990 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:76  | DATA: love your cooki  |
| 21:21:29.053 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:76  | DATA: love your cooki  |
| 21:21:29.053 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:76  | DATA: love your cooki  |
| 21:21:29.197 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:76  |
| 21:21:29.262 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:76  |
| 21:21:29.608 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:76  |
| 21:21:29.671 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:76  |
| 21:21:30.018 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Client C   | SEQ:91  | DATA: es               |
| 21:21:30.081 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:91  | DATA: es               |
| 21:21:30.081 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Switch A   | SEQ:91  | DATA: es               |
| 21:21:30.428 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:91  |
| 21:21:30.490 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:91  |
| 21:21:30.634 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:103 |
| 21:21:30.840 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:93  |
| 21:21:30.901 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:93  |
| 21:21:31.252 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:61  | DATA: just to say tha  |
| 21:21:31.316 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:61  | DATA: just to say tha  |
| 21:21:31.854 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:76  | DATA: t you are an am  |
| 21:21:31.908 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:76  | DATA: t you are an am  |
| 21:21:32.275 | Switch B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:91  | DATA: azing person     |
| 21:21:32.339 | Switch B   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client D   | SEQ:91  | DATA: azing person     |
| 21:21:32.475 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:103 |
| 21:21:32.683 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:103 |
| 21:21:33.091 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:46  | DATA: u this to tell   |
| 21:21:33.156 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:46  | DATA: u this to tell   |
| 21:21:33.298 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:61  | DATA: you how much I   |
| 21:21:33.362 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:61  | DATA: you how much I   |
| 21:21:33.707 | Switch B   | Received | tcp.TcpFinEvent                | From | Switch A   |
| 21:21:33.772 | Switch B   | Sent     | tcp.TcpFinEvent                | To   | Client D   |
| 21:21:34.116 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:76  | DATA: love your cooki  |
| 21:21:34.180 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:76  | DATA: love your cooki  |
| 21:21:34.528 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:76  |
| 21:21:34.528 | Client C   | Received new window size for | Client B | 4 |
| 21:21:34.735 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:76  |
| 21:21:34.735 | Client C   | Received new window size for | Client B | 2 |
| 21:21:35.144 | Switch A   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:91  | DATA: es               |
| 21:21:35.206 | Switch A   | Sent     | tcp.TcpSendDataSegmentEvent    | To   | Client B   | SEQ:91  | DATA: es               |
| 21:21:35.351 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:91  |
| 21:21:35.351 | Client C   | Received new window size for | Client B | 1 |
| 21:21:35.554 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:93  |
| 21:21:35.554 | Client C   | Received new window size for | Client B | 3 |
| 21:21:35.616 | Client C   | Sent     | tcp.TcpFinEvent                | To   | Switch B   |
| 21:21:35.761 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:61  | DATA: just to say tha  |
| 21:21:35.823 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:103 |
| 21:21:35.975 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:76  | DATA: t you are an am  |
| 21:21:36.039 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:103 |
| 21:21:36.182 | Client D   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch B   | SEQ:91  | DATA: azing person     |
| 21:21:36.246 | Client D   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:103 |
| 21:21:36.389 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:46  | DATA: u this to tell   |
| 21:21:36.445 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:93  |
| 21:21:36.590 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:61  | DATA: you how much I   |
| 21:21:36.655 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:93  |
| 21:21:36.798 | Client D   | Received | tcp.TcpFinEvent                | From | Switch B   |
| 21:21:36.862 | Client D   | Sent     | tcp.TcpFinAckEvent             | To   | Switch B   |
| 21:21:36.917 | Client D   | Sent     | tcp.TcpFinEvent                | To   | Switch B   |
| 21:21:37.013 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:76  | DATA: love your cooki  |
| 21:21:37.075 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:93  |
| 21:21:37.217 | Client B   | Received | tcp.TcpSendDataSegmentEvent    | From | Switch A   | SEQ:91  | DATA: es               |
| 21:21:37.281 | Client B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:93  |
| 21:21:37.415 | Switch B   | Received | tcp.TcpFinEvent                | From | Client C   |
| 21:21:37.480 | Switch B   | Sent     | tcp.TcpFinEvent                | To   | Client D   |
| 21:21:37.480 | Switch B   | Sent     | tcp.TcpFinEvent                | To   | Switch A   |
| 21:21:37.638 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:103 |
| 21:21:37.702 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:103 |
| 21:21:37.702 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:103 |
| 21:21:37.845 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:103 |
| 21:21:37.909 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:103 |
| 21:21:37.909 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:103 |
| 21:21:38.047 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Client D   | ACK:103 |
| 21:21:38.112 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:103 |
| 21:21:38.112 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch A   | ACK:103 |
| 21:21:38.255 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:93  |
| 21:21:38.318 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:93  |
| 21:21:38.318 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:93  |
| 21:21:38.456 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:93  |
| 21:21:38.519 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:93  |
| 21:21:38.519 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:93  |
| 21:21:38.664 | Switch B   | Received | tcp.TcpFinAckEvent             | From | Client D   |
| 21:21:38.727 | Switch B   | Sent     | tcp.TcpFinAckEvent             | To   | Client C   |
| 21:21:38.727 | Switch B   | Sent     | tcp.TcpFinAckEvent             | To   | Switch A   |
| 21:21:38.872 | Switch B   | Received | tcp.TcpFinEvent                | From | Client D   |
| 21:21:38.923 | Switch B   | Sent     | tcp.TcpFinEvent                | To   | Client C   |
| 21:21:38.923 | Switch B   | Sent     | tcp.TcpFinEvent                | To   | Switch A   |
| 21:21:39.087 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:93  |
| 21:21:39.150 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:93  |
| 21:21:39.150 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:93  |
| 21:21:39.294 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Client B   | ACK:93  |
| 21:21:39.356 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:93  |
| 21:21:39.356 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Switch B   | ACK:93  |
| 21:21:39.510 | Switch A   | Received | tcp.TcpFinEvent                | From | Switch B   |
| 21:21:39.575 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Client B   |
| 21:21:40.142 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:103 |
| 21:21:40.207 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:103 |
| 21:21:40.349 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:103 |
| 21:21:40.413 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:103 |
| 21:21:40.772 | Switch A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:103 |
| 21:21:40.836 | Switch A   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client A   | ACK:103 |
| 21:21:41.180 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:93  |
| 21:21:41.244 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:93  |
| 21:21:41.590 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:93  |
| 21:21:41.653 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:93  |
| 21:21:41.997 | Switch A   | Received | tcp.TcpFinAckEvent             | From | Switch B   |
| 21:21:42.060 | Switch A   | Sent     | tcp.TcpFinAckEvent             | To   | Client A   |
| 21:21:42.416 | Switch A   | Received | tcp.TcpFinEvent                | From | Switch B   |
| 21:21:42.471 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Client A   |
| 21:21:43.027 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:93  |
| 21:21:43.089 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:93  |
| 21:21:43.234 | Switch B   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:93  |
| 21:21:43.299 | Switch B   | Sent     | tcp.TcpAckDataSegmentEvent     | To   | Client C   | ACK:93  |
| 21:21:43.648 | Client B   | Received | tcp.TcpFinEvent                | From | Switch A   |
| 21:21:43.713 | Client B   | Sent     | tcp.TcpFinAckEvent             | To   | Switch A   |
| 21:21:43.776 | Client B   | Sent     | tcp.TcpFinEvent                | To   | Switch A   |
| 21:21:43.856 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:103 |
| 21:21:44.056 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:103 |
| 21:21:44.262 | Client A   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch A   | ACK:103 |
| 21:21:44.470 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:93  |
| 21:21:44.675 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:93  |
| 21:21:44.881 | Client A   | Received | tcp.TcpFinAckEvent             | From | Switch A   |
| 21:21:45.096 | Client A   | Received | tcp.TcpFinEvent                | From | Switch A   |
| 21:21:45.160 | Client A   | Sent     | tcp.TcpFinAckEvent             | To   | Switch A   |
| 21:21:45.225 | Client A   | Sent     | tcp.TcpFinEvent                | To   | Switch A   |
| 21:21:45.306 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:93  |
| 21:21:45.506 | Client C   | Received | tcp.TcpAckDataSegmentEvent     | From | Switch B   | ACK:93  |
| 21:21:45.732 | Switch A   | Received | tcp.TcpFinAckEvent             | From | Client B   |
| 21:21:45.794 | Switch A   | Sent     | tcp.TcpFinAckEvent             | To   | Client A   |
| 21:21:45.794 | Switch A   | Sent     | tcp.TcpFinAckEvent             | To   | Switch B   |
| 21:21:45.939 | Switch A   | Received | tcp.TcpFinEvent                | From | Client B   |
| 21:21:45.990 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Client A   |
| 21:21:45.990 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Switch B   |
| 21:21:46.142 | Switch A   | Received | tcp.TcpFinAckEvent             | From | Client A   |
| 21:21:46.206 | Switch A   | Sent     | tcp.TcpFinAckEvent             | To   | Client B   |
| 21:21:46.206 | Switch A   | Sent     | tcp.TcpFinAckEvent             | To   | Switch B   |
| 21:21:46.352 | Switch A   | Received | tcp.TcpFinEvent                | From | Client A   |
| 21:21:46.417 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Client B   |
| 21:21:46.417 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Switch B   |
| 21:21:46.769 | Switch B   | Received | tcp.TcpFinAckEvent             | From | Switch A   |
| 21:21:46.833 | Switch B   | Sent     | tcp.TcpFinAckEvent             | To   | Client C   |
| 21:21:46.975 | Switch B   | Received | tcp.TcpFinEvent                | From | Switch A   |
| 21:21:47.031 | Switch B   | Sent     | tcp.TcpFinEvent                | To   | Client C   |
| 21:21:47.396 | Switch B   | Received | tcp.TcpFinAckEvent             | From | Switch A   |
| 21:21:47.461 | Switch B   | Sent     | tcp.TcpFinAckEvent             | To   | Client D   |
| 21:21:48.015 | Switch B   | Received | tcp.TcpFinEvent                | From | Switch A   |
| 21:21:48.072 | Switch B   | Sent     | tcp.TcpFinEvent                | To   | Client D   |
| 21:21:48.234 | Client C   | Received | tcp.TcpFinAckEvent             | From | Switch B   |
| 21:21:48.443 | Client C   | Received | tcp.TcpFinEvent                | From | Switch B   |
| 21:21:48.506 | Client C   | Sent     | tcp.TcpFinAckEvent             | To   | Switch B   |
| 21:21:48.560 | Client C   | Sent     | tcp.TcpFinEvent                | To   | Switch B   |
| 21:21:48.655 | Client D   | Received | tcp.TcpFinAckEvent             | From | Switch B   |
| 21:21:48.655 | Client D   | Received Data: Hi Client D this is Client A and i am sending you this data just to say that you are an amazing person |
| 21:21:48.864 | Client D   | Received | tcp.TcpFinEvent                | From | Switch B   |
| 21:21:49.063 | Switch B   | Received | tcp.TcpFinAckEvent             | From | Client C   |
| 21:21:49.126 | Switch B   | Sent     | tcp.TcpFinAckEvent             | To   | Client D   |
| 21:21:49.126 | Switch B   | Sent     | tcp.TcpFinAckEvent             | To   | Switch A   |
| 21:21:49.286 | Switch B   | Received | tcp.TcpFinEvent                | From | Client C   |
| 21:21:49.349 | Switch B   | Sent     | tcp.TcpFinEvent                | To   | Client D   |
| 21:21:49.349 | Switch B   | Sent     | tcp.TcpFinEvent                | To   | Switch A   |
| 21:21:49.492 | Switch A   | Received | tcp.TcpFinAckEvent             | From | Switch B   |
| 21:21:49.546 | Switch A   | Sent     | tcp.TcpFinAckEvent             | To   | Client B   |
| 21:21:49.902 | Switch A   | Received | tcp.TcpFinEvent                | From | Switch B   |
| 21:21:49.966 | Switch A   | Sent     | tcp.TcpFinEvent                | To   | Client B   |
| 21:21:50.327 | Client B   | Received | tcp.TcpFinAckEvent             | From | Switch A   |
| 21:21:50.327 | Client B   | Received Data: Hi Client B this is Client C, I am sending you this to tell you how much I love your cookies |
| 21:21:50.533 | Client B   | Received | tcp.TcpFinEvent                | From | Switch A   |

  ````
  

</details>


## Feeback
To be honest this project in our opinion was very difficult to implement. It could be that we went into too much details of creating a network, but we couldnt see a way to simplify it and still maintain the idea of creating a netowrk simulator. Maybe if we could have implement certain parts of the network it could have been fine. Overall it was a fun project to try and attempt, its just the time and effort needed is a bit too much for a project of a single course.


