package to8to_app.account;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import io.appium.java_client.android.AndroidDriver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.testng.annotations.BeforeMethod;

import to8to_app.to8to_app_Factory.OverallSituation;

public class Testfactory {	
	@BeforeMethod(alwaysRun =true)
	public void clickSetting(AndroidDriver aDriver){
		
		System.out.println("success!");
	}
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
	@Test
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
}
