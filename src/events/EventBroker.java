package events;

import model.devices.Device;

import java.util.ArrayList;

public class EventBroker {
    private static final ArrayList<Device> devices = new ArrayList<>();

    public static void subscribe(Device device) {
        devices.add(device);
    }

    public static void unSubscribe(Device device) {
        devices.remove(device);
    }

    public static void sendEvent(Event event) {
        for (Device device : devices) {
            device.handleEvent(event);
        }
    }

}
