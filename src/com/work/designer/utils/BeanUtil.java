package com.work.designer.utils;


import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.beanutils.PropertyUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BeanUtil
{
	/**
	 * ��ø�������������������Ӧ��ֵ
	 * @param bean ��������
	 * @param propertyName ������
	 * @return ���ظ����Զ�Ӧ��ֵ
	 */
	public static Object getPropertyValue(Object bean, String propertyName) throws Exception
	{
		try 
		{
			return PropertyUtils.getProperty(bean, propertyName);
		} 
		catch (Exception e)
		{
			throw e;
		}
	}
	
	/**
	 * ͨ���������ȫ�޶�������һ������Ķ���ʵ�����ö�����ͨ���޲εĹ��������������ģ�
	 * @param className ���ȫ�޶���
	 * @return ���ظ������һ������
	 */
	public static Object CreateObject(String className)
	{
		Object obj=null;
		try {
			obj= Class.forName(className,true,Thread.currentThread().getContextClassLoader()).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	/**
	 * �жϸ�����bean���Ƿ����������������,���������Բ��������.
	 * �������Bean�и���������,��Ϊorg,org.manager.id�����������
	 * @param bean ��������
	 * @param propertyName ������
	 * @return ������ڸ������������򷵻�true,���򷵻�false
	 */
	@SuppressWarnings("unchecked")
	public static boolean hasPropertyName(Object bean, String propertyName)
	{
		String[] propertyNames = StringUtils.strToStrArray(propertyName, ".");
		Class clzz =  bean.getClass();
		Field field = null;
		
		for(int i = 0; i < propertyNames.length; i++){
			
			//�������Ƿ��и������Ƶ�����
			field = hasClassField(clzz, propertyNames[i]);
			if(field == null)
				return false;
			
			clzz = field.getType();
		}

		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static Class getProperyClass(Object bean, String propertyName)
	{
		try
		{
			return PropertyUtils.getPropertyType(bean, propertyName);
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		} 
		return null;
	}
	
	
	/**
	 * �ݹ�(ֱ�����õ�Object)�ж�clazz���Ƿ��и�������(fieldName)����,������򷵻ضԸ����Ӧ��<code>Field</code>,���򷵻ؿ�
	 * @param clzz
	 * @param fieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static Field hasClassField(Class clazz, String fieldName)
	{
		try 
		{
			return clazz.getDeclaredField(fieldName);
		} 
		catch (Exception e)
		{
			if(clazz.equals(Object.class))
				return null;
			return hasClassField(clazz.getSuperclass(), fieldName); //�ݹ鴦��,ȥ�����������
		}
	}
	/**
	 * ��ø�������������������Ӧ��ֵ
	 * @param bean ��������
	 * @param propertyName ������
	 * @return ���ظ����Զ�Ӧ��ֵ,������ֵ����ΪString����
	 */
	public static String getPropertyValueToStr(Object bean, String propertyName) throws Exception
	{
		Object val = getPropertyValue(bean, propertyName);
		
		if(val == null)
			return null;
		
		return val.toString();
	}
	
	/**
	 * Ϊ����������������ֵ
	 * @param bean ��������
	 * @param propertyName ������
	 * @param value ֵ
	 */
	public static void setPropertyValue(Object bean, String propertyName, Object value) throws Exception
	{
		try
		{
			PropertyUtils.setProperty(bean, propertyName, value);
		}
		catch (Exception e) 
		{
			throw e;
		} 
	}
	
	
	
	/**
	 * �������Ĳ���collectionת��Ϊһ��xml��ʽ���ַ���
	 * @param collection ��ת��Ϊxml�ļ��϶���
	 * @param collName xml��Ԫ�ص�Ԫ����
	 * @param elementName xml�ж���Ԫ�ص�Ԫ����,Ҳ����xml����Ӧ�ļ���Ԫ�ص�Ԫ������
	 * @return ����xml��ʽ���ַ���
	 * <p>����:
	 * <P>&lt;collName&gt;</P>
	 * <P align=left>&nbsp; &lt;elementName&gt;</P>
	 * <P align=left>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; ...</P>
	 * <P align=left>&nbsp; &lt;/elementName&gt;</P>
	 * <P align=left>&nbsp;&nbsp;&nbsp;&nbsp; ...</P>
	 * <P align=left>&nbsp; &lt;elementName&gt;</P>
	 * <P align=left>&nbsp; &lt;/elementName&gt;</P>
	 * <P>&lt;/collName&gt;</P>&nbsp;
	 */
	public static String getCollection2XML(Collection<Object> collection, String collName, String elementName ) throws Exception
	{
		if(collection == null || collection.size() == 0) return ""; 
		StringBuffer sb = new StringBuffer("<"+collName+">").append("\n");
		for (Object object : collection) 
			sb.append(getBean2XML(object, elementName, 1, new StringBuffer(),  new HashSet<String>()));
		sb.append("</" + collName +">");
		
		return sb.toString();
	}
	
	/**
	 * ��������POJO(bean)����ת��Ϊһ��xml��ʽ���ַ���,���������������xml�ĵ���Ԫ����
	 * @param bean ��ת��Ϊxml��POJO����
	 * @param elementName ���ص�xml�ĵ��ĸ�Ԫ�ص�Ԫ����
	 * @return ���������bean���Ӧ��xml��ʽ���ַ���
	 */
	public static String getBean2XML(Object bean, String elementName) throws Exception
	{
		String xml = getBean2XML(bean, elementName, 0, new StringBuffer(), new HashSet<String>());
		return xml;
	}
	
	@SuppressWarnings("unchecked")
	private static String getBean2XML(Object bean, String elementName, int deep, StringBuffer sb, Set<String> classes) throws Exception
	{
		if(bean == null) return null;
		try
		{
			bean.hashCode();
		}
		catch (Exception e) 
		{
			return null;
		}
		String beanHash = String.valueOf(bean.hashCode());
		if(classes.contains(beanHash) && !originalType(bean)) return null;
		else classes.add(beanHash);
		
		if(bean instanceof Collection)
		{
			for (Object obj : (Collection)bean)
			{
				getBean2XML(obj,elementName, deep, sb, classes);
			}
		}
		else
		{
			for (int i = 0; i < deep; i++) 
				sb.append("\t");
			
			if(elementName == null)
				elementName = StringUtils.lowerFrist(bean.getClass().getSimpleName());
			
				sb.append("<").append(elementName).append(">");
			if(!originalType(bean))
				sb.append("\n");
		}
		
		if (originalType(bean))
		{
			if(bean instanceof Date)
			{
				sb.append(StringUtils.DateToStr((Date)bean, "yyyy-MM-dd HH:mm:ss"));
			}
			else if(bean instanceof String)
			{
				bean = StringUtils.replace((String)bean, "<", "&lt;");
				bean = StringUtils.replace((String)bean, ">", "&gt;");
				bean = StringUtils.replace((String)bean, "&", "&amp;");
				sb.append(bean);
			}
			else
			{
				sb.append(bean.toString());
			}
		}
		
		else{

			PropertyDescriptor[] beanProperties = PropertyUtils.getPropertyDescriptors(bean);
			for (PropertyDescriptor propertyDescriptor : beanProperties)
			{
				if(propertyDescriptor.getReadMethod() == null || propertyDescriptor.getWriteMethod() == null)
					continue;
				String propertyName = propertyDescriptor.getName();
				Object propertyValue = getPropertyValue(bean, propertyName);
				
				getBean2XML(propertyValue, propertyName, deep+1, sb, classes);
			}
		}
		
		if(!(bean instanceof Collection))
		{
			if(!originalType(bean))
				for (int i = 0; i < deep; i++) 
					sb.append("\t");
			
			sb.append("</").append(elementName).append(">").append("\n");
		}
		return sb.toString();	
	}
	
	protected static boolean originalType(Object bean)
	{
		return bean instanceof Boolean || bean instanceof Character
			|| bean instanceof Double || bean instanceof Float
			|| bean instanceof Integer || bean instanceof Long
			|| bean instanceof Short || bean instanceof String
			|| bean instanceof Date || bean instanceof Locale
			|| bean instanceof Timestamp;
	}
	
	/**
	 * ͨ������������Ϊһ��xml��ʽ��������(is)��������ת��Ϊָ����(clazz)��һ������
	 * @param is xml��ʽ���ַ���
	 * @param clazz ��ת����POJO�������
	 * @return ����һ��POJO����
	 */
	@SuppressWarnings("unchecked")
	public static Object getXML2BeanByInputStream(InputStream is, Class clazz, boolean isColl){
		if(is == null)
			return null;
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance(); 
		try{
			DocumentBuilder builder=factory.newDocumentBuilder();
			Document doc = builder.parse(is);
			Element rootElement = doc.getDocumentElement();
			
			if(!isColl)
				return getXML2Bean(rootElement, clazz);

			List result = new ArrayList();
			NodeList nodes = rootElement.getChildNodes();
			for (int i = 0; i < nodes.getLength(); i++) {
				Node node = nodes.item(i);
				if(node.getNodeType()!=Node.ELEMENT_NODE)
					continue;
				result.add(getXML2Bean(node, clazz));
			}
			return result;
			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(is != null)
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
	
	/**
	 * ��xml�ַ����ĵ�ת��Ϊһ��POJO����
	 * @param xml ��ת����XML�ĵ���ʽ���ַ���
	 * @param clazz ��ת����POJO�������
	 * @return ����һ��POJO����
	 */
	@SuppressWarnings("unchecked")
	public static Object getXML2Bean(String xml, Class clazz){
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(xml.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return getXML2BeanByInputStream(is, clazz, false);
	}
	
	/**
	 * �������xml�ַ���ת��ΪPOJO�ļ���,����һ��List����
	 * @param xml ������xml�ַ���
	 * @param clazz ��ת����POJO�������
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List getXML2Collectoin(String xml, Class clazz){
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(xml.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return (List)getXML2BeanByInputStream(is, clazz, true);
	}
	
	
	@SuppressWarnings("unchecked")
	private static Object getXML2Bean(Node node, Class clazz) throws Exception
	{
		Object bean = CreateObject(clazz.getName());
		PropertyDescriptor[] beanProperties = PropertyUtils.getPropertyDescriptors(bean);
		NodeList nodeList = node.getChildNodes();
		
		for (PropertyDescriptor propertyDescriptor : beanProperties) {
			if(propertyDescriptor.getReadMethod() == null || propertyDescriptor.getWriteMethod() == null)
				continue;
			String propertyName = propertyDescriptor.getName();
			Class propertyClass = originalWrapper(propertyDescriptor.getPropertyType());
			Object value = null;
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node propertyNode = nodeList.item(i);
				if(propertyNode.getNodeType() != Node.ELEMENT_NODE)
					continue;
				Element element = (Element)propertyNode;
				if(!element.getTagName().equals(propertyName))
					continue;
				
				
				if (Collection.class.isAssignableFrom(propertyClass)){
					if(getPropertyValue(bean, propertyName) != null)
						continue;
					
					Field field = hasClassField(clazz, propertyName);
					ParameterizedType type = (ParameterizedType)field.getGenericType(); //�ҵ���������Ӧ������  
					Type[] types = type.getActualTypeArguments();
					Class elementClass = (Class)types[0];
					
					
					Collection deails = null;
					if(List.class.isAssignableFrom(propertyClass))
						deails = new ArrayList();
					if(Set.class.isAssignableFrom(propertyClass))
						deails = new HashSet();
					
					for (int j = 0; j < nodeList.getLength(); j++) {
						Node elementNode = nodeList.item(j);
						if(elementNode.getNodeType() != Node.ELEMENT_NODE)
							continue;
						Element nodeElement = (Element)elementNode;
						if(!nodeElement.getTagName().equals(propertyName))
							continue;
						
						deails.add(getXML2Bean(nodeElement, elementClass));
					}
					
					
					setPropertyValue(bean, propertyName, deails);
					continue;
				}

				
				if(!propertyClass.equals(Boolean.class) && !propertyClass.equals(Character.class) &&
						!propertyClass.equals(Double.class) && !propertyClass.equals(Float.class) && 
						!propertyClass.equals(Integer.class) && !propertyClass.equals(Long.class) && 
						!propertyClass.equals(Short.class) && !propertyClass.equals(String.class) && 
						!propertyClass.equals(Date.class) && !propertyClass.equals(Timestamp.class)){	//�������ԭʼ����
					value = getXML2Bean(element, propertyClass);
					setPropertyValue(bean, propertyName, value);
					continue;
				}
				
				
				String textContent = element.getTextContent();
				if(propertyClass.equals(Date.class) || propertyClass.equals(Timestamp.class)){
					SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
					value = formatter.parse(textContent);
					if(propertyClass.equals(Timestamp.class)){
						value = new Timestamp(((Date)value).getTime());
					}
				}
				else{
					Constructor constructor = propertyClass.getConstructor(String.class);
					value = constructor.newInstance(textContent);
				}
				setPropertyValue(bean, propertyName, value);
			}
			
		}
		
		return bean;
	}
	
	/**
	 * �������clazz��ԭʼ����,�򷵻����ԭʼ��������Ӧ�İ�װ�������,���򷵻ظ����ͱ���
	 * @param clazz ��������������
	 * @return �����ԭʼ�����򷵻������Ӧ�İ�װ�������,���򷵻ض������ͱ���
	 */
	@SuppressWarnings("unchecked")
	public static Class originalWrapper(Class clazz){
		if(clazz.equals(boolean.class))return Boolean.class;
		if(clazz.equals(int.class))return Integer.class;
		if(clazz.equals(double.class))return Double.class;
		if(clazz.equals(float.class))return Float.class;
		if(clazz.equals(char.class))return Character.class;
		if(clazz.equals(long.class))return Long.class;
		if(clazz.equals(short.class))return Short.class;
		return clazz;
	}
}