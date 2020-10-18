package project2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class InfixParser {

	/**
	 * Adds elements to two Stacks, one for integers and another for operands.
	 * also requires input file for expression.
	 * 
	 * @param intArr: Integer type Stack
	 * @param operArr: Character type Stack
	 * @param inputFile: FileInputStream type. file should contain
	 */
	public static void readExpression(Stack<Integer> intStack, Stack<Character> operStack, FileInputStream inputFile) {
		Scanner scnr = new Scanner(inputFile);
		String expression = scnr.nextLine();
		String element = "";
		
		for (char ch : expression.toCharArray()) {
			if (Character.isDigit(ch)) {
				element += ch;// Append to {element}
			} else {
				intStack.push(Integer.parseInt(element)); // Push the integer to the Stack
				element = ""; // Clear {element} for next integer
				operStack.push(ch); // Push operand to its own stack
			}
		}
		// Push the last number string to {intArr}
		intStack.push(Integer.parseInt(element));
		
		// Close the scanner
		scnr.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		FileInputStream inputFile = new FileInputStream("input.txt");
		Stack<Integer> intStack = new Stack<Integer>();
		Stack<Character> operStack = new Stack<Character>();
		
		readExpression(intStack, operStack, inputFile);
		System.out.println(intStack);
		System.out.println(operStack);
		
	}

}
