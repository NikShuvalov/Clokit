package shuvalov.nikita.clokit;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class AppUtilsUnitTest {
    @Test
    public void correctValueForWeekStart() throws Exception{
        long firstMonday2017 = Long.valueOf("1483333200001");
        long mlk2017 = Long.valueOf("1484542800001");
        long birthday = Long.valueOf("1493006400001");
        long xmas = Long.valueOf("1514178000001"); //Last week of the year, last monday of year

        assertEquals(firstMonday2017, AppUtils.getWeekStartMillis(20171));
        assertEquals(mlk2017, AppUtils.getWeekStartMillis(20173));
        assertEquals(birthday, AppUtils.getWeekStartMillis(201717));
        assertEquals(xmas, AppUtils.getWeekStartMillis(201752));
    }

    @Test
    public void correctValueForWeeksEnd() throws Exception{
        long firstMonday2017 = Long.valueOf("1483333200001");
        long firstMondayEndOfWeek = Long.valueOf("1483937999999");

        long mlk2017 = Long.valueOf("1484542800001");
        long mlkWeekEnd = Long.valueOf("1485147599999");

        long birthday = Long.valueOf("1493006400001");
        long birthWeeksEnd = Long.valueOf("1493611199999");

        long xmas = Long.valueOf("1514178000001");
        long newYearsEve = Long.valueOf("1514782799999");


        //Checks method with milli param
        assertEquals(firstMondayEndOfWeek,AppUtils.getWeekEndMillis(firstMonday2017));
        assertEquals(mlkWeekEnd, AppUtils.getWeekEndMillis(mlk2017));
        assertEquals(birthWeeksEnd, AppUtils.getWeekEndMillis(birthday));
        assertEquals(newYearsEve, AppUtils.getWeekEndMillis(xmas));

        //CHecks method with weekNum param
        assertEquals(firstMondayEndOfWeek, AppUtils.getWeekEndMillis(20171));
        assertEquals(mlkWeekEnd, AppUtils.getWeekEndMillis(20173));
        assertEquals(birthWeeksEnd, AppUtils.getWeekEndMillis(201717));
        assertEquals(newYearsEve, AppUtils.getWeekEndMillis(201752));
    }

    @Test
    public void correctDisplayOfTime() throws Exception{
        long second = 1000;
        long minute = 60*second;
        long hour = 60*minute;

        assertEquals("03:15", AppUtils.getHoursAndMinutes((3*hour)+(15*minute)));
        assertEquals("10:00", AppUtils.getHoursAndMinutes(10*hour));
        assertEquals("04:20", AppUtils.getHoursAndMinutes((4*hour)+(20*minute)+(59*second)));
        assertEquals("100:59", AppUtils.getHoursAndMinutes((100*hour)+(59*minute)));
    }

}
