package com.kinart.paie.business.services.calcul.parse;


public class Atan extends Function {
    /**
     * Constructor.
     */
	public Atan() {
    }
    
    /**
     * Constructor.
     * This function requires one parameter and calculates Atan(x)
     * @param x
     */    
	public Atan(Function x) {
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

        return Math.atan(values_.get(0).eval());        
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "Atan";
    }
}
