package no.ntnu;


import java.util.ArrayList;

public class MessageQueue implements Channel {

    private final ArrayList<Object> queue;
    private final int size;

    public MessageQueue(int size) {
        if (size < 1) {
            size = 1; // Do not allow weird size
        }
        
        this.size = size;
        queue = new ArrayList<>(size);
    }

    @Override
    public synchronized void send(Object item) throws InterruptedException {
        // If the queue is full, wait for queue space.
        while (this.queue.size() == this.size) {
            wait();
        }
        this.queue.add(item);
        // If an item is still in queue, notify all channels
        if (this.queue.size() == 1) {
            notifyAll();
        }
    }

    @Override
    public synchronized Object receive() throws InterruptedException {
        // element in the queue
            while(queue.isEmpty()){
                wait();
        }
            if(this.queue.size() == this.size){
                notifyAll();
            }
            return this.queue.remove(0);

    }

    @Override
    public int getNumQueuedItems() {
        return queue.size();
    }

    /**
     * Return comma-separated objects
     * @return 
     */
    @Override
    public String getQueueItemList() {
        String res = "";
        for (Object item : queue) {
            res += item + ",";
        }
        return res;
    }
}
