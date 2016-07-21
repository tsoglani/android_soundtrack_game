package game.tsoglani.soundtrack.soundtrackgame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import android.R.color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
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
	private Button hintButton;
	private TextView hintText;
	public static final int startingHints = 10;
	private boolean isLevelComplete = false;
	public static int hints = startingHints;
	public static int correctMustHaveInEachDifficulty = 17;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enigma);
		init();
		database.updateHints();

		hintText.setText(Integer.toString(hints));
		inputText.setTextColor(this.getResources().getColor(
				R.color.transparent_black_percent_10));
		inputText.setText(startingInputTextString);

		showCurrentLevel.setText("movie No "
				+ Integer.toString(EnigmaGameMenuActivity.currentStage + 1));
		// showCurrentLevel.setBackgroundColor(R.color.BlueViolet);
		playButton.setBackgroundResource(R.drawable.play_button);
		// nextButton.setEnabled(true);
		nextButton.setBackgroundResource(R.drawable.next);

		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hintText.setText(Integer.toString(hints));
				if (!EnigmaAct.this.getButtonsList().isEmpty()) {

					nextEnigma3();
				} else {
					nextOnCompleteEnigma();
				}
			}
		});

		inputText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (button.hasFound()) {
					inputText.setEnabled(false);
					inputText.setTextColor(Color.BLACK);
					inputText.setBackgroundColor(Color.CYAN);
					inputText.setText(button.getAnswere());
					return;
				}

				if (inputText.getText().toString()
						.equals(startingInputTextString)) {
					inputText.setTextColor(Color.BLACK);
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
						
						if (database.getCorrectCount() % 5 == 0) {
							hints++;
							Toast.makeText(EnigmaAct.this,
									"Congratulations you earn one Hint",
									Toast.LENGTH_LONG).show();
							hintText.setText(Integer.toString(hints));
							if (isLevelComplete) {
								EnigmaAct.this.runOnUiThread(new Runnable() {
									public void run() {
										try {
											Thread.sleep(1000);
										} catch (InterruptedException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
										EnigmaAct.this
												.startActivity(new Intent(
														EnigmaAct.this,
														EnigmaGameMenuActivity.class));
									}
								});
								isLevelComplete = false;
							}

						}
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
		if ((button != null && button.hasFound() || button != null
				&& SelectEnigmaButton.foundAnsweresId.contains(button
						.getLevel()))
				&& !getButtonsList().isEmpty()) {// itan xwris to
			// &&!getButtonsList().isEmpty()
			foundItFunction();
			// Toast.makeText(EnigmaAct.this,
			// "You have found it",Toast.LENGTH_LONG).show();
		} else if (getButtonsList().isEmpty()) {//
			foundItFunctionOnLevelComplete();// nea function
		}//
		hintButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (button.hasFound()) {
					Toast.makeText(EnigmaAct.this, "You have already found it",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (hints - 3 < 0) {
					Toast.makeText(EnigmaAct.this, "You have not enough hints",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (database.getCorrectCount() % 5 == 0) {
					Toast.makeText(EnigmaAct.this,
							"Congratulations you earn one Hint",
							Toast.LENGTH_SHORT).show();
					hintText.setText(Integer.toString(hints));
					hints++;
				}
				hints -= 3;
				// button.setHasFound(true);
				foundItFunction();
				hintText.setText(Integer.toString(hints));
			}

		});

		ArrayList<SelectEnigmaButton> array = getButtonsList();

	}

	private void foundItFunctionOnLevelComplete() {
		inputText.setBackgroundColor(Color.YELLOW);
		inputText.setTextColor(Color.BLACK);
		inputText.setEnabled(false);
		inputText.setText(button.getAnswere());
	}

	private void foundItFunction() {
		inputText.setBackgroundColor(Color.YELLOW);
		inputText.setTextColor(Color.BLACK);
		inputText.setEnabled(false);
		inputText.setText(button.getAnswere());
		
		hideSoftKeyboard(EnigmaAct.this);
		// EnigmaGameMenuActivity.listlevel_0.remove(button);
		// EnigmaGameMenuActivity.listlevel_1.remove(button);
		// EnigmaGameMenuActivity.listlevel_2.remove(button);
		// EnigmaGameMenuActivity.listlevel_3.remove(button);
		// EnigmaGameMenuActivity.listlevel_4.remove(button);
		database.addContact(button.getLevel(), hints);

		ArrayList<SelectEnigmaButton> array = getButtonsList();

		array.remove(button);
		if (array.isEmpty()) {
			// nextButton.setEnabled(false);
			hints += 10;
			Toast.makeText(EnigmaAct.this,
					"You complete the level , you earn 10 hints",
					Toast.LENGTH_SHORT);
			try {
				playSound("clp");
			} catch (Exception e) {
				e.printStackTrace();
			}
			isLevelComplete = true;
			Toast.makeText(EnigmaAct.this,
					"Congratulations you complete the level ",
					Toast.LENGTH_LONG);

		}
	}

	private ArrayList<SelectEnigmaButton> getButtonsList() {
		ArrayList<SelectEnigmaButton> array = null;
		if (EnigmaGameMenuActivity.difficulty == 0) {
			array = EnigmaGameMenuActivity.listlevel_0;
		} else if (EnigmaGameMenuActivity.difficulty == 1) {
			array = EnigmaGameMenuActivity.listlevel_1;
		} else if (EnigmaGameMenuActivity.difficulty == 2) {
			array = EnigmaGameMenuActivity.listlevel_2;
		} else if (EnigmaGameMenuActivity.difficulty == 3) {
			array = EnigmaGameMenuActivity.listlevel_3;
		} else if (EnigmaGameMenuActivity.difficulty == 4) {
			array = EnigmaGameMenuActivity.listlevel_4;
		}

		return array;
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
		text = text.toLowerCase();// ////////
		ArrayList<Character> listInput = new ArrayList<Character>();
		ArrayList<Character> listAnswere = new ArrayList<Character>();
		for (int i = 0; i < text.length(); i++) {
			listInput.add((char) text.indexOf(i));
		}

		String answere = button.getAnswere();
		answere = answere.toLowerCase();// //////
		for (int i = 0; i < answere.length(); i++) {
			listAnswere.add((char) answere.indexOf(i));
		}

		int differenceBetweenAnswereAndInput = listAnswere.size()
				- listInput.size();
		if (differenceBetweenAnswereAndInput >= 4
				|| differenceBetweenAnswereAndInput <= -4) {
			return false;
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
		// ///prwti lista
		addToList("X Files", "X-Files");
		addToList("Gostbusters", "Gostbusters");
		addToList("The Exorcist", "The Exorcist");
		addToList("Godfather", "The Godfather");
		addToList("Requiem for a Dream", "Requiem for a Dream");
		addToList("happy_days", "Happy Days");
		addToList("Inspector gudget", "Inspector gudget");
		addToList("Teenage Mutant Ninja Turtles",
				"Teenage Mutant Ninja Turtles");
		addToList("super_mario", "Super Mario");
		addToList("James Bond", "James Bond");
		addToList("Mission Impossible", "Mission Impossible");

		addToList("beverly hills", "Beverly Hills");
		addToList("benny hill", "Benny Hill");
		addToList("amelie", "Amelie");
		addToList("la_vida_es_bella", "La vida es bella");
		addToList("the_island", "The Beach");
		addToList("Batman", "Batman");
		addToList("donnie_darko", "Donnie Darko");
		addToList("knight rider", "Knight Rider");
		addToList("The Pink Panther", "The Pink Panther");

		// //// prwti lista
		// /// deyteri lista
		addToList("Toy Story", "Toy Story");
		addToList("Lion King", "Lion King");
		addToList("DuckTales", "DuckTales");
		addToList("Spiderman ", "Spiderman ");
		addToList("Aladdin", "Aladdin");
		addToList("Asterix Obelix", "Asterix Obelix");
		addToList("The Smurfs", "The Smurfs");
		addToList("baywatch", "Baywatch");
		addToList("Titanic", "Titanic");
		addToList("Pulp Fiction", "Pulp Fiction");
		addToList("X-MEN", "X-MEN");
		addToList("Rocky", "Rocky");
		addToList("8 miles", "8 Miles");
		addToList("Braveheart", "Braveheart");
		addToList("The Lord of the Rings", "The Lord of the Rings");
		addToList("the good the bad and the ugly",
				"The good the bad and the ugly");
		addToList("Psycho", "Psycho");
		addToList("Fight Club", "Fight Club");
		addToList("Harry Potter", "Harry Potter");
		addToList("Loser", "Loser");

		// //// defteri lista
		// //// triti lista
		addToList("A Night at the Roxbury", "A Night at the Roxbury");
		addToList("Pretty Woman", "Pretty Woman");
		addToList("Grease", "Grease");
		addToList("Top Gun", "Top Gun");
		addToList("Armagedon", "Armagedon");
		addToList("Zorro", "Zorro");
		addToList("Last Of The Mohicans", "Last Of The Mohicans");
		addToList("matrix", "Matrix");
		addToList("Pirates Of Caribbean", "Pirates Of Caribbean");
		addToList("2001 a space odyssey", "2001 a space odyssey");
		addToList("Rambo", "Rambo");
		addToList("The Phantom Of The Opera", "The Phantom Of The Opera");
		addToList("The Simpsons", "The Simpsons");
		addToList("Indiana Johnes", "Indiana Johnes");
		addToList("The Addams Family", "The Addams Family");
		addToList("Bewitched", "Bewitched");
		addToList("Mortal Kombat", "Mortal Kombat");
		addToList("dangerous minds", "Dangerous minds");
		addToList("Footloose", "Footloose");
		addToList("Eurotrip", "Eurotrip");
		// // trith lista
		// // tetarti lista
		
		addToList("Scent of A Woman", "Scent of A Woman");
		addToList("Sherlock Holmes", "Sherlock Holmes");
		addToList("Flashdance", "Flashdance");
		addToList("The Killing Fields", "The Killing Fields");
		addToList("A NIGHT IN HEAVEN", "A night in heaven");
		addToList("The Bourne Identity", "The Bourne Identity");
		addToList("Eyes Wide Shut", "Eyes Wide Shut");
		addToList("Romeo and Juliet", "Romeo and Juliet");
		addToList("Twisted nerve", "Twisted nerve");
		addToList("star wars", "Star wars");
		addToList("being_john_malkovich", "Being john malkovich");
		addToList("Up", "Up");
		addToList("Gladiator", "Gladiator");
		addToList("The Bridge on the River Kwai",
				"The Bridge on the River Kwai");
		addToList("The Great Escape", "The Great Escape");
		addToList("Hawaii 5-O", "Hawaii 5-O");
		addToList("Layer Cake", "Layer Cake");
		addToList("Robocop ", "Robocop ");
		addToList("The mission", "The mission");
		addToList("North by Northwest", "North by Northwest");

		// /// tetarti lista
		// /// pempti lista
		addToList("Jackass", "Jackass");
		addToList("Austin Power", "Austin Power");
		addToList("Kill Bill", "Kill Bill");
		addToList("300", "300");
		addToList("American Beauty", "American Beauty");
		addToList("Platoon", "Platoon");
		addToList("Beverly Hills Cop", "Beverly Hills Cop");
		addToList("Apocalypse Now", "Apocalypse Now");
		addToList("Fountain", "Fountain");
		addToList("Halloween", "Halloween");
		addToList("Superman", "Superman");
		addToList("Forrest Gump", "Forrest Gump");
		addToList("The Piano", "The Piano");
		addToList("Ben Hur", "Ben Hur");
		addToList("A Summer Place", "A Summer Place");
		addToList("The Magnificent Seven", "The Magnificent Seven");
		addToList("CHARIOTS of FIRE", "Chariots of Fire");
		addToList("Alfred Hitchcock Presents", "Alfred Hitchcock Presents");
		addToList("Edward Scissorhand", "Edward Scissorhand");
		addToList("Lawrence of Arabia", "Lawrence of Arabia");
		// /// pempti lista

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
		try {
			if (mPlayer != null) {
				// mPlayer.stop();
				playSound(-1);
			}
			if (player != null) {
				// mPlayer.stop();

				player.stop();

			}
		} catch (Exception e) {

		}

	}
	public static void hideSoftKeyboard(Activity activity) {
		try{
	    InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
	}catch(Exception e){
		// no needed but want to be sure 
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
		hintButton = (Button) findViewById(R.id.hint_button);
		hintText = (TextView) findViewById(R.id.hinttext);
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

	private void goAtStart() {
		EnigmaGameMenuActivity.currentStage = 1;
		setButton(EnigmaGameMenuActivity.listButtons.get(0));
		nextStage();

	}

	private void nextEnigma2() {
		stopMusic();
		if (button.canNotUseNext()) {
			// if(SelectDifficultyButton.foundCorrect*(EnigmaGameMenuActivity.difficulty+1)<(EnigmaGameMenuActivity.difficulty+1)*1){
			if (!button.useNext2()) {
				goAtStart();
				return;
			}

		}

		EnigmaGameMenuActivity.currentStage++;
		nextStage();
	}

	private void nextOnCompleteEnigma() {
		stopMusic();

		EnigmaGameMenuActivity.generateCurrentCompleteButton();
		SelectEnigmaButton newButton = getNextButtonWhenIsFound(
				button.getLevel(), EnigmaGameMenuActivity.currentCompleteLevel);
		if (newButton == null) {
			newButton = getNextButtonWhenIsFound(-1,
					EnigmaGameMenuActivity.currentCompleteLevel);
		}
		setButton(newButton);
		EnigmaGameMenuActivity.currentStage = button.getLevel();
		startActivity(new Intent(EnigmaAct.this, EnigmaAct.class));
	}

	private void nextEnigma3() {
		stopMusic();
		goToNextButton(EnigmaGameMenuActivity.difficulty);

	}

	/**
	 * 
	 * @param df
	 */
	private void goToNextButton(int df) {
		ArrayList<SelectEnigmaButton> array = null;
		if (df == 0) {
			array = EnigmaGameMenuActivity.listlevel_0;
		} else if (df == 1) {
			array = EnigmaGameMenuActivity.listlevel_1;
		} else if (df == 2) {
			array = EnigmaGameMenuActivity.listlevel_2;
		} else if (df == 3) {
			array = EnigmaGameMenuActivity.listlevel_3;
		} else if (df == 4) {
			array = EnigmaGameMenuActivity.listlevel_4;
		}

		SelectEnigmaButton newButton = getNextButtonWhenIsFound(
				button.getLevel(), array);
		if (newButton == null) {
			newButton = getNextButtonWhenIsFound(-1, array);
		}

		setButton(newButton);
		/*
		 * for(int i=0;i<array.size();i++){
		 * 
		 * if(array.get(i).getLevel()==button.getLevel()){
		 * if((i+1)>array.size()){
		 * 
		 * } setButton(array.get(i+1)); break; } }
		 */
		EnigmaGameMenuActivity.currentStage = button.getLevel();
		startActivity(new Intent(EnigmaAct.this, EnigmaAct.class));
	}

	public SelectEnigmaButton getNextButtonWhenIsFound(int current_id,
			ArrayList<SelectEnigmaButton> array) {
		int returning_id = -1;
		for (int i = 0; i < array.size(); i++) {

			if (array.get(i).getLevel() > current_id) {
				returning_id = i;
				break;
			}
		}
		if (returning_id == -1) {
			return null;
		}
		return array.get(returning_id);

	}

	/**
 * 
 */
	private void nextStage() {
		int x = 1;

		while (SelectEnigmaButton.foundAnsweresId.contains(button.getLevel()
				+ x)) {
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

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_HOME) {

			// gotoHomeScreen();
			stopMusic();
			// return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {

		try {
			player.release();
		} catch (Exception e) {

		} finally {
			super.onDestroy();
		}

	}

	private void gotoHomeScreen() {

		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMain);
	}

}
