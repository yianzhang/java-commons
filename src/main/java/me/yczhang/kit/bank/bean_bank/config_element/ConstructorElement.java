package me.yczhang.kit.bank.bean_bank.config_element;

import me.yczhang.kit.bank.BeanBankException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * <constructor></constructor>
 * Created by YCZhang on 2/16/16.
 */
public class ConstructorElement {

	public static final String TAG_CONSTRUCTOR = "constructor";

	private ValueElement[] paraValues;

	public ValueElement[] getParaValues() {
		return paraValues;
	}

	public static ConstructorElement parse(@Nonnull Element xmlElement) {
		if (!TAG_CONSTRUCTOR.equalsIgnoreCase(xmlElement.getTagName()))
			throw new BeanBankException(String.format("not a %s element", TAG_CONSTRUCTOR));

		ConstructorElement constructorElement = new ConstructorElement();

		NodeList valueElements = xmlElement.getChildNodes();
		constructorElement.paraValues = ValueElement.parse(valueElements);

		return constructorElement;
	}

	public static ConstructorElement[] parse(@Nonnull NodeList xmlElements) {
		List<ConstructorElement> constructorElementList = new LinkedList<>();
		for (int i = 0; i < xmlElements.getLength(); ++i) {
			Node xmlElement = xmlElements.item(i);
			if (xmlElement instanceof Element) {
				constructorElementList.add(parse((Element) xmlElement));
			}
		}

		return constructorElementList.toArray(new ConstructorElement[constructorElementList.size()]);
	}
}
