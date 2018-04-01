package com.zzc.nettyapi.apiutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */
public class ApiRegistry {


    private static final Logger logger = LoggerFactory.getLogger(ApiRegistry.class);
    public static HashMap<String, ApiMethod> urlRegistrys = new HashMap<String, ApiMethod>();

    private static String mappingConfig = "/api-mapping.xml";


    public static void init(){
        init(mappingConfig);
    }
     private static void init(String configPath) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(ApiRegistry.class.getResourceAsStream(configPath));

            NodeList resources = doc.getElementsByTagName("resource");

            for (int i = 0; i < resources.getLength(); i++) {
                Element resource = (Element) resources.item(i);
                NodeList classs = resource.getElementsByTagName("class-name");
                Element clz = (Element) classs.item(0);

                String className = clz.getAttribute("class");


                NodeList nodes = clz.getElementsByTagName("node");


                for (int j = 0; j < nodes.getLength(); j++) {
                    Element node = (Element) nodes.item(j);
                    ApiMethod api = new ApiMethod();

                    api.setClassName(className);
                    String url = null;
                    for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling()) {
                        if (child.getNodeType() == Node.ELEMENT_NODE) {

                            String name = child.getNodeName();
                            String value = child.getFirstChild().getNodeValue();
                            if ("url".equals(name)) {
                                url = value;
                                api.setUrl(url);

                            } else if ("support".equals(name)) {
                                api.getSupportMethods().add(value.toUpperCase());

                            }else if("method".equals(name)){
                                api.setMethodName(value);

                            } else {
                                logger.info("无法解析node--" + name);

                            }
                        }


                    }

                    urlRegistrys.put(url, api);


                }


            }


        } catch (ParserConfigurationException e) {
            logger.error("解析api-mapping出错--" + e.getMessage());
        } catch (SAXException e) {
            logger.error(e.getMessage());
        } catch (IOException e) {
            logger.error("读取文件出错--" + e.getMessage());
        }
    }

}
