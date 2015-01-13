package com.maurice.virolLibgdx.ZBHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * Created by maurice on 14/01/15.
 */
public class UIObjectGenerater {
    private static Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));


    //SETTINGS SCREEN GENERATORS
    public static Label generateLabel(String text){
        Label label = new Label(text,skin);
        return label;
    }
}
