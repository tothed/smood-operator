package am.hackathon.dao;

import am.Constant;
import org.junit.Test;

/**
 *
 */
public class DaoTest {
    @Test
    public void listEvents() throws Exception {
            Dao dao =new Dao(Constant.PROXY);
        System.out.println(dao.listEvents());
    }

    @Test
    public void listForecasts() throws Exception {
        Dao dao =new Dao(Constant.PROXY);
        System.out.println(dao.listForecasts());
    }


    @Test
    public void listForecastsWithLocation() throws Exception {
        Dao dao =new Dao(Constant.PROXY);
        System.out.println(dao.listForecasts("Bratislava"));
    }



    @Test
    public void listPerceptionsWithUser() throws Exception {
        Dao dao =new Dao(Constant.PROXY);
        System.out.println(dao.listPerceptions("s04bit"));
    }
}