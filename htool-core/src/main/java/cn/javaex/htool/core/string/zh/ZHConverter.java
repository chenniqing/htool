package cn.javaex.htool.core.string.zh;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 中文转换
 * 
 * @author 陈霓清
 * @Date 2022年11月30日
 */
public class ZHConverter {

	/** 繁体 */
	public static final int TC = 0;
	/** 简体 */
	public static final int SC = 1;
	
	private static final int NUM_OF_CONVERTERS = 2;
	
	private static final ZHConverter[] CONVERTERS = new ZHConverter[NUM_OF_CONVERTERS];
	private static final String[]  PROPERTY_FILES = new String[2];

	private Properties charMap = new Properties();
	private Set<Object> conflictingSets  = new HashSet<Object>();
	
	static {
		PROPERTY_FILES[TC] = "zh2Hant.properties";
		PROPERTY_FILES[SC] = "zh2Hans.properties";
	}

	/**
	 * @param converterType 0 for traditional and 1 for simplified
	 * @return
	 */
	public static ZHConverter getInstance(int converterType) {
		if (converterType >= 0 && converterType < NUM_OF_CONVERTERS) {
			if (CONVERTERS[converterType] == null) {
				synchronized(ZHConverter.class) {
					if (CONVERTERS[converterType] == null) {
						CONVERTERS[converterType] = new ZHConverter(PROPERTY_FILES[converterType]);
					}
				}
			}
			
			return CONVERTERS[converterType];
		}
			
		return null;
	}

	public static String convert(String text, int converterType) {
		ZHConverter instance = getInstance(converterType);
		return instance.convert(text);
	}

	private ZHConverter(String propertyFile) {
		InputStream is = getClass().getResourceAsStream(propertyFile);

		if (is != null) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new InputStreamReader(is));
				charMap.load(reader);
			} catch (FileNotFoundException e) {
				
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (reader != null) {
						reader.close();
					}
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					
				}
			}
		}
		initializeHelper();
	}

	private void initializeHelper() {
		Map<String, Integer> stringPossibilities = new HashMap<>();
		Iterator<?> iter = charMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (key.length() >= 1) {
				for (int i = 0; i < (key.length()); i++) {
					String keySubstring = key.substring(0, i + 1);
					if (stringPossibilities.containsKey(keySubstring)) {
						Integer integer = (Integer)(stringPossibilities.get(keySubstring));
						stringPossibilities.put(keySubstring, new Integer(integer.intValue() + 1));
					} else {
						stringPossibilities.put(keySubstring, new Integer(1));
					}
				}
			}
		}

		iter = stringPossibilities.keySet().iterator();
		
		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (((Integer) (stringPossibilities.get(key))).intValue() > 1) {
				conflictingSets.add(key);
			}
		}
	}

	/**
	 * 转换
	 * @param in
	 * @return
	 */
	public String convert(String in) {
		StringBuilder outString = new StringBuilder();
		StringBuilder stackString = new StringBuilder();

		for (int i = 0; i < in.length(); i++) {
			char c = in.charAt(i);
			String key = "" + c;
			stackString.append(key);

			if (conflictingSets.contains(stackString.toString())) {
				
			} else if (charMap.containsKey(stackString.toString())) {
				outString.append(charMap.get(stackString.toString()));
				stackString.setLength(0);
			} else {
				CharSequence sequence = stackString.subSequence(0, stackString.length() - 1);
				stackString.delete(0, stackString.length()-1);
				flushStack(outString, new StringBuilder(sequence));
			}
		}

		flushStack(outString, stackString);

		return outString.toString();
	}

	private void flushStack(StringBuilder outString, StringBuilder stackString) {
		while (stackString.length() > 0){
			if (charMap.containsKey(stackString.toString())) {
				outString.append(charMap.get(stackString.toString()));
				stackString.setLength(0);
			} else {
				outString.append("" + stackString.charAt(0));
				stackString.delete(0, 1);
			}
		}
	}
	
}
