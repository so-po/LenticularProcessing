import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TutorialPage implements ActionListener {
    JPanel panel;
    private int index = 0;
    private JButton nextSlideButton;
    private JButton previousSlideButton;
    private JLabel slideLabel;

    private ImageIcon[] slides = {new ImageIcon("src/LenticularTutorial500/Slide1.png"), new ImageIcon("src/LenticularTutorial500/Slide2.png"),
            new ImageIcon("src/LenticularTutorial500/Slide3.png"), new ImageIcon("src/LenticularTutorial500/Slide4.png"),
            new ImageIcon("src/LenticularTutorial500/Slide5.png"), new ImageIcon("src/LenticularTutorial500/Slide6.png")};


    public TutorialPage() {
        JPanel panel = new JPanel();
        nextSlideButton.addActionListener(this);
        previousSlideButton.addActionListener(this);

        previousSlideButton.setEnabled(false);


    }
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == nextSlideButton && index <5) {
            index++;
        } else if (e.getSource() == previousSlideButton && index>0) {
            index--;
        }
        updateButtons();

        slideLabel.setIcon(slides[index]);
    }

    public void updateButtons() {
        if (index >= 5) {
            nextSlideButton.setEnabled(false);
        } else {
            nextSlideButton.setEnabled(true);
        }

        if(index <= 0) {
            previousSlideButton.setEnabled(false);
        } else {
            previousSlideButton.setEnabled(true);
        }
    }
}


