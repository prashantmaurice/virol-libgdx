package com.maurice.virolLibgdx.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.maurice.virolLibgdx.Screens.SettingsScreen;
import com.maurice.virolLibgdx.ZBHelpers.PrefsManager;

import java.util.ArrayList;

public class TabButtonMaster{
    TabButtonMaster instance;
    PrefsManager.Type type;
    ArrayList<TabButton> buttons = new ArrayList<TabButton>();
    public TabButtonMaster(PrefsManager.Type typeinstance) {
        instance = this;
        type = typeinstance;
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
                PrefsManager prefs = PrefsManager.getInstance();
                prefs.updateSettings(type, btn.key);
            }
        }
    }

    public void addInTable(Table table) {
        for(TabButton btn : buttons){
            table.add(btn).height(SettingsScreen.TAB_SIZE);
        }
    }

    public void activateTab(String key) {
        for(TabButton btn : buttons){
            btn.makeInActive();
            if(btn.key.equals(key)){
                btn.makeActive();
                break;
            }
        }
    }
}
