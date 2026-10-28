package io.github.mansouralotaibi.ufuq;

import java.time.LocalDate;
import java.time.chrono.HijrahDate;
import java.time.temporal.ChronoField;

/** Standard solar-position calculation with Umm al-Qura prayer parameters. */
final class PrayerCalculator {
    static final class Times { final int fajr,sunrise,dhuhr,asr,maghrib,isha; Times(int f,int s,int d,int a,int m,int i){fajr=f;sunrise=s;dhuhr=d;asr=a;maghrib=m;isha=i;} }
    static Times calculate(LocalDate date,double lat,double lon,double zone){
        double jd=julian(date.getYear(),date.getMonthValue(),date.getDayOfMonth())-lon/(15d*24d);
        double fajr=adjust(angleTime(jd,lat,18.5,5,true),zone,lon), sunrise=adjust(angleTime(jd,lat,.833,6,true),zone,lon), dhuhr=adjust(midDay(jd,12),zone,lon);
        double asr=adjust(asrTime(jd,lat,1,13),zone,lon), maghrib=adjust(angleTime(jd,lat,.833,18,false),zone,lon);
        int month=HijrahDate.from(date).get(ChronoField.MONTH_OF_YEAR), m=minute(maghrib);
        return new Times(minute(fajr),minute(sunrise),minute(dhuhr),minute(asr),m,m+(month==9?120:90));
    }
    private static double julian(int y,int m,int d){if(m<=2){y--;m+=12;}int a=y/100,b=2-a+a/4;return Math.floor(365.25*(y+4716))+Math.floor(30.6001*(m+1))+d+b-1524.5;}
    private static double[] sun(double jd,double t){double D=jd+t/24d-2451545,g=fix(357.529+.98560028*D,360),q=fix(280.459+.98564736*D,360);double L=fix(q+1.915*sin(g)+.020*sin(2*g),360),e=23.439-.00000036*D;double ra=fix(deg(Math.atan2(cos(e)*sin(L),cos(L)))/15d,24);return new double[]{deg(Math.asin(sin(e)*sin(L))),q/15d-ra};}
    private static double midDay(double jd,double t){return fix(12-sun(jd,t)[1],24);}
    private static double angleTime(double jd,double lat,double angle,double t,boolean before){double decl=sun(jd,t)[0],noon=midDay(jd,t);double x=(-sin(angle)-sin(decl)*sin(lat))/(cos(decl)*cos(lat));double delta=deg(Math.acos(Math.max(-1,Math.min(1,x))))/15d;return noon+(before?-delta:delta);}
    private static double asrTime(double jd,double lat,double factor,double t){double decl=sun(jd,t)[0];double angle=-deg(Math.atan(1d/(factor+Math.tan(rad(Math.abs(lat-decl))))));return angleTime(jd,lat,angle,t,false);}
    private static double adjust(double h,double z,double lon){return h+z-lon/15d;} private static int minute(double h){return(int)Math.round(fix(h,24)*60d)%1440;}
    private static double fix(double v,double max){v%=max;return v<0?v+max:v;} private static double rad(double d){return d*Math.PI/180d;} private static double deg(double r){return r*180d/Math.PI;} private static double sin(double d){return Math.sin(rad(d));} private static double cos(double d){return Math.cos(rad(d));}
    private PrayerCalculator(){}
}
