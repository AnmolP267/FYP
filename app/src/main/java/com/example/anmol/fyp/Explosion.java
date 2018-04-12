package com.example.anmol.fyp;

import android.graphics.Canvas;

/*Transferred from the example program made for the Interim. Explosion is created through a set
* of Particles objects dispersed in a random direction*/
public class Explosion {

    public static final int STATE_ALIVE     = 0;    // at least 1 particle is alive
    public static final int STATE_DEAD      = 1;    // all particles are dead
    private Particle[] particles;           // particles in the explosion
    private int x, y;                       // the explosion's origin
    private int size;                       // number of particles
    private int state;                      // whether it's still active or not



    public Explosion(int particleNr, int x, int y) {
        this.state = STATE_ALIVE;
        this.particles = new Particle[particleNr];
        for (int i = 0; i < this.particles.length; i++) {
            Particle p = new Particle(x, y);
            this.particles[i] = p;
        }
        this.size = particleNr;
    }


    public boolean isAlive() {
        if(this.state == STATE_ALIVE) {
            return true;
        }
        return false;

    }

    public void update() {
        for(int i = 0; i < particles.length; i++) {
            particles[i].update();
        }
    }

    public void draw(Canvas canvas) {
        for(int i = 0; i < particles.length; i++) {
            particles[i].draw(canvas);
        }
    }

}
