package uk.me.domos.conway.life.model;


/**
 * Represents a grid of Cells, the field of play.
 *
 * @author Dominic Brady (domos@domos.me.uk)
 * @version 0.1 (13th June 2017)
 */
public class CellGrid {

    //Variables

    /**
     * The array of cells storing the actual data. Initialised on construction
     */
    private Cell[][] grid = null;

    /**
     * This is the size of grid we will create if a size is not specified
     */
    private static final int DEFAULT_SIZE = 10;

    //Constructors

    public CellGrid(){
        this(DEFAULT_SIZE);
    }

    public CellGrid(int gridSize){
        grid = new Cell[gridSize][gridSize];
    }

    //Methods


}
