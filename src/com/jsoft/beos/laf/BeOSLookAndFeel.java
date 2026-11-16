package com.jsoft.beos.laf;

import com.jsoft.beos.util.BeOSFont;

import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.basic.BasicBorders;
import java.awt.*;

/**
 * BeOS 5 Look and Feel.
 *
 * Extends MetalLookAndFeel — ALL logic (input, focus, layout, keyboard,
 * painting pipeline) comes from Metal untouched.
 * We only override:
 *   - UIDefaults: colours, fonts, borders, insets
 *   - Button UI: subclass MetalButtonUI, only change paint() visuals
 *   - TextField border: swap Metal's border for our ClassicX 2-layer inset
 *   - RootPane UI: subclass MetalRootPaneUI, repaint title bar only
 */
public class BeOSLookAndFeel extends MetalLookAndFeel {

    @Override public String getName()        { return "BeOS 5"; }
    @Override public String getID()          { return "BeOS"; }
    @Override public String getDescription() { return "BeOS 5 Look and Feel by J Soft"; }

    @Override
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);   // Metal handles everything
        // Only override the specific delegates we customise
        String p = "com.jsoft.beos.ui.";
        table.put("ButtonUI",      p + "BeOSButtonUI");
        table.put("ToggleButtonUI",p + "BeOSButtonUI");
        table.put("CheckBoxUI",    p + "BeOSCheckBoxUI");
        table.put("RadioButtonUI", p + "BeOSRadioButtonUI");
        table.put("RootPaneUI",    p + "BeOSRootPaneUI");
    }

    @Override
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);  // Metal sets everything first

        FontUIResource  font      = new FontUIResource(BeOSFont.getDefaultFont());
        FontUIResource  boldFont  = new FontUIResource(BeOSFont.bold());

        // ── BeOS colours ──────────────────────────────────────────────────
        ColorUIResource panelBg   = new ColorUIResource(BeOSColors.PANEL_BG);
        ColorUIResource fieldBg   = new ColorUIResource(BeOSColors.FIELD_BG);
        ColorUIResource fg        = new ColorUIResource(BeOSColors.TEXT_FOREGROUND);
        ColorUIResource selBg     = new ColorUIResource(BeOSColors.TEXT_SELECTION_BG);
        ColorUIResource selFg     = new ColorUIResource(BeOSColors.TEXT_SELECTION_FG);
        ColorUIResource disabled  = new ColorUIResource(BeOSColors.TEXT_DISABLED);

        // ── ClassicX 2-layer inset border for ALL text inputs ─────────────
        // Outer: top/left=#A0A0A0  bottom/right=#FAFAFA
        // Inner: top/left=#808080  bottom/right=#DFDFDF
        // Padding: [2,4,2,4] inside the 2px border → total insets [4,6,4,6]
        BorderUIResource fieldBorder = new BorderUIResource(
                new com.jsoft.beos.ui.BeOSFieldBorder());

        // ── Flush all font and colour overrides ───────────────────────────
        Object[] overrides = {
            // Fonts — FontUIResource so UIManager.put(key, plainFont) wins
            "Button.font",                      font,
            "ToggleButton.font",                font,
            "CheckBox.font",                    font,
            "RadioButton.font",                 font,
            "Label.font",                       font,
            "ComboBox.font",                    font,
            "TextField.font",                   font,
            "FormattedTextField.font",          font,
            "TextArea.font",                    font,
            "EditorPane.font",                  font,
            "TextPane.font",                    font,
            "PasswordField.font",               font,
            "List.font",                        font,
            "Table.font",                       font,
            "TableHeader.font",                 font,
            "Tree.font",                        font,
            "TabbedPane.font",                  font,
            "TitledBorder.font",                font,
            "OptionPane.font",                  font,
            "MenuBar.font",                     font,
            "Menu.font",                        font,
            "MenuItem.font",                    font,
            "CheckBoxMenuItem.font",            font,
            "RadioButtonMenuItem.font",         font,
            "ToolTip.font",                     font,
            "Spinner.font",                     font,
            "ProgressBar.font",                 font,
            "Slider.font",                      font,
            "ToolBar.font",                     font,
            "InternalFrame.titleFont",          boldFont,

            // Panel / window background
            "Panel.background",                 panelBg,
            "OptionPane.background",            panelBg,
            "ToolBar.background",               panelBg,
            "ScrollPane.background",            panelBg,

            // Button colours (read by BeOSButtonUI via UIManager)
            "Button.background",                new ColorUIResource(BeOSColors.BUTTON_TOP),
            "Button.foreground",                fg,
            "Button.highlight",                 new ColorUIResource(Color.WHITE),
            "Button.shadow",                    new ColorUIResource(BeOSColors.PANEL_SHADOW),
            "Button.darkShadow",                new ColorUIResource(BeOSColors.PANEL_DARK_SHADOW),
            "Button.focus",                     new ColorUIResource(new Color(100,148,200)),
            "Button.select",                    new ColorUIResource(BeOSColors.BUTTON_PRESSED_TOP),
            "Button.margin",                    new InsetsUIResource(4, 8, 4, 8),

            "ToggleButton.background",          new ColorUIResource(BeOSColors.BUTTON_TOP),
            "ToggleButton.foreground",          fg,
            "ToggleButton.select",              new ColorUIResource(BeOSColors.BUTTON_PRESSED_TOP),
            "ToggleButton.margin",              new InsetsUIResource(4, 8, 4, 8),

            // CheckBox / Radio
            "CheckBox.background",              panelBg,
            "CheckBox.foreground",              fg,
            "RadioButton.background",           panelBg,
            "RadioButton.foreground",           fg,

            // Text fields — swap Metal border for ClassicX border
            "TextField.background",             fieldBg,
            "TextField.foreground",             fg,
            "TextField.selectionBackground",    selBg,
            "TextField.selectionForeground",    selFg,
            "TextField.inactiveBackground",     new ColorUIResource(new Color(230,230,230)),
            "TextField.inactiveForeground",     disabled,
            "TextField.border",                 fieldBorder,

            "FormattedTextField.background",    fieldBg,
            "FormattedTextField.foreground",    fg,
            "FormattedTextField.selectionBackground", selBg,
            "FormattedTextField.selectionForeground", selFg,
            "FormattedTextField.border",        fieldBorder,

            "PasswordField.background",         fieldBg,
            "PasswordField.foreground",         fg,
            "PasswordField.selectionBackground",selBg,
            "PasswordField.selectionForeground",selFg,
            "PasswordField.border",             fieldBorder,

            "TextArea.background",              fieldBg,
            "TextArea.foreground",              fg,
            "TextArea.selectionBackground",     selBg,
            "TextArea.selectionForeground",     selFg,
            "TextArea.inactiveBackground",      new ColorUIResource(new Color(230,230,230)),
            "TextArea.border",                  fieldBorder,

            "EditorPane.background",            fieldBg,
            "EditorPane.foreground",            fg,
            "EditorPane.selectionBackground",   selBg,
            "EditorPane.selectionForeground",   selFg,
            "EditorPane.border",                fieldBorder,

            "TextPane.background",              fieldBg,
            "TextPane.foreground",              fg,
            "TextPane.selectionBackground",     selBg,
            "TextPane.selectionForeground",     selFg,
            "TextPane.border",                  fieldBorder,

            // ComboBox — same field border
            "ComboBox.background",              fieldBg,
            "ComboBox.foreground",              fg,
            "ComboBox.selectionBackground",     selBg,
            "ComboBox.selectionForeground",     selFg,
            "ComboBox.disabledBackground",      new ColorUIResource(new Color(230,230,230)),
            "ComboBox.disabledForeground",      disabled,
            "ComboBox.border",                  fieldBorder,

            // List / Table / Tree
            "List.background",                  fieldBg,
            "List.foreground",                  fg,
            "List.selectionBackground",         selBg,
            "List.selectionForeground",         selFg,

            "Table.background",                 fieldBg,
            "Table.foreground",                 fg,
            "Table.selectionBackground",        selBg,
            "Table.selectionForeground",        selFg,
            "Table.gridColor",                  new ColorUIResource(BeOSColors.TABLE_GRID),
            "TableHeader.background",           panelBg,
            "TableHeader.foreground",           fg,

            "Tree.background",                  fieldBg,
            "Tree.foreground",                  fg,
            "Tree.selectionBackground",         selBg,
            "Tree.selectionForeground",         selFg,
            "Tree.textBackground",              fieldBg,
            "Tree.textForeground",              fg,

            // Menu
            "MenuBar.background",               panelBg,
            "MenuBar.foreground",               fg,
            "Menu.background",                  panelBg,
            "Menu.foreground",                  fg,
            "Menu.selectionBackground",         selBg,
            "Menu.selectionForeground",         selFg,
            "MenuItem.background",              panelBg,
            "MenuItem.foreground",              fg,
            "MenuItem.selectionBackground",     selBg,
            "MenuItem.selectionForeground",     selFg,
            "CheckBoxMenuItem.background",      panelBg,
            "CheckBoxMenuItem.foreground",      fg,
            "RadioButtonMenuItem.background",   panelBg,
            "RadioButtonMenuItem.foreground",   fg,
            "PopupMenu.background",             panelBg,
            "PopupMenu.foreground",             fg,

            // TabbedPane
            "TabbedPane.background",            panelBg,
            "TabbedPane.foreground",            fg,
            "TabbedPane.selected",              panelBg,
            "TabbedPane.contentAreaColor",      panelBg,

            // Scrollbar
            "ScrollBar.background",             new ColorUIResource(BeOSColors.SCROLL_TRACK),
            "ScrollBar.width",                  16,

            // ProgressBar
            "ProgressBar.background",           new ColorUIResource(BeOSColors.PROGRESS_BG),
            "ProgressBar.foreground",           new ColorUIResource(BeOSColors.PROGRESS_FILL),

            // ToolTip
            "ToolTip.background",               new ColorUIResource(BeOSColors.TOOLTIP_BG),
            "ToolTip.foreground",               new ColorUIResource(BeOSColors.TEXT_FOREGROUND),

            // InternalFrame
            "InternalFrame.activeTitleBackground",   new ColorUIResource(BeOSColors.TITLEBAR_TOP),
            "InternalFrame.activeTitleForeground",   new ColorUIResource(BeOSColors.TITLEBAR_TEXT),
            "InternalFrame.inactiveTitleBackground", new ColorUIResource(BeOSColors.TITLEBAR_INACTIVE_TOP),
            "InternalFrame.inactiveTitleForeground", new ColorUIResource(BeOSColors.TITLEBAR_INACTIVE_TEXT),
            "InternalFrame.titleFont",               boldFont,

            // LCD text antialiasing
            "swing.aatext", Boolean.TRUE,
        };

        table.putDefaults(overrides);
    }
}
