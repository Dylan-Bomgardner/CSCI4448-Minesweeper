import Input.Input;
import Observe.InputObserver;
import UI.GameWindow;

public class Minesweeper implements InputObserver {
    private final int rows;
    private final int columns;
    private final int mineCount;
    private Grid grid;
    private Boolean quit = false;
    private Boolean game_over = false;
    private Boolean win = false;
    private Input input;
    private GameWindow ui;

    public Minesweeper(int rows, int columns, int mineCount, Grid grid, Input input, GameWindow ui) {
        this.rows = rows;
        this.columns = columns;
        this.mineCount = mineCount;
        this.grid = grid;
        this.input = input;
        this.ui = ui;
    }

    public void play() {
        ui.launch();
    }

    public static void main(String[] args) {
        int rows = 5;
        int columns = 5;
        int mineCount = 3;

        Grid grid = new Grid.Builder()
                .setDimensions(rows, columns)
                .addMines(mineCount)
                .build();
        Input input = new Input();
        GameWindow ui = new GameWindow(grid, input);
        Minesweeper game = new Minesweeper(rows, columns, mineCount, grid, input, ui);
        grid.subscribe(ui);
        input.subscribe(grid);
        input.subscribe(game);
        game.play();
    }

    @Override
    public void update(String event, int x, int y) {
        if (event.equals("restart")) {
            resetGame();
        }
    }

    private void resetGame() {
        input.unsubscribe(grid);
        grid.unsubscribe(ui);

        grid = new Grid.Builder()
                .setDimensions(rows, columns)
                .addMines(mineCount)
                .build();

        grid.subscribe(ui);
        input.subscribe(grid);
        ui.resetBoard();
        game_over = false;
        win = false;
    }
}
