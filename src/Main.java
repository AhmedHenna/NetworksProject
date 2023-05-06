import devices.Device;
import devices.Router;
import devices.Switch;
import devices.client.Client;
import events.EventWithDirectSourceDestination;
import events.arp.ArpResponseEvent;
import events.tcp.TcpAckEvent;
import events.tcp.TcpSendDataEvent;
import events.tcp.TcpSynEvent;
import model.IpAddress;
import model.Link;
import routing_strategy.BellmanFordRoutingStrategy;
import routing_strategy.DijkstraRoutingStrategy;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Main {

    private static final BlockingQueue<EventWithDirectSourceDestination> eventQueue = new PriorityBlockingQueue<>();


    public static void main(String[] args) {
        ArrayList<Router> routers = new ArrayList<>();
        IpAddress subnetMask = new IpAddress(255, 255, 255, 0);

        Switch switchA = new Switch("Switch A", randomMac(), null, null, null, eventQueue);

        Switch switchB = new Switch("Switch B", randomMac(), null, null, null, eventQueue);

        Switch switchC = new Switch("Switch C", randomMac(), null, null, null, eventQueue);

        Switch switchD = new Switch("Switch D", randomMac(), null, null, null, eventQueue);

        Router routerA = new Router("Router A", randomMac(), new IpAddress(192, 168, 1, 1), subnetMask, null,
                new Link(switchA, 0), eventQueue, routers
        );

        Router routerB = new Router("Router B", randomMac(), new IpAddress(192, 168, 2, 1), subnetMask, null,
                new Link(switchB, 0), eventQueue, routers
        );

        Router routerC = new Router("Router C", randomMac(), new IpAddress(192, 168, 3, 1), subnetMask, null,
                new Link(switchC, 0), eventQueue, routers
        );

        Router routerD = new Router("Router D", randomMac(), new IpAddress(192, 168, 4, 1), subnetMask, null,
                new Link(switchD, 0), eventQueue, routers
        );

        routers.add(routerA);
        routers.add(routerB);
        routers.add(routerC);
        routers.add(routerD);

        routerA.setRoutingStrategy(new DijkstraRoutingStrategy());
        routerB.setRoutingStrategy(new DijkstraRoutingStrategy());
        routerC.setRoutingStrategy(new DijkstraRoutingStrategy());
        routerD.setRoutingStrategy(new DijkstraRoutingStrategy());

        routerA.addLinkedDevice(routerB, 6);
        routerA.addLinkedDevice(routerC, 3);

        routerB.addLinkedDevice(routerA, 6);
        routerB.addLinkedDevice(routerC, 2);
        routerB.addLinkedDevice(routerD, 2);

        routerC.addLinkedDevice(routerA, 3);
        routerC.addLinkedDevice(routerB, 2);
        routerC.addLinkedDevice(routerD, 5);

        routerD.addLinkedDevice(routerB, 2);
        routerD.addLinkedDevice(routerC, 5);

        routerA.buildRoutes();
        routerB.buildRoutes();
        routerC.buildRoutes();
        routerD.buildRoutes();

        Client clientA = new Client("Client A", randomMac(), new IpAddress(192, 168, 1, 2), subnetMask, routerA,
                new Link(switchA, 0), eventQueue
        );

        Client clientB = new Client("Client B", randomMac(), new IpAddress(192, 168, 2, 3), subnetMask, routerB,
                new Link(switchB, 0), eventQueue
        );

        Client clientC = new Client("Client C", randomMac(), new IpAddress(192, 168, 3, 3), subnetMask, routerC,
                new Link(switchC, 0), eventQueue
        );

        Client clientD = new Client("Client D", randomMac(), new IpAddress(192, 168, 4, 3), subnetMask, routerD,
                new Link(switchD, 0), eventQueue
        );

        switchA.addLinkedDevice(clientA);
        switchB.addLinkedDevice(clientB);
        switchC.addLinkedDevice(clientC);
        switchD.addLinkedDevice(clientD);


        TcpSynEvent tcpSynEvent = new TcpSynEvent(clientA, clientD, 56, 23);
        TcpSendDataEvent sendDataEvent = new TcpSendDataEvent(clientA, clientD,
                "This is some data that I am sending through this very complicated network simulation that I made".getBytes(),
                56, 23, Device.INITIAL_WINDOW_SIZE
        );

        clientA.addOnReceivedEventListener(event -> {
            if (event instanceof ArpResponseEvent) {
                clientA.sendEvent(tcpSynEvent);
            } else if (event instanceof TcpAckEvent) {
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
        clientC.start();
        clientD.start();

        switchA.start();
        switchB.start();
        switchC.start();
        switchD.start();

        routerA.start();
        routerB.start();
        routerC.start();
        routerD.start();

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
                Thread.sleep(200);
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