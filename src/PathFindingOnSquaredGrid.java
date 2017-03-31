import java.awt.*;
import java.util.*;

/*************************************************************************
 *  Author: Dr E Kapetanios
 *  Last update: 22-02-2017
 *
 *************************************************************************/

public class PathFindingOnSquaredGrid {

    private static HashMap<Integer, Integer> parentPointer;
    private static HashMap<Integer, Double> gCost;
    private static ArrayList<Integer> closeList, openList;
    private static Cell[][] grid;
    private static int SIZE;
    private static int GOALX, GOALY;

    static {
        parentPointer = new HashMap<>();
        gCost = new HashMap<>();
        closeList = new ArrayList<>();
        openList = new ArrayList<>();
    }

    public void findPathManhattan(Cell[][] grid, Cell start, Cell end) {
        PriorityQueue<Cell> openList = new PriorityQueue<>(new Heuristic());
        ArrayList<Cell> closeList = new ArrayList<>();

        this.grid = grid;
        SIZE = grid.length;

        GOALX = end.getX();
        GOALY = end.getY();

        show(grid, start.getX(), start.getY(), end.getX(), end.getY());

        Cell endCell = null;

        start.setG(0.0);
        start.setParent(start);
        openList.add(start);

        while (!openList.isEmpty()) {
            Cell current = openList.poll();

            //show(grid, current.getX(), current.getY());

            if (current.getId() == end.getId()) {
                System.out.println("[TRAVELED DISTANCE MANHATTAN] " + current.getG());
                endCell = current;
                break;
            }

            closeList.add(current);

            ArrayList<Cell> neighbours = getNeighboursMan(current, grid);
            checkNeighbourMan(neighbours, closeList, openList, current);
        }

        if (endCell != null) {
            while (endCell != start) {
                //System.out.print(endCell.getId() + " -> " + endCell.getParent().getId() + "\n");
                show(grid, endCell.getX(), endCell.getY(), Color.green);
                endCell = endCell.getParent();
            }
        }
    }

    public void findPathEuclidean(Cell[][] grid, Cell start, Cell end) {
        PriorityQueue<Cell> openList = new PriorityQueue<>(new Heuristic());
        ArrayList<Cell> closeList = new ArrayList<>();

        this.grid = grid;
        SIZE = grid.length;

        GOALX = end.getX();
        GOALY = end.getY();

        show(grid, start.getX(), start.getY(), end.getX(), end.getY());

        Cell endCell = null;

        start.setG(0.0);
        start.setParent(start);
        openList.add(start);

        while (!openList.isEmpty()) {
            Cell current = openList.poll();

            //show(grid, current.getX(), current.getY());

            if (current.getId() == end.getId()) {
                System.out.println("[TRAVELED DISTANCE EUCLIDEAN] " + current.getG());
                endCell = current;
                break;
            }

            closeList.add(current);

            ArrayList<Cell> neighbours = getNeighbours(current, grid);
            checkNeighboursEuc(neighbours, closeList, openList, current);
        }

        if (endCell != null) {
            while (endCell != start) {
                //System.out.print(endCell.getId() + " -> " + endCell.getParent().getId() + "\n");
                show(grid, endCell.getX(), endCell.getY(), Color.BLUE);
                endCell = endCell.getParent();
            }
        }
    }

    public void findPathChebyshev(Cell[][] grid, Cell start, Cell end) {
        PriorityQueue<Cell> openList = new PriorityQueue<>(new Heuristic());
        ArrayList<Cell> closeList = new ArrayList<>();

        this.grid = grid;
        SIZE = grid.length;

        GOALX = end.getX();
        GOALY = end.getY();

        show(grid, start.getX(), start.getY(), end.getX(), end.getY());

        Cell endCell = null;

        start.setG(0.0);
        start.setParent(start);
        openList.add(start);

        while (!openList.isEmpty()) {
            Cell current = openList.poll();

            //show(grid, current.getX(), current.getY());

            if (current.getId() == end.getId()) {
                System.out.println("[TRAVELED DISTANCE CHEBYSHEV] " + current.getG());
                endCell = current;
                break;
            }

            closeList.add(current);

            ArrayList<Cell> neighbours = getNeighbours(current, grid);
            checkNeighboursChe(neighbours, closeList, openList, current);
        }

        if (endCell != null) {
            while (endCell != start) {
                //System.out.print(endCell.getId() + " -> " + endCell.getParent().getId() + "\n");
                show(grid, endCell.getX(), endCell.getY(), Color.CYAN);
                endCell = endCell.getParent();
            }
        }
    }

