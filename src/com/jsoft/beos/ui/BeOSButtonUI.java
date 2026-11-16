package com.jsoft.beos.ui;

import com.jsoft.beos.laf.BeOSColors;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalButtonUI;
import java.awt.*;

/**
 * BeOS button — extends MetalButtonUI.
 * Metal handles ALL logic: focus, keyboard, input map, layout, text, icon.
 * We only override paint() to draw our visual style:
 *   - Vertical gradient surface
 *   - ClassicX bevel (white top/left, shadow bottom/right, reversed on press)
 *   - 1px dark outer border rect
 *   - Dashed focus ring at inset 6
 * Then call super.paintText() and super.paintIcon() — Metal does the rest.
 */
public class BeOSButtonUI extends MetalButtonUI {

    private static final BeOSButtonUI INSTANCE = new BeOSButtonUI();

    public static ComponentUI createUI(JComponent c) { return INSTANCE; }

    @Override
    public void paint(Graphics g0, JComponent c) {
        AbstractButton b     = (AbstractButton) c;
        ButtonModel    model = b.getModel();
        Graphics2D     g     = (Graphics2D) g0.create();

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        int w = c.getWidth();
        int h = c.getHeight();

        boolean pressed  = model.isPressed() && model.isArmed();
        boolean rollover = model.isRollover();
        boolean disabled = !b.isEnabled();

        // ── Gradient surface ─────────────────────────────────────────────
        Color gradTop, gradBot;
        if (disabled) {
            gradTop = gradBot = BeOSColors.PANEL_BG;
        } else if (pressed) {
            gradTop = BeOSColors.BUTTON_PRESSED_TOP;
            gradBot = BeOSColors.BUTTON_PRESSED_BOTTOM;
        } else if (rollover) {
            gradTop = brighter(BeOSColors.BUTTON_TOP,    0.12f);
            gradBot = brighter(BeOSColors.BUTTON_BOTTOM, 0.12f);
        } else {
            gradTop = BeOSColors.BUTTON_TOP;
            gradBot = BeOSColors.BUTTON_BOTTOM;
        }
        g.setPaint(new GradientPaint(0, 2, gradTop, 0, h - 2, gradBot));
        g.fillRect(2, 2, w - 3, h - 3);

        // ── Inner bevel ──────────────────────────────────────────────────
        g.setStroke(new BasicStroke(1f));
        if (pressed) {
            // Reversed: shadow top/left, highlight bottom/right
            g.setColor(BeOSColors.PANEL_DARK_SHADOW);
            g.drawLine(2, 2, w-3, 2);
            g.drawLine(2, 2, 2,   h-3);
            g.setColor(Color.WHITE);
            g.drawLine(2,   h-2, w-2, h-2);
            g.drawLine(w-2, 4,   w-2, h-2);
        } else {
            // Normal: highlight top/left, shadow bottom/right
            g.setColor(Color.WHITE);
            g.drawLine(2, 2, w-3, 2);
            g.drawLine(2, 2, 2,   h-3);
            g.setColor(BeOSColors.PANEL_SHADOW);
            g.drawLine(2,   h-2, w-2, h-2);
            g.drawLine(w-2, 4,   w-2, h-2);
        }

        // ── Outer border rect ────────────────────────────────────────────
        g.setColor(disabled ? BeOSColors.PANEL_DARK_SHADOW : new Color(0x505050));
        g.drawRect(1, 1, w-2, h-2);

        // Default button extra ring
        if (b instanceof JButton && ((JButton)b).isDefaultButton() && !disabled) {
            g.setColor(new Color(0x303030));
            g.drawRect(0, 0, w-1, h-1);
        }

        g.dispose();

        // ── Let Metal paint text + icon + focus ──────────────────────────
        super.paint(g0, c);
    }

    @Override
    protected void paintButtonPressed(Graphics g, AbstractButton b) {
        // Intentionally empty — we handle pressed state in paint() above
    }

    @Override
    protected void paintFocus(Graphics g0, AbstractButton b,
                              Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {
        // Dashed focus ring at inset 6 (ClassicX style)
        Graphics2D g = (Graphics2D) g0.create();
        try {
            Color fc = UIManager.getColor("Button.focus");
            if (fc == null) fc = new Color(100, 148, 200);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    10f, new float[]{2f, 2f}, 0f));
            g.setColor(fc.darker());
            int off = 6;
            g.drawRect(off, off, b.getWidth() - off*2, b.getHeight() - off*2);
        } finally {
            g.dispose();
        }
    }

    private static Color brighter(Color c, float amt) {
        return new Color(
            Math.min(255, (int)(c.getRed()   + (255-c.getRed())   * amt)),
            Math.min(255, (int)(c.getGreen() + (255-c.getGreen()) * amt)),
            Math.min(255, (int)(c.getBlue()  + (255-c.getBlue())  * amt)));
    }
}
