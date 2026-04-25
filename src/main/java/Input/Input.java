package Input;

import Observe.InputObserver;
import Observe.InputSubject;

import java.util.ArrayList;
import java.util.List;

public class Input implements InputSubject {
    private final List<InputObserver> observers = new ArrayList<>();

    @Override
    public void subscribe(InputObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(InputObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, int x, int y) {
        for (InputObserver observer : observers) {
            observer.update(event, x, y);
        }
    }

    public void restartTriggered() {
        notifyObservers("restart", -1, -1);
    }

    public void tileTriggered(int row, int col) {
        notifyObservers("tile_triggered", row, col);
    }

    public void tileFlagged(int row, int col) {
        notifyObservers("tile_flagged", row, col);
    }
}
