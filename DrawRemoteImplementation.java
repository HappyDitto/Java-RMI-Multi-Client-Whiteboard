import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DrawRemoteImplementation extends UnicastRemoteObject implements DrawRemoteInterface {

    private ImageIcon canvas;

    public DrawRemoteImplementation() throws RemoteException {
        canvas = new ImageIcon(new BufferedImage(GUIConstants.CANVAS_WIDTH, GUIConstants.CANVAS_HEIGHT,
                BufferedImage.TYPE_INT_ARGB));
    }

    @Override
    public ImageIcon getCanvas() throws RemoteException {
        return canvas;
    }

    @Override
    public synchronized void drawText(String text, int x, int y, Color color) throws RemoteException {
        Graphics2D g = (Graphics2D) canvas.getImage().getGraphics();
        g.setColor(color);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        g.drawString(text, x, y);
    }

    @Override
    public synchronized void drawRectangle(int x, int y, int width, int height, Color color) throws RemoteException {
        Graphics2D g = (Graphics2D) canvas.getImage().getGraphics();
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(color);
        g.drawRect(x, y, width, height);
    }

    @Override
    public synchronized void drawLine(int x1, int y1, int x2, int y2, Color color) throws RemoteException {
        Graphics2D g = (Graphics2D) canvas.getImage().getGraphics();
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public synchronized void drawCircle(int x, int y, int radius, Color color) throws RemoteException {
        Graphics2D g = (Graphics2D) canvas.getImage().getGraphics();
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(color);
        g.drawOval(x, y, radius, radius);
    }

    @Override
    public synchronized void drawOval(int x, int y, int width, int height, Color color) throws RemoteException {
        Graphics2D g = (Graphics2D) canvas.getImage().getGraphics();
        g.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(color);
        g.drawOval(x, y, width, height);
    }

    @Override
    public synchronized void clear() throws RemoteException {
        canvas = new ImageIcon(new BufferedImage(GUIConstants.CANVAS_WIDTH, GUIConstants.CANVAS_HEIGHT,
                BufferedImage.TYPE_INT_ARGB));
    }
}
