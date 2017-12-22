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
/**
 *
 * @author Micah
 */
public class hackMath {
    
    //Tester
    public static void main(String[] args) {
        System.out.println(testAtan());
    }
    
    /**
     * Calculates the arc tangent for angles of 0 to 2pi
     * @param numerator - the numerator or opposite in SOH-CAH-TOA
     * @param denominator- the denominator or adjacent side
     * @return the angle in radians on [0,2pi]
     */
    public static double aTan(double numerator,double denominator) {
        double ans=0;
        if(denominator==0) { //if vertical return one of the PI's over 2
            if(numerator>0) {  //going up
                ans=Math.PI/2;
            } else {  //going down
                ans=3*Math.PI/2;
            }
        } else {
            ans=Math.atan(numerator/denominator); //get the actual arc tangent because screw dat.
            if(denominator<0) { // if x is negative then add pi
                ans+=Math.PI;
            } else {  //y > 0 will next check for quandrant 4. Flip -pi/2 to 3pi/2
                if(numerator<0) {
                    ans+=2*Math.PI;
                }
            }
        }
        return ans;
    }
    private static boolean testAtan() {
        boolean pass=true;
        double x,y,angle,ans;
        for(int i=0;i<1000;i++) {
        angle=2*Math.PI/1000*i; //do a thousand angles
            x=Math.cos(angle);
            y=Math.sin(angle);
            ans=aTan(y,x);
            if(Math.abs(ans-angle)>1e-9)
                pass=false;
        }
        x=0;  //testt pi/2
        y=1;
        angle=Math.PI/2;
        ans=aTan(y,x);
        if(Math.abs(ans-angle)>1e-9)
            pass=false;
        y=-1;   //test 3pi/2
        angle=3*Math.PI/2;
        ans=aTan(y,x);
        if(Math.abs(ans-angle)>1e-9)
            pass=false;
        return pass;
    }
}
