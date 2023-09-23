import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Buffer {
    List<Integer> privateBuffer = new ArrayList<>();
    final int bufferCap = 10;

    synchronized void produce(int data) {
        try {
            if (this.privateBuffer.size() == this.bufferCap) {
                this.wait();
            }
            privateBuffer.add(data);
            System.out.println("Data " + data + " is added to the buffer.");
            Thread.sleep(1000);
            this.notifyAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    synchronized void consume() {
        try {
            if (this.privateBuffer.size() == 0) {
                this.wait();
            }

            System.out.println("Consuming Data " + privateBuffer.remove(privateBuffer.size() - 1) + " by " + Thread.currentThread().getName() + ".");
            Thread.sleep(1000);
            this.notifyAll();
        } catch (InterruptedException exc){
            exc.printStackTrace();
        }
    }

}


class ProducerThread extends Thread {

    private Buffer buffer;

    public ProducerThread(Buffer b) {
        this.buffer = b;
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            buffer.produce(i);
        }
    }
}

class ConsumerThread extends Thread {
    private Buffer buffer;
    public ConsumerThread(Buffer b){
        this.buffer = b;
    }

    @Override
    public void run() {
        for(int i=0; i<5; i++){
            buffer.consume();
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        ProducerThread pt = new ProducerThread(buffer);
        ConsumerThread ct1 =  new ConsumerThread(buffer);
        ConsumerThread ct2 = new ConsumerThread(buffer);
        pt.setName("Producer Thread");
        ct1.setName("Consumer Thread 1");
        ct2.setName("Consumer Thread 2");
        pt.start();
        ct1.start();
        ct2.start();
    }
}
