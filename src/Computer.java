public class Computer {
    private Boolean isTurn;
    private int score;

    public Computer(){}
    public Computer (boolean bool){
        this.isTurn = bool;
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
