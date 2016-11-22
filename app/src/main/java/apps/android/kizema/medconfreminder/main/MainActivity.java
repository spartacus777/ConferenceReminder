package apps.android.kizema.medconfreminder.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import apps.android.kizema.medconfreminder.App;
import apps.android.kizema.medconfreminder.base.BaseActivity;
import apps.android.kizema.medconfreminder.R;
import apps.android.kizema.medconfreminder.model.DaoSession;
import apps.android.kizema.medconfreminder.model.User;
import apps.android.kizema.medconfreminder.model.UserDao;

public class MainActivity extends BaseActivity {

    public static Intent getIntent(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaoSession daoSession = App.getDaoSession();
        UserDao noteDao = daoSession.getUserDao();

        List<User> users = noteDao.loadAll();
        Log.d("DB", "SIZE : " + users.size());
    }
}
