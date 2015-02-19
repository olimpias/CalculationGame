package com.TyberianSoft.CalculationLogic;

import java.util.Random;

public class TimesTables {
private int number1;
private int number2;
private int result;
private Random random;
public TimesTables() {
	random=new Random();
}
public void GenerateNumber(int score){
	if(score<10){
		number1=random.nextInt(10);
		number2=random.nextInt(10);	
	}else if(score<25){
		number1=random.nextInt(8)+2;
		number2=random.nextInt(8)+2;	
	}else{
		number1=random.nextInt(6)+4;
		number2=random.nextInt(8)+2;
	}
	result=number1*number2;
}
public int getNumber1() {
	return number1;
}public int getNumber2() {
	return number2;
}public int getResult() {
	return result;
}public boolean isResultTrue(int num){
	if(num==result)return true;
	return false;
}
}
