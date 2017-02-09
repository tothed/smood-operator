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
        ToneScore joyScore =  extractScore(tones,"emotion_tone","joy");
        ToneScore agreeabilityScore =  extractScore(tones,"social_tone","agreeableness_big5");

        long scaled = rescale(joyScore.getScore());
        return adjust(scaled,agreeabilityScore.getScore());
    }

    private ToneScore extractScore(List<ToneCategory> tones, String categoryType, String scoreType) {
        ToneCategory category =  Iterables.find(tones, compose(equalTo(/*"social_tone"*/categoryType),GET_TONE_CAT_ID));
        if (category == null)
            throw new ToneAnalysisException("Cannot find '"+categoryType+"' category in the analysis. Available categories are: " + Iterables.transform(tones, GET_TONE_CAT_ID));

        ToneScore score =  Iterables.find(category.getTones(), compose(equalTo(/*"agreeableness_big5"*/scoreType),GET_TONE_SCORE_ID));
        if (score == null)
            throw new ToneAnalysisException("Cannot find '"+scoreType+"' score in the 'emotion_tone' category. Available scores are: " + Iterables.transform(category.getTones(), GET_TONE_SCORE_ID));
        return score;
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
}
