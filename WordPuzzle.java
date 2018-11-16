package il.ac.tau.cs.sw1.ex4;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class WordPuzzle {
	public static final char HIDDEN_CHAR = '_';
	public static final int MAX_VOCABULARY_SIZE = 3000;
	
	public static boolean abc(String s)
	{//checks if the word is from English chars only
		if (s=="") return false;
		String abc="abcdefghijklmnopqrstuvwxyz";
		for (int i=0;i<s.length();i++) 
			if ((abc.indexOf(s.charAt(i))==-1))
				return false;
		return true;
	}

	public static String[] scanVocabulary(Scanner scanner)
	{// Q - 1
		int count=0;
		String tmp;
		String [] arr= new String[MAX_VOCABULARY_SIZE];
		while (scanner.hasNext()&&count<MAX_VOCABULARY_SIZE)
		{
			tmp=scanner.next();
			tmp.toLowerCase();
			if (abc(tmp)&&!isInVocabulary(arr, tmp))// a legal word that hasnt shown yet
			{
					arr[count]=tmp;
					count++;
			}	
		}
		arr= Arrays.copyOfRange(arr, 0, count);
		Arrays.sort(arr);
		return arr;
	}
	public static boolean isInVocabulary(String[] vocabulary, String word){ // Q - 2
		for (String word1: vocabulary)
			if (word1!=null && word1.equals(word)) return true;
		return false;
	}

	
	public static boolean isLegalPuzzleStructure(char[] puzzle){  // Q - 3
		if (countHiddenInPuzzle(puzzle)<1) return false;//at least one '_'
		String s="abcdefghijklmnopqrstuvwxyz";
		for (char letter:puzzle)
			if (s.indexOf(letter)==-1 && letter!=HIDDEN_CHAR) return false;
		return true;
		
	}
	public static int countHiddenInPuzzle(char[] puzzle){ // Q - 4
		int count=0;
		for (char ch:puzzle)
			if (ch==HIDDEN_CHAR) count++;
		return count;
	}
	public static int occourrences(String a,char c) 
	{//count the char's instances in a string
		int count=0;
		for (int i=0;i<a.length();i++)
			if (a.charAt(i)==c)count++;
		return count;	
	}
	public static boolean legal(char[] puzzle,String word,char c) {
		//given a char with more than one instance checks if all the chars are either seen or hidden
		int index=word.indexOf(c),j=0;
		char val=puzzle[index];
		while (j<puzzle.length)
		{
			if (word.charAt(j)==c)
				if (puzzle[j]!=val) return false;
			j++;
		}
		return true;
	}
	public static boolean checkSolution(char[] puzzle, String word){ // Q - 5
		if (puzzle.length!=word.length())return false;
		for (int i=0;i<word.length();i++)
		{
			int count=occourrences(word, word.charAt(i));
			if (count>1 && !legal(puzzle, word, word.charAt(i))) return false;
			if ((puzzle[i]!=HIDDEN_CHAR)&&(puzzle[i]!=word.charAt(i))) return false;//if the char is shown then its the same 
				
		}	
		return true;
	}
	
	
	public static String[] getSolution(char[] puzzle, String[] vocabulary){ // Q - 6
		int count=0;
		String[] sol=new String[vocabulary.length];
		for (int i=0;i<vocabulary.length;i++)
		{
			if (checkSolution(puzzle, vocabulary[i]))
			{
			sol[count]=vocabulary[i];
 			count++;
			}
		}
		sol=Arrays.copyOfRange(sol, 0, count);
		if (sol.length==0) return null;
		else return sol;
		
	}
	
	public static int applyGuess(char guess, String solution, char[] puzzle){ // Q - 7
		
		int count=0;
		String puzz=String.valueOf(puzzle);
		int countsol=occourrences(solution,guess),countpuz=occourrences(puzz, guess);
		if ((countsol>=1)&&(countsol!=countpuz))
		for (int i=0;i<solution.length();i++)
		{
			char c=solution.charAt(i);
			if (c==guess)
			{
				puzzle[i]=guess;
				count++;
			}	
				
		}
		return count;
	}


	public static void main(String[] args) throws Exception{ //Q - 8
		String sol;
		int sol_num,trials;
		if (args.length < 1)
		throw new Exception("ERROR:"+ "Illegal amount of inputs!");
		String file_name=args[0];
		Scanner s1=new Scanner(new File(file_name));
		String[] a=scanVocabulary(s1);
		printReadVocabulary(file_name, a.length);
		printSettingsMessage();
		printEnterPuzzleMessage();
		Scanner scan=new Scanner(System.in);
		String puzz=scan.next();
		char[] puzzle=puzz.toCharArray();
		if (!isLegalPuzzleStructure(puzzle)) printIllegalPuzzleMessage();
		String[] solutions=getSolution(puzzle, a);
		if (solutions.length>1)
		{
			printIllegalSolutionsNumberMessage(solutions);
			sol_num=scan.nextInt();
			sol=solutions[sol_num-1];
		}
		else sol=getSolution(puzzle, a)[0];//only solution
		printGameStageMessage();
		trials=countHiddenInPuzzle(puzzle)+3;
		while (trials>0)
		{
			printEnterYourGuessMessage();
			char guess=scan.next().charAt(0);
			int change=applyGuess(guess, sol, puzzle);
			if (change>0)//means it is a correct guess
			        {
				if (countHiddenInPuzzle(puzzle)==0) 
				{
					printWinMessage();
					break;
				}
				else printCorrectGuess(trials-1);
					}
			        
			else printWrongGuess(trials-1);
			trials--;
		}
		if (trials==0) printGameOver();
		scan.close();
		s1.close();
	}


	/*************************************************************/
	/*********************  Don't change this ********************/
	/*************************************************************/
	
	public static void printReadVocabulary(String vocabularyFileName, int numOfWords){
		System.out.println("Read " + numOfWords + " words from " + vocabularyFileName);
	}

	public static void printSettingsMessage(){
		System.out.println("--- Settings stage ---");
	}
	
	public static void printEnterPuzzleMessage(){
		System.out.println("Enter your puzzle:");
	}
	
	public static void printIllegalPuzzleMessage(){
		System.out.println("Illegal puzzle, try again!");
	}
	
	public static void printIllegalSolutionsNumberMessage(String[] solutions){
		System.out.println("Puzzle doesn't have a single solution, choose a solution!");
		for(int i = 0; i< solutions.length; i++)
		{
			System.out.format("%d	"+solutions[i]+"%n", i+1);
		}
	}
	
	
	public static void printGameStageMessage(){
		System.out.println("--- Game stage ---");
	}
	
	public static void printPuzzle(char[] puzzle){
		System.out.println(puzzle);
	}
	
	public static void printEnterYourGuessMessage(){
		System.out.println("Enter your guess:");
	}
	
	public static void printCorrectGuess(int attemptsNum){
		System.out.println("Correct Guess, " + attemptsNum + " guesses left");
	}
	
	public static void printWrongGuess(int attemptsNum){
		System.out.println("Wrong Guess, " + attemptsNum + " guesses left");
	}

	public static void printWinMessage(){
		System.out.println("Congratulations! You solved the puzzle");
	}
	
	public static void printGameOver(){
		System.out.println("Game over!");
	}

}


