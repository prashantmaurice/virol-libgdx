package com.maurice.virolLibgdx.Screens;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.maurice.virolLibgdx.ZombieBird.ZBGame;

public abstract class AbstractGameScreen implements Screen {
    protected ZBGame game;
    public AbstractGameScreen (ZBGame game) {
        this.game = game;
    }
    public abstract void render (float deltaTime);
    public abstract void resize (int width, int height);
    public abstract void show ();
    public abstract void hide ();
    public abstract void pause ();
    public void resume () {
        //Assets.instance.init(new AssetManager());
    }
    public void dispose () {
        //Assets.instance.dispose();
    }
    public void changeScreen() {
        // TODO Auto-generated method stub

    }
    public int getWidth() {
        // TODO Auto-generated method stub
        return 0;
    }
    public InputProcessor getInputProcessor() {
        // TODO Auto-generated method stub
        return null;
    }
}