package apps.android.kizema.medconfreminder.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import apps.android.kizema.medconfreminder.App;

/**
 * Created by A.Kizema on 22.11.2016.
 */

@Entity
public class AccountUser {

    @Id
    private Long id;

    @NotNull
    private String userId;

    @NotNull
    private String password;

    @NotNull
    private String name;

    public static AccountUser find(String name, String password){
        DaoSession daoSession = App.getDaoSession();
        AccountUserDao userDao = daoSession.getAccountUserDao();

        QueryBuilder<AccountUser> queryBuilder = userDao.queryBuilder();
        queryBuilder.where(AccountUserDao.Properties.Name.eq(name));
        queryBuilder.where(AccountUserDao.Properties.Password.eq(password));

        List<AccountUser> accountUsers = queryBuilder.list();

        if (accountUsers == null || accountUsers.size() == 0){
            return null;
        }

        return accountUsers.get(0);
    }

    @Generated(hash = 608732253)
    public AccountUser(Long id, @NotNull String userId, @NotNull String password,
            @NotNull String name) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
    }

    @Generated(hash = 1045698652)
    public AccountUser() {
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
