package model;

import devices.Device;

public class Link {
    private final Device linkedDevice;
    private final int roundTripTime;

    public Link(Device linkedDevice, int roundTripTime) {
        this.linkedDevice = linkedDevice;
        this.roundTripTime = roundTripTime;

    }

    public Device getLinkedDevice() {
        return linkedDevice;
    }

    public int getRoundTripTime() {
        return roundTripTime;
    }

}
