package uk.me.domos.conway.life.model;

/**
 * Represents an individual cell within the grid
 *
 * @author Dominic Brady (domos@domos.me.uk)
 * @version 1.0 (16th June 2017)
 */
public class Cell {

    //Variables

    private boolean alive = false;

    //Methods

    public boolean isAlive(){
        return alive;
    }

    public void setAlive(boolean alive){
        this.alive = alive;
    }
}
