package apps.android.kizema.medconfreminder.auth.control;


public interface AuthServerApi {

    interface OnRegisterListener{
        void onRegistered();
        void onError(Object err);
    }

    void register(String name, String mail, String pass, String photo, boolean isAdmin, final AuthServerEmulator.OnRegisterListener listener);

    void login(String name, String pass, final AuthServerEmulator.OnRegisterListener listener);

}
