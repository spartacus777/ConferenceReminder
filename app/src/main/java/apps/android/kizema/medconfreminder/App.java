package apps.android.kizema.medconfreminder;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import org.greenrobot.greendao.database.Database;

import apps.android.kizema.medconfreminder.model.DaoMaster;
import apps.android.kizema.medconfreminder.model.DaoSession;
import apps.android.kizema.medconfreminder.util.ImageLoaderHelper;
import apps.android.kizema.medconfreminder.util.UIHelper;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class App extends Application {

    private static Context context;
    private static Handler handler;

    private static DaoSession daoSession;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        handler = new Handler();

        UIHelper.init(context);
        ImageLoaderHelper.init(context);
        initGreenDao();
    }

    public static Context getContext(){
        return context;
    }

    public static Handler getUIHandler(){
        return handler;
    }

    private static void initGreenDao(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "notes-db");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession(){
        return daoSession;
    }
}
