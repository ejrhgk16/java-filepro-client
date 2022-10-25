package socketTestClient;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

import config.SysInfo;

public class FileSendThread implements Runnable {

	private Socket socket;
	private InputStream is;
	private OutputStream os;
	private SysInfo sysinfo;
	private File sendFile;
	private ClientLogger clientLogger;
	
	

	public FileSendThread(SysInfo sysinfo, File sendFile) {
		this.sysinfo = sysinfo;
		this.sendFile = sendFile;
		clientLogger = ClientLogger.getInstance();
		
	}

	@Override
	public void run() {
		
		DataOutputStream dos = null;
		FileInputStream fis = null;
		clientLogger.fine("SendThread", "스레드생성!!");
		System.out.println("스레드생성");
		int readCnt = 0;
		long filesize =0;
		try {
			String filename = sendFile.getName();
			filesize = sendFile.length();
			socket = new Socket();
			socket.connect(new InetSocketAddress(sysinfo.getServerIP(), sysinfo.getServerPort()), 3000);
			
			clientLogger.fine(this.getClass().getSimpleName(), sysinfo.getServerIP()+"_socketConnect");
		
			os = socket.getOutputStream();
			dos = new DataOutputStream(new BufferedOutputStream(os));
			fis = new FileInputStream(sendFile);
			
			
			String fileNS = filename + "/" + filesize;
			System.out.println(fileNS);
			dos.writeUTF(fileNS);
			byte[] buffer = new byte[1024];
			int n = 0;
			

			while ((n = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, n);
				dos.flush();
				readCnt += n;

			}

			clientLogger.info(this.getClass().getSimpleName(), filename+"_sendComplete");

		}catch (IOException  e) {
			clientLogger.severe("SendThread",e.getMessage());
		}finally {

			  if(dos != null) {try {System.out.println("dos.close"); dos.close();} catch (IOException e) {clientLogger.severe(this.getClass().getSimpleName(), e.getMessage());}}
			  if(fis != null) {try {System.out.println("fis.close");fis.close();} catch (IOException e) {clientLogger.severe(this.getClass().getSimpleName(), e.getMessage());}}
			  if(os != null) {try {System.out.println("os.close");os.close();} catch (IOException e) {clientLogger.severe(this.getClass().getSimpleName(), e.getMessage());}}
			  if(readCnt == filesize) {System.out.println("readcnt " + readCnt); System.out.println("filesize  " + filesize); sendFile.delete();}//파일전송 완료 시 삭제
			  try {if(socket!=null) {if(!socket.isClosed()) {System.out.println("sicket.close"); socket.close();}}} catch (IOException e) {clientLogger.severe(this.getClass().getSimpleName(), e.getMessage());}			

		}

	}

}
