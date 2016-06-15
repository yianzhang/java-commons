package me.yczhang.kit.bank.bean_bank.config_element;

import me.yczhang.kit.bank.BeanBankException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * <bean id="..." class="..."></>
 * Created by YCZhang on 2/16/16.
 */
public class BeanElement {

	public static final String TAG_BEAN = "bean";
	public static final String ATTR_ID = "id";
	public static final String ATTR_CLASS = "class";

	private ConstructorElement constructor;
	private PropertyElement[] propertyElements;
	private InitMethodElement[] initMethodElements;

	private String id;
	private String klass;

	public String getId() {
		return id;
	}

	public String getKlass() {
		return klass;
	}

	public ConstructorElement getConstructor() {
		return constructor;
	}

	public PropertyElement[] getPropertyElements() {
		return propertyElements;
	}

	public InitMethodElement[] getInitMethodElements() {
		return initMethodElements;
	}

	public static BeanElement parse(@Nonnull Element xmlElement) {
		if (!TAG_BEAN.equalsIgnoreCase(xmlElement.getTagName()))
			throw new BeanBankException(String.format("not a %s element", TAG_BEAN));

		BeanElement beanElement = new BeanElement();

		if (xmlElement.hasAttribute(ATTR_ID))
			beanElement.id = xmlElement.getAttribute(ATTR_ID);
		else
			throw new BeanBankException(String.format("a %s element lack attr %s", TAG_BEAN, ATTR_ID));

		if (xmlElement.hasAttribute(ATTR_CLASS))
			beanElement.klass = xmlElement.getAttribute(ATTR_CLASS);
		else
			throw new BeanBankException(String.format("the %s element with %s %s lack attr %s", TAG_BEAN, ATTR_ID, beanElement.id, ATTR_CLASS));

		NodeList constructors = xmlElement.getElementsByTagName(ConstructorElement.TAG_CONSTRUCTOR);
		if (constructors.getLength() == 0)
			;
		else if (constructors.getLength() > 1)
			throw new BeanBankException(String.format("the %s element with %s %s has more than 1 %s element", TAG_BEAN, ATTR_ID, ConstructorElement.TAG_CONSTRUCTOR));
		else
			beanElement.constructor = ConstructorElement.parse((Element) constructors.item(0));

		NodeList propertyELements = xmlElement.getElementsByTagName(PropertyElement.TAG_PROPERTY);
		beanElement.propertyElements = PropertyElement.parse(propertyELements);

		NodeList initMethodElements = xmlElement.getElementsByTagName(InitMethodElement.TAG_INIT_METHOD);
		beanElement.initMethodElements = InitMethodElement.parse(initMethodElements);

		return beanElement;
	}

	public static BeanElement[] parse(@Nonnull NodeList xmlElements) {
		List<BeanElement> beanElementList = new LinkedList<>();
		for (int i = 0; i < xmlElements.getLength(); ++i) {
			Node xmlElement = xmlElements.item(i);
			if (xmlElement instanceof Element) {
				beanElementList.add(parse((Element) xmlElements.item(i)));
			}
		}

		return beanElementList.toArray(new BeanElement[beanElementList.size()]);
	}
}
