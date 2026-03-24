import Tile.Tile;
import Tile.Mine;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
}
