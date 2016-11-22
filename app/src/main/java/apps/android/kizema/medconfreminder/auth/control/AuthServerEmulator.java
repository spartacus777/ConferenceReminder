package apps.android.kizema.medconfreminder.auth.control;


import apps.android.kizema.medconfreminder.App;

public class AuthServerEmulator implements AuthServerApi {

    @Override
    public void register(String name, String mail, String pass, String photo, boolean isAdmin, final OnRegisterListener listener){
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
    public void login(String name, String pass, final OnRegisterListener listener) {
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
                        listener.onRegistered();
                    }
                });
            }
        }).start();
    }

}
