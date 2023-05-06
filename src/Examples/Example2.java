package Examples;

import devices.Device;
import devices.Router;
import devices.Switch;
import devices.client.Client;
import events.EventWithDirectSourceDestination;
import events.tcp.TcpAckEvent;
import events.tcp.TcpSendDataEvent;
import events.tcp.TcpSynEvent;
import model.IpAddress;
import model.Link;
import routing_strategy.DijkstraRoutingStrategy;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

public class Example2 {


    private static final BlockingQueue<EventWithDirectSourceDestination> eventQueue = new PriorityBlockingQueue<>();

    public static void main(String[] args) {
        ArrayList<Router> routers = new ArrayList<>();
        IpAddress subnetMask = new IpAddress(255, 255, 255, 0);

        Switch switchA = new Switch("Switch A", Util.randomMac(), null, null, null, eventQueue);

        Switch switchB = new Switch("Switch B", Util.randomMac(), null, null, null, eventQueue);

        Router routerA = new Router("Router A", Util.randomMac(), new IpAddress(192, 168, 1, 1), subnetMask, null,
                new Link(switchA, 0), eventQueue, routers
        );

        Router routerB = new Router("Router B", Util.randomMac(), new IpAddress(192, 168, 2, 1), subnetMask, null,
                new Link(switchB, 0), eventQueue, routers
        );

        routers.add(routerA);
        routers.add(routerB);

        routerA.setRoutingStrategy(new DijkstraRoutingStrategy());
        routerB.setRoutingStrategy(new DijkstraRoutingStrategy());

        routerA.addLinkedDevice(routerB, 0);
        routerB.addLinkedDevice(routerA, 0);

        routerA.buildRoutes();
        routerB.buildRoutes();

        Client clientA = new Client("Client A", Util.randomMac(), new IpAddress(192, 168, 1, 2), subnetMask, routerA,
                new Link(switchA, 0), eventQueue
        );

        Client clientB = new Client("Client B", Util.randomMac(), new IpAddress(192, 168, 2, 3), subnetMask, routerB,
                new Link(switchB, 0), eventQueue
        );


        switchA.addLinkedDevice(clientA);
        switchB.addLinkedDevice(clientB);


        TcpSynEvent tcpSynEvent = new TcpSynEvent(clientA, clientB, 56, 23);
        TcpSendDataEvent sendDataEvent = new TcpSendDataEvent(clientA, clientB,
                "This is some data that I am sending through this very complicated network simulation that I made".getBytes(),
                56, 23, Device.INITIAL_WINDOW_SIZE
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


        Util.listenForQueueUpdates(eventQueue);
    }

}
