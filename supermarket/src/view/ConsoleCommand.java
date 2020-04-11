package view;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ConsoleCommand {
    EXIT("(?i)exit"),
    ADD_GOOD("(?i)add\\s+(countable|uncountable)\\s+good\\s+(\\w+)"),
    NEW_ORDER("(?i)new\\s+order\\s+from\\s+(\\w+)"),
    GOODS_LIST("(?i)goods\\s+list"),
    TOTAL_SALES("(?i)total\\s+sales(\\s+--(credit|cash))?"),
    TOTAL_PROFIT("(?i)total\\s+profit");

    private final Pattern commandPattern;

    public Matcher getStringMatcher(String input) {
        return this.commandPattern.matcher(input);
    }

    ConsoleCommand(String commandPatternString) {
        this.commandPattern = Pattern.compile(commandPatternString);
    }
}
