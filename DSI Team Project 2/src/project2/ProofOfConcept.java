package project2;

import java.io.FileInputStream;
import java.util.Scanner;
import java.util.Stack;

public class ProofOfConcept {
	
	/**
     * Ensures the infix question is in correct readable form
     * @param inputFile: FileInputStream type. file should contain
     */
    public static String readExpression(String expression) {
        StringBuilder trueInfix = new StringBuilder();
        expression = expression.replaceAll("\\s", "");
        for (int i = 0; i < expression.length(); i++) {
            trueInfix.append(expression.charAt(i));
            if (i == expression.length() - 1) { break;}
            if (Character.isDigit(expression.charAt(i)) && Character.isDigit(expression.charAt(i + 1))) { continue; }
            if ((expression.charAt(i) == '>' && expression.charAt(i + 1) == '=') ||
                    (expression.charAt(i) == '<' && expression.charAt(i + 1) == '=')) { continue; }
            trueInfix.append(" ");
        }
        return trueInfix.toString();
    }
	
	
	/** Solves an infix expression given as unprocessed string
	 * 
	 * @param infix: infix expression to be solved (unprocessed string)
	 * @throws Exception
	 */
	public static void infixSolver(String infix) throws Exception {
								
		//Step 1: Parse raw string into infix array
		//FIXME: Add functionality to parse raw string with varying spaces into String[] of values and operators
		String infixArr[] = infix.split(" ");		//FIXME: Delete this line after better parsing method added
		
		//Step 2: Convert Infix Array to Postfix Array
		String postfix = infixToPostfix(infixArr);	//FIXME: I think we want to change infixToPostfix so that it directly gives us the array	
		String postfixArr[] = postfix.split(" ");	//FIXME: Delete this line after above line is fixed
		
		
		try { //Step 3: Evaluate Postfix Array
			int solution = postfixEval(postfixArr);
			
			//Step 4: Display Solution
			System.out.println("Infix:    " + infix);
			System.out.println("Postfix:  " + postfix);
			System.out.println("Solution: " + solution );
			System.out.println();
		} catch (ArithmeticException e) { // Step 4a. safely catch any divide by zero errors
			System.out.println("Cannot divide by zero.\n");
		} catch (NumberFormatException f) {
			System.out.println("FIXME: Failed to parse expression.\n" + f); // FIXME: throws when no space is present to delimit a string.
		}
	}
	

	/** Converts an infix expression to postfix.
	 * @param infix: an infix expression
	 * @return: the postfix expression
	 * @throws Exception: stack's peek() and pop() methods
	 */
	public static String infixToPostfix(String[] infix) throws Exception {
		Stack<String> S = new Stack<String>();
		StringBuilder postfix = new StringBuilder();		
		
		for (String s : infix) {
			if (Character.isDigit(s.charAt(0))) { postfix.append(s).append(' '); }
			else if (s.equals("(")) { S.push(s); }
			else if (s.equals(")")){
				while (!S.peek().equals("(")) { postfix.append(S.pop()).append(' '); } // FIXME: throws NullPointerException when a space is missing before an '(' in a string
				S.pop();
			}else {	//if s is an operator 
				while (!S.isEmpty() && !S.peek().equals("(") && precedence(s, S.peek())) {
					postfix.append(S.pop()).append(' ');
				}
				S.push(s);
			} 
		}
		while (!S.isEmpty()) { postfix.append(S.pop()).append(' '); }	//FIXME: Currently converting LinkedList to string to output?
		return postfix.substring(0, postfix.length() - 1);				//FIXME: I think more efficient if we directly output string array
	}
	
	
	/** Evaluates a postfix expression in which all operands are integers.
	 * @param postfix: tokens in the postfix expression
	 * @return: evaluation result
	 * @throws Exception: stack's pop() method
	 */
	public static int postfixEval(String[] postfix) throws Exception {
		Stack<Integer> S = new Stack<Integer>();
		for (String s : postfix) {
			
			if (Character.isDigit(s.charAt(0))) { S.push(Integer.parseInt(s)); }
			else {
				int right = S.pop(), left = S.pop();
				if (s.equals("+")) { S.push(left + right); }
				if (s.equals("-")) { S.push(left - right); }
				if (s.equals("*")) { S.push(left * right); }
				if (s.equals("/")) { S.push(left / right); }
	            
				
	            //FIXME: test operators added below this point to ensure correct functionality
				if (s.equals("^")) { S.push((int)Math.pow(left, right)); }
				if (s.equals("%")) { S.push(left % right); }
				
				//Comparisons: Assume true = 1 and false = 0
				if (s.equals(">")) { //1 if true, 0 if false
					if (left > right){S.push(1);}
					else {S.push(0);}
				}
				if (s.equals(">=")) { //1 if true, 0 if false
					if (left >= right){S.push(1);}
					else {S.push(0);}
				}
				if (s.equals("<")) { //1 if true, 0 if false
					if (left < right){S.push(1);}
					else {S.push(0);}
				}
				if (s.equals("<=")) { //1 if true, 0 if false
					if (left <= right){S.push(1);}
					else {S.push(0);}
				}
				if (s.equals("==")) { //1 if true, 0 if false
					if (left == right){S.push(1);}
					else {S.push(0);}
				}
				if (s.equals("!=")) { //1 if true, 0 if false
					if (left != right){S.push(1);}
					else {S.push(0);}
				}
				
				//Booleans: if number is zero then {false}; any other int is {true}
				if (s.equals("&&")) { //true if neither number is zero 
					if (left != 0 && right !=0){S.push(1);}
					else {S.push(0);}
				}
				if (s.equals("||")) { //true if either number is nonzero
					if (left != 0 || right !=0){S.push(1);}
					else {S.push(0);}
				}
			}
		}
		return S.pop();
	}
	
	
	/** Tests whether the precedence of {current} operator is not higher than the operator on top of the stack.
	 * @param current: operator represented by string
	 * @param top: operator represented by string
	 * @return: {true} if {current}'s precedence is lower than or equal to {top}; {false} otherwise
	 */
	public static boolean precedence(String current, String top) {
		//find integer value for precedence of each operator
		int currentPrec = checkPrecedence(current);
		int topPrec = checkPrecedence(top);
		
		//return true if current operator precedence is lower than or equal to top
		if (currentPrec <= topPrec) { return true; } 
		else { return false; }	
	}
	
	/** Checks precedence level of the operator passed
	 * 
	 * @param operator: operator represented by string
	 * @return: integer value representing precedence level
	 */
	public static int checkPrecedence(String operator) {
		int precedenceLevel=0;
		
		//assigns correct precedence for the operator being checked
		if (operator.equals("^")) {precedenceLevel = 7;}
		else if (operator.equals("*") || operator.equals("/") || operator.equals("%")) {precedenceLevel = 6;}
		else if (operator.equals("+") || operator.equals("-")) {precedenceLevel = 5;}
		else if (operator.equals(">") || operator.equals(">=") || operator.equals("<") || operator.equals("<=")) {precedenceLevel = 4;}
		else if (operator.equals("==") || operator.equals("!=")) {precedenceLevel = 3;}
		else if (operator.equals("&&")) {precedenceLevel = 2;}
		else if (operator.equals("||")) {precedenceLevel = 1;}

		return precedenceLevel;
	}
	
	
	//Main Method
	public static void main(String[] args) throws Exception {
		
		
		Scanner scnr = new Scanner(new FileInputStream("input.txt"));
		
		while (scnr.hasNext()) {
			infixSolver(readExpression(scnr.nextLine()));
		}
		scnr.close();
	}
}
