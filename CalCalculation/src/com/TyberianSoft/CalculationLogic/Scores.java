package com.TyberianSoft.CalculationLogic;

import java.io.Serializable;



public class Scores implements Serializable{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private String name;
private int score;
public Scores(int score,String name) {
	// TODO Auto-generated constructor stub
	this.name=name;
	this.score=score;
}public String getName() {
	return name;
}public int getScore() {
	return score;
}public void setName(String name) {
	this.name = name;
}public void setScore(int score) {
	this.score = score;
}
}
