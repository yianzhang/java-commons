package me.yczhang.util;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YCZhang on 3/25/16.
 */
public class PatternUtil {

	public static final Pattern DATE_PATTERN = Pattern.compile("%d\\{([^\\}]*)\\}");
	public static String dateConvert(@Nonnull String patternStr, @Nonnull LocalDateTime dateTime) {
		Matcher matcher = DATE_PATTERN.matcher(patternStr);
		String ret = patternStr;
		while (matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); ++i) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern(matcher.group(i));
				String replaceStr = dateTime.format(formatter);
				ret = ret.replace(matcher.group(), replaceStr);
			}
		}
		return ret;
	}
}
