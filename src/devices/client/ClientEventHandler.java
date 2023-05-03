package devices.client;

import devices.Device;
import devices.DeviceEventHandler;
import events.Event;

public abstract class ClientEventHandler implements DeviceEventHandler {

    @Override
    public void processEvent(Device device, Event event) {
        processEvent((Client) device, event);
    }

    public abstract void processEvent(Client client, Event event);
}
