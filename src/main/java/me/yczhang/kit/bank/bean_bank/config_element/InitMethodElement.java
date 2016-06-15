package me.yczhang.kit.bank.bean_bank.config_element;

import me.yczhang.kit.bank.BeanBankException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * <init-method></init-method>
 * Created by YCZhang on 2/16/16.
 */
public class InitMethodElement implements MethodElement {

	public static final String TAG_INIT_METHOD = "init-method";
	public static final String ATTR_NAME = "name";

	private String methodName;
	private ValueElement[] paraValues;

	@Override
	public String getMethodName() {
		return methodName;
	}

	@Override
	public ValueElement[] getParaValues() {
		return paraValues;
	}

	public static InitMethodElement parse(@Nonnull Element xmlElement) {
		if (!TAG_INIT_METHOD.equalsIgnoreCase(xmlElement.getTagName()))
			throw new BeanBankException(String.format("not a %s element", TAG_INIT_METHOD));

		InitMethodElement initMethodElement = new InitMethodElement();

		if (xmlElement.hasAttribute(ATTR_NAME))
			initMethodElement.methodName = xmlElement.getAttribute(ATTR_NAME);
		else
			throw new BeanBankException(String.format("a %s element lack attr %s", TAG_INIT_METHOD, ATTR_NAME));

		NodeList valueElements = xmlElement.getChildNodes();
		initMethodElement.paraValues = ValueElement.parse(valueElements);

		return initMethodElement;
	}

	public static InitMethodElement[] parse(@Nonnull NodeList xmlElements) {
		List<InitMethodElement> initMethodElementList = new LinkedList<>();
		for (int i = 0; i < xmlElements.getLength(); ++i) {
			Node xmlElement = xmlElements.item(i);
			if (xmlElement instanceof Element) {
				initMethodElementList.add(parse((Element) xmlElement));
			}
		}
		return initMethodElementList.toArray(new InitMethodElement[initMethodElementList.size()]);
	}
}
