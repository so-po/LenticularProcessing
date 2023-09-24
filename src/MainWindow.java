import javax.swing.*;

public class MainWindow {

    private JTabbedPane tabbedPane;
    private JPanel panel;
    private JFrame mainFrame;

    public MainWindow() {

        mainFrame = new JFrame();
        JPanel panel = new JPanel();

        //Instantiating each page's class:
        PitchTestPage ptp = new PitchTestPage(this);
        TutorialPage tp = new TutorialPage();
        CreationPage cp = new CreationPage(this);

        //Adding the panels of each page to a tabbedPane so they can be displayed:
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setSize(900, 650);
        tabbedPane.addTab("Tutorial", tp.getPanel());
        tabbedPane.addTab("Image Processing", cp.getPanel());
        tabbedPane.addTab("Pitch Test", ptp.getPanel());

        panel.add(tabbedPane);
        mainFrame.add(panel);

        //Basic Settings:
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setSize(1000, 700);
        panel.setSize(900, 650);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);

    }

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
    }



}
