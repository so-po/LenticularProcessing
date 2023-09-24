import javax.swing.*;

public class QuestionButton extends JLabel {

    public QuestionButton(String txt) {
        this.setText("?");
        this.setSize(20, 20);
        this.setToolTipText(txt);
    }


}
