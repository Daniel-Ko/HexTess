import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI extends JComponent {

    private JFrame frame;
    private JPanel display;
    private JPanel tool;
    private JComponent drawing;
    private JTextField input;

    private HexTess hextess;



    protected void redraw(Graphics g) {
//        Graphics2D g2 = (Graphics2D) g;
//        if(!shapes.isEmpty() && shapes != null) {
//            for(Shape shape : shapes) {
//                g2.evalShapes(shape);
//            }
//        }
    }

    public GUI() {
        hextess = new HexTess();

        frame = new JFrame();
        frame = new JFrame();
        frame.getContentPane().add(this);
        frame.setVisible(true);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setPreferredSize(getPreferredSize());
        this.setFocusable(true);

        // set up the rest of the frame
        tool = new JPanel(); //BoxLayout(display, BoxLayout.LINE_AXIS)
        display = new JPanel();
        display.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        addSizeInputBox();
        addPainter();

        frame.add(tool, BorderLayout.PAGE_START);
        frame.add(display);
        frame.pack();
        frame.setLocationRelativeTo(null); // window appears in center
    }

    private void addSizeInputBox() {
        input = new JTextField(5);
        input.setMaximumSize(new Dimension(0, 25));
        input.addActionListener(new ActionListener() { //on pressing enter, give the hextess a new size
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        try {
                                            hextess.initialise(input.getText());
                                        } catch(IllegalArgumentException exc) {
                                            String title = "Out of bounds";
                                            if(exc instanceof NumberFormatException)
                                                title = "Invalid Input";

                                            JOptionPane.showMessageDialog(frame,
                                                exc.getMessage(), title, JOptionPane.WARNING_MESSAGE);
                                        }

                                        List<Shape> shapes = hextess.evalShapes();
                                        Graphics2D g2d = (Graphics2D) drawing.getGraphics();
                                        for(Shape s : shapes)
                                            g2d.draw(s);
                                    }
                                });
        input.setToolTipText("Enter size (0-20)");
        input.setHorizontalAlignment(SwingConstants.CENTER);

        input.setBorder(BorderFactory.createBevelBorder(2));

        tool.add(new JLabel("Size: "));
        tool.add(input);
    }

    private void addPainter() {
        drawing = new JComponent() {
            protected void paintComponent(Graphics g) {
                redraw(g);
            }
        };

        drawing.setPreferredSize(new Dimension(800,
                500));
        display.add(drawing);
    }




    public static void main(String[] s) {
        SwingUtilities.invokeLater(()-> new GUI());
    }

}
