package com.TyberianSoft.calcalculation;

import com.TyberianSoft.CalculationLogic.TimesTables;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;

@SuppressLint("NewApi")
public class TimesTable extends Activity {
private TimesTables timesTables;
private LinearLayout row1;
private LinearLayout row2;
private TextView [] textViews;
private TextView number1Text;
private TextView number2Text;
private LinearLayout rowResults;
private Button cleanButton;
private myDragEventListener dragListener;
private Handler handle;
private TextView scoreText;
private static final int TIMEPASS=1000;
private int score;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_times_table);
		// Show the Up button in the action bar.
		timesTables=new TimesTables();
		score=0;
		handle=new Handler();
	    dragListener=new myDragEventListener();
	    scoreText=(TextView)findViewById(R.id.score1);
	    rowResults=(LinearLayout)findViewById(R.id.ResultRow);
	    cleanButton=(Button)findViewById(R.id.CleanButton);
		row1=(LinearLayout)findViewById(R.id.Row1);
		number1Text=(TextView)findViewById(R.id.Number1Text);
		number2Text=(TextView)findViewById(R.id.Number2Text);
		row2=(LinearLayout)findViewById(R.id.Row2);
		rowResults.setOnDragListener(dragListener);
	    cleanButton.setOnClickListener(cleanButtonListener);
	    
	    MessageDialog();
		initilizeGame();
		initilizeButtons();
		
	}
	public void initilizeButtons(){
		textViews=new TextView[10];
		for(int i=0;i<textViews.length;i++){
			
				textViews[i]=new TextView(TimesTable.this);
				textViews[i].setText(""+i);
				textViews[i].setBackgroundResource(R.drawable.numberlabel_border);
				textViews[i].setTextAppearance(TimesTable.this, R.style.NumberType);
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				params.setMargins(27, 0, 0, 0);
				textViews[i].setOnTouchListener(labelTouchListener);
				if(i<5){
					row1.addView(textViews[i],i,params);
					
				}else{
					row2.addView(textViews[i],i-5,params);
				}
		}
	}
	private void MessageDialog(){
		AlertDialog.Builder builder=new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle(R.string.Information_Message);
		builder.setMessage(R.string.TimeTableMessage);
		builder.setPositiveButton(R.string.Ok, null);
		AlertDialog errorDialog=builder.create();
		errorDialog.show();
	}
	private void initilizeGame(){
		rowResults.removeAllViews();
		timesTables.GenerateNumber(score);
		number1Text.setText(""+timesTables.getNumber1());
		number2Text.setText(""+timesTables.getNumber2());
		scoreText.setText("Score:"+score);
	}
	private OnClickListener cleanButtonListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			rowResults.removeAllViews();
			
		}
	};
	public void onBackPressed() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle(R.string.Exit);
		builder.setMessage(R.string.Sure);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		builder.setNegativeButton(R.string.No,null);
		AlertDialog errorDialog=builder.create();
		errorDialog.show();
	};
	private boolean isResultTrue(){
		int value=0;
		int digit=1;
		for(int i=rowResults.getChildCount()-1;i>=0;i--){
			TextView num=(TextView)rowResults.getChildAt(i);
			value=value+(digit*Integer.parseInt(num.getText().toString()));
			digit*=10;
		}
		Log.e("Value", value+"");
		if(timesTables.isResultTrue(value))return true;
		return false;
	}
	public void NewLoad(){
		if(isResultTrue()){
			score++;
			  Toast toast=new Toast(TimesTable.this);
			  toast.setDuration(Toast.LENGTH_SHORT);
			  LayoutInflater inflater = getLayoutInflater();
			  View view=inflater.inflate(R.layout.toastlayout, (ViewGroup)findViewById(R.id.toast_layout_root));
			  toast.setView(view);
			  toast.show();
			  handle.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					initilizeGame();	
				}
			}, TIMEPASS);
		}
		
	}
	@SuppressLint("ClickableViewAccessibility")
	private OnTouchListener labelTouchListener =new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			String [] mineTypes={ClipDescription.MIMETYPE_TEXT_PLAIN};
			ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
			ClipData dragData = new ClipData((CharSequence)v.getTag(),mineTypes, item);
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(dragData, shadowBuilder, v, 0);
			return false;
		}
	};
	@SuppressLint("NewApi")
	private class myDragEventListener implements View.OnDragListener{

		@SuppressLint("NewApi")
		@Override
		public boolean onDrag(View v, DragEvent event) {
			final int action=event.getAction();
			switch (action) {
			case DragEvent.ACTION_DRAG_STARTED:
				TextView SelectedView=(TextView)event.getLocalState();
				TextView tmp=null;
				for(int i=0;i<rowResults.getChildCount();i++){
					tmp=(TextView)rowResults.getChildAt(i);
					if(tmp==SelectedView){
						rowResults.removeView(SelectedView);
						break;
					}
				}
				return true;
            case DragEvent.ACTION_DRAG_ENTERED:
				
				return true;
            case DragEvent.ACTION_DRAG_LOCATION:
				
				return true;
            case DragEvent.ACTION_DRAG_EXITED:
				return true;
            case DragEvent.ACTION_DROP:
            	if(v==rowResults){
            		TextView view=(TextView)event.getLocalState();
            		int x=(int)event.getX();
            		int count=rowResults.getChildCount();
            		TextView newView=new TextView(TimesTable.this);
            		newView.setText(""+view.getText());
            		newView.setBackgroundResource(R.drawable.numberlabel_border);
            		newView.setTextAppearance(TimesTable.this, R.style.NumberType);
            		newView.setOnTouchListener(labelTouchListener);
    				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    				params.setMargins(0, 0, 20, 0);
    				if(count<6){
    					if(count==0){
    						rowResults.addView(newView, params);
    					}
    					else if(count==1){
    						TextView text1=(TextView)rowResults.getChildAt(0);
    						Log.e("x Location", x+"");
    						Log.e("Old text", (int)text1.getX()+"");
    						if((int)text1.getX()<x){
    							rowResults.addView(newView,1,params);
    						}else{
    							rowResults.addView(newView,0,params);
    						}
    					}else{
    						boolean isAdded=false;
    						for(int i=0;i<count-1;i++){
    							TextView text1=(TextView)rowResults.getChildAt(i);
    							TextView text2=(TextView)rowResults.getChildAt(i+1);
    							if(isAdded)break;
    							if(text1.getX()<x&&text2.getX()>x){
    								rowResults.addView(newView,i+1,params);
    								isAdded=true;
    							}else if(text1.getX()>x){
    								rowResults.addView(newView,i,params);
    								isAdded=true;
    							}
    						}
    						if(!isAdded){
    							rowResults.addView(newView,params);
    						}
    					}
    				}else{
    					Toast toast=new Toast(TimesTable.this);
    					  toast.setDuration(Toast.LENGTH_SHORT);
    					  LayoutInflater inflater = getLayoutInflater();
    					  View toastView=inflater.inflate(R.layout.toastlayout2, (ViewGroup)findViewById(R.id.toast_layout_root2));
    					  toast.setView(toastView);
    					  toast.show();
    				}
            	}
            	v.invalidate();
            	NewLoad();
				return true;

			}
			return false;
		}
		
	}


}
