import com.jsoft.beos.laf.BeOSLookAndFeel;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

/**
 * BeOSDemo — showcases all major Swing components with the BeOS L&F.
 *
 * Run:  java -cp BeOSLAF.jar BeOSDemo
 */
public class BeOSDemo {

    public static void main(String[] args) throws Exception {
        // ── Apply the LAF before any UI is created ──────────────────────
        UIManager.setLookAndFeel(new BeOSLookAndFeel());
        // Enable L&F window decorations (title bar)
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        SwingUtilities.invokeLater(BeOSDemo::createAndShow);
    }

    private static void createAndShow() {
        JFrame frame = new JFrame("ShopKeeperQB — BeOS 5 Look and Feel Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(820, 620);
        frame.setLocationRelativeTo(null);

        // ── Menu bar ────────────────────────────────────────────────────
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu   = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.add(new JMenuItem("New")).setAccelerator(KeyStroke.getKeyStroke("ctrl N"));
        fileMenu.add(new JMenuItem("Open...")).setAccelerator(KeyStroke.getKeyStroke("ctrl O"));
        fileMenu.add(new JMenuItem("Save")).setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        fileMenu.addSeparator();
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        JMenu editMenu = new JMenu("Edit");
        editMenu.add(new JMenuItem("Cut")).setAccelerator(KeyStroke.getKeyStroke("ctrl X"));
        editMenu.add(new JMenuItem("Copy")).setAccelerator(KeyStroke.getKeyStroke("ctrl C"));
        editMenu.add(new JMenuItem("Paste")).setAccelerator(KeyStroke.getKeyStroke("ctrl V"));

        JMenu viewMenu = new JMenu("View");
        JCheckBoxMenuItem cbItem = new JCheckBoxMenuItem("Show Toolbar", true);
        viewMenu.add(cbItem);
        JRadioButtonMenuItem rb1 = new JRadioButtonMenuItem("Small Icons", true);
        JRadioButtonMenuItem rb2 = new JRadioButtonMenuItem("Large Icons");
        ButtonGroup bg = new ButtonGroup(); bg.add(rb1); bg.add(rb2);
        viewMenu.add(rb1); viewMenu.add(rb2);

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new JMenuItem("About BeOS LAF..."));

        menuBar.add(fileMenu); menuBar.add(editMenu); menuBar.add(viewMenu); menuBar.add(helpMenu);
        frame.setJMenuBar(menuBar);

        // ── Toolbar ─────────────────────────────────────────────────────
        JToolBar toolbar = new JToolBar();
        for (String lbl : new String[]{"New", "Open", "Save", "|", "Cut", "Copy", "Paste"}) {
            if (lbl.equals("|")) { toolbar.addSeparator(); continue; }
            JButton btn = new JButton(lbl);
            btn.setToolTipText(lbl);
            toolbar.add(btn);
        }
        frame.add(toolbar, BorderLayout.NORTH);

        // ── Main tabbed pane ─────────────────────────────────────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Billing",   makeBillingPanel());
        tabs.addTab("Inventory", makeInventoryPanel());
        tabs.addTab("Settings",  makeSettingsPanel());
        frame.add(tabs, BorderLayout.CENTER);

        // ── Status bar ───────────────────────────────────────────────────
        JPanel status = new JPanel(new BorderLayout());
        status.setBorder(new MatteBorder(1, 0, 0, 0, UIManager.getColor("controlShadow")));
        JLabel statusLabel = new JLabel("  Ready — BeOS 5 Look and Feel by J Soft");
        statusLabel.setFont(UIManager.getFont("defaultFont"));
        status.add(statusLabel, BorderLayout.WEST);
        JLabel clock = new JLabel("ShopKeeperQB v1.0  ");
        clock.setFont(UIManager.getFont("defaultFont"));
        status.add(clock, BorderLayout.EAST);
        frame.add(status, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // ── Billing tab ────────────────────────────────────────────────────────

    static JPanel makeBillingPanel() {
        JPanel p = new JPanel(new BorderLayout(6, 6));
        p.setBorder(new EmptyBorder(8, 8, 8, 8));

        // Customer + bill info row
        JPanel top = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 4, 3, 4);
        c.anchor = GridBagConstraints.WEST;

        addField(top, c, 0, "Customer Name:", new JTextField(18));
        addField(top, c, 1, "Phone:", new JTextField(12));
        addField(top, c, 2, "Bill No:", new JTextField(8));

        JComboBox<String> payMode = new JComboBox<>(new String[]{"Cash","Card","UPI","Credit"});
        addField(top, c, 3, "Payment:", payMode);

        p.add(top, BorderLayout.NORTH);

        // Product table
        String[] cols = {"#","Product","Qty","Unit","Rate","Disc%","Total"};
        Object[][] rows = {
            {1,"Rice (25 kg bag)",  2,"Bag", "850.00","0","1700.00"},
            {2,"Cooking Oil 1L",   5,"Pcs", "120.00","2", "588.00"},
            {3,"Sugar 1 kg",       3,"Pcs",  "45.00","0", "135.00"},
            {4,"Wheat Flour 5 kg", 2,"Bag", "220.00","5", "418.00"},
        };
        JTable table = new JTable(rows, cols);
        table.setRowSelectionAllowed(true);
        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(0, 200));
        p.add(sp, BorderLayout.CENTER);

        // Totals panel
        JPanel totals = new JPanel(new GridBagLayout());
        totals.setBorder(new TitledBorder("Settlement"));
        GridBagConstraints tc = new GridBagConstraints();
        tc.insets = new Insets(2,4,2,4);
        tc.anchor = GridBagConstraints.EAST;

        String[][] tFields = {
            {"Subtotal:","2841.00"},{"Discount:","71.00"},{"Tax (GST):","99.44"},{"Grand Total:","2869.44"}
        };
        for (int i = 0; i < tFields.length; i++) {
            tc.gridx=0; tc.gridy=i; totals.add(new JLabel(tFields[i][0]), tc);
            tc.gridx=1; JTextField tf = new JTextField(tFields[i][1], 10); tf.setHorizontalAlignment(SwingConstants.RIGHT); totals.add(tf, tc);
        }
        tc.gridx=0; tc.gridy=tFields.length; tc.gridwidth=2; tc.fill=GridBagConstraints.HORIZONTAL;
        JProgressBar pb = new JProgressBar(0,100); pb.setValue(72); pb.setStringPainted(true); pb.setString("Payment: 72%");
        totals.add(pb, tc);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        JButton saveBtn = new JButton("Save Bill"); saveBtn.setFont(UIManager.getFont("Button.font"));
        JButton holdBtn = new JButton("Hold");
        JButton estBtn  = new JButton("Estimate");
        JButton clearBtn= new JButton("Clear");
        saveBtn.setToolTipText("Save and print bill (Ctrl+S)");
        btnRow.add(clearBtn); btnRow.add(estBtn); btnRow.add(holdBtn); btnRow.add(saveBtn);

        JPanel south = new JPanel(new BorderLayout());
        south.add(totals, BorderLayout.WEST);
        south.add(btnRow, BorderLayout.SOUTH);
        p.add(south, BorderLayout.SOUTH);

        return p;
    }

