package com.TyberianSoft.CalculationLogic;

import java.util.Comparator;

public class ScoreCompare implements Comparator<Scores> {

	@Override
	public int compare(Scores lhs, Scores rhs) {
		if(rhs==null){
			return -1;
		}
		if(lhs==null){
			return 1;
		}
		if(lhs.getScore()>rhs.getScore())return -1;
		else if(lhs.getScore()==rhs.getScore())return 0;
		return 1;
	}

	

}
