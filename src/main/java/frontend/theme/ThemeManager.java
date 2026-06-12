package frontend.theme;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;
import javax.swing.text.JTextComponent;

import frontend.util.AppTheme;

public final class ThemeManager {

    private static final String PREFERENCE_KEY = "theme-mode";
    private static final Preferences PREFERENCES = Preferences.userNodeForPackage(ThemeManager.class);
    private static final ThemePalette LIGHT_PALETTE = ThemePalette.light();
    private static final ThemePalette DARK_PALETTE = ThemePalette.dark();

    private static ThemeMode currentMode = loadPreference();

    private ThemeManager() {
    }

    public static void initialize() {
        applyUiDefaults();
    }

    public static ThemePalette getCurrentPalette() {
        return currentMode == ThemeMode.DARK ? DARK_PALETTE : LIGHT_PALETTE;
    }

    public static ThemeMode getCurrentMode() {
        return currentMode;
    }

    public static boolean isDarkMode() {
        return currentMode == ThemeMode.DARK;
    }

    public static void toggleTheme(Window sourceWindow) {
        currentMode = isDarkMode() ? ThemeMode.LIGHT : ThemeMode.DARK;
        savePreference();
        applyUiDefaults();

        for (Window window : Window.getWindows()) {
            if (window.isDisplayable() || window == sourceWindow) {
                applyTheme(window);
            }
        }
    }

    public static void applyTheme(Component component) {
        if (component == null) {
            return;
        }

        if (component instanceof Window window) {
            SwingUtilities.updateComponentTreeUI(window);
        }

        applyRecursively(component);

        if (component instanceof JComponent swingComponent) {
            swingComponent.revalidate();
        } else if (component instanceof Container container) {
            container.validate();
        }
        component.repaint();
    }

    public static Color resolveBackground(Color original) {
        if (original == null || original.getAlpha() == 0) {
            return original;
        }

        ThemePalette palette = getCurrentPalette();
        if (matchesAny(original, LIGHT_PALETTE.background(), DARK_PALETTE.background())) {
            return withAlpha(palette.background(), original.getAlpha());
        }
        if (matchesAny(original, LIGHT_PALETTE.surface(), DARK_PALETTE.surface())) {
            return withAlpha(palette.surface(), original.getAlpha());
        }
        if (matchesAny(original, LIGHT_PALETTE.cardBackground(), DARK_PALETTE.cardBackground())) {
            return withAlpha(palette.cardBackground(), original.getAlpha());
        }
        if (matchesAny(original, LIGHT_PALETTE.inputBackground(), DARK_PALETTE.inputBackground())) {
            return withAlpha(palette.inputBackground(), original.getAlpha());
        }
        if (matchesAny(original, LIGHT_PALETTE.neutralButtonBackground(), DARK_PALETTE.neutralButtonBackground())) {
            return withAlpha(palette.neutralButtonBackground(), original.getAlpha());
        }
        if (matchesAny(original, LIGHT_PALETTE.selectionBackground(), DARK_PALETTE.selectionBackground())) {
            return withAlpha(palette.selectionBackground(), original.getAlpha());
        }
        if (matchesAny(original, LIGHT_PALETTE.softRed(), DARK_PALETTE.softRed())) {
            return withAlpha(palette.softRed(), original.getAlpha());
        }
        if (matchesAny(original, AppTheme.RED, AppTheme.PRIMARY_HIGHLIGHT,
                LIGHT_PALETTE.primaryRed(), DARK_PALETTE.primaryRed())) {
            return withAlpha(palette.primaryRed(), original.getAlpha());
        }
        if (matchesAny(original, AppTheme.ERROR_HIGHLIGHT,
                LIGHT_PALETTE.dangerRed(), DARK_PALETTE.dangerRed())) {
            return withAlpha(palette.dangerRed(), original.getAlpha());
        }
        if (matchesAny(original, AppTheme.NEUTRAL_DARK,
                LIGHT_PALETTE.neutralChart(), DARK_PALETTE.neutralChart())) {
            return withAlpha(palette.neutralChart(), original.getAlpha());
        }
        if (matchesAny(original, LIGHT_PALETTE.chartTrack(), DARK_PALETTE.chartTrack())) {
            return withAlpha(palette.chartTrack(), original.getAlpha());
        }
        if (matchesAny(original, LIGHT_PALETTE.successSurface(), DARK_PALETTE.successSurface())) {
            return withAlpha(palette.successSurface(), original.getAlpha());
        }
        if (matchesAny(original, LIGHT_PALETTE.dangerSurface(), DARK_PALETTE.dangerSurface())) {
            return withAlpha(palette.dangerSurface(), original.getAlpha());
        }
        if (matchesAny(original, AppTheme.SOFT_GRAY, LIGHT_PALETTE.mutedSurface(), DARK_PALETTE.mutedSurface())) {
            return withAlpha(palette.mutedSurface(), original.getAlpha());
        }
        if (matchesAny(original, AppTheme.PRIMARY_DARK, AppTheme.SECONDARY_DARK,
                LIGHT_PALETTE.buttonBackground(), DARK_PALETTE.buttonBackground())) {
            return withAlpha(palette.buttonBackground(), original.getAlpha());
        }
        if (matchesAny(original, AppTheme.DIFFICULTY_EASY, LIGHT_PALETTE.difficultyEasy(), DARK_PALETTE.difficultyEasy())) {
            return withAlpha(palette.difficultyEasy(), original.getAlpha());
        }
        if (matchesAny(original, AppTheme.DIFFICULTY_MEDIUM, LIGHT_PALETTE.difficultyMedium(), DARK_PALETTE.difficultyMedium())) {
            return withAlpha(palette.difficultyMedium(), original.getAlpha());
        }
        if (matchesAny(original, AppTheme.DIFFICULTY_HARD, LIGHT_PALETTE.difficultyHard(), DARK_PALETTE.difficultyHard())) {
            return withAlpha(palette.difficultyHard(), original.getAlpha());
        }
        if (matchesAny(original, new Color(246, 246, 246), new Color(248, 248, 248),
                new Color(241, 246, 250), new Color(225, 235, 245))) {
            return withAlpha(isDarkMode() ? palette.inputBackground() : original, original.getAlpha());
        }
        return original;
    }

