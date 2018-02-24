// Pranav Sukumar
// Tesla STEM High School
// Senior Division

package acslcontest2;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.*;

public class Contest2New {
	public static void main(String[] args) {
		try{
			int[] bIndex;
			String missingParen, complementString, result;
			ArrayList<String> listOfTokens;
			ArrayList<Integer> matchPositions;
			
			String[] inputArray = new String[5];
			Scanner in = new Scanner(System.in);
			for (int i = 0; i < 5; i ++){
				inputArray[i] = in.nextLine();
			}
			String[] resultArray = new String[5];
			
			for (int i = 0; i < 5; i ++){
				bIndex = indexOfBrackets(inputArray[i]);
				missingParen = findMissingCharAsString(bIndex);
				complementString = complementParen(missingParen);
				listOfTokens = tokenize(inputArray[i]);
				if (isCloseParen(missingParen)) {
					matchPositions = matchClose(listOfTokens, missingParen, complementString);
				} else {
					matchPositions = matchOpen(listOfTokens, missingParen, complementString);
				}
				result = "";
				for (int j = 0; j < matchPositions.size(); j++) {
					//System.out.println("Is Feasible At Location " + matchPositions.get(i));
					result += matchPositions.get(j) + ", ";
				}
				
				resultArray[i] = result;
			}
			
			for (int i = 0; i < 5; i++){
				System.out.println(resultArray[i].substring(0, resultArray[i].length()-2));
			}
			
			
			
			in.close();
		} catch (Exception e){
			System.out.println("I missed this point");
		}
		
	}

	public static int[] indexOfBrackets(String s) {
		int[] indexesOfBrackets = new int[6];
		// { [ ( ) ] }
		for (int i = 0; i < 6; i++) {
			indexesOfBrackets[i] = -1;
		}
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '{') {
				indexesOfBrackets[0] = i;
			} else if (s.charAt(i) == '[') {
				indexesOfBrackets[1] = i;
			} else if (s.charAt(i) == '(') {
				indexesOfBrackets[2] = i;
			} else if (s.charAt(i) == ')') {
				indexesOfBrackets[3] = i;
			} else if (s.charAt(i) == ']') {
				indexesOfBrackets[4] = i;
			} else if (s.charAt(i) == '}') {
				indexesOfBrackets[5] = i;
			}
		}
		return indexesOfBrackets;
	}

	public static String findMissingCharAsString(int[] bIndex) {
		String missingCharAsString = null;
		if (bIndex[0] == -1) {
			missingCharAsString = "{";
		} else if (bIndex[1] == -1) {
			missingCharAsString = "[";
		} else if (bIndex[2] == -1) {
			missingCharAsString = "(";
		} else if (bIndex[3] == -1) {
			missingCharAsString = ")";
		} else if (bIndex[4] == -1) {
			missingCharAsString = "]";
		} else if (bIndex[5] == -1) {
			missingCharAsString = "}";
		}
		return missingCharAsString;
	}

	public static String complementParen(String s) {
		if (s.equals("{")) {
			return "}";
		}
		if (s.equals("[")) {
			return "]";
		}
		if (s.equals("(")) {
			return ")";
		}
		if (s.equals("}")) {
			return "{";
		}
		if (s.equals("]")) {
			return "[";
		}
		if (s.equals(")")) {
			return "(";
		}
		return null;
	}

	public static boolean isOperator(String t) {
		return ((t.equals("+")) || (t.equals("–")) || t.equals("-") || t.equals("*") || t.equals("/"));
	}

	public static boolean isOpenParen(String t) {
		return ((t.equals("{")) || (t.equals("[")) || t.equals("("));
	}

	public static boolean isCloseParen(String t) {
		return ((t.equals("}")) || (t.equals("]")) || t.equals(")"));
	}

	public static boolean isNum(String t) {
		return Character.isDigit(t.charAt(0));
	}

	public static ArrayList<String> tokenize(String s) {
		ArrayList<String> tokens;
		char c;
		boolean isInt;
		int i = 0, l = 0;
		String intString, str;
		tokens = new ArrayList<String>();
		l = s.length();
		while (i < l) {
			c = s.charAt(i);
			str = Character.toString(c);
			if (isOperator(str) || isOpenParen(str) || isCloseParen(str)) {
				tokens.add(str);
			}
			isInt = false;
			if (Character.isDigit(c)) {
				isInt = true;
				intString = "";
				while (isInt && (i < l)) {
					intString = intString + c;
					i++;
					if (i < l) {
						c = s.charAt(i);
						isInt = Character.isDigit(c);
					}
				}
				i--;
				tokens.add(intString);
			}
			i++;
		}
		return tokens;
	}

	public static ArrayList<Integer> matchClose(ArrayList<String> tokens, String missingParen, String c) {
		String t;
		int tokenCount, i = 0, pIndex = 0;
		ArrayList<Integer> position;
		boolean feasible, openMatchFound;
		Stack<String> operators;
		tokenCount = tokens.size();
		position = new ArrayList<Integer>();
		operators = new Stack<String>();
		openMatchFound = false;
		while (i < tokenCount) {
			t = tokens.get(i);
			pIndex = pIndex + t.length();
			if (isOpenParen(t)) {
				operators.push(t);
			}
			if (t.equals(c)) {
				openMatchFound = true;
			} else if (openMatchFound) {
				feasible = false;
				if (isOperator(t)) {
				}
				if (isNum(t)) {
					if ((i > 0) && isOperator(tokens.get(i - 1))) {
						if (operators.peek().equals(c)) {
							feasible = true;
						}
					}
				}
				if (isCloseParen(t)) {
					if (operators.peek().equals(complementParen(t))) {
						operators.pop();
						if (operators.peek().equals(complementParen(missingParen))) {
							feasible = true;
						}
					}
				}
				if (feasible) {
					position.add(pIndex + 1);
				}
			}
			i++;
		}
		return position;
	}

	public static ArrayList<Integer> matchOpen(ArrayList<String> tokens, String missingParen, String c) {
		String t;
		int tokenCount, i = 0, pIndex = 0, totalLength = 0;
		ArrayList<Integer> position, result;
		boolean feasible, closeMatchFound;
		Stack<String> operators;
		tokenCount = tokens.size();
		position = new ArrayList<Integer>();
		result = new ArrayList<Integer>();
		operators = new Stack<String>();
		closeMatchFound = false;
		i = tokenCount;
		while (i > 0) {
			t = tokens.get(i - 1);
			totalLength = totalLength + t.length();
			pIndex = pIndex - t.length();
			if (isCloseParen(t)) {
				operators.push(t);
			}
			if (t.equals(c)) {
				closeMatchFound = true;
			} else if (closeMatchFound) {
				feasible = false;
				if (isOperator(t)) {
				}
				if (isNum(t)) {
					if ((i > 0) && isOperator(tokens.get(i))) {
						if (operators.peek().equals(c)) {
							feasible = true;
						}
					}
				}
				if (isOpenParen(t)) {
					if (operators.peek().equals(complementParen(t))) {
						operators.pop();
						if (operators.peek().equals(complementParen(missingParen))) {
							feasible = true;
						}
					}
				}
				if (feasible) {
					position.add(pIndex + 1);
				}
			}
			i--;
		}
		for (int j = position.size(); j > 0; j--) {
			result.add(totalLength + position.get(j - 1));
		}
		return result;
	}
}
