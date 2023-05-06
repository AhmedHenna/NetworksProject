package Examples;

import events.EventWithDirectSourceDestination;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Util {

    public static void listenForQueueUpdates(BlockingQueue<EventWithDirectSourceDestination> eventQueue) {
        while (true) {
            EventWithDirectSourceDestination peeked = eventQueue.peek();
            if (peeked != null) {
                synchronized (peeked.getDestination()) {
                    peeked.getDestination().notify();
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println("Interrupted queue listener");
                break;
            }
        }
    }

    private static final ArrayList<String> usedMacs = new ArrayList<>();

    public static String randomMac() {
        Random random = new Random();
        String[] mac = {String.format("%02x", random.nextInt(0xff)), String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)), String.format("%02x", random.nextInt(0xff)),
                String.format("%02x", random.nextInt(0xff)), String.format("%02x", random.nextInt(0xff))};
        String address = String.join(":", mac);
        if (usedMacs.contains(address)) {
            return randomMac();
        }
        usedMacs.add(address);
        return address;
    }
}