    public static Color resolveForeground(Color original) {
        if (original == null || original.getAlpha() == 0) {
            return original;
        }

        ThemePalette palette = getCurrentPalette();
        if (matchesAny(original, AppTheme.TEXT, AppTheme.PRIMARY_DARK, AppTheme.SECONDARY_DARK,
                LIGHT_PALETTE.textPrimary(), DARK_PALETTE.textPrimary())) {
            return withAlpha(palette.textPrimary(), original.getAlpha());
        }
        if (matchesAny(original, AppTheme.TEXT_MUTED, LIGHT_PALETTE.textSecondary(), DARK_PALETTE.textSecondary(),
                Color.GRAY, new Color(78, 78, 78), new Color(110, 110, 110), new Color(133, 145, 157))) {
            return withAlpha(palette.textSecondary(), original.getAlpha());
        }
        if (matchesAny(original, AppTheme.BORDER, LIGHT_PALETTE.border(), DARK_PALETTE.border(),
                new Color(200, 200, 200), new Color(200, 210, 220),
                new Color(224, 224, 224), new Color(226, 226, 226))) {
            return withAlpha(palette.border(), original.getAlpha());
        }
        if (matchesAny(original, AppTheme.RED, AppTheme.PRIMARY_HIGHLIGHT,
                LIGHT_PALETTE.primaryRed(), DARK_PALETTE.primaryRed())) {
            return withAlpha(palette.primaryRed(), original.getAlpha());
        }
        if (matchesAny(original, Color.RED, AppTheme.ERROR_HIGHLIGHT,
                LIGHT_PALETTE.dangerRed(), DARK_PALETTE.dangerRed())) {
            return withAlpha(palette.dangerRed(), original.getAlpha());
        }
        if (matchesAny(original, AppTheme.RANK_FIRST, LIGHT_PALETTE.topOneGold(), DARK_PALETTE.topOneGold())) {
            return withAlpha(palette.topOneGold(), original.getAlpha());
        }
        if (matchesAny(original, LIGHT_PALETTE.success(), DARK_PALETTE.success())) {
            return withAlpha(palette.success(), original.getAlpha());
        }
        if (matchesAny(original, AppTheme.NEUTRAL_DARK,
                LIGHT_PALETTE.neutralChart(), DARK_PALETTE.neutralChart())) {
            return withAlpha(palette.neutralChart(), original.getAlpha());
        }
        return original;
    }

