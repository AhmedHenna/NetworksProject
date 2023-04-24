package model;

public class IpAddressMacMapping {

    private final IpAddress ipAddress;
    private final String macAddress;

    public IpAddressMacMapping(IpAddress ipAddress, String macAddress) {
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }
}
