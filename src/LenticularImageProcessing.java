import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LenticularImageProcessing {
    static final JFileChooser fileChooser = new JFileChooser();

    public static float lineWidthPixels(float LPI, float dpi, int frames) {
        //Returns the necessary width of each line in pixels,
        // based on the LPI, printer DPI and the # of frames.
        return ((1/LPI)/frames)*dpi;
    }

    public static void exportImage(BufferedImage img, JPanel panel) {

        int response = fileChooser.showSaveDialog(panel); //<Shows dialog to save files
        if (response == JFileChooser.APPROVE_OPTION) { //If a file path is successfully chosen
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath() + ".png");
            try {
                ImageIO.write(
                        //create an image
                        img,
                        "png",
                        file
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            JOptionPane.showMessageDialog(panel, "Image successfully exported.", "Image Exported",
                    JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(panel, "Error exporting image.", "Exporting Error",
                    JOptionPane.ERROR_MESSAGE);

        }
    }


}
