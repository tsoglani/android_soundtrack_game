package game.tsoglani.soundtrack.soundtrackgame;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.widget.Button;

public class SelectDifficultyButton extends Button{
private int enigmaCounter;
public static int foundCorrect=0;
private int stage;
private Bitmap bitmap;
private Context context;
private boolean isEnable=false;

public static String [] titles = {"Junior","Beginner","Advance","Professional","Expert"};
/**
 * 
 * @param context
 * @param stage
 */
	public SelectDifficultyButton(Context context,int stage) {
		super(context);
		this.stage=stage;
	    this.context=context;
		setText(titles[stage]);
		
		setEditabled(stage);
	}
	/**
	 * 
	 * @param i
	 */
	private void setEditabled(int i){
		foundCorrect= SelectEnigmaButton.foundAnsweresId.size();
		if(foundCorrect<i*EnigmaAct.correctMustHaveInEachDifficulty){
			setEnabled(false);
			isEnable=false;
		}else{
			isEnable=true;
		}
		this.invalidate();
		this.postInvalidate();
	}
	private int getRemainingsToUnlock(){
		foundCorrect= SelectEnigmaButton.foundAnsweresId.size();
	int sum=stage*EnigmaAct.correctMustHaveInEachDifficulty;
	return sum-foundCorrect;
	}
	
	public boolean getIsEnabled(){
		return isEnable;
	}
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		Paint paint= new Paint();
		Matrix matrix=new Matrix();
		try{
		if(isEnable){
			matrix.setTranslate(getWidth()/20, 0);
			bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.unlock);
		}else{
			matrix.setTranslate(getWidth()/30, 0);
			paint.setTextSize(getWidth()/20);
			canvas.drawText(Integer.toString(this.getRemainingsToUnlock())+" to unlock", getWidth()/2+getWidth()/5, getHeight()/4, paint);
		bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.lock);
		}

			float scale=(getHeight()/(float)(bitmap.getHeight()+bitmap.getHeight()/8));

			bitmap=getResizedBitmap(bitmap,scale,scale);
		canvas.drawBitmap(bitmap, 20,0, paint);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

	public Bitmap getResizedBitmap(Bitmap bm, float scaleX, float scaleY) {
		int width = bm.getWidth();
		int height = bm.getHeight();
//		float scaleWidth = ((float) newWidth) / width;
//		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleX, scaleY);

		// "RECREATE" THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(
				bm, 0, 0, width, height, matrix, false);
		bm.recycle();
		return resizedBitmap;
	}

}
