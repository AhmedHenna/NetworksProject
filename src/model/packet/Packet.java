package model.packet;

public class Packet {
    private String destinationMacAddress;
    private String sourceMacAddress;
    private IpPayload ipPayload;

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
