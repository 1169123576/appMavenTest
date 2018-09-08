package to8to_app.imgs.imgLibraies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Properties;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import to8to_app.globe.OverallSituation;

public class TestClassAppNg {
	private static AndroidDriver androidDriver = null;
	private static String devicesName = null;
	private static String platformVersion = null;
	OverallSituation overallSituation = new OverallSituation();

	@BeforeTest
	public static AndroidDriver setUp() throws Exception {
		devicesName = OverallSituation.getDeviceName(devicesName);
		platformVersion = OverallSituation.platformVersion(platformVersion);
		Properties properties = new Properties();
		BufferedReader bufferedReader = new BufferedReader(
				new FileReader("account-config.properties"));
		properties.load(bufferedReader);
		System.out.println(bufferedReader);
		String appActivity=null,appiumServerUrl=null;
		appActivity=properties.getProperty("TLaunchActivity");
		appiumServerUrl=properties.getProperty("AppiumServerName");
		File classpathRoot = new File(System.getProperty("user.dir"));
		File appDir = new File(classpathRoot, "apps");
		File app = new File(appDir, "app-to8to-release-8-13-6.3.0.7.apk");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("noReset", true);
		capabilities.setCapability("platformName", "Android");
		capabilities.setCapability("deviceName", devicesName);
		capabilities.setCapability("platformVersion", platformVersion);
		capabilities.setCapability("app", app.getAbsolutePath());
		capabilities.setCapability("appActivity",appActivity);
		/*capabilities.setCapability("sessionOverride", true); 
		 *	每次启动时覆盖session，否则第二次运行会报错不能新建session
		 */
		androidDriver = new AndroidDriver(new URL(appiumServerUrl), capabilities);
		return androidDriver;
	}
	@Test
	// 首次安装开启APP
	public void BtnStay() {
		try {
			Thread.sleep(3000);
			// 新安装后进入首页--你的装修状态
			MobileElement btnStay = (MobileElement) androidDriver
					.findElementById("com.to8to.housekeeper:id/btn_stay");
			System.out.println("-----btnStay------" + btnStay);
			btnStay.click();
			Thread.sleep(1000);
			// --你的户型
			MobileElement houseTypeLoft = (MobileElement) androidDriver
					.findElementById("com.to8to.housekeeper:id/btn_house_type_loft");
			houseTypeLoft.click();
			Thread.sleep(1500);
			// --你喜欢的风格
			MobileElement ivPictureCheck = (MobileElement) androidDriver
					.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.support.v4.view.ViewPager/android.widget.LinearLayout/android.widget.GridView/android.widget.RelativeLayout[7]/android.widget.ImageView[2]");
			ivPictureCheck.click();
			Thread.sleep(1500);
			// --开启装修之旅
			MobileElement btnStart = (MobileElement) androidDriver
					.findElementById("com.to8to.housekeeper:id/btn_start");
			btnStart.click();
			Thread.sleep(4000);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			try {
				Thread.sleep(5000);
				// startggdate();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("超时");
				e.printStackTrace();
			}
		}
	}

	// 非首次安装开启APP-启动页
	@Test(dependsOnMethods = { "BtnStay" })
	public void startggdate() {
		try {
			Thread.sleep(1000);
			// 我的个人中心
			MobileElement tabBar;
			tabBar = (MobileElement) androidDriver
					.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.FrameLayout[5]/android.widget.TextView");
			tabBar.click();
			System.out.println("tabBar:" + tabBar.toString()
					+ "--androidDriver--" + androidDriver);
			// 判断登录态
			Thread.sleep(2000);
			MobileElement tvLogin = (MobileElement) androidDriver
					.findElementById("com.to8to.housekeeper:id/tv_login");
			System.out.println("tabBar:" + tvLogin);
			tvLogin.click();
		} catch (Exception e) {
		}
	}

	// 账号密码登录
	@Test(dependsOnMethods = { "startggdate" })
	public void loginContact() {
		
		try {
			Properties properties = new Properties();
			BufferedReader bufferedReader = new BufferedReader(
					new FileReader("account-config.properties"));
			properties.load(bufferedReader);
			System.out.println(bufferedReader);
			String accountUserName=null,accountPassword=null;
			accountUserName=properties.getProperty("USERNAME");
			accountPassword=properties.getProperty("PASSWORD");
			// androidDriver=overallSituation.setUp(driver);
			// 短信验证码登录
			Thread.sleep(2000);
			// 账号密码登录
			MobileElement tvAccountLogin = (MobileElement) androidDriver
					.findElementById("com.to8to.housekeeper:id/tv_account_login");
			tvAccountLogin.click();
			/*截图 screenShot*/
			File screenShot = androidDriver
					.getScreenshotAs(OutputType.FILE);
			OverallSituation.snapshot(androidDriver, screenShot);
			Thread.sleep(2000);
			// 输入手机号
			MobileElement loginAccount = (MobileElement) androidDriver
					.findElementById("com.to8to.housekeeper:id/edit_login_account");
			loginAccount.click();
			loginAccount.sendKeys(accountUserName);
			Thread.sleep(1000);
			// 输入密码
			MobileElement loginPassword = (MobileElement) androidDriver
					.findElementById("com.to8to.housekeeper:id/edit_login_password");
			loginPassword.sendKeys(accountPassword);
			Thread.sleep(1000);
			// 触发登录操作
			try {
				MobileElement btnLogin = (MobileElement) androidDriver
						.findElementById("com.to8to.housekeeper:id/btn_login");
				btnLogin.click();
				Thread.sleep(1000);
				// clickSetting(androidDriver);
			} catch (Exception e) {
				System.out.println("登录失败");
				/*截图 screenShot*/
				screenShot = androidDriver
						.getScreenshotAs(OutputType.FILE);
				OverallSituation.snapshot(androidDriver, screenShot);
			}
		} catch (Exception e) {
			// clickSetting();
			System.out.println("error_自定义");
		}
	}

	@Test
	// 个人中心登录态
	public void clickSetting() {
		try {
			// androidDriver=overallSituation.setUp(driver);
			System.out.println("success!");
			/*截图 screenShot*/
			File screenShot = androidDriver.getScreenshotAs(OutputType.FILE);
			OverallSituation.snapshot(androidDriver, screenShot);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test(dependsOnMethods = { "clickSetting" })
	public void indexBanner() {
		try {
			// androidDriver=overallSituation.setUp(driver);
			System.out.println("index_banner111");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterTest
	public void tearDown() throws Exception {
		androidDriver.quit();
	}
}