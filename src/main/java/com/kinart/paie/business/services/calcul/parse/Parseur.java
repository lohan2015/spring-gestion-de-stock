/***

Expression parser
lang:   Java code
author: Jos de Jong, 2010-09-26
site:   www.speqmath.com

Features:
    Operators:
        & | << >>
        = <> < > <= >=
        + -
 * / %
        ^
        !

    Functions:
        Abs, Cube, Exp, Log, Log10, Sign, Sqrt, Square
        Sin, Cos, Tan, ASin, ACos, ATan, Atan2
        Factorial
        Avg, Max, Median, Min, Prod, Std, Sum, Var
        You can assign new functions, like f(x,y)=2*x^y+3

    Variables:
        Pi, e
        You can assign variables, like myvar=3.4/2

    Other:
        Scientific notation supported
        Error handling supported

 */

package com.kinart.paie.business.services.calcul.parse;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Parseur
{
	/**
	 * Constructor
	 */
	public Parseur()
	{
		// initialize the parser
		init();
	}

	/**
	 * Parse the given expression
	 * 
	 * @param new_expr
	 *            A string containing an expression, for example "2+3"
	 * @return ans A string containing the answer, or an error when an error occurred
	 */
	public String parse(final String new_expr)
	{
		try
		{
			// initialize all variables
			expr = new_expr;
			ans = 0.0;
			errorId = null;

			Function function = parse_start();

			// evaluate the expression
			ans = function.eval();

			if (function instanceof FunctionAssign)
			{
				// function assignment
				FunctionAssign fa = (FunctionAssign) function;
				String desc = "";
				desc += fa.getName();
				desc += "(";
				for (int i = 0; i < fa.getVariables().size(); i++)
				{
					if (i > 0)
					{
						desc += ", ";
					}
					desc += fa.getVariables().get(i).getName();
				}
				desc += ")";

				ans_str = "Fonction " + desc + " defini";
			}
			else if (function instanceof VariableAssign)
			{
				// variable assignment
				VariableAssign variableAssign = (VariableAssign) function;
				ans_str = String.format("%s = %g", variableAssign.getName(), ans);

				// add the answer to memory as variable "Ans"
				variablelist.put("ANS", ans);
			}
			else
			{
				DecimalFormat formatter = new DecimalFormat("#.##########");
				ans_str = formatter.format(ans);

				// add the answer to memory as variable "Ans"
				variablelist.put("ANS", ans);
			}
		}
		catch (Error err)
		{
			ans_str = err.get();
			expr = "";
			errorId = err.get_id();
		}

		return ans_str;
	}

	/**
	 * Get the next character from the expression. The character is stored into the char t. If the end of the expression is reached, the function puts null ('\0') in t.
	 */
	private void getChar()
	{
		t_pos++;
		if (t_pos < expr.length())
		{
			t = expr.charAt(t_pos);
		}
		else
		{
			t = '\0';
		}
	}

	/**
	 * Get the first character from the expression. The character is stored into the char t. If the end of the expression is reached, the function puts zero ('\0') in t.
	 */
	private void getFirstChar()
	{
		t_pos = 0;
		if (expr.length() > 0)
		{
			t = expr.charAt(0);
		}
		else
		{
			t = '\0';
		}
	}

	/**
	 * Get next token in the current string expr. Uses the Parser data expr, e, token, t, token_type and err
	 */
	private void getToken() throws Error
	{
		token_type = TOKENTYPE.NOTHING;
		token = "";

		// skip over whitespaces
		while (t == ' ' || t == '\t')
		{ // space or tab
			getChar();
		}

		// check for end of expression
		if (t == '\0')
		{
			// token is still empty
			token_type = TOKENTYPE.DELIMETER;
			return;
		}

		// check for minus, comma, parentheses, factorial
		if (t == '-' || t == ',' || t == '(' || t == ')' || t == '!')
		{
			token_type = TOKENTYPE.DELIMETER;
			token += t;
			getChar();
			return;
		}

		// check for operators (delimeters)
		if (isDelimeter(t))
		{
			token_type = TOKENTYPE.DELIMETER;
			while (isDelimeter(t))
			{
				token += t;
				getChar();
			}
			return;
		}

		// check for a number
		if (isDigitDot(t))
		{
			token_type = TOKENTYPE.NUMBER;
			while (isDigitDot(t))
			{
				token += t;
				getChar();
			}

			// check for scientific notation like "2.3e-4" or "1.23e50"
			if (t == 'E' || t == 'e')
			{
				token += t;
				getChar();

				if (t == '+' || t == '-')
				{
					token += t;
					getChar();
				}

				while (isDigit(t))
				{
					token += t;
					getChar();
				}
			}
			return;
		}

		// check for variables or functions
		if (isAlpha(t))
		{
			while (isAlpha(t) || isDigit(t))
			{
				token += t;
				getChar();
			}

			// skip whitespaces
			while (isWhiteSpace(t))
			{ // space or tab
				getChar();
			}

			// check the next non-whitespace character
			if (t == '(')
			{
				token_type = TOKENTYPE.FUNCTION;
			}
			else
			{
				token_type = TOKENTYPE.VARIABLE;
			}

			return;
		}

		// something unknown is found, wrong characters -> a syntax error
		token_type = TOKENTYPE.UNKNOWN;
		while (t != '\0')
		{
			token += t;
			getChar();
		}
		throw new Error(1, token, row(), col());
	}

	/**
	 * checks if the given char c is whitespace whitespace when space chr(32) or tab chr(9)
	 */
	private boolean isWhiteSpace(final char c)
	{
		return c == 32 || c == 9; // space or tab
	}

	/**
	 * checks if the given char c is a delimiter minus is checked apart, can be unary minus
	 */
	private boolean isDelimeter(final char c)
	{
		return c == '&' || c == '|' || c == '<' || c == '>' || c == '=' || c == '+' || c == '/' || c == '*' || c == '%' || c == '^' || c == '!';
	}

	/**
	 * checks if the given char c is a letter or underscore
	 */
	private boolean isAlpha(final char c)
	{
		return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_');
	}

	/**
	 * checks if the given char c is a digit or dot
	 */
	private boolean isDigitDot(final char c)
	{
		return ((c >= '0' && c <= '9') || c == '.');
	}

	/**
	 * checks if the given char c is a digit
	 */
	private boolean isDigit(final char c)
	{
		return ((c >= '0' && c <= '9'));
	}

	/**
	 * checks if the given variable name is legal to use, i.e. not equal to "pi", "e", etc.
	 */
	private boolean isLegalVariableName(String name)
	{
		String nameUpper = name.toUpperCase();

		boolean isLegal = !constants.containsKey(nameUpper);

		return isLegal;
	}

	// parse levels below are in order or precedence
	private Function parse_start() throws Error
	{

		// get the first character in expression
		getFirstChar();

		getToken();

		// check if expression is not empty
		if (StringUtils.isEmpty(token)) { throw new Error(4, row(), col()); }

		Function ans = parse_variable_ass();

		// check for garbage at the end of the expression
		// an expression ends with a character '\0' and token_type DELIMETER
		if (!StringUtils.isEmpty(token))
		{
			if (token_type == TOKENTYPE.DELIMETER)
			{
				// user entered a not existing operator like "//"
				throw new Error(101, token, row(), col());
			}
			else
			{
				throw new Error(5, token, row(), col());
			}
		}

		return ans;
	}

	/*
	 * assignment of variable, for example "a = 3.4/2"
	 */
	private Function parse_variable_ass() throws Error
	{
		if (token_type == TOKENTYPE.VARIABLE)
		{
			// copy current token (we may have to revert to it)
			char t_now = t;
			int t_pos_now = t_pos;
			TOKENTYPE token_type_now = token_type;
			String token_now = token;

			getToken();
			if (token.equals("="))
			{
				// assignment of a variable
				String var_name = token_now.toUpperCase();

				// check if variable name is legal
				if (!isLegalVariableName(var_name)) { throw new Error(303, var_name); }

				getToken();
				return new VariableAssign(variablelist, var_name, parse_conditions());
			}

			// No variable defined. go back to previous token
			t = t_now;
			t_pos = t_pos_now;
			token_type = token_type_now;
			token = token_now;
		}

		return parse_function_ass();
	}

	/*
	 * assignment of variable, for example "a = 3.4/2"
	 */
	private Function parse_function_ass() throws Error
	{
		if (token_type == TOKENTYPE.FUNCTION)
		{
			// check for a function assignment like "f(a,b)=a*b"

			// copy current token (we may have to revert to it)
			char t_now = t;
			int t_pos_now = t_pos;
			TOKENTYPE token_type_now = token_type;
			String token_now = token;

			String name = token.toUpperCase();

			getToken();
			if (token.equals("("))
			{
				// possible assignment of a function.
				// Verify this by checking if it has the form "f(a,b,c,...)="
				boolean hasCorrectParams = true;
				localvariablelist = new Vector<LocalVariable>();
				while (true)
				{
					getToken();
					if (token_type != TOKENTYPE.VARIABLE)
					{
						// wrong, we would have expected a variable name.
						// So this is no function assignment.
						hasCorrectParams = false;
						break;
					}

					// add this variable to the local variable list
					String var_name = token.toUpperCase();
					localvariablelist.add(new LocalVariable(var_name));

					getToken();
					if (token.equals(")"))
					{
						// closing parenthesis found
						hasCorrectParams = true;
						break;
					}

					if (!token.equals(","))
					{
						// wrong, we would have expected a comma.
						// So this is no function assignment.
						hasCorrectParams = false;
						break;
					}
				}

				if (hasCorrectParams)
				{
					getToken();

					if (token.equals("="))
					{
						// we have a function assignment like "f(a,b)=a*b"

						// assign a new function
						// note: the localvariablelinks will be copied and owned by
						// the assigned function. We do not need to delete the links
						getToken();
						FunctionAssign function = new FunctionAssign(functionlist, name, localvariablelist, parse_conditions());

						return function;
					}
				}

				// Function not assigned. Clear the created local variables
				localvariablelist.clear();

				// revert to previous token
				t = t_now;
				t_pos = t_pos_now;
				token_type = token_type_now;
				token = token_now;
			}
		}

		return parse_conditions();
	}

	/*
	 * conditional operators and bitshift
	 */
	private Function parse_conditions() throws Error
	{
		Function ans = parse_comparison();
		while (token.equals("&") || token.equals("|") || token.equals("<<") || token.equals(">>"))
		{
			if (token.equals("&"))
			{
				getToken();
				ans = new And(ans, parse_comparison());
			}
			else if (token.equals("|"))
			{
				getToken();
				ans = new Or(ans, parse_comparison());
			}
			else if (token.equals("<<"))
			{
				getToken();
				ans = new Bitshiftleft(ans, parse_comparison());
			}
			else if (token.equals(">>"))
			{
				getToken();
				ans = new Bitshiftright(ans, parse_comparison());
			}
		}

		return ans;
	}

	/*
	 * comparison operators
	 */
	private Function parse_comparison() throws Error
	{
		Function ans = parse_addsubtract();

		while (token.equals("=") || token.equals("<>") || token.equals("<") || token.equals(">") || token.equals("<=") || token.equals(">="))
		{
			if (token.equals("="))
			{
				getToken();
				ans = new Equal(ans, parse_addsubtract());
			}
			else if (token.equals("<>"))
			{
				getToken();
				ans = new Unequal(ans, parse_addsubtract());
			}
			else if (token.equals("<"))
			{
				getToken();
				ans = new Smaller(ans, parse_addsubtract());
			}
			else if (token.equals(">"))
			{
				getToken();
				ans = new Larger(ans, parse_addsubtract());
			}
			else if (token.equals("<="))
			{
				getToken();
				ans = new Smallereq(ans, parse_addsubtract());
			}
			else if (token.equals(">="))
			{
				getToken();
				ans = new Largereq(ans, parse_addsubtract());
			}
		}

		return ans;
	}

	/*
	 * add or subtract
	 */
	private Function parse_addsubtract() throws Error
	{
		Function ans = parse_multiplydivide();

		while (token.equals("+") || token.equals("-"))
		{
			if (token.equals("+"))
			{
				getToken();
				ans = new Add(ans, parse_multiplydivide());
			}
			else if (token.equals("-"))
			{
				getToken();
				ans = new Subtract(ans, parse_multiplydivide());
			}
		}

		return ans;
	}

	/*
	 * multiply, divide, modulus
	 */
	private Function parse_multiplydivide() throws Error
	{
		Function ans = parse_pow();

		while (token.equals("*") || token.equals("/") || token.equals("%") || token.equals("||"))
		{
			if (token.equals("*"))
			{
				getToken();
				ans = new Multiply(ans, parse_pow());
			}
			else if (token.equals("/"))
			{
				getToken();
				ans = new Divide(ans, parse_pow());
			}
			else if (token.equals("%"))
			{
				getToken();
				ans = new Modulus(ans, parse_pow());
			}
			else if (token.equals("||"))
			{
				getToken();
				ans = new Xor(ans, parse_pow());
			}
		}

		return ans;
	}

	/*
	 * power
	 */
	private Function parse_pow() throws Error
	{
		Function ans = parse_factorial();

		while (token.equals("^"))
		{
			getToken();
			ans = new Pow(ans, parse_factorial());
		}

		return ans;
	}

	/*
	 * Factorial
	 */
	private Function parse_factorial() throws Error
	{
		Function ans = parse_unaryminus();

		while (token.equals("!"))
		{
			getToken();
			ans = new Factorial(ans);
		}

		return ans;
	}

	/*
	 * Unary minus
	 */
	private Function parse_unaryminus() throws Error
	{
		if (token.equals("-"))
		{
			getToken();
			return new UnaryMinus(parse_function());
		}

		return parse_function();
	}

	/*
	 * functions
	 */
	private Function parse_function() throws Error
	{
		Function ans;

		if (token_type == TOKENTYPE.FUNCTION)
		{
			String fn_name = token.toUpperCase();

			getToken();

			// parse the parentheses and parameters of the function
			Vector<Function> values = new Vector<Function>();
			if (token.equals("("))
			{
				getToken();
				values.add(parse_conditions());

				// parse a list with parameters
				while (token.equals(","))
				{
					getToken();
					values.add(parse_conditions());
				}

				if (!token.equals(")")) { throw new Error(3, row(), col()); }
				getToken();

				// arithmetic functions
				if (fn_name.equals("ABS"))
					ans = new Abs();

				else if (fn_name.equals("CUBE"))
					ans = new Cube();
				else if (fn_name.equals("EXP"))
					ans = new Exp();
				else if (fn_name.equals("LOG"))
					ans = new Log();
				else if (fn_name.equals("LOG10"))
					ans = new Log10();
				else if (fn_name.equals("POW"))
					ans = new Pow();
				else if (fn_name.equals("SIGN"))
					ans = new Sign();
				else if (fn_name.equals("SQRT"))
					ans = new Sqrt();
				else if (fn_name.equals("SQUARE"))
					ans = new Square();

				// probabiliy functions
				else if (fn_name.equals("FACTORIAL"))
					ans = new Factorial();

				// statistical functions
				else if (fn_name.equals("AVG"))
					ans = new Avg();
				else if (fn_name.equals("MAX"))
					ans = new Max();
				else if (fn_name.equals("MEDIAN"))
					ans = new Median();
				else if (fn_name.equals("MIN"))
					ans = new Min();
				else if (fn_name.equals("PROD"))
					ans = new Prod();
				else if (fn_name.equals("STD"))
					ans = new Std();
				else if (fn_name.equals("SUM"))
					ans = new Sum();
				else if (fn_name.equals("VAR"))
					ans = new Var();

				// trigonometric functions
				else if (fn_name.equals("SIN"))
					ans = new Sin();
				else if (fn_name.equals("COS"))
					ans = new Cos();
				else if (fn_name.equals("TAN"))
					ans = new Tan();
				else if (fn_name.equals("ASIN"))
					ans = new Asin();
				else if (fn_name.equals("ACOS"))
					ans = new Acos();
				else if (fn_name.equals("ATAN"))
					ans = new Atan();
				else if (fn_name.equals("ATAN2"))
					ans = new Atan2();
				else if (functionlist.containsKey(fn_name))
				{
					// it is an assigned function
					ans = new FunctionGet(functionlist, fn_name);
				}
				else
				{
					// unknown function
					throw new Error(102, fn_name, row(), col());
				}

				// apply the parameter values to the found function
				ans.set(values);

				return ans;
			}
			else
			{
				throw new Error(3, row(), col());
			}
		}

		return parse_parentheses();
	}

	/*
	 * parentheses
	 */
	private Function parse_parentheses() throws Error
	{
		// check if it is a parenthesized expression
		if (token.equals("("))
		{
			getToken();
			Function ans = parse_conditions(); // start again

			if (!token.equals(")")) { throw new Error(3, row(), col()); }
			getToken();
			return ans;
		}

		// if not parenthesized then the expression is a value
		return parse_number();
	}

	/*
	 * parse a number
	 */
	private Function parse_number() throws Error
	{
		if (token_type == TOKENTYPE.NUMBER)
		{
			// this is a number
			Function ans;
			if (token.equals("."))
			{
				ans = new Value(0.0);
			}
			else
			{
				ans = new Value(Double.parseDouble(token));
			}
			getToken();

			return ans;
		}

		return parse_constant();
	}

	/*
	 * parse a constant or variable
	 */
	private Function parse_constant() throws Error
	{
		if (token_type == TOKENTYPE.VARIABLE)
		{
			// first make an uppercase copy of the variable name
			String const_name = token.toUpperCase();

			if (constants.containsKey(const_name))
			{
				// the constant is found
				getToken();
				return new Value(constants.get(const_name));
			}
		}

		return parse_variable();
	}

	/*
	 * parse a constant or variable
	 */
	private Function parse_variable() throws Error
	{
		Function ans;

		if (token_type == TOKENTYPE.VARIABLE)
		{
			// first make an uppercase copy of the variable name
			String var_name = token.toUpperCase();

			getToken();

			// check if this is a local variable
			boolean found = false;
			int i = 0;
			for (i = 0; i < localvariablelist.size(); i++)
			{
				if (localvariablelist.get(i).getName().equals(var_name))
				{

					found = true;
					break;
				}
			}

			if (found)
			{
				// create a link to this function variable
				ans = new LocalVariableGet(localvariablelist.get(i));
			}
			else
			{
				// create a normal variable object
				ans = new VariableGet(variablelist, var_name);
			}
		}
		else
		{
			// unexpected end of the expression... uh oh
			ans = parse_end();
		}

		return ans;
	}

	/*
	 * Evaluated when the expression is not yet ended but expected to end
	 */
	private Function parse_end() throws Error
	{
		if (StringUtils.isEmpty(token))
		{
			// syntax error or unexpected end of expression
			throw new Error(6, row(), col());
		}
		else
		{
			throw new Error(7, row(), col());
		}
	}

	/**
	 * Initialize the parser, load constants
	 */
	private void init()
	{
		constants.put("PI", 3.1415926535897932384626433832795);
		constants.put("E", 2.7182818284590452353602874713527);
	}

	/**
	 * Shortcut for getting the current row value (one based) Returns the line of the currently handled expression
	 */
	private int row()
	{
		return -1;
	}

	/**
	 * Shortcut for getting the current col value (one based) Returns the column (position) where the last token starts
	 */
	private int col()
	{
		return t_pos - token.length() + 1;
	}

	// enumerations
	private enum TOKENTYPE
	{
		NOTHING, DELIMETER, NUMBER, VARIABLE, FUNCTION, UNKNOWN
	};

	// data
	private String expr = ""; // holds the expression
	private int t_pos = 0; // points to the current position in expr
	private char t = '\0'; // holds the current token character in expr
	private String token = ""; // holds the token
	private TOKENTYPE token_type = TOKENTYPE.NOTHING; // type of the token

	private double ans; // holds the result of the expression
	private String ans_str; // holds a string containing the result
	private Integer errorId = null;
	// of the expression

	private Map<String, Double> constants = new HashMap<String, Double>(); // list with constants (like Pi and e)
	private Map<String, Double> variablelist = new HashMap<String, Double>(); // list with assigned variables (defined by the user)
	private Map<String, FunctionAssign> functionlist = new HashMap<String, FunctionAssign>();// list with assigned functions (defined by the user)
	private Vector<LocalVariable> localvariablelist = new Vector<LocalVariable>(); // list with local function variables (used during function assignment)
	public Integer getErrorId()
	{
		return errorId;
	}

	public void setErrorId(Integer errorId)
	{
		this.errorId = errorId;
	}
}
