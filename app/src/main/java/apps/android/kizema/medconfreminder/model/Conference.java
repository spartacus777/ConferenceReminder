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
        @Index(value = "conferenceId", unique = true)
})
public class Conference {

    @Id
    private Long id;

    @NotNull
    private String conferenceId;

    @NotNull
    private String userId;//owner

    @NotNull
    private String conferenceName;

    @Property
    private String location;

    @Property
    private String date;

    @ToMany(referencedJoinProperty = "conferneceId")
    private List<Topic> topics;

    @ToMany(referencedJoinProperty = "conferneceId")
    private List<ConferenceUserTable> invitedDoctors;

    public static Conference findById(String conferenceId){
        DaoSession daoSession = App.getDaoSession();
        ConferenceDao confDao = daoSession.getConferenceDao();

        QueryBuilder<Conference> queryBuilder = confDao.queryBuilder();
        List<Conference> conferences = queryBuilder.where(ConferenceDao.Properties.ConferenceId.eq(conferenceId)).list();
        if (conferences == null || conferences.size() == 0){
            return null;
        }

        return conferences.get(0);
    }


    public static Conference findById(long conferenceId){
        DaoSession daoSession = App.getDaoSession();
        ConferenceDao confDao = daoSession.getConferenceDao();

        QueryBuilder<Conference> queryBuilder = confDao.queryBuilder();
        List<Conference> conferences = queryBuilder.where(ConferenceDao.Properties.Id.eq(conferenceId)).list();
        if (conferences == null || conferences.size() == 0){
            return null;
        }

        return conferences.get(0);
    }

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 394449787)
private transient ConferenceDao myDao;

@Generated(hash = 1894370669)
public Conference(Long id, @NotNull String conferenceId, @NotNull String userId, @NotNull String conferenceName,
        String location, String date) {
    this.id = id;
    this.conferenceId = conferenceId;
    this.userId = userId;
    this.conferenceName = conferenceName;
    this.location = location;
    this.date = date;
}

@Generated(hash = 1436823095)
public Conference() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public String getConferenceId() {
    return this.conferenceId;
}

public void setConferenceId(String conferenceId) {
    this.conferenceId = conferenceId;
}

public String getUserId() {
    return this.userId;
}

public void setUserId(String userId) {
    this.userId = userId;
}

public String getConferenceName() {
    return this.conferenceName;
}

public void setConferenceName(String conferenceName) {
    this.conferenceName = conferenceName;
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 1363804658)
public List<Topic> getTopics() {
    if (topics == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        TopicDao targetDao = daoSession.getTopicDao();
        List<Topic> topicsNew = targetDao._queryConference_Topics(id);
        synchronized (this) {
            if (topics == null) {
                topics = topicsNew;
            }
        }
    }
    return topics;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 1067351932)
public synchronized void resetTopics() {
    topics = null;
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

public String getLocation() {
    return this.location;
}

public void setLocation(String location) {
    this.location = location;
}

public String getDate() {
    return this.date;
}

public void setDate(String date) {
    this.date = date;
}

/**
 * To-many relationship, resolved on first access (and after reset).
 * Changes to to-many relations are not persisted, make changes to the target entity.
 */
@Generated(hash = 656827969)
public List<ConferenceUserTable> getInvitedDoctors() {
    if (invitedDoctors == null) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        ConferenceUserTableDao targetDao = daoSession.getConferenceUserTableDao();
        List<ConferenceUserTable> invitedDoctorsNew = targetDao._queryConference_InvitedDoctors(id);
        synchronized (this) {
            if (invitedDoctors == null) {
                invitedDoctors = invitedDoctorsNew;
            }
        }
    }
    return invitedDoctors;
}

/** Resets a to-many relationship, making the next get call to query for a fresh result. */
@Generated(hash = 1123481836)
public synchronized void resetInvitedDoctors() {
    invitedDoctors = null;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 1463206369)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getConferenceDao() : null;
}

}
