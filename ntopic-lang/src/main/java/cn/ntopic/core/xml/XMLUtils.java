/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.xml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 采用XPath方式解析XML文件
 *
 * @author obullxl 2021年06月12日: 新增
 */
public final class XMLUtils {

    /**
     * 解析XML文件
     *
     * @param file XML文件路径
     */
    public static XMLNode toXMLNode(String file) {
        InputStream input = null;
        try {
            input = new FileInputStream(file);

            // 解析
            return toXMLNode(input);
        } catch (Throwable e) {
            throw new RuntimeException("解析XML文件异常[" + file + "]", e);
        } finally {
            IOUtils.closeQuietly(input);
        }
    }

    /**
     * 开始解析XML文件
     *
     * @param input XML文件输入流，使用完成之后不进行关闭！
     */
    public static XMLNode toXMLNode(InputStream input) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(
                "com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl",
                    XMLUtils.class.getClassLoader());
            dbf.setNamespaceAware(true);

            // XXE漏洞
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            return parse(dbf.newDocumentBuilder().parse(input).getDocumentElement());
        } catch (Throwable e) {
            throw new RuntimeException("解析XML输入流异常！", e);
        }
    }

    /**
     * 根据属性名称查找XML节点
     */
    public static Optional<XMLNode> findChildByName(XMLNode rootNode, String childName) {
        if (rootNode == null || StringUtils.isBlank(childName)) {
            return Optional.empty();
        }

        for (XMLNode childNode : rootNode.getChildren()) {
            if(StringUtils.equalsIgnoreCase(childNode.getName(), childName)) {
                return Optional.of(childNode);
            }
        }

        return Optional.empty();
    }

    /**
     * 解析XML节点及其儿子节点
     */
    private static XMLNode parse(Node element) {
        // 节点基本信息
        XMLNode node = toXMLNode(element);

        // 解析儿子节点
        parse(node, element);

        // XML节点信息
        return node;
    }

    /**
     * 解析XML完整儿子节点
     */
    private static void parse(XMLNode parentNode, Node parentElement) {
        // 儿子节点
        NodeList elements = parentElement.getChildNodes();
        for (int i = 0; i < elements.getLength(); i++) {
            Node element = elements.item(i);
            if (element.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }

            // 解析单节点
            XMLNode node = toXMLNode(element);
            parentNode.getChildren().add(node);

            // 循环其子节点
            parse(node, element);
        }
    }

    /**
     * 复制XML节点基本信息，节点名称和节点属性名称均转化为小写！
     */
    private static XMLNode toXMLNode(Node element) {
        // 节点信息
        XMLNode node = new XMLNode();

        // 节点名称
        node.setName(element.getNodeName());

        // 节点内容
        if (!isParentNode(element)) {
            node.setText(StringUtils.trimToEmpty(element.getTextContent()));
        }

        // 节点属性
        NamedNodeMap props = element.getAttributes();
        for (int i = 0; i < props.getLength(); i++) {
            Node prop = props.item(i);
            // 属性
            node.getAttributes().put(prop.getNodeName(), prop.getTextContent());
        }

        return node;
    }

    /**
     * 监测XML节点是否还有儿子节点
     */
    private static boolean isParentNode(Node element) {
        NodeList elements = element.getChildNodes();
        for (int i = 0; i < elements.getLength(); i++) {
            Node child = elements.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                return true;
            }
        }

        return false;
    }

}
