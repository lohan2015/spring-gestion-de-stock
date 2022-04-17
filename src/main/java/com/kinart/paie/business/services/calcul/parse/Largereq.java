package com.kinart.paie.business.services.calcul.parse;


public class Largereq extends Function {
    /**
     * Constructor.
     */
	public Largereq() {
    }

    /**
     * Constructor.
     * This function requires two parameters and calculates Largereq(a,b)
     * or a >= b
     * @param a
     * @param b
     */
	public Largereq(Function a, Function b) {
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

        double a = values_.get(0).eval();
        double b = values_.get(1).eval();
        double largereq = (Double.compare(a, b) >= 0) ? 1.0 : 0.0;

        return (largereq);
    }

    /**
     * Return the name of the function
     */
    public String name() {
        return ">=";
    }
}
