package event;

import model.Device;
import model.packet.Packet;

public abstract class NetworkPacketEvent extends Event {
    private final Device source;
    private final Device destination;
    private final Packet packet;

    public NetworkPacketEvent(long timestampMillis, Device source, Device destination, Packet packet) {
        super(timestampMillis);
        this.source = source;
        this.destination = destination;
        this.packet = packet;
    }

    public Device getSource() {
        return source;
    }

    public Device getDestination() {
        return destination;
    }

    public Packet getPacket() {
        return packet;
    }
}
