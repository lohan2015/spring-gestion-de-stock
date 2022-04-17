package com.kinart.paie.business.services.calcul.parse;



public class Acos extends Function {
    /**
     * Constructor.
     */
	public Acos() {
    }
    
    /**
     * Constructor.
     * This function requires one parameter and calculates Acos(x)
     * @param x
     */    
	public Acos(Function x) {
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

        return Math.acos(values_.get(0).eval());        
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "Acos";
    }
}
