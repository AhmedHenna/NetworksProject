package model.devices;

import events.Event;
import events.EventBroker;
import model.IpAddress;
import model.Link;
import model.TcpConnection;
import model.packet.Packet;

import java.util.ArrayList;


public abstract class Device {
    private final String name;
    private final String macAddress;
    private final IpAddress ipAddress;
    private final IpAddress subnetMask;
    private final IpAddress defaultGateway;
    private final ArrayList<Link> linkedDevices;
    private final ArrayList<TcpConnection> tcpConnections = new ArrayList<>();
    private final ArrayList<TcpConnection> tcpConnectionsWithSynSent = new ArrayList<>();
    private final ArrayList<TcpConnection> tcpConnectionsWithFinSent = new ArrayList<>();
    private final ArrayList<TcpConnection> tcpConnectionWithFinReceived = new ArrayList<>();


    public Device(String name, String macAddress, IpAddress ipAddress, IpAddress subnetMask, IpAddress defaultGateway, ArrayList<Link> linkedDevices) {
        this.name = name;
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.subnetMask = subnetMask;
        this.defaultGateway = defaultGateway;
        this.linkedDevices = linkedDevices;
        EventBroker.subscribe(this);
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

    public IpAddress getDefaultGateway() {
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

    protected abstract void processSentEvent(Event event);

    protected abstract void processReceivedEvent(Event event);

    public void handleEvent(Event event) {
        if (event.getSource() == this) {
            processSentEvent(event);
        } else if (event.getDestination() == this) {
            processReceivedEvent(event);
        }
    }

    protected void sendEvent(Event event) {
        EventBroker.sendEvent(event);
    }

}
