package to8to_app.testInfoClass;

import java.io.File;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.OutputType;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import to8to_app.globe.OverallSituation;

public class TestClassApp_load {

	private AndroidDriver androidDriver = null;
	String fileName=null;
	@Test
	public void BtnStay(){
		try {
			System.out.println("--driver--" + androidDriver);
			Thread.sleep(3000);
			//新安装后进入首页
			MobileElement btnStay = (MobileElement) androidDriver
					.findElementById("com.to8to.housekeeper:id/btn_stay");
			System.out.println("-----btnStay------" + btnStay);
			btnStay.click();
			Thread.sleep(1500);
			MobileElement houseTypeLoft = (MobileElement) androidDriver
					.findElementById("com.to8to.housekeeper:id/btn_house_type_loft");
			houseTypeLoft.click();
			Thread.sleep(4500);
			MobileElement ivPictureCheck = (MobileElement) androidDriver
					.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.support.v4.view.ViewPager/android.widget.LinearLayout/android.widget.GridView/android.widget.RelativeLayout[7]/android.widget.ImageView[2]");
			ivPictureCheck.click();
			Thread.sleep(2000);
			MobileElement btnStart = (MobileElement) androidDriver
					.findElementById("com.to8to.housekeeper:id/btn_start");
			btnStart.click();
			Thread.sleep(3000);	
			loginContact(androidDriver);
		} catch (Exception e) {
			try {
				Thread.sleep(5000);
				System.out.println("这是闪屏广告~~");
				loginContact(androidDriver);
			} catch (InterruptedException e1) {
				System.out.println("这是跳转有误~~");
				e1.printStackTrace();
			}
			//androidDriver.quit();
			e.printStackTrace();
		}	
	}
	// 账号密码登录
	@Test
	public void loginContact(AndroidDriver androidDriver) {
		// 跳转到登录页面？？
		try {
			//短信验证码登录
			MobileElement tabBar;
			tabBar = (MobileElement) androidDriver
					.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.FrameLayout[5]/android.widget.TextView");
			tabBar.click();
			System.out.println("tabBar:" + tabBar.toString()
					+ "--androidDriver--" + androidDriver);
			Thread.sleep(2000);
				MobileElement tvLogin = (MobileElement) androidDriver
						.findElementById("com.to8to.housekeeper:id/tv_login");
				tvLogin.click();
				Thread.sleep(1000);
				// 账号密码登录
				//boolean isInstall = false;
				MobileElement tvAccountLogin = (MobileElement) androidDriver
						.findElementById("com.to8to.housekeeper:id/tv_account_login");
				tvAccountLogin.click();

				Thread.sleep(2000);
				// 输入手机号
				MobileElement loginAccount = (MobileElement) androidDriver
						.findElementById("com.to8to.housekeeper:id/edit_login_account");
				loginAccount.click();
				loginAccount.sendKeys("18675503241");
				Thread.sleep(1000);		
				// 输入密码
				MobileElement loginPassword = (MobileElement) androidDriver
						.findElementById("com.to8to.housekeeper:id/edit_login_password");
				loginPassword.sendKeys("to8to123");
				Thread.sleep(1000);
				// 触发登录操作
				try {
					MobileElement btnLogin = (MobileElement) androidDriver
							.findElementById("com.to8to.housekeeper:id/btn_login");
					btnLogin.click();
					Thread.sleep(500);
					clickSetting(androidDriver);
				} catch (Exception e) {
					System.out.println("登录失败");
					//截图
					File screenShot = androidDriver.getScreenshotAs(OutputType.FILE);
					OverallSituation.snapshot(androidDriver, screenShot);	
					e.printStackTrace();
				}			
			} catch (Exception e) {
				System.out.println("該用戶已登录~");
				clickSetting(androidDriver);
				e.printStackTrace();
			}	 
	}
	@Test
	public void clickSetting(AndroidDriver aDriver){
		System.out.println("success!");
		File screenShot = androidDriver.getScreenshotAs(OutputType.FILE);
		OverallSituation.snapshot(androidDriver, screenShot);
	}
}