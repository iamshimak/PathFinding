import java.awt.*;
import java.util.*;

/**
 * Created by ShimaK on 29-Mar-17.
 */

public class PathFindingOnSquaredGrid {
    private Cell[][] grid;
    private int SIZE;
    private int GOALX, GOALY;
    private int DISTANCE;
    private boolean IS_DIAGONAL_ALLOWED;
    private double HORIZONTAL_VERTICAL_COST, DIAGONAL_COST;

    /**
     * Gets and N by N Cell[][]
     *
     * @param grid grid
     */
    public PathFindingOnSquaredGrid(Cell[][] grid) {
        if (grid == null) {
            throw new NullPointerException();
        }
        if (grid.length != grid[0].length) {
            throw new IllegalArgumentException();
        }
        this.grid = grid;
        SIZE = grid.length;
    }

    /**
     * Find shortest path using findPath
     *
     * @param start path finding start Cell
     * @param end   path finding end Cell
     */
    public void findPath(Cell start, Cell end) {
        PriorityQueue<Cell> openList = new PriorityQueue<>((o1, o2) -> o1.getF() < o2.getF() ? -1 : o1.getF() > o2.getF() ? 1 : 0);
        ArrayList<Cell> closeList = new ArrayList<>();

        GOALX = end.getX();
        GOALY = end.getY();

        Cell lastCell = null;

        calculateHeuristic();

        start.setG(0);
        start.setF(start.getG() + start.getH());
        start.setParent(start);
        openList.add(start);

        while (!openList.isEmpty()) {
            Cell current = openList.poll();

            if (current.getId() == end.getId()) {
                lastCell = current;
                break;
            }

            closeList.add(current);

            ArrayList<Cell> neighbours = getAdjacentCells(current, grid, IS_DIAGONAL_ALLOWED);
            checkNeighbours(neighbours, closeList, openList, current);
        }
        displayPath(lastCell, start, end);
    }

    private void setManhattanDistance() {
        HORIZONTAL_VERTICAL_COST = 1.0;
        DIAGONAL_COST = 1.0;
        DISTANCE = 1;
        IS_DIAGONAL_ALLOWED = false;
    }

    private void setEuclideanDistance() {
        HORIZONTAL_VERTICAL_COST = 1.0;
        DIAGONAL_COST = 1.4;
        DISTANCE = 2;
        IS_DIAGONAL_ALLOWED = true;
    }

    private void setChebyshevDistance() {
        HORIZONTAL_VERTICAL_COST = 1.0;
        DIAGONAL_COST = 1.0;
        DISTANCE = 3;
        IS_DIAGONAL_ALLOWED = true;
    }

