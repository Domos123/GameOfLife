package uk.me.domos.conway.life.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a grid of Cells, the field of play.
 *
 * @author Dominic Brady (domos@domos.me.uk)
 * @version 1.0 (14th June 2017)
 */
public class CellGrid {

    //Variables

    /**
     * The array of cells storing the data for the next generation
     */
    private Cell[][] grid = null;

    /**
     * The array of cells storing the data for the generation currently being displayed
     */
    private Cell[][] displayGrid = null;

    /**
     * All numbers of neighbours which can sustain life in a living cell
     */
    private Set<Integer> stableValues = new HashSet<>();

    /**
     * All numbers of neighbours which will cause a dead cell to be born
     */
    private Set<Integer> birthValues = new HashSet<>();

    /**
     * Whether we should wrap when counting neighbours
     */
    private boolean shouldWrap = false;

    /**
     * This is the size of grid we will create if a size is not specified
     */
    private static final int DEFAULT_SIZE = 10;

    //Constructors

    /**
     * Create a new grid with size defined by DEFAULT_SIZE
     */
    public CellGrid(){
        this(DEFAULT_SIZE, DEFAULT_SIZE);
    }

    /**
     * Construct a new grid of a given size
     *
     * @param gridSizeI The number of rows
     * @param gridSizeJ The number of columns
     */
    public CellGrid(int gridSizeI, int gridSizeJ){
        grid = new Cell[gridSizeI][gridSizeJ];
        grid = new Cell[gridSizeI][gridSizeJ];
    }

    //Methods

    /**
     * @return The number of rows in this grid
     */
    public int getRows(){
        return grid.length;
    }

    /**
     * @return The number of columns in this grid
     */
    public int getCols(){
        return grid[0].length;
    }

    /**
     * Check whether the grid's edges will wrap when counting neighbours
     * @return True if the edges will wrap
     */
    public boolean checkShouldWrap(){
        return shouldWrap;
    }

    /**
     * Set whether the grid's edges should wrap when counting neighbours
     * @param shouldWrap If this is true, the edges will wrap
     */
    public void setShouldWrap(boolean shouldWrap){
        this.shouldWrap = shouldWrap;
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
     *
     * @param i The row of the cell to check
     * @param j The column of the cell to check
     * @throws IllegalArgumentException if the given position is not valid
     */
    private void checkCellBounds(int i, int j) throws IllegalArgumentException{
        if (i >= getRows() || i < 0 || j >= getCols() || j < 0){
            throw new IllegalArgumentException(String.format("Cannot access cell at position %1$d, %2$d: Cell does not exist", i, j));
        }
    }

    /**
     * Counts how many living neighbours a cell has
     *
     * @param i The row of the cell to check
     * @param j The column of the cell to check
     * @return The number of neighbours the requested cell has
     */
    public int countNeighbours(int i, int j){
        int count = 0;
        int posI, posJ;
        checkCellBounds(i,j);
        for (int dI=-1; dI<2; dI++){
            for (int dJ=-1; dJ<2; dJ++){

                //Handle optional wrapping
                if (shouldWrap){
                    posI = (i + dI) % getRows();
                    if (posI < 0){ //Check for oob positions
                        posI += getRows();
                    }
                    posJ = (j + dJ) % getCols();
                    if (posJ < 0){
                        posJ += getCols();
                    }
                } else {
                    posI = i + dI;
                    posJ = j + dJ;
                }

                //Check for invalid positions, and don't count cell as its own neighbour
                if (posI >= getRows() || posJ >= getCols() || posI < 0 || posJ < 0 || posI == i || posJ == j){
                    break;
                }

                if (checkCell(posI, posJ)){
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Add a value to the set of values which will cause a cell to be born
     *
     * @param value The value to be added
     */
    public void addBirthValue(int value) {
        if (!birthValues.contains(value)){
            birthValues.add(value);
        }
    }

    /**
     * Remove a value from the set of values which will cause a cell to be born
     *
     * @param value The value to be removed
     */
    public void removeBirthValue(int value){
        if (birthValues.contains(value)){
            birthValues.remove(value);
        }
    }

    /**
     * Add a value to the set of values which will cause a living cell to be stable
     *
     * @param value The value to be added
     */
    public void addStableValue(int value) {
        if (!stableValues.contains(value)){
            stableValues.add(value);
        }
    }

    /**
     * Remove a value from the set of values which will cause a living cell to be stable
     *
     * @param value The value to be removed
     */
    public void removeStableValue(int value){
        if (stableValues.contains(value)){
            stableValues.remove(value);
        }
    }

    /**
     * Process one generation of births and deaths
     */
    public void doGeneration(){
        for (int i=0; i<displayGrid.length; i++){
            for (int j=0; j<displayGrid[0].length; j++){
                //Handle starvations
                if (!stableValues.contains(countNeighbours(i,j))){
                    grid[i][j].setAlive(false);
                    break;
                }
                //Handle births
                if (birthValues.contains(countNeighbours(i,j))){
                    grid[i][j].setAlive(true);
                }
            }
        }

        //Swap arrays
        Cell[][] tempGrid = displayGrid;
        displayGrid = grid;
        grid = tempGrid;
    }
}
