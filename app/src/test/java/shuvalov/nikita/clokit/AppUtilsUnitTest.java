package shuvalov.nikita.clokit;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class AppUtilsUnitTest {
    @Test
    public void correctValueForWeekStart() throws Exception{
        long firstMonday2017 = Long.valueOf("1483246800001"); //New Year's day
        long mlk2017 = Long.valueOf("1484456400001");
        long birthday = Long.valueOf("1493006400001");
        long preBirthday = Long.valueOf("1492920000001");
        long xmasEve = Long.valueOf("1514091600001"); //Last week of the year, last monday of year

        //ToDo: Add tests for daylight savings bullshit

        assertEquals(firstMonday2017, AppUtils.getWeekStartMillis(20171));
        assertEquals(mlk2017, AppUtils.getWeekStartMillis(20173));
        assertEquals(preBirthday, AppUtils.getWeekStartMillis(201717));
        assertEquals(xmasEve, AppUtils.getWeekStartMillis(201752));

    }

//    @Test
//    public void correctValueForWeeksEnd() throws Exception{
//        long firstMonday2017 = Long.valueOf("1483333200001");
//        long firstMondayEndOfWeek = Long.valueOf("1483937999999");
//
//        long mlk2017 = Long.valueOf("1484542800001");
//        long mlkWeekEnd = Long.valueOf("1485147599999");
//
//        long birthday = Long.valueOf("1493006400001");
//        long birthWeeksEnd = Long.valueOf("1493611199999");
//
//        long xmas = Long.valueOf("1514178000001");
//        long newYearsEve = Long.valueOf("1514782799999");
//
//        long marchSeven = Long.valueOf("1488776400001");
//        long marchTwelve = Long.valueOf("1489377599999");//Edge case, daylight savings
//
//        long marchSecond2020 = Long.valueOf("1583125200000"); //Sunday is first day of month.
//        long marchEighth2020 = Long.valueOf("1583726399999"); //Daylight savings
//
//
//
//        //Checks method with milli param
//        assertEquals(firstMondayEndOfWeek,AppUtils.getWeekEndMillis(firstMonday2017));
//        assertEquals(mlkWeekEnd, AppUtils.getWeekEndMillis(mlk2017));
//        assertEquals(birthWeeksEnd, AppUtils.getWeekEndMillis(birthday));
//        assertEquals(newYearsEve, AppUtils.getWeekEndMillis(xmas));
//        assertEquals(marchTwelve, AppUtils.getWeekEndMillis(marchSeven));
//        assertEquals(marchEighth2020, AppUtils.getWeekEndMillis(marchSecond2020));
//
//
//        //CHecks method with weekNum param
//        assertEquals(firstMondayEndOfWeek, AppUtils.getWeekEndMillis(20171));
//        assertEquals(mlkWeekEnd, AppUtils.getWeekEndMillis(20173));
//        assertEquals(birthWeeksEnd, AppUtils.getWeekEndMillis(201717));
//        assertEquals(newYearsEve, AppUtils.getWeekEndMillis(201752));
//        assertEquals(marchTwelve, AppUtils.getWeekEndMillis(201710));
//        assertEquals(marchEighth2020, AppUtils.getWeekEndMillis(2020));
//    }

    @Test
    public void correctGetHoursAndMinutes() throws Exception{
        long second = 1000;
        long minute = 60*second;
        long hour = 60*minute;


        assertEquals("03:15", AppUtils.getHoursAndMinutes((3*hour)+(15*minute)));
        assertEquals("10:00", AppUtils.getHoursAndMinutes(10*hour));
        assertEquals("04:20", AppUtils.getHoursAndMinutes((4*hour)+(20*minute)+(59*second)));
        assertEquals("100:59", AppUtils.getHoursAndMinutes((100*hour)+(59*minute)));
    }

    @Test
    public void correctGetDate()throws Exception{
        long birthday = Long.valueOf("1493006400001");
        long firstMonday2017 = Long.valueOf("1483333200001");
        long xmas = Long.valueOf("1514178000001");

        assertEquals("04/24/2017", AppUtils.getDate(birthday));
        assertEquals("01/02/2017", AppUtils.getDate(firstMonday2017));
        assertEquals("12/25/2017", AppUtils.getDate(xmas));
    }

}
