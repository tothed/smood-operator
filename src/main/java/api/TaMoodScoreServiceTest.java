package api;

import junit.framework.TestCase;

public class TaMoodScoreServiceTest extends TestCase {
    public void testGet() throws Exception {
        TaMoodScoreService service = new TaMoodScoreService();
        long score = service.analyze("My wife has expensive taste");
    }
}