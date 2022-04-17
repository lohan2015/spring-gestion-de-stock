package com.kinart.paie.business.services.calcul.parse;

import java.util.HashMap;
import java.util.Map;


public class VariableAssign extends Function {
    /**
     * Constructor
     */    
	public VariableAssign(Map<String, Double> variablelist) {
        variablelist_ = variablelist;        
    }
    
    /**
     * Constructor
     */    
	public VariableAssign(Map<String, Double> variablelist, 
                final String name,
                Function value) {
        variablelist_ = variablelist; 
        setName(name);       
        set(value);
    }

    /**
     * Set a variable name
     */
    public void setName(final String name) {
        clear();
        name_ = name;         
    }
    
    /**
     * Get the variable name
     */    
    public final String getName() {
        return name_;        
    }

    /**
     * Clear all settings: name, values
     */    
    public void clear() {
        name_ = "";
    	values_.clear();
    }
    
    public double eval() throws java.lang.Error, Error {
        if (values_.size() != 1) {
            throw new java.lang.Error("403"); // uninitialized value
        }

        // evaluate the variable 
        double value = values_.get(0).eval();

        // assign the new value
        variablelist_.put(name_, value); 
        
        return value;
    }
    
    public final String name() {
        return "VariableAssign";
    }

    // variablelist_ points to the list with variable assignments
    private Map<String, Double> variablelist_ = 
        new HashMap<String, Double>();  
    private String name_;    // variable name
}
