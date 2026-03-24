package Tile;

import Observe.ObserveSubject;
import Observe.Observer;

import javax.security.auth.Subject;
import java.util.List;

public class Tile {
    List<Observer> observers;

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
}
