package model.packet.transport;

public abstract class TransportPayload {
    protected int sourcePort;
    protected int destinationPort;
    protected byte[] payload;

    public TransportPayload(int sourcePort, int destinationPort, byte[] payload) {
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.payload = payload;
    }

    public int getSourcePort() {
        return sourcePort;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public byte[] getPayload() {
        return payload;
    }
}
