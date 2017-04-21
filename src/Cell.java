/**
 * Created by ShimaK on 29-Mar-17.
 */
public class Cell {
    private int x;
    private int y;
    private int id;
    private double g;
    private double h;
    private double f;
    private Cell parent;
    private boolean isOpen;

    public Cell(int x, int y, int id, boolean open) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.isOpen = open;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getId() {
        return id;
    }

    public double getG() {
        return g;
    }

    public double getH() {
        return h;
    }

    public double getF() {
        return f;
    }

    public Cell getParent() {
        return parent;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void setF(double f) {
        this.f = f;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public Cell clone() throws CloneNotSupportedException {
        return (Cell) super.clone();
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                ", g=" + g +
                '}';
    }
}
