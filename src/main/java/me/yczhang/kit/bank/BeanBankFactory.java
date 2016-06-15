package me.yczhang.kit.bank;

import me.yczhang.data_structure.tuple.Tuple;
import me.yczhang.data_structure.tuple.Tuple2;
import me.yczhang.kit.bank.bean_bank.config_element.*;
import me.yczhang.util.ReflectionUtil;
import me.yczhang.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by YCZhang on 2/15/16.
 */
public class BeanBankFactory {

	public static final String PARSE_METHOD_NAME = "parse";

	private Map<String, Tuple2<Class, Object>> beans = new HashMap<>();
	private Map<String, BeanElement> beanElementMap = new HashMap<>();

	public static BeanBankFactory create() {
		return new BeanBankFactory();
	}

	public <T> BeanBankFactory setRef(@Nonnull String name, @Nonnull Class<T> klass, @Nullable T object) {
		beans.put(name, Tuple.of(klass, object));
		return this;
	}

	public BeanBankFactory load(@Nonnull InputStream is) throws ParserConfigurationException, IOException, SAXException {
		try {
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = builderFactory.newDocumentBuilder();
			Document document = builder.parse(is);
			Element root = document.getDocumentElement();
			if (BeansElement.TAG_BEANS.equals(root.getTagName())) {
				BeansElement beansElement = BeansElement.parse(root);
				for (Map.Entry<String, BeanElement> entry : beansElement.getBeanElementMap().entrySet()) {
					beanElementMap.put(entry.getKey(), entry.getValue());
				}
			} else {
				NodeList nodeList = root.getElementsByTagName(BeansElement.TAG_BEANS);
				BeansElement[] beansElements = BeansElement.parse(nodeList);
				for (BeansElement beansElement : beansElements) {
					for (Map.Entry<String, BeanElement> entry : beansElement.getBeanElementMap().entrySet()) {
						beanElementMap.put(entry.getKey(), entry.getValue());
					}
				}
			}
		}
		finally {
			is.close();
		}

		return this;
	}

	public BeanBankFactory load(@Nonnull File xmlFile) throws IOException, SAXException, ParserConfigurationException {
		load(new FileInputStream(xmlFile));
		return this;
	}

	public <T> void inject(@Nonnull Class<T> klass, @Nullable T object) {
		try {
			produce();

			Field[] fields = Stream.of(klass.getFields())
					.filter(field -> field.getAnnotation(BeanField.class) != null)
					.filter(field -> object != null || Modifier.isStatic(field.getModifiers()))
					.toArray(Field[]::new);

			for (Field field : fields) {
				BeanField beanField = field.getAnnotation(BeanField.class);
				String id = beanField.id();
				boolean isAllowNull = beanField.isAllowNull();
				if (!isAllowNull && !this.beans.containsKey(id))
					throw new BeanBankException("lack bean " + id);
				Object value = null;
				if (this.beans.containsKey(id))
					value = this.beans.get(id).e2;

				if (value != null)
					ReflectionUtil.setFieldValue(object, field, value);
			}
		}
		catch (BeanBankException e) {
			throw e;
		}
		catch (Exception e) {
			throw new BeanBankException(e);
		}
	}

	public BeanBank build() {
		try {
			produce();

			BeanBank beanBank = new BeanBank();
			for (Map.Entry<String, Tuple2<Class, Object>> entry : this.beans.entrySet()) {
				beanBank.put(entry.getKey(), entry.getValue().e2);
			}
			return beanBank;
		}
		catch (BeanBankException e) {
			throw e;
		}
		catch (Exception e) {
			throw new BeanBankException(e);
		}
	}

	public void injectIntoObjBank() {
		try {
			produce();

			for (Map.Entry<String, Tuple2<Class, Object>> entry : this.beans.entrySet()) {
				ObjBank.put(entry.getKey(), entry.getValue().e2);
			}
		}
		catch (BeanBankException e) {
			throw e;
		}
		catch (Exception e) {
			throw new BeanBankException(e);
		}
	}

	public <T> T getBean(String id, Class<T> klass) {
		if (beans.containsKey(id))
			return klass.cast(ReflectionUtil.cast(klass, beans.get(id).e2));
		else
			return null;
	}

	private void produce() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ParseException {
		for (String id : beanElementMap.keySet()) {
			getOrProduceBean(id);
		}
	}

	private Tuple2<Class, Object> getOrProduceBean(String id) throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, NoSuchFieldException, ParseException {
		if (beans.containsKey(id))
			return beans.get(id);

		if (!beanElementMap.containsKey(id))
			throw new BeanBankException(String.format("not found the definition of this bean with id %s", id));

		BeanElement beanElement = beanElementMap.get(id);
		Class klass = ReflectionUtil.parseClass(beanElement.getKlass());
		Object instance = newInstance(klass, beanElement.getConstructor());

		for (PropertyElement propertyElement : beanElement.getPropertyElements()) {
			setProperty(klass, instance, propertyElement);
		}

		for (InitMethodElement initMethodElement : beanElement.getInitMethodElements()) {
			invokeMethod(klass, instance, initMethodElement);
		}

		beans.put(id, Tuple.of(klass, instance));

		return beans.get(id);
	}

