package com.work.designer.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.StringTokenizer;

import org.eclipse.jdt.internal.corext.util.JavaConventionsUtil;

@SuppressWarnings("restriction")
public class StringUtils {

	/**
	 * ǰ����ʶ
	 */
	public static final int BEFORE = 1;

	/**
	 * ��̱�ʶ
	 */
	public static final int AFTER = 2;

	public static final String DEFAULT_PATH_SEPARATOR = ",";
	
	/**
		 * ��һ���м�����ŷָ������ַ���ת��Ϊ�ַ����������
		 * 
		 * @param str
		 *            ��ת���ķ�������
		 * @return �ַ�������
		 */
	public static String[] strToStrArray(String str) {
		return strToStrArrayManager(str, DEFAULT_PATH_SEPARATOR);
	}

	/**
	 * ���ַ������󰴸����ķָ���separatorת��Ϊ�ַ����������
	 * 
	 * @param str
	 *            ��ת���ķ�������
	 * @param separator
	 *            �ַ��ͷָ���
	 * @return �ַ�������
	 */
	public static String[] strToStrArray(String str, String separator) {
		return strToStrArrayManager(str, separator);
	}

	private static String[] strToStrArrayManager(String str, String separator) {

		StringTokenizer strTokens = new StringTokenizer(str, separator);
		String[] strArray = new String[strTokens.countTokens()];
		int i = 0;

		while (strTokens.hasMoreTokens()) {
			strArray[i] = strTokens.nextToken().trim();
			i++;
		}

		return strArray;
	}

	/**
	 * �ַ����滻
	 * 
	 * @param str
	 *            Դ�ַ���
	 * @param pattern
	 *            ���滻���ַ���
	 * @param replace
	 *            �滻Ϊ���ַ���
	 * @return
	 */
	public static String replace(String str, String pattern, String replace) {
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();
		while ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));

		return result.toString();
	}

	/**
	 * ����ĸ��д
	 * @param string
	 * @return
	 */
	public static String upperFrist(String string) {
		if (string == null)
			return null;
		String upper = string.toUpperCase();
		return upper.substring(0, 1) + string.substring(1);
	}

	/**
	 * ����ĸСд
	 * @param string
	 * @return
	 */
	public static String lowerFrist(String string) {
		if (string == null)
			return null;
		String lower = string.toLowerCase();
		return lower.substring(0, 1) + string.substring(1);
	}

	/**
	 * ��һ���������͵Ķ���ת��Ϊָ����ʽ���ַ���
	 * @param date ��ת��������
	 * @param format ת��Ϊ�ַ�������Ӧ��ʽ
	 * ���磺DateToStr(new Date() ,"yyyy.MM.dd G 'at' hh:mm:ss a zzz");
	 * @return һ���ַ���<p>
	 */
	public static String DateToStr(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}

	/**
	 * ����һ��ָ�����ȵ�����ַ���
	 * @param length ���ص��ַ�������
	 * @return ����һ�����
	 */
	public static String randomString(int length) {
		if (length < 1) {
			return null;
		}
		Random randGen = new Random();
		char[] numbersAndLetters = ("abcdefghijklmnopqrstuvwxyz"
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = numbersAndLetters[randGen.nextInt(51)];
		}
		return new String(randBuffer);
	}
	
	public static boolean isKeyValue(String identifier)
	{
		char[] ch = identifier.toCharArray();

		for (int i = 0; i < ch.length; i++) 
		{
			char c = ch[i];
			if (!isChinese(c))
				continue;
			return true;
		}
		return JavaConventionsUtil.validateFieldName(identifier, null).getSeverity() == 4;
	}

	private static boolean isChinese(char c) 
	{
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

		return (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS)
				|| (ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS)
				|| (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A)
				|| (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION)
				|| (ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION)
				|| (ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS);
	}
}
