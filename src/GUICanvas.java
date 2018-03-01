
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * PaintComponent to repaint upon each change of field:shapes
 */
public class GUICanvas extends JComponent {
    private List<Shape> incrementalShapes;
    
    public GUICanvas() {
        setBorder(BorderFactory.createLineBorder(new Color(212,200,190), 10));
    }
    
    public void drawOnCanvas(List<Shape> shapes) {
        this.incrementalShapes = new ArrayList<>();
        int shapeIndex = 0;

        if (shapes != null) {
            if (!shapes.isEmpty()) {
                for(; shapeIndex < shapes.size(); shapeIndex++) {
                    incrementalShapes.add(shapes.get(shapeIndex));
                    paintImmediately(0, 0, 800, 700);
                    sleep(100);
                }
            }
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (incrementalShapes != null) {
            if (!incrementalShapes.isEmpty()) {
                for(Shape s : incrementalShapes)
                    g2d.draw(s);
            }
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 700);
    }

    /** Creates a pause between actions
     *  Code is from Victoria University's ecs100 package.
     * @param millis to sleep for
     */
    private void sleep(double millis) {
        long time = (long)(Math.max(0, Math.min(millis, 60000)));
        try { Thread.sleep(time); }
        catch (InterruptedException e) { }
    }
}
