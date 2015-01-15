package com.maurice.virolLibgdx.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.maurice.virolLibgdx.ZBHelpers.AssetLoader;

public class TabButton extends TextButton{
    TabButton instance;
    TabButtonMaster master;
    static Skin skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
    public TabButton(String text, Skin skin) {
        super(text, skin);
        instance = this;
        makeInActive();
    }
    public static TabButton getNewTabButton(String text){
        return new TabButton(text,skin);
    }
    public static TabButton getNewTabButton(String text, TabButtonMaster masterInstance){
        TabButton btn = new TabButton(text,skin);
        btn.setMaster(masterInstance);
        return btn;
    }
    public void setMaster(TabButtonMaster masterInstance){
        instance.master = masterInstance;
        instance.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Tab button clicked");
                master.clicked(instance);
            }
        });
    }
    public void makeActive(){
        TextButton.TextButtonStyle tabOptStyleActive = new TextButton.TextButtonStyle();
        tabOptStyleActive.down = new TextureRegionDrawable(AssetLoader.blankBG);
        tabOptStyleActive.up = new TextureRegionDrawable(AssetLoader.blankBG);
        tabOptStyleActive.fontColor = Color.WHITE;
        tabOptStyleActive.font = AssetLoader.whiteFont;
        tabOptStyleActive.font.setScale(0.35f);
        instance.setColor(UIColors.SETTINGS_TAB_BLUE);
        instance.pad(10,20,10,20);
        instance.setStyle(tabOptStyleActive);
    }
    public void makeInActive(){
        TextButton.TextButtonStyle tabOptStyleInactive = new TextButton.TextButtonStyle();
        tabOptStyleInactive.down = new TextureRegionDrawable(AssetLoader.blankBG);
        tabOptStyleInactive.up = new TextureRegionDrawable(AssetLoader.blankBG);
        tabOptStyleInactive.fontColor = Color.GRAY;
        tabOptStyleInactive.font = AssetLoader.whiteFont;
        tabOptStyleInactive.font.setScale(0.35f);
        instance.setStyle(tabOptStyleInactive);
        instance.pad(10,20,10,20);
        instance.setColor(UIColors.colorFromHex(0xFF202226L));
    }
}
