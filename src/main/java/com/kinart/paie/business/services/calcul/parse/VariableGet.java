package com.kinart.paie.business.services.calcul.parse;

import java.util.HashMap;
import java.util.Map;


public class VariableGet extends Function {
    /**
     * Constructor
     */    
    VariableGet(Map<String, Double> variablelist) {
        variablelist_ = variablelist;        
    }
    
    /**
     * Constructor
     */    
    public VariableGet(Map<String, Double> variablelist, final String name) {
        variablelist_ = variablelist; 
        setName(name);       
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

    public double eval() throws java.lang.Error {
        // check if this variable exists in the variablelist
        if (!variablelist_.containsKey(name_)) {
            throw new java.lang.Error("103");
        }

        // return the value of the variable
        return variablelist_.get(name_);     
    }
    
    public final String name() {
        return "VariableGet";
    }

    // variablelist_ points to the list with variable assignments
    private Map<String, Double> variablelist_ = 
        new HashMap<String, Double>();  
    private String name_;    // variable name
}
