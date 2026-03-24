package Tile;

public class Tile {
    private int numMinesSurrounding = 0;

    private Boolean clicked = false;
    private Boolean flagged = false;
    public Tile() {}

    public void click() {
        clicked = true;
    }

    public void flag() {
        flagged = !flagged;
    }

    public Boolean clicked() { return clicked; }

    public Boolean flagged() { return flagged; }

    public Boolean isMine() { return false; }

    public void setNumMinesSurrounding(int numMines) { this.numMinesSurrounding = numMines; }
    public int getNumMinesSurrounding() { return numMinesSurrounding; }
}
