package com.movie.quiz;

import java.util.ArrayList;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

public class EnigmaGameMenuActivity extends Activity {
	private static String calledBy;
	private LinearLayout linear;
	private LinearLayout layout;
	// private GameMenuView gameMenuView;
	public static int currentStage = 0;
	public static int difficulty=0;
	private ScrollView scroll;
	public static int totalLevels = 100;// the number of how much quiz I would
										// have
	public static ArrayList<SelectEnigmaButton> listButtons= new ArrayList<SelectEnigmaButton>();
	public static ArrayList<SelectEnigmaButton> listlevel_0= new ArrayList<SelectEnigmaButton>();
	public static ArrayList<SelectEnigmaButton> listlevel_1= new ArrayList<SelectEnigmaButton>();
	public static ArrayList<SelectEnigmaButton> listlevel_2= new ArrayList<SelectEnigmaButton>();
	public static ArrayList<SelectEnigmaButton> listlevel_3= new ArrayList<SelectEnigmaButton>();
	public static ArrayList<SelectEnigmaButton> listlevel_4= new ArrayList<SelectEnigmaButton>();
	// public static String str;
	/**
 * 
 */
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();

		/*
		 * GameMenuView view= new GameMenuView(this); ScrollView sv = new
		 * ScrollView(this);
		 * 
		 * for(int i = 0; i < 20; i++) { Button b = new Button(this);
		 * b.setText("movie "+Integer.toString(i+1)); view.addView(b); }
		 * layout.addView(sv);
		 */

		setContentView(layout);

	}

	/**
 * 
 */
	private void init() {
		layout = new LinearLayout(this);
		/*listButtons = new ArrayList<SelectEnigmaButton>();
		listlevel_1= new ArrayList<SelectEnigmaButton>();
		listlevel_2= new ArrayList<SelectEnigmaButton>();
		listlevel_3= new ArrayList<SelectEnigmaButton>();
		listlevel_4= new ArrayList<SelectEnigmaButton>();
		listlevel_5= new ArrayList<SelectEnigmaButton>();
		*/
		ScrollView sv = new ScrollView(this);
		sv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		// GameMenuView view= new GameMenuView(this);
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		ll.setOrientation(1);
		sv.addView(ll);

		createSelectQuizButtons();

		for (int i = 0; i < 20; i++) {
			ll.addView(listButtons.get(i + difficulty * 20));
		}

		layout.addView(sv);

	}

	/**
	 * 
	 * @return
	 */
	public static String getCurrentMusic() {
		return EnigmaAct.getCurrentButton().getQuestion();
	}

	/**
	 * 
	 * @return
	 */
	public static String getCurrentAnswere() {
		
			return  EnigmaAct.getCurrentButton().getAnswere();
	
	}
	
	

	/**
	 * 
	 */
	private void createSelectQuizButtons() {
		listButtons.removeAll(listButtons);
		for (int i = 0; i < totalLevels; i++) {
			SelectEnigmaButton b = new SelectEnigmaButton(this);
			b.setText("movie " + Integer.toString(i + 1));
			b.setLevel( i);
			b.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(EnigmaGameMenuActivity.this,EnigmaAct.class));
					SelectEnigmaButton b = (SelectEnigmaButton) v;
					currentStage = Integer.parseInt((String) (b.getText().subSequence(6, b.getText().length())))-1;
					EnigmaAct.setButton(b);

				}

			});
			if(b.hasFound()){
				b.invalidate();
				b.postInvalidate();
			
			
			}
			listButtons.add(b);
			if(b.getLevel()<20){
				listlevel_0.add(b);
			}else if(b.getLevel()<40){
				listlevel_1.add(b);
			}else if(b.getLevel()<60){
				listlevel_2.add(b);
			}else if(b.getLevel()<80){
				listlevel_3.add(b);
			}else if(b.getLevel()<totalLevels){
				listlevel_4.add(b);
			}
			
			
		}
		removeFindQuizFromArrayLists(listlevel_0);
		removeFindQuizFromArrayLists(listlevel_1);
		removeFindQuizFromArrayLists(listlevel_2);
		removeFindQuizFromArrayLists(listlevel_3);
		removeFindQuizFromArrayLists(listlevel_4);
		for(int j=0; j<10;j++){
			Log.e(Integer.toString(EnigmaGameMenuActivity.listlevel_0.size()), Integer.toString(EnigmaGameMenuActivity.listlevel_1.size()));
			}
	}
	private void removeFindQuizFromArrayLists(ArrayList <SelectEnigmaButton>array){
		for(int id:SelectEnigmaButton.foundAnsweresId){
			for(int i=0;i<array.size();i++){
				
				if(array.get(i).getLevel()==id){
					array.remove(i);
					break;
				}
			}
		}
	}

	/**
 * 
 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_game, menu);
	
		return true;
	}

	/**
	 * 
	 * @param s
	 */
	public static void setCalledby(String s) {
		calledBy = s;

		for (int i = 0; i < SelectDifficultyButton.titles.length; i++) {
			if (calledBy.equals(SelectDifficultyButton.titles[i])) {
				difficulty = i;
				break;
			}
		}

	}
	public void onBackPressed() {
		 // super.onBackPressed();
		
			startActivity(new Intent(this,MainGameMenuActivity.class));
		 
		}
}
