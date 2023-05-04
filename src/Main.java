import devices.Device;
import devices.Switch;
import devices.client.Client;
import events.Event;
import events.arp.ArpRequestEvent;
import events.tcp.TcpSendDataEvent;
import events.tcp.TcpSynEvent;
import model.IpAddress;
import model.Link;

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
        TcpSendDataEvent sendDataEvent = new TcpSendDataEvent(clientA, clientB, "This is data This is data This is data This is dataThis is data This is data This is data".getBytes(), 56, 23, Device.WINDOW_SIZE);

        ArrayList<Event> events = new ArrayList<>();
        events.add(arpRequestEvent);
        events.add(tcpSynEvent);
        events.add(sendDataEvent);

        sendEvents(events);
    }

    private static void sendEvents(ArrayList<Event> events) {
        for (Event event : events) {
            event.getSource().sendEvent(event);
        }
    }
}