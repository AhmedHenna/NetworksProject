package event;


public abstract class Event implements Comparable<Event> {
    private final long timestampMillis;

    public Event(long timestampMillis){
        this.timestampMillis = System.currentTimeMillis();
    }

    public long getTimestampMillis() {
        return timestampMillis;
    }

    public int compareTo(Event other) {
        return Long.compare(this.timestampMillis, other.timestampMillis);
    }

}