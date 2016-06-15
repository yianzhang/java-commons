package me.yczhang.kit.bank.bean_bank.config_element;

import me.yczhang.kit.bank.BeanBankException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * <beans></beans>
 * Created by YCZhang on 2/16/16.
 */
public class BeansElement {

	public static final String TAG_BEANS = "beans";

	private Map<String, BeanElement> beanElementMap = new HashMap<>();

	public Map<String, BeanElement> getBeanElementMap() {
		return Collections.unmodifiableMap(beanElementMap);
	}

	public static BeansElement parse(@Nonnull Element xmlElement) {
		if (!TAG_BEANS.equalsIgnoreCase(xmlElement.getTagName()))
			throw new BeanBankException(String.format("not a %s element", TAG_BEANS));

		BeansElement beansElement = new BeansElement();

		BeanElement[] beanElements = BeanElement.parse(xmlElement.getElementsByTagName(BeanElement.TAG_BEAN));
		for (BeanElement beanElement : beanElements) {
			beansElement.beanElementMap.put(beanElement.getId(), beanElement);
		}

		return beansElement;
	}

	public static BeansElement[] parse(@Nonnull NodeList xmlElements) {
		List<BeansElement> list = new LinkedList<>();

		for (int i = 0; i < xmlElements.getLength(); ++i) {
			Node node = xmlElements.item(i);
			if (node instanceof Element) {
				BeansElement beansElement = BeansElement.parse((Element) node);
				list.add(beansElement);
			}
		}

		return list.toArray(new BeansElement[list.size()]);
	}
}
