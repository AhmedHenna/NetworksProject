package event.types;

import event.NetworkPacketEvent;
import model.Device;
import model.packet.Packet;

//TODO: Figure out how to handle events per device without large switch or if/else
//TODO: Write out all possible events
//TODO: Fix event structure to make sense
public class ArpRequestEvent extends NetworkPacketEvent {

    public ArpRequestEvent(long timestampMillis, Device source, Device destination, Packet packet) {
        super(timestampMillis, source, destination, packet);
    }
}
