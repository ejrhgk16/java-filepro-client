package socketTestClient;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import config.SysInfo;

public class ClientMain {

	public static void main(String[] args) {

		SysInfo sysInfo = new SysInfo();
		sysInfo.readConfigFile();
		ExecutorService es = Executors.newFixedThreadPool(sysInfo.getThreadNum());
		ClientLogger clientLogger = ClientLogger.getInstance();
		FileSearcher fileSearcher = new FileSearcher(sysInfo, es);
		List<Future<?>> futureList = new ArrayList<Future<?>>();

		while (!Thread.currentThread().isInterrupted()) {
			System.out.println("와일시작");
			File[] fileList = fileSearcher.getFileList();
			if (fileList != null) {
				for (File f : fileList) {
					if (f.isFile()) {
						System.out.println("mainfor");
						Future<?> future = fileSearcher.sendFile(f);
						futureList.add(future);
					}

				}

				for (Future<?> future : futureList) {
					try {
						future.get();
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			try {
				Thread.sleep(sysInfo.getLoopIntervalTime());
			} catch (InterruptedException e) {
				clientLogger.severe("Main", e.getMessage());
				es.shutdown();
				Thread.currentThread().interrupt();

			}

		}
	}
}
