package model.packet.transport;

public class UdpPayload extends TransportPayload {
    private final int length;

    public UdpPayload(int sourcePort, int destinationPort, byte[] payload, int length, String checksum) {
        super(sourcePort, destinationPort, payload, checksum);
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
