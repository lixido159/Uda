package com.example.m.calendertwo;

import java.util.Calendar;

public class Festival {

    public static String getFestival(int lunarMonth, int lunarDay,int year ,int nowMonth, int nowDay){
        switch (lunarMonth){
            case 1:
                switch (lunarDay){
                    case 1:
                        return "春节";
                    case 15:
                        return "元宵节";
                };
                break;
            case 5:
                if (lunarDay==5)
                    return "端午节";
                break;
            case 7:
                switch (lunarDay){
                    case 7:
                        return "七夕节";
                    case 15:
                        return "中元节";
                };
                break;
            case 8:
                if (lunarDay==15)
                    return "中秋节";
                break;
            case 9:
                if (lunarDay==9)
                    return "重阳节";
                break;
            case 12:
                switch (lunarDay){
                    case 8:
                        return "腊八节";
                    case 23:
                        return "小年";
                    case 30:
                        return "除夕";
                };
                break;
        }
        switch (nowMonth){
            case 1:
                switch (nowDay){
                    case 1:
                        return "元旦";
                };
                break;
            case 2:
                switch (nowDay){
                    case 14:
                        return "情人节";
                };
                break;
            case 3:
                switch (nowDay){
                    case 8:
                        return "妇女节";
                    case 12:
                        return "植树节";
                };
                break;
            case 4:
                switch (nowDay){
                    case 1:
                        return "愚人节";
                    case 5:
                        return "清明节";
                };
                break;
            case 5:
                switch (nowDay){
                    case 1:
                        return "劳动节";
                    case 4:
                        return "青年节";
                };
                break;
            case 6:
                switch (nowDay){
                    case 1:
                        return "儿童节";
                };
                break;
            case 7:
                switch (nowDay){
                    case 1:
                        return "建党节";
                };
                break;
            case 8:
                switch (nowDay){
                    case 1:
                        return "建军节";
                };
                break;
            case 9:
                switch (nowDay){
                    case 10:
                        return "教师节";
                };
                break;
            case 10:
                switch (nowDay){
                    case 1:
                        return "国庆节";
                };
                break;
            case 12:
                switch (nowDay){
                    case 24:
                        return "平安夜";
                    case 25:
                        return "圣诞节";
                };
                break;
        }
        if ((nowMonth==5&&nowDay>=8&&nowDay<=14)||(nowMonth==6&&nowDay>=15&&nowDay<=21)||
                (nowMonth==11&&nowDay>=22&&nowDay<=28)){
            Calendar calendar=Calendar.getInstance();
            calendar.set(Calendar.YEAR,year);
            calendar.set(Calendar.MONTH,nowMonth-1);
            calendar.set(Calendar.DAY_OF_MONTH,nowDay);
            int week=calendar.get(Calendar.WEEK_OF_MONTH);
            int day=calendar.get(Calendar.DAY_OF_WEEK);
            calendar.set(Calendar.DAY_OF_MONTH,1);
            //第一天是周日
            if (calendar.get(Calendar.DAY_OF_WEEK)<=Calendar.SUNDAY){
                switch (nowMonth){
                    case 5:
                        if (week==2&&day==Calendar.SUNDAY)
                            return "母亲节";
                        break;
                    case 6:
                        if (week==3&&day==Calendar.SUNDAY)
                            return "父亲节";
                        break;
                    case 11:
                        if (week==4&&day==Calendar.THURSDAY)
                            return "感恩节";
                        break;
                }
            }else if (calendar.get(Calendar.DAY_OF_WEEK)<=Calendar.THURSDAY){//周一到周四
                switch (nowMonth){
                    case 5:
                        if (week==3&&day==Calendar.SUNDAY)
                            return "母亲节";
                        break;
                    case 6:
                        if (week==4&&day==Calendar.SUNDAY)
                            return "父亲节";
                        break;
                    case 11:
                        if (week==4&&day==Calendar.THURSDAY)
                            return "感恩节";
                        break;
                }
            }else {
                switch (nowMonth){
                    case 5:
                        if (week==3&&day==Calendar.SUNDAY)
                            return "母亲节";
                        break;
                    case 6:
                        if (week==4&&day==Calendar.SUNDAY)
                            return "父亲节";
                        break;
                    case 11:
                        if (week==5&&day==Calendar.THURSDAY)
                            return "感恩节";
                        break;
                }
            }
        }
        return DateInfo.getChinaDayString(lunarDay);
    }
}
