# Minesweeper

Created by Henry Miller and Dylan Bomgardner

A simple Java application that imitates the game Minesweeper.


## Overview

This project is a desktop Minesweeper game with three main responsibilities:

- `Grid` manages board state, mine placement, reveal logic, flagging, and win/loss conditions.
- `GameWindow` renders the UI and converts mouse actions into input events.
- `Input` acts as an event source that decouples UI actions from game logic.

`Minesweeper` is the main method that you can use to run the game. It creates the objects, connects them together, and handles restart behavior.

## Requirements

- Java 25
- Gradle wrapper included in the repo

## Build and Run

Run the tests:

```bash
./gradlew test
```

Run the application from your own environment by just launching `Minesweeper.main`.

## Design Patterns Used

### Observer Pattern

- `Input` implements `InputSubject`
- `Grid` implements `GridSubject` and `InputObserver`
- `GameWindow` implements `GridObserver`
- `Minesweeper` implements `InputObserver`

This arrangement decouples:

- UI input from game logic
- game logic from rendering
- top-level game control from direct UI handling

### Builder Pattern

`Grid.Builder` is used to construct the board:

- `setDimensions(rows, cols)` creates the tile matrix
- `addMines(numMines)` places mines randomly
- `build()` returns the configured `Grid`

This is used both at startup and during restart.

### MVC

We use the Model-View Controller Pattern assisted by the Observers to separate logic.

- Model: `Grid`, `Tile`, `Mine`
- View: `GameWindow`
- Controller/orchestration: `Minesweeper`, `Input`

### Command Pattern

We tried using the command pattern in a similar fashion to Polymorphia, but it wasn't
working as well as we thought. We wanted to have commands for each tile such as blank,
number, mine, or flag, but the logic for each of these was too different and unlike Polymorphia,
there was no agent playing the game, so we scrapped what we had on the pattern.

## Testing

Current tests cover:

- tile and mine behavior
- input event dispatch
- grid creation
- click and flag behavior
- neighbor count initialization
- zero-tile flood reveal
- win detection
- observer registration behavior
- builder behavior

We had 100% test coverage across all methods that do not include the UI.

## Main Classes

- `Minesweeper.java`: startup, wiring, restart handling
- `Grid.java`: board logic and game state
- `Input/Input.java`: semantic input event dispatch
- `UI/GameWindow.java`: Swing rendering and mouse handling
- `Tile/Tile.java`: base tile type
- `Tile/Mine.java`: mine tile subtype
