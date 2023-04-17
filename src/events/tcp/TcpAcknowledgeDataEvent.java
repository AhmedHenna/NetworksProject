package events.tcp;

import model.Device;

public class TcpAcknowledgeDataEvent extends TcpDataEvent{
    public TcpAcknowledgeDataEvent(Device source, Device destination, byte[] data, int sourcePort, int destinationPort, int acknowledgmentNumber) {
        super(source, destination, data, sourcePort, destinationPort, 0, acknowledgmentNumber);
    }
}
