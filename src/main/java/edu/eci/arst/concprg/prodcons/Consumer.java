/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;

/**
 *
 * @author hcadavid
 */
public class Consumer extends Thread{
    
    private Queue<Integer> queue;
    
    
    public Consumer(Queue<Integer> queue){
        this.queue=queue;        
    }
    
    @Override
    public void run() {
        while (true) {

            if (queue.size()>0) {
                synchronized (queue) {
                    try {
                        int elem = queue.poll();
                        System.out.println("Consumer consumes " + elem);
                        queue.notifyAll();
                        Thread.sleep(1000);         // Duerme el hilo para que sea mas lento que el productor (punto1.3)
                        //queue.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        }

    }

}
