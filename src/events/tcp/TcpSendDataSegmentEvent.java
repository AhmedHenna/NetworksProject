package events.tcp;

import devices.Device;

import java.util.ArrayList;

public class TcpSendDataSegmentEvent extends TcpDataSegmentEvent {
    private long sentAt;

    public TcpSendDataSegmentEvent(Device source, Device destination, byte[] data, int sourcePort, int destinationPort,
                                   int sequenceNumber, String checksum, long sentAt, int windowSize
    ) {
        super(source, destination, data, sourcePort, destinationPort, sequenceNumber, 0, checksum, windowSize);
        this.sentAt = sentAt;
    }

    public long getSentAt() {
        return sentAt;
    }

    public void setSentAt(long sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public ArrayList<String> getAdditionalLogs() {
        ArrayList<String> additional = super.getAdditionalLogs();
        additional.add(source.toLength("SEQ:"+ getSequenceNumber(), 7));
        additional.add("DATA: "+source.toLength(new String(getData()),16));
        return additional;
    }
}
