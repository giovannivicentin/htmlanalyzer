package main.java;

public class HtmlAnalyzer {
    public static void main(String[] args) {
        // check if the program is run with a URL argument
        if (args.length == 0) {
            // if not, print an error message
            System.err.println("Please provide a URL as an argument.");
            return;
        }
        // retrieve the URL argument from the command line
        String urlString = args[0];
        // create a new WebPageReader object with the URL
        WebPageReader webPageReader = new WebPageReader(urlString);
        // get the HTML content of the web page
        String html = webPageReader.getHtmlContent();
        
        // check if the HTML content is not null
        if (html != null) {
            // create a new HtmlParser object with the HTML content
            HtmlParser htmlParser = new HtmlParser(html);
            // print the maximum tag in the HTML and its content
            System.out.println("Tag: " + htmlParser.getMaxTag());
            System.out.println("Content: " + htmlParser.getMaxTagContent());
        }
    }
}
