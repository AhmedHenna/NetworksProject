package events.tcp;

import devices.Device;

import java.util.ArrayList;

public class TcpAckDataSegmentEvent extends TcpDataSegmentEvent {
    public TcpAckDataSegmentEvent(Device source, Device destination, int sourcePort, int destinationPort,
                                  int acknowledgmentNumber, String checksum, int windowSize
    ) {
        super(source, destination, null, sourcePort, destinationPort, 0, acknowledgmentNumber, checksum, windowSize);
    }

    @Override
    public ArrayList<String> getAdditionalLogs() {
        ArrayList<String> additional = super.getAdditionalLogs();
        additional.add(source.toLength("ACK:" + getAcknowledgmentNumber(), 7));
        return additional;
    }
}
