import devices.Device;
import devices.Router;
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

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Main {

    private static final BlockingQueue<EventWithDirectSourceDestination> eventQueue = new PriorityBlockingQueue<>();

//    public static void main(String[] args) {
//        IpAddress subnetMask = new IpAddress(255, 255, 255, 0);
//
//        Switch switchA = new Switch("Switch A", "00:00:00:00:00:00", null, null, null, eventQueue);
//
//        Client clientA = new Client("Client A", "11:11:11:11:11:11", new IpAddress(192, 168, 1, 2), subnetMask, null,
//                new Link(switchA, 0), eventQueue
//        );
//
//        Client clientB = new Client("Client B", "12:11:11:11:11:11", new IpAddress(192, 168, 1, 3), subnetMask, null,
//                new Link(switchA, 0), eventQueue
//        );
//
//        switchA.addLinkedDevice(clientA);
//        switchA.addLinkedDevice(clientB);
//
//
//        ArpRequestEvent arpRequestEvent = new ArpRequestEvent(clientA, null, clientB.getIpAddress());
//        TcpSynEvent tcpSynEvent = new TcpSynEvent(clientA, clientB, 56, 23);
//        TcpSendDataEvent sendDataEvent = new TcpSendDataEvent(clientA, clientB,
//                "This is data This is data This is data This is dataThis is data This is data This is data".getBytes(),
//                56, 23, Device.WINDOW_SIZE
//        );
//
//        clientA.addOnReceivedEventListener(event -> {
//            if (event instanceof ArpResponseEvent) {
//                clientA.sendEvent(tcpSynEvent);
//            } else if (event instanceof TcpAckEvent) {
//                clientA.sendEvent(sendDataEvent);
//            }
//        });
//        clientA.addOnSentEventListener(event -> {
//            if (event instanceof TcpAckEvent) {
//                clientA.sendEvent(sendDataEvent);
//            }
//        });
//
//
//        clientA.start();
//        clientB.start();
//        switchA.start();
//
//        clientA.sendEvent(arpRequestEvent);
//
//
//        listenForQueueUpdates();
//    }


    public static void main(String[] args) {
        ArrayList<Router> routers = new ArrayList<>();
        IpAddress subnetMask = new IpAddress(255, 255, 255, 0);

        Switch switchA = new Switch("Switch A", randomMac(), null, null, null, eventQueue);

        Switch switchB = new Switch("Switch B", randomMac(), null, null, null, eventQueue);

        Router routerA = new Router("Router A", randomMac(), new IpAddress(192, 168, 1, 1), subnetMask, null,
                new Link(switchA, 0), eventQueue, routers
        );

        Router routerB = new Router("Router B", randomMac(), new IpAddress(192, 168, 2, 1), subnetMask, null,
                new Link(switchB, 0), eventQueue, routers
        );

        routers.add(routerA);
        routers.add(routerB);

        routerA.addLinkedDevice(routerB);
        routerB.addLinkedDevice(routerA);

        Client clientA = new Client("Client A", randomMac(), new IpAddress(192, 168, 1, 2), subnetMask, routerA,
                new Link(switchA, 0), eventQueue
        );

        Client clientB = new Client("Client B", randomMac(), new IpAddress(192, 168, 2, 3), subnetMask, routerB,
                new Link(switchB, 0), eventQueue
        );

        switchA.addLinkedDevice(clientA);
        switchB.addLinkedDevice(clientB);


        TcpSynEvent tcpSynEvent = new TcpSynEvent(clientA, clientB, 56, 23);
        TcpSendDataEvent sendDataEvent = new TcpSendDataEvent(clientA, clientB,
                "This is data This is data This is data This is dataThis is data This is data This is data".getBytes(),
                56, 23, Device.WINDOW_SIZE
        );

        clientA.addOnReceivedEventListener(event -> {
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
        switchB.start();
        routerA.start();
        routerB.start();

        clientA.sendEvent(tcpSynEvent);


        listenForQueueUpdates();
    }


    private static void listenForQueueUpdates() {
        while (true) {
            EventWithDirectSourceDestination peeked = eventQueue.peek();
            if (peeked != null) {
                synchronized (peeked.getDestination()) {
                    peeked.getDestination().notify();
                }
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Interrupted queue listener");
                break;
            }
        }
    }

    private static final ArrayList<String> usedMacs = new ArrayList<>();

    public static String randomMac() {
        Random random = new Random();
        String[] mac = {String.format("%02x", random.nextInt(0xff)), String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)), String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)), String.format("%02x", random.nextInt(0xff))};
        String address = String.join(":", mac);
        if (usedMacs.contains(address)) {
            return randomMac();
        }
        usedMacs.add(address);
        return address;
    }
}