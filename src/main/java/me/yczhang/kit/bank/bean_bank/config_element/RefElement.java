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
public class RefElement implements ValueElement {

	public static final String TAG_REF = "ref";
	public static final String SEP_STR = "::";

	private String type;
	private String parserKlass;

	private String refId;
	private String[] refMethodOrFieldNames;

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getParserKlass() {
		return parserKlass;
	}

	public String getRefId() {
		return refId;
	}

	public String[] getRefMethodOrFieldNames() {
		return refMethodOrFieldNames;
	}

	public static RefElement parse(@Nonnull Element xmlElement) {
		if (!TAG_REF.equalsIgnoreCase(xmlElement.getTagName()))
			throw new BeanBankException(String.format("not a %s element", TAG_REF));

		RefElement refElement = new RefElement();

		if (xmlElement.hasAttribute(ATTR_TYPE))
			refElement.type = xmlElement.getAttribute(ATTR_TYPE);
		if (xmlElement.hasAttribute(ATTR_PARSER_CLASS))
			refElement.parserKlass = xmlElement.getAttribute(ATTR_PARSER_CLASS);

		String[] seps = StringUtils.splitByWholeSeparatorPreserveAllTokens(xmlElement.getTextContent(), SEP_STR);
		if (seps.length == 0)
			throw new BeanBankException(String.format("a %s element's content is empty", TAG_REF));
		refElement.refId = seps[0];
		if (seps.length > 1) {
			refElement.refMethodOrFieldNames = new String[seps.length - 1];
			for (int i = 0; i < seps.length - 1; ++i)
				refElement.refMethodOrFieldNames[i] = seps[i + 1];
		}
		else {
			refElement.refMethodOrFieldNames = new String[0];
		}

		return refElement;
	}

	public static RefElement[] parse(@Nonnull NodeList xmlElements){
		List<RefElement> refElementList = new LinkedList<>();
		for (int i = 0; i < xmlElements.getLength(); ++i) {
			Node xmlElement = xmlElements.item(i);
			if (xmlElement instanceof Element) {
				refElementList.add(parse((Element) xmlElement));
			}
		}
		return refElementList.toArray(new RefElement[refElementList.size()]);
	}
}
