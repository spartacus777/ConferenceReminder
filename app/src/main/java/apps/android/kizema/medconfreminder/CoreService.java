package apps.android.kizema.medconfreminder;

import apps.android.kizema.medconfreminder.auth.control.AuthServerApi;
import apps.android.kizema.medconfreminder.auth.control.AuthServerEmulator;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class CoreService {

    private static CoreService instance;

    private AuthServerApi authServerApi;

    private CoreService(){
        authServerApi = new AuthServerEmulator();
    }

    public static synchronized CoreService getInstance(){
        if (instance == null){
            instance = new CoreService();
        }

        return instance;
    }

    public AuthServerApi getAuthServerApi() {
        return authServerApi;
    }
}
