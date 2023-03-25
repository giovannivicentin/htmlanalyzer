package main.java;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class HtmlParser {
    private String html;
    private String maxTag;
    private String maxTagContent;

    public HtmlParser(String html) {
        // initialize the html instance variable with the input html
        this.html = html;
        // parse the html to get the max tag and max tag content
        parseHtml();
    }

    // private method to parse the html and get the max tag and max tag content
    private void parseHtml() {
        // keep track of the tags and their depths
        Stack<String> tagStack = new Stack<>();
        Stack<Integer> depthStack = new Stack<>();
        // initialize the max depth to 0 and the max tag and max tag to empty strings
        int maxDepth = 0;
        maxTag = "";
        maxTagContent = "";

        // define two regular expressions to match opening and closing tags
        Pattern openTagPattern = Pattern.compile("^\\s*<([^/][^>]*?)>");
        Pattern closeTagPattern = Pattern.compile("^\\s*</(.*?)>");
        Matcher matcher;

        try {
            // loop through each line of the html
            for (String htmlLine : html.split("\n")) {
                matcher = openTagPattern.matcher(htmlLine);
                if (matcher.find()) {
                    // get the tag name and current depth
                    String tag = matcher.group(1);
                    int depth = tagStack.size();
                    // check if the tag is not in the stack or is a new occurrence at a greater depth
                    if (!tagStack.contains(tag) || depthStack.indexOf(depth) > tagStack.lastIndexOf(tag)) {
                        // add the tag and depth to the stacks and update the max tag
                        tagStack.push(tag);
                        depthStack.push(depth);
                        if (depth > maxDepth) {
                            maxDepth = depth;
                            maxTag = tag;
                         // Reset maxTagContent when a new max depth tag is found
                            maxTagContent = "";
                        }
                    }
                } else {
                    matcher = closeTagPattern.matcher(htmlLine);
                    if (matcher.find()) {
                        // pop the tag and depth from the stacks if the closing tag matches the top tag in the stack
                        String tag = matcher.group(1);
                        if (!tagStack.empty() && tagStack.peek().equals(tag)) {
                            tagStack.pop();
                            depthStack.pop();
                        }
                    }
                }

                // append the content to maxTagContent if the current tag is the max depth tag
                if (!tagStack.empty() && tagStack.peek().equals(maxTag)) {
                    maxTagContent += htmlLine.replaceAll("\\<.*?>", "") + "\n";
                }
            }
        } catch (PatternSyntaxException e) {
            // print an error message if the html is malformed
            System.err.println("Malformed HTML");
        }
    }
    public String getMaxTag() {
        return maxTag;
    }
    public String getMaxTagContent() {
        return maxTagContent.trim();
    }
}
