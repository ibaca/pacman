package pacman.client;

import static javafx.application.Platform.runLater;
import static javafx.scene.control.Alert.AlertType.INFORMATION;

import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pacman.shared.Drawer;
import pacman.shared.MainLoop;
import pacman.shared.Maze;

public class JfxGame extends Application {
    private final int WINDOW_WIDTH = 700;
    private final int WINDOW_HEIGHT = 800;

    private MainLoop game;
    private TimerTask ticker;
    private Timer thread = new Timer("game-loop", true);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        MenuBar menuBar = new MenuBar();
        Menu menuGame = new Menu("Game");
        MenuItem newGame = new MenuItem("New");
        MenuItem pauseGame = new MenuItem("Pause");
        MenuItem exitGame = new MenuItem("Exit");
        menuGame.getItems().addAll(newGame, pauseGame, exitGame);
        menuBar.getMenus().add(menuGame);

        Menu menuScore = new Menu("Score: 0");
        menuBar.getMenus().add(menuScore);

        Drawer.Toaster score = s -> runLater(() -> menuScore.setText("Score: " + s));
        Drawer.Toaster alert = text -> runLater(() -> {
            Alert a = new Alert(INFORMATION);
            a.setTitle("PAC-MAN");
            a.setHeaderText(null);
            a.setContentText(text);
            a.showAndWait();
        });

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        Drawer drawer = new Drawer(new JfxCanvas(canvas), score, alert);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuBar);
        vBox.getChildren().add(canvas);

        Runnable restart = () -> restart(drawer);
        newGame.setOnAction(actionEvent -> restart.run());
        pauseGame.setOnAction(actionEvent -> playPause(JfxGame.this.game));
        exitGame.setOnAction(actionEvent -> System.exit(0));

        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> canvas.requestFocus());
        canvas.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
            switch (keyEvent.getCode()) {
                case UP: game.pacMan.nextDirection = Maze.UP; break;
                case DOWN: game.pacMan.nextDirection = Maze.DOWN; break;
                case LEFT: game.pacMan.nextDirection = Maze.LEFT; break;
                case RIGHT: game.pacMan.nextDirection = Maze.RIGHT; break;
                case SPACE: playPause(JfxGame.this.game); break;
            }
        });

        Scene scene = new Scene(vBox, WINDOW_WIDTH, WINDOW_HEIGHT);
        stage.setTitle("PAC-MAN");
        stage.setScene(scene);
        stage.show();

        restart.run();
        canvas.requestFocus();
    }

    public void restart(Drawer drawer) {
        if (game != null) {
            game.endGame();
            ticker = null;
        }
        game = new MainLoop(drawer, 1);
        playPause(JfxGame.this.game); // start ticker
    }

    public void playPause(MainLoop game) {
        if (ticker == null) {  // play
            ticker = new TimerTask() {
                @Override public void run() {
                    if (game.gameEnded) cancel();
                    else game.tick();
                }
            };
            thread.scheduleAtFixedRate(ticker, 0, game.delta);
        } else {  // pause
            ticker.cancel();
            ticker = null;
        }
    }
}
