import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/*************************************************************************
 *  Author: Dr E Kapetanios
 *  Last update: 22-02-2017
 *
 *************************************************************************/

public class PathFindingOnSquaredGrid {

    private static HashMap<Integer, Integer> parentPointer;
    private static HashMap<Integer, Double> gCost;
    private static ArrayList<Integer> closeList, openList;
    private static boolean[][] grid;
    private static int SIZE;
    private static int GOALX, GOALY;

    static {
        parentPointer = new HashMap<>();
        gCost = new HashMap<>();
        closeList = new ArrayList<>();
        openList = new ArrayList<>();
    }

    public void pathFindingManhattan(boolean[][] grid, int startX, int startY, int goalX, int goalY) {
        this.grid = grid;
        SIZE = grid.length;
        GOALX = goalX;
        GOALY = goalY;

        int goalAxis = id(goalX, goalY);

        openList.add(id(startX, startY));
        parentPointer.put(id(startX, startY), id(startX, startY));
        gCost.put(id(startX, startY), 0.0);

        while (!openList.isEmpty()) {
            int pointer = openList.get(openList.indexOf(searchMinHMan(openList)));

            int[] poiA = axis(pointer);
            show(grid, poiA[0], poiA[1]);

            if (pointer == goalAxis) {
                System.out.println("Path distance " + gCost.get(pointer));
                break;
            }

            openList.remove(openList.indexOf(pointer));
            closeList.add(pointer);

            HashMap<Integer, Double> neighbourCost = new HashMap<>();
            ArrayList<Integer> neighbours = neighbours(pointer, neighbourCost);
            gcostNeighbours(neighbourCost, pointer);

            addNeighbours(neighbours, neighbourCost, pointer);
        }
    }

    public void pathFindingEuclidean(boolean[][] grid, int startX, int startY, int goalX, int goalY) {
        this.grid = grid;
        SIZE = grid.length;
        GOALX = goalX;
        GOALY = goalY;

        int goalAxis = id(goalX, goalY);

        openList.add(id(startX, startY));
        parentPointer.put(id(startX, startY), id(startX, startY));
        gCost.put(id(startX, startY), 0.0);

        while (!openList.isEmpty()) {
            int pointer = openList.get(openList.indexOf(searchMinHEcu(openList)));

            int[] poiA = axis(pointer);
            show(grid, poiA[0], poiA[1]);

            if (pointer == goalAxis) {
                System.out.println("Path distance " + gCost.get(pointer));
                break;
            }

            openList.remove(openList.indexOf(pointer));
            closeList.add(pointer);

            HashMap<Integer, Double> neighbourCost = new HashMap<>();
            ArrayList<Integer> neighbours = neighbours(pointer, neighbourCost);
            gcostNeighbours(neighbourCost, pointer);

            addNeighbours(neighbours, neighbourCost, pointer);
        }
    }

    public void pathFindingChebyshev(boolean[][] grid, int startX, int startY, int goalX, int goalY) {
        this.grid = grid;
        SIZE = grid.length;
        GOALX = goalX;
        GOALY = goalY;

        int goalAxis = id(goalX, goalY);

        openList.add(id(startX, startY));
        parentPointer.put(id(startX, startY), id(startX, startY));
        gCost.put(id(startX, startY), 0.0);

        while (!openList.isEmpty()) {
            int pointer = openList.get(openList.indexOf(searchMinHChe(openList)));

            int[] poiA = axis(pointer);
            show(grid, poiA[0], poiA[1]);

            if (pointer == goalAxis) {
                System.out.println("Path distance " + gCost.get(pointer));
                break;
            }

            openList.remove(openList.indexOf(pointer));
            closeList.add(pointer);

            HashMap<Integer, Double> neighbourCost = new HashMap<>();
            ArrayList<Integer> neighbours = neighbours(pointer, neighbourCost);
            gcostNeighbours(neighbourCost, pointer);

            addNeighbours(neighbours, neighbourCost, pointer);
        }
    }

    public void pathManttan() {

    }