	private Object newInstance(Class klass, ConstructorElement constructorElement) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ParseException, NoSuchFieldException, ClassNotFoundException {
		if (constructorElement == null) {
			return klass.newInstance();
		}
		else {
			Tuple2<Class[], Object[]> args = parseValues(constructorElement.getParaValues());
			return klass.getConstructor(args.e1).newInstance(args.e2);
		}
	}

	private void setProperty(Class klass, Object object, PropertyElement propertyElement) throws InvocationTargetException, NoSuchMethodException, NoSuchFieldException, IllegalAccessException, ParseException, ClassNotFoundException, InstantiationException {
		Tuple2<Class[], Object[]> args = parseValues(propertyElement.getParaValues());
		setField(klass, object, propertyElement.getName(), args);
	}

	private Object invokeMethod(Class klass, Object object, MethodElement methodElement) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException, InstantiationException, ParseException, NoSuchFieldException {
		Tuple2<Class[], Object[]> args = parseValues(methodElement.getParaValues());
		return klass.getMethod(methodElement.getMethodName(), args.e1).invoke(object, args.e2);
	}

	private Tuple2<Class[], Object[]> parseValues(ValueElement[] valueElements) throws NoSuchMethodException, ParseException, InstantiationException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, ClassNotFoundException {
		Class[] klasses = new Class[valueElements.length];
		Object[] objects = new Object[valueElements.length];

		for (int i = 0; i < valueElements.length; ++i) {
			ValueElement valueElement = valueElements[i];

			Tuple2<Class, Object> tuple = parseValue(valueElement);
			klasses[i] = tuple.e1;
			objects[i] = tuple.e2;
		}

		return Tuple.of(klasses, objects);
	}

	private Tuple2<Class, Object> parseValue(ValueElement valueElement) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchFieldException, ParseException {
		Class klass = null;
		Object object = null;

		if (valueElement instanceof ConstantElement) {
			ConstantElement constantElement = (ConstantElement) valueElement;
			klass = String.class;
			object = constantElement.getIsNull() ? null : constantElement.getTextContent();
		}
		else if (valueElement instanceof RefElement) {
			RefElement refElement = (RefElement) valueElement;
			Tuple2<Class, Object> t = getOrProduceBean(refElement.getRefId());
			for (String fieldOrMethod : refElement.getRefMethodOrFieldNames()) {
				t = getField(t.e1, t.e2, fieldOrMethod);
			}
			klass = t.e1;
			object = t.e2;
		}

		String parserKlassName = valueElement.getParserKlass();
		if (!StringUtils.isBlank(parserKlassName)) {
			Class parserKlass = ReflectionUtil.parseClass(parserKlassName);
			Method parserMethod = parserKlass.getMethod(PARSE_METHOD_NAME, klass);
			klass = parserMethod.getReturnType();
			object = parserMethod.invoke(parserKlass.newInstance(), object);
		}

		String type = valueElement.getType();
		if (!StringUtils.isBlank(type)) {
			klass = ReflectionUtil.parseClass(type);
			if (object == null) {
				// do nothing
			} else if (object instanceof String) {
				object = StringUtil.parse(klass, (String) object);
			} else {
				object = ReflectionUtil.cast(klass, object);
			}
		}

		/*if (klass.isPrimitive() && object == null) {
			throw new ClassCastException("a primitive class " + klass + " cannot be null");
		}*/

		return Tuple.of(klass, object);
	}

	private void setField(Class klass, Object object, String fieldName, Tuple2<Class[], Object[]> args) throws NoSuchMethodException, IllegalAccessException, NoSuchFieldException, InvocationTargetException {
		String setMethodName = transformToSetMethodName(fieldName);
		try {
			Method method = klass.getMethod(setMethodName, args.e1);
			method.invoke(object, args.e2);
		}
		catch (NoSuchMethodException e) {
			if (args.e2.length == 1) {
				Field field = klass.getField(fieldName);
				field.set(object, args.e2[0]);
			}
			else {
				throw e;
			}
		}
	}

	private Tuple2<Class, Object> getField(Class klass, Object object, String fieldName) throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
		try {
			String getMethodName = transformToGetMethodName(fieldName);
			Method method = klass.getMethod(getMethodName);
			return Tuple.of(method.getReturnType(), method.invoke(object));
		}
		catch (NoSuchMethodException e) {
			Field field = klass.getField(fieldName);
			return Tuple.of(field.getType(), field.get(object));
		}
	}

	public static String transformToSetMethodName(String name) {
		if (StringUtils.isBlank(name))
			return "set";
		else
			return "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

	public static String transformToGetMethodName(String name) {
		if (StringUtils.isBlank(name))
			return "get";
		else
			return "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
	}

}
