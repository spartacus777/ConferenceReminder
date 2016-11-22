package apps.android.kizema.medconfreminder.auth.helpers;

import android.content.SharedPreferences;

import apps.android.kizema.medconfreminder.App;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class Session {

    private static final String APP_PREFERANSES = "APP_PREFERANSES";
    private static final String PASS = "PASS";
    private static final String NAME = "NAME";

    private static Session instance;

    public void saveToken(String name, String pass) {
        SharedPreferences settings = App.getContext().getSharedPreferences(APP_PREFERANSES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(NAME, name);
        editor.putString(PASS, pass);
        editor.apply();
    }

    public String getNameToken() {
        SharedPreferences settings = App.getContext().getSharedPreferences(APP_PREFERANSES, 0);
        return settings.getString(NAME, null);
    }

    public String getPassToken() {
        SharedPreferences settings = App.getContext().getSharedPreferences(APP_PREFERANSES, 0);
        return settings.getString(PASS, null);
    }

    public void clearAuthToken() {
        SharedPreferences settings = App.getContext().getSharedPreferences(APP_PREFERANSES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(NAME, null);
        editor.putString(PASS, null);
        editor.commit();
    }

    private Session(){}

    public static synchronized Session getInstance(){
        if (instance == null){
            instance = new Session();
        }

        return instance;
    }

}
