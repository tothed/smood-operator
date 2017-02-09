package api;

import com.google.common.collect.Lists;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;
import junit.framework.TestCase;
import org.junit.Assert;

import java.util.List;

public class TaMoodScoreServiceTest extends TestCase {
    public void testRetrieve() throws Exception {
        List<ToneCategory> categories = Lists.newArrayList();

        ToneCategory emo = new ToneCategory();
        emo.setId("emotion_tone");
        ToneScore joyScore = new ToneScore();
        joyScore.setId("joy");
        joyScore.setScore(0.8);
        emo.setTones(Lists.newArrayList(joyScore));

        ToneCategory social = new ToneCategory();
        social.setId("social_tone");
        ToneScore agreeableness = new ToneScore();
        agreeableness.setId("agreeableness_big5");
        agreeableness.setScore(0.6);
        social.setTones(Lists.newArrayList(agreeableness));

        categories.add(emo);
        categories.add(social);

        // Do the test
        TaMoodScoreService service = new TaMoodScoreService();
        long value = service.retrieve(categories);

        Assert.assertEquals((long)((joyScore.getScore() * 200 -100)*agreeableness.getScore()),value);
    }
}