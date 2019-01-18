package org.thane.utils.scripts;

import javax.script.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSExpression implements InvokableJS {

    private static final Pattern UNKNOWN_VARIABLE_PATTERN = Pattern.compile(".*?\"(.*?)\"\\s+is\\s+not\\s+defined.*?");

    private Map<String, Object> variables = new HashMap<>();
    private CompiledScript compiledScript;
    private final String script;

    public JSExpression(String expression, String... variableNames) {
        for (String string : variableNames) {
            variables.put(string, null);
        }
        this.script = expression;
        while (true) {
            try {
                ScriptContext context = new SimpleScriptContext();
                for (Map.Entry<String, Object> entry : variables.entrySet()) {
                    context.setAttribute(entry.getKey(), entry.getValue(), ScriptContext.ENGINE_SCOPE);
                }
                JSFunction.ENGINE.eval(expression, context);
                compiledScript = ((Compilable) JSFunction.ENGINE).compile(expression);
                break;
            } catch (ScriptException e) {
                Matcher matcher = UNKNOWN_VARIABLE_PATTERN.matcher(e.getMessage());
                if (matcher.matches()) {
                    variables.put(matcher.group(1), null);
                }
            }
        }
    }

    public void resetVariables() {
        variables.keySet().forEach(s -> variables.put(s, null));
    }

    public void setVariables(Map<String, Object> variables) {
        this.variables = variables;
    }

    public void setVariable(String name, Object value) {
        variables.put(name, value);
    }

    public Set<String> getUnsetVariables() {
        Set<String> unset = new HashSet<>();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            if (entry.getValue() == null) {
                unset.add(entry.getKey());
            }
        }
        return unset;
    }

    public void removeVariable(String name) {
        variables.remove(name);
    }

    public void addVariable(String name) {
        variables.put(name, null);
    }

    public Object invoke() {
        return invoke(true);
    }

    public Object invoke(boolean resetValues) {
        ScriptContext context = new SimpleScriptContext();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            context.setAttribute(entry.getKey(), entry.getValue(), ScriptContext.ENGINE_SCOPE);
        }
        Object response = null;
        try {
            response = compiledScript.eval(context);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        if (resetValues) resetVariables();
        return response;
    }

    public static boolean isExpression(String script) {
        return !JSFunction.FUNCTION_PATTERN.matcher(script).matches();
    }

    @Override
    public String toString() {
        return script;
    }
}
