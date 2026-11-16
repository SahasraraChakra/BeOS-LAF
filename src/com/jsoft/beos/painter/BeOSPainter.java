package com.jsoft.beos.painter;

import com.jsoft.beos.laf.BeOSColors;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * Stateless paint utilities shared by all BeOS UI delegates.
 */
public final class BeOSPainter {

    private BeOSPainter() {}

    // ── Gradient fill ──────────────────────────────────────────────────────

    public static void fillVerticalGradient(Graphics2D g, int x, int y,
                                             int w, int h,
                                             Color top, Color bottom) {
        GradientPaint gp = new GradientPaint(x, y, top, x, y + h, bottom);
        g.setPaint(gp);
        g.fillRect(x, y, w, h);
    }

    public static void fillVerticalGradientRound(Graphics2D g, int x, int y,
                                                  int w, int h, int arc,
                                                  Color top, Color bottom) {
        GradientPaint gp = new GradientPaint(x, y, top, x, y + h, bottom);
        g.setPaint(gp);
        g.fill(new RoundRectangle2D.Float(x, y, w, h, arc, arc));
    }

    // ── Raised bevel (BeOS panel / button face) ────────────────────────────

    /**
     * Draws a classic BeOS two-tone raised bevel.
     * Light on top/left, shadow on bottom/right.
     */
    public static void drawRaisedBevel(Graphics2D g, int x, int y, int w, int h) {
        // outer light
        g.setColor(BeOSColors.PANEL_HIGHLIGHT);
        g.drawLine(x, y, x + w - 2, y);          // top
        g.drawLine(x, y, x,         y + h - 2);  // left
        // outer shadow
        g.setColor(BeOSColors.PANEL_DARK_SHADOW);
        g.drawLine(x + w - 1, y,         x + w - 1, y + h - 1); // right
        g.drawLine(x,         y + h - 1, x + w - 1, y + h - 1); // bottom
        // inner light
        g.setColor(BeOSColors.PANEL_HIGHLIGHT);
        g.drawLine(x + 1, y + 1, x + w - 3, y + 1);
        g.drawLine(x + 1, y + 1, x + 1,     y + h - 3);
        // inner shadow
        g.setColor(BeOSColors.PANEL_SHADOW);
        g.drawLine(x + w - 2, y + 1,     x + w - 2, y + h - 2);
        g.drawLine(x + 1,     y + h - 2, x + w - 2, y + h - 2);
    }

    /**
     * Draws a sunken bevel (pressed / inset area).
     */
    public static void drawSunkenBevel(Graphics2D g, int x, int y, int w, int h) {
        g.setColor(BeOSColors.PANEL_DARK_SHADOW);
        g.drawLine(x, y, x + w - 2, y);
        g.drawLine(x, y, x,         y + h - 2);
        g.setColor(BeOSColors.PANEL_HIGHLIGHT);
        g.drawLine(x + w - 1, y,         x + w - 1, y + h - 1);
        g.drawLine(x,         y + h - 1, x + w - 1, y + h - 1);
        g.setColor(BeOSColors.PANEL_SHADOW);
        g.drawLine(x + 1, y + 1, x + w - 3, y + 1);
        g.drawLine(x + 1, y + 1, x + 1,     y + h - 3);
        g.setColor(BeOSColors.PANEL_HIGHLIGHT);
        g.drawLine(x + w - 2, y + 1,     x + w - 2, y + h - 2);
        g.drawLine(x + 1,     y + h - 2, x + w - 2, y + h - 2);
    }

    // ── Text field border ──────────────────────────────────────────────────

    public static void drawFieldBorder(Graphics2D g, int x, int y, int w, int h) {
        // Outer dark
        g.setColor(BeOSColors.FIELD_BORDER_DARK);
        g.drawRect(x, y, w - 1, h - 1);
        // Inner top/left slightly lighter
        g.setColor(BeOSColors.PANEL_SHADOW);
        g.drawLine(x + 1, y + 1, x + w - 3, y + 1);
        g.drawLine(x + 1, y + 1, x + 1,     y + h - 3);
        // Inner bottom/right highlight
        g.setColor(BeOSColors.FIELD_BORDER_LIGHT);
        g.drawLine(x + w - 2, y + 1,     x + w - 2, y + h - 2);
        g.drawLine(x + 1,     y + h - 2, x + w - 2, y + h - 2);
    }

    // ── Arrow helpers ──────────────────────────────────────────────────────

    public enum Direction { NORTH, SOUTH, EAST, WEST }

    public static void fillArrow(Graphics2D g, Direction dir, int cx, int cy, int size, Color color) {
        g.setColor(color);
        int[] xs, ys;
        int h = size / 2;
        switch (dir) {
            case NORTH:
                xs = new int[]{cx,      cx + h,  cx - h};
                ys = new int[]{cy - h,  cy + h,  cy + h};
                break;
            case SOUTH:
                xs = new int[]{cx,      cx + h,  cx - h};
                ys = new int[]{cy + h,  cy - h,  cy - h};
                break;
            case EAST:
                xs = new int[]{cx + h,  cx - h,  cx - h};
                ys = new int[]{cy,      cy - h,  cy + h};
                break;
            default: // WEST
                xs = new int[]{cx - h,  cx + h,  cx + h};
                ys = new int[]{cy,      cy - h,  cy + h};
                break;
        }
        g.fillPolygon(xs, ys, 3);
    }

    // ── Check mark ────────────────────────────────────────────────────────

    public static void drawCheckMark(Graphics2D g, int x, int y, int size, Color color) {
        g.setColor(color);
        Stroke old = g.getStroke();
        g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int x1 = x + 2, y1 = y + size / 2;
        int x2 = x + size / 3 + 1, y2 = y + size - 3;
        int x3 = x + size - 2, y3 = y + 2;
        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x2, y2, x3, y3);
        g.setStroke(old);
    }

    // ── BeOS-style close / zoom / hide button ────────────────────────────

    /**
     * Draws the BeOS window tab close button (small square with X).
     */
    public static void drawCloseButton(Graphics2D g, int x, int y, int sz, boolean hover) {
        // Background
        fillVerticalGradient(g, x, y, sz, sz,
                hover ? new Color(220, 80, 80) : BeOSColors.TITLEBAR_TOP,
                hover ? new Color(180, 40, 40) : BeOSColors.TITLEBAR_BOTTOM);
        drawRaisedBevel(g, x, y, sz, sz);
        // X mark
        g.setColor(Color.WHITE);
        Stroke s = g.getStroke();
        g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int m = 3;
        g.drawLine(x + m, y + m, x + sz - m - 1, y + sz - m - 1);
        g.drawLine(x + sz - m - 1, y + m, x + m, y + sz - m - 1);
        g.setStroke(s);
    }

    /**
     * Draws the BeOS zoom (maximize) button.
     */
    public static void drawZoomButton(Graphics2D g, int x, int y, int sz, boolean hover) {
        fillVerticalGradient(g, x, y, sz, sz,
                hover ? new Color(80, 180, 80) : BeOSColors.TITLEBAR_TOP,
                hover ? new Color(40, 140, 40) : BeOSColors.TITLEBAR_BOTTOM);
        drawRaisedBevel(g, x, y, sz, sz);
        g.setColor(Color.WHITE);
        int m = 3;
        g.drawRect(x + m, y + m, sz - 2 * m - 1, sz - 2 * m - 1);
    }

    // ── RenderingHints helper ──────────────────────────────────────────────

    public static void setAA(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,        RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,           RenderingHints.VALUE_RENDER_QUALITY);
    }
}
