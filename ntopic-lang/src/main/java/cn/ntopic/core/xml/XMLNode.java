/**
 * Author: obullxl@163.com
 * Copyright (c) 2020-2021 All Rights Reserved.
 */
package cn.ntopic.core.xml;

import cn.ntopic.core.builder.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XML节点模型
 *
 * @author obullxl 2021年06月12日: 新增
 */
public class XMLNode extends ToString {

    /**
     * 节点名
     */
    private String name;

    /**
     * 文本内容，可能为空
     */
    private String text;

    /**
     * 节点属性
     */
    private Map<String, String> attributes;

    /**
     * 子节点
     */
    private List<XMLNode> children;

    // ~~~~~~~~~~~ getters and setters ~~~~~~~~~~~~~~ //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setChildren(List<XMLNode> children) {
        this.children = children;
    }

    public List<XMLNode> getChildren() {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }

        return this.children;
    }

    public Map<String, String> getAttributes() {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }

        return this.attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

}
