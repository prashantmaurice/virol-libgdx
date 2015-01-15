package com.maurice.virolLibgdx.ZBHelpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by maurice on 16/01/15.
 */
public class PrefsManager {
    static PrefsManager instance;
    private Preferences prefs;



    public enum Type { SOUND, LEVEL};
    private static final String PREFS_NAME = "virol_userprefs";
    private static final String KEY_FIRST_TIME_CHECK = "success";
    private static final String KEY_SOUND = "sound";
    private static final String KEY_LEVEL = "level";
    public String SOUND = "ON";//this should match name on tab
    public String LEVEL = "EASY";
    public PrefsManager(){
        loadSettings();
    }
    public static PrefsManager getInstance(){
        if(instance==null) instance = new PrefsManager();
        return instance;
    }
    public void loadSettings(){
        prefs = getPrefs();
        if(prefs.contains(KEY_FIRST_TIME_CHECK)){
            SOUND = prefs.getString(KEY_SOUND);
            LEVEL = prefs.getString(KEY_LEVEL);
            System.out.println("PREFS:extracted="+prefs.getString(KEY_FIRST_TIME_CHECK)+"="+SOUND+"="+LEVEL);
        }else{
            //FirstTime check
            System.out.println("PREFS:saving prefs for the first time");
            updateSettings();
        }
        System.out.println("PREFS:pulled="+prefs.getString(KEY_FIRST_TIME_CHECK)+"="+SOUND+"="+LEVEL);
    }
    public void updateSettings(){
        prefs = getPrefs();
        prefs.putString(KEY_SOUND,SOUND);
        prefs.putString(KEY_LEVEL,LEVEL);
        prefs.putString(KEY_FIRST_TIME_CHECK,"dummy");
        prefs.flush();
        System.out.println("PREFS:pushed="+prefs.getString(KEY_FIRST_TIME_CHECK)+"="+SOUND+"="+LEVEL);
//        System.out.println("PREFS:pushedcheck="+prefs.getString(KEY_FIRST_TIME_CHECK)+"="+prefs.getString(KEY_SOUND)+"="+prefs.getString(KEY_LEVEL));
    }
    public void updateSettings(Type type, String key) {
        switch (type){
            case SOUND:
                SOUND = key;
                updateSettings();break;
            case LEVEL:
                LEVEL = key;
                updateSettings();break;
        }
    }
    protected Preferences getPrefs(){
        return Gdx.app.getPreferences( PREFS_NAME );
    }

    //specific;
    public void saveSound(){

    }
}
