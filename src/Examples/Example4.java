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

public class Example4 {

    private static final BlockingQueue<EventWithDirectSourceDestination> eventQueue = new PriorityBlockingQueue<>();

    public static void main(String[] args) {
        IpAddress subnetMask = new IpAddress(255, 255, 255, 0);

        Switch switchA = new Switch("Switch A", Util.randomMac(), null, null, null, eventQueue);

        Switch switchB = new Switch("Switch B", Util.randomMac(), null, null, null, eventQueue);


        Client clientA = new Client("Client A", Util.randomMac(), new IpAddress(192, 168, 1, 2), subnetMask, null,
                new Link(switchA, 0), eventQueue
        );

        Client clientB = new Client("Client B", Util.randomMac(), new IpAddress(192, 168, 1, 3), subnetMask, null,
                new Link(switchA, 0), eventQueue
        );

        Client clientC = new Client("Client C", Util.randomMac(), new IpAddress(192, 168, 1, 4), subnetMask, null,
                new Link(switchB, 0), eventQueue
        );

        Client clientD = new Client("Client D", Util.randomMac(), new IpAddress(192, 168, 1, 5), subnetMask, null,
                new Link(switchB, 0), eventQueue
        );

        switchA.addLinkedDevice(clientA);
        switchA.addLinkedDevice(clientB);
        switchA.addLinkedDevice(switchB);

        switchB.addLinkedDevice(clientC);
        switchB.addLinkedDevice(clientD);
        switchB.addLinkedDevice(switchA);


        ArpRequestEvent arpRequestEvent = new ArpRequestEvent(clientA, clientD, clientD.getIpAddress());

        TcpSynEvent tcpSynEvent = new TcpSynEvent(clientA, clientD, 56, 23);
        TcpSendDataEvent sendDataEvent = new TcpSendDataEvent(clientA, clientD,
                "Hi Client D this is Client A and i am sending you this data just to say that you are an amazing person".getBytes(),
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

        ArpRequestEvent arpRequestEvent2 = new ArpRequestEvent(clientC, clientB, clientB.getIpAddress());
        TcpSynEvent tcpSynEvent2 = new TcpSynEvent(clientC, clientB, 56, 23);
        TcpSendDataEvent sendDataEvent2 = new TcpSendDataEvent(clientC, clientB,
                "Hi Client B this is Client C, I am sending you this to tell you how much I love your cookies".getBytes(),
                56, 23, Device.INITIAL_WINDOW_SIZE
        );

        clientC.addOnReceivedEventListener(event -> {
            if (event instanceof ArpResponseEvent) {
                clientC.sendEvent(tcpSynEvent2);
            }
            if (event instanceof TcpAckEvent) {
                clientC.sendEvent(sendDataEvent2);
            }
        });
        clientC.addOnSentEventListener(event -> {
            if (event instanceof TcpAckEvent) {
                clientC.sendEvent(sendDataEvent2);
            }
        });


        clientA.start();
        clientB.start();
        clientC.start();
        clientD.start();

        switchA.start();
        switchB.start();

        clientA.sendEvent(arpRequestEvent);
        clientC.sendEvent(arpRequestEvent2);

        Util.listenForQueueUpdates(eventQueue);
    }

}