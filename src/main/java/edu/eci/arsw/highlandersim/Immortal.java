package edu.eci.arsw.highlandersim;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Immortal extends Thread {

    private ImmortalUpdateReportCallback updateCallback=null;
    
    private AtomicInteger health;
    
    private int defaultDamageValue;

    private final List<Immortal> immortalsPopulation;

    private final String name;

    private final Random r = new Random(System.currentTimeMillis());

    private boolean paused;
    private boolean dead;
    private  int totalHealth = 0;

    public Immortal(String name, List<Immortal> immortalsPopulation, int health, int defaultDamageValue, ImmortalUpdateReportCallback ucb) {
        super(name);
        this.updateCallback=ucb;
        this.name = name;
        this.immortalsPopulation = immortalsPopulation;
        this.health = new AtomicInteger(health);
        this.defaultDamageValue=defaultDamageValue;
    }

    public void run() {

        while (!dead) {
            synchronized (this) {
                while (paused){
                    try {
                        this.wait();
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Immortal im;

            int myIndex = immortalsPopulation.indexOf(this);

            int nextFighterIndex = r.nextInt(immortalsPopulation.size());

            //avoid self-fight
            if (nextFighterIndex == myIndex) {
                nextFighterIndex = ((nextFighterIndex + 1) % immortalsPopulation.size());
            }

            im = immortalsPopulation.get(nextFighterIndex);

            this.fight(im);

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    public void fight(Immortal i2) {
        boolean shouldAttack;
        boolean isDead = getHealth() <= 0;
        if (isDead) {
            synchronized (immortalsPopulation) {
                immortalsPopulation.remove(this);
            }
            dead = true;
            updateCallback.processReport("Fight: " + this + " vs " + i2 + "\n");
        }else {
            shouldAttack = false;
            synchronized (i2) {
                if (i2.getHealth()>0) {
                    shouldAttack=true;
                    i2.changeHealth(-defaultDamageValue);
                }
            }
            synchronized (this) {
                if (shouldAttack) {
                    this.changeHealth(defaultDamageValue);
                }
            }
            updateCallback.processReport(this + " says:" + i2 + " is already dead!\n");

        }
    }

    public void changeHealth(int v) {
            health.addAndGet(v);
    }

    public int getHealth() {return health.intValue();}

    public boolean isPaused() {
        paused = !paused;
        return paused;
    }
    public synchronized void resumen() {
        paused = false;
        this.notify();
    }

    @Override
    public String toString() {

        return name + "[" + health + "]";
    }

}
