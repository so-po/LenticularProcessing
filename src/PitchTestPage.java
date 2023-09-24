import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;


public class PitchTestPage implements ActionListener {

    JPanel panel;
    private JTextField lpiInputField;
    private JTextField dpiInputField;
    private JButton exportA4PitchTestButton;
    private JComboBox rangeComboBox;
    private JLabel exampleImage;


    public PitchTestPage(MainWindow mw) {
        rangeComboBox.addActionListener(this); exportA4PitchTestButton.addActionListener(this);
        lpiInputField.addActionListener(this); dpiInputField.addActionListener(this);

//
//        ImageIcon icon = new ImageIcon("src/PitchTestExample.png");
//        exampleImage.setIcon(icon);

        exportA4PitchTestButton.setEnabled(false);

    }


    /*
        vertical:
            -margins (2) are 5% of the paper
            -bars (11) are 5% of the paper
            -spacing between bars (10) are 3.5% of the paper
            5*11 + 3.5*10 + 2*5 = 100%
        horizontal:
            -margins (2) are 10%
            -bar is 90%

     */
    public BufferedImage createPitchTest(float lpi, float range, int dpi ) {

        final int paperWidth = (int)(dpi*8.27f); //pixels*inches
        final int paperHeight = (int)(dpi*11.69f);
        //^Note: These dimensions smaller than an actual A4 paper because
        // BufferedImages only accept int heights.

        BufferedImage pitchTest = new BufferedImage(paperWidth, paperHeight, BufferedImage.TYPE_INT_ARGB);
        //^An empty image.
        Graphics2D g2D = pitchTest.createGraphics();
        g2D.setColor(Color.BLACK);
        g2D.setFont(new Font("Arial", Font.PLAIN, dpi/5));

        final int vMargins = paperHeight/20; //vertical margins, ~5% of the paper
        final int hMargins = paperWidth/10; //horizontal margins, ~10% of the paper
        final float barSpacing = paperHeight*(3.5f/100); //spacing between bars, 3.5% of the paper
        final int barSize = (paperWidth/20); //size of the bar, ~5% of the paper

        float yPos = vMargins;
        float lineWidth;
        float currentLPI = lpi - range;
        while(currentLPI < lpi+range) {
            //Set the line width:
            lineWidth = LenticularImageProcessing.lineWidthPixels(currentLPI, dpi, 2);
            g2D.setStroke(new BasicStroke(lineWidth));

            //Write the currentLPI:
            g2D.drawString(String.valueOf(Math.ceil(currentLPI * 10) / 10), hMargins/2, (int)yPos);

            float prevYPos = yPos;
            while (yPos < (prevYPos+barSize)) { //draw lines until the bar's height is 5% of the image's height
                yPos += lineWidth*2;
                g2D.draw(new Line2D.Double(hMargins, yPos, paperWidth-hMargins, yPos));
            }
            yPos += barSpacing;
            currentLPI += range/5; //Increment the currentLPI

        }
        g2D.dispose();

        return pitchTest;

    }


    public JPanel getPanel() {

        return this.panel;
    }

    private void createUIComponents() {
        rangeComboBox = new JComboBox(new String[]{"5", "1", "0.5"});
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action Performed");

        int tempdpi = 0;
        float templpi = 0;
        float temprange = 0;

        try {
            tempdpi = Integer.parseInt(dpiInputField.getText());
            templpi = Float.parseFloat(lpiInputField.getText());
            temprange = Float.parseFloat((String)rangeComboBox.getSelectedItem());

        } catch (Exception Ignored) {}

        if(e.getSource() == exportA4PitchTestButton) {
            if (tempdpi < 100 || tempdpi > 1200) {
                JOptionPane.showMessageDialog(panel,
                        "For the DPI, please input a number within 0 and 1200.",
                        "Missing/Incorrect Settings",
                        JOptionPane.ERROR_MESSAGE);
                tempdpi = 0;
            }
            if (templpi > 200 || templpi < 20) {
                JOptionPane.showMessageDialog(panel,
                        "For the LPI, please input a number within 20 and 200.",
                        "Missing/Incorrect Settings",
                        JOptionPane.ERROR_MESSAGE);
                templpi = 0;
            }
            if (temprange <= 0) {
                JOptionPane.showMessageDialog(panel,
                        "Please enter a range.",
                        "Missing/Incorrect Settings",
                        JOptionPane.ERROR_MESSAGE);
                templpi = 0;
            }

            if(temprange != 0 && tempdpi != 0 && templpi != 0) {
                LenticularImageProcessing.exportImage(createPitchTest((float) templpi, temprange, tempdpi), panel);
                //float lpi, float range, int dpi
            }
        }


        //Enable the export button if all settings are filled out & all images are imported:
        if(temprange != 0 && tempdpi != 0 && templpi != 0){
            System.out.println("enabled!");
            exportA4PitchTestButton.setEnabled(true);
        } else {
            exportA4PitchTestButton.setEnabled(false);
        }


    }
}
