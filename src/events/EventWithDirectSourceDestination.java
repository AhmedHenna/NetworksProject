package events;

import devices.Device;

public class EventWithDirectSourceDestination {
    private final Event event;
    private final Device destination;
    private final Device source;


    public EventWithDirectSourceDestination(Event event, Device source, Device destination) {
        this.event = event;
        this.destination = destination;
        this.source = source;
    }

    public Event getEvent() {
        return event;
    }

    public Device getDestination() {
        return destination;
    }

    public Device getSource() {
        return source;
    }
}
