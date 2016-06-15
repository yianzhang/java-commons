package me.yczhang.kit.bank.bean_bank.config_element;

import me.yczhang.kit.bank.BeanBankException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by YCZhang on 2/16/16.
 */
public class PropertyElement {

	public static final String TAG_PROPERTY = "property";
	public static final String ATTR_NAME = "name";

	private String name;
	private ValueElement[] paraValues;

	public String getName() {
		return name;
	}

	public ValueElement[] getParaValues() {
		return paraValues;
	}

	public static PropertyElement parse(@Nonnull Element xmlElement) {
		if (!TAG_PROPERTY.equalsIgnoreCase(xmlElement.getTagName()))
			throw new BeanBankException(String.format("not a %s element", TAG_PROPERTY));

		PropertyElement propertyElement = new PropertyElement();

		if (xmlElement.hasAttribute(ATTR_NAME))
			propertyElement.name = xmlElement.getAttribute(ATTR_NAME);
		else
			throw new BeanBankException(String.format("a %s element lack attr %s", TAG_PROPERTY, ATTR_NAME));

		NodeList valueElements = xmlElement.getChildNodes();
		propertyElement.paraValues = ValueElement.parse(valueElements);

		return propertyElement;
	}

	public static PropertyElement[] parse(@Nonnull NodeList xmlElements) {
		List<PropertyElement> propertyElementList = new LinkedList<>();
		for (int i = 0; i < xmlElements.getLength(); ++i) {
			Node xmlElement = xmlElements.item(i);
			if (xmlElement instanceof Element) {
				propertyElementList.add(parse((Element) xmlElement));
			}
		}

		return propertyElementList.toArray(new PropertyElement[propertyElementList.size()]);
	}
}
