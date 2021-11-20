import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static javax.swing.JOptionPane.showInputDialog;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class WhiteBoardCanvas extends JPanel implements MouseListener {
    private DrawRemoteInterface remoteInterface;
    private Point start;
    private Point end;
    private Color color;
    private int option;
    private Client client;

    public WhiteBoardCanvas(DrawRemoteInterface remoteInterface, Client clent){
        this.remoteInterface = remoteInterface;
        this.option = 0;
        this.color = Color.BLACK;
        this.client = clent;
        addMouseListener(this);
        this.setBackground(Color.white);
    }

    public void setOption(int option){
        this.option = option;
    }

    public void setColor(Color color){
        this.color = color;
    }

    public void reset() {
        this.start = null;
        this.end = null;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            Image img = remoteInterface.getCanvas().getImage();
            g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
            this.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Manager has closed the canvas, all connection"
                    + "will be refused. Please close the program");
            System.exit(-1);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (isLeftMouseButton(e)) {
            start = e.getPoint();
        } else if (isRightMouseButton(e)) {
            reset();
        } else {
            System.out.println("?");
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isLeftMouseButton(e)) {
            end = e.getPoint();
        } else if (isRightMouseButton(e)) {
            reset();
            return;
        } else {
            System.out.println("?");
        }

        try {
            switch (option) {
                case GUIConstants.LINE -> remoteInterface.drawLine(start.x, start.y, end.x, end.y, color);
                case GUIConstants.RECTANGLE -> {
                    int x = min(start.x, end.x);
                    int y = min(start.y, end.y);
                    int width = abs(start.x - end.x);
                    int height = abs(start.y - end.y);
                    remoteInterface.drawRectangle(x, y, width, height, color);
                }
                case GUIConstants.CIRCLE -> {
                    int x = min(start.x, end.x);
                    int y = min(start.y, end.y);
                    int width = abs(start.x - end.x);
                    int height = abs(start.y - end.y);
                    int radius = (int) (Math.sqrt(width*width + height*height)/2);
                    remoteInterface.drawCircle(x, y, radius, color);
                }
                case GUIConstants.OVAL -> {
                    int x = min(start.x, end.x);
                    int y = min(start.y, end.y);
                    int width = abs(start.x - end.x);
                    int height = abs(start.y - end.y);
                    remoteInterface.drawOval(x, y, width, height, color);
                }
                case GUIConstants.TEXT -> {
                    String text = showInputDialog("Enter a message:");
                    if (text != null)
                        remoteInterface.drawText(text, end.x, end.y, color);
                }
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(this, "Cannot connect to server");
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
