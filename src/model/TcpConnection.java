package model;

public class TcpConnection {
    private final IpAddress destinationIp;
    private final int destinationPort;
    private final int sourcePort;

    public TcpConnection(IpAddress destinationIp, int destinationPort, int sourcePort) {
        this.destinationIp = destinationIp;
        this.destinationPort = destinationPort;
        this.sourcePort = sourcePort;
    }

    public IpAddress getDestinationIp() {
        return destinationIp;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public int getSourcePort() {
        return sourcePort;
    }
}
