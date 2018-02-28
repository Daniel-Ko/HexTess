import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

/**
 * Created by Dan Ko on 9/12/2017.
 */
public class HexTess {
    private double offset;
    private double SIZE = 0;
    private double sixth;
    //private List<Shape> shapes = new ArrayList<Shape>();
    private List<Shape> shapes;
    //fields used in initTessie and its submethods
    private double[] xC;
    private double[] yC;
    private double[] hexX;
    private double[] hexY;
    private double[] hexBotX = new double[6];
    private double[] hexBotY = new double[6];
    
    public HexTess(double offset) {
        this.offset = offset;
    }

    public void setSize(String size) throws NumberFormatException, IllegalFormatException{
        try {
            int tessSize = Integer.parseInt(size);
            if(tessSize > 300 || tessSize < 0)
                throw new IllegalArgumentException("Choose a size (0-20)");
            SIZE = tessSize;

        } catch(NumberFormatException e) {
            throw new NumberFormatException("Not a number");
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
    /** Asks for a size to scale the tessellation and prepares data structures to evalShapes
     */
    public void initialise(String size) throws IllegalFormatException{
        try {
            setSize(size);
        } catch(IllegalFormatException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        shapes = new ArrayList<>();
        sixth = SIZE / 6.0;
    }

    public List<Shape> evalShapes() {
        initTessie(offset, 0, false, Color.red);
        initTessie(offset, SIZE, true, Color.blue);

        return this.shapes;
    }

    /** Initialises x and y position arrays with initial values based on whether we are drawing top or bottom of tessellation.
     *   Once everything is prepared, it fires off 5 shapes to run through the algorithm in recordPath
     */
    private void initTessie(double x, double y, boolean bot, Color color) {

        /** arrays to record coordinates **/

        // x will always be the same as we traverse from left to right
        double[] xCoordsInitial = { x, x + sixth, x, x };
        // setting y as if we were traversing top
        double[] yCoordsInitial = { y, y + (2.0 * sixth), y + (2 * sixth), y + (2 * sixth) };

        if (bot)
            yCoordsInitial = flipper(yCoordsInitial, y); // flipping y values to the bottom side

        xC = xCoordsInitial;
        yC = yCoordsInitial;

        /** hexagonal points **/ // we will be collecting x and y points from triangles

        hexX = new double[6];
        hexY = new double[6];

        for (int i = 0; i < 5; i++) { // we evalShapes 5 tesselating shapes from left to right on both the top and bottom.
            recordPath(i, bot);
        }
    }

    /** left and right sides have an un-tilted square. Rest are triangles and tilted squares
     *   Accounting for we're drawing top or bottom, we find a path through all the vertices, recording shape outlines as we go and
     *   making use of duplicate vertices for the next shape.
     *   Each if case is a single shape!
     */
    private void recordPath(int shapeNum, boolean isBot) {

        if (shapeNum == 0) { //left triangle + left square (latter only draws on top side iteration)
            Path2D.Double triLeft = new Path2D.Double();
            addShape(triLeft);

            if (!isBot)
                shapes.add(new Rectangle2D.Double(xC[2], yC[2], sixth, 2.0 * sixth)); // leftmost rect draws once

            hexdex(0, true, isBot, xC[1]); // hexagon coordinates are updated
            hexdex(0, false, isBot, yC[1]);

        } else if (shapeNum == 1) { //tilted left square
            xC[3] = xC[2] + 2 * sixth;
            yC[3] = yC[0];
            xC[2] = xC[1] + 2 * sixth;

            if (!isBot)
                yC[2] = yC[1] - sixth; // drawing for top/bot slightly different for this point only. See shapeNum==3 case.
            else
                yC[2] = yC[1] + sixth;

            Path2D.Double tiltedSqLeft = new Path2D.Double();
            addShape(tiltedSqLeft);

        } else if (shapeNum == 2) { //middle inverted triangle
            xC[0] = xC[3] + 2 * sixth;
            yC[0] = yC[3];

            triPrep(xC); // each triangle drawn only effectively draws 3 points, with the 4th point here being set as the 3rd
            triPrep(yC); // This assignment is also seen in step 4 and implied in step 0

            Path2D.Double midTri = new Path2D.Double();
            addShape(midTri);

            hexdex(1, true, isBot, xC[1]);  //update coordinates again
            hexdex(1, false, isBot, yC[1]);

        } else if (shapeNum == 3) { //tilted right square
            xC[3] = xC[0] + 2 * sixth;
            yC[3] = yC[0];
            xC[2] = xC[1] + 2 * sixth;

            if (!isBot)
                yC[2] = yC[1] + sixth;
            else
                yC[2] = yC[1] - sixth;

            Path2D.Double tiltedSqRight = new Path2D.Double();
            addShape(tiltedSqRight);

        } else if (shapeNum == 4) { //right triangle + right rectangle (latter only draws on top side iteration)
            xC[0] = xC[3];
            yC[0] = yC[2];
            triPrep(xC);
            triPrep(yC);
            Path2D.Double rightTri = new Path2D.Double();
            addShape(rightTri);

            if (!isBot)
                shapes.add(new Rectangle2D.Double(xC[1], yC[1], sixth, 2.0 * sixth)); // rightmost rect draws once
            hexdex(2, true, isBot, xC[1]);
            hexdex(2, false, isBot, yC[1]);
        }
    }

    private void addShape(Path2D.Double path) {
        path.moveTo(xC[0], yC[0]);
        
        for(int i = 0; i < xC.length; i++) {
            path.lineTo(xC[i], yC[i]);
        }
        path.closePath();

        shapes.add(path);
    }

    /** This method changes each default (top) value into its bottom counterpart
     */
    private double[] flipper(double[] mirror, double y) {
        for (int b = 0; b < 4; b++)
            mirror[b] = (2 * y ) - mirror[b]; // this formula flips the y coordinate. X remains same.

        return mirror;
    }

    /** The algorithm is considering each shape to have 4 vertices as it is the largest-vertexed shape we evalShapes. When
     *   encountering triangles, we record the "4th point" to be the same as its 3rd point. Therefore this method will
     *   only be called in cases where we record triangles
     */
    private double[] triPrep(double[] recordThreePoints) { // 2->1, 3->2 for triangle coordinates only
        for (int t = 1; t < 3; t++)
            recordThreePoints[t] = recordThreePoints[t + 1];

        return recordThreePoints;
    }

    /** To record the outline of the hexagon, we record its vertices whenever a shape shares a corner with it
     */
    private void hexdex(int index, boolean x, boolean isBot, double val) {
        if (!isBot) {
            if (x)
                hexX[index] = val;
            else
                hexY[index] = val;
        } else {
            index = 5 - index; //get reverse values
            if (x)
                hexBotX[index] = val;
            else
                hexBotY[index] = val;
        }
    }
}