    public void checkNeighbourMan(ArrayList<Cell> neighbours, ArrayList<Cell> closeList, PriorityQueue<Cell> openList, Cell parent) {
        for (Cell nei : neighbours) {
            if (closeList.contains(nei)) continue;

            if (!openList.contains(nei)) {
                nei.setParent(parent);
                nei.setG(parent.getG() + gcost(parent.getX(), parent.getY(), nei.getX(), nei.getY()));
                nei.setH((double) manhattan(nei.getX(), parent.getY(), GOALX, GOALY));
                openList.add(nei);
            } else {
                double openG = nei.getG();
                double neighG = parent.getG() + gcost(parent.getX(), parent.getY(), nei.getX(), nei.getY());

                if (neighG < openG) {
                    nei.setG(neighG);
                    nei.setParent(parent);
                }
            }
        }
    }

    public void checkNeighboursEuc(ArrayList<Cell> neighbours, ArrayList<Cell> closeList, PriorityQueue<Cell> openList, Cell parent) {
        for (Cell nei : neighbours) {
            if (closeList.contains(nei)) continue;

            if (!openList.contains(nei)) {
                nei.setParent(parent);
                nei.setG(parent.getG() + gcost(parent.getX(), parent.getY(), nei.getX(), nei.getY()));
                nei.setH(euclidean(nei.getX(), parent.getY(), GOALX, GOALY));
                openList.add(nei);
            } else {
                double openG = nei.getG();
                double neighG = parent.getG() + gcost(parent.getX(), parent.getY(), nei.getX(), nei.getY());

                if (neighG < openG) {
                    nei.setG(neighG);
                    nei.setParent(parent);
                }
            }
        }
    }

    public void checkNeighboursChe(ArrayList<Cell> neighbours, ArrayList<Cell> closeList, PriorityQueue<Cell> openList, Cell parent) {
        for (Cell nei : neighbours) {
            if (closeList.contains(nei)) continue;

            if (!openList.contains(nei)) {
                nei.setParent(parent);
                nei.setG(parent.getG() + gcost(parent.getX(), parent.getY(), nei.getX(), nei.getY()));
                nei.setH((double) chebyshev(nei.getX(), parent.getY(), GOALX, GOALY));
                openList.add(nei);
            } else {
                double openG = nei.getG();
                double neighG = parent.getG() + gcost(parent.getX(), parent.getY(), nei.getX(), nei.getY());

                if (neighG < openG) {
                    nei.setG(neighG);
                    nei.setParent(parent);
                }
            }
        }
    }

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

    private double gcost(int ax, int ay, int bx, int by) {
        double DIAGONAL = 1.4;
        double HOR_VERTI = 1;
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

    private int manhattan(int ax, int ay, int bx, int by) {
        return Math.abs(ax - bx) + Math.abs(ay - by);
    }

    private double euclidean(int ax, int ay, int bx, int by) {
        return Math.sqrt(Math.pow(ax - bx, 2) + Math.pow(ay - by, 2));
    }

    private int chebyshev(int ax, int ay, int bx, int by) {
        return (Math.max(Math.abs(ax - bx), Math.abs(ay - by)));
    }

    private int id(int x, int y) {
        return SIZE * y + x;
    }

    private static int id(int x, int y, int SIZE) {
        return SIZE * y + x;
    }

    private int[] axis(int id) {
        int y = id / SIZE;
        int x = id - (y * SIZE);
        return new int[]{x, y};
    }

    private boolean open(int x, int y) {
        return grid[y][x].isOpen();
    }

    private static void clear() {
        parentPointer.clear();
        gCost.clear();
        closeList.clear();
        openList.clear();
    }

    // given an N-by-N matrix of open cells, return an N-by-N matrix
    // of cells reachable from the top
    public static boolean[][] flow(boolean[][] open) {
        int N = open.length;

        boolean[][] full = new boolean[N][N];
        for (int j = 0; j < N; j++) {
            flow(open, full, 0, j);
        }

        return full;
    }

    // determine set of open/blocked cells using depth first search
    public static void flow(boolean[][] open, boolean[][] full, int i, int j) {
        int N = open.length;

        // base cases
        if (i < 0 || i >= N) return;    // invalid row
        if (j < 0 || j >= N) return;    // invalid column
        if (!open[i][j]) return;        // not an open cell
        if (full[i][j]) return;         // already marked as open

        full[i][j] = true;

        flow(open, full, i + 1, j);   // down
        flow(open, full, i, j + 1);   // right
        flow(open, full, i, j - 1);   // left
        flow(open, full, i - 1, j);   // up
    }

    // does the system percolate?
    public static boolean percolates(boolean[][] open) {
        int N = open.length;

        boolean[][] full = flow(open);
        for (int j = 0; j < N; j++) {
            if (full[N - 1][j]) return true;
        }

        return false;
    }

    // does the system percolate vertically in a direct way?
    public static boolean percolatesDirect(boolean[][] open) {
        int N = open.length;

        boolean[][] full = flow(open);
        int directPerc = 0;
        for (int j = 0; j < N; j++) {
            if (full[N - 1][j]) {
                // StdOut.println("Hello");
                directPerc = 1;
                int rowabove = N - 2;
                for (int i = rowabove; i >= 0; i--) {
                    if (full[i][j]) {
                        // StdOut.println("i: " + i + " j: " + j + " " + full[i][j]);
                        directPerc++;
                    } else break;
                }
            }
        }

        // StdOut.println("Direct Percolation is: " + directPerc);
        if (directPerc == N) return true;
        else return false;
    }

    // draw the N-by-N boolean matrix to standard draw
    public static void show(boolean[][] a, boolean which) {
        int N = a.length;
        StdDraw.setXscale(-1, N);
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (a[i][j] == which)
                    StdDraw.square(j, N - i - 1, .5);
                else StdDraw.filledSquare(j, N - i - 1, .5);
    }

    // draw the N-by-N boolean matrix to standard draw
    public static void show(Cell[][] a, int x1, int y1, int x2, int y2) {
        int N = a.length;
        StdDraw.setXscale(-1, N);
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (a[i][j].isOpen())
                    if ((i == x1 && j == y1) || (i == x2 && j == y2)) {
                        StdDraw.circle(j, N - i - 1, .5);
                    } else StdDraw.square(j, N - i - 1, .5);
                else StdDraw.filledSquare(j, N - i - 1, .5);
    }

    public static void show(Cell[][] a, int x1, int y1, Color color) {
        StdDraw.setPenColor(color);
        StdDraw.filledSquare(x1, a.length - y1 - 1, .5);
    }

    // return a random N-by-N boolean matrix, where each entry is
    // true with probability p
    public static Cell[][] randomCell(int N, double p) {
        Cell[][] a = new Cell[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                int id = id(j, i, a.length);
                a[i][j] = new Cell(j, i, id, StdRandom.bernoulli(p));
            }
        }

        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                System.out.print(a[x][y].getId() + " ");
            }
            System.out.println();
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

