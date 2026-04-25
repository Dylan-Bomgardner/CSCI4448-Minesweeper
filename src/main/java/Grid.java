import Observe.GridObserver;
import Observe.GridSubject;
import Observe.InputObserver;
import Observe.Observer;
import Tile.Tile;
import Tile.Mine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid implements GridSubject, InputObserver {
    private List<List<Tile>> tiles;
    private List<GridObserver> observers;
    private int numMines;
    private int tilesCleared = 0;
    private int numTiles;
    private boolean gameOver = false;

    @Override
    public void update(String event, int x, int y) {
        switch (event) {
            case "tile_triggered":
                click(x, y);
                break;
            case "tile_flagged":
                flag(x, y);
                break;
            case "restart":
                break;
            default:
                break;
        }
    }

    public Grid(List<List<Tile>> tiles, int numMines) {
        this.tiles = tiles;
        this.numMines = numMines;
        this.numTiles = tiles.size() * tiles.getFirst().size();
        this.observers = new ArrayList<>();
        initializeNeighborCounts();
    }

    private Grid(Builder builder) {
        this.tiles = builder.tiles;
        this.numMines = builder.numMines;
        this.numTiles = tiles.size() * tiles.getFirst().size();
        this.observers = new ArrayList<>();
        initializeNeighborCounts();
    }

    public Boolean allTilesCleared() { return (numMines + tilesCleared == numTiles); }

    public Boolean clickValid(int x, int y) {
        return x >= 0 && y >= 0 && tiles.size() > x && tiles.getFirst().size() > y;
    }

    public void click(int x, int y) {
        if (gameOver || !clickValid(x, y)) {
            return;
        }

        Tile clickedTile = tiles.get(x).get(y);

        if (clickedTile.flagged() || clickedTile.clicked()) {
            return;
        }
        if (clickedTile.isMine()) {
            clickedTile.click();
            gameOver = true;
            notifyObservers("bomb_click", x, y);
            revealRemainingMines(x, y);
            return;
        }

        revealConnectedTiles(x, y);

        if (allTilesCleared()) {
            gameOver = true;
            notifyObservers("win", -1, -1);
        }
    }

    public void flag(int x, int y) {
        if (gameOver || !clickValid(x, y)) {
            return;
        }
        Tile flaggedTile = tiles.get(x).get(y);
        if (flaggedTile.clicked()) {
            return;
        }
        flaggedTile.flag();
        String event = flaggedTile.flagged() ? "flag" : "remove_flag";
        notifyObservers(event, x, y);
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

    public Tile getTile(int x, int y) {
        if (!clickValid(x, y)) {
            throw new IndexOutOfBoundsException("Invalid Coords");
        }
        return tiles.get(x).get(y);
    }

    public int getRowCount() {
        return tiles.size();
    }

    public int getColumnCount() {
        return tiles.isEmpty() ? 0 : tiles.getFirst().size();
    }

    private void initializeNeighborCounts() {
        for (int row = 0; row < tiles.size(); row++) {
            for (int column = 0; column < tiles.getFirst().size(); column++) {
                Tile tile = tiles.get(row).get(column);
                if (!tile.isMine()) {
                    tile.setNumMinesSurrounding(countNeighboringMines(row, column));
                }
            }
        }
    }

    private int countNeighboringMines(int row, int column) {
        int count = 0;
        // want  to view every tile around the current one, which is why we skip (0, 0) as well
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                if (rowOffset == 0 && columnOffset == 0) {
                    continue;
                }

                int neighborRow = row + rowOffset;
                int neighborColumn = column + columnOffset;

                if (clickValid(neighborRow, neighborColumn) && tiles.get(neighborRow).get(neighborColumn).isMine()) {
                    count++;
                }
            }
        }
        return count;
    }

    private void revealConnectedTiles(int startRow, int startColumn) {
        List<int[]> tilesToReveal = new ArrayList<>();
        tilesToReveal.add(new int[]{startRow, startColumn});

        while (!tilesToReveal.isEmpty()) {
            int[] currentTileCoordinates = tilesToReveal.removeLast();
            int row = currentTileCoordinates[0];
            int column = currentTileCoordinates[1];

            if (!clickValid(row, column)) {
                continue;
            }

            Tile tile = tiles.get(row).get(column);
            if (tile.clicked() || tile.flagged() || tile.isMine()) {
                continue;
            }

            tile.click();
            tilesCleared++;
            notifyObservers("show_" + tile.getNumMinesSurrounding(), row, column);

            if (tile.getNumMinesSurrounding() != 0) {
                continue;
            }

            for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
                for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                    if (rowOffset == 0 && columnOffset == 0) {
                        continue;
                    }
                    int[] neighborCoordinates = {row + rowOffset, column + columnOffset};
                    tilesToReveal.add(neighborCoordinates);
                }
            }
        }
    }

    private void revealRemainingMines(int clickedRow, int clickedColumn) {
        for (int row = 0; row < tiles.size(); row++) {
            for (int column = 0; column < tiles.getFirst().size(); column++) {
                if (row == clickedRow && column == clickedColumn) {
                    continue;
                }

                Tile tile = tiles.get(row).get(column);
                if (tile.isMine()) {
                    notifyObservers("reveal_mine", row, column);
                }
            }
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
            if (tiles == null) {
                throw new IllegalStateException("tiles must be set before adding mines");
            }
            int rows = tiles.size();
            int cols = tiles.getFirst().size();

            if (numMines > rows * cols) {
                throw new IllegalArgumentException("Too many mines for grid size");
            }

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
            if (tiles == null) {
                throw new IllegalStateException("tiles must be set");
            }
            if (numMines < 0)  {
                throw new IllegalStateException("numMines must be >= 0");
            }
            return new Grid(this);
        }
    }
}
