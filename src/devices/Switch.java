package devices;

import devices.Device;
import events.Event;
import model.IpAddress;
import model.Link;

import java.util.ArrayList;

public class Switch extends Device {

    private final ArrayList<Link> linkedDevices = new ArrayList<>();

    public Switch(String name, String macAddress, IpAddress ipAddress, IpAddress subnetMask, Device defaultGateway) {
        super(name, macAddress, ipAddress, subnetMask, defaultGateway, null);
    }

    @Override
    public void sendEvent(Event event) {
        System.err.println("sendEvent unimplemented in Switch, use sendSwitchEvent");
    }

    private void sendSwitchEvent(Event event, Device eventSource) {
        Device destination = event.getDestination();
        boolean isLinkedToDest = false;

        for (Link link : linkedDevices) {
            if (link.getLinkedDevice() == destination) {
                isLinkedToDest = true;
                break;
            }
        }
        if (isLinkedToDest) {
            processSentEvent(destination, event);
            destination.processReceivedEvent(this, event);
        } else {
            sendToAll(event, eventSource);
        }
    }

    private void sendToAll(Event event, Device eventSource) {
        for (Link link : linkedDevices) {
            if (link.getLinkedDevice() != eventSource) {
                processSentEvent(link.getLinkedDevice(), event);
                link.getLinkedDevice().processReceivedEvent(this, event);
            }
        }
    }

    @Override
    public void processReceivedEvent(Device source, Event event) {
        logReceivedEvent(event, source);
        if (event.getPacket().getDestinationMacAddress().equals("ff:ff:ff:ff:ff:ff")) {
            sendToAll(event, source);
        } else {
            sendSwitchEvent(event, source);
        }
    }

    @Override
    public void processSentEvent(Device destination, Event event) {
        logSentEvent(event, destination);
    }

    public void addLinkedDevice(Device device) {
        this.linkedDevices.add(new Link(device, 0, 0, 0));
    }
}
