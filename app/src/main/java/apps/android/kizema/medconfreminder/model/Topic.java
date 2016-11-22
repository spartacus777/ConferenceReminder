package apps.android.kizema.medconfreminder.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import apps.android.kizema.medconfreminder.App;

/**
 * Created by A.Kizema on 22.11.2016.
 */

@Entity(indexes = {
        @Index(value = "topicId", unique = true)
})
public class Topic {

    @Id
    private Long id;

    private long conferneceId;

    @NotNull
    private String topicId;

    @NotNull
    private String userId;//owner

    @NotNull
    private String name;

    public static List<Topic> getAllForConferenceId(long conferenceId){
        DaoSession daoSession = App.getDaoSession();
        TopicDao confDao = daoSession.getTopicDao();

        QueryBuilder<Topic> queryBuilder = confDao.queryBuilder();
        List<Topic> topics = queryBuilder.where(TopicDao.Properties.ConferneceId.eq(conferenceId)).list();

        return topics;
    }

@Generated(hash = 1024431196)
public Topic(Long id, long conferneceId, @NotNull String topicId,
        @NotNull String userId, @NotNull String name) {
    this.id = id;
    this.conferneceId = conferneceId;
    this.topicId = topicId;
    this.userId = userId;
    this.name = name;
}

@Generated(hash = 849012203)
public Topic() {
}


public String getTopicId() {
    return this.topicId;
}

public void setTopicId(String topicId) {
    this.topicId = topicId;
}

public String getUserId() {
    return this.userId;
}

public void setUserId(String userId) {
    this.userId = userId;
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

public long getConferneceId() {
    return this.conferneceId;
}

public void setConferneceId(long conferneceId) {
    this.conferneceId = conferneceId;
}


}
