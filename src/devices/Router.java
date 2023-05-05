package devices;

import events.Event;
import events.EventWithDirectSourceDestination;
import events.OnEvent;
import model.IpAddress;
import model.Link;
import model.Route;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

public class Router extends Device {

    public final ArrayList<Route> routingTable = new ArrayList<>();
    private final ArrayList<Link> linkedDevices = new ArrayList<>();
    public final ArrayList<Router> allRouters;


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
        logReceivedEvent(event, source);
        for (OnEvent onReceivedEvent : onReceivedEventListeners) {
            onReceivedEvent.onEvent(event);
        }
        sendEvent(event);
    }

    @Override
    public void sendEvent(Event event) {
        Router toRouter = findRouterForDevice(event.getDestination());
        if (toRouter == this) {
            processSentEvent(networkLink.getLinkedDevice(), event);
            sendEventToDevice(networkLink.getLinkedDevice(), event);
        } else if(toRouter != null) {
            processSentEvent(toRouter, event);
            sendEventToDevice(getNextHopForDestination(toRouter), event);
        }
    }

    private Router findRouterForDevice(Device device){
        for(Router router : allRouters){
            if(DeviceUtil.isInSameNetwork(router, device)){
                return router;
            }
        }
        return null;
    }

    private Router getNextHopForDestination(Router destination){
        for(Route route: routingTable){
            if(route.getDestination() == destination){
                return route.getNextHop();
            }
        }
        return null;
    }

    public void addLinkedDevice(Device device) {
        this.linkedDevices.add(new Link(device, 0));
        buildRoutes();
    }

    private void buildRoutes() {
        routingTable.clear();
        for (Router r : allRouters) {
            routingTable.add(findBestPathTo(r));
        }
    }

    private Route findBestPathTo(Router r) {
        if (isLinkedDevice(r)) {
            return new Route(r, r);
        }
        //Dijisktra here to find next hop
        return new Route(r, r);
    }

    private boolean isLinkedDevice(Device device) {
        for (Link l : linkedDevices) {
            if (l.getLinkedDevice() == device) {
                return true;
            }
        }
        return false;
    }
}
