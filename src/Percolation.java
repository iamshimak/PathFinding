/**
 * Created by ShimaK on 21-Mar-17.
 */
public class Percolation {
    private boolean[][] field;
    private int index[];
    private final int COLUMN_SIZE;
    private final int LENGTH;

    public Percolation(int N) {
        COLUMN_SIZE = N;
        field = new boolean[N][N];
        index = new int[N * N];
        LENGTH = N * N;
        for (int x = 0; x < N * N; x++) {
            index[x] = x;
        }
    }

    public void open(int row, int column) {
        field[row][column] = true;
        checkNeighbours(row, column);
    }

    public void checkNeighbours(int row, int column) {
        int currentNode = getIndex(row, column);
        //top neighbour
        if (row - 1 >= 0 && isOpen(row - 1, column)) {
            union(currentNode, getIndex(row - 1, column));
        }
        //right neighbour
        if (column + 1 < COLUMN_SIZE && isOpen(row, column + 1)) {
            union(currentNode, getIndex(row, column + 1));
        }
        //left neighbour
        if (column - 1 >= 0 && isOpen(row, column - 1)) {
            union(currentNode, getIndex(row, column - 1));
        }
        //bottom neighbour
        if (row + 1 < COLUMN_SIZE && isOpen(row + 1, column)) {
            union(currentNode, getIndex(row + 1, column));
        }
    }

    public boolean isOpen(int row, int column) {
        return field[row][column];
    }

    public int getIndex(int row, int column) {
        return row * COLUMN_SIZE + column;
    }

    public void union(int x, int y) {
        int rootX = getRoot(x);
        int rootY = getRoot(y);
        if (rootX == rootY) return;
        index[x] = rootY;
    }

    public int getRoot(int base) {
        while (base != index[base]) {
            base = index[base];
        }
        return base;
    }

    public void display() {
        System.out.println("---------------------------------");
        for (int x = 0; x < COLUMN_SIZE; x++) {
            for (int y = 0; y < COLUMN_SIZE; y++) {
                if (field[x][y]) {
                    System.out.print(1);
                } else {
                    System.out.print(0);
                }
            }
            System.out.println();
        }
        System.out.println("---------------------------------");
    }

    public boolean percolates() {
        int topid, bottomid;
        for (int x = 0; x < COLUMN_SIZE; x++) {
            topid = getIndex(0, x);
            for (int y = LENGTH - 1; y >= LENGTH - COLUMN_SIZE; y--) {
                bottomid = getIndex(COLUMN_SIZE - 1, y - (LENGTH - COLUMN_SIZE));
                if (getRoot(topid) == getRoot(bottomid)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        Percolation p = new Percolation(5);
        p.open(0, 3);
        p.display();
        System.out.println("Percolates " + p.percolates());
        p.open(1, 3);
        p.display();
        System.out.println("Percolates " + p.percolates());
        p.open(2, 3);
        p.display();
        System.out.println("Percolates " + p.percolates());
        p.open(3, 3);
        p.display();
        System.out.println("Percolates " + p.percolates());
        p.open(4, 3);
        p.display();
        System.out.println("Percolates " + p.percolates());
    }
}
