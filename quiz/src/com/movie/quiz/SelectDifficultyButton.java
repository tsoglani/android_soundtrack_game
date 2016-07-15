package com.movie.quiz;


import android.content.Context;
import android.widget.Button;

public class SelectDifficultyButton extends Button{
private int enigmaCounter;
public static int foundCorrect=0;
private int stage;

public static String [] titles = {"Beginner","Amateur","Advance","Profesional","Expert"};
/**
 * 
 * @param context
 * @param stage
 */
	public SelectDifficultyButton(Context context,int stage) {
		super(context);
		this.stage=stage;
	    setText(titles[stage]);
		
		setEditabled(stage);
	}
	/**
	 * 
	 * @param i
	 */
	private void setEditabled(int i){
		foundCorrect= SelectEnigmaButton.foundAnsweresId.size();
		if(foundCorrect<i*15){
			this.setEnabled(false);
		}
	}
	
	

}
