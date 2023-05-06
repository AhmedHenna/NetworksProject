package model;

import devices.Router;

public class Route {
    private final Router destination;
    private final Router nextHop;


    public Route(Router destination, Router nextHop) {
        this.destination = destination;
        this.nextHop = nextHop;
    }

    public Router getDestination() {
        return destination;
    }

    public Router getNextHop() {
        return nextHop;
    }
}
