package to8to_app.testInfoClass;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Vector;

import io.appium.java_client.android.AndroidDriver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.testng.annotations.BeforeMethod;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelSftp.LsEntry;

import to8to_app.globe.OverallSituation;
import to8to_app.globe.SftpFileControl;

public class Testfactory {	
	public void clickSetting(AndroidDriver aDriver){
		System.out.println("success!");
	}
	/*获取测试设备的设备号封装方法*/
	public void getDeviceName(){
		//adb -devices
		String command="adb devices";
		String s="设备ID：";
		String line = null;
		StringBuilder sb = new StringBuilder();
		Runtime runtime = Runtime.getRuntime();
		try {
		Process process = runtime.exec(command);
		BufferedReader	bufferedReader = new BufferedReader
				(new InputStreamReader(process.getInputStream()));
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line );
				if (line.contains(s)) {
					System.out.println(line);
				}
			}
			//分割出devices
			System.out.println("原字符："+sb+";");
			String devicesCode=sb.toString();
			System.out.println("设备号："+devicesCode+";");
			//已第24個字符开始,到右边-7个位置结束
			String devices1=StringUtils.substring(devicesCode,24, -7); 
			System.out.println("设备号截取后："+devices1+";");
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	/*截图公共方法测试*/
	public void snapshot() throws IOException {
		//获取当前工作文件夹+"devicesName:"+devicesName
		String devices=null;
		String screenShotName="123456.png";
		System.out.println("screenShotName++++"+screenShotName);
		String devicesName=OverallSituation.getDeviceName(devices);
		//String screenShotName=screenShot.getName();	
		System.out.println("devicesName++++"+devicesName);
		StringBuffer sBuffer = new StringBuffer(screenShotName);
		StringBuffer devicesname= sBuffer.append(screenShotName).insert(0,devicesName);
        String currentpath = System.getProperty("user.dir");     
        File imgDir = new File(currentpath, "Images");
        //File scrFile = driver.getScreenshotAs(OutputType.FILE); 
        System.out.println("截图保存目录:" + imgDir + "\\" + devicesname.toString() + "\n");
            //保存屏幕截图
            FileUtils.copyFile(imgDir, new File(imgDir + "\\" + screenShotName));          
       
            System.out.println("已保存的屏幕截图:" + currentpath); 
    }
	/*测试获取测试设备的Android版本号封装方法*/
	public void platformVersion(){
		String command="adb shell getprop ro.build.version.release";
		String s="android版本：";
		String line = null;
		System.out.println("command："+command);
		StringBuilder sb = new StringBuilder();
		Runtime runtime = Runtime.getRuntime();
		Process process;
		try {
			process = runtime.exec(command);
			BufferedReader	bufferedReader = new BufferedReader
					(new InputStreamReader(process.getInputStream()));
			while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
					if (line.contains(s)) {
						System.out.println(line);
					}
				}
			String platformVersion=sb.toString();
			System.out.println("原字符："+sb+";");
			System.out.println("新字符："+platformVersion+";");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*测试文件上传封装方法*/
	public static void main(String[] args) {
		SftpFileControl sf = new SftpFileControl();
		String host = "119.29.173.247";
		int port = 22;
		String username = "ftpuser1";
		String password = "to8to123";
		String directory = "/home/ftpuser1";
		String saveFile = "E:\\页端中大型需求开发规范（0623）(1).docx";
		// String upLoadDirectory = "E:\\371a6d5067a773ff192e7cc457c0b8fe.jpg";
		ChannelSftp sftp = null;
		Session sshSession = null;
		try {
			JSch jsch = new JSch();
			jsch.getSession(username, host, port);
			sshSession = jsch.getSession(username, host, port);
			System.out.println("Session created.");
			sshSession.setPassword(password);
			Properties sshConfig = new Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sshSession.setConfig(sshConfig);
			sshSession.connect();
			System.out.println("Session connected.");
			System.out.println("Opening Channel.");
			Channel channel = sshSession.openChannel("sftp");
			channel.connect();
			sftp = (ChannelSftp) channel;
			System.out.println("Connected to " + host);
			// // + "."
			// sf.upload(directory, uploadFile, sftp);
			// sf.download(directory, downloadFile, saveFile, sftp);
			// sf.delete(directory, deleteFile, sftp);

			// sftp.cd(directory);

			Vector<LsEntry> v = sf.listFiles(directory, sftp);
			for (LsEntry e : v) {
				if (!e.getFilename().startsWith(".")) {
					// saveFile = upLoadDirectory + e.getFilename();
					// String directory, String uploadFile, ChannelSftp sftp
					sf.upload(directory, saveFile, sftp);
				}
			}
			System.out.println("finished");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sftp.exit();
			sshSession.disconnect();
		}
	}
}