package com.kinart.paie.business.services.calcul.parse;


public class Subtract extends Function {
    /**
     * Constructor.
     */
	public Subtract() {
    }
    
    /**
     * Constructor.
     * This function requires two parameters and calculates Subtract(a,b) or a-b
     * @param a
     * @param b
     */    
	public Subtract(Function a, Function b) {
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

        return values_.get(0).eval() - values_.get(1).eval();        
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "-";
    }
}
