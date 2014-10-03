package utils;

public class Digits {
	public static String setDigits(int number, int digitNumber){
		String N;
		N="";
		if (number>0) N=""+number;
		if(digitNumber>countDigits(number)){
			for(int i =0;i<digitNumber-countDigits(number);i++){
				N="0"+N;
			}
		}
		return N;
	}
	public static int countDigits(int number){
		int N=0;
		while (number>=1) {
			number/=10;
			N++;
		}
		return N;
	}
}