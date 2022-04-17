package com.kinart.paie.business.services.calcul.parse;

import java.util.HashMap;
import java.util.Map;


public class FunctionGet extends Function {
    /**
     * Constructor
     */
    public FunctionGet( Map<String, FunctionAssign> functionlist) {
        functionlist_ = functionlist;
    }

    /**
     * Constructor
     */
    public FunctionGet( Map<String, FunctionAssign> functionlist, 
                        final String name) {
        functionlist_ = functionlist;
        setName(name);
    }

    /**
     * Set the name of the function
     */
    public void setName(final String name) {
        clear();
        name_ = name;
    }

    /**
     * Clear all settings: name, values
     */    
    public void clear() {
        name_ = "";
    	values_.clear();
    }
    
    /**
     * Get the name of the function
     */    
    public String getName() {
        return name_;
    }

    /**
     * Evaluate the Function
     */
    public double eval() throws java.lang.Error, Error {
        // check if the function exists in the functionlist
        if (!functionlist_.containsKey(name_)) {
            throw new java.lang.Error("102");
        }

        // evaluate the function, and provide the parameter values for the 
        // function
        return functionlist_.get(name_).eval(values_);        
    }
    
    /**
     * Get the name of the class
     */
    public String name() {
        return "FunctionGet";
    }

    // functionlist_ points to the list with function assignments
    private Map<String, FunctionAssign> functionlist_ = 
        new HashMap<String, FunctionAssign>();  
    private String name_;  // function name
};
