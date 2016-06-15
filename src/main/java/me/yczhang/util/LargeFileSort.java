package me.yczhang.util;

import me.yczhang.data_structure.tuple.Tuple;
import me.yczhang.data_structure.tuple.Tuple2;
import me.yczhang.data_structure.tuple.Tuple3;
import me.yczhang.trait.Filter;
import me.yczhang.trait.Transformation;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by YCZhang on 9/21/15.
 */
@Deprecated
public class LargeFileSort {

	private final int DEFAULT_TEMP_FILE_SIZE = 100000;
	private final int DEFAULT_MERGE_FILE_SIZE = 10;

	private int tempFileSize = DEFAULT_TEMP_FILE_SIZE;
	private int mergeFileSize = DEFAULT_MERGE_FILE_SIZE;
	private File tempDir = new File("large_file_sort_temp_" + System.nanoTime());
	private Comparator<String> comparator;
	private Filter<String> filter;
	private Transformation<String, String> transformation;
	private Charset sourceCharset = Charset.forName("utf-8");
	private Charset sinkCharset = Charset.forName("utf-8");

	private List<File> sources;
	private File sink;

	public LargeFileSort(List<File> sources, File sink) {
		this.sources = sources;
		this.sink = sink;
	}

	/**
	 * 文件行数，不是大小
	 * @param tempFileSize
	 * @return
	 */
	public LargeFileSort setTempFileSize(int tempFileSize) {
		if (tempFileSize > 0)
			this.tempFileSize = tempFileSize;
		else
			this.tempFileSize = DEFAULT_TEMP_FILE_SIZE;
		return this;
	}

	/**
	 * 文件个数，不是大小
	 * @param mergeFileSize
	 * @return
	 */
	public LargeFileSort setMergeFileSize(int mergeFileSize) {
		if (mergeFileSize > 1)
			this.mergeFileSize = mergeFileSize;
		else
			this.mergeFileSize = DEFAULT_MERGE_FILE_SIZE;
		return this;
	}

	public LargeFileSort setTempDir(File tempDir) {
		this.tempDir = tempDir;
		return this;
	}

	public LargeFileSort setFilter(Filter<String> filter) {
		this.filter = filter;
		return this;
	}

	public LargeFileSort setTransformation(Transformation<String, String> transformation) {
		this.transformation = transformation;
		return this;
	}

	public LargeFileSort setComparator(Comparator<String> comparator) {
		this.comparator = comparator;
		return this;
	}

	public LargeFileSort setSourceCharset(Charset charset) {
		this.sourceCharset = charset;
		return this;
	}

	public LargeFileSort setSinkCharset(Charset charset) {
		this.sinkCharset = charset;
		return this;
	}

	public void sort() throws IOException {
		tempDir.mkdirs();

		FileUtil.clearDir(tempDir);

		int files = _split(sources, new File(tempDir, "0"));
		int i = 0;
		while (files > 1) {
			++i;
			_merge(new File(tempDir, String.valueOf(i - 1)), new File(tempDir, String.valueOf(i)));
			files = (files / mergeFileSize) + (files % mergeFileSize == 0 ? 0 : 1);
		}

		if (sink.exists() && sink.isFile())
			sink.delete();
		sink.getParentFile().mkdirs();
		new File(tempDir + "/" + i + "/0").renameTo(sink);

		FileUtil.del(tempDir);
	}

	private int _split(List<File> sources, File tempDir) throws IOException {
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
		for (File source : sources) {
			if (source == null || !source.exists() || !source.isFile())
				continue;

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

					if (sortedData.size() >= tempFileSize) {
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
			if (sources.size() >= mergeFileSize) {
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
				sortedData.add(Tuple.of(tuple.e1, tuple.e2, line));
			}
		}
	}

	public static LargeFileSort create(@Nonnull List<File> sources, @Nonnull File sink) {
		Objects.requireNonNull(sources);
		Objects.requireNonNull(sink);

		return new LargeFileSort(sources, sink);
	}

	public static LargeFileSort create(@Nonnull File[] sources, @Nonnull File sink) {
		Objects.requireNonNull(sources);
		Objects.requireNonNull(sink);

		return new LargeFileSort(Arrays.asList(sources), sink);
	}
}
