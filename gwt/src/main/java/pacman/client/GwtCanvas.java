package pacman.client;

import static java.lang.Math.toRadians;

import elemental2.dom.CanvasRenderingContext2D;
import elemental2.dom.CanvasRenderingContext2D.FillStyleUnionType;
import elemental2.dom.HTMLCanvasElement;
import jsinterop.base.Js;
import pacman.shared.Canvas;

public class GwtCanvas implements Canvas {

    private HTMLCanvasElement canvas;
    private CanvasRenderingContext2D c2d;

    public GwtCanvas(HTMLCanvasElement canvas) {
        this.canvas = canvas;
        this.c2d = Js.cast(canvas.getContext("2d"));
    }

    @Override public double getWidth() {
        return canvas.width;
    }

    @Override public double getHeight() {
        return canvas.height;
    }

    @Override public void setFill(String color) {
        c2d.fillStyle = FillStyleUnionType.of(color);
    }

    @Override public void fillRect(double x, double y, double w, double h) {
        c2d.fillRect(x, y, w, h);
    }

    @Override public void fillPolygon(double[] xPoints, double[] yPoints, int nPoints) {
        c2d.beginPath();
        c2d.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i <= nPoints; i++) {
            c2d.lineTo(xPoints[i], yPoints[i]);
        }
        c2d.closePath();
        c2d.fill();
    }

    @Override public void fillOval(double x, double y, double w, double h) {
        c2d.beginPath();
        c2d.arc(x + w / 2., y + h / 2., w / 2., 0, Math.PI * 2);
        c2d.closePath();
        c2d.fill();
    }

    @Override public void fillArc(double x, double y, double w, double h, double startAngle, double arcExtent) {
        c2d.beginPath();
        c2d.arc(x + w / 2., y + h / 2., w / 2., -toRadians(startAngle), -toRadians(startAngle + arcExtent), true);
        c2d.lineTo(x + w / 2., y + h / 2.);
        c2d.closePath();
        c2d.fill();
    }
}
