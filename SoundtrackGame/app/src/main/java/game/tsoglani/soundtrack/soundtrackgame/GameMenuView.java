package game.tsoglani.soundtrack.soundtrackgame;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class GameMenuView extends ViewGroup{

	public GameMenuView(Context context) {
		super(context);
		this.setVisibility(VISIBLE);
		this.setWillNotDraw(false);
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount= this.getChildCount();
		boolean startOnLineBegine=true;
		
		
		for(int i=0;i<childCount;i++){
			View view=getChildAt(i);
			SelectEnigmaButton b1=null;
			if(view instanceof SelectEnigmaButton){
				 b1= (SelectEnigmaButton) view;
				// Log.e(Integer.toString(childCount),"GameMenuView");
			}
			
			if(startOnLineBegine){
			//	Log.e(Integer.toString(childCount),"startOnLineBegine");
				b1.layout(0, i*getHeight()/10, getWidth()/2-20, i*getHeight()/10+getHeight()/11);
				
			}else{
				b1.layout(getWidth()/2, i*getHeight()/10, getWidth(), i*getHeight()/10+getHeight()/11);
			}
			//b1.layout(0, 10, 300, 300);
			
				startOnLineBegine=!startOnLineBegine;
		}
		
	}

}
