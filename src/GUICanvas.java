
import java.awt.BasicStroke;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

/**
 * PaintComponent to repaint upon each change of field:shapes
 * Using the yeti-ui color scheme. Thank you for the lovely colours!
 */
public class GUICanvas extends JComponent {
    private List<Shape> incrementalShapes;

    private Color[] drawCols = {
            new Color(206,64,69),   // mahogany
            new Color(220,181,83),  // rob roy
            new Color(159,185,110), // olivine
            new Color(150,192,216), // regent st. blue
            new Color(160,116,196), // lilac bush
    };

    private int randomCol;
    
    public GUICanvas() {
        setBackground(new Color(236,234,232)); //Desert Storm
        setBorder(BorderFactory.createCompoundBorder(
                new EtchedBorder(new Color(212,200,190), new Color(212,200,190)),
                new LineBorder(new Color(212,200,190), 10)) //Swirl
        );
    }

    /** Calls repaint and the delay between them. Also does the main checking to see if the shapes passed in to draw are valid and exist.
     *  The method keeps track of a growing List which will have added to it the next shape from @param shapes. The exact next shape to add is
     *  kept track of by a shapeIndex.
     *
     *  This has the benefit of incremental drawing
     * @param shapes List of awt.Shapes to be drawn. Will be passed in through GUI via generation in HexTess
     */
    public void drawOnCanvas(List<Shape> shapes) {
        this.incrementalShapes = new ArrayList<>();
        int shapeIndex = 0;

        if (shapes != null) {
            if (!shapes.isEmpty()) {
                this.randomCol = (int)(Math.random() * drawCols.length); //choose random colour for this tessie drawing

                for(; shapeIndex < shapes.size(); shapeIndex++) {
                    incrementalShapes.add(shapes.get(shapeIndex)); //add the next shape
                    paintImmediately(0, 0, 800, 700); //force paint
                    sleep(100); // Delay
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
                g2d.setColor(drawCols[randomCol]); //set colour
                g2d.setStroke(new BasicStroke(3));

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
