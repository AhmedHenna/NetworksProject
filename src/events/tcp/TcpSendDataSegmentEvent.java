package events.tcp;

import model.devices.Device;

public class TcpSendDataSegmentEvent extends TcpDataSegmentEvent {
    private long sentAt;
    public TcpSendDataSegmentEvent(Device source, Device destination, byte[] data, int sourcePort, int destinationPort, int sequenceNumber, String checksum, long sentAt, int windowSize) {
        super(source, destination, data, sourcePort, destinationPort, sequenceNumber, 0, checksum, windowSize);
        this.sentAt = sentAt;
    }

    public long getSentAt() {
        return sentAt;
    }
}
