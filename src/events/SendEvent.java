package events;

import devices.Device;

public interface SendEvent {
    void sendEvent(Device destination, Event event);
}
