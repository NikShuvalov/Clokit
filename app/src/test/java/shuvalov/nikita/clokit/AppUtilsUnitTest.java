package shuvalov.nikita.clokit;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class AppUtilsUnitTest {
    @Test
    public void correctValueForWeekStart() throws Exception{
        long firstMonday2017 = Long.valueOf("1483333201000");
        long mlk2017 = Long.valueOf("1484542801000");
        //ToDo: Add more tests for edge cases. Like last week of year and shit.
        assertEquals(firstMonday2017, AppUtils.getWeekStartMillis(20171));
        assertEquals(mlk2017, AppUtils.getWeekStartMillis(20173));


    }

}
