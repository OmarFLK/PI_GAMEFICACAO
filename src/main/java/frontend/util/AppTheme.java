package frontend.util;

import java.awt.Color;
import java.awt.Font;

public final class AppTheme {

    public static final Color BACKGROUND = new Color(245, 245, 245);
    public static final Color SURFACE = new Color(255, 255, 255);
    public static final Color PRIMARY_DARK = new Color(8, 13, 19);
    public static final Color SECONDARY_DARK = new Color(17, 24, 32);
    public static final Color RED = new Color(193, 18, 31);
    public static final Color RED_HOVER = new Color(139, 0, 0);
    public static final Color RED_SOFT = new Color(255, 241, 243);
    public static final Color PRIMARY_HIGHLIGHT = new Color(139, 0, 0);
    public static final Color STUDENT_HIGHLIGHT = PRIMARY_HIGHLIGHT;
    public static final Color PROFESSOR_HIGHLIGHT = PRIMARY_HIGHLIGHT;
    public static final Color ERROR_HIGHLIGHT = new Color(220, 38, 38);
    public static final Color RANK_FIRST = new Color(180, 83, 9);
    public static final Color NEUTRAL_DARK = new Color(55, 65, 81);
    public static final Color DIFFICULTY_EASY = new Color(254, 226, 226);
    public static final Color DIFFICULTY_MEDIUM = new Color(252, 165, 165);
    public static final Color DIFFICULTY_HARD = new Color(185, 28, 28);
    public static final Color DIFFICULTY_PROGRESSIVE = STUDENT_HIGHLIGHT;
    public static final Color BORDER = new Color(221, 221, 221);
    public static final Color TEXT = new Color(22, 28, 36);
    public static final Color TEXT_MUTED = new Color(82, 96, 112);
    public static final Color SOFT_GRAY = new Color(238, 240, 242);

    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 34);
    public static final Font TEXT_FONT = new Font("Segoe UI", Font.PLAIN, 16);

    private AppTheme() {
    }
}
