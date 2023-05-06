package routing_strategy;

import devices.Router;
import model.Link;

import java.util.Set;

public abstract class RoutingStrategy {
    protected static void fillWithAllRoutersFrom(Router r, Set<Router> routers) {
        routers.add(r);
        for (Link l : r.getLinkedDevices()) {
            Router linkedRouter = (Router) l.getLinkedDevice();
            if (!routers.contains(linkedRouter)) {
                fillWithAllRoutersFrom(linkedRouter, routers);
            }
        }
    }

    public abstract Router findBestNextHop(Router from, Router to);
}
