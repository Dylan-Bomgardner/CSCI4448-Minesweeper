import Observe.GridObserver;
import Observe.GridSubject;
import Observe.Observer;
import Tile.Tile;

import java.util.List;

public class Grid implements GridSubject {
    private List<List<Tile>> tiles;
    private List<GridObserver> observers;

    public Grid(List<List<Tile>> tiles) {
        this.tiles = tiles;
    }

    private Boolean clickValid(int x, int y) {
        return tiles.size() < x || tiles.getFirst().size() < y;
    }

    public void click(int x, int y) {
        if (!clickValid(x, y)) return;

        Tile clickedTile = tiles.get(x).get(y);
        if (clickedTile.clicked()) return;

        clickedTile.click();
    }

    public void flag(int x, int y) {
        if (!clickValid(x, y)) return;

        Tile flaggedTile = tiles.get(x).get(y);
        flaggedTile.flag();
    }

    @Override
    public void subscribe(GridObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unsubscribe(GridObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String event, int x, int y) {
        for (GridObserver observer : observers) {
            observer.update(event, x, y);
        }
    }
}
