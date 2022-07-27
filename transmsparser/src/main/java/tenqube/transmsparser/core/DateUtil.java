package tenqube.transmsparser.core;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


class DateUtil {


    static final DecimalFormat decimalFormat = new DecimalFormat("00");// decimalformat은

    static String getStringDateAsYYYYMMddHHmm(Calendar cal){
        if(cal == null)cal = Calendar.getInstance();
        return  cal.get(Calendar.YEAR)+"-"+
                decimalFormat.format((cal.get(Calendar.MONTH)+1))+"-"+
                decimalFormat.format(cal.get(Calendar.DATE))+" "+
                decimalFormat.format(cal.get(Calendar.HOUR_OF_DAY))+":"+
                decimalFormat.format(cal.get(Calendar.MINUTE));
    }

    static String getStringDateAsYYYYMMddHHmmss(Calendar cal){
        if(cal == null)cal = Calendar.getInstance();

        return  cal.get(Calendar.YEAR)+"-"+
                decimalFormat.format((cal.get(Calendar.MONTH)+1))+"-"+
                decimalFormat.format(cal.get(Calendar.DATE))+" "+
                decimalFormat.format(cal.get(Calendar.HOUR_OF_DAY))+":"+
                decimalFormat.format(cal.get(Calendar.MINUTE))+":"+
                decimalFormat.format(cal.get(Calendar.SECOND));

    }


    static Calendar convertStringToCalendarFULL(String date){

        Calendar cal= Calendar.getInstance();
        Date d;
        if (date==null || TextUtils.isEmpty(date)) {
            return  cal;
        }

        try {
            final SimpleDateFormat fullDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

            d=fullDF.parse(date);
            cal.setTime(d);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return  cal;
    }


    static String getFinishDate(String spentDate, int installmentCount) {

        if (installmentCount == 1)
            return  spentDate;

        String finishDate;
        Calendar cal = convertStringToCalendarFULL(spentDate);
        int position = (installmentCount-1);
        int starDay = cal.get(Calendar.DATE);

        cal.add(Calendar.MONTH, position); //2015 6 4

        int currentYear = cal.get(Calendar.YEAR); //2015
        int currentMonth = cal.get(Calendar.MONTH);//2015
        int actualDay = cal.getActualMaximum(Calendar.DATE);//30
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);


        if (actualDay > starDay) { //30> 1
            finishDate = currentYear + "-" + decimalFormat.format((currentMonth+1)) + "-" + decimalFormat.format(starDay);
        } else {//30,31
            finishDate = currentYear+"-" + decimalFormat.format((currentMonth+1)) + "-" + decimalFormat.format(actualDay);
        }

        finishDate +=  " " +
                decimalFormat.format(hour) +":"+
                decimalFormat.format(minute) +":"+
                decimalFormat.format(second);

        SimpleDateFormat fullDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
        try {
            Date d = fullDF.parse(finishDate);
            finishDate = fullDF.format(d);
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return finishDate;
    }

    static int getDiffDateAsCalendar(Calendar before, Calendar after) {

        Calendar newBefore = Calendar.getInstance();
        newBefore.set(before.get(Calendar.YEAR), before.get(Calendar.MONTH), before.get(Calendar.DATE));
        Calendar newAfter = Calendar.getInstance();
        newAfter.set(after.get(Calendar.YEAR), after.get(Calendar.MONTH), after.get(Calendar.DATE));

        long start, end;
        start = newBefore.getTimeInMillis();
        end = newAfter.getTimeInMillis();
        Long diff = (end - start) / (1000 * 60 * 60 * 24);

        return diff.intValue()+1;

    }

}