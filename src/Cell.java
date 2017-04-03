/**
 * Created by ShimaK on 29-Mar-17.
 */
public class Cell {
    private int x;
    private int y;
    private int id;
    private int g;
    private Double h;
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

    public int getG() {
        return g;
    }

    public Double getH() {
        return h;
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

    public void setG(int g) {
        this.g = g;
    }

    public void setH(Double h) {
        this.h = h;
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
