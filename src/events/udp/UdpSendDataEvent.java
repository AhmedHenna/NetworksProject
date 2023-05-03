package events.udp;

import devices.Device;
import events.Event;
import model.packet.IpPayload;
import model.packet.Packet;
import model.packet.transport.UdpPayload;

public class UdpSendDataEvent extends Event {
    private final byte[] data;
    private final String checksum;
    private final int sourcePort;
    private final int destinationPort;


    public UdpSendDataEvent(Device source, Device destination, int sourcePort, int destinationPort, byte[] data, String checksum) {
        super(source, destination);
        this.data = data;
        this.checksum = checksum;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        recreatePacket();
    }


    @Override
    public Packet createPacket() {
        UdpPayload udpPayload = new UdpPayload(sourcePort, destinationPort, data, checksum);
        IpPayload ipPayload = new IpPayload(destination.getIpAddress(), source.getIpAddress(), udpPayload);
        return new Packet(destination.getMacAddress(), source.getMacAddress(), ipPayload);
    }
}
