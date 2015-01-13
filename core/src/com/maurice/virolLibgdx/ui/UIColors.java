package com.maurice.virolLibgdx.ui;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by maurice on 27/12/14.
 */
public class UIColors {
    //GENERIC
    public static Color WHITE = colorFromInt(255, 255, 255, 1);
    public static Color GREEN = colorFromInt(122, 255, 122, 1);


    //MENU SCREEN
    public static Color MENU_DARKBLUE = colorFromInt(28, 34, 69, 1);
    public static Color MENU_LIGHTBLUE_LINE = colorFromInt(103, 158, 232, 1);
    public static Color MENU_WHITE = colorFromInt(255, 255, 255, 1);
    public static Color MENU_BUTTON = colorFromInt(65, 159, 255, 1);

    //SETTINGS BUTTON
    public static Color SETTINGS_TAB_BLUE = colorFromInt(103, 158, 232, 1);

    //ABOUT ME
    public static Color ABOUT_BG = colorFromInt(27, 60, 100, 1);

    //MAIN GAME SCREEN
    public static Color GAME_BG = colorFromInt(28, 32, 47, 1);
    public static Color GAME_BG_BOTTOM = colorFromInt(18, 22, 37, 1);
    public static Color GAME_PLAYER_BLUE = colorFromInt(51, 181, 229, 1);
    public static Color GAME_PLAYER_RED = colorFromInt(226, 82, 82, 1);
    public static Color GAME_ANIM_LINE_COLOR = Color.GRAY;












    //UTILITIES
    public static Color colorFromHex(long hex){
        float a = (hex & 0xFF000000L) >> 24;
        float r = (hex & 0xFF0000L) >> 16;
        float g = (hex & 0xFF00L) >> 8;
        float b = (hex & 0xFFL);
        return new Color(r/255f, g/255f, b/255f, a/255f);
    }
    private static Color colorFromInt(int r, int g, int b, int a){
        return new Color((float)r/255f, (float)g/255f, (float)b/255f, (float)a);
    }
}
