package me.yczhang.kit.bank.bean_bank.config_element;

import me.yczhang.kit.bank.BeanBankException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * <value></value>
 * Created by YCZhang on 2/16/16.
 */
public class ConstantElement implements ValueElement {

	public static final String TAG_CONSTANT = "value";
	public static final String ATTR_ISNULL = "isNull";
	public static final String DEFAULT_TYPE = "string";

	private String type;
	private String parserKlass;
	private boolean isNull = false;
	private String textContent;

	@Override
	public String getType() {
		return parserKlass == null && type == null ? DEFAULT_TYPE : type;
	}

	@Override
	public String getParserKlass() {
		return parserKlass;
	}

	public boolean getIsNull() {
		return isNull;
	}

	public String getTextContent() {
		return textContent;
	}

	public static ConstantElement parse(@Nonnull Element xmlElement) {
		if (!TAG_CONSTANT.equalsIgnoreCase(xmlElement.getTagName()))
			throw new BeanBankException(String.format("not a %s element", TAG_CONSTANT));

		ConstantElement constantElement = new ConstantElement();

		if (xmlElement.hasAttribute(ATTR_TYPE))
			constantElement.type = xmlElement.getAttribute(ATTR_TYPE);
		if (xmlElement.hasAttribute(ATTR_PARSER_CLASS))
			constantElement.parserKlass = xmlElement.getAttribute(ATTR_PARSER_CLASS);
		if (xmlElement.hasAttribute(ATTR_ISNULL))
			constantElement.isNull = Boolean.parseBoolean(xmlElement.getAttribute(ATTR_ISNULL));

		constantElement.textContent = xmlElement.getTextContent();

		return constantElement;
	}

	public static ConstantElement[] parse(@Nonnull NodeList xmlElements) {
		List<ConstantElement> constantElementList = new LinkedList<>();
		for (int i = 0; i < xmlElements.getLength(); ++i) {
			Node xmlElement = xmlElements.item(i);
			if (xmlElement instanceof Element) {
				constantElementList.add(parse((Element) xmlElement));
			}
		}

		return constantElementList.toArray(new ConstantElement[constantElementList.size()]);
	}
}
