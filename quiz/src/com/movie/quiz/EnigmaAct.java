package com.movie.quiz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import android.R.color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EnigmaAct extends Activity {
	private static MediaPlayer mPlayer;
	private Button nextButton;
	private TextView showCurrentLevel, inputView;
	private Button playButton;
	private EditText inputText;
	private String startingInputTextString = "enter film's soundtrack";
	private boolean isPlayingAtTheTime = true;
	private static SelectEnigmaButton button;
	public static ArrayList<String> listOfMusic = new ArrayList();
	public static ArrayList<String> listOfAnswere = new ArrayList();
	private static MediaPlayer player;
	private static Database database;
	private boolean isNearToFind = false;
	
	public static int correctMustHaveInEachDifficulty = 15;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enigma);
		init();

		inputText.setTextColor(R.color.transparent_white_percent_95);
		inputText.setText(startingInputTextString);

		showCurrentLevel.setText("movie No "
				+ Integer.toString(EnigmaGameMenuActivity.currentStage + 1));
		// showCurrentLevel.setBackgroundColor(R.color.BlueViolet);
		playButton.setBackgroundResource(R.drawable.play_button);
		nextButton.setEnabled(true);
		nextButton.setBackgroundResource(R.drawable.next);

		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				nextEnigma3();
			}
		});

		inputText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (button.hasFound()) {
					inputText.setEnabled(false);
					inputText.setBackgroundColor(R.color.YellowGreen);
					inputText.setText(button.getAnswere());
					return;
				}
				if (inputText.getText().toString()
						.equals(startingInputTextString)) {
					inputText.setTextColor(R.color.black);
					inputText.setText("");
				}
			}
		});
		inputText.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (button.hasFound()) {
					foundItFunction();
					return true;
				}
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					if (isInputCorrect()) {
						button.setHasFound(true);
						foundItFunction();
						Toast.makeText(EnigmaAct.this, "Correct, you found it",
								Toast.LENGTH_LONG).show();
					} else if (!isNearToFind) {
						Toast.makeText(EnigmaAct.this, "incorrect ,try again",
								Toast.LENGTH_SHORT).show();
					} else if (isNearToFind) {
						isNearToFind = false;
					}
					return true;
				}
				return false;
			}
		});

		playButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				switchPlayStop();

			}
		});
		if (button != null
				&& button.hasFound()
				|| button != null
				&& SelectEnigmaButton.foundAnsweresId.contains(button
						.getLevel())) {
			foundItFunction();
			Toast.makeText(EnigmaAct.this, "You have found it",
					Toast.LENGTH_LONG).show();
		}
		
		
		
		ArrayList <SelectEnigmaButton> array=null;
		if(EnigmaGameMenuActivity.difficulty==0){
			array=EnigmaGameMenuActivity.listlevel_0;
		}else if(EnigmaGameMenuActivity.difficulty==1){
			array=EnigmaGameMenuActivity.listlevel_1;
		}else if(EnigmaGameMenuActivity.difficulty==2){
			array=EnigmaGameMenuActivity.listlevel_2;
		}else if(EnigmaGameMenuActivity.difficulty==3){
			array=EnigmaGameMenuActivity.listlevel_3;
		}else if(EnigmaGameMenuActivity.difficulty==4){
			array=EnigmaGameMenuActivity.listlevel_4;
		}
		if(array.isEmpty()){
			nextButton.setEnabled(false);
		}
		Toast.makeText(this, "Congratulations you complete the level ", Toast.LENGTH_LONG);
		
	}

	private void foundItFunction() {
		inputText.setBackgroundColor(R.color.YellowGreen);
		inputText.setEnabled(false);
		inputText.setText(button.getAnswere());
		EnigmaGameMenuActivity.listlevel_0.remove(button);
		EnigmaGameMenuActivity.listlevel_1.remove(button);
		EnigmaGameMenuActivity.listlevel_2.remove(button);
		EnigmaGameMenuActivity.listlevel_3.remove(button);
		EnigmaGameMenuActivity.listlevel_4.remove(button);
		database.addContact(button.getLevel());
		
		ArrayList <SelectEnigmaButton> array=null;
		if(EnigmaGameMenuActivity.difficulty==0){
			array=EnigmaGameMenuActivity.listlevel_0;
		}else if(EnigmaGameMenuActivity.difficulty==1){
			array=EnigmaGameMenuActivity.listlevel_1;
		}else if(EnigmaGameMenuActivity.difficulty==2){
			array=EnigmaGameMenuActivity.listlevel_2;
		}else if(EnigmaGameMenuActivity.difficulty==3){
			array=EnigmaGameMenuActivity.listlevel_3;
		}else if(EnigmaGameMenuActivity.difficulty==4){
			array=EnigmaGameMenuActivity.listlevel_4;
		}
		if(array.isEmpty()){
			nextButton.setEnabled(false);
		}
		
	}

	/**
	 * 
	 * @return
	 */
	private boolean isInputCorrect() {
		if (inputText.getText().toString()
				.equalsIgnoreCase(button.getAnswere())
				|| (button.getAnswere()
						.contains(inputText.getText().toString()) && inputText
						.getText().toString().length() >= 10)) {
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(inputText.getWindowToken(), 0);
			return true;
		}
		String text = inputText.getText().toString();
		ArrayList<Character> listInput = new ArrayList<Character>();
		ArrayList<Character> listAnswere = new ArrayList<Character>();
		for (int i = 0; i < text.length(); i++) {
			listInput.add((char) text.indexOf(i));
		}
		for (int i = 0; i < button.getAnswere().length(); i++) {
			listAnswere.add((char) button.getAnswere().indexOf(i));
		}

		int inputSize = listInput.size();
		int answereSize = listAnswere.size();
		int counter1 = 0;
		int counter2 = 0;
		for (int i = 0; i < listInput.size(); i++) {
			if (listAnswere.contains(listInput.get(i))) {
				listAnswere.remove(listInput.get(i));
				counter1++;
			}
		}
		listInput.removeAll(listInput);
		listAnswere.removeAll(listAnswere);
		for (int i = 0; i < text.length(); i++) {

			listInput.add((char) text.indexOf(i));
		}
		for (int i = 0; i < button.getAnswere().length(); i++) {
			listAnswere.add((char) button.getAnswere().indexOf(i));
		}

		for (int i = 0; i < listAnswere.size(); i++) {
			if (listInput.contains(listAnswere.get(i))) {
				counter2++;
				listInput.remove(listAnswere.get(i));
			}
		}
		int maxMistake = (int) (15.0 * listAnswere.size() / 100);
		int nearMistake = (int) (40.0 * listAnswere.size() / 100);
		if ((answereSize - maxMistake) <= counter1
				&& counter1 <= (answereSize + maxMistake)
				|| (answereSize - maxMistake) <= counter2
				&& counter2 < (answereSize + maxMistake)) {
			return true;
		}
		if (answereSize - nearMistake < counter1
				&& counter1 < answereSize + nearMistake
				|| answereSize - nearMistake < counter2
				&& counter2 < answereSize + nearMistake) {
			Toast.makeText(this, "You are close", Toast.LENGTH_LONG).show();
			isNearToFind = true;
		}

		/*
		 * else if(inputText.getText().toString().contains(" ")){
		 * ArrayList<String> list= new ArrayList<String>(); String [] words
		 * =inputText.getText().toString().split(" "); for(String word:words){
		 * list.add(word); }
		 * 
		 * if(button.getAnswere().equalsIgnoreCase("")){
		 * 
		 * 
		 * } }
		 */

		return false;
	}

	/**
	 * 
	 */
	public static void generateTable() {
		if (!listOfMusic.isEmpty() && !listOfAnswere.isEmpty()) {
			return;
		}
		addToList("amelie", "amelie");
		addToList("being_john_malkovich", "being john malkovich");
		addToList("donnie_darko", "donnie darko");
		addToList("happy_days", "happy days");
		addToList("la_vida_es_bella", "la vida es bella");
		addToList("super_mario", "super mario");
		addToList("the_island", "the island");
		addToList("X Files", "X Files");
		addToList("The Exorcist", "The Exorcist");
		addToList("Inspector gudget", "Inspector gudget");
		addToList("Gostbusters", "Gostbusters");
		addToList("James Bond", "James Bond");
		addToList("Mission Impossible", "Mission Impossible");
		addToList("Requiem for a Dream", "Requiem for a Dream");
		addToList("Godfather", "Godfather");
		addToList("beverly hills", "beverly hills");
		addToList("the good the bad and the ugly",
				"the good the bad and the ugly");
		addToList("Psycho", "Psycho");
		addToList("Rocky", "Rocky");
		addToList("Rambo", "Rambo");
		addToList("Last Of The Mohicans", "Last Of The Mohicans");
		addToList("Romeo and Juliet", "Romeo and Juliet");
		addToList("Mortal Kombat ", "Mortal Kombat ");
		addToList("matrix", "matrix");
		addToList("Eyes Wide Shut", "Eyes Wide Shut");
		/*
		 * addToList("", ""); addToList("", ""); addToList("", "");
		 * addToList("", ""); addToList("", ""); addToList("", "");
		 * addToList("", ""); addToList("", ""); addToList("", "");
		 * addToList("", ""); addToList("", ""); addToList("", "");
		 * addToList("", "");
		 */
	}

	/**
	 * 
	 * @param key
	 * @param title
	 */
	public static void addToList(String music, String title) {
		listOfMusic.add(music);
		listOfAnswere.add(title);
	}

	/**
	 * 
	 */
	private void switchPlayStop() {
		if (isPlayingAtTheTime) {
			playButton.setBackgroundResource(R.drawable.stop_button);
			stopMusic();

			playSound(EnigmaGameMenuActivity.getCurrentMusic());

			Toast.makeText(this, "find the film's soundtrack ",
					Toast.LENGTH_SHORT).show();
			// playSound(R.drawable);

		} else {
			playButton.setBackgroundResource(R.drawable.play_button);
			stopMusic();
		}
		isPlayingAtTheTime = !isPlayingAtTheTime;
	}

	private void stopMusic() {
		if (mPlayer != null) {
			// mPlayer.stop();
			playSound(-1);
		}
		if (player != null) {
			// mPlayer.stop();
			player.stop();
		}

	}

	/**
	 * 
	 * @param soundId
	 */
	public void playSound(int soundId) {
		try {
			// vibrate();
			if (mPlayer == null || !mPlayer.isPlaying() && soundId != -1) {
				mPlayer = MediaPlayer.create(this, soundId);
				mPlayer.start();
			} else {
				mPlayer.stop();
			}

		} catch (Exception e) {
			playSound(soundId);
		}
	}

	/**
	 * 
	 * @param title
	 */
	public void playSound(String title) {
		AssetFileDescriptor afd;
		if (title == null) {

			return;
		}
		try {
			afd = getAssets().openFd(title + ".mp3");

			player = new MediaPlayer();
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			player.prepare();
			player.start();

		} catch (Exception e) {
			playSound(title);
			// e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param folder
	 * @param title
	 */
	public void playSound(String folder, String title) {
		AssetFileDescriptor afd;
		try {
			afd = getAssets().openFd(folder + "/" + title + ".mp3");

			MediaPlayer player = new MediaPlayer();
			player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
					afd.getLength());
			player.prepare();
			player.start();

		} catch (IOException e) {
			playSound(folder, title + ".mp3");
			// e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	private void init() {
		nextButton = (Button) findViewById(R.id.next_button);
		showCurrentLevel = (TextView) findViewById(R.id.preview_stage);
		playButton = (Button) findViewById(R.id.play_sound_button);
		inputText = (EditText) findViewById(R.id.input_text);
		inputView = (TextView) findViewById(R.id.preview_stage);
		listOfMusic = new ArrayList();
		listOfAnswere = new ArrayList();
		database = new Database(this);
	}

	/**
	 * 
	 */
	private void nextEnigma() {
		stopMusic();
		startActivity(new Intent(EnigmaAct.this, EnigmaAct.class));
		EnigmaGameMenuActivity.currentStage++;
		int x = 1;
		SelectEnigmaButton curBut = EnigmaGameMenuActivity.listButtons
				.get(button.getLevel() + x);
		while (curBut.hasFound()) {
			EnigmaGameMenuActivity.currentStage++;
			x++;
			curBut = EnigmaGameMenuActivity.listButtons.get(button.getLevel()
					+ x);
		}

		setButton(EnigmaGameMenuActivity.listButtons.get(button.getLevel() + x));

	}

	private void goAtStart(){
		EnigmaGameMenuActivity.currentStage = 1;
		setButton(EnigmaGameMenuActivity.listButtons.get(0));
			nextStage();
			
	}
	
	private void nextEnigma2() {
		stopMusic();
		if (button.canNotUseNext()) {
			//if(SelectDifficultyButton.foundCorrect*(EnigmaGameMenuActivity.difficulty+1)<(EnigmaGameMenuActivity.difficulty+1)*1){
			if(!button.useNext2()){	
				goAtStart();
				return;
			}
			
		}

		EnigmaGameMenuActivity.currentStage++;
		nextStage();
	}
	
	private void nextEnigma3() {
		stopMusic();
			 goToNextButton(EnigmaGameMenuActivity.difficulty);
		
	}
	/**
	 * 
	 * @param df
	 */
	private void goToNextButton(int df){
		ArrayList <SelectEnigmaButton> array=null;
		if(df==0){
			array=EnigmaGameMenuActivity.listlevel_0;
		}else if(df==1){
			array=EnigmaGameMenuActivity.listlevel_1;
		}else if(df==2){
			array=EnigmaGameMenuActivity.listlevel_2;
		}else if(df==3){
			array=EnigmaGameMenuActivity.listlevel_3;
		}else if(df==4){
			array=EnigmaGameMenuActivity.listlevel_4;
		}
		for(int i=0;i<array.size();i++){
			if(array.get(i).getLevel()==button.getLevel()&&i<array.size()-1){
				setButton(array.get(i+1));
				break;
			}else if(i>=array.size()-1){
				setButton(array.get(0));
				i=0;
			}
		}
		EnigmaGameMenuActivity.currentStage=button.getLevel();
		startActivity(new Intent(EnigmaAct.this, EnigmaAct.class));
	}

	/**
 * 
 */
	private void nextStage() {
		int x = 1;

		while (SelectEnigmaButton.foundAnsweresId.contains(button.getLevel()+ x)) {
			x++;
			EnigmaGameMenuActivity.currentStage++;
			
		}
		setButton(EnigmaGameMenuActivity.listButtons.get(button.getLevel() + x));
		startActivity(new Intent(EnigmaAct.this, EnigmaAct.class));
	}

	/**
 * 
 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_enigma, menu);
		return true;
	}

	public static void setButton(SelectEnigmaButton btn) {
		button = btn;
	}

	public static SelectEnigmaButton getCurrentButton() {
		return button;
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();

		startActivity(new Intent(this, EnigmaGameMenuActivity.class));
		stopMusic();
		return;
	}
}
