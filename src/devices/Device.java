package devices;

import events.Event;
import events.EventWithDirectSourceDestination;
import events.OnEvent;
import model.IpAddress;
import model.Link;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;


public abstract class Device extends Thread {

    //MSS is 1460 bytes, downscaled for testing purposes 1460/100 = 14.6 ~= 15
    public static int MSS = 15;

    //Number of event allowed at a given point
    public static int INITIAL_WINDOW_SIZE = 3;
    public static int SENT_SEGMENT_TIMEOUT = 15000;
    protected final Link networkLink;
    private final String macAddress;
    private final IpAddress ipAddress;
    private final IpAddress subnetMask;
    private final Device defaultGateway;
    private final BlockingQueue<EventWithDirectSourceDestination> eventQueue;
    protected ArrayList<OnEvent> onSentEventListeners = new ArrayList<>();
    protected ArrayList<OnEvent> onReceivedEventListeners = new ArrayList<>();


    public Device(String name, String macAddress, IpAddress ipAddress, IpAddress subnetMask, Device defaultGateway,
                  Link networkLink, BlockingQueue<EventWithDirectSourceDestination> eventQueue
    ) {
        super(name);
        this.macAddress = macAddress;
        this.ipAddress = ipAddress;
        this.subnetMask = subnetMask;
        this.defaultGateway = defaultGateway;
        this.networkLink = networkLink;
        this.eventQueue = eventQueue;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public IpAddress getIpAddress() {
        return ipAddress;
    }

    public IpAddress getSubnetMask() {
        return subnetMask;
    }

    public Device getDefaultGateway() {
        return defaultGateway;
    }

    public Link getNetworkLink() {
        return networkLink;
    }

    public void addOnReceivedEventListener(OnEvent onReceivedEvent) {
        this.onReceivedEventListeners.add(onReceivedEvent);
    }

    public void removeOnReceivedEventListener(OnEvent onReceivedEvent) {
        this.onReceivedEventListeners.remove(onReceivedEvent);
    }

    public void addOnSentEventListener(OnEvent onSentEvent) {
        this.onSentEventListeners.add(onSentEvent);
    }

    public void removeOnSentEventListener(OnEvent onSentEvent) {
        this.onSentEventListeners.remove(onSentEvent);
    }

    public abstract void processReceivedEvent(Device source, Event event);

    /**
     * @return false when should abort sending event
     */
    public abstract boolean processSentEvent(Device destination, Event event);

    public void sendEventToDevice(Device destination, Event event) {
        this.eventQueue.add(new EventWithDirectSourceDestination(event, this, destination));
    }


    public abstract void sendEvent(Event event);

    protected void pauseBeforeSending(Event event){
        try {
            // Sleep to leave enough time between events to ensure proper ordering
            sleep(50);
            event.updateTimestamp();

        } catch (InterruptedException e) {
            System.out.println("Interrupted while waiting to call sent event listeners");
        }
    }

    protected void pauseBeforeReceiving(){
        try {
            // Sleep to leave enough time between events to ensure proper ordering
            sleep(50);
        } catch (InterruptedException e) {
            System.out.println("Interrupted while waiting to call sent event listeners");
        }
    }

    protected void callOnReceivedListeners(Event event) {
        for (OnEvent onReceivedEvent : onReceivedEventListeners) {
            onReceivedEvent.onEvent(event);
        }
    }

    protected void callOnSentListeners(Event event) {
        for (OnEvent onSentEvent : onSentEventListeners) {
            onSentEvent.onEvent(event);
        }
    }

    protected boolean currentIsSource(Device source) {
        if (source != this) {
            System.out.println(
                    "ERROR: Cannot send event where source device is different from current" + "\n Current IP: " + ipAddress + " Current MAC: " + macAddress + "\n Source IP: " + source.ipAddress + " Dest MAC: " + source.macAddress);
            return false;
        }
        return true;
    }

    public void logSentEvent(Event event, Device destination) {
        logEvent(event, destination, "Sent");
    }

    public void logReceivedEvent(Event event, Device source) {
        logEvent(event, source, "Received");
    }

    private void logEvent(Event event, Device device, String type) {
        boolean isReceivedEvent = type.toLowerCase().startsWith("r");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        ArrayList<String> logs = new ArrayList<>();
        logs.add(dateFormat.format(isReceivedEvent ? new Date(System.currentTimeMillis()) : new Date(event.getTimestampMillis())));
        logs.add(toLength(isReceivedEvent ? "Received" : "Sent", 8));
        logs.add(toLength(event.getClass().toString().replace("class events.", ""), 30));
        logs.add(toLength(isReceivedEvent ? "From" : "To", 4));
        logs.add(toLength(device.toString(), 10));
        logs.addAll(event.getAdditionalLogs());
        String[] logsArray = logs.toArray(new String[0]);
        log(logsArray);
    }


    public void log(String... log) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        ArrayList<String> fullLog = new ArrayList<>();
        fullLog.add(dateFormat.format(new Date(System.currentTimeMillis())));
        fullLog.add(toLength(toString(), 10));
        fullLog.addAll(List.of(log));
        for (int i = 0; i < fullLog.size(); i++) {
            String s = fullLog.get(i);
            System.out.print("| " + s + " ");
            if (i == fullLog.size() - 1) {
                System.out.print("|");
            }
        }
        System.out.println();
    }

    @Override
    public String toString() {
        return getName();
    }

    public String toLength(String s, int l) {
        if (s.length() > l) {
            return s.substring(0, l - 1);
        } else if (s.length() < l) {
            return s + " ".repeat(l - s.length());
        }
        return s;
    }

    @Override
    public void run() {
        super.run();
        synchronized (this) {
            while (true) {
                try {
                    wait();
                    EventWithDirectSourceDestination eventWithDirectSourceDestination = eventQueue.take();
                    processReceivedEvent(eventWithDirectSourceDestination.getSource(),
                            eventWithDirectSourceDestination.getEvent()
                    );

                } catch (InterruptedException e) {
                    System.out.println("Thread interrupted: " + getName());
                    break;
                }

            }
        }

    }
}
