package com.kinart.paie.business.services.calcul.parse;


public class Atan2 extends Function {
    /**
     * Constructor.
     */
	public Atan2() {
    }
    
    /**
     * Constructor.
     * This function requires two parameters and calculates Atan2(x,y)
     * @param x
     */    
	public Atan2(Function x, Function y) {
        set(x, y);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws java.lang.Error, Error {
        if (values_.size() != 2) {
            throw new java.lang.Error("8"); // wrong number of parameters
        }

        double x = values_.get(0).eval();
        double y = values_.get(1).eval();

        return Math.atan2(x, y);        
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "Atan2";
    }
}
