package devices;

import events.Event;

public interface DeviceEventHandler {
    public void processEvent(Device device, Event event);
}
