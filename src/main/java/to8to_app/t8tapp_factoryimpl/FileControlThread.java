package to8to_app.t8tapp_factoryimpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.Properties;
import java.util.Vector;

import to8to_app.globe.OverallSituation;
import to8to_app.globe.SftpFileControl;
import to8to_app.imgs.imgLibraies.TestClassAppNg;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.ChannelSftp.LsEntry;

public class FileControlThread implements Runnable {
	//上传文件操作，采用独立线程上传，不会影响主线程
	private Socket socket;
	public FileControlThread(Socket socket)
	{
		this.socket = socket;
	}

	public void run() {
		Properties properties = new Properties();
		try {
			properties.load(TestClassAppNg.class.getClassLoader().getClass().getResourceAsStream("/properties_file/ftpUtils-config.properties"));
			String ftpHost = null, ftpUserName = null, ftpPassword = null, directory = null;
			String saveFile=null;
			OverallSituation overallSituation = new OverallSituation();
			
			String saveFileName=overallSituation.getFileControl(saveFile);
			System.out.println("saveFileName:"+saveFileName);
			Integer port = 0;
			ftpHost = properties.getProperty("HOSTNAME");
			port = Integer.parseInt(properties.getProperty("PORT"));
			ftpUserName = properties.getProperty("USERNAME");
			ftpPassword = properties.getProperty("PASSWORD");
			directory = properties.getProperty("DIRECTORY");
			System.out.println("截图保存目录:" +saveFileName);
			/*开启ftp连接，上传保存到项目中的截图到ftp服务器 */
			SftpFileControl sf = new SftpFileControl();
			ChannelSftp sftp = null;
			Session sshSession = null;
			JSch jsch = new JSch();
			sshSession = jsch.getSession(ftpUserName, ftpHost, port);
			System.out.println("Session created.");
			sshSession.setPassword(properties.getProperty(ftpPassword));
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			System.out.println("Session connected.");
			System.out.println("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			System.out.println("Connected to "
					+ properties.getProperty(ftpHost));
			Vector<LsEntry> v = sf.listFiles(directory, sftp);
			for (LsEntry e : v) {
				if (!e.getFilename().startsWith(".")) {
					/* saveFile = upLoadDirectory + e.getFilename();
					 String directory, String uploadFile, ChannelSftp sftp
					 *
					 *	文件上传
					 */
					sf.upload(directory, saveFileName, sftp);
				}
			}
			System.out.println("finished");
		} catch (FileNotFoundException e) {
			System.out.println("FileReader-Exception");
		} catch (IOException e) {
			System.out.println("FileReader-Exception");
		} catch (NumberFormatException e) {
			System.out.println("String转Integer-Exception");
		} catch (JSchException e) {
			System.out.println("文件上传连接Ftp服务器-Exception");
		} catch (SftpException e1) {
			System.out.println("文件操作-Exception");
		}
	}

}
