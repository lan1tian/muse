package com.mogujie.jarvis.tasks.util;

import com.google.common.primitives.Ints;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 时间参数解析工具
 */
public class HiveScriptParamUtils {
    private static final String defaultFormat = "yyyyMMdd";
    private static final String defaultFormatMGD = "yyyyMMdd HHmmss";

    public static String parse(String text, DateTime date) {
        Map<String, String> dates = new HashMap<>();
        Pattern patternYtd = Pattern.compile("\\$YTD\\(.*?\\)|\\$\\{YTD\\(.*?\\)\\}");
        Pattern patternMgd = Pattern.compile("\\$MGD\\(.*?\\)|\\$\\{MGD\\(.*?\\)\\}");
        Matcher matcherYtd = patternYtd.matcher(text);
        Matcher matcherMgd = patternMgd.matcher(text);
        while (matcherYtd.find()) {
            String s = matcherYtd.group();
            dates.put(s, getDateStr4YTD(s, date));
        }
        while (matcherMgd.find()) {
            String s = matcherMgd.group();
            dates.put(s, replaceWithDateStrMGD(s));
        }
        for (Entry<String, String> entry : dates.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        return text;
    }

    private static String getDateStr4YTD(String paramInput, DateTime date) {
        String params = paramInput.substring(paramInput.indexOf('(') + 1, paramInput.indexOf(')'));
        if (params.equals("")) {
            return getDateStr4YTD(0, defaultFormat, date);
        }
        String[] param = params.split(",");
        if (param.length == 1) {
            Integer plusDay = Ints.tryParse(param[0]);
            if (plusDay != null) {
                return getDateStr4YTD(plusDay, defaultFormat, date);
            } else {
                return getDateStr4YTD(0, param[0], date);
            }
        } else if (param.length == 2) {
            Integer plusDay = Ints.tryParse(param[0]);
            if (plusDay == null) {
                throw new IllegalArgumentException("日期参数不合法。");
            }
            return getDateStr4YTD(plusDay, param[1], date);
        } else {
            throw new IllegalArgumentException("日期参数不合法。");
        }
    }

    private static String getDateStr4YTD(int plusDay, String format, DateTime date) {
        date = date.plusDays(plusDay);
        return DateTimeFormat.forPattern(format).print(date);
    }


    private static String replaceWithDateStrMGD(String dateParam) {
        String destStr = null;
        String params = dateParam.substring(dateParam.indexOf('(') + 1, dateParam.indexOf(')'));
        switch (countParam(params)) {
            case 0:
                destStr = getDateStrMGD();
                break;
            case 1:
                destStr = getDateStrMGD(params);
                break;
            case 2:
                String[] param = params.split(",");
                // 传入的是日期格式
                destStr = getDateStrMGD(Integer.parseInt(param[0].substring(0, param[0].length() - 1)),
                        param[1], param[0].substring(param[0].length() - 1, param[0].length()));
                break;

            default:
                break;
        }
        return destStr;
    }


    private static int countParam(String str) {
        // 没有参数
        if (str.equals("")) {
            return 0;
        } else if (str.contains(",")) {
            // 两个参数
            return 2;
        } else {
            return 1;
        }
    }

    private static String getDateStrMGD() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date date = cal.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(defaultFormatMGD);
        return sdf.format(date);
    }

    private static String getDateStrMGD(String param) {
        SimpleDateFormat sdf = null;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        int hour = 0, min = 0, sec = 0;
        if (StringUtils.isNumeric(param.substring(0, param.length() - 1))) {
            // 传入的是加减数
            int num = Integer.parseInt(param.substring(0, param.length() - 1));
            switch (param.substring(param.length() - 1, param.length())) {
                case "Y":
                case "y":
                    cal.add(Calendar.YEAR, num);
                    break;
                case "M":
                case "m":
                    cal.add(Calendar.MONTH, num);
                    break;
                case "D":
                case "d":
                    cal.add(Calendar.DATE, num);
                    break;
                case "H":
                case "h":
                    cal.add(Calendar.HOUR, num);
                    break;
                case "I":
                case "i":
                    cal.add(Calendar.MINUTE, num);
                    break;
                case "S":
                case "s":
                    cal.add(Calendar.SECOND, num);
            }

            sdf = new SimpleDateFormat(defaultFormatMGD);
        } else {
            // 传入的是日期格式
            if (param.indexOf("HH") != -1) {
                int idxHour = param.indexOf(":");
                if (StringUtils.isNumeric(param.substring(idxHour - 2, idxHour))) {
                    hour = Integer.parseInt(param.substring(idxHour - 2, idxHour));
                    StringBuffer paramBuffer = new StringBuffer(param);
                    paramBuffer.replace(idxHour - 2, idxHour, "HH");
                    param = paramBuffer.toString();
                }
                int idxMin = param.indexOf(":", idxHour + 1);
                if (StringUtils.isNumeric(param.substring(idxMin - 2, idxMin))) {
                    min = Integer.parseInt(param.substring(idxMin - 2, idxMin));
                    StringBuffer paramBuffer = new StringBuffer(param);
                    paramBuffer.replace(idxMin - 2, idxMin, "mm");
                    param = paramBuffer.toString();
                }
                if (StringUtils.isNumeric(param.substring(idxMin + 1, idxMin + 3))) {
                    sec = Integer.parseInt(param.substring(idxMin + 1, idxMin + 3));
                    StringBuffer paramBuffer = new StringBuffer(param);
                    paramBuffer.replace(idxMin + 1, idxMin + 3, "ss");
                    param = paramBuffer.toString();
                }
            }
            sdf = new SimpleDateFormat(param);
        }
        if (hour != 0)
            cal.set(Calendar.HOUR_OF_DAY, hour);// 时
        if (min != 0)
            cal.set(Calendar.MINUTE, min);// 分
        if (sec != 0)
            cal.set(Calendar.SECOND, sec);// 秒

        return sdf.format(cal.getTime());
    }


