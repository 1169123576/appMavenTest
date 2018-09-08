package to8to_app.globe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.SftpException;

public class OverallSituation {
	private static String devicesName = null;

	// 截图处理公共方法
	public static void snapshot(TakesScreenshot driver, File screenShot) {
		// 获取当前工作文件夹+"devicesName:"+devicesName
		devicesName = OverallSituation.getDeviceName(devicesName);
		devicesName = devicesName + "-";
		String screenShotName = screenShot.getName();
		System.out.println("screenShotName=" + screenShotName);
		StringBuffer sBuffer = new StringBuffer();
		StringBuffer devicesname = sBuffer.append(screenShotName).insert(0,
				devicesName);
		String currentpath = System.getProperty("user.dir");
		File imgDir = new File(currentpath, "Images");
		File scrFile = driver.getScreenshotAs(OutputType.FILE);
		//
		try {
			/*
			 * 保存屏幕截图， devicesname:图片名称
			 */
			FileUtils.copyFile(scrFile, new File(imgDir + "\\" + devicesname));
		} catch (IOException e) {
			System.out.println("保存截图失败");
			e.printStackTrace();
		} finally {
			System.out.println("已保存的屏幕截图:" + currentpath);
			/*
			 * 上传截图到ftp服务器 调用SftpFileControl类中封装的上传方法
			 * 读取配置文件中的ftp参数ftpUtils-config.properties
			 * 
			 * 目前上传下载都在主线程里执行，容易造成线程阻塞，需要采用多线程方法处理，
			 * 开启独立的线程进行文件、图片上传和下载操作
			 */
			Properties properties = new Properties();
			try {
				BufferedReader bufferedReader = new BufferedReader(
						new FileReader("ftpUtils-config.properties"));
				properties.load(bufferedReader);
				System.out.println(bufferedReader);
				String ftpHost = null, ftpUserName = null, ftpPassword = null, directory = null;
				Integer port = 0;
				ftpHost = properties.getProperty("HOSTNAME");
				port = Integer.parseInt(properties.getProperty("PORT"));
				ftpUserName = properties.getProperty("USERNAME");
				ftpPassword = properties.getProperty("PASSWORD");
				directory = properties.getProperty("DIRECTORY");
				String saveFile = imgDir + "\\" + devicesname;
				System.out.println("截图保存目录:" +saveFile);
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
						sf.upload(directory, saveFile, sftp);
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
	
	// 获取当前時間
	public String getDatetime() {
		SimpleDateFormat date = new SimpleDateFormat("yyyymmdd hhmmss");
		return date.format(new Date());
	}

	// 获取设备号name：集成adb命令
	public static String getDeviceName(String devicesName) {
		String command = "adb devices";
		String s = "设备ID：";
		String line = null;
		StringBuilder sbuider = new StringBuilder();
		Runtime runtime = Runtime.getRuntime();
		try {
			Process process = runtime.exec(command);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			while ((line = bufferedReader.readLine()) != null) {
				sbuider.append(line);
				if (line.contains(s)) {
					System.out.println(line);
				}
			}
			// 分割出devices
			// System.out.println("原字符："+sbuider+";");
			String devicesCode = sbuider.toString();
			// System.out.println("设备号："+devicesCode+";");
			// 以第24個字符开始,到右边-7个位置结束
			devicesName = StringUtils.substring(devicesCode, 24, -7);
			System.out.println("设备号截取后：" + devicesName + ";");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return devicesName;
	}

	// 获取Android版本号
	public static String platformVersion(String platformVersion) {
		// adb -devices
		String command = "adb shell getprop ro.build.version.release";
		String s = "android版本：";
		String line = null;
		// System.out.println("command："+command);
		StringBuilder sbuilder = new StringBuilder();
		Runtime runtime = Runtime.getRuntime();
		Process process;
		try {
			process = runtime.exec(command);
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(process.getInputStream()));
			while ((line = bufferedReader.readLine()) != null) {
				sbuilder.append(line);
				if (line.contains(s)) {
					System.out.println(line);
				}
			}
			platformVersion = sbuilder.toString();
			// System.out.println("原字符："+sbuilder+";");
			System.out.println("新字符：" + platformVersion + ";");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return platformVersion;
	}
}