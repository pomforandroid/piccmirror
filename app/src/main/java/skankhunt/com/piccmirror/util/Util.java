package skankhunt.com.piccmirror.util;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by skankhunt on 2018/1/25.
 */

public class Util {

    //手机号码的正则表达式
    public final static String PHONE_PATTERN = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";


    /**
     * 检测是否11位手机号码
     * @param input
     * @return
     */
    public static boolean isPhoneNum( CharSequence input) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return true;
        }
        return false;
    }

    public static void showSnackbar(View parentView, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG).show();
    }
}
