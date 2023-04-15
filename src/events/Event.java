package events;

import model.Device;
import model.packet.Packet;

// TODO Connection Lost, Connection Established, Send Packet, Receive Packet, End Transmission, Error (Duplicate ACK), Timeout
// TODO Add transmission time between nodes
// TODO Add checksum, Flow control and congestion control are more important
// TODO Rotures need to be used, no need for ARP can just use mac striaght up, but BONUS for ARP implementation
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
