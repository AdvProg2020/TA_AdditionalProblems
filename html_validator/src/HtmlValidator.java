import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlValidator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Pattern pattern = Pattern.compile("<\\/?(\\w+)(\\s+\\w+=\"[^\"]+\")*\\s*\\/?>");
        StringBuilder htmlCode = new StringBuilder();
        while (true) {
            String inputLine = scanner.nextLine().trim();
            htmlCode.append(inputLine);
            if (inputLine.equals("</html>"))
                break;
        }
        String code = htmlCode.toString();
        Matcher matcher = pattern.matcher(code);

        Stack<String> tagStack = new Stack<>();

        while (matcher.find()) {
            String fullMatch = matcher.group(0);
            String tagName = matcher.group(1);
            if (fullMatch.charAt(1) == '/') {
                String lastTag = tagStack.pop();
                if (!lastTag.equals(tagName)) {
                    System.out.format("Last Tag: %s\t Closed Tag: %s\n", lastTag, tagName);
                    System.out.println("NO");
                    return;
                }
            } else if (fullMatch.charAt(fullMatch.length() - 2) != '/') {
                tagStack.push(tagName);
            }
        }

        if (tagStack.size() == 0)
            System.out.println("YES");
        else
            System.out.println("NO");

    }
}
