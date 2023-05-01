package events.tcp;

import model.devices.Device;

public class TcpSendDataSegmentEvent extends TcpDataSegmentEvent {
    public TcpSendDataSegmentEvent(Device source, Device destination, byte[] data, int sourcePort, int destinationPort, int sequenceNumber, String checksum) {
        super(source, destination, data, sourcePort, destinationPort, sequenceNumber, 0, checksum);
    }
}
