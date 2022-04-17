package com.kinart.paie.business.services.calcul.parse;


public class Pow extends Function {
    /**
     * Constructor.
     */
	public Pow() {
    }
        
    /**
     * Constructor.
     * This function requires two parameters and calculates Pow(a,b) or a^b
     * @param a
     * @param b
     */    
	public Pow(Function a, Function b) {
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

        return Math.pow(values_.get(0).eval(), values_.get(1).eval());        
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "^";
    }
}
