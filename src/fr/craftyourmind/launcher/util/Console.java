package fr.craftyourmind.launcher.util;

import fr.craftyourmind.launcher.MainAux;
import fr.craftyourmind.launcher.logger.*;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.swing.*;
import javax.swing.plaf.ScrollBarUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class Console implements ILogListener {
    public LogType logType = LogType.Minimal;

    private LogSource logSource = LogSource.ALL;

    public LogLevel logLevel = LogLevel.INFO;

    public Document displayAreaDoc;

    public Stage console;

    private SimpleAttributeSet RED = new SimpleAttributeSet();

    private SimpleAttributeSet YELLOW = new SimpleAttributeSet();

    private SimpleAttributeSet WHITE = new SimpleAttributeSet();

    public Console() throws IOException {
        FXMLLoader consoleFXML = new FXMLLoader(getClass().getResource("/fxml/Console.fxml"));
        Parent root = consoleFXML.load();
        this.console = new Stage();
        this.console.setTitle("CYM Console");
        console.getIcons().add(new Image("/images/icon.png"));
        console.setX(0);
        console.setY(0);
        console.setOnCloseRequest(event -> {
            System.out.println("Console is closing");
            if (!MainAux.getInstance().getStage().isShowing())
                System.exit(0);
        });
        this.console.setScene(new Scene(root, 800.0D, 400.0D));
        SwingNode swingNode = (SwingNode) consoleFXML.getNamespace().get("textarea");
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(800, 358));
        panel.setBackground(new Color(56, 56, 56));
        JTextPane displayArea = new JTextPane() {
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };
        displayArea.setBackground(new Color(56, 56, 56));
        displayArea.setEditable(false);
        displayArea.setPreferredSize(new Dimension(800, 358));
        this.displayAreaDoc = displayArea.getDocument();
        displayArea.setMargin(null);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setVerticalScrollBarPolicy(22);
        scrollPane.setHorizontalScrollBarPolicy(31);
        scrollPane.setPreferredSize(new Dimension(800, 358));
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(15, 2147483647));
        scrollPane.getVerticalScrollBar().setUI(newScrollBarUI());
        panel.add(scrollPane);
        swingNode.setContent(panel);
        StyleConstants.setForeground(this.RED, Color.RED);
        StyleConstants.setForeground(this.YELLOW, Color.YELLOW);
        StyleConstants.setForeground(this.WHITE, Color.WHITE);
    }

    public synchronized void refreshLogs() {
        try {
            this.displayAreaDoc.remove(0, this.displayAreaDoc.getLength());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        List<LogEntry> entries = Logger.getLogEntries();
        for (LogEntry entry : entries) {
            if ((this.logSource == LogSource.ALL || entry.source == this.logSource) && (this.logLevel == LogLevel.DEBUG || this.logLevel.includes(entry.level)))
                addMessage(entry, this.displayAreaDoc);
        }
        try {
            if (displayAreaDoc.getLength() > 0)
                this.displayAreaDoc.remove(0, 1);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private synchronized void addMessage(LogEntry entry, Document d) {
        AttributeSet color = null;
        switch (entry.level) {
            case ERROR:
                color = this.RED;
                break;
            case WARN:
                color = this.YELLOW;
            case INFO:
                color = this.WHITE;
                break;
        }
        try {
            d.insertString(d.getLength(), entry.toString(this.logType) + "\n ", color);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onLogEvent(LogEntry entry) {
        if ((this.logSource == LogSource.ALL || entry.source == this.logSource) && (this.logLevel == LogLevel.DEBUG || this.logLevel.includes(entry.level)))
            SwingUtilities.invokeLater(() -> addMessage(entry, this.displayAreaDoc));
    }

    private static ScrollBarUI newScrollBarUI() {
        return new BasicScrollBarUI() {
            public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
                super.paintThumb(g, c, thumbBounds);
                int tw = thumbBounds.width;
                int th = thumbBounds.height;
                g.translate(thumbBounds.x, thumbBounds.y);
                Graphics2D g2 = (Graphics2D) g;
                Paint gp = null;
                if (this.scrollbar.getOrientation() == 1)
                    gp = new GradientPaint(0.0F, 0.0F, new Color(230, 229, 229), tw, 0.0F, new Color(230, 229, 229));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, tw - 1, th - 1, 0, 0);
                g2.drawRoundRect(0, 0, tw - 1, th - 1, 0, 0);
            }

            public void paintTrack(Graphics g, JComponent c, Rectangle thumbBounds) {
                super.paintThumb(g, c, thumbBounds);
                int tw = thumbBounds.width;
                int th = thumbBounds.height;
                g.translate(thumbBounds.x, thumbBounds.y);
                Graphics2D g2 = (Graphics2D) g;
                Paint gp = null;
                if (this.scrollbar.getOrientation() == 1)
                    gp = new GradientPaint(0.0F, 0.0F, new Color(161, 159, 159), 3.0F, 0.0F, new Color(207, 205, 205));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, tw - 1, th - 1, 0, 0);
                g2.drawRoundRect(0, 0, tw - 1, th - 1, 0, 0);
            }

            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton jbutton = new JButton();
                jbutton.setPreferredSize(new Dimension(0, 0));
                jbutton.setMinimumSize(new Dimension(0, 0));
                jbutton.setMaximumSize(new Dimension(0, 0));
                return jbutton;
            }
        };
    }
}
