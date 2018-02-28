import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.geom.Point2D;

public class GUI extends JComponent {

    private JFrame frame;
    private JPanel display;
    private JPanel tool;
    private GUICanvas drawing;
    private JTextField input;
    private HexTess hextess;
    
    
    
    public GUI() {
        frame = new JFrame();
        frame.getContentPane().add(this);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setFocusable(true);

        // set up the rest of the frame
        tool = new JPanel();
        display = new JPanel();
        display.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        addSizeInputBox();
    
        drawing = new GUICanvas();
        display.add(drawing);

        frame.add(tool, BorderLayout.PAGE_START);
        frame.add(display, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null); // window appears in center

        //determine what the max size is (the smaller of the two so drawing doesn't exceed borders)
        int maxSize = (int) display.getHeight() > (int) display.getWidth() ?
                (int) display.getWidth() :
                (int) display.getWidth();

        //pass in absolute centre for hextess to create relative offset (messy but until GUICanvas can transform points...)
        hextess = new HexTess(
                new Point2D.Double(
                        display.getWidth() / 2.0,
                        display.getHeight() / 2.0),
                maxSize
                );
    }

    private void addSizeInputBox() {
        input = new JTextField(5);
        input.setMaximumSize(new Dimension(0, 25));
    
        input.setToolTipText("Enter size (0-20)");
        input.setHorizontalAlignment(SwingConstants.CENTER);
    
        input.setBorder(BorderFactory.createBevelBorder(2));
    
        tool.add(new JLabel("Size: "));
        
        //on pressing enter, redraw hextess
        input.addActionListener(e -> {
            if(!isInputError())
                drawing.drawOnCanvas(hextess.evalShapes());
        });
        
        tool.add(input);
    }

    private boolean isInputError() {
        try {
            
            hextess.initialise(input.getText());
            return false;
            
        } catch(IllegalArgumentException exc) {
        
            //choose isInputError display title
            String title = "Out of bounds";
            if(exc instanceof NumberFormatException)
                title = "Invalid Input";
        
            JOptionPane.showMessageDialog(frame,
                    exc.getMessage(), title, JOptionPane.WARNING_MESSAGE);
            
            return true;
        }
    }


    public static void main(String[] s) {
        SwingUtilities.invokeLater(()-> new GUI());
    }

}
