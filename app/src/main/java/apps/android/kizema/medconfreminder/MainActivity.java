package apps.android.kizema.medconfreminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.List;

import apps.android.kizema.medconfreminder.model.DaoSession;
import apps.android.kizema.medconfreminder.model.User;
import apps.android.kizema.medconfreminder.model.UserDao;

public class MainActivity extends AppCompatActivity {

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
