package devices;

import model.IpAddress;

public class DeviceUtil {

    public static boolean isInSameNetwork(Device device1, Device device2) {

        // When device is null, then event meant for broadcast
        if(device2 == null || device1 == null){
            return true;
        }
        IpAddress device1Ip = device1.getIpAddress();
        IpAddress device2Ip = device2.getIpAddress();
        IpAddress device1Subnet = device1.getSubnetMask();
        IpAddress device2Subnet = device2.getSubnetMask();

        if (device1Subnet.toString().equals(device2Subnet.toString())) {
            if (device1Subnet.getFirstChunk() == 255) {
                if (device1Ip.getFirstChunk() != device2Ip.getFirstChunk()) {
                    return false;
                }
            }
            if (device1Subnet.getSecondChunk() == 255) {
                if (device1Ip.getSecondChunk() != device2Ip.getSecondChunk()) {
                    return false;
                }
            }
            if (device1Subnet.getThirdChunk() == 255) {
                if (device1Ip.getThirdChunk() != device2Ip.getThirdChunk()) {
                    return false;
                }
            }
            if (device1Subnet.getFourthChunk() == 255) {
                if (device1Ip.getFourthChunk() != device2Ip.getFourthChunk()) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

}
