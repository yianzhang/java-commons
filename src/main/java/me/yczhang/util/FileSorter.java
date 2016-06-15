package me.yczhang.util;

import me.yczhang.data_structure.tuple.Tuple;
import me.yczhang.data_structure.tuple.Tuple2;
import me.yczhang.data_structure.tuple.Tuple3;
import me.yczhang.trait.Filter;
import me.yczhang.trait.Transformation;
import org.apache.commons.lang3.ObjectUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 利用磁盘对大文件进行排序
 * Created by YCZhang on 4/20/16.
 */
public class FileSorter {

	private final int DEFAULT_TEMP_FILE_LINE_COUNT = 100000;
	private final int DEFAULT_MERGE_FILE_COUNT = 10;

	private int tempFileLineCount = DEFAULT_TEMP_FILE_LINE_COUNT;
	private int mergeFileCount = DEFAULT_MERGE_FILE_COUNT;
	private File tempDir = new File("large_file_sort_temp_" + System.nanoTime());
	private Comparator<String> comparator;
	private Filter<String> defaultFilter;
	private Transformation<String, String> defaultTransformation;
	private Charset defaultSourceCharset = Charset.forName("utf-8");
	private Charset sinkCharset = Charset.forName("utf-8");

	private Map<File, Tuple3<Charset, Filter<String>, Transformation<String, String>>> sources = new HashMap<>();
	private File sink;

	public FileSorter(@Nonnull List<File> sources, @Nonnull File sink) {
		for (File source : sources) {
			this.sources.put(source, Tuple.of(null, null, null));
		}
		this.sink = sink;
	}

	public FileSorter(@Nonnull File sink) {
		this.sink = sink;
	}

	/**
	 * 新建一个实例
	 * @param sources
	 * @param sink
	 * @return
	 */
	public static FileSorter create(@Nonnull List<File> sources, @Nonnull File sink) {
		return new FileSorter(sources, sink);
	}

	/**
	 * 新建一个实例
	 * @param sources
	 * @param sink
	 * @return
	 */
	public static FileSorter create(@Nonnull File[] sources, @Nonnull File sink) {
		return new FileSorter(Arrays.asList(sources), sink);
	}

	/**
	 * 新建一个实例
	 * @param sink
	 * @return
	 */
	public static FileSorter create(@Nonnull File sink) {
		return new FileSorter(sink);
	}

	/**
	 * 增加需要排序的文件
	 * @param source
	 * @param charset
	 * @param filter
	 * @param transformation
	 * @return
	 */
	public FileSorter addSource(@Nonnull File source, @Nullable String charset, @Nullable Filter<String> filter, @Nullable Transformation<String, String> transformation) {
		this.sources.put(source, Tuple.of(charset != null ? Charset.forName(charset) : null, filter, transformation));
		return this;
	}

	/**
	 * 增加需要排序的文件
	 * @param source
	 * @param charset
	 * @return
	 */
	public FileSorter addSource(@Nonnull File source, @Nullable String charset) {
		this.sources.put(source, Tuple.of(charset != null ? Charset.forName(charset) : null, null, null));
		return this;
	}

	/**
	 * 增加需要排序的文件
	 * @param source
	 * @param transformation
	 * @return
	 */
	public FileSorter addSource(@Nonnull File source, @Nullable Transformation<String, String> transformation) {
		this.sources.put(source, Tuple.of(null, null, transformation));
		return this;
	}

	/**
	 * 增加需要排序的文件
	 * @param source
	 * @param charset
	 * @param transformation
	 * @return
	 */
	public FileSorter addSource(@Nonnull File source, @Nullable String charset, @Nullable Transformation<String, String> transformation) {
		this.sources.put(source, Tuple.of(charset != null ? Charset.forName(charset) : null, null, transformation));
		return this;
	}

