package model.devices;

import events.Event;
import model.IpAddress;
import model.Link;
import model.TcpConnection;

import java.util.ArrayList;


public abstract class Device {
    private final String name;
    private final String macAddress;
    private final IpAddress ipAddress;
    private final IpAddress subnetMask;
    private final Device defaultGateway;
    private final ArrayList<Link> linkedDevices;
    private final ArrayList<TcpConnection> tcpConnections = new ArrayList<>();
    private final ArrayList<TcpConnection> tcpConnectionsWithSynSent = new ArrayList<>();
    private final ArrayList<TcpConnection> tcpConnectionsWithFinSent = new ArrayList<>();
    private final ArrayList<TcpConnection> tcpConnectionWithFinReceived = new ArrayList<>();


    public Device(String name, String macAddress, IpAddress ipAddress, IpAddress subnetMask, Device defaultGateway, ArrayList<Link> linkedDevices) {
        this.name = name;
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.subnetMask = subnetMask;
        this.defaultGateway = defaultGateway;
        this.linkedDevices = linkedDevices;
    }

    public String getName() {
        return name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    public IpAddress getSubnetMask() {
        return subnetMask;
    }

    public Device getDefaultGateway() {
        return defaultGateway;
    }

    public ArrayList<Link> getLinkedDevices() {
        return linkedDevices;
    }

    public ArrayList<TcpConnection> getTcpConnections() {
        return tcpConnections;
    }

    public ArrayList<TcpConnection> getTcpConnectionsWithSynSent() {
        return tcpConnectionsWithSynSent;
    }

    public ArrayList<TcpConnection> getTcpConnectionsWithFinSent() {
        return tcpConnectionsWithFinSent;
    }

    public ArrayList<TcpConnection> getTcpConnectionWithFinReceived() {
        return tcpConnectionWithFinReceived;
    }

    protected abstract void processReceivedEvent(Event event);


    public void sendEvent(Event event) {
        Device destination = event.getDestination();
        Device source = event.getSource();

        if (!currentIsSource(source)) {
            return;
        }

        if (!isLinkedDevice(destination)) {
            return;
        }

        destination.processReceivedEvent(event);
    }

    private boolean currentIsSource(Device source) {
        if (source != this) {
            System.out.println("ERROR: Cannot send event where source device is different from current" +
                    "\n Current IP: " + ipAddress + " Current MAC: " + macAddress +
                    "\n Source IP: " + source.ipAddress + " Dest MAC: " + source.macAddress);
            return false;
        }
        return true;
    }

    private boolean isLinkedDevice(Device destination) {
        boolean isLinkedDevice = false;

        for (Link link : linkedDevices) {
            if (link.getLinkedDevice() == destination) {
                isLinkedDevice = true;
                break;
            }
        }

        if (!isLinkedDevice) {
            System.out.println("ERROR: Cannot send event to device not directly linked." +
                    "\n Source IP: " + ipAddress + " Source MAC: " + macAddress +
                    "\n Dest IP: " + destination.ipAddress + " Dest MAC: " + destination.macAddress);
        }

        return isLinkedDevice;
    }

}
