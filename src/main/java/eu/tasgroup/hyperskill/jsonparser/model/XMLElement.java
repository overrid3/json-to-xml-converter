package eu.tasgroup.hyperskill.jsonparser.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XMLElement implements TreeElement<XMLElement> {

    private String tagName;
    private String text;
    private List<XMLElement> children;
    private XMLElement parent;
    private Map<String, String> attributes;

    public XMLElement() {
        children = new ArrayList<>();
        attributes = new LinkedHashMap<>();
        this.text = "";
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void insertAttributeEntry(String key, String value) {
        attributes.put(key, value);
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public void setChildren(List<XMLElement> children) {
        this.children = children;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getText() {
        return text;
    }

    public void setText(Object text) {

        if (text == null) {
            this.text = null;
        } else {
            this.text = text.toString();
        }

    }

    public void setParent(XMLElement e) {
        this.parent = e;
    }

    public void addChild(XMLElement e) {
        e.setParent(this);
        children.add(e);
    }

    @Override
    public XMLElement getParent() {
        return parent;
    }

    @Override
    public List<XMLElement> getChildren() {
        return children;
    }
}
