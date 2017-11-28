import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.List;

/**
 * PaintComponent to repaint upon each change of field:shapes
 */
public class GUICanvas extends JComponent {
    List<Shape> shapes;
    
    public GUICanvas() {
        setBorder(BorderFactory.createBevelBorder(2));
    }
    
    public void drawOnCanvas(List<Shape> shapes) {
        this.shapes = shapes;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        if(shapes != null) {
            if(!shapes.isEmpty()) {
                for(int i = 0; i < HexTess.SIZE; i++) {
                    for(Shape s : shapes) {
                        g2d.translate((HexTess.SIZE * i / HexTess.SIZE), (i / HexTess.SIZE));
                        g2d.draw(s);
                    }
                }
            }
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 500);
    }
}
