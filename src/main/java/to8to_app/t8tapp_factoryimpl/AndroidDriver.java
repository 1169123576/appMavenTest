package to8to_app.t8tapp_factoryimpl;

public class AndroidDriver {
	  private AndroidDriver(){
	    }
	    private static AndroidDriver androidDriver=new AndroidDriver();
	    public static synchronized AndroidDriver getInstance(){   //多线程时注意线程安全
	        if(androidDriver == null){
	        	androidDriver = new AndroidDriver();
	        }
	        return androidDriver;
	    }
}