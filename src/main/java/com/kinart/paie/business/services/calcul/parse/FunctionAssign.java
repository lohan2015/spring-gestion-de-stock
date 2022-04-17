package com.kinart.paie.business.services.calcul.parse;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/// Function assignment
public class FunctionAssign extends Function {
    /**
     * Constructor
     */
    public FunctionAssign(Map<String, FunctionAssign> functionlist) {
        functionlist_ = functionlist;        
    }
    
    /**
     * Constructor
     */    
    public FunctionAssign(Map<String, FunctionAssign> functionlist,
                          final String name, 
                          Vector<LocalVariable> variables, 
                          Function expr) {
        functionlist_ = functionlist;
        set(name, variables, expr);
    }
    
    /**
     * Clear all settings: name, variables, expression
     */    
    public void clear() {
        name_ = "";
        variables_.clear();
    	values_.clear();
    }

    /**
     * Set function name, variables, and expression
     * For example the function f(x,y)=x^y+2 whould be inserted as:
     * @param name          A string with the function name, for example "f"
     * @param variables     A vector containing the local variables x and y
     * @param expr          The function expression, like x^y+2
     */
    public void set(final String name, 
                    Vector<LocalVariable> variables, 
                    Function expr) {
        set(expr); // must be executed first, this clears the function too.
        setName(name);
        setVariables(variables);
    }

    /**
     * Set the name of the function
     */
    public void setName(final String name) {
        name_ = name;
    }
    
    /**
     * Returns the name of the function
     */
    public String getName() {
        return name_;
    }

    /**
     * Set function variables
     */     
    public void setVariables(Vector<LocalVariable> variables) {
        variables_ = variables;
    }
    
    /**
     * Get function variables
     */     
    public Vector<LocalVariable> getVariables() {
        return variables_;
    }

    /**
     * Evaluate the function assignment. This will register the function
     * in the functionlist
     */
    public double eval() {
        functionlist_.put(name_, this);
        
        // TODO: return a message?
        return 0.0;
    }
    
    /**
     * Evaluate the function for the provided parameter values.
     * @param values    The parameter values that will be filled in as
     *                  local variables.
     */    
    public double eval(Vector<Function> values) throws java.lang.Error, Error {
        if (values_.size() != 1) {
            // expression of the function is undefined
            throw new java.lang.Error("301");
        }
    
        if (values.size() != variables_.size())
        {
            // wrong number of parameter values provided
            throw new java.lang.Error("8");
        }
    
        // link the function variables to the provided parameters
        for (int i = 0; i < variables_.size(); i++)
        {
            variables_.get(i).set(values.get(i));
        }
    
        // evaluate the function
        return values_.get(0).eval();        
    }
    
    /**
     * Returns the name of the class
     */
    public String name() {
        return "FunctionAssign";
    }

    // functionlist_ points to the list with function assignments
    private Map<String, FunctionAssign> functionlist_ = 
        new HashMap<String, FunctionAssign>();
    private String name_;
    private Vector<LocalVariable> variables_ = new Vector<LocalVariable>();
}
