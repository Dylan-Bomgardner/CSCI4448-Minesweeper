import Tile.Tile;
import Tile.Mine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TileTest {
    @Test
    public void testTileCreation() {
        Tile newTile = new Tile();
        assertFalse(newTile.isMine());
        assertFalse(newTile.flagged());
        assertFalse(newTile.clicked());
    }

    @Test
    public void testMine() {
        Tile newTile = new Mine();
        assertTrue(newTile.isMine());
    }
}
