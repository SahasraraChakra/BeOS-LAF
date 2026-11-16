package com.jsoft.beos.ui;

import com.jsoft.beos.laf.BeOSColors;
import com.jsoft.beos.util.BeOSFont;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRootPaneUI;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;

/**
 * BeOS decorated window title bar.
 * Extends BasicRootPaneUI — no dependency on MetalRootPaneUI internals.
 *
 * Layout (same pattern as MetalRootPaneUI.MetalRootLayout):
 *   JRootPane
 *     glassPane      : (0,0,W,H)
 *     layeredPane    : (bw, bw, W-2bw, H-2bw)   inside window border
 *       titlePane    : (0, 0, lpW, tbH)          at top of layered pane
 *       menuBar      : (0, tbH, lpW, mbH)        if present
 *       contentPane  : (0, tbH+mbH, lpW, rem)    fills rest
 */
public class BeOSRootPaneUI extends BasicRootPaneUI {

    private static final int BORDER  = 4;  // window border thickness px
    private static final int TB_H    = 24; // title bar height px

    private JRootPane      root;
    private Window         window;
    private JComponent     titlePane;
    private LayoutManager  savedLayout;

    public static ComponentUI createUI(JComponent c) { return new BeOSRootPaneUI(); }

    // ── Install / uninstall ───────────────────────────────────────────────

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        root = (JRootPane) c;
        if (root.getWindowDecorationStyle() != JRootPane.NONE) {
            install();
        }
    }

    @Override
    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        uninstall();
        root = null;
        window = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent e) {
        super.propertyChange(e);
        if ("windowDecorationStyle".equals(e.getPropertyName())) {
            uninstall();
            if (root.getWindowDecorationStyle() != JRootPane.NONE) install();
        }
    }

    private void install() {
        window     = SwingUtilities.getWindowAncestor(root);
        titlePane  = new BeOSTitleBarPane(root);
        savedLayout = root.getLayout();
        root.setLayout(new BeOSRootLayout());
        root.setBorder(new BeOSWindowBorder());
        // Add title pane to layered pane at FRAME_CONTENT_LAYER (Metal pattern)
        root.getLayeredPane().add(titlePane, JLayeredPane.FRAME_CONTENT_LAYER);
        root.revalidate();
    }

    private void uninstall() {
        if (titlePane != null) {
            root.getLayeredPane().remove(titlePane);
            titlePane = null;
        }
        if (savedLayout != null) {
            root.setLayout(savedLayout);
            savedLayout = null;
        }
        root.setBorder(null);
    }

    // ── Layout ────────────────────────────────────────────────────────────

    private class BeOSRootLayout implements LayoutManager2 {

        @Override
        public void layoutContainer(Container parent) {
            int W  = parent.getWidth();
            int H  = parent.getHeight();
            int bw = BORDER;

            // Glass pane — always full window size
            Component glass = root.getGlassPane();
            if (glass != null) glass.setBounds(0, 0, W, H);

            // Layered pane — inside border on all 4 sides
            JLayeredPane lp = root.getLayeredPane();
            int lpX = bw, lpY = bw;
            int lpW = W - 2*bw;
            int lpH = H - 2*bw;
            lp.setBounds(lpX, lpY, lpW, lpH);

            // Title pane — top of layered pane
            int tbH = 0;
            if (titlePane != null) {
                tbH = titlePane.getPreferredSize().height;
                titlePane.setBounds(0, 0, lpW, tbH);
            }

            // Menu bar — directly below title pane
            int mbH = 0;
            JMenuBar mb = root.getJMenuBar();
            if (mb != null && mb.isVisible()) {
                mbH = mb.getPreferredSize().height;
                mb.setBounds(0, tbH, lpW, mbH);
            }

            // Content pane — fills remaining space
            Container cp = root.getContentPane();
            if (cp != null) {
                cp.setBounds(0, tbH + mbH, lpW, Math.max(0, lpH - tbH - mbH));
            }
        }

        @Override public Dimension preferredLayoutSize(Container p) {
            Insets i = p.getInsets();
            Dimension d = root.getContentPane().getPreferredSize();
            int mbH = root.getJMenuBar() != null ? root.getJMenuBar().getPreferredSize().height : 0;
            return new Dimension(d.width + i.left + i.right,
                                 d.height + TB_H + mbH + i.top + i.bottom);
        }
        @Override public Dimension minimumLayoutSize(Container p) {
            Insets i = p.getInsets();
            Dimension d = root.getContentPane().getMinimumSize();
            return new Dimension(d.width + i.left + i.right,
                                 d.height + TB_H + i.top + i.bottom);
        }
        @Override public Dimension maximumLayoutSize(Container p) {
            return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }
        @Override public void addLayoutComponent(String n, Component c) {}
        @Override public void removeLayoutComponent(Component c) {}
        @Override public void addLayoutComponent(Component c, Object o) {}
        @Override public float getLayoutAlignmentX(Container c) { return 0.5f; }
        @Override public float getLayoutAlignmentY(Container c) { return 0.5f; }
        @Override public void invalidateLayout(Container c) {}
    }

    // ── Title bar pane ────────────────────────────────────────────────────

    public static class BeOSTitleBarPane extends JComponent {

        private static final int H        = TB_H;
        private static final int BTN_SZ   = 16;
        private static final int BTN_Y    = (H - BTN_SZ) / 2;

        private final JRootPane root;
        private Window  win;
        private boolean active   = true;
        private boolean cHover, zHover, mHover;
        private Point   dragPt;

        // Button rects
        private final Rectangle rClose = new Rectangle(6,    BTN_Y, BTN_SZ, BTN_SZ);
        private final Rectangle rZoom  = new Rectangle(25,   BTN_Y, BTN_SZ, BTN_SZ);
        private final Rectangle rMin   = new Rectangle(44,   BTN_Y, BTN_SZ, BTN_SZ);

        public BeOSTitleBarPane(JRootPane root) {
            this.root = root;
            setPreferredSize(new Dimension(0, H));
            setOpaque(true);

            MouseInputAdapter ma = new MouseInputAdapter() {
                @Override public void mouseClicked(MouseEvent e) {
                    resolve();
                    if (win == null) return;
                    if (rClose.contains(e.getPoint())) {
                        win.dispatchEvent(new WindowEvent(win, WindowEvent.WINDOW_CLOSING));
                    } else if (rZoom.contains(e.getPoint()) && win instanceof Frame) {
                        Frame f = (Frame) win;
                        f.setExtendedState(f.getExtendedState() == Frame.MAXIMIZED_BOTH
                                ? Frame.NORMAL : Frame.MAXIMIZED_BOTH);
                    } else if (rMin.contains(e.getPoint()) && win instanceof Frame) {
                        ((Frame) win).setExtendedState(Frame.ICONIFIED);
                    }
                }
                @Override public void mouseMoved(MouseEvent e) {
                    boolean c = rClose.contains(e.getPoint());
                    boolean z = rZoom.contains(e.getPoint());
                    boolean m = rMin.contains(e.getPoint());
                    if (c!=cHover||z!=zHover||m!=mHover){cHover=c;zHover=z;mHover=m;repaint();}
                }
                @Override public void mouseExited(MouseEvent e) {
                    cHover=false; zHover=false; mHover=false; repaint();
                }
                @Override public void mousePressed(MouseEvent e) {
                    resolve();
                    if (!rClose.contains(e.getPoint()) &&
                        !rZoom.contains(e.getPoint())  &&
                        !rMin.contains(e.getPoint())) {
                        dragPt = e.getLocationOnScreen();
                    }
                }
                @Override public void mouseDragged(MouseEvent e) {
                    if (dragPt == null || win == null) return;
                    if (win instanceof Frame &&
                        ((Frame)win).getExtendedState() == Frame.MAXIMIZED_BOTH) return;
                    Point now = e.getLocationOnScreen();
                    Point loc = win.getLocation();
                    win.setLocation(loc.x + now.x - dragPt.x,
                                    loc.y + now.y - dragPt.y);
                    dragPt = now;
                }
                @Override public void mouseReleased(MouseEvent e) { dragPt = null; }
            };
            addMouseListener(ma);
            addMouseMotionListener(ma);

            // Window focus tracking
            root.addHierarchyListener(e -> {
                resolve();
                if (win != null) {
                    win.addWindowFocusListener(new WindowFocusListener() {
                        @Override public void windowGainedFocus(WindowEvent e) { active=true;  repaint(); }
                        @Override public void windowLostFocus(WindowEvent e)   { active=false; repaint(); }
                    });
                }
            });
        }

        private void resolve() {
            if (win == null) win = SwingUtilities.getWindowAncestor(root);
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

            int w = getWidth(), h = getHeight();

            // Gradient background
            Color top = active ? BeOSColors.TITLEBAR_TOP    : BeOSColors.TITLEBAR_INACTIVE_TOP;
            Color bot = active ? BeOSColors.TITLEBAR_BOTTOM : BeOSColors.TITLEBAR_INACTIVE_BOTTOM;
            g.setPaint(new GradientPaint(0, 0, top, 0, h, bot));
            g.fillRect(0, 0, w, h);

            // Top highlight, bottom shadow
            g.setColor(active ? BeOSColors.TITLEBAR_HIGHLIGHT : new Color(220,220,220));
            g.drawLine(0, 0, w-1, 0);
            g.setColor(active ? BeOSColors.TITLEBAR_SHADOW : new Color(130,130,130));
            g.drawLine(0, h-1, w-1, h-1);

            resolve();
            boolean isFrame = (win instanceof Frame);

            // Close button
            paintTitleButton(g, rClose.x, rClose.y, BTN_SZ, cHover && active, 'X');
            // Zoom button (frames only)
            if (isFrame) {
                paintTitleButton(g, rZoom.x, rZoom.y, BTN_SZ, zHover && active, 'Z');
                paintTitleButton(g, rMin.x,  rMin.y,  BTN_SZ, mHover && active, 'M');
            }

            // Centered title text
            String title = "";
            if (win instanceof Frame)  title = ((Frame)win).getTitle();
            if (win instanceof Dialog) title = ((Dialog)win).getTitle();
            if (title == null) title = "";

            g.setFont(BeOSFont.bold().deriveFont(11f));
            FontMetrics fm = g.getFontMetrics();
            int tx = (w - fm.stringWidth(title)) / 2;
            int ty = (h - fm.getHeight()) / 2 + fm.getAscent();

            // Drop shadow for active
            if (active) {
                g.setColor(new Color(0,0,0,70));
                g.drawString(title, tx+1, ty+1);
            }
            g.setColor(active ? BeOSColors.TITLEBAR_TEXT : BeOSColors.TITLEBAR_INACTIVE_TEXT);
            g.drawString(title, tx, ty);

            g.dispose();
        }

        /**
         * Draws a single title bar button with raised bevel + symbol.
         * type: 'X'=close  'Z'=zoom  'M'=minimize
         */
        private void paintTitleButton(Graphics2D g, int x, int y, int sz,
                                      boolean hover, char type) {
            // Background gradient
            Color hoverTop, hoverBot;
            switch (type) {
                case 'X': hoverTop=new Color(220,80,80);  hoverBot=new Color(180,40,40);  break;
                case 'Z': hoverTop=new Color(80,180,80);  hoverBot=new Color(40,140,40);  break;
                default:  hoverTop=new Color(220,200,60); hoverBot=new Color(180,160,20); break;
            }
            Color gt = hover ? hoverTop : BeOSColors.TITLEBAR_TOP;
            Color gb = hover ? hoverBot : BeOSColors.TITLEBAR_BOTTOM;
            g.setPaint(new GradientPaint(x, y, gt, x, y+sz, gb));
            g.fillRect(x, y, sz, sz);

            // Raised bevel
            g.setColor(BeOSColors.TITLEBAR_HIGHLIGHT);
            g.drawLine(x,      y,      x+sz-2, y     );
            g.drawLine(x,      y,      x,      y+sz-2);
            g.setColor(BeOSColors.TITLEBAR_SHADOW);
            g.drawLine(x,      y+sz-1, x+sz-1, y+sz-1);
            g.drawLine(x+sz-1, y,      x+sz-1, y+sz-1);

            // Symbol
            g.setColor(Color.WHITE);
            Stroke old = g.getStroke();
            g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int m = 4;
            switch (type) {
                case 'X':
                    g.drawLine(x+m, y+m, x+sz-m-1, y+sz-m-1);
                    g.drawLine(x+sz-m-1, y+m, x+m, y+sz-m-1);
                    break;
                case 'Z':
                    g.drawRect(x+m, y+m, sz-2*m-1, sz-2*m-1);
                    break;
                case 'M':
                    g.drawLine(x+m, y+sz-m-1, x+sz-m-1, y+sz-m-1);
                    break;
            }
            g.setStroke(old);
        }
    }

    // ── Window border — raised emboss ─────────────────────────────────────

    public static class BeOSWindowBorder extends javax.swing.border.AbstractBorder {
        private static final int BW = BORDER;
        @Override public Insets getBorderInsets(Component c) { return new Insets(BW,BW,BW,BW); }
        @Override public Insets getBorderInsets(Component c, Insets i) { i.set(BW,BW,BW,BW); return i; }

        @Override
        public void paintBorder(Component c, Graphics g0, int x, int y, int w, int h) {
            Graphics2D g = (Graphics2D) g0.create();
            // Outermost — bright top/left, dark bottom/right
            g.setColor(BeOSColors.PANEL_HIGHLIGHT);
            g.drawLine(x,     y,     x+w-2, y    );
            g.drawLine(x,     y,     x,     y+h-2);
            g.setColor(BeOSColors.PANEL_DARK_SHADOW);
            g.drawLine(x,     y+h-1, x+w-1, y+h-1);
            g.drawLine(x+w-1, y,     x+w-1, y+h-1);
            // Middle layer
            g.setColor(new Color(220,220,220));
            g.drawLine(x+1,   y+1,   x+w-3, y+1  );
            g.drawLine(x+1,   y+1,   x+1,   y+h-3);
            g.setColor(BeOSColors.PANEL_SHADOW);
            g.drawLine(x+1,   y+h-2, x+w-2, y+h-2);
            g.drawLine(x+w-2, y+1,   x+w-2, y+h-2);
            // Inner dark frame
            g.setColor(BeOSColors.BORDER_OUTER);
            g.drawRect(x+2, y+2, w-5, h-5);
            // Inner highlight
            g.setColor(new Color(200,200,200));
            g.drawRect(x+3, y+3, w-7, h-7);
            g.dispose();
        }
    }
}
