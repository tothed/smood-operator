package api;

import com.google.common.collect.Lists;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.SentenceTone;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneScore;
import org.junit.Assert;
import org.junit.Test;

import java.util.EnumSet;
import java.util.List;

public class TaMoodScoreServiceTest {

    @Test
    public void retrieveJoyScore() throws Exception {
        List<ToneCategory> categories = buildDocToneCategories("joy");

        // Do the test
        TaMoodScoreService service = new TaMoodScoreService();
        long value = service.retrieveScore(categories, MoodType.JOY);

        Assert.assertEquals(36, value);
    }

    @Test
    public void retrieveSadnessScore() throws Exception {
        List<ToneCategory> categories = buildDocToneCategories("sadness");
        // Do the test
        TaMoodScoreService service = new TaMoodScoreService();
        long value = service.retrieveScore(categories, MoodType.SADNESS);

        Assert.assertEquals(36, value);
    }

    @Test
    public void retrieveFearScore() throws Exception {

        List<ToneCategory> categories = buildDocToneCategories("fear");
        // Do the test
        TaMoodScoreService service = new TaMoodScoreService();
        long value = service.retrieveScore(categories, MoodType.FEAR);

        Assert.assertEquals(36, value);
    }


    @Test
    public void retrieveReasons() throws Exception {

        List<SentenceTone> sentencesTone = buildSentenceTones();

        TaMoodScoreService service = new TaMoodScoreService();

        List<String> fearReasons = service.retrieveReasons(sentencesTone, MoodType.FEAR);
        List<String> joyReasons = service.retrieveReasons(sentencesTone, MoodType.JOY);
        List<String> sadnessReasons = service.retrieveReasons(sentencesTone, MoodType.SADNESS);

        Assert.assertEquals(2, joyReasons.size());
        Assert.assertEquals(1, fearReasons.size());
        Assert.assertEquals(1, sadnessReasons.size());
    }


    List<ToneCategory> buildDocToneCategories(String toneScoreId) {
        List<ToneCategory> categories = Lists.newArrayList();

        ToneCategory emo = new ToneCategory();
        emo.setId("emotion_tone");
        ToneScore joyScore = new ToneScore();
        joyScore.setId(toneScoreId);
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

        return categories;
    }

    List<SentenceTone> buildSentenceTones() {
        SentenceTone tone1 = new SentenceTone();
        tone1.setText("My wife has expensive taste");
        ToneCategory cat1 = new ToneCategory();
        ToneScore fear1 = new ToneScore();
        fear1.setId("fear");
        fear1.setScore(0.5);
        ToneScore joy1 = new ToneScore();
        joy1.setId("joy");
        joy1.setScore(0.1);
        cat1.setTones(Lists.newArrayList(fear1, joy1));
        cat1.setId("emotion_tone");
        tone1.setTones(Lists.newArrayList(cat1));

        SentenceTone tone2 = new SentenceTone();
        tone2.setText("The dishwasher has died, but we replaced it with a maid");
        ToneCategory cat2 = new ToneCategory();
        ToneScore sadness2 = new ToneScore();
        sadness2.setId("sadness");
        sadness2.setScore(0.6);
        ToneScore joy2 = new ToneScore();
        joy2.setId("joy");
        joy2.setScore(0.8);
        cat2.setTones(Lists.newArrayList(sadness2, joy2));
        cat2.setId("emotion_tone");
        tone2.setTones(Lists.newArrayList(cat2));
        return Lists.newArrayList(tone1, tone2);
    }

    @Test
    public void testBuildMoodScores() throws Exception {

        List<SentenceTone> sentencesTone = buildSentenceTones();
        // Sadness
        List<ToneCategory> docTones = Lists.newArrayList();

        ToneCategory emo = new ToneCategory();
        emo.setId("emotion_tone");

        // JOY
        ToneScore joyScore = new ToneScore();
        joyScore.setId("joy");
        joyScore.setScore(0.8);

        // FEAR
        ToneScore fearScore = new ToneScore();
        fearScore.setId("fear");
        fearScore.setScore(0.8);

        // SADNESS
        ToneScore sadnessScore = new ToneScore();
        sadnessScore.setId("sadness");
        sadnessScore.setScore(0.8);
        emo.setTones(Lists.newArrayList(joyScore,fearScore,sadnessScore));

        ToneCategory social = new ToneCategory();
        social.setId("social_tone");
        ToneScore agreeableness = new ToneScore();
        agreeableness.setId("agreeableness_big5");
        agreeableness.setScore(0.6);
        social.setTones(Lists.newArrayList(agreeableness));

        docTones.add(emo);
        docTones.add(social);

        // Do the test
        TaMoodScoreService service = new TaMoodScoreService();
        EnumSet<MoodType> enums = EnumSet.of(MoodType.JOY, MoodType.FEAR, MoodType.SADNESS);
        List<MoodScore> result = service.buildMoodScores(enums, docTones, sentencesTone);
    }
}