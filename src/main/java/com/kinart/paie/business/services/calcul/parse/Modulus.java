package com.kinart.paie.business.services.calcul.parse;


public class Modulus extends Function {
    /**
     * Constructor.
     */
	public Modulus() {
    }
        
    /**
     * Constructor.
     * This function requires two parameters and calculates Modulus(a,b) or a%b,
     * the reminder of a division.
     * @param a
     * @param b
     */    
	public Modulus(Function a, Function b) {
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

        double a = values_.get(0).eval();
        double b = values_.get(1).eval();
        
        // values must be integer
        int a_int = (int)a;
        int b_int = (int)b;
        if ((double)a_int != a || (double)b_int != b) {
          throw new java.lang.Error("400");
        }
        // TODO: change into a warning?

        return a_int % b_int;
    }
    
    /**
     * Return the name of the function
     */
    public String name() {
        return "%";
    }
}
