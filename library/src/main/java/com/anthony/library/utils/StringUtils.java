package com.anthony.library.utils;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;

import com.anthony.library.R;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static String getMoney(float cost) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return "￥" + decimalFormat.format(cost);
    }

    public static class PrimaryClickableSpan extends ClickableSpan {

        private Context context;

        public PrimaryClickableSpan(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View widget) {
            // do nothing
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.colorPrimary));
            ds.setUnderlineText(false);
        }
    }

    public static int getChineseCount(CharSequence str) {
        String regEx = "[\\u4e00-\\u9fa5]";
        int count = 0;
        if (!TextUtils.isEmpty(str)) {
            Pattern p = Pattern.compile(regEx);// 计算有几个unicode字
            Matcher m = p.matcher(str);
            while (m.find()) {
                for (int i = 0; i <= m.groupCount(); i++) {
                    count = count + 1;
                }
            }
            count += str.length();
        }
        return count;
    }

    /**
     * return if str is empty
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.isEmpty() || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }



    /**
     * decode Unicode string
     *
     * @param s
     * @return
     */
    public static String decodeUnicodeStr(String s) {
        StringBuilder sb = new StringBuilder(s.length());
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '\\' && chars[i + 1] == 'u') {
                char cc = 0;
                for (int j = 0; j < 4; j++) {
                    char ch = Character.toLowerCase(chars[i + 2 + j]);
                    if ('0' <= ch && ch <= '9' || 'a' <= ch && ch <= 'f') {
                        cc |= (Character.digit(ch, 16) << (3 - j) * 4);
                    } else {
                        cc = 0;
                        break;
                    }
                }
                if (cc > 0) {
                    i += 5;
                    sb.append(cc);
                    continue;
                }
            }
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * encode Unicode string
     *
     * @param s
     * @return
     */
    public static String encodeUnicodeStr(String s) {
        StringBuilder sb = new StringBuilder(s.length() * 3);
        for (char c : s.toCharArray()) {
            if (c < 256) {
                sb.append(c);
            } else {
                sb.append("\\u");
                sb.append(Character.forDigit((c >>> 12) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 8) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 4) & 0xf, 16));
                sb.append(Character.forDigit((c) & 0xf, 16));
            }
        }
        return sb.toString();
    }



    // 基本数据类型
    private final static String[] types = {"int", "java.lang.String", "boolean", "char",
            "float", "double", "long", "short", "byte"};
    /**
     * 将对象转化为String
     *
     * @param object
     * @return
     */
    public static <T> String objectToString(T object) {
        if (object == null) {
            return "Object{object is null}";
        }
        if (object.toString().startsWith(object.getClass().getName() + "@")) {
            StringBuilder builder = new StringBuilder(object.getClass().getSimpleName() + "{");
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                boolean flag = false;
                for (String type : types) {
                    if (field.getType().getName().equalsIgnoreCase(type)) {
                        flag = true;
                        Object value = null;
                        try {
                            value = field.get(object);
                        } catch (IllegalAccessException e) {
                            value = e;
                        } finally {
                            builder.append(String.format("%s=%s, ", field.getName(),
                                    value == null ? "null" : value.toString()));
                            break;
                        }
                    }
                }
                if (!flag) {
                    builder.append(String.format("%s=%s, ", field.getName(), "Object"));
                }
            }
            return builder.replace(builder.length() - 2, builder.length() - 1, "}").toString();
        } else {
            return object.toString();
        }
    }
}
