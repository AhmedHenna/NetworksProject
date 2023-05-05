package events.tcp;

import devices.Device;

public class TcpAckDataSegmentEvent extends TcpDataSegmentEvent {
    public TcpAckDataSegmentEvent(Device source, Device destination, int sourcePort, int destinationPort,
                                  int acknowledgmentNumber, String checksum, int windowSize
    ) {
        super(source, destination, null, sourcePort, destinationPort, 0, acknowledgmentNumber, checksum, windowSize);
    }
}
