package routing_strategy;

import devices.Router;
import model.Link;

import java.util.*;

public class BellmanFordRoutingStrategy extends RoutingStrategy {
    @Override
    public Router findBestNextHop(Router from, Router to) {
        Router[] result = bellmanFordSearch(from, to);
        return result[1];
    }

    private Router[] bellmanFordSearch(Router from, Router to) {
        // Initialize distance array and predecessor array
        Map<Router, Integer> distance = new HashMap<>();
        Map<Router, Router> predecessor = new HashMap<>();
        Set<Router> allRouters = new HashSet<>();
        fillWithAllRoutersFrom(from, allRouters);

        for (Router router : allRouters) {
            distance.put(router, Integer.MAX_VALUE);
            predecessor.put(router, null);
        }
        distance.put(from, 0);


        // Relax edges V-1 times
        for (int i = 0; i < allRouters.size() - 1; i++) {
            for (Router router : allRouters) {
                for (Link link : router.getLinkedDevices()) {
                    Router neighbor = (Router) link.getLinkedDevice();
                    int weight = link.getRoundTripTime();
                    if (distance.get(router) != Integer.MAX_VALUE && distance.get(router) + weight < distance.get(neighbor)) {
                        distance.put(neighbor, distance.get(router) + weight);
                        predecessor.put(neighbor, router);
                    }
                }
            }
        }

        // Check for negative weight cycles
        for (Router router : allRouters) {
            for (Link link : router.getLinkedDevices()) {
                Router neighbor = (Router) link.getLinkedDevice();
                int weight = link.getRoundTripTime();
                if (distance.get(router) != Integer.MAX_VALUE && distance.get(router) + weight < distance.get(neighbor)) {
                    throw new RuntimeException("Graph contains negative weight cycle");
                }
            }
        }

        // Shortest path using predecessor array
        List<Router> path = new ArrayList<>();
        Router current = to;
        while (current != null) {
            path.add(0, current);
            current = predecessor.get(current);
        }
        return path.toArray(new Router[0]);
    }


}
