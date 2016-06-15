package me.yczhang.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by YCZhang on 11/26/15.
 */
public class GZipUtil {

	public static byte[] gzip(byte[] source) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gos = new GZIPOutputStream(baos);
		gos.write(source, 0, source.length);
		gos.finish();
		byte[] ret = baos.toByteArray();
		gos.close();
		baos.close();

		return ret;
	}

	public static byte[] ungzip(byte[] source) throws IOException {
		GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(source));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] bs = new byte[1024];
		while (true) {
			int l = gis.read(bs, 0, 1024);
			if (l < 0)
				break;
			baos.write(bs, 0, l);
		}
		byte[] ret = baos.toByteArray();
		gis.close();
		baos.close();

		return ret;
	}
}
