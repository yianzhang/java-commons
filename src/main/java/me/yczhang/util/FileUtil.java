package me.yczhang.util;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Objects;

/**
 * Created by YCZhang on 5/18/15.
 */
public class FileUtil {

	public static void mkParentDir(String filePath) {
		new File(filePath).getParentFile().mkdirs();
	}

	public static void clearDir(@Nonnull File dir) {
		Objects.requireNonNull(dir);

		if (dir.isFile())
			return;

		File[] fs = dir.listFiles();
		for (File f : fs) {
			clearDir(f);
			f.delete();
		}
	}

	public static void clearDir(String dirPath) {
		clearDir(new File(dirPath));
	}

	public static void del(File dir) {
		clearDir(dir);
		dir.delete();
	}
}
