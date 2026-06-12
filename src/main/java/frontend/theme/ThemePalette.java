package frontend.theme;

import java.awt.Color;

import frontend.util.AppTheme;

public final class ThemePalette {

    private final Color background;
    private final Color surface;
    private final Color cardBackground;
    private final Color textPrimary;
    private final Color textSecondary;
    private final Color border;
    private final Color primaryRed;
    private final Color primaryRedHover;
    private final Color softRed;
    private final Color dangerRed;
    private final Color topOneGold;
    private final Color neutralChart;
    private final Color inputBackground;
    private final Color buttonBackground;
    private final Color buttonText;
    private final Color neutralButtonBackground;
    private final Color selectionBackground;
    private final Color chartTrack;
    private final Color success;
    private final Color successSurface;
    private final Color dangerSurface;
    private final Color mutedSurface;
    private final Color avatarBackground;
    private final Color avatarShape;
    private final Color difficultyEasy;
    private final Color difficultyMedium;
    private final Color difficultyHard;
    private final Color shadow;

    private ThemePalette(
            Color background,
            Color surface,
            Color cardBackground,
            Color textPrimary,
            Color textSecondary,
            Color border,
            Color primaryRed,
            Color primaryRedHover,
            Color softRed,
            Color dangerRed,
            Color topOneGold,
            Color neutralChart,
            Color inputBackground,
            Color buttonBackground,
            Color buttonText,
            Color neutralButtonBackground,
            Color selectionBackground,
            Color chartTrack,
            Color success,
            Color successSurface,
            Color dangerSurface,
            Color mutedSurface,
            Color avatarBackground,
            Color avatarShape,
            Color difficultyEasy,
            Color difficultyMedium,
            Color difficultyHard,
            Color shadow) {
        this.background = background;
        this.surface = surface;
        this.cardBackground = cardBackground;
        this.textPrimary = textPrimary;
        this.textSecondary = textSecondary;
        this.border = border;
        this.primaryRed = primaryRed;
        this.primaryRedHover = primaryRedHover;
        this.softRed = softRed;
        this.dangerRed = dangerRed;
        this.topOneGold = topOneGold;
        this.neutralChart = neutralChart;
        this.inputBackground = inputBackground;
        this.buttonBackground = buttonBackground;
        this.buttonText = buttonText;
        this.neutralButtonBackground = neutralButtonBackground;
        this.selectionBackground = selectionBackground;
        this.chartTrack = chartTrack;
        this.success = success;
        this.successSurface = successSurface;
        this.dangerSurface = dangerSurface;
        this.mutedSurface = mutedSurface;
        this.avatarBackground = avatarBackground;
        this.avatarShape = avatarShape;
        this.difficultyEasy = difficultyEasy;
        this.difficultyMedium = difficultyMedium;
        this.difficultyHard = difficultyHard;
        this.shadow = shadow;
    }

    public static ThemePalette light() {
        return new ThemePalette(
                AppTheme.BACKGROUND,
                AppTheme.SURFACE,
                AppTheme.SURFACE,
                AppTheme.TEXT,
                AppTheme.TEXT_MUTED,
                AppTheme.BORDER,
                AppTheme.RED,
                AppTheme.RED_HOVER,
                AppTheme.RED_SOFT,
                AppTheme.ERROR_HIGHLIGHT,
                AppTheme.RANK_FIRST,
                AppTheme.NEUTRAL_DARK,
                AppTheme.SURFACE,
                AppTheme.PRIMARY_DARK,
                Color.WHITE,
                new Color(252, 252, 252),
                AppTheme.RED_SOFT,
                new Color(240, 244, 248),
                new Color(40, 167, 69),
                new Color(220, 252, 231),
                new Color(254, 226, 226),
                AppTheme.SOFT_GRAY,
                new Color(230, 235, 240),
                new Color(166, 176, 186),
                AppTheme.DIFFICULTY_EASY,
                AppTheme.DIFFICULTY_MEDIUM,
                AppTheme.DIFFICULTY_HARD,
                new Color(0, 0, 0, 24));
    }

    public static ThemePalette dark() {
        return new ThemePalette(
                new Color(17, 17, 17),
                new Color(26, 26, 26),
                new Color(31, 31, 31),
                new Color(249, 250, 251),
                new Color(209, 213, 219),
                new Color(55, 65, 81),
                new Color(248, 113, 113),
                new Color(252, 165, 165),
                new Color(59, 24, 28),
                new Color(251, 113, 133),
                new Color(251, 191, 36),
                new Color(229, 231, 235),
                new Color(38, 38, 38),
                new Color(248, 113, 113),
                new Color(17, 24, 39),
                new Color(38, 38, 38),
                new Color(63, 29, 34),
                new Color(55, 65, 81),
                new Color(74, 222, 128),
                new Color(20, 83, 45),
                new Color(127, 29, 29),
                new Color(45, 45, 45),
                new Color(42, 47, 54),
                new Color(156, 163, 175),
                new Color(127, 29, 29),
                new Color(185, 28, 28),
                new Color(239, 68, 68),
                new Color(0, 0, 0, 90));
    }

    public Color background() {
        return background;
    }

    public Color surface() {
        return surface;
    }

    public Color cardBackground() {
        return cardBackground;
    }

    public Color textPrimary() {
        return textPrimary;
    }

    public Color textSecondary() {
        return textSecondary;
    }

    public Color border() {
        return border;
    }

    public Color primaryRed() {
        return primaryRed;
    }

    public Color primaryRedHover() {
        return primaryRedHover;
    }

    public Color softRed() {
        return softRed;
    }

    public Color dangerRed() {
        return dangerRed;
    }

    public Color topOneGold() {
        return topOneGold;
    }

    public Color neutralChart() {
        return neutralChart;
    }

    public Color inputBackground() {
        return inputBackground;
    }

    public Color buttonBackground() {
        return buttonBackground;
    }

    public Color buttonText() {
        return buttonText;
    }

    public Color neutralButtonBackground() {
        return neutralButtonBackground;
    }

    public Color selectionBackground() {
        return selectionBackground;
    }

    public Color chartTrack() {
        return chartTrack;
    }

    public Color success() {
        return success;
    }

    public Color successSurface() {
        return successSurface;
    }

    public Color dangerSurface() {
        return dangerSurface;
    }

    public Color mutedSurface() {
        return mutedSurface;
    }

    public Color avatarBackground() {
        return avatarBackground;
    }

    public Color avatarShape() {
        return avatarShape;
    }

    public Color difficultyEasy() {
        return difficultyEasy;
    }

    public Color difficultyMedium() {
        return difficultyMedium;
    }

    public Color difficultyHard() {
        return difficultyHard;
    }

    public Color shadow() {
        return shadow;
    }
}
