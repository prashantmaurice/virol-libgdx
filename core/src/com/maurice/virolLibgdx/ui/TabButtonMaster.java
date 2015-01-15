package com.maurice.virolLibgdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.maurice.virolLibgdx.Screens.SettingsScreen;

import java.util.ArrayList;

public class TabButtonMaster{
    TabButtonMaster instance;
    ArrayList<TabButton> buttons = new ArrayList<TabButton>();
    public TabButtonMaster() {
        instance = this;
    }
    public void addTabs(String[] texts){
        for (String text : texts) {
            buttons.add(TabButton.getNewTabButton(text, instance));
        }
    }

    public void clicked(TabButton instance) {
        for(TabButton btn : buttons){
            btn.makeInActive();
            if(instance.equals(btn)){
                btn.makeActive();
            }
        }
    }

    public void addInTable(Table table) {
        for(TabButton btn : buttons){
            table.add(btn).height(SettingsScreen.TAB_SIZE);
        }
    }
}
