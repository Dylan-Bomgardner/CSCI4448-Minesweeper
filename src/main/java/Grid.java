import Observe.GridObserver;
import Observe.GridSubject;
import Observe.Observer;
import Tile.Tile;
import Tile.Mine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid implements GridSubject {
    private List<List<Tile>> tiles;
    private List<GridObserver> observers;
    private int numMines;
    private int tilesCleared = 0;
    private int numTiles;

    public Grid(List<List<Tile>> tiles, int numMines) {
        this.tiles = tiles;
        this.numMines = numMines;
        this.numTiles = tiles.size() * tiles.getFirst().size();
        this.observers = new ArrayList<>();
    }

    private Grid(Builder builder) {
        this.tiles = builder.tiles;
        this.numMines = builder.numMines;
        this.numTiles = tiles.size() * tiles.getFirst().size();
        this.observers = new ArrayList<>();
        // Todo set the number for the blank tiles
    }

    public Boolean allTilesCleared() { return (numMines + tilesCleared == numTiles); }

    public Boolean clickValid(int x, int y) {
        return tiles.size() > x && tiles.getFirst().size() > y;
    }

    public void click(int x, int y) {
        if (!clickValid(x, y)) return;

        Tile clickedTile = tiles.get(x).get(y);

        if (clickedTile.flagged()) return;
        if (clickedTile.clicked()) return;

        clickedTile.click();

        String event;
        if (clickedTile.isMine()) {
            event = "bomb_click";
        } else {
            event = "blank_click";
            tilesCleared++;
        }
        notifyObservers(event, x, y);
    }

    public void flag(int x, int y) {
        if (!clickValid(x, y)) return;

        Tile flaggedTile = tiles.get(x).get(y);
        flaggedTile.flag();

        notifyObservers("flag", x, y);
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

    public static class Builder {
        private List<List<Tile>> tiles;
        private int numMines;
        Random random = new Random();

        public Builder setTiles(List<List<Tile>> tiles) {
            this.tiles = tiles;
            return this;
        }

        public Builder setDimensions(int rows, int cols) {
            this.tiles = new ArrayList<>();
            for (int r = 0; r < rows; r++) {
                List<Tile> row = new ArrayList<>();
                for (int c = 0; c < cols; c++) {
                    row.add(new Tile());
                }
                tiles.add(row);
            }
            return this;
        }

        public Builder addMines(int numMines) {
            if (tiles == null) throw new IllegalStateException("tiles must be set before adding mines");
            int rows = tiles.size();
            int cols = tiles.getFirst().size();

            if (numMines > rows * cols) throw new IllegalArgumentException("Too many mines for grid size");

            this.numMines = numMines;
            int minesPlaced = 0;

            while (minesPlaced < numMines) {
                int row = random.nextInt(rows);
                int column = random.nextInt(cols);

                if (!tiles.get(row).get(column).isMine()) {
                    tiles.get(row).set(column, new Mine());
                    minesPlaced++;
                }
            }
            return this;
        }

        public Grid build() {
            if (tiles == null) throw new IllegalStateException("tiles must be set");
            if (numMines < 0)  throw new IllegalStateException("numMines must be >= 0");
            return new Grid(this);
        }
    }
}