    private void addNeighbours(ArrayList<Integer> neighbours, HashMap<Integer, Double> neighbourCost, int parent) {
        for (int x = 0; x < neighbours.size(); x++) {
            int n = neighbours.get(x);
            //System.out.println("[ID] " + n);
            if (closeList.contains(n)) continue;

            if (!openList.contains(n)) {
                openList.add(n);
                gCost.put(n, neighbourCost.get(n));
                parentPointer.put(n, parent);
                //System.out.println(parentPointer.toString());
            } else {
                int open = openList.get(openList.indexOf(neighbours.get(x)));
                int nei = neighbours.get(x);

                double opee = gCost.get(nei);
                double nee = neighbourCost.get(open);

                if (nee < opee) {
                    gCost.put(nei, neighbourCost.get(open));
                    parentPointer.put(nei, parent);
                }
            }
        }
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

    private int searchMinHMan(ArrayList<Integer> arr) {
        int current = arr.get(0);
        int parent = parentPointer.get(current);
        int[] parentAxis = axis(parent);
        int[] childAxis = axis(current);
        double minG = gcost(parentAxis[0], parentAxis[1], childAxis[0], childAxis[1]);
        double minH = manhattan(childAxis[0], childAxis[1], GOALX, GOALY);
        int minId = current;

        for (int x = 1; x < arr.size(); x++) {
            current = arr.get(x);
            parent = parentPointer.get(current);
            parentAxis = axis(parent);
            childAxis = axis(current);
            double G = gcost(parentAxis[0], parentAxis[1], childAxis[0], childAxis[1]);
            double H = manhattan(childAxis[0], childAxis[1], GOALX, GOALY);
            if (H < minH) {
                minH = H;
                minId = id(childAxis[0], childAxis[1]);
            }
        }

        return minId;
    }

    private int searchMinHEcu(ArrayList<Integer> arr) {
        int current = arr.get(0);
        int[] childAxis = axis(current);
        double minH = euclidean(childAxis[0], childAxis[1], GOALX, GOALY);
        int minId = current;

        for (int x = 1; x < arr.size(); x++) {
            current = arr.get(x);
            childAxis = axis(current);
            double H = euclidean(childAxis[0], childAxis[1], GOALX, GOALY);
            if (H < minH) {
                minH = H;
                minId = id(childAxis[0], childAxis[1]);
            }
        }

        return minId;
    }

    private int searchMinHChe(ArrayList<Integer> arr) {
        int current = arr.get(0);
        int[] childAxis = axis(current);
        double minH = chebyshev(childAxis[0], childAxis[1], GOALX, GOALY);
        int minId = current;

        for (int x = 1; x < arr.size(); x++) {
            current = arr.get(x);
            childAxis = axis(current);
            double H = chebyshev(childAxis[0], childAxis[1], GOALX, GOALY);
            if (H < minH) {
                minH = H;
                minId = id(childAxis[0], childAxis[1]);
            }
        }

        return minId;
    }

    private ArrayList<Integer> neighbours(int id, HashMap<Integer, Double> gCost) {
        ArrayList<Integer> nei = new ArrayList<>();

        int[] ax = axis(id);
        double G = this.gCost.get(id);

        //East
        if (ax[0] < SIZE - 1 && open(ax[0] + 1, ax[1])) {
            nei.add(id(ax[0] + 1, ax[1]));
            double gcost = gcost(ax[0], ax[1], ax[0] + 1, ax[1]);
            gCost.put(id(ax[0] + 1, ax[1]), G + gcost);
        }
        //South East
        if (ax[0] < SIZE - 1 && ax[1] < SIZE - 1 && open(ax[0] + 1, ax[1] + 1)) {
            nei.add(id(ax[0] + 1, ax[1] + 1));
            double gcost = gcost(ax[0], ax[1], ax[0] + 1, ax[1] + 1);
            gCost.put(id(ax[0] + 1, ax[1] + 1), G + gcost);
        }
        //South
        if (ax[1] < SIZE - 1 && open(ax[0], ax[1] + 1)) {
            nei.add(id(ax[0], ax[1] + 1));
            double gcost = gcost(ax[0], ax[1], ax[0], ax[1] + 1);
            gCost.put(id(ax[0], ax[1] + 1), G + gcost);
        }
        //South West
        if (ax[0] > 0 && ax[1] < SIZE - 1 && open(ax[0] - 1, ax[1] + 1)) {
            nei.add(id(ax[0] - 1, ax[1] + 1));
            double gcost = gcost(ax[0], ax[1], ax[0] - 1, ax[1] + 1);
            gCost.put(id(ax[0] - 1, ax[1] + 1), G + gcost);
        }
        //West
        if (ax[0] > 0 && open(ax[0] - 1, ax[1])) {
            nei.add(id(ax[0] - 1, ax[1]));
            double gcost = gcost(ax[0], ax[1], ax[0] - 1, ax[1]);
            gCost.put(id(ax[0] - 1, ax[1]), G + gcost);
        }
        //North West
        if (ax[0] > 0 && ax[1] > 0 && open(ax[0] - 1, ax[1] - 1)) {
            nei.add(id(ax[0] - 1, ax[1] - 1));
            double gcost = gcost(ax[0], ax[1], ax[0] - 1, ax[1] - 1);
            gCost.put(id(ax[0] - 1, ax[1] - 1), G + gcost);
        }
        //North
        if (ax[1] > 0 && open(ax[0], ax[1] - 1)) {
            nei.add(id(ax[0], ax[1] - 1));
            double gcost = gcost(ax[0], ax[1], ax[0], ax[1] - 1);
            gCost.put(id(ax[0], ax[1] - 1), G + gcost);
        }
        //North East
        if (ax[0] + 1 < SIZE - 1 && ax[1] > 0 && open(ax[0] + 1, ax[1] - 1)) {
            nei.add(id(ax[0] + 1, ax[1] - 1));
            double gcost = gcost(ax[0], ax[1], ax[0] + 1, ax[1] - 1);
            gCost.put(id(ax[0] + 1, ax[1] - 1), G + gcost);
        }

        return nei;
    }

    private void gcostNeighbours(HashMap<Integer, Double> n, int point) {
        double pg = gCost.get(point);
        int[] parent = axis(point);

        // Iterating over keys only
        for (Integer id : n.keySet()) {
            int[] child = axis(id);
            double cg = gcost(parent[0], parent[1], child[0], child[1]);
            n.put(id, pg + cg);
        }
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

    private static int id(int x, int y) {
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

    public static void show(boolean[][] a, int x1, int y1) {
        StdDraw.setPenColor(StdDraw.ORANGE);
        StdDraw.filledSquare(y1, a.length - x1 - 1, .5);
        /*int N = a.length;
        StdDraw.setXscale(-1, N);
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (a[i][j] == which)
                    if ((i == x1 && j == y1)) {
                        StdDraw.setPenColor(StdDraw.ORANGE);
                        StdDraw.filledSquare(j, N - i - 1, .5);
                    } else StdDraw.square(j, N - i - 1, .5);
                else{
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.filledSquare(j, N - i - 1, .5);
                }*/
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

    public static Cell[][] randomCell(int N, double p) {
        Cell[][] a = new Cell[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                a[i][j] = new Cell(j, i, id(j, i), StdRandom.bernoulli(p));
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

        /*for (int x = 0; x < randomlyGenMatrix.length; x++) {
            for (int y = 0; y < randomlyGenMatrix.length; y++) {
                randomlyGenMatrix[x][y] = true;
            }
        }*/

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

        /*Scanner sc = new Scanner(System.in);

        // Start the clock ticking in order to capture the time being spent on inputting the coordinates
        // You should position this command accordingly in order to perform the algorithmic analysis
        Stopwatch timer = new Stopwatch();

        new PathFindingOnSquaredGrid().pathFindingManhattan(randomlyGenMatrix, Aj, Ai, Bj, Bi);

        // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH
        // Stop the clock ticking in order to capture the time being spent on inputting the coordinates
        // You should position this command accordingly in order to perform the algorithmic analysis
        StdOut.println("Elapsed time Manhattan = " + timer.elapsedTime());

        clear();

        sc.nextInt();

        timer = new Stopwatch();

        new PathFindingOnSquaredGrid().pathFindingEuclidean(randomlyGenMatrix, Aj, Ai, Bj, Bi);
        StdOut.println("Elapsed time Euclidean= " + timer.elapsedTime());

        clear();

        sc.nextInt();

        timer = new Stopwatch();

        new PathFindingOnSquaredGrid().pathFindingChebyshev(randomlyGenMatrix, Aj, Ai, Bj, Bi);

        StdOut.println("Elapsed time Chebyshev= " + timer.elapsedTime());*/

        //show(randomlyGenMatrix, true, Aj, Ai, Bj, Bi);
    }

}