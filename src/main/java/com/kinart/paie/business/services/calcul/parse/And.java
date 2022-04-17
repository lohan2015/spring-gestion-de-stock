package com.kinart.paie.business.services.calcul.parse;


public class And extends Function {
    /**
     * Constructor.
     */
	public And() {
    }
        
    /**
     * Constructor.
     * This function requires two parameters and calculates And(a,b) or a&b,
     * @param a
     * @param b
     */    
	public And(Function a, Function b) {
        set(a, b);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws java.lang.Error, Error {
        if (values_.size() != 2) {
            throw new java.lang.Error("8"); // wrong number of parameters
        }

        int a = (int)values_.get(0).eval();
        int b = (int)values_.get(1).eval();

        return a & b;
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "&";
    }
}
