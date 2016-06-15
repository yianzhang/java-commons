package me.yczhang.util;

import java.io.*;
import java.util.zip.*;

public class ZipUtil {

	public static void zip(File[] sources, File destination, boolean overwrite) throws IOException {
		for (File f : sources) {
			if (!f.exists())
				throw new ZipException(String.format("source %s not exists", f.getAbsolutePath()));
		}

		File p = destination.getParentFile();
		if (!p.exists()) {
			if (!p.mkdirs())
				throw new ZipException(String.format("parent direction %s mkdirs error", p.getAbsolutePath()));
		}
		else if (p.isFile()) {
			throw new ZipException(String.format("parent direction %s not direction", p.getAbsolutePath()));
		}

		if (destination.exists() && !destination.isFile())
			throw new ZipException(String.format("destination ％s is a direction", destination.getAbsolutePath()));
		else if (destination.exists() && !overwrite)
			throw new ZipException(String.format("destination %s exists", destination.getAbsolutePath()));

		ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destination));
		try {
			for (File f : sources) {
				_zip(f, zos, f.getName());
			}
		} finally {
			zos.close();
		}
	}

	public static void zip(File source, File destination, boolean overwrite) throws IOException {
		zip(new File[]{source}, destination, overwrite);
	}

	public static void zip(String[] sources, String destination, boolean overwrite) throws IOException {
		File[] files = new File[sources.length];
		for (int i = 0; i < sources.length; ++i) {
			files[i] = new File(sources[i]);
		}

		zip(files, new File(destination), overwrite);
	}

	public static void zip(String source, String destination, boolean overwrite) throws IOException {
		zip(new File[]{new File(source)}, new File(destination), overwrite);
	}

	private static void _zip(File f, ZipOutputStream zos, String n) throws IOException {
		if (f.isFile()) {
			zos.putNextEntry(new ZipEntry(n));
			BufferedInputStream is = new BufferedInputStream(new FileInputStream(f));
			try {
				byte[] b = new byte[1024];
				for (;true;) {
					int l = is.read(b, 0, 1024);
					if (l==-1) {
						break;
					}
					if (l==0) {
						continue;
					}
					
					zos.write(b, 0, l);
				}
			} finally {
				is.close();
			}
		} else {
			File[] fs = f.listFiles();
			for (File f0:fs) {
				_zip(f0, zos, n+"/"+f0.getName());
			}
		}
	}

	public static void unzip(File zip_file, File destination_direction, boolean overwrite) throws IOException {
		if (!zip_file.exists())
			throw new ZipException(String.format("zip file %s not exists", zip_file.getAbsolutePath()));

		if (!zip_file.isFile())
			throw new ZipException(String.format("zip file %s is not a file", zip_file.getAbsolutePath()));

		if (destination_direction.exists() && destination_direction.isFile())
			throw new ZipException(String.format("destination direction %s is not a direction", destination_direction.getAbsolutePath()));

		if (!destination_direction.exists() && !destination_direction.mkdirs())
			throw new ZipException(String.format("destination direction %s mkdirs error", destination_direction.getAbsolutePath()));

		ZipFile zf = new ZipFile(zip_file);
		ZipInputStream zis = new ZipInputStream(new FileInputStream(zip_file));
		try {
			for (;true;) {
				ZipEntry ze = zis.getNextEntry();
				if (ze==null) {
					break;
				}

				File f = new File(destination_direction.getPath()+"/"+ze.getName());

				if (ze.isDirectory()) {
					f.mkdirs();
					continue;
				}

				if (f.exists()) {
					if (!overwrite) {
						throw new ZipException(f.getPath()+" exists");
					}
					if (!f.isFile()) {
						throw new ZipException(f.getPath()+" exists and is a dir");
					}

					f.delete();
				}

				File pf = f.getParentFile();
				if (!pf.exists()) {
					pf.mkdirs();
				}

				BufferedInputStream is = new BufferedInputStream(zf.getInputStream(ze));
				BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(f));
				try {
					byte[] b = new byte[1024];
					for (;true;) {
						int l = is.read(b, 0, 1024);
						if (l==-1) {
							break;
						}
						if (l==0) {
							continue;
						}

						os.write(b, 0, l);
					}
				} finally {
					is.close();
					os.close();
				}
			}
		} finally {
			zis.close();
		}
	}

	public static void unzip(String zip_file, String destination_direction, boolean overwrite) throws IOException {
		unzip(new File(zip_file), new File(destination_direction), overwrite);
	}
}
