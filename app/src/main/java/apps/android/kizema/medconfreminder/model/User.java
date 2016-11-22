package apps.android.kizema.medconfreminder.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import java.util.List;

import apps.android.kizema.medconfreminder.App;

/**
 * Created by A.Kizema on 22.11.2016.
 */
@Entity(indexes = {
        @Index(value = "userId", unique = true)
})
public class User {

    @NotNull
    private String userId;

    @Property
    private String email;

    @NotNull
    private String name;

    @NotNull
    private boolean isAdmin;

    @Property
    private String photo;

    public static User findById(String userId){
        DaoSession daoSession = App.getDaoSession();
        UserDao userDao = daoSession.getUserDao();

        List<User> users = userDao.queryRaw("userId = ?", userId);
        if (users == null || users.size() == 0){
            return null;
        }

        return users.get(0);
    }

    @Generated(hash = 1758588706)
    public User(@NotNull String userId, String email, @NotNull String name,
                boolean isAdmin, String photo) {
        this.userId = userId;
        this.email = email;
        this.name = name;
        this.isAdmin = isAdmin;
        this.photo = photo;
    }

    @Generated(hash = 586692638)
    public User() {
    }


    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsAdmin() {
        return this.isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getPhoto() {
        return this.photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
