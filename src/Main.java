import events.Event;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Event> events = new ArrayList<>();

        //Create Devices and events here
        //Add events to list in the order you'd like them sent

        sendEvents(events);
    }

    private static void sendEvents(ArrayList<Event> events){
        for(Event event: events){
            event.getSource().sendEvent(event);
        }
    }
}