package com.zzc.nettyapi.apiutil;

import com.zzc.nettyapi.annotation.parser.AnnotationMappingConfiguration;
import com.zzc.nettyapi.hotload.core.classloader.NettyServerClassLoader;
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
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhengzechao
 * @date 2018/3/30
 */
public class ApiRegistry {


    private static final Logger log = LoggerFactory.getLogger(ApiRegistry.class);
    public static HashMap<String, ApiMethod> urlRegistrys = new HashMap<String, ApiMethod>();

    public static ConcurrentHashMap<String,Boolean> reloadClass = new ConcurrentHashMap<>();
    private static String mappingConfig = "/api-mapping.xml";


    public static void init(boolean scan, AnnotationMappingConfiguration configuration) throws IllegalAccessException, IOException, ParserConfigurationException {
//        if (scan) {
//            //扫描注解解析
//            Map<String, ApiMethod> methodMap = configuration.registerMapping();
//            urlRegistrys.putAll(methodMap);
//        }
        init(mappingConfig);

    }

    private static void init(String configPath) throws IllegalAccessException, ParserConfigurationException, IOException {
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
                String methodName = null;
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
                            } else if ("method".equals(name)) {
                                methodName = value;
                                api.setMethodName(methodName);
                            } else {
                                log.info("cant parse node -" + name);
                            }
                        }
                    }
                    ApiMethod previous = urlRegistrys.put(url, api);
                    reloadClass.put(className,false);
                    if (Objects.nonNull(previous)) {
                        throw new IllegalAccessException("mapping method conflict on the url:{}" + url);
                    }
                    log.info("mapping url:{} on the class:{} - method:{}", url, className, methodName);
                }
            }
        } catch (ParserConfigurationException e) {
            log.error("解析api-mapping出错--" + e.getMessage());
            throw e;
        } catch (SAXException e) {
            log.error(e.getMessage());
        } catch (IOException e) {
            log.error("读取文件出错--" + e.getMessage());
            throw e;
        } catch (IllegalAccessException e) {
            throw e;
        }
    }


}
