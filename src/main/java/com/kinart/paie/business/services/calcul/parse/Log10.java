package com.kinart.paie.business.services.calcul.parse;


public class Log10 extends Function {
    /**
     * Constructor.
     */
	public Log10() {
    }
    
    /**
     * Constructor.
     * This function requires one parameter and calculates Log10(x)
     * @param x
     */    
	public Log10(Function x) {
        set(x);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws java.lang.Error, Error {
        if (values_.size() != 1) {
            throw new java.lang.Error("8"); // wrong number of parameters
        }

        return Math.log10(values_.get(0).eval());        
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "Log10";
    }
}
