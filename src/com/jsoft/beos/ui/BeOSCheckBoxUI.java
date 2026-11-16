package com.jsoft.beos.ui;

import com.jsoft.beos.laf.BeOSColors;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalCheckBoxUI;
import java.awt.*;

/**
 * BeOS CheckBox — extends MetalCheckBoxUI.
 * Metal handles all logic. We only provide a custom Icon
 * with the ClassicX 2-layer inset border look.
 */
public class BeOSCheckBoxUI extends MetalCheckBoxUI {

    private static final BeOSCheckBoxUI INSTANCE = new BeOSCheckBoxUI();

    public static ComponentUI createUI(JComponent c) { return INSTANCE; }

    @Override
    public void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        // Replace Metal's icon with ours
        b.setIcon(CHECK_ICON);
    }

    static final Icon CHECK_ICON = new Icon() {
        private static final int S = 13;
        @Override public int getIconWidth()  { return S; }
        @Override public int getIconHeight() { return S; }

        @Override
        public void paintIcon(Component c, Graphics g0, int x, int y) {
            AbstractButton b = (AbstractButton) c;
            ButtonModel    m = b.getModel();
            Graphics2D     g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean enabled  = m.isEnabled();
            boolean selected = m.isSelected();

            // Box fill
            g.setColor(enabled ? Color.WHITE : new Color(230,230,230));
            g.fillRect(x, y, S, S);

            // Outer ring
            g.setColor(new Color(160,160,160));
            g.drawLine(x,     y,     x+S-2, y    );
            g.drawLine(x,     y,     x,     y+S-2);
            g.setColor(new Color(250,250,250));
            g.drawLine(x,     y+S-1, x+S-1, y+S-1);
            g.drawLine(x+S-1, y,     x+S-1, y+S-1);

            // Inner ring
            g.setColor(new Color(128,128,128));
            g.drawLine(x+1,   y+1,   x+S-3, y+1  );
            g.drawLine(x+1,   y+1,   x+1,   y+S-3);
            g.setColor(new Color(223,223,223));
            g.drawLine(x+1,   y+S-2, x+S-2, y+S-2);
            g.drawLine(x+S-2, y+1,   x+S-2, y+S-2);

            // Check mark
            if (selected) {
                g.setColor(enabled ? BeOSColors.CHECK_MARK : BeOSColors.TEXT_DISABLED);
                g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.drawLine(x+2,     y+S/2,   x+S/3+1, y+S-3);
                g.drawLine(x+S/3+1, y+S-3,   x+S-2,   y+2  );
            }

            g.dispose();
        }
    };
}
