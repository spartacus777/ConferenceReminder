package apps.android.kizema.medconfreminder;

import android.app.Application;
import android.content.Context;

import apps.android.kizema.medconfreminder.util.ImageLoaderHelper;
import apps.android.kizema.medconfreminder.util.UIHelper;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class App extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        UIHelper.init(context);
        ImageLoaderHelper.init(context);
    }

    public static Context getContext(){
        return context;
    }
}
