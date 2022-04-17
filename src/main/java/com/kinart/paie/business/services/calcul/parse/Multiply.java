package com.kinart.paie.business.services.calcul.parse;


public class Multiply extends Function {
    /**
     * Constructor.
     */
	public Multiply() {
    }
        
    /**
     * Constructor.
     * This function requires two parameters and calculates Multiply(a,b) or a*b
     * @param a
     * @param b
     */    
	public Multiply(Function a, Function b) {
        set(a, b);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws Error {
        if (values_.size() != 2) {
            throw new Error(8, name()); // wrong number of parameters
        }

        return values_.get(0).eval() * values_.get(1).eval();        
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "*";
    }
}