    // test client
    public static void main(String[] args) {
        // boolean[][] open = StdArrayIO.readBoolean2D();

        // The following will generate a 10x10 squared grid with relatively few obstacles in it
        // The lower the second parameter, the more obstacles (black cells) are generated
        //boolean[][] randomlyGenMatrix = random(100, 0.8);

        Cell[][] randomlyGenMatrix = randomCell(100, 0.8);

        printCell(randomlyGenMatrix);
        showCell(randomlyGenMatrix);

       /* System.out.println();
        System.out.println("The system percolates: " + percolates(randomlyGenMatrix));

        System.out.println();
        System.out.println("The system percolates directly: " + percolatesDirect(randomlyGenMatrix));
        System.out.println();*/

        // Reading the coordinates for points A and B on the input squared grid.

        // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH

        Scanner in = new Scanner(System.in);
        System.out.print("Enter i for A > ");
        int Ai = in.nextInt();

        System.out.print("Enter j for A > ");
        int Aj = in.nextInt();

        System.out.print("Enter i for B > ");
        int Bi = in.nextInt();

        System.out.print("Enter j for B > ");
        int Bj = in.nextInt();

        PathFindingOnSquaredGrid pathfinding = new PathFindingOnSquaredGrid();

        Cell start = new Cell(Ai, Aj, id(Ai, Aj, randomlyGenMatrix.length), true);
        Cell end = new Cell(Bi, Bj, id(Bi, Bj, randomlyGenMatrix.length), true);

        //pathfinding.findPathManhattan(randomlyGenMatrix, start, end);

        Scanner sc = new Scanner(System.in);

        // Start the clock ticking in order to capture the time being spent on inputting the coordinates
        // You should position this command accordingly in order to perform the algorithmic analysis
        Stopwatch timer = new Stopwatch();

        pathfinding.findPathManhattan(randomlyGenMatrix, start, end);

        // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH
        // Stop the clock ticking in order to capture the time being spent on inputting the coordinates
        // You should position this command accordingly in order to perform the algorithmic analysis
        StdOut.println("Elapsed time Manhattan = " + timer.elapsedTime());

        clear();

        sc.nextInt();

        timer = new Stopwatch();

        pathfinding.findPathEuclidean(randomlyGenMatrix, start, end);
        StdOut.println("Elapsed time Euclidean= " + timer.elapsedTime());

        clear();

        sc.nextInt();

        timer = new Stopwatch();

        pathfinding.findPathChebyshev(randomlyGenMatrix, start, end);

        StdOut.println("Elapsed time Chebyshev= " + timer.elapsedTime());

        //show(randomlyGenMatrix, true, Aj, Ai, Bj, Bi);
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