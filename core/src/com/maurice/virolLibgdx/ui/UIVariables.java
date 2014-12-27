package com.maurice.virolLibgdx.ui;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by maurice on 27/12/14.
 */
public class UIVariables {

    //MENU SCREEN
    public static Color CLR_MENU_DARKBLUE = colorFromInt(28, 34, 69, 1);
    public static Color CLR_MENU_LIGHTBLUE_LINE = colorFromInt(103, 158, 232, 1);
    public static Color CLR_MENU_WHITE = colorFromInt(255, 255, 255, 1);
    public static Color CLR_MENU_BUTTON = colorFromInt(65, 159, 255, 1);












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
