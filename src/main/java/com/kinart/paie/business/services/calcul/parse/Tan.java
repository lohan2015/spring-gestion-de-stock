package com.kinart.paie.business.services.calcul.parse;


public class Tan extends Function {
    /**
     * Constructor.
     */
	public Tan() {
    }
    
    /**
     * Constructor.
     * This function requires one parameter and calculates Tan(x)
     * @param x
     */    
	public Tan(Function x) {
        set(x);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws Error {
        if (values_.size() != 1) {
            throw new Error(8, name()); // wrong number of parameters
        }

        return Math.tan(values_.get(0).eval());        
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "Tan";
    }
}
