import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CreationPage implements ActionListener {

    private JPanel panel;
    private JButton exportButton;
    private JButton importButton;
    private JTextField dpiInputField;
    private JTextField lpiInputField;
    private JButton lpiLabel;
    private JList fileList;


    JFileChooser fileChooser;
    ArrayList<BufferedImage> images; //this array should act as a parallel array with listModel.
    // Be careful not to delete items in one list and not the other.


    DefaultListModel<String> listModel;

    public CreationPage(MainWindow mw) {
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "png", "jpg"));
        fileChooser.setAcceptAllFileFilterUsed(false); //^< prevents user from selecting non-image files.


        importButton.addActionListener(this);exportButton.addActionListener(this);
        exportButton.setEnabled(false);

        images = new ArrayList<BufferedImage>(); //list of imported images, stored as BufferedImages
        listModel = new DefaultListModel<String>(); //list of file paths of images, to show to user
        fileList.setModel(listModel); //<so that fileList's contents can be accessed via listModel
        fileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fileList.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                //If the delete key is pressed, remove the selected file in the list:
                if(e.getKeyCode() == 8) { //<(delete/backspace keyCode is 8)
                    if(!fileList.isSelectionEmpty()) {
                        int selectedIndex = listModel.indexOf(fileList.getSelectedValue());
                        images.remove(selectedIndex); //remove the actual image from storage
                        listModel.remove(selectedIndex); //remove the file path from the list

                    }
                }
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if (event.getSource() == importButton) {
            int response = fileChooser.showOpenDialog(panel); //(Open a file selection dialog.)
            if (response == JFileChooser.APPROVE_OPTION) { //(When a file is chosen.)
                String path = fileChooser.getSelectedFile().getAbsolutePath();
                try {
                    //Make an image from the file path:
                    BufferedImage tempImg = ImageIO.read(new File(path));
                    //Store imported image:
                    images.add(tempImg);
                    listModel.addElement(path);
                    fileList.update(panel.getGraphics());

                } catch (IOException ex){

                    JOptionPane.showMessageDialog(panel,
                            "The file type is incorrect or corrupted. " +
                                    "Please upload a png or jpg file.",
                            "Error Importing Image",
                            JOptionPane.ERROR_MESSAGE);
                }

            }

        } else if (event.getSource() == exportButton) {

            float templpi = 0;
            float tempdpi = 0;

            try {
                templpi = Float.parseFloat(lpiInputField.getText());
                tempdpi = Float.parseFloat(dpiInputField.getText());

            } catch (Exception ignored){}

            if (templpi > 200 || templpi < 20) {
                JOptionPane.showMessageDialog(panel,
                        "For the LPI, please input a number within 20 and 200.",
                        "Missing/Incorrect Settings",
                        JOptionPane.ERROR_MESSAGE);
                templpi = 0;
            }

            if (tempdpi > 1200 || tempdpi < 0) {
                JOptionPane.showMessageDialog(panel,
                        "For the DPI, please input a number within 0 and 1200.",
                        "Missing/Incorrect Settings",
                        JOptionPane.ERROR_MESSAGE);
                tempdpi = 0;
            }

            if (templpi > 0 && tempdpi > 0 && images.size()>1) {
                try {
                    LenticularImageProcessing.exportImage(createInterlacedImage(templpi, tempdpi, images), panel);
                    //float LPI, float dpi, ArrayList<BufferedImage> images
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(panel,
                            "Please upload 2 or more images.",
                            "Error Interlacing Images",
                            JOptionPane.ERROR_MESSAGE);

                }
            }

        }

        //Enable the export button if all settings are filled out & all images are imported:
        if((lpiInputField.getText()!=null) && (dpiInputField.getText()!=null) && (images.size()>1)) {
            exportButton.setEnabled(true);
        } else {
            exportButton.setEnabled(false);
        }

    }
    public BufferedImage createInterlacedImage(float LPI, float dpi, ArrayList<BufferedImage> images) throws Exception {
        //Returns an interlaced buffered image, using the images input.
        //!NOTE: To use this method, the 'images' ArrayList must have a size of ≥ 2.

        if (images.size() < 2) {
            throw new Exception("More than 2 images must be uploaded to interlace images.");
        }

        float lineWidth = LenticularImageProcessing.lineWidthPixels(LPI, dpi, images.size());
        float lineAlternation = 2*lineWidth;

        int imgH = images.get(1).getHeight();
        int imgW = images.get(1).getWidth();

        BufferedImage combinedImage = new BufferedImage((int)(dpi*8.27f), (int)(dpi*11.69f),
                BufferedImage.TYPE_INT_ARGB);
        //^ A new, empty A4 image

        Graphics2D combinedG2D = (Graphics2D) combinedImage.getGraphics();
        //^In order to edit & add stuff to combinedImage

        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1.0f);
        //^A filter to use

        combinedG2D.drawImage(images.get(0), 0, 0, panel);
        //^Draw the first image underneath

        for (int i = 1; i < images.size(); i++) {
            //^Draw every image after the first image
            BufferedImage img = images.get(i);
            Graphics2D imageg2D = (Graphics2D)img.getGraphics();

            imageg2D.setComposite(alphaComposite);
            //^Sets a filter that makes everything drawn after this subtract.
            // ^(acts much like an alpha mask.)

            imageg2D.setColor(Color.BLACK);
            imageg2D.setStroke(new BasicStroke(lineWidth));
            //^ Change line size. Units are pixels

            float start = (lineWidth)*(i);
            for (float j = start; j < imgH; j+= lineAlternation) {
                imageg2D.draw(new Line2D.Double(0, j, imgW, j));
            }
            combinedG2D.drawImage(img, 0, 0, panel);
            imageg2D.dispose();

        }
        combinedG2D.dispose();
        return combinedImage;

    }

    public JPanel getPanel() {
        //for MainWindow
        return panel;
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here

        lpiInputField = new JFormattedTextField();
        dpiInputField = new JFormattedTextField();
    }


}
