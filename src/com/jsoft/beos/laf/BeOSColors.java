package com.jsoft.beos.laf;

import java.awt.Color;

/**
 * BeOS 5 authentic color palette.
 * All colors extracted from BeOS 5 / Haiku reference screenshots.
 */
public final class BeOSColors {

    // ── Window chrome ──────────────────────────────────────────────────────
    /** Title bar background gradient – top */
    public static final Color TITLEBAR_TOP          = new Color(152, 177, 205);
    /** Title bar background gradient – bottom */
    public static final Color TITLEBAR_BOTTOM       = new Color(102, 133, 166);
    /** Title bar text */
    public static final Color TITLEBAR_TEXT         = new Color(255, 255, 255);
    /** Title bar highlight stripe (top 1px) */
    public static final Color TITLEBAR_HIGHLIGHT    = new Color(188, 210, 230);
    /** Title bar shadow stripe (bottom 1px) */
    public static final Color TITLEBAR_SHADOW       = new Color(70, 100, 130);
    /** Inactive title bar – top */
    public static final Color TITLEBAR_INACTIVE_TOP    = new Color(200, 200, 200);
    /** Inactive title bar – bottom */
    public static final Color TITLEBAR_INACTIVE_BOTTOM = new Color(160, 160, 160);
    /** Inactive title bar text */
    public static final Color TITLEBAR_INACTIVE_TEXT   = new Color(120, 120, 120);

    // ── Window border ──────────────────────────────────────────────────────
    public static final Color BORDER_OUTER          = new Color(96,  96,  96);
    public static final Color BORDER_INNER_LIGHT    = new Color(220, 220, 220);
    public static final Color BORDER_INNER_DARK     = new Color(140, 140, 140);

    // ── Panel / background ─────────────────────────────────────────────────
    public static final Color PANEL_BG              = new Color(216, 216, 216);
    public static final Color PANEL_HIGHLIGHT       = new Color(255, 255, 255);
    public static final Color PANEL_SHADOW          = new Color(152, 152, 152);
    public static final Color PANEL_DARK_SHADOW     = new Color(100, 100, 100);

    // ── Text ───────────────────────────────────────────────────────────────
    public static final Color TEXT_FOREGROUND       = new Color(0,   0,   0);
    public static final Color TEXT_DISABLED         = new Color(150, 150, 150);
    public static final Color TEXT_SELECTION_BG     = new Color(100, 148, 200);
    public static final Color TEXT_SELECTION_FG     = new Color(255, 255, 255);

    // ── Button ─────────────────────────────────────────────────────────────
    public static final Color BUTTON_TOP            = new Color(232, 232, 232);
    public static final Color BUTTON_BOTTOM         = new Color(196, 196, 196);
    public static final Color BUTTON_PRESSED_TOP    = new Color(180, 180, 180);
    public static final Color BUTTON_PRESSED_BOTTOM = new Color(210, 210, 210);
    public static final Color BUTTON_BORDER_LIGHT   = new Color(255, 255, 255);
    public static final Color BUTTON_BORDER_DARK    = new Color(110, 110, 110);
    public static final Color BUTTON_DEFAULT_RING   = new Color(80,  80,  80);

    // ── TextField / TextArea ───────────────────────────────────────────────
    public static final Color FIELD_BG              = new Color(255, 255, 255);
    public static final Color FIELD_BORDER_DARK     = new Color(100, 100, 100);
    public static final Color FIELD_BORDER_LIGHT    = new Color(230, 230, 230);

    // ── Checkbox / Radio ───────────────────────────────────────────────────
    public static final Color CHECK_BG              = new Color(255, 255, 255);
    public static final Color CHECK_BORDER          = new Color(110, 110, 110);
    public static final Color CHECK_MARK            = new Color(0,   0,   0);

    // ── Scroll bar ─────────────────────────────────────────────────────────
    public static final Color SCROLL_TRACK          = new Color(200, 200, 200);
    public static final Color SCROLL_THUMB_TOP      = new Color(235, 235, 235);
    public static final Color SCROLL_THUMB_BOTTOM   = new Color(185, 185, 185);
    public static final Color SCROLL_ARROW_BG       = new Color(216, 216, 216);

    // ── Progress bar ───────────────────────────────────────────────────────
    public static final Color PROGRESS_BG           = new Color(200, 200, 200);
    public static final Color PROGRESS_FILL         = new Color(100, 148, 200);

    // ── Menu ───────────────────────────────────────────────────────────────
    public static final Color MENU_BG               = new Color(216, 216, 216);
    public static final Color MENU_SELECTION_BG     = new Color(100, 148, 200);
    public static final Color MENU_SELECTION_FG     = new Color(255, 255, 255);
    public static final Color MENU_SEPARATOR        = new Color(160, 160, 160);

    // ── Tooltip ────────────────────────────────────────────────────────────
    public static final Color TOOLTIP_BG            = new Color(255, 255, 220);
    public static final Color TOOLTIP_FG            = new Color(0,   0,   0);
    public static final Color TOOLTIP_BORDER        = new Color(0,   0,   0);

    // ── Table / List ───────────────────────────────────────────────────────
    public static final Color TABLE_BG              = new Color(255, 255, 255);
    public static final Color TABLE_ALT_BG          = new Color(245, 245, 245);
    public static final Color TABLE_GRID            = new Color(200, 200, 200);
    public static final Color TABLE_HEADER_TOP      = new Color(235, 235, 235);
    public static final Color TABLE_HEADER_BOTTOM   = new Color(200, 200, 200);

    // ── Tab ────────────────────────────────────────────────────────────────
    public static final Color TAB_SELECTED_TOP      = new Color(232, 232, 232);
    public static final Color TAB_SELECTED_BOTTOM   = new Color(216, 216, 216);
    public static final Color TAB_UNSELECTED_TOP    = new Color(200, 200, 200);
    public static final Color TAB_UNSELECTED_BOTTOM = new Color(180, 180, 180);

    // ── Slider ─────────────────────────────────────────────────────────────
    public static final Color SLIDER_TRACK          = new Color(180, 180, 180);
    public static final Color SLIDER_THUMB_TOP      = new Color(235, 235, 235);
    public static final Color SLIDER_THUMB_BOTTOM   = new Color(185, 185, 185);

    // ── Spinner ────────────────────────────────────────────────────────────
    public static final Color SPINNER_ARROW         = new Color(80,  80,  80);

    private BeOSColors() {}

    /** Blend two colors by ratio t ∈ [0,1] (0 = c1, 1 = c2). */
    public static Color blend(Color c1, Color c2, float t) {
        int r = (int)(c1.getRed()   + t * (c2.getRed()   - c1.getRed()));
        int g = (int)(c1.getGreen() + t * (c2.getGreen() - c1.getGreen()));
        int b = (int)(c1.getBlue()  + t * (c2.getBlue()  - c1.getBlue()));
        return new Color(clamp(r), clamp(g), clamp(b));
    }

    private static int clamp(int v) { return Math.max(0, Math.min(255, v)); }
}
