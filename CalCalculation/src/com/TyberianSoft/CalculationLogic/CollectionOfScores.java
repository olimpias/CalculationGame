package com.TyberianSoft.CalculationLogic;

import java.io.Serializable;
import java.util.ArrayList;

public class CollectionOfScores implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private ArrayList<Scores> scores;

public CollectionOfScores() {
	// TODO Auto-generated constructor stub
	scores=new ArrayList<Scores>();
}
public ArrayList<Scores> getScores() {
	return scores;
}public void setScores(ArrayList<Scores> scores) {
	this.scores = scores;
}
}
