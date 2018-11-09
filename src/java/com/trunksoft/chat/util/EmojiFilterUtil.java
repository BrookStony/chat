package com.trunksoft.chat.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/6/1.
 */
public class EmojiFilterUtil {

    private static final Pattern pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
            Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE);

    public static String filterEmoji(String source) {
        if(source != null)
        {
            source = source.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*");
            Matcher matcher = pattern.matcher(source);
            if ( matcher.find())
            {
                source = matcher.replaceAll("*");
                return source;
            }
            return source;
        }
        return source;
    }
}
