package routing_strategy;

import devices.Router;
import model.Route;

public interface RoutingStrategy {
    Router findBestNextHop(Router from, Router to);
}
