import Observe.InputObserver;

public class Minesweeper implements InputObserver {
    private Grid grid;
    private Boolean quit = false;
    private Boolean game_over = false;
    private Boolean win = false;

    public Minesweeper(Grid grid) {
        this.grid = grid;
    }

    public void play() {
        while (!quit) {
            continue;
        }
    }

    @Override
    public void update(String event, int x, int y) {
        if (event.equals("bomb_click")) {
            win = false;
            game_over = true;
        } else if (event.equals("blank_click")) {
            // do nothing for now
            if (grid.allTilesCleared()) {
                win = true;
                game_over = true;
            }
        }
    }
}
