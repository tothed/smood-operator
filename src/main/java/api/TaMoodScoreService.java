package api;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.*;

import java.util.EnumSet;
import java.util.List;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;

/****
 * Mood service working with the IBM Tone Analyzer
 */
public class TaMoodScoreService {

    private final ToneAnalyzer toneAnalyzer = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
    private static final ToneOptions emotionOption = new ToneOptions.Builder().addTone(Tone.EMOTION).build();
    private final Function<ToneCategory, String> GET_TONE_CAT_ID = new Function<ToneCategory, String>() {
        public String apply(ToneCategory input) { return input.getId(); }
    };
    private final Function<ToneScore, String> GET_TONE_SCORE_ID = new Function<ToneScore, String>() {
        public String apply(ToneScore input) { return input.getId(); }
    };

    /****
     * Analyzes the text for the given mood types
     * @param text text to analyze
     * @param moodTypes type of the mood to look for
     * @return score for each requested mood and the reason
     * @throws ToneAnalysisException
     */
    public List<MoodScore> analyze(String text, EnumSet<MoodType> moodTypes) throws ToneAnalysisException {
        try {
            toneAnalyzer.setUsernameAndPassword("facd674e-0350-40db-8472-dae5cbb9c6da", "4Ggt6ZJORvPj");
            ServiceCall<ToneAnalysis> serviceResult = toneAnalyzer.getTone(text, emotionOption);
            ToneAnalysis analysis = serviceResult.execute();
            List<ToneCategory> docTones = analysis.getDocumentTone().getTones();
            List<SentenceTone> sentencesTone = analysis.getSentencesTone();

            return buildMoodScores(moodTypes, docTones, sentencesTone);
        } catch (ToneAnalysisException tex) {
            throw tex;
        } catch(Exception ex) {
            throw new ToneAnalysisException("Failed to analyse text mood, using Tone Analyzer",ex);
        }
    }

    /***
     * High level method to organize the mood retrieval
     * @param moodTypes the requested mood types
     * @param docTones tones per document
     * @param sentencesTone Identified sentecens and their tones
     * @return
     */
    List<MoodScore> buildMoodScores(EnumSet<MoodType> moodTypes, List<ToneCategory> docTones, List<SentenceTone> sentencesTone) {
        List<MoodScore> moodScores = Lists.newArrayList();
        for(MoodType moodType: moodTypes) {
            Long score = retrieveScore(docTones,moodType);
            if (score != null) {
                List<String> reasons = retrieveReasons(sentencesTone, moodType);
                moodScores.add(new MoodScore(score, moodType, reasons));
            }
        }
        return moodScores;
    }

    /***
     * Gets the mood reasons from the sentence tone
     * @param sentencesTone
     * @param moodType
     * @return
     */
    List<String> retrieveReasons(List<SentenceTone> sentencesTone, final MoodType moodType) {
        List<String> result = Lists.newArrayList();

        for (SentenceTone tone : sentencesTone) {
            ToneCategory emoCat = Iterables.find(tone.getTones(), compose(equalTo("emotion_tone"), GET_TONE_CAT_ID));
            if (emoCat != null) {
                if (Iterables.indexOf(emoCat.getTones(), new MoodTypePredicate(moodType)) != -1)
                    result.add(tone.getText());
            }
        }

        return result;
    }

    /***
     * Retrieves the mood score we want from the tone categories
     * @param toneCategories
     * @param moodType
     * @return
     */
    Long retrieveScore(List<ToneCategory> toneCategories, MoodType moodType) {
        List<ToneCategory> tones = toneCategories;
        ToneScore score =  extractScore(tones,"emotion_tone",getScoreType(moodType));
        if (score == null) return null;
        ToneScore agreeabilityScore =  extractScore(tones,"social_tone","agreeableness_big5");

        long scaled = rescale(score.getScore());
        return adjust(scaled,agreeabilityScore.getScore());
    }

    private String getScoreType(MoodType moodType) {
        switch (moodType) {
            case FEAR: return "fear";
            case SADNESS: return "sadness";
            default: return "joy";
        }
    }

    private ToneScore extractScore(List<ToneCategory> tones, String categoryType, String scoreType) {
        Optional<ToneCategory> category =  Iterables.tryFind(tones, compose(equalTo(categoryType),GET_TONE_CAT_ID));
        if (!category.isPresent())
            return null;
            //throw new ToneAnalysisException("Cannot find '"+categoryType+"' category in the analysis. Available categories are: " + Iterables.transform(tones, GET_TONE_CAT_ID));

        List<ToneScore> tonesInCat = category.get().getTones();
        Optional<ToneScore> score =  Iterables.tryFind(tonesInCat, compose(equalTo(scoreType),GET_TONE_SCORE_ID));
        if (!score.isPresent())
            return null;
            //throw new ToneAnalysisException("Cannot find '"+scoreType+"' score in the 'emotion_tone' category. Available scores are: " + Iterables.transform(tonesInCat, GET_TONE_SCORE_ID));
        return score.get();
    }

    /***
     * Rescales from <0,1> to scale of <-100, 1000>
     * @param score
     * @return
     */
    private long rescale(Double score) {
        if (score == null)
            throw new ToneAnalysisException("The joy score returned from the Tone Analyzer is NULL");
        return ((long) (score * 200)) - 100;
    }

    private long adjust(long score, double reliability) {
        return (long) (score * reliability);
    }

    private class MoodTypePredicate implements Predicate<ToneScore> {
        private final MoodType moodType;
        public MoodTypePredicate(MoodType moodType) {this.moodType = moodType;}
        @Override
        public boolean apply(ToneScore input) {
            return getScoreType(moodType).equals(input.getId());
        }
    }
}
