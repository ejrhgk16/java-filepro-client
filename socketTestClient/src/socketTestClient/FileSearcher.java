package socketTestClient;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


import config.SysInfo;

public class FileSearcher {

	private ExecutorService excutorService;
	private SysInfo sysInfo;
	private ClientLogger clientLogger;


	public FileSearcher(SysInfo sysInfo, ExecutorService es) {
		this.sysInfo = sysInfo;
		excutorService = es;
		clientLogger = ClientLogger.getInstance();
		
	}

	public File[] getFileList() {
		File file = sysInfo.getSearchDirectory();
		return file.listFiles();
	}

	public Future<?> sendFile(File f) {
		System.out.println("sendfile   "+f.getName()+"  sendfile  " + f.length());
		FileSendThread fileSendThread = new FileSendThread(sysInfo, f);
		Future<?>future= excutorService.submit(fileSendThread);
		clientLogger.fine("Main", f.getName() + "_submit!");
		return future;
		
	}

}
