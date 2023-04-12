package model;

import model.packet.Packet;

import java.util.ArrayList;

//TODO: event.Event system pub/sub for pre setting up of events
//Who subscribes? Device or separate handler?
public abstract class Device {
    String macAddress;
    IpAddress ipAddress;
    IpAddress subnetMask;
    IpAddress defaultGateway;
    ArrayList<Link> linkedDevices = new ArrayList<>();
    abstract void sendPacket();
    abstract void processReceivedPacket(Packet packet);

}
