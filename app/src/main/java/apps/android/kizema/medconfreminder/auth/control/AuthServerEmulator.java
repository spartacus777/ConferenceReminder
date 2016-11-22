package apps.android.kizema.medconfreminder.auth.control;


import apps.android.kizema.medconfreminder.App;
import apps.android.kizema.medconfreminder.model.AccountUser;
import apps.android.kizema.medconfreminder.model.AccountUserDao;
import apps.android.kizema.medconfreminder.model.DaoSession;
import apps.android.kizema.medconfreminder.model.User;
import apps.android.kizema.medconfreminder.model.UserDao;
import apps.android.kizema.medconfreminder.util.UserHelper;

public class AuthServerEmulator implements AuthServerApi {

    @Override
    public void register(String name, String mail, String pass, String photo, boolean isAdmin, final OnRegisterListener listener){

        DaoSession daoSession = App.getDaoSession();
        UserDao noteDao = daoSession.getUserDao();

        User user = new User();
        user.setUserId(UserHelper.generateUserId());
        user.setEmail(mail);
        user.setName(name);
        user.setIsAdmin(isAdmin);
        user.setPhoto(photo);
        noteDao.insert(user);

        AccountUserDao accountUserDao = daoSession.getAccountUserDao();
        AccountUser accountUser = new AccountUser();
        accountUser.setUserId(user.getUserId());
        accountUser.setPassword(pass);
        accountUser.setName(name);
        accountUserDao.insert(accountUser);

        //emulate server work
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                App.getUIHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onRegistered();
                    }
                });
            }
        }).start();
    }

    @Override
    public void login(final String name, final String pass, final OnRegisterListener listener) {

        //emulate server work
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                App.getUIHandler().post(new Runnable() {
                    @Override
                    public void run() {

                        if (AccountUser.find(name, pass) != null){
                            listener.onRegistered();
                        } else {
                            listener.onError("");
                        }

                    }
                });
            }
        }).start();
    }

}
