package UI;

import Input.Input;
import Observe.GridObserver;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class GameWindow extends JFrame implements GridObserver {
    private static final int WINDOW_WIDTH = 640;
    private static final int WINDOW_HEIGHT = 480;
    private static final int TOP_BAR_HEIGHT = 50;
    private static final int GRID_PADDING = 0;

    private final Input input;
    private final JButton[][] tileButtons;
    private final ImageIcon tilePressedIcon;
    private final ImageIcon tileNotPressedIcon;
    private final ImageIcon tileFlaggedIcon;
    private final ImageIcon tileBombIcon;
    private final Font tileFont;
    private final JLabel statusLabel;
    private boolean gameOver = false;

    public GameWindow(Object grid, Input input) {
        super("Minesweeper");
        int rows = getGridDimension(grid, "getRowCount");
        int columns = getGridDimension(grid, "getColumnCount");
        this.input = input;
        this.tileButtons = new JButton[rows][columns];
        int tileSize = calculateTileSize(rows, columns);
        this.tilePressedIcon = loadScaledIcon("Images/TIlePressed.png", tileSize, tileSize);
        this.tileNotPressedIcon = loadScaledIcon("Images/TileNotPressed.png", tileSize, tileSize);
        this.tileFlaggedIcon = createOverlayIcon(tileNotPressedIcon, "Images/Flag.png", tileSize, tileSize);
        this.tileBombIcon = createOverlayIcon(tilePressedIcon, "Images/Mine.png", tileSize, tileSize);
        this.tileFont = new Font("SansSerif", Font.BOLD, Math.max(12, tileSize - 6));
        this.statusLabel = new JLabel("", JLabel.LEFT);
        this.statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        this.statusLabel.setForeground(new Color(180, 0, 0));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(WINDOW_WIDTH, TOP_BAR_HEIGHT));
        topBar.setBackground(new Color(210, 210, 210));
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        topBar.add(statusLabel, BorderLayout.WEST);
        topBar.add(createRestartButton(), BorderLayout.CENTER);

        JPanel gridPanel = new JPanel(new GridLayout(rows, columns, 0, 0));
        gridPanel.setBackground(new Color(235, 235, 235));
        gridPanel.setBorder(BorderFactory.createEmptyBorder());
        gridPanel.setPreferredSize(new Dimension(columns * tileSize, rows * tileSize));
        addGridButtons(gridPanel, rows, columns, tileSize);

        JPanel gridWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        gridWrapper.setBackground(new Color(235, 235, 235));
        gridWrapper.add(gridPanel);

        add(topBar, BorderLayout.NORTH);
        add(gridWrapper, BorderLayout.CENTER);

        pack();
        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
    }

    private JButton createRestartButton() {
        File imageFile = new File("Images/Restart.jpg");
        if (!imageFile.exists()) {
            return new JButton("Restart");
        }

        ImageIcon restartIcon = new ImageIcon(imageFile.getPath());
        Image scaledImage = restartIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        JButton restartButton = new JButton(new ImageIcon(scaledImage));
        restartButton.setFocusPainted(false);
        restartButton.setBorder(BorderFactory.createEmptyBorder());
        restartButton.setContentAreaFilled(false);
        restartButton.addActionListener(e -> {
            if (input != null) {
                input.restartTriggered();
            }
        });
        return restartButton;
    }

    private void addGridButtons(JPanel gridPanel, int rows, int columns, int tileSize) {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                JButton tileButton = new JButton();
                int currentRow = row;
                int currentColumn = column;
                configureTileButton(tileButton, tileSize);
                if (tileNotPressedIcon != null) {
                    tileButton.setIcon(tileNotPressedIcon);
                }
                tileButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (input == null || gameOver) {
                            return;
                        }

                        if (e.getButton() == MouseEvent.BUTTON1) {
                            input.tileTriggered(currentRow, currentColumn);
                        } else if (e.getButton() == MouseEvent.BUTTON3) {
                            input.tileFlagged(currentRow, currentColumn);
                        }
                    }
                });
                tileButtons[row][column] = tileButton;
                gridPanel.add(tileButton);
            }
        }
    }

    private void configureTileButton(JButton tileButton, int tileSize) {
        Dimension tileDimension = new Dimension(tileSize, tileSize);
        tileButton.setFocusPainted(false);
        tileButton.setMargin(new Insets(0, 0, 0, 0));
        tileButton.setBorder(BorderFactory.createEmptyBorder());
        tileButton.setBorderPainted(false);
        tileButton.setContentAreaFilled(false);
        tileButton.setHorizontalTextPosition(JButton.CENTER);
        tileButton.setVerticalTextPosition(JButton.CENTER);
        tileButton.setFont(tileFont);
        tileButton.setPreferredSize(tileDimension);
        tileButton.setMinimumSize(tileDimension);
        tileButton.setMaximumSize(tileDimension);
    }

    private int calculateTileSize(int rows, int columns) {
        int availableWidth = WINDOW_WIDTH - GRID_PADDING;
        int availableHeight = WINDOW_HEIGHT - TOP_BAR_HEIGHT - GRID_PADDING;
        int tileWidth = Math.max(1, availableWidth / columns);
        int tileHeight = Math.max(1, availableHeight / rows);
        return Math.min(tileWidth, tileHeight);
    }

    private int getGridDimension(Object grid, String methodName) {
        try {
            return (Integer) grid.getClass().getMethod(methodName).invoke(grid);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Unable to read grid dimensions", e);
        }
    }

    private ImageIcon loadScaledIcon(String path, int width, int height) {
        File imageFile = new File(path);
        if (!imageFile.exists()) {
            return null;
        }

        ImageIcon imageIcon = new ImageIcon(imageFile.getPath());
        Image scaledImage = imageIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private ImageIcon createOverlayIcon(ImageIcon baseIcon, String overlayPath, int width, int height) {
        if (baseIcon == null) {
            return null;
        }

        File overlayFile = new File(overlayPath);
        if (!overlayFile.exists()) {
            return baseIcon;
        }

        BufferedImage combinedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = combinedImage.createGraphics();
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.drawImage(baseIcon.getImage(), 0, 0, width, height, null);

        ImageIcon overlayIcon = new ImageIcon(overlayFile.getPath());
        graphics.drawImage(overlayIcon.getImage(), 0, 0, width, height, null);
        graphics.dispose();

        return new ImageIcon(combinedImage);
    }

    public void launch() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    public void update(String event, int x, int y) {
        switch (event) {
            case "bomb_click":
                setTileAppearance(x, y, tileBombIcon, "", null);
                gameOver = true;
                statusLabel.setText("You lose");
                break;
            case "show_0":
                setTileAppearance(x, y, tilePressedIcon, "", null);
                break;
            case "show_1":
                setTileAppearance(x, y, tilePressedIcon, "1", new Color(37, 99, 235));
                break;
            case "show_2":
                setTileAppearance(x, y, tilePressedIcon, "2", new Color(22, 163, 74));
                break;
            case "show_3":
                setTileAppearance(x, y, tilePressedIcon, "3", new Color(220, 38, 38));
                break;
            case "show_4":
                setTileAppearance(x, y, tilePressedIcon, "4", new Color(30, 64, 175));
                break;
            case "show_5":
                setTileAppearance(x, y, tilePressedIcon, "5", new Color(127, 29, 29));
                break;
            case "show_6":
                setTileAppearance(x, y, tilePressedIcon, "6", new Color(8, 145, 178));
                break;
            case "show_7":
                setTileAppearance(x, y, tilePressedIcon, "7", Color.BLACK);
                break;
            case "show_8":
                setTileAppearance(x, y, tilePressedIcon, "8", Color.GRAY);
                break;
            case "flag":
                setTileAppearance(x, y, tileFlaggedIcon, "", null);
                break;
            case "remove_flag":
                setTileAppearance(x, y, tileNotPressedIcon, "", null);
                break;
            default:
                break;
        }
    }

    private void setTileAppearance(int row, int column, ImageIcon icon, String text, Color textColor) {
        if (row < 0 || column < 0 || row >= tileButtons.length || column >= tileButtons[row].length) {
            return;
        }

        JButton tileButton = tileButtons[row][column];
        tileButton.setIcon(icon);
        tileButton.setText(text);
        tileButton.setForeground(textColor == null ? Color.BLACK : textColor);
    }

    public void resetBoard() {
        gameOver = false;
        statusLabel.setText("");
        for (int row = 0; row < tileButtons.length; row++) {
            for (int column = 0; column < tileButtons[row].length; column++) {
                setTileAppearance(row, column, tileNotPressedIcon, "", null);
            }
        }
    }
}
