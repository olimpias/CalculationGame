package com.TyberianSoft.CalculationLogic;

import java.util.Random;

public class MathematicalOperations {
private int num1;
private int num2;
private int result;
private Random random;
private char operation;
public MathematicalOperations() {
	random=new Random();
	// TODO Auto-generated constructor stub
}
public void GenerateOperation(int score){
	int op=random.nextInt(3);
	if(op==2){
		if(score<100){
			num1=10*random.nextInt(10)+random.nextInt(10);
			num2=random.nextInt(8)+2;
		}else if(score<200){
			num1=10*random.nextInt(10)+random.nextInt(10);
			num2=10*random.nextInt(2)+random.nextInt(10);
		}else if(score<300){
			num1=10*random.nextInt(10)+random.nextInt(10);
			num2=10*random.nextInt(3)+random.nextInt(10);	
		}else if(score<500){
			num1=10*random.nextInt(10)+random.nextInt(10);
			num2=10*random.nextInt(5)+random.nextInt(10);	
		}else if(score<700){
			num1=10*random.nextInt(10)+random.nextInt(10);
			num2=10*random.nextInt(7)+random.nextInt(10);
		}else {
			num1=10*random.nextInt(10)+random.nextInt(10);
			num2=10*random.nextInt(10)+random.nextInt(10);
		}
	}else{
		if(score%2==0){
			num1=10*random.nextInt(10)+random.nextInt(10);
			num2=10*random.nextInt(10)+random.nextInt(10);
		}else{
			num1=10*(random.nextInt(4)+6)+random.nextInt(10);
			num2=10*random.nextInt(10)+(random.nextInt(4)+6);
		}
	}
	


switch (op) {
case 0:
	operation='+';
	result=num1+num2;
	break;
case 1:
	operation='-';
	if(num1<num2){
		int tmp=num2;
		num2=num1;
		num1=tmp;
	}
	result=num1-num2;
	break;

case 2:
	operation='x';
	result=num1*num2;
	break;

default:
	operation='x';
	result=num1*num2;
	break;
}
}
public int getNum1() {
	return num1;
}public int getNum2() {
	return num2;
}public char getOperation() {
	return operation;
}public int getResult() {
	return result;
}public boolean isResultTrue(int num){
	if(result==num)return true;
	return false;
}
}
