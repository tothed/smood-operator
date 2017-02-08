package am.hackathon;

import am.Constant;
import com.cloudant.client.api.Database;
import org.junit.Test;
import wasdev.sample.servlet.CloudantClientMgr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class EventsTest {
    @Test
    public void test() throws IOException {
        InputStream stream = getClass().getResourceAsStream("Events.csv");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String[] columns = reader.readLine().split(";");
        String line;
        Database db = CloudantClientMgr.getDB(Constant.PROXY);
        while ((line = reader.readLine())!=null){
            String[] values =line.split(";");
            Map<String, Object> map = new HashMap<>();
            map.put("type","event");
            for (int i=0; i<values.length; i++) {
                map.put(columns[i],values[i]);
            }
            db.save(map);
        }
        db.ensureFullCommit();
    }
}
