/*
    Class for human player
    Has three parameters String, boolean and int, returns the name of user, turn of player and score respectively
 */

public class Player {
    private String name;
    private Boolean isTurn;
    private int score;

    public Player(){};
    public Player (boolean bool){
        this.isTurn = bool;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getTurn() {
        return isTurn;
    }

    public void setTurn(Boolean turn) {
        isTurn = turn;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
