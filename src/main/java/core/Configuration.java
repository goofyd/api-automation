package core;

import java.util.Properties;
import java.util.ResourceBundle;

public class Configuration {
	
	private static ResourceBundle configProps = ResourceBundle.getBundle("config");
	private static Properties props = new Properties();
	
	public static void init() {
		configProps.getKeys().asIterator().forEachRemaining(x -> {
			props.put(x, configProps.getString(x));
			String sysProp = System.getProperty((String)x);
			String sysEnv = System.getenv((String)x);
			if (!sysProp.isEmpty())
				props.put(x, sysProp);
			if (!sysEnv.isEmpty())
				props.put(x, sysEnv);
		});
		
	}
	
	public static String get(String key) {
		return props.getProperty(key);
	}
	
	public static int getInt(String key) {
		return Integer.parseInt(props.getProperty(key));
	}
	
	public static double getDouble(String key) {
		return Double.parseDouble(props.getProperty(key));
	}
	
	public static boolean getBoolean(String key) {
		return Boolean.parseBoolean(props.getProperty(key));
	}
	
	public static String getEnvArg(String key) {
		if (props.getProperty("env") == null || props.getProperty("env").isEmpty())
			throw new IllegalArgumentException("The environment property is not declared in config.properties");
		return props.getProperty(String.format("%s.%s", props.getProperty("env"), key));
	}
	
	static {
		init();
	}

}
