import java.awt.*;
import java.util.*;

/*************************************************************************
 *  Author: Dr E Kapetanios
 *  Last update: 22-02-2017
 *
 *************************************************************************/

public class PathFindingOnSquaredGrid {
    private static Cell[][] grid;
    private static int SIZE;
    private static int GOALX, GOALY;

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
        this.SIZE = grid.length;
    }

    /**
     * Find shortest path using Manhattan
     *
     * @param start path finding start Cell
     * @param end   path finding end Cell
     */
    public void findPathManhattan(Cell start, Cell end) {
        PriorityQueue<Cell> openList = new PriorityQueue<>(new Heuristic());
        ArrayList<Cell> closeList = new ArrayList<>();

        GOALX = end.getX();
        GOALY = end.getY();

        Cell lastCell = null;

        start.setG(0);
        start.setParent(start);
        openList.add(start);

        while (!openList.isEmpty()) {
            Cell current = openList.poll();

            if (current.getId() == end.getId()) {
                lastCell = current;
                break;
            }

            closeList.add(current);

            ArrayList<Cell> neighbours = getNeighboursMan(current, grid);
            checkNeighbourMan(neighbours, closeList, openList, current);
        }

        if (lastCell != null) {
            System.out.println("\nTraveled distance (G cost) : " + lastCell.getG());
            while (lastCell != start) {
                //System.out.print(endCell.getId() + " -> " + endCell.getParent().getId() + "\n");
                drawLine(lastCell.getParent().getX(), lastCell.getParent().getY(), lastCell.getX(), lastCell.getY());
                lastCell = lastCell.getParent();
            }
        } else {
            System.out.println("\nNo path found for this axises");
        }
        drawCircles(grid, start.getX(), start.getY(), end.getX(), end.getY());
    }

    /**
     * Finding shortest path using Euclidean
     *
     * @param start path finding start Cell
     * @param end   path finding end Cell
     */
    public void findPathEuclidean(Cell start, Cell end) {
        PriorityQueue<Cell> openList = new PriorityQueue<>(new Heuristic());
        ArrayList<Cell> closeList = new ArrayList<>();

        GOALX = end.getX();
        GOALY = end.getY();

        Cell lastCell = null;

        start.setG(0);
        start.setParent(start);
        openList.add(start);

        while (!openList.isEmpty()) {
            Cell current = openList.poll();

            //drawCircle(grid, current.getX(), current.getY());

            if (current.getId() == end.getId()) {
                lastCell = current;
                break;
            }

            closeList.add(current);

            ArrayList<Cell> neighbours = getNeighbours(current, grid);
            checkNeighboursEuc(neighbours, closeList, openList, current);
        }

        if (lastCell != null) {
            System.out.println("\nTraveled distance (G cost) :" + lastCell.getG());
            while (lastCell != start) {
                //System.out.print(lastCell.getId() + " -> " + lastCell.getParent().getId() + "\n");
                drawLine(lastCell.getParent().getX(), lastCell.getParent().getY(), lastCell.getX(), lastCell.getY());
                lastCell = lastCell.getParent();
            }
        } else {
            System.out.println("\nThere is no path between these axises");
        }
        drawCircles(grid, start.getX(), start.getY(), end.getX(), end.getY());
    }

    /**
     * Finding shortest path using Chebyshev
     *
     * @param start path finding start Cell
     * @param end   path finding end Cell
     */
    public void findPathChebyshev(Cell start, Cell end) {
        PriorityQueue<Cell> openList = new PriorityQueue<>(new Heuristic());
        ArrayList<Cell> closeList = new ArrayList<>();

        GOALX = end.getX();
        GOALY = end.getY();

        Cell lastCell = null;

        start.setG(0);
        start.setParent(start);
        openList.add(start);

        while (!openList.isEmpty()) {
            Cell current = openList.poll();

            if (current.getId() == end.getId()) {
                System.out.println("Traveled distance in Chebyshev: " + current.getG());
                lastCell = current;
                break;
            }

            closeList.add(current);

            ArrayList<Cell> neighbours = getNeighbours(current, grid);
            checkNeighboursChe(neighbours, closeList, openList, current);
        }

        if (lastCell != null) {
            while (lastCell != start) {
                //System.out.print(lastCell.getId() + " -> " + lastCell.getParent().getId() + "\n");
                drawLine(lastCell.getParent().getX(), lastCell.getParent().getY(), lastCell.getX(), lastCell.getY());
                lastCell = lastCell.getParent();
            }
        } else {
            System.out.println("There is no path for these axises\n");
        }
        drawCircles(grid, start.getX(), start.getY(), end.getX(), end.getY());
    }

    /**
     * Check adjacent Cells and add to openList
     * If already added compare G cost with openList Cell's G cost
     * Calculating H value using Manhattan distance
     *
     * @param neighbours Adjacent Cells of current Cell
     * @param closeList  Cells already searched
     * @param openList   Adjacent Cells previously added
     * @param parent     Current Cell
     */
    public void checkNeighbourMan(ArrayList<Cell> neighbours, ArrayList<Cell> closeList, PriorityQueue<Cell> openList, Cell parent) {
        drawCircle(grid, parent.getX(), parent.getY(), Color.ORANGE);
        for (Cell nei : neighbours) {
            if (closeList.contains(nei)) continue;
            drawCircle(grid, nei.getX(), nei.getY(), Color.YELLOW);

            if (!openList.contains(nei)) {
                nei.setParent(parent);
                nei.setG(parent.getG() + gcost(parent.getX(), parent.getY(), nei.getX(), nei.getY()));
                nei.setH((double) manhattan(nei.getX(), parent.getY(), GOALX, GOALY));
                openList.add(nei);
            } else {
                int openG = nei.getG();
                int neighG = parent.getG() + gcost(parent.getX(), parent.getY(), nei.getX(), nei.getY());

                if (neighG < openG) {
                    nei.setG(neighG);
                    nei.setParent(parent);
                }
            }
        }
    }

    /**
     * Check adjacent Cells and add to openList
     * If already added compare G cost with openList Cell's G cost
     * Calculating H value using Euclidean distance
     *
     * @param neighbours Adjacent Cells of current Cell
     * @param closeList  Cells already searched
     * @param openList   Adjacent Cells previously added
     * @param parent     Current Cell
     */
    public void checkNeighboursEuc(ArrayList<Cell> neighbours, ArrayList<Cell> closeList, PriorityQueue<Cell> openList, Cell parent) {
        drawCircle(grid, parent.getX(), parent.getY(), Color.ORANGE);
        for (Cell nei : neighbours) {
            if (closeList.contains(nei)) continue;
            drawCircle(grid, nei.getX(), nei.getY(), Color.YELLOW);

            if (!openList.contains(nei)) {
                nei.setParent(parent);
                nei.setG(parent.getG() + gcost(parent.getX(), parent.getY(), nei.getX(), nei.getY()));
                nei.setH(euclidean(nei.getX(), parent.getY(), GOALX, GOALY));
                openList.add(nei);
            } else {
                int openG = nei.getG();
                int neighG = parent.getG() + gcost(parent.getX(), parent.getY(), nei.getX(), nei.getY());

                if (neighG < openG) {
                    nei.setG(neighG);
                    nei.setParent(parent);
                }
            }
        }
    }

    /**
     * Check adjacent Cells and add to openList
     * If already added compare G cost with openList Cell's G cost
     * Calculating H value using Chebyshev distance
     *
     * @param neighbours Adjacent Cells of current Cell
     * @param closeList  Cells already searched
     * @param openList   Adjacent Cells previously added
     * @param parent     Current Cell
     */
    public void checkNeighboursChe(ArrayList<Cell> neighbours, ArrayList<Cell> closeList, PriorityQueue<Cell> openList, Cell parent) {
        drawCircle(grid, parent.getX(), parent.getY(), Color.ORANGE);
        for (Cell nei : neighbours) {
            if (closeList.contains(nei)) continue;
            drawCircle(grid, nei.getX(), nei.getY(), Color.YELLOW);

            if (!openList.contains(nei)) {
                nei.setParent(parent);
                nei.setG(parent.getG() + gcost(parent.getX(), parent.getY(), nei.getX(), nei.getY()));
                nei.setH((double) chebyshev(nei.getX(), parent.getY(), GOALX, GOALY));
                openList.add(nei);
            } else {
                int openG = nei.getG();
                int neighG = parent.getG() + gcost(parent.getX(), parent.getY(), nei.getX(), nei.getY());

                if (neighG < openG) {
                    nei.setG(neighG);
                    nei.setParent(parent);
                }
            }
        }
    }

    /**
     * Get adjacent Cells of current Cell
     * Search in 8 directions
     *
     * @param parent current Cell
     * @param grid   Matrix to search
     * @return adjacent Cells
     */
    private ArrayList<Cell> getNeighbours(Cell parent, Cell[][] grid) {
        ArrayList<Cell> nei = new ArrayList<>();

        int x = parent.getX();
        int y = parent.getY();

        //East
        if (x < SIZE - 1 && open(x + 1, y)) {
            nei.add(grid[y][x + 1]);
        }
        //South East
        if (x < SIZE - 1 && y < SIZE - 1 && open(x + 1, y + 1)) {
            nei.add(grid[y + 1][x + 1]);
        }
        //South
        if (y < SIZE - 1 && open(x, y + 1)) {
            nei.add(grid[y + 1][x]);
        }
        //South West
        if (x > 0 && y < SIZE - 1 && open(x - 1, y + 1)) {
            nei.add(grid[y + 1][x - 1]);
        }
        //West
        if (x > 0 && open(x - 1, y)) {
            nei.add(grid[y][x - 1]);
        }
        //North West
        if (x > 0 && y > 0 && open(x - 1, y - 1)) {
            nei.add(grid[y - 1][x - 1]);
        }
        //North
        if (y > 0 && open(x, y - 1)) {
            nei.add(grid[y - 1][x]);
        }
        //North East
        if (x + 1 < SIZE - 1 && y > 0 && open(x + 1, y - 1)) {
            nei.add(grid[y - 1][x + 1]);
        }

        return nei;
    }

    /**
     * Get adjacent Cells of current Cell
     * Search in 4 directions
     *
     * @param parent current Cell
     * @param grid   Matrix to search
     * @return adjacent Cells
     */
    private ArrayList<Cell> getNeighboursMan(Cell parent, Cell[][] grid) {
        ArrayList<Cell> nei = new ArrayList<>();

        int x = parent.getX();
        int y = parent.getY();

        //East
        if (x < SIZE - 1 && open(x + 1, y)) {
            nei.add(grid[y][x + 1]);
        }
        //South
        if (y < SIZE - 1 && open(x, y + 1)) {
            nei.add(grid[y + 1][x]);
        }
        //West
        if (x > 0 && open(x - 1, y)) {
            nei.add(grid[y][x - 1]);
        }
        //North
        if (y > 0 && open(x, y - 1)) {
            nei.add(grid[y - 1][x]);
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
    private int gcost(int ax, int ay, int bx, int by) {
        int DIAGONAL = 14;
        int HOR_VERTI = 10;

        //south east
        if (ax + 1 == bx && ay + 1 == by) {
            return DIAGONAL;
        }
        //south west
        if (ax - 1 == bx && ay + 1 == by) {
            return DIAGONAL;
        }
        //north west
        if (ax - 1 == bx && ay - 1 == by) {
            return DIAGONAL;
        }
        //north east
        if (ax + 1 == bx && ay - 1 == by) {
            return DIAGONAL;
        }
        //east
        if (ax + 1 == bx && ay == by) {
            return HOR_VERTI;
        }
        //south
        if (ax == bx && ay + 1 == by) {
            return HOR_VERTI;
        }
        //west
        if (ax - 1 == bx && ay == by) {
            return HOR_VERTI;
        }
        //north
        if (ax == bx && ay - 1 == by) {
            return HOR_VERTI;
        }
        //check only the first node
        if (ax == bx && ay == by) {
            return 0;
        }
        throw new IllegalArgumentException();
    }

    /**
     * Calculate H cost using Manhattan distance
     *
     * @param ax parent X
     * @param ay parent Y
     * @param bx current X
     * @param by current Y
     * @return H cost
     * @throws IllegalArgumentException
     */
    private int manhattan(int ax, int ay, int bx, int by) {
        return Math.abs(ax - bx) + Math.abs(ay - by);
    }

    /**
     * Calculate H cost using Euclidean distance
     *
     * @param ax parent X
     * @param ay parent Y
     * @param bx current X
     * @param by current Y
     * @return H cost
     * @throws IllegalArgumentException
     */
    private double euclidean(int ax, int ay, int bx, int by) {
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
    private int chebyshev(int ax, int ay, int bx, int by) {
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
    protected static int id(int x, int y, int SIZE) {
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
    public static void drawCircles(Cell[][] a, int x1, int y1, int x2, int y2) {
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
    public static void drawCircle(Cell[][] a, int x1, int y1, Color color) {
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
    public static void drawLine(int x1, int y1, int x2, int y2) {
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
    public static Cell[][] randomCell(int N, double p) {
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
    public static void showCell(Cell[][] a) {
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
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("\nOut of range try again\n");
                }
            } while (!isEmpty);

            System.out.println();

            String option = null;
            do {
                //TODO has to change
                System.out.println("Which distance do you prefer?");
                System.out.println("[1] Manhattan\n[2] Euclidean\n[3] Chebyshev\n");
                System.out.print("Answer: ");
                option = sc.next();
                if (!option.matches("[123]")) {
                    System.out.println("Invalid option try again");
                    sc.next();
                }
            } while (!option.matches("[123]"));

            Stopwatch timer = null;

            PathFindingOnSquaredGrid pathfinding = new PathFindingOnSquaredGrid(randomlyGenMatrix);

            Cell start = new Cell(startx, starty, id(startx, starty, randomlyGenMatrix.length), true);
            Cell end = new Cell(endx, endy, id(endx, endy, randomlyGenMatrix.length), true);

            switch (option) {
                case "1":
                    timer = new Stopwatch();
                    pathfinding.findPathManhattan(start, end);
                    //System.out.println("Manhattan");
                    System.out.println("Elapsed time Manhattan = " + timer.elapsedTime());
                    break;
                case "2":
                    timer = new Stopwatch();
                    pathfinding.findPathEuclidean(start, end);
                    //System.out.println("Euclidean");
                    System.out.println("Elapsed time Euclidean= " + timer.elapsedTime());
                    break;
                case "3":
                    timer = new Stopwatch();
                    pathfinding.findPathChebyshev(start, end);
                    //System.out.println("Chebushev");
                    System.out.println("Elapsed time Chebyshev= " + timer.elapsedTime());
                    break;
            }

            System.out.println();

            boolean isValidated = false;
            do {
                System.out.println("Options\n[Y] Find path sc same matrix again\n[N] Try different matrix\n[Q] Quit");
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

    //TODO didnt understand
    class Heuristic implements Comparator<Object> {

        @Override
        public int compare(Object o1, Object o2) {

            Cell cell1 = (Cell) o1;
            Cell cell2 = (Cell) o2;

            return Double.compare(cell1.getH(), cell2.getH());
        }
    }
}