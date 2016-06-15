package me.yczhang.kit.bank.bean_bank.config_element;

import me.yczhang.kit.bank.BeanBankException;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by YCZhang on 2/16/16.
 */
public interface ValueElement {

	public static final String ATTR_TYPE = "type";
	public static final String ATTR_PARSER_CLASS = "parser-class";

	String getType();
	String getParserKlass();

	public static ValueElement parse(@Nonnull Element xmlElement) {
		if (ConstantElement.TAG_CONSTANT.equalsIgnoreCase(xmlElement.getTagName())) {
			return ConstantElement.parse(xmlElement);
		}
		else if (RefElement.TAG_REF.equalsIgnoreCase(xmlElement.getTagName())) {
			return RefElement.parse(xmlElement);
		}
		else {
			throw new BeanBankException(String.format("not a %s element", StringUtils.join(new String[]{ConstantElement.TAG_CONSTANT, RefElement.TAG_REF}, " or ")));
		}
	}

	public static ValueElement[] parse(@Nonnull NodeList xmlElements) {
		List<ValueElement> valueElementList = new LinkedList<>();
		for (int i = 0; i < xmlElements.getLength(); ++i) {
			Node node = xmlElements.item(i);
			if (node instanceof Element) {
				valueElementList.add(parse((Element) node));
			}
		}
		return valueElementList.toArray(new ValueElement[valueElementList.size()]);
	}

}
