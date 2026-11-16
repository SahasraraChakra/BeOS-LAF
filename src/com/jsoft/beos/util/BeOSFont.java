package com.jsoft.beos.util;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Font resolver + AA hint helper.
 *
 * Tries Swiss721 BT → Arial → SansSerif at 11pt.
 *
 * AA rendering: the LAF installs a SwingUtilities2-style hint via
 * UIDefaults "SwingUtilities2.aaTextInfo" so ALL Swing text rendering
 * picks up LCD sub-pixel antialiasing automatically without the caller
 * needing to set it.
 */
public final class BeOSFont {

    private static final Font DEFAULT_FONT;

    static {
        Font f = tryFont("Swiss721 BT");
        if (f == null) f = tryFont("Swis721 BT");
        if (f == null) f = tryFont("Arial");
        if (f == null) f = new Font(Font.SANS_SERIF, Font.PLAIN, 11);
        DEFAULT_FONT = f.deriveFont(Font.PLAIN, 11f);
    }

    private static Font tryFont(String name) {
        Font f = new Font(name, Font.PLAIN, 11);
        return f.getFamily().equalsIgnoreCase(name) ? f : null;
    }

    public static Font getDefaultFont()           { return DEFAULT_FONT; }
    public static Font bold()                     { return DEFAULT_FONT.deriveFont(Font.BOLD); }
    public static Font derive(int style, float pt){ return DEFAULT_FONT.deriveFont(style, pt); }

    /**
     * Returns a RenderingHints map that enables LCD sub-pixel AA for text.
     * Install this in UIDefaults under the key "swing.aatext" = true
     * and Swing picks it up for every component.
     *
     * We also expose it so individual delegates can apply it to their
     * Graphics2D instances.
     */
    public static Map<RenderingHints.Key, Object> aaHints() {
        Map<RenderingHints.Key, Object> m = new HashMap<>();
        m.put(RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        m.put(RenderingHints.KEY_ANTIALIASING,        RenderingHints.VALUE_ANTIALIAS_ON);
        m.put(RenderingHints.KEY_RENDERING,           RenderingHints.VALUE_RENDER_QUALITY);
        m.put(RenderingHints.KEY_FRACTIONALMETRICS,   RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        return m;
    }

    /** Apply AA hints to a Graphics2D. Convenience for delegates. */
    public static void applyAA(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,   RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,        RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,           RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,   RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }

    private BeOSFont() {}
}
