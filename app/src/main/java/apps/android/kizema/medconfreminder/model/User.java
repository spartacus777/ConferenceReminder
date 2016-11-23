package apps.android.kizema.medconfreminder.model;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import apps.android.kizema.medconfreminder.App;

/**
 * Created by A.Kizema on 22.11.2016.
 */
@Entity(indexes = {
        @Index(value = "userId", unique = true)
})
public class User {

    @Id
    private Long id;

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

    @ToMany(referencedJoinProperty = "userId")
    private List<ConferenceUserTable> conferenceIds;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;

    public static User findById(String userId){
        DaoSession daoSession = App.getDaoSession();
        UserDao userDao = daoSession.getUserDao();

        QueryBuilder<User> queryBuilder = userDao.queryBuilder();
        List<User> users = queryBuilder.where(UserDao.Properties.UserId.eq(userId)).list();
        if (users == null || users.size() == 0){
            return null;
        }

        return users.get(0);
    }

    public static User findById(long userId){
        DaoSession daoSession = App.getDaoSession();
        UserDao userDao = daoSession.getUserDao();

        QueryBuilder<User> queryBuilder = userDao.queryBuilder();
        List<User> users = queryBuilder.where(UserDao.Properties.Id.eq(userId)).list();
        if (users == null || users.size() == 0){
            return null;
        }

        return users.get(0);
    }

    public static List<User> loadAllDoctors(){
        DaoSession daoSession = App.getDaoSession();
        UserDao userDao = daoSession.getUserDao();

        QueryBuilder<User> queryBuilder = userDao.queryBuilder();
        List<User> users = queryBuilder.where(UserDao.Properties.IsAdmin.eq(false)).list();

        return users;
    }

    @Generated(hash = 447406661)
    public User(Long id, @NotNull String userId, String email, @NotNull String name,
            boolean isAdmin, String photo) {
        this.id = id;
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1570181084)
    public List<ConferenceUserTable> getConferenceIds() {
        if (conferenceIds == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ConferenceUserTableDao targetDao = daoSession.getConferenceUserTableDao();
            List<ConferenceUserTable> conferenceIdsNew = targetDao
                    ._queryUser_ConferenceIds(id);
            synchronized (this) {
                if (conferenceIds == null) {
                    conferenceIds = conferenceIdsNew;
                }
            }
        }
        return conferenceIds;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 182630596)
    public synchronized void resetConferenceIds() {
        conferenceIds = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }
}