    private static void applyRecursively(Component component) {
        ThemePalette palette = getCurrentPalette();

        if (component instanceof ThemeAware aware) {
            aware.applyTheme(palette);
        } else {
            applyStandardComponent(component, palette);
        }

        if (component instanceof Container container) {
            for (Component child : container.getComponents()) {
                applyRecursively(child);
            }
        }
    }

    private static void applyStandardComponent(Component component, ThemePalette palette) {
        if (component instanceof JTextComponent textComponent) {
            textComponent.setBackground(palette.inputBackground());
            textComponent.setForeground(palette.textPrimary());
            textComponent.setCaretColor(palette.textPrimary());
            textComponent.setSelectionColor(palette.primaryRed());
            textComponent.setSelectedTextColor(palette.buttonText());
        } else if (component instanceof JTable table) {
            table.setBackground(palette.cardBackground());
            table.setForeground(palette.textPrimary());
            table.setGridColor(palette.border());
            table.setSelectionBackground(palette.selectionBackground());
            table.setSelectionForeground(palette.textPrimary());
            applyTableHeader(table.getTableHeader(), palette);
        } else if (component instanceof JTableHeader header) {
            applyTableHeader(header, palette);
        } else if (component instanceof JComboBox<?> comboBox) {
            comboBox.setBackground(palette.inputBackground());
            comboBox.setForeground(palette.textPrimary());
        } else if (component instanceof JList<?> list) {
            list.setBackground(palette.inputBackground());
            list.setForeground(palette.textPrimary());
            list.setSelectionBackground(palette.selectionBackground());
            list.setSelectionForeground(palette.textPrimary());
        } else if (component instanceof JMenuBar menuBar) {
            menuBar.setBackground(palette.surface());
            menuBar.setForeground(palette.textPrimary());
        } else if (component instanceof JMenu || component instanceof JMenuItem) {
            component.setBackground(palette.surface());
            component.setForeground(palette.textPrimary());
        } else if (component instanceof AbstractButton button) {
            Color originalBackground = button.getBackground();
            button.setBackground(resolveBackground(originalBackground));
            if (matchesAny(originalBackground,
                    AppTheme.PRIMARY_DARK,
                    AppTheme.SECONDARY_DARK,
                    AppTheme.RED,
                    AppTheme.ERROR_HIGHLIGHT,
                    LIGHT_PALETTE.buttonBackground(),
                    DARK_PALETTE.buttonBackground(),
                    LIGHT_PALETTE.primaryRed(),
                    DARK_PALETTE.primaryRed(),
                    LIGHT_PALETTE.dangerRed(),
                    DARK_PALETTE.dangerRed())) {
                button.setForeground(palette.buttonText());
            } else {
                button.setForeground(resolveForeground(button.getForeground()));
            }
        } else {
            if (component.isBackgroundSet() && component.getBackground() != null) {
                component.setBackground(resolveBackground(component.getBackground()));
            }
            if (component.isForegroundSet() && component.getForeground() != null) {
                component.setForeground(resolveForeground(component.getForeground()));
            }
        }

        if (component instanceof JScrollPane scrollPane) {
            scrollPane.setBackground(palette.cardBackground());
            scrollPane.getViewport().setBackground(palette.cardBackground());
        } else if (component instanceof JViewport viewport) {
            viewport.setBackground(palette.cardBackground());
        }

        if (component instanceof JComponent swingComponent && swingComponent.getBorder() != null) {
            swingComponent.setBorder(resolveBorder(swingComponent.getBorder()));
        }
    }

    private static void applyTableHeader(JTableHeader header, ThemePalette palette) {
        if (header == null) {
            return;
        }
        header.setBackground(palette.neutralChart());
        header.setForeground(isDarkMode() ? palette.background() : palette.surface());
    }

