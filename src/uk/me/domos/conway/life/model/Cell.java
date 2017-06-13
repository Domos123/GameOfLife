package uk.me.domos.conway.life.model;

/**
 * Represents an individual cell within the grid
 *
 * @author Dominic Brady (domos@domos.me.uk)
 * @version 0.1 (13th June 2017)
 */
public class Cell {
    private boolean alive = false;

    public boolean isAlive(){
        return alive;
    }

    public void setAlive(boolean alive){
        this.alive = alive;
    }
}
