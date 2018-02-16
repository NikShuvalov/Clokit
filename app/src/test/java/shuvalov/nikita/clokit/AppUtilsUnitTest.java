package shuvalov.nikita.clokit;

import org.junit.Test;

import java.util.Calendar;

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

        assertEquals(firstMonday2017, AppUtils.getWeekStartMillis(20171));
        assertEquals(mlk2017, AppUtils.getWeekStartMillis(20173));
        assertEquals(preBirthday, AppUtils.getWeekStartMillis(201717));
        assertEquals(xmasEve, AppUtils.getWeekStartMillis(201752));

    }

    @Test
    public void correctValueForWeeksEnd() throws Exception{
        long firstSunday2017 = Long.valueOf("1483246800001"); //New Year's day
        long firstSaturdayEndOfWeek = Long.valueOf("1483851599999");//First Saturday

        long mlk2017 = Long.valueOf("1484456400001"); //Sunday Prior
        long mlkWeekEnd = Long.valueOf("1485061199999");

        long preBirthday = Long.valueOf("1492920000001");
        long birthWeeksEnd = Long.valueOf("1493524799999");

        long xmasEve = Long.valueOf("1514091600001"); //Last week of the year, last Sunday of year
        long newYearsEve = Long.valueOf("1514696399999");

        long marchSixth = Long.valueOf("1488690000001");
        long marchEleventh = Long.valueOf("1489294799999");//Edge case, daylight savings

        long marchFirst2020 = Long.valueOf("1583038800001"); //First day of month, Sunday
        long marchSeventh2020 = Long.valueOf("1583643599999"); //Daylight savings

        long marchTwelve17 = Long.valueOf("1489294800001"); //Actual dst date
        long marchEighteen17 = Long.valueOf("1489895999999"); //WeeksEnd of dst



        //Checks method with milli param, which I am not currently using.
//        assertEquals(firstSaturdayEndOfWeek,AppUtils.getWeekEndMillis(firstSunday2017));
//        assertEquals(mlkWeekEnd, AppUtils.getWeekEndMillis(mlk2017));
//        assertEquals(birthWeeksEnd, AppUtils.getWeekEndMillis(preBirthday));
//        assertEquals(newYearsEve, AppUtils.getWeekEndMillis(xmasEve));
//        assertEquals(marchEleventh, AppUtils.getWeekEndMillis(marchSixth));
//        assertEquals(marchSeventh2020, AppUtils.getWeekEndMillis(marchFirst2020));

        //CHecks method with weekNum param
        assertEquals(firstSaturdayEndOfWeek, AppUtils.getWeekEndMillis(20171));
        assertEquals(mlkWeekEnd, AppUtils.getWeekEndMillis(20173));
        assertEquals(birthWeeksEnd, AppUtils.getWeekEndMillis(201717));
        assertEquals(newYearsEve, AppUtils.getWeekEndMillis(201752));
        assertEquals(marchEleventh, AppUtils.getWeekEndMillis(201710));
        assertEquals(marchSeventh2020, AppUtils.getWeekEndMillis(202010));
        assertEquals(marchEighteen17, AppUtils.getWeekEndMillis(201711));
    }

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
