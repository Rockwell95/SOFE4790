import java.util.*;
import java.io.*;
import java.util.regex.*;

class Line {
    String content;
    long lineNumber;
    public Line(String content, long lineNumber) {
        this.content = content;
        this.lineNumber = lineNumber;
    }
    public boolean isEnd() {
        return lineNumber < 0;
    }
    public static Line END() {
        return new Line(null, -1);
    }
}

class FileIterator implements Iterable<Line> {
    public String filename;
    public int repeat;

    public FileIterator(String filename, int repeat) {
        this.filename = filename;
        this.repeat = repeat;
    }

    class MyIterator implements Iterator<Line> {
        public int count;
        public boolean done;
        public BufferedReader reader;
        public int linenum;

        public MyIterator() {
            count = 1;
            done = false;
            try {
                reader = new BufferedReader(new FileReader(filename));
            } catch(IOException e) {
                reader = null;
            }
        }

        @Override
        public boolean hasNext() {
            return !done;
        }

        @Override
        public Line next() {
            Line line = Line.END();
            if(reader == null) {
                done = true;
                return Line.END();
            } else {
                String s;
                try {
                    s = reader.readLine();
                } catch(IOException e) {
                    done = true;
                    return Line.END();
                }
                if(s == null) {
                    if(count < repeat) {
                        count += 1;
                        try {
                            reader.close();
                            reader = new BufferedReader(new FileReader(filename));
                        } catch(IOException e) {
                            done = true;
                            return Line.END();
                        }
                        return next();
                    } else {
                        done = true;
                        return Line.END();
                    }
                } else {
                    line = new Line(s, linenum++);
                }
            }
            return line;
        }
    }

    @Override
    public Iterator<Line> iterator() {
        return new MyIterator();
    }

    public static void main(String[] args) {
        FileIterator f = new FileIterator(args[0], Integer.parseInt(args[1]));
        for(Line line : f) {
            System.out.println(line.lineNumber + ": " + line.content);
        }
    }
}

class Util {
    public static Pattern whitespace = Pattern.compile("\\s+");
    public static String[] words(String line) {
        return whitespace.split(line);
    }


    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }
 
    // taken from: 
    // http://en.wikibooks.org/wiki/Algorithm_Implementation/Strings/Levenshtein_distance#Java
    public static int editDistance(String str1, String str2) {
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];        
 
        for (int i = 0; i <= str1.length(); i++)                                 
            distance[i][0] = i;                                                  
        for (int j = 1; j <= str2.length(); j++)                                 
            distance[0][j] = j;                                                  
 
        for (int i = 1; i <= str1.length(); i++)                                 
            for (int j = 1; j <= str2.length(); j++)                             
                distance[i][j] = minimum(                                        
                        distance[i - 1][j] + 1,                                  
                        distance[i][j - 1] + 1,                                  
                        distance[i - 1][j - 1] + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));
 
        return distance[str1.length()][str2.length()];                           
    }
}
