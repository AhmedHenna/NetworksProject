package events.arp;

import events.Event;
import model.devices.Device;
import model.IpAddress;
import model.packet.IpPayload;
import model.packet.Packet;

public class ArpRequestEvent extends Event {

    private final IpAddress targetIp;

    public ArpRequestEvent(Device source, Device destination, IpAddress targetIp) {
        super(source, destination);
        this.targetIp = targetIp;
    }

    @Override
    public Packet createPacket() {
        IpPayload ipPayload = new IpPayload(targetIp, source.getIpAddress(), null);
        return new Packet("ff:ff:ff:ff:ff:ff", this.source.getMacAddress(), ipPayload);
    }
}
