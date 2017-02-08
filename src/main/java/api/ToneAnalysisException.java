package api;

public class ToneAnalysisException extends RuntimeException {
    public ToneAnalysisException() {
    }
    public ToneAnalysisException(String message) {
        super(message);
    }
    public ToneAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
