package events.tcp;

import events.Event;
import model.Device;
import model.packet.IpPayload;
import model.packet.Packet;
import model.packet.transport.TcpPayload;

import java.util.ArrayList;

public abstract class TcpHandshakeEvent extends Event {

    protected final int sourcePort;
    protected final int destinationPort;

    private final ArrayList<TcpPayload.Flag> flags;

    public TcpHandshakeEvent(Device source, Device destination, int sourcePort, int destinationPort) {
        super(source, destination);
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.flags = getFlags();
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    protected abstract ArrayList<TcpPayload.Flag> getFlags();

    @Override
    public Packet createPacket() {
        TcpPayload tcpPayload = new TcpPayload(sourcePort, destinationPort, null, 0, 0, flags);
        IpPayload ipPayload = new IpPayload(destination.getIpAddress(), source.getIpAddress(), tcpPayload);
        return new Packet(destination.getMacAddress(), source.getMacAddress(), ipPayload);
    }

}
