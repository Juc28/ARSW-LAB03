/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author hcadavid
 */
public class Producer extends Thread {

    private Queue<Integer> queue = null;

    private int dataSeed = 0;
    private Random rand = null;
    private final long stockLimit;

    public Producer(Queue<Integer> queue, long stockLimit) {
        this.queue = queue;
        rand = new Random(System.currentTimeMillis());
        this.stockLimit = stockLimit;
    }

    @Override
    public void run() {
        while (true) {
            dataSeed = dataSeed + rand.nextInt(100);
            synchronized (queue) {
                try {
                    queueAddLimit(dataSeed);
                    //Thread.sleep(1000);     Se comenta para que en el punto 3 el productor sea mas rapido
                    //queue.notifyAll();      Se comenta pues en el punto 3 ya no tiene sentido hacer esto
                } catch (Exception ex) {
                    try {
                        queue.wait();           //Pone el hilo en espera cuando la cola este llena
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    //Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void queueAddLimit(int dataSeed) throws InterruptedException{
        if (queue.size() < stockLimit){
            queue.add(dataSeed);
            System.out.println("Producer added " + dataSeed);
        } else {
            throw new InterruptedException("");
        }
    }
}
