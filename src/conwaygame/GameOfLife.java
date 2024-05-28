package conwaygame;
import java.util.ArrayList;

/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {
        StdIn.setFile(file);
        int row = StdIn.readInt();
        int column = StdIn.readInt();
        
        grid = new boolean[row][column];
       
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                grid[i][j] = StdIn.readBoolean();
            }
        }
    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {
        if (grid[row][col] == ALIVE){
            return true;
        }
        return false; 
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {
        int total = 0;

        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                if (grid[i][j] == ALIVE){
                    total++;
                }
            }
        }
        if (total > 0){
            return true;
        }
        return false; 
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {

        // WRITE YOUR CODE HERE
        int aliveNeighbors = 0;

        int newColLeft = 0;
        int newRowUp = 0;
        int newColRight = 0;
        int newRowDown = 0;

        if (col - 1 == -1){
            newColLeft = grid[0].length - 1;
        }
        else if (col - 1 != -1){
            newColLeft = col - 1;
        }

        if (row - 1 == -1){
            newRowUp = grid.length - 1;
        }
        else if (row - 1 != -1){
            newRowUp = row - 1;
        }

        if (col + 1 == grid[0].length){
            newColRight = 0;
        }
        else if (col + 1 != grid[0].length){
            newColRight = col + 1;
        }

        if (row + 1 == grid.length){
            newRowDown = 0;
        }
        else if (row + 1 != grid.length){
            newRowDown = row + 1;
        }

        // Left Up Diagonal
        if (grid[newRowUp][newColLeft] == ALIVE){
            aliveNeighbors++;
        }

        // Left
        if (grid[row][newColLeft] == ALIVE) {
            aliveNeighbors++;
        }
        
        // Left Down Diagonal
        if (grid[newRowDown][newColLeft] == ALIVE){
            aliveNeighbors++;
        }

        // Up
        if (grid[newRowUp][col] == ALIVE){
            aliveNeighbors++;
        }

        // Down
        if (grid[newRowDown][col] == ALIVE){
            aliveNeighbors++;
        }

        // Right Up Diagonal
        if (grid[newRowUp][newColRight] == ALIVE){
            aliveNeighbors++;
        }

        // Right 
        if (grid[row][newColRight] == ALIVE){
            aliveNeighbors++;
        }

        // Right Down Diagonal
        if (grid[newRowDown][newColRight] == ALIVE){
            aliveNeighbors++;
        }
    
        return aliveNeighbors;
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {
        
        boolean[][] newGrid = new boolean[grid.length][grid[0].length];

        for (int i = 0; i < newGrid.length; i++){
            for (int j = 0; j < newGrid[i].length; j++){
                if (grid[i][j] == ALIVE && numOfAliveNeighbors(i,j) <= 1){
                    newGrid[i][j] = DEAD;
                }
                else if (grid[i][j] == DEAD && numOfAliveNeighbors(i, j) == 3) {
                    newGrid[i][j] = ALIVE;
                }
                else if (grid[i][j] == ALIVE && (numOfAliveNeighbors(i, j) == 2 || numOfAliveNeighbors(i, j) == 3)) {
                    newGrid[i][j] = ALIVE;
                }
                else if (grid[i][j] == ALIVE && numOfAliveNeighbors(i, j) >= 4) {
                    newGrid[i][j] = DEAD;
                }
                else{
                    newGrid[i][j] = grid[i][j];
                }
            }
        }

        return newGrid;
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {

        grid = computeNewGrid();
        totalAliveCells = getTotalAliveCells();
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {

        for (int i = 0; i < n; i++){
            nextGeneration();
        }
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {

        WeightedQuickUnionUF wqu = new WeightedQuickUnionUF(grid.length, grid[0].length);

        for (int row = 0; row < grid.length; row++){
            for (int col = 0; col < grid[row].length; col++){
                if (grid[row][col] == ALIVE){
                    int newColLeft = 0;
                    int newRowUp = 0;
                    int newColRight = 0;
                    int newRowDown = 0;

                    if (col - 1 == -1){
                        newColLeft = grid[0].length - 1;
                    }
                    else if (col - 1 != -1){
                        newColLeft = col - 1;
                    }

                    if (row - 1 == -1){
                        newRowUp = grid.length - 1;
                    }
                    else if (row - 1 != -1){
                        newRowUp = row - 1;
                    }

                    if (col + 1 == grid[0].length){
                        newColRight = 0;
                    }
                    else if (col + 1 != grid[0].length){
                        newColRight = col + 1;
                    }

                    if (row + 1 == grid.length){
                        newRowDown = 0;
                    }
                    else if (row + 1 != grid.length){
                        newRowDown = row + 1;
                    }

                    // Left Up Diagonal
                    if (grid[newRowUp][newColLeft] == ALIVE){
                        wqu.union(row, col, newRowUp, newColLeft);
                    }

                    // Left
                    if (grid[row][newColLeft] == ALIVE) {
                        wqu.union(row, col, row, newColLeft);
                    }
                    
                    // Left Down Diagonal
                    if (grid[newRowDown][newColLeft] == ALIVE){
                        wqu.union(row, col, newRowDown, newColLeft);
                    }

                    // Up
                    if (grid[newRowUp][col] == ALIVE){
                        wqu.union(row, col, newRowUp, col);
                    }

                    // Down
                    if (grid[newRowDown][col] == ALIVE){
                        wqu.union(row, col, newRowDown, col);
                    }

                    // Right Up Diagonal
                    if (grid[newRowUp][newColRight] == ALIVE){
                        wqu.union(row, col, newRowUp, newColRight);
                    }

                    // Right 
                    if (grid[row][newColRight] == ALIVE){
                        wqu.union(row, col, row, newColRight);
                    }

                    // Right Down Diagonal
                    if (grid[newRowDown][newColRight] == ALIVE){
                        wqu.union(row, col, newRowDown, newColRight);
                    }
                }
            }
        }

        ArrayList<Integer> roots = new ArrayList<Integer>();

        int counter = 0;

        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid[i].length; j++){
                if (grid[i][j] == ALIVE){
                    if (!roots.contains(wqu.find(i, j))){
                        roots.add(wqu.find(i, j));
                        counter++;
                    }
                }
            }
        }

        return counter;
    }
}
