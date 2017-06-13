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
     * All numbers of neighbours which can sustain life in a living cell
     */
    private int[] stable = {2,3};

    /**
     * All numbers of neighbours which will cause a dead cell to be born
     */
    private int[] birth = {3};

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

    public int getRows(){
        return grid.length;
    }

    public int getCols(){
        return grid[0].length;
    }

    /**
     * Check whether a given cell within the grid is alive
     *
     * @param i The row of the cell to check
     * @param j The column of the cell to check
     * @return True if the cell is alive
     */
    public boolean checkCell(int i, int j){
        checkCellBounds(i, j);
        return grid[i][j].isAlive();
    }

    /**
     * Birth a given cell
     *
     * @param i The row of the cell to birth
     * @param j The column of the cell to birth
     */
    public void birthCell(int i, int j){
        checkCellBounds(i, j);
        grid[i][j].setAlive(true);
    }

    /**
     * Kill a given cell
     *
     * @param i The row of the cell to kill
     * @param j The column of the cell to kill
     */
    public void killCell(int i, int j){
        checkCellBounds(i, j);
        grid[i][j].setAlive(false);
    }

    /**
     * Helper function to bounds check a cell
     * @param i The row of the cell to check
     * @param j The column of the cell to check
     * @throws IllegalArgumentException if the given position is not valid
     */
    void checkCellBounds(int i, int j) throws IllegalArgumentException{
        if (i >= getRows() || i < 0 || j >= getCols() || j < 0){
            throw new IllegalArgumentException(String.format("Cannot access cell at position %1$d, %2$d: Cell does not exist", i, j));
        }
    }
}
