package routing_strategy;

import devices.Router;
import model.Link;

import java.util.*;

public class DijkstraRoutingStrategy extends RoutingStrategy {
    @Override
    public Router findBestNextHop(Router from, Router to) {
        Router[] result = dijkstraSearch(from, to);
        return result[1];
    }

    private Router[] dijkstraSearch(Router from, Router to) {
        Set<Router> visited = new HashSet<>();
        Set<Router> unvisited = new HashSet<>();
        Map<Router, Router> parents = new HashMap<>();
        List<Router> correctPath = new LinkedList<>();
        Map<Router, Integer> roundTripTime = new HashMap<>();
        visited.add(from);

        //fills all unvisited Routers
        fillWithAllRoutersFrom(from, unvisited);

        unvisited.remove(from);

        //initialize all Routers to be max
        for (Router r : unvisited) {
            parents.put(r, from);
            for (Link l : r.getLinkedDevices()) {
                roundTripTime.put(r, Integer.MAX_VALUE);
            }
        }

        //initialize adjacent Routers to be their actual round trip time
        for (Router r : unvisited) {
            parents.put(r, from);
            for (Link l : r.getLinkedDevices()) {
                if (l.getLinkedDevice() == from) {
                    roundTripTime.put(r, l.getRoundTripTime());
                }
            }
        }

        while (!unvisited.isEmpty()) {
            Router smallest = unvisited.iterator().next();
            for (Router r : unvisited) {
                if (roundTripTime.get(r) < roundTripTime.get(smallest)) {
                    smallest = r;
                }
            }
            unvisited.remove(smallest);
            visited.add(smallest);

            for (Link l : smallest.getLinkedDevices()) {
                Router current = (Router) l.getLinkedDevice();
                if (roundTripTime.get(current) != null && roundTripTime.get(smallest) + l.getRoundTripTime() < roundTripTime.get(current)) {
                    roundTripTime.put(current, roundTripTime.get(smallest) + l.getRoundTripTime());
                    parents.put(current, smallest);
                }
            }
        }

        return makeArrayReverse(parents, to, correctPath);
    }

    private Router[] makeArrayReverse(Map<Router, Router> parents, Router target, List<Router> correctPath) {
        //putting the shortest path in the arraylist
        for (Router n2 = target; n2 != null; n2 = parents.get(n2)) {
            correctPath.add(n2);
        }

        //reverse it since we put it from target to start
        Collections.reverse(correctPath);
        Router[] path = new Router[correctPath.size()];
        path = correctPath.toArray(path);
        return path;
    }



}
