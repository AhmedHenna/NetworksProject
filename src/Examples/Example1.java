package Examples;

import devices.Device;
import devices.Switch;
import devices.client.Client;
import events.EventWithDirectSourceDestination;
import events.arp.ArpRequestEvent;
import events.arp.ArpResponseEvent;
import events.tcp.TcpAckEvent;
import events.tcp.TcpSendDataEvent;
import events.tcp.TcpSynEvent;
import model.IpAddress;
import model.Link;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Example1 {

    private static final BlockingQueue<EventWithDirectSourceDestination> eventQueue = new PriorityBlockingQueue<>();

    public static void main(String[] args) {
        IpAddress subnetMask = new IpAddress(255, 255, 255, 0);

        Switch switchA = new Switch("Switch A", Util.randomMac(), null, null, null, eventQueue);

        Client clientA = new Client("Client A", Util.randomMac(), new IpAddress(192, 168, 1, 2), subnetMask, null,
                new Link(switchA, 0), eventQueue
        );

        Client clientB = new Client("Client B", Util.randomMac(), new IpAddress(192, 168, 1, 3), subnetMask, null,
                new Link(switchA, 0), eventQueue
        );


        switchA.addLinkedDevice(clientA);
        switchA.addLinkedDevice(clientB);


        ArpRequestEvent arpRequestEvent = new ArpRequestEvent(clientA, clientB, clientB.getIpAddress());

        TcpSynEvent tcpSynEvent = new TcpSynEvent(clientA, clientB, 56, 23);
        TcpSendDataEvent sendDataEvent = new TcpSendDataEvent(clientA, clientB,
                "Just testing out this long message to make sure that evreything works".getBytes(),
                56, 23, Device.INITIAL_WINDOW_SIZE
        );

        clientA.addOnReceivedEventListener(event -> {
            if (event instanceof ArpResponseEvent) {
                clientA.sendEvent(tcpSynEvent);
            }
            if (event instanceof TcpAckEvent) {
                clientA.sendEvent(sendDataEvent);
            }
        });
        clientA.addOnSentEventListener(event -> {
            if (event instanceof TcpAckEvent) {
                clientA.sendEvent(sendDataEvent);
            }
        });


        clientA.start();
        clientB.start();
        switchA.start();
        clientA.sendEvent(arpRequestEvent);

        Util.listenForQueueUpdates(eventQueue);
    }

}