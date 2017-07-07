//Micky Panleartkitsakul
//260673374

import java.util.*;
import java.io.*;

// This class implements a google-like search engine
public class SearchEngine {

    public HashMap<String, LinkedList<String> > wordIndex;                  // this will contain a set of pairs (String, LinkedList of Strings) 
    public DirectedGraph internet;             // this is our internet graph
    
    
    
    // Constructor initializes everything to empty data suctures
    // It also sets the location of the internet files
    SearchEngine() {
 // Below is the directory that contains all the internet files
 HtmlParsing.internetFilesLocation = "internetFiles";
 wordIndex = new HashMap<String, LinkedList<String> > ();  
 internet = new DirectedGraph();    
    } // end of constructor2015
    
    
    // Returns a String description of a searchEngine
    public String toString () {
 return "wordIndex:\n" + wordIndex + "\ninternet:\n" + internet;
    }
    
    
    // This does a graph traversal of the internet, starting at the given url.
    // For each new vertex seen, it updates the wordIndex, the internet graph,
    // and the set of visited vertices.
    
    void traverseInternet(String url) throws Exception {
      /* WRITE SOME CODE HERE */
 
 /* Hints
    0) This should take about 50-70 lines of code (or less)
    1) To parse the content of the url, call
    htmlParsing.getContent(url), which returns a LinkedList of Strings 
    containing all the words at the given url. Also call htmlParsing.getLinks(url).
    and assign their results to a LinkedList of Strings.
    2) To iterate over all elements of a LinkedList, use an Iterator,
    as described in the text of the assignment
    3) Refer to the description of the LinkedList methods at
    http://docs.oracle.com/javase/6/docs/api/ .
    You will most likely need to use the methods contains(String s), 
    addLast(String s), iterator()
    4) Refer to the description of the HashMap methods at
    http://docs.oracle.com/javase/6/docs/api/ .
    You will most likely need to use the methods containsKey(String s), 
    get(String s), put(String s, LinkedList l).  
 */
     internet.addVertex(url);
     internet.setVisited(url,  true);
     LinkedList<String> neighbors = HtmlParsing.getLinks(url);
     LinkedList<String> content = HtmlParsing.getContent(url);
     Iterator<String> iter = content.iterator();
     
     // Add every word in content to wordIndex
     while(iter.hasNext()) {
      String s = iter.next();
      // If wordIndex contains the word
      if (wordIndex.containsKey(s)) {
       if (!(wordIndex.get(s)).contains(url)) {
        (wordIndex.get(s)).addLast(url);
       }
      }
      // If wordIndex does not contain the word
      else {
       LinkedList<String> link = new LinkedList<String>();
       link.addLast(url);
       wordIndex.put(s, link);
      }
     }
     
     iter = neighbors.iterator();
     while(iter.hasNext()) {
      String s = iter.next();
      internet.addEdge(url, s);
      if (!(internet.getVisited(s)))
       traverseInternet(s);
  }
 } // end of traverseInternet
    
    
    /* This computes the pageRanks for every vertex in the internet graph.
       It will only be called after the internet graph has been constructed using 
       traverseInternet.
       Use the iterative procedure described in the text of the assignment to
       compute the pageRanks for every vertices in the graph. 
       
       This method will probably fit in about 30 lines.
    */
    // WRITE YOUR CODE HERE
    void computePageRanks() {
     LinkedList<String> v = internet.getVertices();
     Iterator<String> iter = v.iterator();
     while (iter.hasNext()) {
      internet.setPageRank(iter.next(), 1);
     }
     
     for (int i = 0; i < 100; i++) {
      iter = v.iterator();
      while (iter.hasNext()) {
       double pr = 0.5;
       String s = iter.next();
       Iterator<String> ref = internet.getEdgesInto(s).iterator();
       while (ref.hasNext()) {
        String t = ref.next();
        pr += 0.5 * (internet.getPageRank(t)/internet.getOutDegree(t));
       }
       internet.setPageRank(s, pr);
      }
      
     }
 
    } // end of computePageRanks
    
 
    /* Returns the URL of the page with the high page-rank containing the query word
       Returns the String "" if no web site contains the query.
       This method can only be called after the computePageRanks method has been executed.
       Start by obtaining the list of URLs containing the query word. Then return the URL 
       with the highest pageRank.
       This method should take about 25 lines of code.
    */
    String getBestURL(String query) {
        LinkedList<String> sites;
        query = query.toLowerCase();
        if (wordIndex.containsKey(query))
         sites = wordIndex.get(query);
        else
         return new String("");
        Iterator<String> iter = sites.iterator();
        double bestPR = -1;
        String best = "";
        while (iter.hasNext()) {
            String s = iter.next();
            double value = internet.getPageRank(s);
            //System.out.println(s + " " + score);
            if (bestPR < value) {
                bestPR = value;
                best = s + "\tPage Rank: " + bestPR;
                }
        }
        return best;
    } // end of getBestURL
    
 
    public static void main(String args[]) throws Exception{  
 SearchEngine mySearchEngine = new SearchEngine();
 // to debug your program, start with.
 // mySearchEngine.traverseInternet("http://www.cs.mcgill.ca/~blanchem/250/a.html");
 
 // When your program is working on the small example, move on to
 mySearchEngine.traverseInternet("http://www.cs.mcgill.ca");
 

 mySearchEngine.computePageRanks();
 
 BufferedReader stndin = new BufferedReader(new InputStreamReader(System.in));
 String query;
 do {
     System.out.print("Enter query: ");
     query = stndin.readLine();
     if ( query != null && query.length() > 0 ) {
  System.out.println("Best site = " + mySearchEngine.getBestURL(query));
     }
 } while (query!=null && query.length()>0);    
    } // end of main
}