package com.kinart.paie.business.services.calcul.parse;


public class Square extends Function {
    /**
     * Constructor.
     */
	public Square() {
    }
    
    /**
     * Constructor.
     * This function requires one parameter and calculates Square(x)
     * @param x
     */    
	public Square(Function x) {
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

        double x = values_.get(0).eval();
        return x * x;        
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "Square";
    }
}
