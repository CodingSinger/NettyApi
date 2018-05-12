package com.zzc.nettyapi.apiutil;

import com.google.common.collect.Lists;
import com.sun.tools.javac.util.StringUtils;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * @author zhengzechao
 * @date 2018/5/12
 * Email ooczzoo@gmail.com
 */

public class CustomerConfiguration {
    /**
     * TODO 解析custom-conf成类元数据
     */
    private Logger log = LoggerFactory.getLogger(CustomerConfiguration.class);
    private List<Class> methodFilter; //methodFilter类元数据
    private List<Class> convertProcessor; //自定义数据转换器

    public List<Class> getConvertProcessor() {
        return convertProcessor;
    }

    public List<Class> getMethodFilter() {
        return methodFilter;
    }

    private String defaultConfiguration = "custom-conf.xml";
    public CustomerConfiguration() {
    }
    public void load() {
        //获取用户configuration
        String property = System.getProperty("custom.conf.path");
        if (!StringUtil.isNullOrEmpty(property)) {
            defaultConfiguration = property;
        }
        load(defaultConfiguration);
    }

    private void load(String path) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            Document doc = db.parse(ApiRegistry.class.getResourceAsStream(File.separator+path));
            methodFilter = Lists.newArrayList();
            convertProcessor = Lists.newArrayList();
            loadCustomeClass(doc, "filter", methodFilter);
            loadCustomeClass(doc, "convert", convertProcessor);

        } catch (ParserConfigurationException e) {
            log.error("");
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.error("load custom configuration fail");
            e.printStackTrace();
        }

    }


    private void loadCustomeClass(Document doc, String tagName, List<Class> container) {
        String className = null;
        NodeList filters = doc.getElementsByTagName(tagName);
        try {
            for (int i = 0; i < filters.getLength(); i++) {
                Element node = (Element) filters.item(i);
                NodeList classes = node.getElementsByTagName("class");
                for (int j = 0;j<classes.getLength();j++){
                    Element filter = (Element) classes.item(j);
                    if (filter.getTagName() != "class"){
                        continue;
                    }
                    className = filter.getAttribute("name");
                    Class filterClass = Class.forName(className);
                    container.add(filterClass);
                }
            }
        } catch (ClassNotFoundException e) {
            log.error("custome class:{} can't find", className);
            e.printStackTrace();
        }


    }
}
