package game.tsoglanakos.com.soundtrackgame;


import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.Button;

public class SelectEnigmaButton extends Button {

	private int number;
	private int databaseId;
	private String answere, question;
	private boolean isFound = false;
	private Context context;
	private boolean canUseNext;
	
	public static ArrayList<Integer> foundAnsweresId = new ArrayList<Integer>();

	public SelectEnigmaButton(Context context) {
		super(context);
		this.context = context;

	}

	public void setHasFound(boolean isFound) {
		if (isFound) {
			if (!foundAnsweresId.contains(number)) {
				foundAnsweresId.add(number);
			}

		}
		this.isFound = isFound;
	}

	public void setLevel(int level) {

		try {
			number = level;
			EnigmaAct.generateTable();

			setQuestion(EnigmaAct.listOfMusic.get(level));
			setAnswere(EnigmaAct.listOfAnswere.get(level));

		} catch (Exception e) {
			// e.printStackTrace();

		}
	}

	public boolean hasFound() {
		if (foundAnsweresId.contains(number)) {
			isFound = true;
		}
		return isFound;
	}

	/**
	 * 
	 */
	@SuppressLint("ResourceAsColor")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		
		Paint paint = new Paint();
		paint.setColor(Color.RED);

		if (hasFound()) {
			
			paint.setColor(getResources().getColor(R.color.GreenYellow));
			 //paint.setTextSize(getHeight()/2);
			//canvas.drawText("Found", getWidth()/2+getWidth()/5,getHeight()/2, paint);
			canvas.drawRect(new Rect(0, 0,getWidth(),getHeight()-5),paint);
			super.onDraw(canvas);
			//paint.setColor(R.color.black);
			Bitmap fingerprint = BitmapFactory.decodeResource(context.getResources(), R.drawable.correct_3);
			Matrix aMatrix = new Matrix();
			aMatrix.setTranslate(getWidth() / 20,0);
			canvas.drawBitmap(fingerprint, aMatrix, paint);
			Bitmap fingerprint2 = BitmapFactory.decodeResource(context.getResources(), R.drawable.correct_1);
			Matrix aMatrix2 = new Matrix();
			aMatrix2.setTranslate(getWidth() / 2+getWidth()/5,0);
			canvas.drawBitmap(fingerprint2, aMatrix2, paint);
		}
		super.onDraw(canvas);
	}

	/**
	 * 
	 * @return
	 */
	public int getLevel() {
		return number;
	}

	/**
	 * 
	 * @return
	 */
	public String getAnswere() {

		return answere;
	}

	/**
	 * 
	 * @param answere
	 */
	public void setAnswere(String answere) {
		this.answere = answere;
	}

	/**
	 * 
	 * @return
	 */
	public String getQuestion() {

		return question;
	}

	/**
	 * 
	 * @param question
	 */
	public void setQuestion(String question) {
		this.question = question;
	}

	public void setDbId(int databaseId) {
		this.databaseId = databaseId;
	}

	public int getDbID() {
		return databaseId;
	}
	
	public boolean canNotUseNext(){

		return ((number+1)%20==0&&number>=19);
	}
	public boolean useNext2(){
		for(int i=1;i<10;i++){
			if(SelectDifficultyButton.foundCorrect<EnigmaAct.correctMustHaveInEachDifficulty*i&&number>=19*i){
				return false;
			}
		}
		
		
		
		return true;
	}
	

}
