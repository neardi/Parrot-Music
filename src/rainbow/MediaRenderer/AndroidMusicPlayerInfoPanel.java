package rainbow.MediaRenderer;

import rainbow.service.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;

public class AndroidMusicPlayerInfoPanel extends ViewGroup{

	String TAG = "onlinemusic";
	Context mContext ;
	Bitmap mCoverBitmap = null;
	Bitmap mdefaultCover = null;
	Bitmap mBgBitmap = null;
	int mDrawedPercent = 0;
	Paint CoverPaint ;
	Paint TextPaint ;
	Paint TextPaint_artist ;

	String  mArtist = "";
	String	mTitle = "";
	
	int mPanelTextWidth = 0;
	

	public AndroidMusicPlayerInfoPanel(Context context) {
		 
		super(context);
	//	mPanel = (ViewGroup) View.inflate(context, R.layout.playinginfopanel, null);
		LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
	//	addView(View.inflate(context, R.layout.playinginfopanel, null),params);
		
		mContext = context;
		mdefaultCover = ((BitmapDrawable)getResources().getDrawable(R.drawable.album)).getBitmap();
		mBgBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.bg)).getBitmap();
		DrawInit();
	}
	
	public void setInfo(String title,String artist, Bitmap cover ){
		mArtist = artist;
		mTitle = title;
		if(mCoverBitmap != null && !mCoverBitmap.isRecycled()){
			mCoverBitmap.recycle();
		}
		mCoverBitmap =cover; 

		mPanelTextWidth = (int) TextPaint.measureText(mTitle);
		if(mPanelTextWidth < TextPaint_artist.measureText(mArtist))
			mPanelTextWidth = (int) TextPaint_artist.measureText(mArtist);
		
		mPanelTextWidth +=mPanelTextWidth/8 ;
	}
	
	protected void DrawInit(){
		CoverPaint = new Paint();
		CoverPaint.setAlpha(mDrawedPercent);

		TextPaint = new Paint();
		TextPaint.setColor(Color.WHITE);
		TextPaint.setAntiAlias(true); 
		TextPaint.setTextSize(20);
		TextPaint.setTextAlign(Paint.Align.CENTER);
		
		TextPaint_artist = new Paint();
		TextPaint_artist.setColor(Color.WHITE);
		TextPaint_artist.setAntiAlias(true); 
		TextPaint_artist.setTextAlign(Paint.Align.CENTER);
		TextPaint_artist.setTextSize(16);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mDrawedPercent += 10;
		
		CoverPaint.setAlpha(mDrawedPercent);
		TextPaint.setAlpha(mDrawedPercent);
		canvas.drawBitmap(mBgBitmap,null,new Rect(50, 520, 200+mPanelTextWidth, 660),CoverPaint);
		
		if(mCoverBitmap != null)
			canvas.drawBitmap(mCoverBitmap,null,new Rect(60, 530, 180, 653),CoverPaint);
		else 
			canvas.drawBitmap(mdefaultCover,null,new Rect(60, 530, 180, 653),CoverPaint);
		
		canvas.drawText(mTitle, 180+mPanelTextWidth/2, 580, TextPaint);
		canvas.drawText(mArtist,180+mPanelTextWidth/2, 620, TextPaint_artist);
	
		mHandler.sendEmptyMessageDelayed(1,5);
		//Log.i(TAG, String.format("===========>infopanel ondraw percent : %d",mDrawedPercent));
	}



	private  void  updateDisplay(){
		invalidate();
	}
	
	private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
               
                
            	Message m = obtainMessage(1);
        		if(mDrawedPercent > 235){
        			mDrawedPercent = 0;
        			return;
        		}
        		 updateDisplay();
        		//sendMessageDelayed(m, 10);
            }
        }
    };


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		
	}
}
