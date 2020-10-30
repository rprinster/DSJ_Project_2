package project2;

import java.io.FileInputStream;
import java.util.Scanner;
import java.util.Stack;

/**
 * <h1>Project 2 - Infix Parser</h1>
 * This program parses an infix formatted mathematical expression and solves it
 * using postfix conversion.
 * 
 * @author	Connor Clawson, James Smotherman, Ryan Prinster
 * @version	1.0
 * @since	10-29-2020
 */
public class ExpressionSolver {
	
	/**
	 * Ensures the infix expression is in correct readable form.
	 * @param expression: infix expression string
	 * @return Formatted infix string
	 */
    public static String readExpression(String expression) {
        StringBuilder trueInfix = new StringBuilder();
        expression = expression.replaceAll("\\s", "");
        for (int i = 0; i < expression.length(); i++) {
        	
            trueInfix.append(expression.charAt(i));
            if (i == expression.length() - 1) { break; } // end of string
            if (Character.isDigit(expression.charAt(i)) && Character.isDigit(expression.charAt(i + 1))) { continue; } // Integer or single character operator
            
            // The following manage two-character comparison operators
            if ((expression.charAt(i) == '>' && expression.charAt(i + 1) == '=') ||
                    (expression.charAt(i) == '<' && expression.charAt(i + 1) == '=')) { continue; } // Greater/lesser than or equal to
            if (expression.charAt(i) == '|' && expression.charAt(i + 1) == '|') { continue; } // or
            if (expression.charAt(i) == '&' && expression.charAt(i + 1) == '&') { continue; } // and
            if (expression.charAt(i) == '=' && expression.charAt(i + 1) == '=') { continue; } // equals
            if (expression.charAt(i) == '!' && expression.charAt(i + 1) == '=') { continue; } // does not equal
            trueInfix.append(" ");
        }
        return trueInfix.toString();
    }
	
	/**
	 * Solves the previously formatted infix expression
	 * @param infix: infix expression to be solved
	 * @throws ArithmeticException Safely stops division by zero solves.
	 */
	public static void solve(String infix) throws Exception {
		
		//Step 1: Convert Infix String to postfix Array
		String infixArr[] = infix.split(" ");
		String postfix = infixToPostfix(infixArr);	
		String postfixArr[] = postfix.split(" ");
		
		
		try { //Step 2: Evaluate Postfix Array
			int solution = postfixEval(postfixArr);
			
			//Step 3: Display Solution
			System.out.println("Infix:    " + infix);
			System.out.println("Postfix:  " + postfix);
			System.out.println("Solution: " + solution );
			System.out.println();
		} catch (ArithmeticException e) { // Safely catch any divide by zero errors
			System.out.println("Cannot divide by zero.\n");
		}
	}
	

