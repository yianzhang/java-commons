package me.yczhang.kit.config;

import jodd.props.Props;
import me.yczhang.data_structure.topology.Topology;
import me.yczhang.data_structure.tuple.Tuple;
import me.yczhang.util.ObjUtil;
import me.yczhang.util.ReflectionUtil;
import me.yczhang.util.StringUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;
import javax.management.ReflectionException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用于配置类字段写入的工厂类
 * Created by YCZhang on 9/14/15.
 */
@NotThreadSafe
public class ConfigFactory {

	/**
	 * Jodd Props
	 */
	protected Props props;
	/**
	 * 字段的属性解析器
	 * fieldName -> parser
	 */
	protected Map<String, ConfigPropParser> parsers;
	/**
	 * Jodd Props 的profile功能
	 */
	protected String[] profiles;
	/**
	 * 当字段的值解析后，添加进profiles
	 */
	protected Set<String> fieldNameOfProfiles;

	protected ConfigFactory() {
		this.props = new Props();
		this.parsers = new HashMap<>();
	}

	protected ConfigFactory(Props props) {
		this.props = props;
		this.parsers = new HashMap<>();
	}

	/**
	 * 新建
	 * @return
	 */
	public static ConfigFactory create() {
		return new ConfigFactory();
	}

	/**
	 * 新建，以props为基
	 * @param props
	 * @return
	 */
	public static ConfigFactory create(@Nonnull Props props) {
		Objects.requireNonNull(props);

		return new ConfigFactory(props);
	}

	/**
	 * 载入资源
	 * @param resource
	 * @param encoding 编码格式
	 * @return self
	 * @throws IOException
	 */
	public ConfigFactory addResource(@Nonnull InputStream resource, @Nonnull  String encoding) throws IOException {
		Objects.requireNonNull(resource);
		Objects.requireNonNull(encoding);

		this.props.load(resource, encoding);
		return this;
	}

	/**
	 * 载入资源
	 * @param resource
	 * @return self
	 * @throws IOException
	 */
	public ConfigFactory addResource(@Nonnull InputStream resource) throws IOException {
		Objects.requireNonNull(resource);

		this.props.load(resource);
		return this;
	}

	/**
	 * 载入资源
	 * @param resource
	 * @param encoding 编码格式
	 * @return self
	 * @throws IOException
	 */
	public ConfigFactory addResource(@Nonnull File resource, @Nonnull String encoding) throws IOException {
		Objects.requireNonNull(resource);
		Objects.requireNonNull(encoding);

		this.props.load(resource, encoding);
		return this;
	}

	/**
	 * 载入资源
	 * @param resource
	 * @return self
	 * @throws IOException
	 */
	public ConfigFactory addResource(@Nonnull File resource) throws IOException {
		Objects.requireNonNull(resource);

		this.props.load(resource);
		return this;
	}

	/**
	 * 载入资源
	 * @param resource
	 * @return self
	 */
	public ConfigFactory addResource(@Nonnull String... resource) {
		Objects.requireNonNull(resource);

		this.props.load(StringUtils.join(resource, "\n"));
		return this;
	}

	/**
	 * 载入资源
	 * @param resource
	 * @return self
	 */
	public ConfigFactory addResource(@Nonnull Map<String, String> resource) {
		Objects.requireNonNull(resource);

		this.props.load(resource);
		return this;
	}

	/**
	 * 载入资源
	 * @param resource
	 * @param prefix 前缀
	 * @return self
	 */
	public ConfigFactory addResource(@Nonnull Map<String, String> resource, @Nonnull String prefix) {
		Objects.requireNonNull(resource);
		Objects.requireNonNull(prefix);

		this.props.load(resource, prefix);
		return this;
	}

	/**
	 * 配置解析器
	 * @param fieldName 字段名
	 * @param parser
	 * @return
	 */
	public ConfigFactory setPropParser(@Nonnull String fieldName, @Nonnull ConfigPropParser parser) {
		Objects.requireNonNull(fieldName);
		Objects.requireNonNull(parser);

		this.parsers.put(fieldName, parser);
		return this;
	}

	/**
	 * 配置解析后加入profile的字段
	 * @param fieldNames
	 * @return
	 */
	public ConfigFactory setProfileByFields(@Nonnull String... fieldNames) {
		Objects.requireNonNull(fieldNames);

		this.fieldNameOfProfiles = new HashSet<>(Arrays.asList(fieldNames));
		return this;
	}

	/**
	 * 配置profile
	 * @param profiles
	 * @return
	 */
	public ConfigFactory setProfiles(@Nonnull String... profiles) {
		Objects.requireNonNull(profiles);

		this.profiles = profiles;
		return this;
	}

