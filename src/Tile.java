/*
    This class holds string value, row and column of the tile which player plays
 */

public class Tile {
    String string;
    int row;
    int col;

    public Tile(String string, int row, int col) {
        this.string = string;
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "Tile{" +
                "string='" + string + '\'' +
                ", row=" + row +
                ", col=" + col +
                '}';
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
}
