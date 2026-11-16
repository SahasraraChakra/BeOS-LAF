import com.jsoft.beos.laf.BeOSLookAndFeel;
import com.jsoft.beos.ui.BeOSRootPaneUI;
import javax.swing.*;
import java.awt.*;

/**
 * Minimal test — prints whether title bar pane is present and sized.
 * Run headless via Xvfb or check output manually.
 */
public class TitleBarTest {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new BeOSLookAndFeel());
        JFrame.setDefaultLookAndFeelDecorated(true);

        JFrame f = new JFrame("TitleBar Test");
        f.setSize(400, 300);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JRootPane root = f.getRootPane();
        System.out.println("Decoration style: " + root.getWindowDecorationStyle());
        System.out.println("Root border: " + root.getBorder());
        System.out.println("Root layout: " + root.getLayout());

        // Check layered pane for title bar
        JLayeredPane lp = root.getLayeredPane();
        boolean found = false;
        for (Component c : lp.getComponents()) {
            if (c instanceof BeOSRootPaneUI.BeOSTitleBarPane) {
                System.out.println("TitleBarPane FOUND in LayeredPane: " + c.getBounds());
                found = true;
            }
        }
        if (!found) System.out.println("ERROR: TitleBarPane NOT found in LayeredPane");

        f.setVisible(true);
        // Force layout
        f.revalidate();
        f.doLayout();
        // Check again after layout
        for (Component c : lp.getComponents()) {
            if (c instanceof BeOSRootPaneUI.BeOSTitleBarPane) {
                System.out.println("TitleBarPane after layout: bounds=" + c.getBounds()
                    + " visible=" + c.isVisible());
            }
        }
        // Exit after brief pause
        Thread.sleep(500);
        System.exit(0);
    }
}
