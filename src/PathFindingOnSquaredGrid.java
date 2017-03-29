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
    private static HashMap<Integer, Double> gCost;
    private static ArrayList<Integer> closeList, openList;
    private static boolean[][] grid;
    private static int SIZE;
    private static int GOALX, GOALY;
    private static int STARTX, STARTY;

    static {
        parentPointer = new HashMap<>();
        gCost = new HashMap<>();
        closeList = new ArrayList<>();
        openList = new ArrayList<>();
    }

    public void path(boolean[][] grid, int startX, int startY, int goalX, int goalY) {
        this.grid = grid;
        SIZE = grid.length;
        GOALX = goalX;
        GOALY = goalY;
        STARTX = startX;
        STARTY = startY;

        int goalAxis = id(goalX, goalY);

        openList.add(id(startX, startY));
        parentPointer.put(id(startX, startY), id(startX, startY));
        gCost.put(id(startX, startY), 0.0);

        while (!openList.isEmpty()) {
            int pointer = openList.get(openList.indexOf(searchMinH(openList)));

            int[] poiA = axis(pointer);
            show(grid, true, poiA[0], poiA[1]);

            if (pointer == goalAxis) break;

            openList.remove(openList.indexOf(pointer));
            closeList.add(pointer);

            //TODO declare hashmap here and pass it's reference - [DONE]
            //TODO test
            HashMap<Integer, Double> neighbourCost = new HashMap<>();
            ArrayList<Integer> neighbours = neighbours(pointer, neighbourCost);
            gcostNeighbours(neighbourCost, pointer);

            for (int x = 0; x < neighbours.size(); x++) {
                //int[] parenAxis = axis(pointer);
                //int[] childAxis = axis(neighbours.get(x));
                //gCost.put(neighbours.get(x), gcost(parenAxis[0], parenAxis[1], childAxis[0], childAxis[1]));

                int n = neighbours.get(x);
                System.out.println("[ID] " + n);
                if (closeList.contains(n)) continue;

                if (!openList.contains(n)) {
                    openList.add(n);
                    gCost.put(n, neighbourCost.get(n));
                    parentPointer.put(n, pointer);
                    System.out.println(parentPointer.toString());
                } else {
                    int open = openList.get(openList.indexOf(neighbours.get(x)));
                    int nei = neighbours.get(x);

                    double opee = gCost.get(nei);
                    double nee = neighbourCost.get(open);

                    if (nee < opee) {
                        gCost.put(nei, neighbourCost.get(open));
                        parentPointer.put(nei, pointer);
                    }
                }
            }
        }
    }

    public void mergesort(ArrayList<Integer> inputArray) {
        for (int i = 1; i < inputArray.size(); i++) {
            int key = inputArray.get(i);
            for (int j = i - 1; j >= 0; j--) {
                if (key < inputArray.get(j)) {
                    // Shifting Each Element to its right as key is less than the existing element at current index
                    inputArray.set(j + 1, inputArray.get(j));
                    // Special case scenario when all elements are less than key, so placing key value at 0th Position
                    if (j == 0) {
                        inputArray.set(0, key);
                    }
                } else {
                    // Putting Key value after element at current index as Key value is no more less than the existing element at current index
                    inputArray.set(j + 1, key);
                    break; // You need to break the loop to save un necessary iteration
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

    private int searchMinH(ArrayList<Integer> arr) {
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

    public static void show(boolean[][] a, boolean which, int x1, int y1) {
        int N = a.length;
        StdDraw.setXscale(-1, N);
        StdDraw.setYscale(-1, N);
        StdDraw.setPenColor(StdDraw.BLACK);
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (a[i][j] == which)
                    if ((i == x1 && j == y1)) {
                        StdDraw.filledCircle(j, N - i - 1, .5);
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

       /* System.out.println();
        System.out.println("The system percolates: " + percolates(randomlyGenMatrix));

        System.out.println();
        System.out.println("The system percolates directly: " + percolatesDirect(randomlyGenMatrix));
        System.out.println();*/

        // Reading the coordinates for points A and B on the input squared grid.

        // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH
        // Start the clock ticking in order to capture the time being spent on inputting the coordinates
        // You should position this command accordingly in order to perform the algorithmic analysis
        Stopwatch timerFlow = new Stopwatch();

        Scanner in = new Scanner(System.in);
        System.out.print("Enter i for A > ");
        int Ai = in.nextInt();

        System.out.print("Enter j for A > ");
        int Aj = in.nextInt();

        System.out.print("Enter i for B > ");
        int Bi = in.nextInt();

        System.out.print("Enter j for B > ");
        int Bj = in.nextInt();

        //new PathFindingOnSquaredGrid().findPath(randomlyGenMatrix, Aj, Ai, Bj, Bi);
        new PathFindingOnSquaredGrid().path(randomlyGenMatrix, Aj, Ai, Bj, Bi);


        // THIS IS AN EXAMPLE ONLY ON HOW TO USE THE JAVA INTERNAL WATCH
        // Stop the clock ticking in order to capture the time being spent on inputting the coordinates
        // You should position this command accordingly in order to perform the algorithmic analysis
        StdOut.println("Elapsed time = " + timerFlow.elapsedTime());

        // System.out.println("Coordinates for A: [" + Ai + "," + Aj + "]");
        // System.out.println("Coordinates for B: [" + Bi + "," + Bj + "]");

        show(randomlyGenMatrix, true, Aj, Ai, Bj, Bi);
    }

}