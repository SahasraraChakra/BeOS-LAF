package com.jsoft.beos.ui;

import javax.swing.border.AbstractBorder;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * ClassicX 2-layer inset border for all text inputs and combo boxes.
 * Implements UIResource so Metal/Swing knows it was installed by the LAF
 * and won't block user-set borders.
 *
 * Outer ring:  top/left = #A0A0A0   bottom/right = #FAFAFA
 * Inner ring:  top/left = #808080   bottom/right = #DFDFDF
 * Padding:     [top=2, left=4, bottom=2, right=4] inside the 2px border
 * Total insets:[top=4, left=6, bottom=4, right=6]
 */
public class BeOSFieldBorder extends AbstractBorder implements UIResource {

    private static final Color OUTER_TL = new Color(160, 160, 160);
    private static final Color OUTER_BR = new Color(250, 250, 250);
    private static final Color INNER_TL = new Color(128, 128, 128);
    private static final Color INNER_BR = new Color(223, 223, 223);

    private static final Insets INSETS = new Insets(4, 6, 4, 6);

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(INSETS.top, INSETS.left, INSETS.bottom, INSETS.right);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.set(INSETS.top, INSETS.left, INSETS.bottom, INSETS.right);
        return insets;
    }

    @Override
    public void paintBorder(Component c, Graphics g0, int x, int y, int w, int h) {
        Graphics2D g = (Graphics2D) g0.create();

        // Outer ring
        g.setColor(OUTER_TL);
        g.drawLine(x,       y,       x+w-2,  y      ); // top
        g.drawLine(x,       y,       x,      y+h-2  ); // left
        g.setColor(OUTER_BR);
        g.drawLine(x,       y+h-1,   x+w-1,  y+h-1  ); // bottom
        g.drawLine(x+w-1,   y,       x+w-1,  y+h-1  ); // right

        // Inner ring
        g.setColor(INNER_TL);
        g.drawLine(x+1,     y+1,     x+w-3,  y+1    ); // top
        g.drawLine(x+1,     y+1,     x+1,    y+h-3  ); // left
        g.setColor(INNER_BR);
        g.drawLine(x+1,     y+h-2,   x+w-2,  y+h-2  ); // bottom
        g.drawLine(x+w-2,   y+1,     x+w-2,  y+h-2  ); // right

        g.dispose();
    }
}
