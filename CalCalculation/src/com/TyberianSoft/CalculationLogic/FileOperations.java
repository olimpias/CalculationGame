package com.TyberianSoft.CalculationLogic;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
public class FileOperations {
private ObjectOutputStream outputStream;
private ObjectInputStream inputStream;
private int LowestScore;
private CollectionOfScores scores;
public static final String FILENAME="scores.txt";

public FileOperations() {
}
public void ReadFile(FileInputStream input) throws Exception{
	inputStream=new ObjectInputStream(input);
	scores = (CollectionOfScores)inputStream.readObject();
	Collections.sort(scores.getScores(), new ScoreCompare());
	inputStream.close();
	input.close();
	if(scores.getScores().size()==0){
		LowestScore=0;
	}else{
		int size=scores.getScores().size();
		if(size!=8){
			LowestScore=0;
		}else{
			Scores o=scores.getScores().get(size-1);
			LowestScore=o.getScore();
		}
		
	}
}
public ArrayList<Scores> getScores() {
	return scores.getScores();
}
public void WriteFile(Scores score,FileOutputStream output) throws Exception{
	AddingScore(score);
	outputStream=new ObjectOutputStream(output);
	outputStream.writeObject(scores);
	outputStream.close();output.close();
}
public int getLowestScore() {
	return LowestScore;
}
private void AddingScore(Scores score){
	if(scores==null)
	scores=new CollectionOfScores();
	scores.getScores().add(score);
	
	Collections.sort(scores.getScores(), new ScoreCompare());
	ArrayList<Scores> newScores=new ArrayList<Scores>();
	for(int i=0;i<scores.getScores().size()&&i<8;i++){
		if(scores.getScores().get(i)!=null){
			newScores.add(scores.getScores().get(i));
			
		}else{
			
		}
		
	}
	scores.setScores(newScores);
}
	
}
