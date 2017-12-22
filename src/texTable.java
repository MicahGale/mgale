/**
*Copyright 2017 Micah Gale
*
*Permission is hereby granted, free of charge, to any person obtaining a copy of this
* software and associated documentation files (the "Software"), to deal in the Software
* without restriction, including without limitation the rights to use, copy, modify, merge,
* publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to 
*whom the Software is furnished to do so, subject to the following conditions:
*
*The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
*
*THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
*TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
*THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
*CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
*OTHER DEALINGS IN THE SOFTWARE.
*
*/
package mgale;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;



/**
 *Creates an object that can store tabular data then dump it to a .tex file
 * @author mgale
 */
public class texTable {
    private  final String[] headers;
    private final String format;
    private  final boolean everyLine;
    private ArrayList<String[]> data;
    
    public texTable(String[] headers, boolean everyLine, String format) {
        this.headers= headers;
        this.everyLine= everyLine;
        this.format= format;
        this.data=new ArrayList();
    }
    /**
     * Adds the string array data to the meat of the table
     * @param data 
     */
    public void addData(String[] data) {
        if(data.length==this.headers.length) {  // only go if the column number matchess
            this.data.add(data);
        } else {
            System.out.println("Incorrect column number");
        }
    }
    public void writeTable(String location) {
        try {
            PrintWriter writer = new PrintWriter(location, "UTF-8");
            writer.printf("\\begin{tabular}{%s} \n",this.format);
            if (this.everyLine) {
                writer.println("\\hline");
            }
            for (byte i=0;i<this.headers.length;i++) { // print out all of the headers
                if (i < this.headers.length-1) { //checks if last column
                    writer.printf("\\textbf{%s} & ",this.headers[i]);
                } else {
                    writer.printf("\\textbf{%s} \\\\ \n",this.headers[i]);
                    if (this.everyLine) {
                        writer.println("\\hline"); //add in the hline
                    }
                } 
            }
            for(byte i =0; i<this.data.size();i++) {  //iterate over rows
                for(byte j=0; j<this.data.get(i).length;j++) { //iterate over columns
                    if (j==(this.data.get(i).length-1)) {
                        writer.printf(" %s \\\\ \n",this.data.get(i)[j]);
                        if (this.everyLine) {
                            writer.println("\\hline");
                        }
                    } else {
                        writer.printf(" %s & ",this.data.get(i)[j]); //print out the content
                    }
                }
            }
            writer.println("\\end{tabular}");
            writer.close();
         } catch (FileNotFoundException ex) {

        } catch  (UnsupportedEncodingException ex) {

        }
        
    }
    
}