	/*public String getValue(@Nonnull String name) {
		Objects.requireNonNull(name);

		return props.getValue(name);
	}

	public String getValue(@Nonnull String name, String[] profiles) {
		Objects.requireNonNull(name);

		return props.getValue(name, profiles);
	}

	public Map<String, String> innerMap(@Nonnull String prefix) {
		Objects.requireNonNull(prefix);

		Map<String, String> ret = new HashMap<>();
		props.innerMap(prefix).forEach((x, y) -> ret.put(x, (String) y));

		return ret;
	}*/

	/**
	 * 将字段值写入一个新建实例
	 * @param klass
	 * @param <T>
	 * @return 新建的实例
	 * @throws ConfigException
	 */
	public <T> T build(@Nonnull Class<T> klass) throws ConfigException {
		Objects.requireNonNull(klass);

		try {
			Map<String, Field> fields = Stream.of(klass.getFields())
					.filter(x -> x.getAnnotation(ConfigField.class) != null)
					.collect(Collectors.toMap(Field::getName, x -> x));

			T config = klass.newInstance();
			build(config, fields);

			return config;
		}
		catch (Exception e) {
			throw new ConfigException(e);
		}
	}

	/**
	 * 将字段值写入一个类
	 * @param klass
	 * @param <T>
	 * @throws ConfigException
	 */
	public <T> void inject(@Nonnull Class<T> klass, @Nullable T object) throws ConfigException {
		Objects.requireNonNull(klass);

		try {
			Map<String, Field> fields = Stream.of(klass.getFields())
					.filter(x -> x.getAnnotation(ConfigField.class) != null)
					.filter(x -> object != null || Modifier.isStatic(x.getModifiers()))
					.collect(Collectors.toMap(Field::getName, x -> x));

			build(object, fields);
		}
		catch (Exception e) {
			throw new ConfigException(e);
		}
	}

	/**
	 *
	 * @param object 实例对象，静态为null
	 * @param fields 需要解析的字段
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	protected void build(Object object, Map<String, Field> fields) throws ClassNotFoundException, IllegalAccessException, InstantiationException, ParseException, ReflectionException {
		Map<String, ConfigField> metas = fields.entrySet()
				.stream()
				.map(x -> Tuple.of(x.getKey(), x.getValue().getAnnotation(ConfigField.class)))
				.collect(Collectors.toMap(x -> x.e1, x -> x.e2));

		Topology<String> topology = new Topology<>();
		for (Map.Entry<String, ConfigField> entry : metas.entrySet()) {
			topology.addElement(entry.getKey());
			if (entry.getValue().refFields() != null) {
				for (String ref : entry.getValue().refFields()) {
					topology.addDirectFollowship(ref, entry.getKey());
				}
			}
		}

		for (String fieldName : topology.getTopologyList()) {
			if (fields.containsKey(fieldName)) {
				Field field = fields.get(fieldName);
				ConfigField meta = metas.get(fieldName);

				String propName = meta.propName();
				String defValue = StringUtil.isBlank(meta.defValue(), null);
				boolean isAllowedNull = meta.isAllowedNull();
				String parserKlass = StringUtil.isBlank(meta.parser(), null);
				String valueStr = props.getValue(propName, profiles);
				Object[] refValues = ArrayUtils.isEmpty(meta.refFields())? null : Stream.of(meta.refFields()).filter(fields::containsKey).map(x -> {
					try {
						return ReflectionUtil.getFieldValue(object, fields.get(x));
					} catch (ReflectionException e) {
						throw new ConfigException(e);
					}
				}).toArray();

				Object value;
				if (parsers.containsKey(fieldName)) {
					ConfigPropParser parser = parsers.get(fieldName);
					value = parser.parse(ObjUtil.isNull(valueStr, defValue), refValues);
				}
				else if (parserKlass != null) {
					ConfigPropParser parser = (ConfigPropParser) Class.forName(parserKlass).newInstance();
					value = parser.parse(ObjUtil.isNull(valueStr, defValue), refValues);
				}
				else {
					value = StringUtil.parse(field.getType(), ObjUtil.isNull(valueStr, defValue));
				}

				if (value == null) {
					value = ReflectionUtil.getFieldValue(object, field);
				}

				if (value == null) {
					if (!isAllowedNull)
						throw new ConfigException("lack prop " + propName);
				}
				else {
					ReflectionUtil.setFieldValue(object, field, value);
				}

				if (value != null && fieldNameOfProfiles != null && fieldNameOfProfiles.contains(fieldName)) {
					List<String> temp = profiles == null ? new LinkedList<>() : new LinkedList<>(Arrays.asList(profiles));
					temp.add(value.toString());
					profiles = temp.toArray(new String[temp.size()]);
				}
			}
		}
	}
}
