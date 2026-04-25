import Tile.Tile;
import Tile.Mine;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GridTest {

    @Test
    public void testGridCreation() {
        List<List<Tile>> tiles = new ArrayList<>();
        tiles.add(new ArrayList<>());
        tiles.add(new ArrayList<>());
        tiles.getFirst().add(new Tile());
        tiles.getFirst().add(new Mine());
        tiles.get(1).add(new Tile());
        tiles.get(1).add(new Tile());

        Grid grid = new Grid(tiles, 1);
        assertFalse(grid.allTilesCleared());
    }

    @Test
    public void testClickValid() {
        List<List<Tile>> tiles = new ArrayList<>();
        tiles.add(new ArrayList<>());
        tiles.add(new ArrayList<>());
        tiles.getFirst().add(new Tile());
        tiles.getFirst().add(new Mine());
        tiles.get(1).add(new Tile());
        tiles.get(1).add(new Tile());

        Grid grid = new Grid(tiles, 1);
        // adding bad innput
        assertFalse(grid.clickValid(-1, 0));
        assertFalse(grid.clickValid(3, 3));
        assertTrue(grid.clickValid(1, 1));
    }

    @Test
    public void testClick() {
        List<List<Tile>> tiles = new ArrayList<>();
        tiles.add(new ArrayList<>());
        tiles.add(new ArrayList<>());
        tiles.getFirst().add(new Tile());
        tiles.getFirst().add(new Mine());
        tiles.get(1).add(new Tile());
        tiles.get(1).add(new Tile());

        Grid grid = new Grid(tiles, 1);
        grid.click(0, 0);
        assertTrue(tiles.get(0).get(0).clicked());
    }

    @Test
    public void testFlag() {
        List<List<Tile>> tiles = new ArrayList<>();
        tiles.add(new ArrayList<>());
        tiles.add(new ArrayList<>());
        tiles.getFirst().add(new Tile());
        tiles.getFirst().add(new Mine());
        tiles.get(1).add(new Tile());
        tiles.get(1).add(new Tile());

        Grid grid = new Grid(tiles, 1);
        grid.flag(0, 0);
        assertTrue(tiles.get(0).get(0).flagged());
    }

    @Test
    public void testNeighborCountsAreInitialized() {
        List<List<Tile>> tiles = new ArrayList<>();
        tiles.add(new ArrayList<>());
        tiles.add(new ArrayList<>());
        tiles.get(0).add(new Tile());
        tiles.get(0).add(new Mine());
        tiles.get(1).add(new Tile());
        tiles.get(1).add(new Tile());

        Grid grid = new Grid(tiles, 1);

        assertEquals(1, grid.getTile(0, 0).getNumMinesSurrounding());
        assertEquals(1, grid.getTile(1, 0).getNumMinesSurrounding());
        assertEquals(1, grid.getTile(1, 1).getNumMinesSurrounding());
    }

    @Test
    public void testZeroTileClickRevealsConnectedArea() {
        List<List<Tile>> tiles = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            tiles.add(new ArrayList<>());
            for (int column = 0; column < 3; column++) {
                tiles.get(row).add(new Tile());
            }
        }
        tiles.get(0).set(0, new Mine());

        Grid grid = new Grid(tiles, 1);
        grid.click(2, 2);

        assertFalse(grid.getTile(0, 0).clicked());
        assertTrue(grid.getTile(0, 1).clicked());
        assertTrue(grid.getTile(1, 0).clicked());
        assertTrue(grid.getTile(1, 1).clicked());
        assertTrue(grid.getTile(2, 2).clicked());
        assertTrue(grid.allTilesCleared());
    }

    @Test
    public void testWinningClickSendsWinEvent() {
        List<List<Tile>> tiles = new ArrayList<>();
        tiles.add(new ArrayList<>());
        tiles.get(0).add(new Tile());
        tiles.get(0).add(new Mine());

        Grid grid = new Grid(tiles, 1);
        grid.click(0, 0);

        assertTrue(grid.getTile(0, 0).clicked());
        assertTrue(grid.allTilesCleared());
    }

    @Test
    public void testBombClickLocksBoard() {
        List<List<Tile>> tiles = new ArrayList<>();
        tiles.add(new ArrayList<>());
        tiles.add(new ArrayList<>());
        tiles.get(0).add(new Mine());
        tiles.get(0).add(new Mine());
        tiles.get(1).add(new Tile());
        tiles.get(1).add(new Tile());

        Grid grid = new Grid(tiles, 2);

        grid.click(0, 0);
        grid.flag(1, 0);

        assertTrue(grid.getTile(0, 0).clicked());
        assertFalse(grid.getTile(1, 0).flagged());
    }
}
