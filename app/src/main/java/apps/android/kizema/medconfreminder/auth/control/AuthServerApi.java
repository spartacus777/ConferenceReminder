package apps.android.kizema.medconfreminder.auth.control;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public interface AuthServerApi {

    interface OnRegisterListener{
        void onRegistered();
        void onError(Object err);
    }

    void register(String name, String mail, String pass, String photo, boolean isAdmin, final AuthServerEmulator.OnRegisterListener listener);

}
