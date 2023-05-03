package devices;

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
    protected final Link networkLink;
    private final String name;
    private final String macAddress;
    private final IpAddress ipAddress;
    private final IpAddress subnetMask;
    private final Device defaultGateway;


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

    public abstract void processReceivedEvent(Device source, Event event);

    public abstract void processSentEvent(Device destination, Event event);


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

    public void logSentEvent(Event event, Device destination) {
        log("Sending event: " + event.getClass() + " To: " + destination);
    }

    public void logReceivedEvent(Event event, Device source) {
        log("Received event: " + event.getClass() + " From: " + source);
    }

    public void log(String log) {
        System.out.println(System.currentTimeMillis() + ": " + this + " " + log);
    }

    @Override
    public String toString() {
        return getName() + " - " + macAddress;
    }
}
