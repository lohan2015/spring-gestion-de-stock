package com.kinart.paie.business.services.calcul.parse;


public class Sin extends Function {
    /**
     * Constructor.
     */
	public Sin() {
    }
    
    /**
     * Constructor.
     * This function requires one parameter and calculates Sin(x)
     * @param x
     */    
	public Sin(Function x) {
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

        return Math.sin(values_.get(0).eval());        
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "Sin";
    }
}
