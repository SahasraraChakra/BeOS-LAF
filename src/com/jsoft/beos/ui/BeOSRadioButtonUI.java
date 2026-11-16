package com.jsoft.beos.ui;

import com.jsoft.beos.laf.BeOSColors;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalRadioButtonUI;
import java.awt.*;

/**
 * BeOS RadioButton — extends MetalRadioButtonUI.
 * Metal handles all logic. We only provide a custom Icon.
 */
public class BeOSRadioButtonUI extends MetalRadioButtonUI {

    private static final BeOSRadioButtonUI INSTANCE = new BeOSRadioButtonUI();

    public static ComponentUI createUI(JComponent c) { return INSTANCE; }

    @Override
    public void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        b.setIcon(RADIO_ICON);
    }

    static final Icon RADIO_ICON = new Icon() {
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

            // Circle fill
            g.setColor(enabled ? Color.WHITE : new Color(230,230,230));
            g.fillOval(x, y, S-1, S-1);

            // Outer ring — dark top-left, light bottom-right
            g.setColor(new Color(160,160,160));
            g.drawArc(x,   y,   S-1, S-1, 45,  180);
            g.setColor(new Color(250,250,250));
            g.drawArc(x,   y,   S-1, S-1, 225, 180);

            // Inner ring
            g.setColor(new Color(128,128,128));
            g.drawArc(x+1, y+1, S-3, S-3, 45,  180);
            g.setColor(new Color(223,223,223));
            g.drawArc(x+1, y+1, S-3, S-3, 225, 180);

            // Dot
            if (selected) {
                g.setColor(enabled ? BeOSColors.CHECK_MARK : BeOSColors.TEXT_DISABLED);
                int ds = 5;
                g.fillOval(x+(S-ds)/2, y+(S-ds)/2, ds, ds);
            }

            g.dispose();
        }
    };
}
