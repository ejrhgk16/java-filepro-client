package config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Properties;

import socketTestClient.ClientLogger;

public class SysInfo {
	private File searchDirectory;
	private int threadNum;
	private String serverIP;
	private int serverPort;
	private String logPath;
	private int loopIntervalTime;

	public void readConfigFile() {
	       Properties properties = new Properties();
	       String path = SysInfo.class.getResource("Config.properties").getPath();
	       try {
			path = URLDecoder.decode(path, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block2
			e.printStackTrace();
		} 
	       try {
			properties.load(new FileReader(path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	       String searchDirectoryPath = properties.getProperty("SearchDirectoryPath");
	       searchDirectory =new File(searchDirectoryPath);
	       checkDirectory(searchDirectory);
	       threadNum = Integer.parseInt(properties.getProperty("ThreadNum"));
	       serverIP = properties.getProperty("ServerIP");
	       serverPort = Integer.parseInt(properties.getProperty("ServerPort"));
	       logPath = properties.getProperty("LogPath");
	       File logDirect = new File(logPath);
	       checkDirectory(logDirect);
	       String logLevel = properties.getProperty("LogLevel");
	       System.out.println(logLevel);
	       ClientLogger clientLogger = ClientLogger.getInstance();
	       clientLogger.setLogConfig(logLevel, logPath);
	       loopIntervalTime = Integer.parseInt(properties.getProperty("LoopIntervalTime"));
	}
	
	public void checkDirectory(File f) {
		if(f.exists() == false) {
			f.mkdirs();
		}else {
			if(f.isDirectory() == false) {
				f.mkdirs();
			}
		}
		
	}
	
	public int getLoopIntervalTime(){
		return loopIntervalTime;
	}
	

	public File getSearchDirectory() {
		return searchDirectory;
	}
	
	public String getLogPath() {
		return logPath;
	}

	public int getThreadNum() {
		return threadNum;
	}

	public String getServerIP() {
		return serverIP;
	}

	public int getServerPort() {
		return serverPort;
	}
	
	

}