    private void calculateHeuristic() {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                switch (DISTANCE) {
                    case 1:
                        grid[x][y].setH(manhattanCost(x, y, GOALX, GOALX));
                        break;
                    case 2:
                        grid[x][y].setH(euclideanCost(x, y, GOALX, GOALX));
                        break;
                    case 3:
                        grid[x][y].setH(chebyshevCost(x, y, GOALX, GOALX));
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
            }
        }
    }

    /**
     * Check adjacent Cells and add to openList
     * If already added compare G cost with openList Cell's G cost
     * Calculates F and assign to Cell instance
     *
     * @param neighbours Adjacent Cells of current Cell
     * @param closeList  Cells already searched
     * @param openList   Adjacent Cells previously added
     * @param parent     Current Cell
     */
    public void checkNeighbours(ArrayList<Cell> neighbours, ArrayList<Cell> closeList, PriorityQueue<Cell> openList, Cell parent) {
        drawCircle(grid, parent.getX(), parent.getY(), Color.ORANGE);
        for (Cell nei : neighbours) {
            if (closeList.contains(nei)) continue;
            drawCircle(grid, nei.getX(), nei.getY(), Color.YELLOW);

            if (!openList.contains(nei)) {
                nei.setParent(parent);
                nei.setG(parent.getG() + gCost(parent.getX(), parent.getY(), nei.getX(), nei.getY()));
                nei.setF(nei.getG() + nei.getH());
                openList.add(nei);
            } else {
                double openG = nei.getG();
                double neighG = parent.getG() + gCost(parent.getX(), parent.getY(), nei.getX(), nei.getY());

                if (neighG < openG) {
                    nei.setG(neighG);
                    nei.setParent(parent);
                }
            }
        }
    }

    public void displayPath(Cell currentCell, Cell startCell, Cell goalCell) {
        if (currentCell != null) {
            System.out.println("\nTraveled distance (G cost) : " + currentCell.getG());
            while (currentCell != startCell) {
                drawLine(currentCell.getParent().getX(), currentCell.getParent().getY(), currentCell.getX(), currentCell.getY());
                currentCell = currentCell.getParent();
            }
        } else {
            System.out.println("\nNo path found for this axises");
        }
        drawCircles(grid, startCell.getX(), startCell.getY(), goalCell.getX(), goalCell.getY());
    }

    /**
     * Get adjacent Cells of current Cell
     * Search in 8 directions
     *
     * @param parent current Cell
     * @param grid   Matrix to search
     * @return adjacent Cells
     */
    private ArrayList<Cell> getAdjacentCells(Cell parent, Cell[][] grid, boolean isDiagonalAllowed) {
        ArrayList<Cell> nei = new ArrayList<>();

        int x = parent.getX();
        int y = parent.getY();
        //TODO optimize this method
        //Right
        if (x < SIZE - 1 && open(x + 1, y)) {
            nei.add(grid[y][x + 1]);
        }
        //Right Bottom
        if (x < SIZE - 1 && y < SIZE - 1 && open(x + 1, y + 1) && isDiagonalAllowed) {
            nei.add(grid[y + 1][x + 1]);
        }
        //Bottom
        if (y < SIZE - 1 && open(x, y + 1)) {
            nei.add(grid[y + 1][x]);
        }
        //Bottom Left
        if (x > 0 && y < SIZE - 1 && open(x - 1, y + 1) && isDiagonalAllowed) {
            nei.add(grid[y + 1][x - 1]);
        }
        //Left
        if (x > 0 && open(x - 1, y)) {
            nei.add(grid[y][x - 1]);
        }
        //Top Left
        if (x > 0 && y > 0 && open(x - 1, y - 1) && isDiagonalAllowed) {
            nei.add(grid[y - 1][x - 1]);
        }
        //Top
        if (y > 0 && open(x, y - 1)) {
            nei.add(grid[y - 1][x]);
        }
        //Top Right
        if (x + 1 < SIZE - 1 && y > 0 && open(x + 1, y - 1) && isDiagonalAllowed) {
            nei.add(grid[y - 1][x + 1]);
        }

        return nei;
    }

    /**
     * Calculate G cost between parent and current Axis
     *
     * @param ax parent X
     * @param ay parent Y
     * @param bx current X
     * @param by current Y
     * @return G cost
     */
    private double gCost(int ax, int ay, int bx, int by) {
        //south east
        if (ax + 1 == bx && ay + 1 == by) {
            return DIAGONAL_COST;
        }
        //south west
        if (ax - 1 == bx && ay + 1 == by) {
            return DIAGONAL_COST;
        }
        //north west
        if (ax - 1 == bx && ay - 1 == by) {
            return DIAGONAL_COST;
        }
        //north east
        if (ax + 1 == bx && ay - 1 == by) {
            return DIAGONAL_COST;
        }
        //east
        if (ax + 1 == bx && ay == by) {
            return HORIZONTAL_VERTICAL_COST;
        }
        //south
        if (ax == bx && ay + 1 == by) {
            return HORIZONTAL_VERTICAL_COST;
        }
        //west
        if (ax - 1 == bx && ay == by) {
            return HORIZONTAL_VERTICAL_COST;
        }
        //north
        if (ax == bx && ay - 1 == by) {
            return HORIZONTAL_VERTICAL_COST;
        }
        //check only the first node
        if (ax == bx && ay == by) {
            return 0;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Calculate H cost using findPath distance
     *
     * @param ax parent X
     * @param ay parent Y
     * @param bx current X
     * @param by current Y
     * @return H cost
     */
    private int manhattanCost(int ax, int ay, int bx, int by) {
        return Math.abs(ax - bx) + Math.abs(ay - by);
    }

    /**
     * Calculate H cost using euclidean distance
     *
     * @param ax parent X
     * @param ay parent Y
     * @param bx current X
     * @param by current Y
     * @return H cost
     * @throws IllegalArgumentException
     */
    private double euclideanCost(int ax, int ay, int bx, int by) {
        return Math.sqrt(Math.pow(ax - bx, 2) + Math.pow(ay - by, 2));
    }

    /**
     * Calculate H cost using Cehbyshev distance
     *
     * @param ax parent X
     * @param ay parent Y
     * @param bx current X
     * @param by current Y
     * @return H cost
     * @throws IllegalArgumentException
     */
    private int chebyshevCost(int ax, int ay, int bx, int by) {
        return (Math.max(Math.abs(ax - bx), Math.abs(ay - by)));
    }

    /**
     * Calculate Cell's Id
     *
     * @param x    X value
     * @param y    Y value
     * @param SIZE grid size
     * @return id
     */
    private static int id(int x, int y, int SIZE) {
        return SIZE * y + x;
    }

    private int[] axis(int id) {
        int y = id / SIZE;
        int x = id - (y * SIZE);
        return new int[]{x, y};
    }

    /**
     * Check given Axis is open
     *
     * @param x X value
     * @param y Y value
     * @return Id
     */
    private boolean open(int x, int y) {
        return grid[y][x].isOpen();
    }

    /**
     * Draw 2 circles in the grid
     *
     * @param a  grid
     * @param x1 first circle's x
     * @param y1 first circle's y
     * @param x2 second circle's x
     * @param y2 second circle's y
     */
    private static void drawCircles(Cell[][] a, int x1, int y1, int x2, int y2) {
        int N = a.length;
        StdDraw.setXscale(-1, N);
        StdDraw.setYscale(-1, N);

        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        StdDraw.filledCircle(x1, N - y1 - 1, .5);

        StdDraw.setPenColor(StdDraw.GREEN);
        StdDraw.filledCircle(x2, N - y2 - 1, .5);
    }

    /**
     * Draw circle in the grid
     *
     * @param a     grid
     * @param x1    circle's x
     * @param y1    circle's y
     * @param color circle's colour
     */
    private static void drawCircle(Cell[][] a, int x1, int y1, Color color) {
        StdDraw.setPenColor(color);
        StdDraw.filledSquare(x1, a.length - y1 - 1, .5);
    }

    /**
     * Draw a line between 2 cells
     *
     * @param x1 first cell's x
     * @param y1 first cell's y
     * @param x2 second cell's x
     * @param y2 second cell's y
     */
    private void drawLine(int x1, int y1, int x2, int y2) {
        StdDraw.setPenColor(Color.RED);
        StdDraw.setPenRadius(0.01);
        StdDraw.line(x2, SIZE - y2 - 1, x1, SIZE - y1 - 1);
    }

    /**
     * Populate a random N-by-N Cell matrix
     * Where each entry is true with probability
     *
     * @param N size
     * @param p probability
     * @return Cell[][] matrix
     */
    private static Cell[][] randomCell(int N, double p) {
        Cell[][] a = new Cell[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int id = id(j, i, a.length);
                a[i][j] = new Cell(j, i, id, StdRandom.bernoulli(p));
            }
        }
        return a;
    }

    /**
     * Print the  M-by-N array of booleans to standard output.
     */
    public static void printCell(Cell[][] a) {
        int M = a.length;
        int N = a[0].length;
        StdOut.println(M + " " + N);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (a[i][j].isOpen()) StdOut.print("1 ");
                else StdOut.print("0 ");
            }
            StdOut.println();
        }
    }

    // draw the N-by-N Cell matrix to standard draw
    private static void showCell(Cell[][] a) {
        int N = a.length;
        StdDraw.setXscale(-1, N);
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (a[i][j].isOpen())
                    StdDraw.square(j, N - i - 1, .5);
                else StdDraw.filledSquare(j, N - i - 1, .5);
    }

    private static Cell[][] arrangedObstacles(int size) {
        Cell[][] cells = new Cell[size][size];
        boolean obstacleDecider = true;
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                if (x % 2 == 0) {
                    cells[x][y] = new Cell(y, x, id(y, x, size), true);
                } else {
                    if (obstacleDecider && y + 1 == size || !obstacleDecider && y == 0) {
                        cells[x][y] = new Cell(y, x, id(y, x, size), true);
                    } else {
                        cells[x][y] = new Cell(y, x, id(y, x, size), false);
                    }
                }
            }
            if (x % 2 != 0) {
                obstacleDecider = !obstacleDecider;
            }
        }
        return cells;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("-----------|Path Finding|-----------");

        System.out.print("Enter grid size: ");
        int size = 0;
        double prob = 0.0;

        boolean isValid = false;
        do {
            try {
                System.out.print("Enter grid size: ");
                size = sc.nextInt();

                System.out.print("Enter probability: ");
                prob = sc.nextDouble();
                if (prob >= 0 && prob <= 1) {
                    isValid = true;
                } else {
                    System.out.println("\nEnter between 0 - 1\n");
                }
            } catch (Exception e) {
                System.out.println("\nInvalid input try again\n");
            }
        } while (!isValid);

        System.out.println();

        Cell[][] randomlyGenMatrix = randomCell(size, prob);
        //Cell[][] randomlyGenMatrix = arrangedObstacles(size);

        do {
            showCell(randomlyGenMatrix);

            int startx = 0;
            int starty = 0;
            int endx = 0;
            int endy = 0;

            String o = null;

            boolean isEmpty = false;
            do {
                try {
                    System.out.print("Enter x for start > ");
                    startx = sc.nextInt();

                    System.out.print("Enter y for start > ");
                    starty = sc.nextInt();

                    if (randomlyGenMatrix[starty][startx].isOpen()) {
                        isEmpty = true;
                    } else {
                        System.out.println("\nAxis is closed Try another one\n");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\nEnter valid answer\n");
                    sc.next();
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("\nInputs are out of range try again\n");
                }
            } while (!isEmpty);

            System.out.println();

            isEmpty = false;

            do {
                try {
                    System.out.print("Enter x for end > ");
                    endx = sc.nextInt();

                    System.out.print("Enter y for end > ");
                    endy = sc.nextInt();

                    if (randomlyGenMatrix[endy][endx].isOpen()) {
                        isEmpty = true;
                    } else {
                        System.out.println("\nAxis is closed Try another one\n");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("\nEnter valid answer\n");
                    sc.next();
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("\nOut of range try again\n");
                    sc.next();
                }
            } while (!isEmpty);

            System.out.println();

            String option = null;
            do {
                //TODO has to change
                System.out.println("Which distance do you prefer?");
                System.out.println("[1] manhattan\n[2] euclidean\n[3] chebyshev\n");
                System.out.print("Answer: ");
                option = sc.next();
                if (!option.matches("[123]")) {
                    System.out.println("Invalid option try again");
                    sc.next();
                }
            } while (!option.matches("[123]"));

            Stopwatch timer = null;

            PathFindingOnSquaredGrid pathFinding = new PathFindingOnSquaredGrid(randomlyGenMatrix);

            Cell start = randomlyGenMatrix[starty][startx];
            Cell end = randomlyGenMatrix[endy][endx];

            switch (option) {
                case "1":
                    timer = new Stopwatch();
                    pathFinding.setManhattanDistance();
                    pathFinding.findPath(start, end);
                    System.out.println("Elapsed time findPath = " + timer.elapsedTime());
                    break;
                case "2":
                    timer = new Stopwatch();
                    pathFinding.setEuclideanDistance();
                    pathFinding.findPath(start, end);
                    System.out.println("Elapsed time euclidean= " + timer.elapsedTime());
                    break;
                case "3":
                    timer = new Stopwatch();
                    pathFinding.setChebyshevDistance();
                    pathFinding.findPath(start, end);
                    System.out.println("Elapsed time chebyshev= " + timer.elapsedTime());
                    break;
            }

            System.out.println();

            boolean isValidated = false;
            do {
                System.out.println("Options\n[Y] Find path with same matrix again\n[N] Try different matrix\n[Q] Quit");
                System.out.print("Answer: ");
                o = sc.next();
                o = o.toLowerCase();
                if (o.matches("[ynq]")) {
                    isValidated = true;
                } else {
                    System.out.println("\nInvalid input try again");
                }
            } while (!isValidated);

            StdDraw.clear();
            StdDraw.setPenColor();
            StdDraw.setPenRadius();
            System.out.println();

            if (o.equals("q")) {
                System.exit(1);
            } else if (o.equals("n")) {
                main(new String[]{});
            }
        } while (true);
    }
}