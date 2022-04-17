package com.kinart.paie.business.services.calcul.parse;

import java.util.Arrays;
import java.util.Vector;

public class Median extends Function {
    /**
     * Constructor.
     */
	public Median() {
    }

    /**
     * Constructor.
     * This function requires one or more parameters and
     * calculates Median(a,b,c,...)
     * @param values   A vector with values
     */
	public Median(Vector<Function> values) {
        set(values);
    }

    /**
     * Evaluate the function
     * @return    The result of the function
     */
    public double eval() throws Error {
        if (values_.size() == 0) {
            throw new Error(8, name()); // wrong number of parameters
        }

        double ans = 0.0;

        // evaluate all values
        double[] v = new double[values_.size()];
        for (int i = 0; i < values_.size(); i++) {
            v[i] = values_.get(i).eval();
        }

        // sort the values
        Arrays.sort(v);

        if (values_.size() % 2 == 0) {
            // even number of values. return the average of the middle values
            double m1 = v[values_.size() / 2  - 1];
            double m2 = v[values_.size() / 2];
            ans = (m1 + m2) / 2.0;
        } else {
            // odd number of values. return the middle value
            ans = v[(values_.size() - 1) / 2];
        }

        return ans;
    }

    /**
     * Return the name of the function
     */
    public String name() {
        return "Median";
    }
}
