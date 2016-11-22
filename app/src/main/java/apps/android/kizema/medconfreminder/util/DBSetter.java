package apps.android.kizema.medconfreminder.util;

import java.util.List;

import apps.android.kizema.medconfreminder.App;
import apps.android.kizema.medconfreminder.model.Conference;
import apps.android.kizema.medconfreminder.model.ConferenceDao;
import apps.android.kizema.medconfreminder.model.Topic;
import apps.android.kizema.medconfreminder.model.TopicDao;
import apps.android.kizema.medconfreminder.model.User;
import apps.android.kizema.medconfreminder.model.UserDao;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class DBSetter {

    private static final String ANTON_DEMO_USER_ID = "welkfjwdwemdwedwedwe";
    private static final String DOC_DEMO_USER_ID = "lmj3b2herb43fcds";

    private static final String DEMOCONF_1 = "09382hdndwefk4328e23d";
    private static final long DEMOCONF_1_L = 8923832;

    public static void setDemoDb(){
        UserDao userDao = App.getDaoSession().getUserDao();

        {
            User user = User.findById(ANTON_DEMO_USER_ID);
            if (user == null) {
                user = new User();
                user.setUserId(ANTON_DEMO_USER_ID);
                user.setEmail("demouser@mail.com");
                user.setName("Anton Demo");
                user.setIsAdmin(true);
                userDao.insert(user);
            }
        }

        {
            User user = User.findById(DOC_DEMO_USER_ID);
            if (user == null) {
                user = new User();
                user.setUserId(DOC_DEMO_USER_ID);
                user.setEmail("doc@mail.com");
                user.setName("Doctor");
                user.setIsAdmin(false);
                userDao.insert(user);
            }
        }


        ConferenceDao confDao = App.getDaoSession().getConferenceDao();
        Conference conference = Conference.findById(DEMOCONF_1);
        if (conference == null){
            conference = new Conference();
            conference.setUserId(ANTON_DEMO_USER_ID);
            conference.setConferenceId(DEMOCONF_1);
            conference.setConferenceName("Main Demo conference");
            conference.setId(DEMOCONF_1_L);
            confDao.insert(conference);
        }

        TopicDao topicDao = App.getDaoSession().getTopicDao();

        List<Topic> demoTopics = Topic.getAllForConferenceId(DEMOCONF_1_L);
        if (demoTopics == null || demoTopics.size() == 0){
            Topic topic = new Topic();
            topic.setTopicId(UserHelper.generateConfId());
            topic.setUserId(DOC_DEMO_USER_ID);
            topic.setName("Physics in our life");
            topic.setConferneceId(DEMOCONF_1_L);
            topicDao.insert(topic);

            Topic topic2 = new Topic();
            topic2.setTopicId(UserHelper.generateConfId());
            topic2.setUserId(DOC_DEMO_USER_ID);
            topic2.setName("Math in our life");
            topic2.setConferneceId(DEMOCONF_1_L);
            topicDao.insert(topic2);
        }

    }

}