    private static Border resolveBorder(Border border) {
        if (border instanceof LineBorder lineBorder) {
            return new LineBorder(
                    resolveForeground(lineBorder.getLineColor()),
                    lineBorder.getThickness(),
                    lineBorder.getRoundedCorners());
        }
        if (border instanceof MatteBorder matteBorder && matteBorder.getMatteColor() != null) {
            java.awt.Insets insets = matteBorder.getBorderInsets();
            return BorderFactory.createMatteBorder(
                    insets.top,
                    insets.left,
                    insets.bottom,
                    insets.right,
                    resolveForeground(matteBorder.getMatteColor()));
        }
        if (border instanceof CompoundBorder compoundBorder) {
            return new CompoundBorder(
                    resolveBorder(compoundBorder.getOutsideBorder()),
                    resolveBorder(compoundBorder.getInsideBorder()));
        }
        if (border instanceof TitledBorder titledBorder) {
            Border resolvedBase = titledBorder.getBorder() == null ? null : resolveBorder(titledBorder.getBorder());
            return BorderFactory.createTitledBorder(
                    resolvedBase,
                    titledBorder.getTitle(),
                    titledBorder.getTitleJustification(),
                    titledBorder.getTitlePosition(),
                    titledBorder.getTitleFont(),
                    getCurrentPalette().textSecondary());
        }
        return border;
    }

    private static void applyUiDefaults() {
        ThemePalette palette = getCurrentPalette();
        UIManager.put("Panel.background", palette.surface());
        UIManager.put("Label.foreground", palette.textPrimary());
        UIManager.put("Button.background", palette.neutralButtonBackground());
        UIManager.put("Button.foreground", palette.textPrimary());
        UIManager.put("Button.disabledText", palette.textSecondary());
        UIManager.put("RadioButton.background", palette.surface());
        UIManager.put("RadioButton.foreground", palette.textPrimary());
        UIManager.put("CheckBox.background", palette.surface());
        UIManager.put("CheckBox.foreground", palette.textPrimary());
        UIManager.put("TextField.background", palette.inputBackground());
        UIManager.put("TextField.foreground", palette.textPrimary());
        UIManager.put("TextField.inactiveBackground", palette.mutedSurface());
        UIManager.put("TextField.inactiveForeground", palette.textSecondary());
        UIManager.put("PasswordField.background", palette.inputBackground());
        UIManager.put("PasswordField.foreground", palette.textPrimary());
        UIManager.put("PasswordField.inactiveBackground", palette.mutedSurface());
        UIManager.put("PasswordField.inactiveForeground", palette.textSecondary());
        UIManager.put("TextArea.background", palette.inputBackground());
        UIManager.put("TextArea.foreground", palette.textPrimary());
        UIManager.put("TextArea.inactiveBackground", palette.mutedSurface());
        UIManager.put("TextArea.inactiveForeground", palette.textSecondary());
        UIManager.put("ComboBox.background", palette.inputBackground());
        UIManager.put("ComboBox.foreground", palette.textPrimary());
        UIManager.put("Table.background", palette.cardBackground());
        UIManager.put("Table.foreground", palette.textPrimary());
        UIManager.put("Table.selectionBackground", palette.selectionBackground());
        UIManager.put("Table.selectionForeground", palette.textPrimary());
        UIManager.put("TableHeader.background", palette.neutralChart());
        UIManager.put("TableHeader.foreground", isDarkMode() ? palette.background() : palette.surface());
        UIManager.put("OptionPane.background", palette.surface());
        UIManager.put("OptionPane.messageForeground", palette.textPrimary());
        UIManager.put("ScrollPane.background", palette.cardBackground());
        UIManager.put("Viewport.background", palette.cardBackground());
        UIManager.put("ToolTip.background", palette.cardBackground());
        UIManager.put("ToolTip.foreground", palette.textPrimary());
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(palette.border()));
    }

    private static ThemeMode loadPreference() {
        try {
            return ThemeMode.valueOf(PREFERENCES.get(PREFERENCE_KEY, ThemeMode.LIGHT.name()));
        } catch (IllegalArgumentException | SecurityException exception) {
            return ThemeMode.LIGHT;
        }
    }

    private static void savePreference() {
        try {
            PREFERENCES.put(PREFERENCE_KEY, currentMode.name());
            PREFERENCES.flush();
        } catch (BackingStoreException | SecurityException ignored) {
            // The theme still works if the environment blocks Preferences.
        }
    }

    private static boolean matchesAny(Color color, Color... candidates) {
        for (Color candidate : candidates) {
            if (candidate != null
                    && color.getRed() == candidate.getRed()
                    && color.getGreen() == candidate.getGreen()
                    && color.getBlue() == candidate.getBlue()) {
                return true;
            }
        }
        return false;
    }

    private static Color withAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }
}
