package pacman.client;

import static com.google.gwt.safehtml.shared.SafeHtmlUtils.fromTrustedString;
import static elemental2.dom.DomGlobal.clearInterval;
import static elemental2.dom.DomGlobal.document;
import static elemental2.dom.DomGlobal.setInterval;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.RootPanel;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLCanvasElement;
import javax.annotation.Nullable;
import jsinterop.base.Js;
import org.jboss.gwt.elemento.core.Elements;
import org.jboss.gwt.elemento.core.EventType;
import org.jboss.gwt.elemento.core.Key;
import pacman.shared.Drawer;
import pacman.shared.MainLoop;
import pacman.shared.Maze;

public class GwtGame implements EntryPoint {

    private MainLoop game;
    private @Nullable Double ticker;

    @Override public void onModuleLoad() {
        MenuItem menuScore = new MenuItem(fromTrustedString("Score: 0"));
        Drawer.Toaster score = s -> menuScore.setText("Score: " + s);
        Drawer.Toaster alert = DomGlobal::alert;

        //Create canvas
        HTMLCanvasElement canvas = Elements.canvas().get();
        canvas.width = 700; canvas.height = 700;

        EventType.bind(document, EventType.keydown, ev -> {
            switch (Key.fromEvent(ev)) {
                case ArrowUp: game.pacMan.nextDirection = Maze.UP; break;
                case ArrowDown: game.pacMan.nextDirection = Maze.DOWN; break;
                case ArrowLeft: game.pacMan.nextDirection = Maze.LEFT; break;
                case ArrowRight: game.pacMan.nextDirection = Maze.RIGHT; break;
                case Spacebar: playPause(game); break;
            }
        });
        EventType.bind(canvas, EventType.mouseover, ev -> canvas.focus());

        //Set drawer and menu bar
        Drawer drawer = new Drawer(new GwtCanvas(canvas), score, alert);
        Runnable restart = () -> restart(drawer);

        Command newGame = restart::run;
        Command pauseGame = () -> playPause(game);

        MenuBar bar = new MenuBar(true);
        bar.addItem("Nuevo", newGame);
        bar.addItem("Pausar", pauseGame);

        MenuBar rootBar = new MenuBar();
        rootBar.addItem("Juego", bar);
        rootBar.addItem(menuScore);

        RootPanel body = RootPanel.get();
        body.add(rootBar);

        HTMLPanel canvasContainer = new HTMLPanel("");
        canvasContainer.addStyleName("canvasContainer");
        canvasContainer.getElement().appendChild(Js.cast(canvas));
        body.add(canvasContainer);

        canvas.focus();
        restart.run();
    }

    public void restart(Drawer drawer) {
        if (game != null) {
            game.endGame();
            stop();
        }
        game = new MainLoop(drawer, 1);
        playPause(game); // start ticker
    }

    public void playPause(MainLoop game) {
        if (ticker == null) {  // play
            ticker = setInterval(args -> {
                if (ticker == null || game.gameEnded) {
                    stop(); return;
                }
                game.tick();
            }, game.delta);
        } else {  // pause
            stop();
        }
    }

    private void stop() {
        if (ticker == null) return;
        clearInterval(ticker);
        ticker = null;
    }
}