	/**
	 * Converts an infix expression to postfix.
	 * @param infix: an infix expression
	 * @return: the postfix expression
	 * @throws Exception: stack's peek() and pop() methods
	 */
	public static String infixToPostfix(String[] infix) throws Exception {
		Stack<String> S = new Stack<String>(); 
		StringBuilder postfix = new StringBuilder();		
		
		for (String s : infix) { // For each String in the infix String array
			if (Character.isDigit(s.charAt(0))) { postfix.append(s).append(' '); } // If digit, add to postfix string
			else if (s.equals("(")) { S.push(s); }	// If "(" is found, push onto stack
			else if (s.equals(")")){	// If ")" is found, pop stack until "(" is found
				while (!S.peek().equals("(")) { postfix.append(S.pop()).append(' '); } 
				S.pop();
			}else {	// if s is an operator
				while (!S.isEmpty() && !S.peek().equals("(") && precedence(s, S.peek())) { // While precedence <= top of stack
					postfix.append(S.pop()).append(' '); // Pop stack and append to postfix string
				}
				S.push(s); // Push operator onto stack 
			} 
		}
		while (!S.isEmpty()) { postfix.append(S.pop()).append(' '); } //Convert Stack to String to be returned
		return postfix.substring(0, postfix.length() - 1);
	}
	
	
	/**
	 * Evaluates a postfix expression in which all operands are integers.
	 * @param postfix: String array of postfix expression
	 * @return: Integer solution of expression
	 * @throws Exception: EmptyStackException
	 */
	public static int postfixEval(String[] postfix) throws Exception {
		Stack<Integer> S = new Stack<Integer>();
		for (String s : postfix) { // For each String s in postfix array
			if (Character.isDigit(s.charAt(0))) { S.push(Integer.parseInt(s)); } // If integer, push onto stack 
			else { // Else it is an operator
				int right = S.pop(), left = S.pop(); // Pop top two numbers from stack
				
				// Evaluate two number expression based on given operator. Push result onto stack
				if (s.equals("+")) { S.push(left + right); }
				if (s.equals("-")) { S.push(left - right); }
				if (s.equals("*")) { S.push(left * right); }
				if (s.equals("/")) { S.push(left / right); }
				if (s.equals("^")) { S.push((int)Math.pow(left, right)); }
				if (s.equals("%")) { S.push(left % right); }
				
				// For comparisons: Assume true = 1 and false = 0
				if (s.equals(">")) { // 1 if true, 0 if false
					if (left > right){S.push(1);}
					else {S.push(0);}
				}
				if (s.equals(">=")) { // 1 if true, 0 if false
					if (left >= right){S.push(1);}
					else {S.push(0);}
				}
				if (s.equals("<")) { // 1 if true, 0 if false
					if (left < right){S.push(1);}
					else {S.push(0);}
				}
				if (s.equals("<=")) { // 1 if true, 0 if false
					if (left <= right){S.push(1);}
					else {S.push(0);}
				}
				if (s.equals("==")) { // 1 if true, 0 if false
					if (left == right){S.push(1);}
					else {S.push(0);}
				}
				if (s.equals("!=")) { // 1 if true, 0 if false
					if (left != right){S.push(1);}
					else {S.push(0);}
				}
				
				// For booleans: if number is zero then {false}; any other int is {true}
				if (s.equals("&&")) { // true if neither number is zero 
					if (left != 0 && right !=0){S.push(1);}
					else {S.push(0);}
				}
				if (s.equals("||")) { // true if either number is nonzero
					if (left != 0 || right !=0){S.push(1);}
					else {S.push(0);}
				}
			}
		}
		return S.pop(); // Return solution
	}
	
	
	/**
	 * Tests whether the precedence of {current} operator is not higher than the operator on top of the stack.
	 * @param current: Operator represented by string
	 * @param top: Operator represented by string
	 * @return {true} if {current}'s precedence is lower than or equal to {top}; {false} otherwise
	 */
	public static boolean precedence(String current, String top) {
		// call checkPrecedence() to find integer value precedence of each operator
		int currentPrec = checkPrecedence(current);
		int topPrec = checkPrecedence(top);
		
		// return true if current operator precedence is lower than or equal to top
		if (currentPrec <= topPrec) { return true; } 
		else { return false; }	
	}
	
	/**
	 * Checks precedence level of the operator passed
	 * @param operator - operator represented by string
	 * @return integer value representing precedence level
	 */
	public static int checkPrecedence(String operator) {
		int precedenceLevel=0;
		
		// assigns correct precedence level for the operator being checked
		if (operator.equals("^")) {precedenceLevel = 7;}
		else if (operator.equals("*") || operator.equals("/") || operator.equals("%")) {precedenceLevel = 6;}
		else if (operator.equals("+") || operator.equals("-")) {precedenceLevel = 5;}
		else if (operator.equals(">") || operator.equals(">=") || operator.equals("<") || operator.equals("<=")) {precedenceLevel = 4;}
		else if (operator.equals("==") || operator.equals("!=")) {precedenceLevel = 3;}
		else if (operator.equals("&&")) {precedenceLevel = 2;}
		else if (operator.equals("||")) {precedenceLevel = 1;}

		return precedenceLevel;
	}
	
	/**
	 * 
	 * @param args -  Never used.
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Scanner scnr = new Scanner(new FileInputStream("input.txt"));
		while (scnr.hasNext()) {
			String expression = scnr.nextLine(); // read each line as an expression
			if (expression.isEmpty()) { // skip empty lines
				continue;
			} else {
				solve(readExpression(expression)); // solve current line after parsing input
			}
		}
		scnr.close();
	}
}