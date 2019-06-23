package cn.com.onlinetool.fastpay.util;


import cn.com.onlinetool.fastpay.pay.wxpay.util.WXPayUtil;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author choice
 * @description: 转换工具类
 * @date 2019-06-06 15:32
 */
@Slf4j
public final class ConverterUtil {


    /**
     * 获取对象中所有的属性
     * @param obj
     * @return
     */
    public static List<Field> getFields(Object obj){
        List<Field> fields = new LinkedList<>();
        Collections.addAll(fields,obj.getClass().getDeclaredFields());
        if(obj.getClass().getSuperclass().getDeclaredFields().length > 0){
            try {
                fields.addAll(getFields(obj.getClass().getSuperclass().newInstance()));
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return fields;
    }

    /**
     * object转map
     * @param obj 数据对象
     * @return
     * @throws Exception
     */
    public static Map objectToMap(Object obj) throws Exception {
        return objectToMap(obj,null,null);
    }


    /**
     * object转map
     * @param obj 数据对象
     * @param from 将map key 从 from 格式化为 to
     * @param to 将map key 从 from 格式化为 to
     * @return
     * @throws Exception
     */
    public static Map<String,String> objectToMap(Object obj,CaseFormat from,CaseFormat to) throws Exception {

        if (obj == null) {
            return null;
        }

        Map<String, String> map = new HashMap<>();
        List<Field> fields = getFields(obj);
        for (Field field : fields) {
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }
            field.setAccessible(true);
            if(null != field.get(obj)){
                if(null != from && null != to){
                    map.put(from.to(to, field.getName()), field.get(obj).toString());
                }else {
                    map.put(field.getName(), field.get(obj).toString());
                }
            }

        }

        return map;
    }


    /**
     * object转map
     * @param map 数据对象
     * @param beanClass 目标对象
     * @return
     * @throws Exception
     */
    public static <T> T mapToObject(Map map, Class<T> beanClass) throws Exception {
        return mapToObject(map,beanClass,null,null);
    }

    /**
     * object转map
     * @param map 数据对象
     * @param beanClass 目标对象
     * @param from 将 beanClass中的字段名称从 from 格式化为 to 用于获取map中的数据
     * @param to 将 beanClass中的字段名称从 from 格式化为 to 用于获取map中的数据
     * @return
     * @throws Exception
     */
    public static <T> T mapToObject(Map map, Class<T> beanClass,CaseFormat from,CaseFormat to) throws Exception {
        if (map == null){
            return null;
        }

        Object obj = beanClass.newInstance();

        List<Field> fields = getFields(obj);
        for (Field field : fields) {
            int mod = field.getModifiers();
            if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
                continue;
            }

            field.setAccessible(true);

            if(null != from && null != to){
                field.set(obj, map.get(from.to(to,field.getName())));
            }else{
                field.set(obj, map.get(field.getName()));
            }

        }

        return (T)obj;
    }

    /**
     * xml 转 指定类型的obj
     * @param xml 源数据
     * @param beanClass 返回的数据类型
     * @param <T> 返回类型
     * @return
     * @throws Exception
     */
    public static <T> T xmlToObject(String xml, Class<T> beanClass) throws Exception {
        return xmlToObject(xml,beanClass,null,null);
    }


    /**
     * xml 转 指定类型的obj
     * @param xml 源数据
     * @param beanClass 返回的数据类型
     * @param from 字段名称 从 from 转成 to
     * @param to 字段名称 从 from 转成 to
     * @param <T> 返回类型
     * @return
     * @throws Exception
     */
    public static <T> T xmlToObject(String xml, Class<T> beanClass,CaseFormat from,CaseFormat to) throws Exception {
        Map<String, String> xmlMap = xmlToMap(xml);
        return mapToObject(xmlMap,beanClass,from,to);
    }



    /**
     * object 转 xml
     * @param data 数据对象
     * @return
     * @throws Exception
     */
    public static String objectToXml(Object data) throws Exception {
        return objectToXml(data,null,null);
    }

    /**
     * object 转 xml
     * @param object 数据对象
     * @param from 字段名称 从 from 转成 to
     * @param to 字段名称 从 from 转成 to
     * @return
     * @throws Exception
     */
    public static String objectToXml(Object object,CaseFormat from,CaseFormat to) throws Exception {
        Map<String,String> objectMap = objectToMap(object);
        return mapToXml(objectMap,from,to);
    }






    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML) throws Exception {
        return xmlToMap(strXML,null,null);
    }


    /**
     * XML格式字符串转换为Map
     * @param from 字段名称 从 from 转成 to
     * @param to 字段名称 从 from 转成 to
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     * @throws Exception
     */
    public static Map<String, String> xmlToMap(String strXML,CaseFormat from,CaseFormat to) throws Exception {
        try {
            Map<String, String> data = new HashMap<String, String>();
            DocumentBuilder documentBuilder = WXPayUtil.newDocumentBuilder();
            InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"));
            org.w3c.dom.Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0; idx < nodeList.getLength(); ++idx) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    org.w3c.dom.Element element = (org.w3c.dom.Element) node;
                    String nodeName = element.getNodeName();
                    if(null != from && null != to){
                        nodeName = from.to(to,nodeName);
                    }
                    data.put(nodeName, element.getTextContent());
                }
            }
            try {
                stream.close();
            } catch (Exception ex) {
                // do nothing
            }
            return data;
        } catch (Exception ex) {
            log.info("Invalid XML, can not convert to map. Error message: {}. XML content: {}", ex.getMessage(), strXML);
            throw ex;
        }

    }



    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data) throws Exception {
        return mapToXml(data,null,null);

    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     * @throws Exception
     */
    public static String mapToXml(Map<String, String> data,CaseFormat from,CaseFormat to) throws Exception {
        org.w3c.dom.Document document = WXPayUtil.newDocument();
        org.w3c.dom.Element root = document.createElement("xml");
        document.appendChild(root);
        for (String key: data.keySet()) {
            String value = data.get(key);
            if (value == null) {
                value = "";
            }
            value = value.trim();
            if(null != from && null != to){
                value = from.to(to,value);
            }
            org.w3c.dom.Element filed = document.createElement(key);
            filed.appendChild(document.createTextNode(value));
            root.appendChild(filed);
        }
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource source = new DOMSource(document);
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        transformer.transform(source, result);
        String output = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
        try {
            writer.close();
        }
        catch (Exception ex) {
        }
        return output;
    }

}
