package com.TyberianSoft.calcalculation;

import com.TyberianSoft.CalculationLogic.DatabaseConnection;
import com.TyberianSoft.CalculationLogic.FileOperations;
import com.TyberianSoft.CalculationLogic.MathematicalOperations;
import com.TyberianSoft.CalculationLogic.Scores;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputFilter;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.DialogInterface;
@SuppressLint("NewApi")
public class MathematicalOperationsGui extends Activity {
	private MathematicalOperations mathOp;
	private LinearLayout row1;
	private LinearLayout row2;
	private TextView [] textViews;
	private TextView number1Text;
	private TextView number2Text;
	private LinearLayout rowResults;
	private Button cleanButton;
	private myDragEventListener dragListener;
	private TextView scoreText;
	private TextView operationText;
	private long TimeRemaining;
	private int score;
	private TextView remainingText;
	private FileOperations op;
	private int lowestScore;
	private MyOwnTimer timer;
	private  int lowestLimit;
	private  int mediumLimit;
	private  int highLimit;
	private Handler handle;
	private static final int decLimit=1000;
	private static final int TIMEPASS=1000;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mathematical_operations_gui);
		// Show the Up button in the action bar.
		mathOp=new MathematicalOperations();
		score=0;
		handle=new Handler();
		op=new FileOperations();
	    try {
			op.ReadFile(openFileInput(FileOperations.FILENAME));
		} catch (Exception e) {
			lowestScore=0;
			e.printStackTrace();
		}
	    lowestLimit=5000;
	    mediumLimit=10000;
	    highLimit=15000;
	    lowestScore=op.getLowestScore();
	    dragListener=new myDragEventListener();
	    remainingText=(TextView)findViewById(R.id.TimeRemainingMath);
	    scoreText=(TextView)findViewById(R.id.score1Math);
	    operationText=(TextView)findViewById(R.id.OperatorTextMath);
	    rowResults=(LinearLayout)findViewById(R.id.ResultRowMath);
	    cleanButton=(Button)findViewById(R.id.CleanButtonMath);
		row1=(LinearLayout)findViewById(R.id.Row1Math);
		number1Text=(TextView)findViewById(R.id.Number1TextMath);
		number2Text=(TextView)findViewById(R.id.Number2TextMath);
		row2=(LinearLayout)findViewById(R.id.Row2Math);
		rowResults.setOnDragListener(dragListener);
	    cleanButton.setOnClickListener(cleanButtonListener);
	    TimeRemaining=10000;
	    remainingText.setText("Time: "+TimeRemaining/1000); 
		initilizeGame();
		initilizeButtons();
		MessageDialog();	
	}
	@Override
	protected void onPause() {
		
		super.onPause();	
	}
	@Override
	protected void onDestroy() {
		
		super.onDestroy();		
	}
	@Override
	protected void onResume() {
		
		super.onResume();		
	}
	private void GameOverDialog(){
		AlertDialog.Builder builder=new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
		builder.setMessage(R.string.GameOver);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(timer!=null){
					timer.cancel();
				}
				finish();
			}
		});
		AlertDialog errorDialog=builder.create();
		errorDialog.show();
	}
	@SuppressLint("WorldReadableFiles")
	public void SaveScore(){
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle(R.string.ScoreBoard);
		alert.setMessage(R.string.Enter_Your_Name);
		
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		InputFilter[] filter=new InputFilter[1];
		filter[0]=new InputFilter.LengthFilter(10);
		input.setFilters(filter);
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
		  String value = input.getText().toString();
		  Scores newScoreSave=new Scores(score, value);
		  new SaveScore().execute(newScoreSave);
		  try {
			op.WriteFile(newScoreSave,openFileOutput(FileOperations.FILENAME, Context.MODE_PRIVATE));
		} catch (Exception e) {
			remainingText.setText(getResources().getString(R.string.time_remaining_format,TimeRemaining));
			e.printStackTrace();
		}finally{
			if(timer!=null){
				timer.cancel();
			}
			finish();
		}  
		  }
		});
		alert.show();
	}
	private boolean isResultTrue(int value){
		
		Log.e("Value", value+"");
		if(mathOp.isResultTrue(value))return true;
		return false;
	}
	public void NewLoad(){
		int value=0;
		int digit=1;
		for(int i=rowResults.getChildCount()-1;i>=0;i--){
			TextView num=(TextView)rowResults.getChildAt(i);
			value=value+(digit*Integer.parseInt(num.getText().toString()));
			digit*=10;
		}
		if(isResultTrue(value)){
			Toast toast=new Toast(MathematicalOperationsGui.this);
			  toast.setDuration(Toast.LENGTH_SHORT);
			  LayoutInflater inflater = getLayoutInflater();
			  View view=inflater.inflate(R.layout.toastlayout, (ViewGroup)findViewById(R.id.toast_layout_root));
			  toast.setView(view);
			  toast.show();
			  if(value<10){
				  score+=5;
				  TimeRemaining=TimeRemaining+lowestLimit;
				  if(lowestLimit!=0){
					  lowestLimit-=highLimit;
				  }
				  if(timer!=null){
					  timer.cancel();	  
				  }
				  startTimer();
			  }else if(value<100){
				  score+=10;
				  TimeRemaining=TimeRemaining+mediumLimit;
				  if(mediumLimit!=0){
					  mediumLimit-=decLimit;
				  }
				  if(timer!=null){
					  timer.cancel();	  
				  }
				  startTimer();
			  }else{
				  score+=15;
				 TimeRemaining=TimeRemaining+highLimit;
				 if(highLimit!=0){
					 highLimit-=decLimit;
					 
				 }
				 if(timer!=null){
					  timer.cancel();	  
				  }
				  startTimer();
			  }
			  handle.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					initilizeGame();	
				}
			},TIMEPASS);
		}
		
	}
	public void initilizeButtons(){
		textViews=new TextView[10];
		for(int i=0;i<textViews.length;i++){
			
				textViews[i]=new TextView(this);
				textViews[i].setText(""+i);
				textViews[i].setBackgroundResource(R.drawable.numberlabel_border);
				textViews[i].setTextAppearance(this, R.style.NumberType);
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
		builder.setMessage(R.string.mathmeatical_Message);
		builder.setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				startTimer();
			}
		});
		AlertDialog errorDialog=builder.create();
		errorDialog.show();
	}
	public void onBackPressed() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle(R.string.Exit);
		builder.setMessage(R.string.Sure);
		builder.setCancelable(false);
		builder.setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(timer!=null)
					timer.cancel();
				finish();
			}
		});
		builder.setNegativeButton(R.string.No,null);
		AlertDialog errorDialog=builder.create();
		errorDialog.show();
	};
	private void initilizeGame(){
		rowResults.removeAllViews();
		mathOp.GenerateOperation(score);
		number1Text.setText(""+mathOp.getNum1());
		number2Text.setText(""+mathOp.getNum2());
		operationText.setText(""+mathOp.getOperation());
		scoreText.setText("Score:"+score);
	}
	private OnClickListener cleanButtonListener=new OnClickListener() {
		@Override
		public void onClick(View v) {
			rowResults.removeAllViews();		
		}
	};

	@SuppressLint("ClickableViewAccessibility")
	private OnTouchListener labelTouchListener=new OnTouchListener() {
		
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
            		TextView newView=new TextView(MathematicalOperationsGui.this);
            		newView.setText(""+view.getText());
            		newView.setBackgroundResource(R.drawable.numberlabel_border);
            		newView.setTextAppearance(MathematicalOperationsGui.this, R.style.NumberType);
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
    					Toast toast=new Toast(MathematicalOperationsGui.this);
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
	private void startTimer(){
		timer=new MyOwnTimer(TimeRemaining, 1000);
		timer.start();
	}
	private class MyOwnTimer extends CountDownTimer{
		
		public MyOwnTimer(long startTime, long interval)
        {
            super(startTime, interval);
        }
		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			remainingText.setText("Time: "+0); 
			if(score>lowestScore){
				SaveScore();
			}else{
				GameOverDialog();
			}
		}
		@Override
		public void onTick(long millisUntilFinished) {
			 TimeRemaining=millisUntilFinished;
			 remainingText.setText("Time: "+TimeRemaining/1000);
		}
		
	}
	public class SaveScore extends AsyncTask<Scores, Scores, Void>{
		private DatabaseConnection connection;
        @Override
        protected void onPreExecute() {
        	// TODO Auto-generated method stub
        	super.onPreExecute();
        	connection=new DatabaseConnection();
        }
		@Override
		protected Void doInBackground(Scores... params) {
			connection.insertScore(params[0]);
			return null;
		}		
	}
}
