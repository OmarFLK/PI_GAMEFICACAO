package frontend.theme;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

public final class ThemeToggleButton extends JButton implements ThemeAware {

    public ThemeToggleButton() {
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setToolTipText("Alternar tema claro e escuro");
        addActionListener(event -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            ThemeManager.toggleTheme(window);
        });
        applyTheme(ThemeManager.getCurrentPalette());
    }

    @Override
    public void applyTheme(ThemePalette palette) {
        setText(ThemeManager.isDarkMode() ? "Modo Claro" : "Modo Escuro");
        setForeground(palette.textPrimary());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        ThemePalette palette = ThemeManager.getCurrentPalette();
        g2.setColor(palette.cardBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        g2.setColor(getModel().isRollover() ? palette.primaryRed() : palette.border());
        g2.setStroke(new BasicStroke(getModel().isRollover() ? 2f : 1f));
        g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 20, 20);
        g2.dispose();

        super.paintComponent(graphics);
    }
}
