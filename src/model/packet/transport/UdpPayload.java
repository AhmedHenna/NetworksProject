package model.packet.transport;

public class UdpPayload extends TransportPayload {
    private final int length;

    public UdpPayload(int sourcePort, int destinationPort, String payload, int length) {
        super(sourcePort, destinationPort, payload);
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