	/**
	 * 分割的文件行数
	 * @param tempFileLineCount
	 * @return
	 */
	public FileSorter setTempFileLineCount(int tempFileLineCount) {
		if (tempFileLineCount > 0)
			this.tempFileLineCount = tempFileLineCount;
		else
			this.tempFileLineCount = DEFAULT_TEMP_FILE_LINE_COUNT;
		return this;
	}

	/**
	 * 每次合并的文件个数
	 * @param mergeFileCount
	 * @return
	 */
	public FileSorter setMergeFileCount(int mergeFileCount) {
		if (mergeFileCount > 1)
			this.mergeFileCount = mergeFileCount;
		else
			this.mergeFileCount = DEFAULT_MERGE_FILE_COUNT;
		return this;
	}

	/**
	 * 设置存储临时文件的目录
	 * @param tempDir
	 * @return
	 */
	public FileSorter setTempDir(@Nonnull File tempDir) {
		this.tempDir = tempDir;
		return this;
	}

	/**
	 * 设置默认的过滤器
	 * @param defaultFilter
	 * @return
	 */
	public FileSorter setDefaultFilter(@Nullable Filter<String> defaultFilter) {
		this.defaultFilter = defaultFilter;
		return this;
	}

	/**
	 * 设置默认的格式转换器
	 * @param transformation
	 * @return
	 */
	public FileSorter setDefaultTransformation(@Nullable Transformation<String, String> transformation) {
		this.defaultTransformation = transformation;
		return this;
	}

	/**
	 * 设置比较器
	 * @param comparator
	 * @return
	 */
	public FileSorter setDefaultComparator(@Nullable Comparator<String> comparator) {
		this.comparator = comparator;
		return this;
	}

	/**
	 * 设置默认的源文件编码
	 * @param charset
	 * @return
	 */
	public FileSorter setDefaultSourceCharset(@Nonnull Charset charset) {
		this.defaultSourceCharset = charset;
		return this;
	}

	/**
	 * 设置汇文件编码
	 * @param charset
	 * @return
	 */
	public FileSorter setSinkCharset(@Nonnull Charset charset) {
		this.sinkCharset = charset;
		return this;
	}

	/**
	 * 排序
	 * @throws IOException
	 */
	public void sort() throws IOException {
		tempDir.mkdirs();

		FileUtil.clearDir(tempDir);

		int files = _split(sources, new File(tempDir, "0"));
		int i = 0;
		while (files > 1) {
			++i;
			_merge(new File(tempDir, String.valueOf(i - 1)), new File(tempDir, String.valueOf(i)));
			files = (files / mergeFileCount) + (files % mergeFileCount == 0 ? 0 : 1);
		}

		if (sink.exists() && sink.isFile())
			sink.delete();
		sink.getParentFile().mkdirs();
		new File(tempDir + "/" + i + "/0").renameTo(sink);

		FileUtil.del(tempDir);
	}