    // ── Inventory tab ──────────────────────────────────────────────────────

    static JPanel makeInventoryPanel() {
        JPanel p = new JPanel(new BorderLayout(6,6));
        p.setBorder(new EmptyBorder(8,8,8,8));

        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
        search.add(new JLabel("Search:"));
        search.add(new JTextField(20));
        search.add(new JButton("Find"));
        search.add(new JButton("Add Product"));
        search.add(new JButton("Edit"));
        p.add(search, BorderLayout.NORTH);

        String[] cols = {"ID","Product Name","Category","Opening","Current","Rate","MRP"};
        Object[][] rows = {
            {"P001","Basmati Rice 1kg","Grains",  100, 78,  "62.00","72.00"},
            {"P002","Sunflower Oil 1L","Oil",       50, 35, "102.00","120.00"},
            {"P003","Wheat Flour 5kg","Grains",     30, 22, "210.00","250.00"},
            {"P004","Toor Dal 500g",  "Pulses",     80, 61,  "80.00", "95.00"},
            {"P005","Amul Butter 100g","Dairy",     60, 48,  "52.00", "58.00"},
        };
        JTable table = new JTable(rows, cols);
        JScrollPane sp = new JScrollPane(table);

        JPanel detail = new JPanel(new GridBagLayout());
        detail.setBorder(new TitledBorder("Product Details"));
        detail.setPreferredSize(new Dimension(220, 0));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3,4,3,4); c.anchor = GridBagConstraints.WEST;
        String[] lbls = {"Name:","Category:","Opening Stock:","Current Stock:","Rate:","MRP:"};
        for (int i=0;i<lbls.length;i++) {
            c.gridx=0;c.gridy=i; detail.add(new JLabel(lbls[i]),c);
            c.gridx=1; detail.add(new JTextField(10),c);
        }
        c.gridx=0;c.gridy=lbls.length;c.gridwidth=2;c.fill=GridBagConstraints.HORIZONTAL;
        detail.add(new JButton("Update Stock"),c);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sp, detail);
        split.setDividerLocation(540);
        p.add(split, BorderLayout.CENTER);

        return p;
    }

    // ── Settings tab ──────────────────────────────────────────────────────

    static JPanel makeSettingsPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new EmptyBorder(10,10,10,10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4); c.anchor = GridBagConstraints.WEST;

        // Shop info
        c.gridx=0;c.gridy=0;c.gridwidth=2; p.add(titledSep("Shop Information"), c); c.gridwidth=1;
        addField(p,c,1,"Shop Name:",  new JTextField("J Soft Stores", 22));
        addField(p,c,2,"Address:",    new JTextField("123 Main Street, Chennai", 22));
        addField(p,c,3,"Phone:",      new JTextField("+91 9876543210", 14));
        addField(p,c,4,"GSTIN:",      new JTextField("33ABCDE1234F1Z5", 16));

        // Billing config
        c.gridx=0;c.gridy=5;c.gridwidth=2; p.add(titledSep("Billing Options"), c); c.gridwidth=1;
        c.gridx=0;c.gridy=6; p.add(new JCheckBox("Enable GST", true), c);
        c.gridx=1; p.add(new JCheckBox("Print after save", true), c);
        c.gridx=0;c.gridy=7; p.add(new JCheckBox("Auto-generate Bill No.", true), c);
        c.gridx=1; p.add(new JCheckBox("Stock validation on sale", true), c);

        c.gridx=0;c.gridy=8;
        p.add(new JLabel("Default GST Rate:"), c);
        c.gridx=1; JSpinner gstSpinner = new JSpinner(new SpinnerNumberModel(18, 0, 28, 1)); p.add(gstSpinner, c);

        // Slider demo
        c.gridx=0;c.gridy=9;c.gridwidth=2; p.add(titledSep("UI Settings"), c); c.gridwidth=1;
        c.gridx=0;c.gridy=10; p.add(new JLabel("Font Size:"), c);
        c.gridx=1; JSlider sl = new JSlider(8,18,11); sl.setMajorTickSpacing(2); sl.setPaintTicks(true); sl.setPaintLabels(true); p.add(sl, c);

        c.gridx=0;c.gridy=11;c.gridwidth=2;c.fill=GridBagConstraints.NONE;
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT,6,0));
        btns.add(new JButton("Cancel")); btns.add(new JButton("Apply")); btns.add(new JButton("Save Settings"));
        p.add(btns, c);

        return p;
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private static void addField(JPanel p, GridBagConstraints c, int row, String label, JComponent field) {
        c.gridx=0; c.gridy=row; c.fill=GridBagConstraints.NONE;
        p.add(new JLabel(label), c);
        c.gridx=1; c.fill=GridBagConstraints.HORIZONTAL;
        p.add(field, c);
    }

    private static JSeparator titledSep(String text) {
        JSeparator sep = new JSeparator();
        // Simple label instead (TitledBorder on a panel)
        return sep; // caller uses TitledBorder via TitledBorder(text)
    }

    private static JLabel titledLabel(String text) {
        JLabel l = new JLabel("<html><b>" + text + "</b></html>");
        l.setForeground(new Color(60,60,60));
        return l;
    }
}
