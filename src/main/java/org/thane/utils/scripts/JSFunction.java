package org.thane.utils.scripts;

import org.bukkit.Bukkit;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class JSFunction implements InvokableJS {

    static final Pattern FUNCTION_PATTERN = Pattern.compile(".*?function\\s+(.*?)\\((.*?)\\)\\s*\\{.*?", Pattern.MULTILINE | Pattern.DOTALL);

    static final ScriptEngine ENGINE = new ScriptEngineManager().getEngineByName("js");

    private final String functionName;
    private final String script;
    private final List<String> parameters;

    public JSFunction(String script) {
        try {
            ENGINE.eval(script);
        } catch (ScriptException e) {
            Bukkit.getLogger().severe("The following javascript config file could not load!\n\n"
                    + script + "\n\nPlugin functions are unlikely to work as intended.");
        }
        this.script = script;
        Matcher matcher = FUNCTION_PATTERN.matcher(script);
        if (matcher.matches()) {
            functionName = matcher.group(1);
            parameters = Arrays.stream(matcher.group(2).split(",")).map(String::trim).collect(Collectors.toList());
        } else {
            functionName = null;
            parameters = null;
        }
    }

    public JSFunction(File jsFile) {
        StringBuilder script = new StringBuilder();
        try {
            Files.lines(jsFile.toPath()).forEach(s -> script.append(s).append('\n'));
            ENGINE.eval(new FileReader(jsFile));
        } catch (IOException | ScriptException e) {
            e.printStackTrace();
        }
        this.script = script.toString();
        Matcher matcher = FUNCTION_PATTERN.matcher(this.script);
        if (matcher.matches()) {
            parameters = Arrays.stream(matcher.group(2).split(",")).map(String::trim).collect(Collectors.toList());
        } else parameters = null;
        functionName = jsFile.getName().replace(".js", "").trim();
    }

    public Object invoke(LinkedHashMap<String, Object> variables) {
        return invoke(variables.values().toArray(new Object[0]));
    }

    public Object invoke(Object... arguments) {
        try {
            return ((Invocable) ENGINE).invokeFunction(functionName, arguments);
        } catch (ScriptException e) {
            StringBuilder builder = new StringBuilder("Function " + functionName + " could not accept arguments\n");
            for (Object object : arguments) {
                builder.append(object.getClass().getName()).append(": ").append(object.toString()).append(",\n");
            }
            Bukkit.getLogger().severe(builder.toString());
        } catch (NoSuchMethodException e) {
            Bukkit.getLogger().severe("Function " + functionName + " has not been registered within the engine!");
        }
        return null;
    }

    public String getFunctionName() {
        return functionName;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public static boolean isFunction(String script) {
        return FUNCTION_PATTERN.matcher(script).matches();
    }

    public static String getFunctionName(String script) {
        Matcher matcher = FUNCTION_PATTERN.matcher(script);
        if (matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }

    public static List<String> getFunctionParameters(String script) {
        Matcher matcher = FUNCTION_PATTERN.matcher(script);
        if (matcher.matches()) {
            return Arrays.stream(script.split(",")).map(String::trim).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public String toString() {
        return script;
    }
}
