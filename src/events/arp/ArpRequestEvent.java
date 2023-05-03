package events.arp;

import events.Event;
import model.IpAddress;
import model.devices.Device;
import model.packet.IpPayload;
import model.packet.Packet;

public class ArpRequestEvent extends Event {

    private final IpAddress targetIp;

    public ArpRequestEvent(Device source, Device destination, IpAddress targetIp) {
        super(source, destination);
        this.targetIp = targetIp;
        recreatePacket();
    }

    @Override
    public Packet createPacket() {
        IpPayload ipPayload = new IpPayload(targetIp, source.getIpAddress(), null);
        return new Packet("ff:ff:ff:ff:ff:ff", this.source.getMacAddress(), ipPayload);
    }
}
