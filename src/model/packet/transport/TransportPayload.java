package model.packet.transport;

public abstract class TransportPayload {
    protected int sourcePort;
    protected int destinationPort;
    protected String payload;

    public TransportPayload(int sourcePort, int destinationPort, String payload) {
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

    public String getPayload() {
        return payload;
    }
}
