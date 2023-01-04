package cn.javaex.htool.core.reflection;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * java 反射工具的封装，用来java反射相关的内容 
 * 
 * @author 陈霓清
 * @Date 2022年12月4日
 */
public class ClassUtils {
	
	/**
	 * 校验指定类有无自定义的父类
	 *     验证是否是Object的直接子类
	 * @param clazz
	 * @return
	 */
	public static boolean hasCustomSuperClass(Class<?> clazz) {
		Class<?> superClass = clazz.getSuperclass();
		String superClassName = superClass.getName();
		
		if (!"java.lang.Object".equalsIgnoreCase(superClassName)) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 根据字段名fieldName获得该字段的get方法，当为布尔类型时获得该字段的is方法，支持获取自定义父类中的字段的get方法
	 * name --> getName() [为布尔类型时获得isName()方法]
	 * @param clazz : 该字段所在的类
	 * @param fieldName : 字段名
	 * @return Method : get或者is方法
	 */
	public static Method getGetMethodByField(Class<?> clazz, String fieldName) {
		Method method = null;
		String getMethodName = null;
		
		try {
			Field field = clazz.getDeclaredField(fieldName);
			Class<?> type = field.getType();
			getMethodName = getGetMethodNameByFieldName(fieldName, type.getTypeName());
		} catch (NoSuchFieldException | SecurityException e1) {
			// 字段属于父类
			while (hasCustomSuperClass(clazz)) {
				clazz = clazz.getSuperclass();
				try {
					Field field = clazz.getDeclaredField(fieldName);
					Class<?> type = field.getType();
					
					getMethodName = getGetMethodNameByFieldName(fieldName, type.getTypeName());
					
					break;
				} catch (Exception e) {
					
				}
			}
		}
		
		try {
			method = clazz.getDeclaredMethod(getMethodName);
		} catch (Exception e) {
			method = getSuperClassMethod(clazz, getMethodName);
		}
		
		return method;
	}
	
	/**
	 * 根据字段Field获得该字段的set方法
	 * name --> setName()
	 * @param clazz : 该字段所在的类
	 * @param field : 字段
	 * @return Method : set方法
	 */
	public static Method getSetMethodByField(Class<?> clazz, Field field) {
		String setMethodName = getSetMethodNameByFieldName(field.getName());
		Method method = null;
		
		try {
			method = clazz.getDeclaredMethod(setMethodName, field.getType());
		} catch (Exception e) {
			method = getSuperClassMethod(clazz, setMethodName, field.getType());
		}
		
		return method;
	}
	
	/**
	 * 根据字段名fieldName获得该字段的set方法
	 * name --> setName()
	 * @param clazz : 该字段所在的类
	 * @param fieldName : 字段名
	 * @return Method : set方法
	 */
	public static Method getSetMethodByField(Class<?> clazz, String fieldName) {
		Field field = getDeclaredField(clazz, fieldName);
		return getSetMethodByField(clazz, field);
	}
	
	/**
	 * 获取class类和其所有自定义父类的所有属性
	 * @param clazz
	 * @param isFindParent 是否查找父类中的属性
	 * @return
	 */
	public static Field[] getDeclaredFields(Class<?> clazz, boolean isFindParent) {
		List<Field> list = new ArrayList<Field>();
		Field[] fileds = clazz.getDeclaredFields();
		
		if (fileds.length > 0) {
			list.addAll(Arrays.asList(fileds));
		}
		
		if (isFindParent) {
			while (hasCustomSuperClass(clazz)) {
				clazz = clazz.getSuperclass();
				Field[] aFields = clazz.getDeclaredFields();
				if (aFields.length > 0) {
					list.addAll(Arrays.asList(aFields));
				}
			}
		}
		
		return list.toArray(new Field[]{});
	}
	
	/**
	 * 在class类和其所有自定义父类中查找指定属性fieldName
	 * @param clazz
	 * @return
	 */
	public static Field getDeclaredField(Class<?> clazz, String fieldName) {
		try {
			return clazz.getDeclaredField(fieldName);
		} catch (Exception e) {
			while (hasCustomSuperClass(clazz)) {
				clazz = clazz.getSuperclass();
				try {
					return clazz.getDeclaredField(fieldName);
				} catch (Exception e1) {
					
				}
			}
		}
		
		return null;
	}
	
	/**
	 * 根据基本类型type获得该基本类型的包装类，没有包装类的返回自身
	 * int --> java.lang.Integer
	 * @param type : 基本类型
	 * @return Class<?> : 包装类
	 */
	public static Class<?> getWrapperClass(Class<?> type) {
		try {
			String className = type.getName();
			
			if (className.indexOf(".") == -1) {
				if ("int".equals(className)) {
					className = "java.lang.Integer";
				} else {
					className = "java.lang." + (className.charAt(0) + "").toUpperCase() + className.substring(1);
				}
				
				return Class.forName(className);
			}
			
			return type;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 转换对象value类型值到基本类型type的值
	 * valueOf("23", int.class)-->将字符串"23"转换为int值: 23;
	 * valueOf("abc", int.class)-->将字符串"abc"转换为int值: 转换异常，返回0;
	 * valueOf("true", boolean.class)-->将字符串"true"转换为布尔值: true
	 * @param value : 待转换的对象
	 * @param type : 转换后的基本类型
	 * @return Object : 转换后的值
	 */
	public static Object valueOf(Object value, Class<?> type) {
		Object obj = value;
		try {
			Class<?> clazz = getWrapperClass(type);
			// 当该字段类型非字符串类型时，调用该字段的包装类的valueOf方法自动转换value值;
			// 若type为String，则取得valueOf方法会抛出找不到方法异常，直接返回value值
			Method valueOfMethod = clazz.getDeclaredMethod("valueOf", type);
			try {
				obj = valueOfMethod.invoke(null, value);
			} catch (Exception e) {
				try {
					valueOfMethod = clazz.getDeclaredMethod("valueOf", String.class);
					obj = valueOfMethod.invoke(null, value);
				} catch (Exception ex) {
					obj = 0;
				}
			}
		} catch (Exception e) {
			// 当type为字符串类型或Object对象类型时会抛出异常
			e.printStackTrace();
		}
		
		return obj;
	}
	
	/**
	 * 反射成员方法
	 * @param clazz
	 * @param methodName : set方法名，例如:setXXX()
	 * @param parameterTypes
	 * @return
	 */
	public static Method getInvisibleMethod(Class<?> clazz, String methodName, Class<?>...parameterTypes) {
		try {
			Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
			method.setAccessible(true);
			return method;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 从包中读取类的集合
	 * @param packageName 包的名称
	 * @param isFindChildPackage 是否读取子包
	 * @return
	 */
	public static List<String> listClassNames(String packageName, boolean isFindChildPackage) {
		List<String> fileNames = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String packagePath = packageName.replace(".", "/");
		URL url = loader.getResource(packagePath);
		if (url != null) {
			String type = url.getProtocol();
			if (type.equals("file")) {
				fileNames = listClassNamesByFile(url.getPath(), null, isFindChildPackage);
			} else if (type.equals("jar")) {
				fileNames = listClassNamesByJar(url.getPath(), isFindChildPackage);
			}
		} else {
			fileNames = listClassNamesByJars(((URLClassLoader) loader).getURLs(), packagePath, isFindChildPackage);
		}
		
		return fileNames;
	}

	//==================私有方法==================//
	/**
	 * 根据字段名获得该字段的get方法名 或 is方法名
	 * name --> getName 或者 isName
	 * @param fieldName : 字段名
	 * @return String : 方法名
	 */
	private static String getGetMethodNameByFieldName(String fieldName, String typeName) {
		if ("boolean".equals(typeName)) {
			if (fieldName.startsWith("is")) {
				return fieldName;
			}
			
			return "is" + formatField(fieldName);
		}
		
		return "get" + formatField(fieldName);
	}
	
	/**
	 * 根据字段名获得该字段的set方法名
	 * name --> setName
	 * @param fieldName : 字段名
	 * @return String : 方法名
	 */
	private static String getSetMethodNameByFieldName(String fieldName) {
		return "set" + formatField(fieldName);
	}
	
	/**
	 * 格式化字段
	 * @param fieldName
	 * @return
	 */
	private static String formatField(String fieldName) {
		if (fieldName != null) {
			char first = fieldName.charAt(0);
			// 对象属性第一个字母若是字母，则需将第一个字母大写，否则不做处理，返回fieldName
			if (first >= 'A' && first <= 'z') {
				return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
			}
		}
		
		return fieldName;
	}
	
	/**
	 * 获取class类的所有自定义的父类的方法methodName(逐级查找，直到找到指定的方法然后返回)
	 * @param clazz
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 */
	private static Method getSuperClassMethod(Class<?> clazz, String methodName, Class<?>...parameterTypes) {
		while (hasCustomSuperClass(clazz)) {
			clazz = clazz.getSuperclass();
			try {
				Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
				if (!method.isAccessible()) {
					method.setAccessible(true);
				}
				
				return method;
			} catch (Exception e) {
				
			}
		}
		
		return null;
	}
	
	/**
	 * 从项目文件获取某包下所有类
	 * @param filePath 文件路径
	 * @param className 类名集合
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 */ 
	private static List<String> listClassNamesByFile(String filePath, List<String> className, boolean childPackage) {
		List<String> myClassName = new ArrayList<String>();
		File file = new File(filePath);
		File[] childFiles = file.listFiles();
		
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				if (childPackage) {
					myClassName.addAll(listClassNamesByFile(childFile.getPath(), myClassName, childPackage));
				}
			} else {
				String childFilePath = childFile.getPath();
				if (childFilePath.endsWith(".class")) {
					childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."));
					childFilePath = childFilePath.replace("\\", ".");
					myClassName.add(childFilePath);
				}
			}
		}
		
		return myClassName;
	} 
	
	/**
	 * 从所有jar中搜索该包，并获取该包下所有类
	 * @param urls URL集合
	 * @param packagePath 包路径
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 */ 
	private static List<String> listClassNamesByJars(URL[] urls, String packagePath, boolean childPackage) { 
		List<String> myClassName = new ArrayList<String>();
		
		if (urls != null) { 
			for (int i = 0;i < urls.length;i++) { 
				URL url = urls[i];
				String urlPath = url.getPath();
				// 不必搜索classes文件夹 
				if (urlPath.endsWith("classes/")) { 
					continue;
				}
				
				String jarPath = urlPath + "!/" + packagePath;
				myClassName.addAll(listClassNamesByJar(jarPath, childPackage));
			}
		}
		
		return myClassName;
	}
	
	/**
	 * 从jar获取某包下所有类
	 * @param jarPath jar文件路径
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 */ 
	@SuppressWarnings("resource")
	private static List<String> listClassNamesByJar(String jarPath, boolean childPackage) {
		List<String> myClassName = new ArrayList<String>();
		String[] jarInfo = jarPath.split("!");
		String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
		String packagePath = jarInfo[1].substring(1);
		
		try {
			JarFile jarFile = new JarFile(jarFilePath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (entryName.endsWith(".class")) {
					if (childPackage) {
						if (entryName.startsWith(packagePath)) {
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
							myClassName.add(entryName);
						}
					} else {
						int index = entryName.lastIndexOf("/");
						String myPackagePath;
						if (index != -1) {
							myPackagePath = entryName.substring(0, index);
						} else {
							myPackagePath = entryName;
						}
						
						if (myPackagePath.equals(packagePath)) {
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
							myClassName.add(entryName);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return myClassName;
	}
	
}
