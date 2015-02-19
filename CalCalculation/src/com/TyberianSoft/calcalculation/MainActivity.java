package com.TyberianSoft.calcalculation;
import java.util.ArrayList;
import java.util.List;
import com.TyberianSoft.CalculationLogic.DatabaseConnection;
import com.TyberianSoft.CalculationLogic.FileOperations;
import com.TyberianSoft.CalculationLogic.Scores;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
private Button timesTableButton;
private Button mathematicalOperationButton;
private Button scoreBoardButton;
private Button globalScoreboardButton;
private Button BackScoreBoardButton;
private ListView scoreboard;
private ProgressDialog bar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BackToMainPage();
	}
	private OnClickListener globalScoreboardButtonListener =new OnClickListener() {
		@Override
		public void onClick(View v) {
			GoToGlobalScoreboard();
		}
	};
	private OnClickListener timeTableButtonListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startActivity(new Intent(MainActivity.this,TimesTable.class));
			
		}
	};
    private OnClickListener mathematicalOperationsButtonListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			startActivity(new Intent(MainActivity.this,MathematicalOperationsGui.class));
		}
	};
    private OnClickListener scoreBoardButtonListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			GoToScoreBoard();
		}
	};
     private OnClickListener backButtonListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			BackToMainPage();
		}
	};
	@SuppressLint("InlinedApi")
	private void GoToGlobalScoreboard(){
		setContentView(R.layout.scoreboard);
		BackScoreBoardButton=(Button)findViewById(R.id.BackButton2);
		BackScoreBoardButton.setOnClickListener(backButtonListener);
		scoreboard=(ListView)findViewById(R.id.listView);
		bar=new ProgressDialog(MainActivity.this);
		bar.setTitle("Please Wait...");
		bar.setMessage("Connecting to Server");
		bar.setProgress(ProgressDialog.THEME_DEVICE_DEFAULT_LIGHT);
		bar.setCancelable(false);
		bar.show();
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				new ConnectionToNetWork().execute();
			}
			
		});
		thread.start();
		
		
	}
	private class ConnectionToNetWork extends AsyncTask<Object, Object, List<Scores>>{
		private DatabaseConnection connection;
        @Override
        protected void onPreExecute() {
        	// TODO Auto-generated method stub
        	super.onPreExecute();
        	connection=new DatabaseConnection();
        }
		@Override
		protected List<Scores> doInBackground(Object... params) {
			
			return connection.getWorldScores();
		}
		@Override
		protected void onPostExecute(List<Scores> result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(result==null)result=new ArrayList<Scores>();
			ArrayScoreBoardList adapter=new  ArrayScoreBoardList(MainActivity.this, result);
			scoreboard.setAdapter(adapter);
			if(bar!=null)bar.dismiss();
		}
	}
	private void BackToMainPage(){
		setContentView(R.layout.activity_main);
		timesTableButton=(Button)findViewById(R.id.TimesTableButton);
		mathematicalOperationButton=(Button)findViewById(R.id.MathematicalOperationButton);
		scoreBoardButton=(Button)findViewById(R.id.ScoreBoardButton);
		globalScoreboardButton=(Button)findViewById(R.id.GlobalScoreBoardButton);
		globalScoreboardButton.setOnClickListener(globalScoreboardButtonListener);
		mathematicalOperationButton.setOnClickListener(mathematicalOperationsButtonListener);
		timesTableButton.setOnClickListener(timeTableButtonListener);
		scoreBoardButton.setOnClickListener(scoreBoardButtonListener);
	}
	
	private void GoToScoreBoard(){
		setContentView(R.layout.scoreboard);
		BackScoreBoardButton=(Button)findViewById(R.id.BackButton2);
		BackScoreBoardButton.setOnClickListener(backButtonListener);
		scoreboard=(ListView)findViewById(R.id.listView);
		 FileOperations op=new FileOperations();
		try {
			op.ReadFile(openFileInput(FileOperations.FILENAME));
			ArrayScoreBoardList adapter=new ArrayScoreBoardList(this, op.getScores());
			scoreboard.setAdapter(adapter);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		
	}private class ArrayScoreBoardList extends  ArrayAdapter<Scores>{
		
		private List<Scores> list;
		private Context context;
	

		public ArrayScoreBoardList(Context context,
				List<Scores> objects) {
			
			super(context, R.layout.listlayout, objects);
			this.list=objects;
            this.context=context;
		}@Override
		public boolean areAllItemsEnabled() {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean isEnabled(int position) {
			// TODO Auto-generated method stub
			return false;
		}



		
		
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
		  if(convertView==null){
			  LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		        convertView = inflater.inflate(R.layout.listlayout, parent, false);
		  }
		  TextView text=(TextView)convertView.findViewById(R.id.ScoresText);
		  Scores s=list.get(position);
		  text.setText((position+1)+"-"+s.getName()+" "+s.getScore());
		  return convertView;
		}
		
	}
	
    

}
