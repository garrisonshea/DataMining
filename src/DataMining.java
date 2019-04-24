//*********************************************************
// Purpose of program:
//
// We are given a starting page and a target page.
// We want to find pages on the starting page
// and print pages that are similar to the target page

// We decide what pages to print by sketching them
// This is a vector that computes similarity
// We get a double from [0, 1] and that is compared to the threshold value
// Print if greater than the threshold ===> pass our test of similarity

// this is done recursively

public class DataMining {
    public static void surf(String startURL, Sketch targetSketch, double threshold) {
        //this determines how deep we traverse into sub pages
        //read all the HTML for the start page
        In start = new In(startURL);
        String startHTML = start.readAll();
        start.close();

        // print the start URL if it's close to the target
        Sketch startSketch = new Sketch(startHTML, 5, 10000);
        if (startSketch.similarTo(targetSketch) > threshold) {
            StdOut.println(startURL);
        }

        //need to find all links in the HTML text
        //search for http://
        int n = 0;
        while (n < startHTML.length()) {
            int linkStart = startHTML.indexOf("https://", n);
            if (linkStart == -1) {
                break;
            }
            int linkEnd = startHTML.indexOf("\"", linkStart);
            if (linkStart > n)
                n = linkEnd;
            else {
                n++;
            }

            // this block avoids errors while parsing the HTML
            // links can end with either " or <, so we accomodate for both
            // Input stream declared outside of try, catch block so we can access it
            In link = null;
            String linkHTML = null;
            try {
                link = new In(startHTML.substring(linkStart, linkEnd));
                linkHTML = link.readAll();
                link.close();
            } catch (Exception e) {
                linkEnd = startHTML.indexOf("<", linkStart);
            }
            if (linkHTML == null) {
                try {
                    link = new In(startHTML.substring(linkStart, linkEnd));
                    linkHTML = link.readAll();
                    link.close();
                } catch (Exception e) {
                    continue;
                }
            }


            // try {
            // 	link = new In(startHTML.substring(linkStart, linkEnd));
            // } catch (Exception e) {
            // 	linkEnd = startHTML.indexOf("<", linkStart);
            // 	try {
            // 		link = new In(startHTML.substring(linkStart, linkEnd));
            // 	} catch (Exception e2) {
            // 		continue;
            // 	}
            // }

            // String linkHTML = link.readAll();
            // link.close();

            if (linkHTML.length() == 0)
                continue;
            Sketch linkSketch = new Sketch(linkHTML, 5, 10000);
            if (linkSketch.similarTo(targetSketch) > threshold) {
                String linkURL = startHTML.substring(linkStart, linkEnd);
                StdOut.println(linkURL);
                // surf(linkURL, targetSketch, threshold, depth - 1);
            }
        }
    }

    public static void main(String[] args) {
        //3 command line arguments
        //startingURL, targetURL, threshold
        //read in the beginning web site, target, threshold
        String startURL = args[0];
        String targetURL = args[1];

        //read all HTML for the target page
        In target = new In(targetURL);
        String targetHTML = target.readAll();
        target.close();

        Sketch targetSketch = new Sketch(targetHTML, 5, 10000);

        //call the method using the url
        surf(startURL, targetSketch, Double.parseDouble(args[2]));
    }
}