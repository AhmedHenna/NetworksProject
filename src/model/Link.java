package model;

import devices.Device;

public class Link {
    private final Device linkedDevice;
    private final int roundTripTime;
    private final int congestionWindow;
    private final int receiverWinder;

    public Link(Device linkedDevice, int roundTripTime, int congestionWindow, int receiverWinder) {
        this.linkedDevice = linkedDevice;
        this.roundTripTime = roundTripTime;
        this.congestionWindow = congestionWindow;
        this.receiverWinder = receiverWinder;
    }

    public Device getLinkedDevice() {
        return linkedDevice;
    }

    public int getRoundTripTime() {
        return roundTripTime;
    }

    public int getCongestionWindow() {
        return congestionWindow;
    }

    public int getReceiverWinder() {
        return receiverWinder;
    }
}
