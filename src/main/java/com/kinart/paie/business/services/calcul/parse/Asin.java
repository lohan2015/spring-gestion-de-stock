package com.kinart.paie.business.services.calcul.parse;


public class Asin extends Function {
    /**
     * Constructor.
     */
	public Asin() {
    }
    
    /**
     * Constructor.
     * This function requires one parameter and calculates Asin(x)
     * @param x
     */    
	public Asin(Function x) {
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

        return Math.asin(values_.get(0).eval());        
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "Asin";
    }
}
