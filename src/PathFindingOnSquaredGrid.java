import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/*************************************************************************
 *  Author: Dr E Kapetanios
 *  Last update: 22-02-2017
 *
 *************************************************************************/

public class PathFindingOnSquaredGrid {

    private static HashMap<Integer, Integer> parentPointer;
    private static ArrayList<Integer> closeList, openList;
    private static Graph graph;
    private static boolean[][] grid;
    private static int SIZE;
    private static int GOALX, GOALY;
    private static int STARTX, STARTY;

    static {
        parentPointer = new HashMap<>();
        closeList = new ArrayList<>();
        openList = new ArrayList<>();
    }

    public void findPath(boolean[][] grid, int startX, int startY, int goalX, int goalY) {
        this.grid = grid;
        SIZE = grid.length;
        GOALX = goalX;
        GOALY = goalY;
        STARTX = startX;
        STARTY = startY;

        graph = new Graph(SIZE * SIZE);
        initializeGraph(grid);
        System.out.println(graph);

        int pointerX = startX;
        int pointerY = startY;
        parentPointer.put(id(pointerX, pointerY), id(pointerX, pointerY));
        //System.out.println("[POINTER ID] " + id(pointerX, pointerY));
        while (id(pointerX, pointerY) != id(goalX, goalY)) {
            int pointerid = id(pointerX, pointerY);
            Iterator iter = graph.getV(pointerid).iterator();

            int min = SIZE * SIZE;
            int minId = 0;
            while (iter.hasNext()) {
                int mover = (Integer) iter.next();
                if (parentPointer.get(mover) == null) {
                    int axis[] = axis(mover);
                    int F = manhattan(axis[0], axis[1], GOALX, GOALY) + manhattan(axis[0], axis[1], STARTX, STARTY);
                    if (F < min) {
                        min = F;
                        minId = mover;
                    }
                }
            }

            iter = null;

            int tempmin = id(pointerX, pointerY);
            parentPointer.put(minId, tempmin);

            int axis[] = axis(minId);
            pointerX = axis[0];
            pointerY = axis[1];

            System.out.println("[ID] " + minId + " [X] " + axis[0] + " [Y] " + axis[1]);
        }

        for (int x : parentPointer.keySet()) {
            System.out.println("[KEY] " + x + " [PARENT] " + parentPointer.get(x));
        }
    }

    private int manhattan(int ax, int ay, int bx, int by) {
        return Math.abs(ax - ay) + Math.abs(bx - by);
    }

    private int id(int x, int y) {
        return SIZE * y + x;
    }

    private int[] axis(int id) {
        int y = id / SIZE;
        int x = id - (y * SIZE);
        return new int[]{x, y};
    }

    private boolean open(int x, int y) {
        return grid[x][y];
    }

    public void initializeGraph(boolean[][] grid) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                //East
                if (x < SIZE - 1 && open(x + 1, y)) {
                    graph.addEdge(id(x, y), id(x + 1, y));
                }
                //South East
                if (x < SIZE - 1 && y < SIZE - 1 && open(x + 1, y + 1)) {
                    graph.addEdge(id(x, y), id(x + 1, y + 1));
                }
                //South
                if (y < SIZE - 1 && open(x, y + 1)) {
                    graph.addEdge(id(x, y), id(x, y + 1));
                }
                //South West
                if (x > 0 && y < SIZE - 1 && open(x - 1, y + 1)) {
                    graph.addEdge(id(x, y), id(x - 1, y + 1));
                }
                //West
                if (y > 0 && open(x, y - 1)) {
                    graph.addEdge(id(x, y), id(x, y - 1));
                }
                //North West
                if (x > 0 && y > 0 && open(x - 1, y - 1)) {
                    graph.addEdge(id(x, y), id(x - 1, y - 1));
                }
                //North
                if (x > 0 && open(x - 1, y)) {
                    graph.addEdge(id(x, y), id(x - 1, y));
                }
                //North East
                if (x < SIZE - 1 && y > 0 && open(x, y - 1)) {
                    graph.addEdge(id(x, y), id(x + 1, y - 1));
                }
            }
        }
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

    // draw the N-by-N boolean matrix to standard draw, including the points A (x1, y1) and B (x2,y2) to be marked by a circle
    public static void show(boolean[][] a, boolean which, int x1, int y1, int x2, int y2) {
        int N = a.length;
        StdDraw.setXscale(-1, N);
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (a[i][j] == which)
                    if ((i == x1 && j == y1) || (i == x2 && j == y2)) {
                        StdDraw.circle(j, N - i - 1, .5);
                    } else StdDraw.square(j, N - i - 1, .5);
                else StdDraw.filledSquare(j, N - i - 1, .5);
    }

    // return a random N-by-N boolean matrix, where each entry is
    // true with probability p
    public static boolean[][] random(int N, double p) {
        boolean[][] a = new boolean[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = StdRandom.bernoulli(p);
        return a;
    }


    // test client
    public static void main(String[] args) {
        // boolean[][] open = StdArrayIO.readBoolean2D();

        // The following will generate a 10x10 squared grid with relatively few obstacles in it
        // The lower the second parameter, the more obstacles (black cells) are generated
        boolean[][] randomlyGenMatrix = random(10, 0.8);

        for (int x = 0; x < randomlyGenMatrix.length; x++) {
            for (int y = 0; y < randomlyGenMatrix.length; y++) {
                randomlyGenMatrix[x][y] = true;
            }
        }

        StdArrayIO.print(randomlyGenMatrix);
        show(randomlyGenMatrix, true);

        System.out.println();
        System.out.println("The system percolates: " + percolates(randomlyGenMatrix));

        System.out.println();
        System.out.println("The system percolates directly: " + percolatesDirect(randomlyGenMatrix));
        System.out.println();

        // Reading the coordinates for points A and B on the input squared grid.

        // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH
        // Start the clock ticking in order to capture the time being spent on inputting the coordinates
        // You should position this command accordingly in order to perform the algorithmic analysis
        Stopwatch timerFlow = new Stopwatch();

        Scanner in = new Scanner(System.in);
        System.out.println("Enter i for A > ");
        int Ai = in.nextInt();

        System.out.println("Enter j for A > ");
        int Aj = in.nextInt();

        System.out.println("Enter i for B > ");
        int Bi = in.nextInt();

        System.out.println("Enter j for B > ");
        int Bj = in.nextInt();

        new PathFindingOnSquaredGrid().findPath(randomlyGenMatrix, Ai, Aj, Bi, Bj);


        // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH
        // Stop the clock ticking in order to capture the time being spent on inputting the coordinates
        // You should position this command accordingly in order to perform the algorithmic analysis
        StdOut.println("Elapsed time = " + timerFlow.elapsedTime());

        // System.out.println("Coordinates for A: [" + Ai + "," + Aj + "]");
        // System.out.println("Coordinates for B: [" + Bi + "," + Bj + "]");

        show(randomlyGenMatrix, true, Ai, Aj, Bi, Bj);
    }

}