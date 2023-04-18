package events;

import model.devices.Device;
import model.packet.Packet;


public abstract class Event {

    protected final long timestampMillis;

    protected final Device source;
    protected final Device destination;
    protected final Packet packet;

    public Event(Device source, Device destination) {
        this.timestampMillis = System.currentTimeMillis();
        this.source = source;
        this.destination = destination;
        this.packet = createPacket();
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

    public long getTimestampMillis() {
        return timestampMillis;
    }

    public int compareTo(Event other) {
        return Long.compare(this.timestampMillis, other.getTimestampMillis());
    }

    public abstract Packet createPacket();

}
