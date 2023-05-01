import events.Event;
import events.arp.ArpRequestEvent;
import events.tcp.TcpSynEvent;
import model.IpAddress;
import model.Link;
import model.devices.Client;
import model.devices.Switch;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        IpAddress subnetMask = new IpAddress(255, 255, 255, 0);

        Switch switchA = new Switch("Switch A", "00:00:00:00:00:00", null, null, null);

        Client clientA = new Client(
                "Client A",
                "11:11:11:11:11:11",
                new IpAddress(192, 168, 1, 2),
                subnetMask,
                null,
                new Link(switchA, 0, 0, 0)
        );

        Client clientB = new Client(
                "Client B",
                "12:11:11:11:11:11",
                new IpAddress(192, 168, 1, 3),
                subnetMask,
                null,
                new Link(switchA, 0, 0, 0)
        );

        switchA.addLinkedDevice(clientA);
        switchA.addLinkedDevice(clientB);


        ArpRequestEvent arpRequestEvent = new ArpRequestEvent(clientA, null, clientB.getIpAddress());
        TcpSynEvent tcpSynEvent = new TcpSynEvent(clientA, clientB, 56, 23);


        ArrayList<Event> events = new ArrayList<>();
        events.add(arpRequestEvent);
        events.add(tcpSynEvent);

        sendEvents(events);
    }

    private static void sendEvents(ArrayList<Event> events) {
        for (Event event : events) {
            event.getSource().sendEvent(event);
        }
    }
}