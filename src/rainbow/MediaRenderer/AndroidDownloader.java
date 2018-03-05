package rainbow.MediaRenderer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Environment;
import android.os.storage.StorageManager;
import android.util.Log;

public abstract class AndroidDownloader {
	static int mTmpFilesnum = 0;
	final static String TAG = "Rainbow";
	
	String mUrl = "";
	HttpURLConnection mConnection;
	public final static String TmpDir = "/Rainbow/cache";
	public  static String TmpDirFullPath = "";
	
	File mTmpfile;
	String mTmpFileName ;
	boolean isStoped = false;
	static boolean isinited = false;
	
	int mNotifyRate = 10;
	int mTotalSize = 0;
	int mDownloadedSize = 0;
	final int 			MAX_RECONNECT_TIMES = 1;
	public DownloadFinished mDownloadFinished = null;
	
	
	
	static void init(){
		if(!isinited){
			String SDCard=Environment.getExternalStorageDirectory()+"";
			TmpDirFullPath = SDCard+TmpDir;
			Log.i(TAG, "Cache Dir :"+TmpDirFullPath);
			isinited = true;
			delAllFile(TmpDirFullPath);	
		}
	}
	
	public AndroidDownloader(String urlStr,int notifyRate) {
		mUrl = urlStr;
		mNotifyRate = notifyRate;
	//	mTmpFileName = tmpFileName;
		init();
	}
	
	public  String getCacheFile(){
		return mTmpfile.toString();
	}
	
	public boolean startDownload(){
	

		if(!initConnection())
			return false;
		
		DownloadThread thread = new DownloadThread();
		thread.start();
		return true;
	}
	
	
	
	int getCanPlayPosition(){
		return mTotalSize/mNotifyRate;
	}
	
	boolean  initConnection(){
		 URL url;
		try {
			url = new URL(mUrl);
		
		 mConnection =(HttpURLConnection)url.openConnection();  
		 mConnection.setConnectTimeout(5*1000);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 Log.w(TAG, "MalformedURLException : ");
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 Log.w(TAG, "IOException : ");

			return false;
		} 
		 return true;
	}
	
	boolean createTmpFile(){
		String SDCard=Environment.getExternalStorageDirectory()+"";   
		//File lastFile = new File(SDCard+TmpDir+"/"+(mTmpFilesnum++) +".tmp");
		String pathName=TmpDirFullPath+"/"+(mTmpFilesnum++) +".tmp";
		mTmpfile=new File(pathName);
		File Dir = new File(TmpDirFullPath);
		if(!Dir.exists())
			Dir.mkdirs();
/*		if(lastFile.canRead())
			lastFile.delete();*/
		if(mTmpfile.canRead())
			mTmpfile.delete();
		
		try {
			mTmpfile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e(TAG,"create new file failed : "+mTmpfile.getPath());
			return false;
		}
		return true;
	}
	
	void Download(){
		InputStream input;
		FileOutputStream output;
		int offset = 0;
		
		try {
			mTotalSize = mConnection.getContentLength();
			Log.d(TAG, "file total size: "+mTotalSize);

			 if(mTotalSize < 0){
				 if(!reconnect(MAX_RECONNECT_TIMES))
					 return;
			 }
		
			output=new FileOutputStream(mTmpfile);
			input  = mConnection.getInputStream();
			byte[] buffer=new byte[10*1024]; 
			int notifyPlayPosition = getCanPlayPosition();
			boolean hasNotified = false;
			
			int i = 0;
			while( (offset = input.read(buffer))!=-1){
				if(isStoped)
					break;
				output.write(buffer, 0, offset);
				mDownloadedSize += offset;	
				if(!hasNotified && mDownloadedSize > notifyPlayPosition){
					Downloadfinished();
					hasNotified = true;
				}
			//	if(flushFileCasheTimes)
				if(i++ > 100){
					Log.d(TAG, String.format("Downloaded  %d/%d",mDownloadedSize,mTotalSize));
					i = 0;
				}
			}
			Log.d(TAG, String.format("Downloaded  %d/%d",mDownloadedSize,mTotalSize));
			output.flush();
			input.close();
			output.close();
			
		} catch (FileNotFoundException e) {
			Log.e(TAG, "File not Found");
			e.printStackTrace();		
			return ;
		} catch (IOException e) {
			Log.e(TAG, "IOException err");
			e.printStackTrace();
			return ;
		}
		if(mNotifyRate ==1)
			Downloadfinished();
		if(mDownloadFinished != null){
			mDownloadFinished.Downloadfinished();
		}
		
	}
	
	private boolean reconnect(int times){
		Log.d(TAG, String.format("reconnecting  times: %d",times));

		int currentTimes = 0;
		while(currentTimes++ < times){
			mConnection.disconnect();
				 try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			}
			initConnection();
			mTotalSize = mConnection.getContentLength();
			if(mTotalSize > 0)
				return true;
		}
		return false;
	}
	
	public void setDownloadFinished(DownloadFinished mDownloadFinished){
		this.mDownloadFinished = mDownloadFinished;
	}
	
	public int getTatalSize(){
		return mTotalSize;
	}
	
	public int getDownloadedSize(){
		return mDownloadedSize;
	}
	
	
	void close(){
		if(mConnection	!=null	)
			mConnection.disconnect();
		isStoped = true;
	}
  
	public void Destroy(){
		close();

		if(mTmpfile!= null &&mTmpfile.canRead())
			mTmpfile.delete();
		Log.d(TAG,"Destroy");
	}
	
	class DownloadThread extends Thread{
		
		@Override
		public void run() {
			super.run();
			Download();	
		}
	}
	
	public abstract void Downloadfinished();
	
	public  interface  DownloadFinished{
		void Downloadfinished();
	}
	
	
	  public static void delAllFile(String path) {
          File file = new File(path);
          if (!file.exists()) {
                  return;
          }
          if (!file.isDirectory()) {
         return;
          }
          String[] tempList = file.list();
          File temp = null;
          for (int i = 0; i < tempList.length; i++) {
                  if (path.endsWith(File.separator)) {
                          temp = new File(path + tempList[i]);
                  }
                  else {
                          temp = new File(path + File.separator + tempList[i]);
                  }
                  if (temp.isFile()) {
                          temp.delete();
                  }
                  if (temp.isDirectory()) {
                          delAllFile(path+"/"+ tempList[i]);
                          delFolder(path+"/"+ tempList[i]);                  }
          }
	  }
	  
	  public static void delFolder(String folderPath) {
          try {
                  delAllFile(folderPath); 
                  String filePath = folderPath;
                  filePath = filePath.toString();
                  java.io.File myFilePath = new java.io.File(filePath);
                  myFilePath.delete(); 

          }
          catch (Exception e) {
                  System.out.println("删除文件夹操作出错");
                  e.printStackTrace();

          }
	  }
}
