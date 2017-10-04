package dataflow.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class LoadProperties {

	private static Properties properties = null;
	private static long lastModified = 0;
	private static String PROPERTIES = CustomConstants.SOURCE_DIR_LOCAL + CustomConstants.PROPERTIES_FLE;

	private static void initialize() {

		try {
			File fileConfig = new File(PROPERTIES);
			if (lastModified < fileConfig.lastModified()) {
				System.out.println("Load Properties: " + PROPERTIES);
				properties = null;
				properties = new Properties();
				properties.load(new FileInputStream(fileConfig));
				lastModified = fileConfig.lastModified();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String getProperty(String key) {
		initialize();
		String prop;
		try {
			prop = properties.getProperty(key);
		} catch (NullPointerException e) {
			PROPERTIES = CustomConstants.SOURCE_DIR_LOCAL + CustomConstants.PROPERTIES_FLE;
			initialize();
			prop = properties.getProperty(key);
		}
		return prop;
	}

	public static Properties getProperties() {
		initialize();
		return properties;
	}

	public static long getLastModified() {
		return lastModified;
	}

	public static void setLastModified(long lastModified) {
		LoadProperties.lastModified = lastModified;
	}

}
