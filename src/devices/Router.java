package devices;

import events.Event;
import events.EventWithDirectSourceDestination;
import events.OnEvent;
import model.IpAddress;
import model.Link;
import model.Route;
import routing_strategy.DijkstraRoutingStrategy;
import routing_strategy.RoutingStrategy;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class Router extends Device {

    public final ArrayList<Route> routingTable = new ArrayList<>();
    public final ArrayList<Router> allRouters;
    private final ArrayList<Link> linkedDevices = new ArrayList<>();
    private RoutingStrategy routingStrategy = new DijkstraRoutingStrategy();


    public Router(String name, String macAddress, IpAddress ipAddress, IpAddress subnetMask, Device defaultGateway,
                  Link networkLink, BlockingQueue<EventWithDirectSourceDestination> eventQueue,
                  ArrayList<Router> allRouters
    ) {
        super(name, macAddress, ipAddress, subnetMask, defaultGateway, networkLink, eventQueue);
        this.allRouters = allRouters;
    }


    @Override
    public boolean processSentEvent(Device destination, Event event) {
        logSentEvent(event, destination);
        for (OnEvent onSentEvent : onSentEventListeners) {
            onSentEvent.onEvent(event);
        }
        return true;
    }

    @Override
    public void processReceivedEvent(Device source, Event event) {
        pauseBeforeReceiving();
        logReceivedEvent(event, source);
        for (OnEvent onReceivedEvent : onReceivedEventListeners) {
            onReceivedEvent.onEvent(event);
        }
        sendEvent(event);
    }

    @Override
    public void sendEvent(Event event) {
        pauseBeforeSending(event);
        Router toRouter = findRouterForDevice(event.getDestination());
        if (toRouter == this) {
            processSentEvent(networkLink.getLinkedDevice(), event);
            sendEventToDevice(networkLink.getLinkedDevice(), event);
        } else if (toRouter != null) {
            Router nextHop = getNextHopForDestination(toRouter);
            processSentEvent(nextHop, event);
            sendEventToDevice(nextHop, event);
        }
    }

    private Router findRouterForDevice(Device device) {
        for (Router router : allRouters) {
            if (DeviceUtil.isInSameNetwork(router, device)) {
                return router;
            }
        }
        return null;
    }

    private Router getNextHopForDestination(Router destination) {
        for (Route route : routingTable) {
            if (route.getDestination() == destination) {
                return route.getNextHop();
            }
        }
        return null;
    }

    public void addLinkedDevice(Device device, int roundTripTime) {
        this.linkedDevices.add(new Link(device, roundTripTime));
    }

    public void setRoutingStrategy(RoutingStrategy routingStrategy) {
        this.routingStrategy = routingStrategy;
    }

    public void buildRoutes() {
        for (Router r : allRouters) {
            if (r != this) {
                routingTable.add(findBestPathTo(r));
            }
        }
    }

    private Route findBestPathTo(Router r) {
        Router nextHop = routingStrategy.findBestNextHop(this, r);
        return new Route(r, nextHop);
    }

    public ArrayList<Link> getLinkedDevices() {
        return linkedDevices;
    }
}
