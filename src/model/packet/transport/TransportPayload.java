package model.packet.transport;

public abstract class TransportPayload {
    protected int sourcePort;
    protected int destinationPort;
    protected byte[] payload;
    protected String checksum;

    public TransportPayload(int sourcePort, int destinationPort, byte[] payload, String checksum) {
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.payload = payload;
        this.checksum = checksum;
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

    public String getChecksum() {
        return checksum;
    }
}
