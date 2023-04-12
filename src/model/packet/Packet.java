package model.packet;

public class Packet {
    private final String destinationMacAddress;
    private final String sourceMacAddress;
    private final IpPayload ipPayload;

    public Packet(String destinationMacAddress, String sourceMacAddress, IpPayload ipPayload) {
        this.destinationMacAddress = destinationMacAddress;
        this.sourceMacAddress = sourceMacAddress;
        this.ipPayload = ipPayload;
    }

    public String getDestinationMacAddress() {
        return destinationMacAddress;
    }

    public String getSourceMacAddress() {
        return sourceMacAddress;
    }

    public IpPayload getIpPayload() {
        return ipPayload;
    }
}
