package api;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.*;

import java.util.List;

import static com.google.common.base.Predicates.compose;
import static com.google.common.base.Predicates.equalTo;

/****
 * Mood service woring with the IBM Tone Analyzer
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

    public TaMoodScoreService() {
    }

//    @PostConstruct
//    public void init() {
//        toneAnalyzer.setUsernameAndPassword("facd674e-0350-40db-8472-dae5cbb9c6da", "4Ggt6ZJORvPj");
//    }

    public long analyze(String text) throws ToneAnalysisException {
        try {
            toneAnalyzer.setUsernameAndPassword("facd674e-0350-40db-8472-dae5cbb9c6da", "4Ggt6ZJORvPj");
            ServiceCall<ToneAnalysis> serviceResult = toneAnalyzer.getTone(text, emotionOption);
            ToneAnalysis analysis = serviceResult.execute();
            return retrieve(analysis);
        } catch (ToneAnalysisException tex) {
            throw tex;
        } catch(Exception ex) {
            throw new ToneAnalysisException("Failed to analyse text mood, using Tone Analyzer",ex);
        }
    }

    /***
     * Retrieves the score we want (the joy score) from the analysis
     * @param analysis
     * @return
     */
    private long retrieve(ToneAnalysis analysis) {
        List<ToneCategory> tones = analysis.getDocumentTone().getTones();
        ToneCategory emotionCat =  Iterables.find(tones, compose(equalTo("emotion_tone"),GET_TONE_CAT_ID));
        if (emotionCat == null)
            throw new ToneAnalysisException("Cannot find 'emotion_tone' category in the analysis. Available categories are: " + Iterables.transform(tones, GET_TONE_CAT_ID));

        ToneScore joyScore =  Iterables.find(emotionCat.getTones(), compose(equalTo("joy"),GET_TONE_SCORE_ID));
        if (joyScore == null)
            throw new ToneAnalysisException("Cannot find 'joy' score in the 'emotion_tone' category. Available scores are: " + Iterables.transform(emotionCat.getTones(), GET_TONE_SCORE_ID));

        return normalizeScore(joyScore.getScore());
    }

    private long normalizeScore(Double score) {
        if (score == null)
            throw new ToneAnalysisException("The joy score returned from the Tone Analyzer is NULL");
        return ((long) (score * 200)) - 100;
    }
}