    private static String getDateStrMGD(int amount, String format, String type) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        switch (type) {
            case "Y":
            case "y":
                cal.add(Calendar.YEAR, amount);
                break;
            case "M":
            case "m":
                cal.add(Calendar.MONTH, amount);
                break;
            case "D":
            case "d":
                cal.add(Calendar.DATE, amount);
                break;
            case "H":
            case "h":
                cal.add(Calendar.HOUR, amount);
                break;
            case "I":
            case "i":
                cal.add(Calendar.MINUTE, amount);
                break;
            case "S":
            case "s":
                cal.add(Calendar.SECOND, amount);
        }
        int hour = 0, min = 0, sec = 0;
        if (format.indexOf("HH") == -1 || format.indexOf("mm") == -1 || format.indexOf("ss") == -1) {
            int idxHour = format.indexOf(":");
            if (StringUtils.isNumeric(format.substring(idxHour - 2, idxHour))) {
                hour = Integer.parseInt(format.substring(idxHour - 2, idxHour));
                StringBuffer paramBuffer = new StringBuffer(format);
                paramBuffer.replace(idxHour - 2, idxHour, "HH");
                format = paramBuffer.toString();
            }
            int idxMin = format.indexOf(":", idxHour + 1);
            if (StringUtils.isNumeric(format.substring(idxMin - 2, idxMin))) {
                min = Integer.parseInt(format.substring(idxMin - 2, idxMin));
                StringBuffer paramBuffer = new StringBuffer(format);
                paramBuffer.replace(idxMin - 2, idxMin, "mm");
                format = paramBuffer.toString();
            }
            if (StringUtils.isNumeric(format.substring(idxMin + 1, idxMin + 3))) {
                sec = Integer.parseInt(format.substring(idxMin + 1, idxMin + 3));
                StringBuffer paramBuffer = new StringBuffer(format);
                paramBuffer.replace(idxMin + 1, idxMin + 3, "ss");
                format = paramBuffer.toString();
            }
        }

        if (hour != 0)
            cal.set(Calendar.HOUR_OF_DAY, hour);// 时
        if (min != 0)
            cal.set(Calendar.MINUTE, min);// 分
        if (sec != 0)
            cal.set(Calendar.SECOND, sec);// 秒

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(cal.getTime());
    }


    public static void main(String[] args) {

        String text;

        DateTime today = DateTime.now();
        text = parse("where paytime >= unix_timestamp('$MGD(-1M, yyyy-MM-dd 00:00:00)')",today);

        System.out.println(parse(
                " dwd_usr_users_${YTD(-1,yyyy-MM-dd)}    from dwd_usr_users_${YTD(-1)} a left outer join (select userid,min(realname) realname,min(province) province, min(city) city,min(area) area,min(address) address"
                        + " from dwd_usr_address_${YTD(yyyy-MM-dd)}  group by userid )b "
                        + "on(a.userid=b.userid) left outer join dwd_usr_extra_${YTD(yyyy-MM-dd)} c on(a.userid=c.userid)"
                        + " where a.userid is not null",today));


        System.out.println(parse(
                " dwd_usr_users_$YTD(-1,yyyy-MM-dd)    from dwd_usr_users_$YTD(-1) a left outer join (select userid,min(realname) realname,min(province) province, min(city) city,min(area) area,min(address) address"
                        + " from dwd_usr_address_$YTD(yyyy-MM-dd)  group by userid )b "
                        + "on(a.userid=b.userid) left outer join dwd_usr_extra_$YTD(yyyy-MM-dd) c on(a.userid=c.userid)"
                        + " where a.userid is not null",today));

        System.out.println(parse("$MGD(-1d,yyyy-MM-dd 00:00:00)",today));
        text = "select count(distinct e2.buyeruserid) buyercnt, e1.level userlevel from user_score$tmptable e1\n" +
                "join dwd_trd_tradeorder e2 on e1.userid = e2.buyeruserid \n" +
                "where e2.paytime >= unix_timestamp('$MGD(-31d,yyyy-MM-dd 00:00:00)') AND \n" +
                "e2.paytime <= unix_timestamp('$MGD(-1d, yyyy-MM-dd 00:00:00)') and e2.level = 2\n" +
                "GROUP BY e1.level"
                + "union"
                + "insert overwrite table mid_member_level_buy partition (stat_date='$YTD(yyyy-MM-dd)')\n" +
                "select * from (\n" +
                "select buyeruserid, count(distinct from_unixtime(paytime, 'yyyyMMdd')) cnt \n" +
                "from dwd_trd_tradeorder \n" +
                "where paytime >= unix_timestamp('$MGD(-1M, yyyy-MM-dd 00:00:00)') \n" +
                "and paytime <= unix_timestamp('$MGD(yyyy-MM-dd 23:59:59)')\n" +
                "and level = 2\n" +
                "group by buyeruserid) x where x.cnt >= 3;"
        ;
        System.out.println(parse(text,today));
    }


}
