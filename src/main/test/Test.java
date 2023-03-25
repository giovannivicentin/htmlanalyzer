package main.test;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {
    public static void main(String[] args) {
        // check if the program is run with a URL argument
        if (args.length == 0) {
            // if not, print an error message to the standard error stream
            System.err.println("Please provide a URL as an argument.");
            return;
        }

        String urlString = args[0];

        try {
            URL url = new URL(urlString);
            // open a connection to the URL and cast the resulting object to an HttpURLConnection object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // read the HTML content from the connection input
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String html = "";
            // loop through each line of the HTML content and append it to the String
            while ((line = reader.readLine()) != null) {
                html += line + "\n";
            }
            reader.close();

            // create two stacks to keep track of the tags and their depths
            Stack<String> tagStack = new Stack<>();
            Stack<Integer> depthStack = new Stack<>();
            int maxDepth = 0;
            String maxTag = "";
            String maxTagContent = "";

            // define two regular expressions to match opening and closing tags
            Pattern openTagPattern = Pattern.compile("^\\s*<([^/][^>]*?)>");
            Pattern closeTagPattern = Pattern.compile("^\\s*</(.*?)>");
            Matcher matcher;

            // loop through each line of the html
            for (String htmlLine : html.split("\n")) {
                matcher = openTagPattern.matcher(htmlLine);
                if (matcher.find()) {
                    // get the tag name and current depth
                    String tag = matcher.group(1);
                    int depth = tagStack.size();
                    // check if the tag is not in the stack or is a new occurrence at a greater depth
                    if (!tagStack.contains(tag) || depthStack.indexOf(depth) > tagStack.lastIndexOf(tag)) {
                        // add the tag and depth to the stacks and update the max tag and depth if necessary
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
                
                // Append content to maxTagContent if the current tag is the max depth tag
                if (!tagStack.empty() && tagStack.peek().equals(maxTag)) {
                    maxTagContent += htmlLine.replaceAll("\\<.*?>", "") + "\n";
                }
            }

            // print the max tag and max tag content
            System.out.println("Tag: " + maxTag);
            System.out.println("Content: " + maxTagContent.trim());
        } catch (IOException e) {
            // print an error message if there is a problem with the URL connection
            System.err.println("URL connection error");
        } catch (PatternSyntaxException e) {
            // print an error message if the HTML is malformed and cannot be parsed
            System.err.println("Malformed HTML");
        }
    }
}
