import Input.Input;
import Observe.InputObserver;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputTest {
    @Test
    public void testRegisterAndUnregister() {
        Input input = new Input();
        int[] updateCount = {0};
        InputObserver observer = (event, x, y) -> updateCount[0]++;

        input.register(observer);
        input.notifyObservers("test", 1, 2);
        assertEquals(1, updateCount[0]);

        input.unregister(observer);
        input.notifyObservers("test", 3, 4);
        assertEquals(1, updateCount[0]);
    }

    @Test
    public void testRestartTriggered() {
        Input input = new Input();
        String[] lastEvent = {null};
        int[] lastX = {0};
        int[] lastY = {0};
        InputObserver observer = (event, x, y) -> {
            lastEvent[0] = event;
            lastX[0] = x;
            lastY[0] = y;
        };
        input.register(observer);

        input.restartTriggered();

        assertEquals("restart", lastEvent[0]);
        assertEquals(-1, lastX[0]);
        assertEquals(-1, lastY[0]);
    }

    @Test
    public void testTileTriggered() {
        Input input = new Input();
        String[] lastEvent = {null};
        int[] lastX = {0};
        int[] lastY = {0};
        InputObserver observer = (event, x, y) -> {
            lastEvent[0] = event;
            lastX[0] = x;
            lastY[0] = y;
        };
        input.register(observer);

        input.tileTriggered(2, 3);

        assertEquals("tile_triggered", lastEvent[0]);
        assertEquals(2, lastX[0]);
        assertEquals(3, lastY[0]);
    }

    @Test
    public void testTileFlagged() {
        Input input = new Input();
        String[] lastEvent = {null};
        int[] lastX = {0};
        int[] lastY = {0};
        InputObserver observer = (event, x, y) -> {
            lastEvent[0] = event;
            lastX[0] = x;
            lastY[0] = y;
        };
        input.register(observer);

        input.tileFlagged(4, 5);

        assertEquals("tile_flagged", lastEvent[0]);
        assertEquals(4, lastX[0]);
        assertEquals(5, lastY[0]);
    }
}