	private int _split(Map<File, Tuple3<Charset, Filter<String>, Transformation<String, String>>> sources, File tempDir) throws IOException {
		tempDir.mkdirs();

		TreeSet<Tuple2<Long, String>> sortedData = null;
		if (comparator == null)
			sortedData = new TreeSet<>(new Comparator<Tuple2<Long, String>>() {
				@Override
				public int compare(Tuple2<Long, String> o1, Tuple2<Long, String> o2) {
					int c = o1.e2.compareTo(o2.e2);
					if (c != 0)
						return c;
					return o1.e1.compareTo(o2.e1);
				}
			});
		else
			sortedData = new TreeSet<>(new Comparator<Tuple2<Long, String>>() {
				@Override
				public int compare(Tuple2<Long, String> o1, Tuple2<Long, String> o2) {
					int c = comparator.compare(o1.e2, o2.e2);
					if (c != 0)
						return c;
					return o1.e1.compareTo(o2.e1);
				}
			});

		int i = 0;
		long index = 0;
		for (File source : sources.keySet()) {
			if (source == null || !source.exists() || !source.isFile())
				continue;

			Tuple3<Charset, Filter<String>, Transformation<String, String>> process = sources.get(source);
			Charset sourceCharset = ObjectUtils.defaultIfNull(process.e1, defaultSourceCharset);
			Filter<String> filter = ObjectUtils.defaultIfNull(process.e2, defaultFilter);
			Transformation<String, String> transformation = ObjectUtils.defaultIfNull(process.e3, defaultTransformation);

			try (
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(source), sourceCharset))
			) {
				while (true) {
					String line = reader.readLine();
					if (line == null)
						break;
					if (filter != null && !filter.accept(line))
						continue;
					if (transformation != null) {
						line = transformation.transform(line);
						if (line == null)
							continue;
					}

					sortedData.add(Tuple.of(index++, line));

					if (sortedData.size() >= tempFileLineCount) {
						_dump(sortedData, new File(tempDir, String.valueOf(i)));
						sortedData.clear();
						++i;
					}
				}
			}
		}

		if (sortedData.size() > 0) {
			_dump(sortedData, new File(tempDir, String.valueOf(i)));
			++i;
		}

		return i;
	}

	private void _dump(TreeSet<Tuple2<Long, String>> data, File sink) throws FileNotFoundException {
		try (
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(sink), sinkCharset))
		) {
			Tuple2<Long, String> tuple = null;
			while ((tuple = data.pollFirst()) != null) {
				writer.println(tuple.e2);
			}
		}
	}

	private void _merge(File sourceDir, File sinkDir) throws IOException {
		sinkDir.mkdirs();

		File[] files = sourceDir.listFiles();
		Arrays.sort(files, (x, y) -> CompareUtil.compare(Integer.parseInt(x.getName()), Integer.parseInt(y.getName())));
		List<File> sources = new LinkedList<>();
		int i = 0;
		for (File file : files) {
			sources.add(file);
			if (sources.size() >= mergeFileCount) {
				_merge1(sources, new File(sinkDir, String.valueOf(i)));
				++i;
				sources.clear();
			}
		}
		if (sources.size() > 1) {
			_merge1(sources, new File(sinkDir, String.valueOf(i)));
			++i;
		}
		else if (sources.size() == 1) {
			sources.get(0).renameTo(new File(sinkDir, String.valueOf(i)));
			++i;
		}

		FileUtil.del(sourceDir);
	}

	private void _merge1(List<File> sources, File sink) throws IOException {
		TreeSet<Tuple3<BufferedReader, Long, String>> sortedData = null;
		if (comparator == null) {
			sortedData = new TreeSet<>(new Comparator<Tuple3<BufferedReader, Long, String>>() {
				@Override
				public int compare(Tuple3<BufferedReader, Long, String> o1, Tuple3<BufferedReader, Long, String> o2) {
					int c = o1.e3.compareTo(o2.e3);
					if (c != 0)
						return c;
					return o1.e2.compareTo(o2.e2);
				}
			});
		}
		else {
			sortedData = new TreeSet<>(new Comparator<Tuple3<BufferedReader, Long, String>>() {
				@Override
				public int compare(Tuple3<BufferedReader, Long, String> o1, Tuple3<BufferedReader, Long, String> o2) {
					int c = comparator.compare(o1.e3, o2.e3);
					if (c != 0)
						return c;
					return o1.e2.compareTo(o2.e2);
				}
			});
		}

		long index = 0;

		for (File source : sources) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(source), sinkCharset));
			String line = reader.readLine();
			if (line == null) {
				try {
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				continue;
			}
			sortedData.add(Tuple.of(reader, index++, line));
		}

		try (
				PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(sink), sinkCharset))
		) {
			while (true) {
				Tuple3<BufferedReader, Long, String> tuple = sortedData.pollFirst();
				if (tuple == null)
					break;

				writer.println(tuple.e3);
				String line = tuple.e1.readLine();
				if (line == null)
					continue;
				tuple.e3 = line;
				sortedData.add(tuple);
			}
		}
	}
}
