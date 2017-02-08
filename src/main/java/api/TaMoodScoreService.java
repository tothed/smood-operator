package api;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.Tone;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneCategory;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaMoodScoreService {

    private final ToneAnalyzer toneAnalyzer = new ToneAnalyzer(ToneAnalyzer.VERSION_DATE_2016_05_19);
    private static final ToneOptions emotionOption = new ToneOptions.Builder().addTone(Tone.EMOTION).build();

    public TaMoodScoreService() {
    }

    @PostConstruct
    public void init() {
        toneAnalyzer.setUsernameAndPassword("facd674e-0350-40db-8472-dae5cbb9c6da", "4Ggt6ZJORvPj");
    }

    public long analyze(String text) {
        try {
            toneAnalyzer.setUsernameAndPassword("facd674e-0350-40db-8472-dae5cbb9c6da", "4Ggt6ZJORvPj");
            ServiceCall<ToneAnalysis> serviceResult = toneAnalyzer.getTone(text, emotionOption);
            ToneAnalysis analysis = serviceResult.execute();
            return retrieve(analysis);
        } catch (Exception ex) {
            throw new ToneAnalysisException("Failed to analyse text mood, usign Tone Analyzer",ex);
        }
    }

    private long retrieve(ToneAnalysis analysis) {
        List<ToneCategory> tones = analysis.getDocumentTone().getTones();
        return 1;
    }
}
