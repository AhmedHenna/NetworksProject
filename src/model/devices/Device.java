package model.devices;

import events.Event;
import model.IpAddress;
import model.Link;
import model.TcpConnection;

import java.util.ArrayList;


public abstract class Device {

    //MSS is 1460 bytes, downscaled for testing purposes 1460/100 = 14.6 ~= 15
    public static int MSS = 15;

    //Number of event allowed at a given point
    public static int WINDOW_SIZE = 3;
    private final String name;
    private final String macAddress;
    private final IpAddress ipAddress;
    private final IpAddress subnetMask;
    private final Device defaultGateway;
    protected final Link networkLink;
    protected final ArrayList<TcpConnection> tcpConnections = new ArrayList<>();
    protected final ArrayList<TcpConnection> tcpConnectionsWithSynSent = new ArrayList<>();
    protected final ArrayList<TcpConnection> tcpConnectionsWithFinInitiated = new ArrayList<>();

    protected final ArrayList<TcpConnection> tcpConnectionsWithFinReceived = new ArrayList<>();


    public Device(String name, String macAddress, IpAddress ipAddress, IpAddress subnetMask, Device defaultGateway, Link networkLink) {
        this.name = name;
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.subnetMask = subnetMask;
        this.defaultGateway = defaultGateway;
        this.networkLink = networkLink;
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

    public Link getNetworkLink() {
        return networkLink;
    }

    public ArrayList<TcpConnection> getTcpConnections() {
        return tcpConnections;
    }

    public ArrayList<TcpConnection> getTcpConnectionsWithSynSent() {
        return tcpConnectionsWithSynSent;
    }


    public ArrayList<TcpConnection> getTcpConnectionsWithFinInitiated() {
        return tcpConnectionsWithFinInitiated;
    }

    public ArrayList<TcpConnection> getTcpConnectionsWithFinReceived() {
        return tcpConnectionsWithFinReceived;
    }

    protected abstract void processReceivedEvent(Device source, Event event);

    protected abstract void processSentEvent(Device destination, Event event);


    public abstract void sendEvent(Event event);

    protected boolean currentIsSource(Device source) {
        if (source != this) {
            System.out.println("ERROR: Cannot send event where source device is different from current" +
                    "\n Current IP: " + ipAddress + " Current MAC: " + macAddress +
                    "\n Source IP: " + source.ipAddress + " Dest MAC: " + source.macAddress);
            return false;
        }
        return true;
    }

    protected void logSentEvent(Event event, Device destination) {
        System.out.println(System.currentTimeMillis() + ": " + this + " Sending event: " + event.getClass() + " To: " + destination);
    }

    protected void logReceivedEvent(Event event,  Device source) {
        System.out.println(System.currentTimeMillis() + ": " + this + " Received event: " + event.getClass() + " From: " + source);
    }

    @Override
    public String toString() {
        return getName() + " - " + macAddress;
    }
}
