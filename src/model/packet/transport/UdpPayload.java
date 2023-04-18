package model.packet.transport;

public class UdpPayload extends TransportPayload {

    public UdpPayload(int sourcePort, int destinationPort, byte[] payload, String checksum) {
        super(sourcePort, destinationPort, payload, checksum);
    }

}
