package devices;

import events.Event;

public interface DeviceEventHandler {
    void processEvent(Device device, Event event);
}
