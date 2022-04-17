package com.kinart.paie.business.services.calcul.parse;


public class Smallereq extends Function {
    /**
     * Constructor.
     */
	public Smallereq() {
    }

    /**
     * Constructor.
     * This function requires two parameters and calculates Smallereq(a,b)
     * or a <= b
     * @param a
     * @param b
     */
	public Smallereq(Function a, Function b) {
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
        double smallereq = (Double.compare(a, b) <= 0) ? 1.0 : 0.0;

        return (smallereq);
    }

    /**
     * Return the name of the function
     */
    public String name() {
        return "<=";
    }
}
