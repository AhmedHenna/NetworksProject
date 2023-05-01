package events.tcp;

import model.devices.Device;

public class TcpAcknowledgeDataSegmentEvent extends TcpDataSegmentEvent {
    public TcpAcknowledgeDataSegmentEvent(Device source, Device destination, int sourcePort, int destinationPort, int acknowledgmentNumber, String checksum) {
        super(source, destination, null, sourcePort, destinationPort, 0, acknowledgmentNumber, checksum);
    }
}
