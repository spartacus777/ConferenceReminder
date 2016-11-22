package apps.android.kizema.medconfreminder.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import apps.android.kizema.medconfreminder.App;

/**
 * Created by A.Kizema on 22.11.2016.
 */

@Entity(indexes = {
        @Index(value = "userId", unique = true)
})
public class ConferenceUserTable {

    @Id
    private Long id;

    private long conferneceId;

    private long userId;

    public static List<ConferenceUserTable> getAllUsersForConference(long conferneceId){
        DaoSession daoSession = App.getDaoSession();
        ConferenceUserTableDao userDao = daoSession.getConferenceUserTableDao();

        QueryBuilder<ConferenceUserTable> queryBuilder = userDao.queryBuilder();
        List<ConferenceUserTable> confs = queryBuilder.where(ConferenceUserTableDao.Properties.ConferneceId.eq(conferneceId)).list();

        return confs;
    }

@Generated(hash = 1456982538)
public ConferenceUserTable(Long id, long conferneceId, long userId) {
    this.id = id;
    this.conferneceId = conferneceId;
    this.userId = userId;
}

@Generated(hash = 1136478773)
public ConferenceUserTable() {
}

public Long getId() {
    return this.id;
}

public void setId(Long id) {
    this.id = id;
}

public long getConferneceId() {
    return this.conferneceId;
}

public void setConferneceId(long conferneceId) {
    this.conferneceId = conferneceId;
}

public long getUserId() {
    return this.userId;
}

public void setUserId(long userId) {
    this.userId = userId;
}
}
