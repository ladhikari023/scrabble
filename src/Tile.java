/*
    This class holds string value, row and column of the tile which player plays
 */

import java.util.HashMap;
import java.util.Map;

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

    public int getValue(){
        int value = 0;
        Map<String,Integer> map = new HashMap();
        map.put("A",1);
        map.put("B",2);
        map.put("C",3);
        map.put("D",4);
        map.put("E",1);
        map.put("F",2);
        map.put("G",3);
        map.put("H",4);
        map.put("I",1);
        map.put("K",2);
        map.put("L",3);
        map.put("M",4);
        map.put("N",1);
        map.put("O",2);
        map.put("P",3);
        map.put("Q",4);
        map.put("R",2);
        map.put("S",3);
        map.put("T",4);
        map.put("U",1);
        map.put("V",2);
        map.put("W",3);
        map.put("X",4);
        map.put("Y",3);
        map.put("Z",4);
        return value;
    }
}
