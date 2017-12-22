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

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Creates a mathematical vector. 
 * Handles all the vector math that I need... I think
 * @author mgale
 */
public class Vector implements Cloneable{
    private double[] vector;
    
    public static void main(String[] args) {
        System.out.println("Dot Product:     "+testDotProd());
        System.out.println("Unit Vector:     "+testUnitVec());
        System.out.println("Distance calc:   "+testDistance());
        System.out.println("Rotation Matrix: "+testRotation());
        System.out.println("Angle init:      "+testAngleInit());
    }
    
    /**
     * Creates a vector from the given array.
     * @param init 
     */
    public Vector(double[] init) {
        this.vector=init.clone();
    }
    /**
     * Creates a new 3-D unit vector given by polar coordinates.
     * @param phi- the polar angle from z-axis. [0,pi]
     * @param theta- the azimuthal angle measured from y-axis [0,2pi]
     */
    public Vector(double phi, double theta) {
        this.vector=new double[3];
        this.vector[2]=Math.cos(phi);
        this.vector[1]=Math.sin(phi)*Math.cos(theta);
        this.vector[0]=Math.sin(phi)*Math.sin(theta);
    }
    public double[] getContent() {
        return this.vector;
    }
    public double getContent(int i) {
        return this.vector[i];
    }
    /**
     * Scales the vector by the specified scalar quantity;
     * @param multiplier the scalar quantity
     */
    public void scale(double multiplier) {
        for(int i=0;i<this.vector.length;i++) {
            this.vector[i]*=multiplier;  //multiply every component by scalar
        }
    }
    public int length() {
        return this.vector.length;
    }
    /**
     * Makes the vector into a unit vector.
     */
    public void makeUnit() {
        double mag=this.getMagnitude();
        for(int i=0; i<this.vector.length;i++) {
            this.vector[i]=this.vector[i]/mag;
        }
    }
    public double getMagnitude() {
        return Math.sqrt(Vector.square(this));
    }
    /**
     * 
     * @return 
     */
    @Override
    public Vector  clone()  { 
        Vector ret=new Vector(new double[]{});
        try {
            ret= (Vector) super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Vector.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }  
    public String toString() {
        return Arrays.toString(this.vector);
    }
    public void setValue(double[] newVec) {
        this.vector=newVec.clone();
    }
    public void setValue(int i, double val) {
        this.vector[i]=val;
    }
    public void invertVal(int i) {
        this.vector[i]=-this.vector[i];
    }
    public boolean isEqual(Vector V) {
        boolean ans=true;
        if (V.length()!= this.length())
            ans=false;
        for(int i=0;i<this.vector.length;i++) {
            if(Math.abs(this.vector[i]-V.getContent(i))>1e-7)
                ans=false;
        }
        return ans;
    }
    /**
     * Rotates this vector by two angles. Is currently broken
     * 
     * @param phi the polar angle of rotation
     * @param theta the azimuthal angle of rotation
     */
    public void rotateVector(double phi, double theta) throws VectorSizeException {
        if (this.vector.length!=3)
            throw new VectorSizeException("Rotations can only work in 3D not: "+this.vector.length);
        double[][] A= new double[3][3];//the rotation matrix defined by [column][row]
        double cosPhi, sinPhi, sinTheta, cosTheta;
        cosPhi=Math.cos(-phi);
        sinPhi=Math.sin(-phi);
        sinTheta=Math.sin(theta);
        cosTheta=Math.cos(theta);
        //set up rotation matrix
        A[0]=new double[]{cosTheta,cosPhi*sinTheta,sinTheta*sinPhi};
        A[1]=new double[]{-sinTheta,cosPhi*cosTheta, sinPhi*cosTheta};
        A[2]=new double[]{0,-sinPhi,cosPhi};
        double[] newVec=new double[3];
        
        //Now to do vector and matrix multiplication. Oh loordy
        //insert relevant meme here.
        double buffer;
        for(int i=0;i<3;i++) {  // loop over all dimension
            buffer=0;
            for(int j=0;j<3;j++) {
                buffer+=A[j][i]*this.vector[j];
            }
            newVec[i]=buffer;
        }
        //store it!
        this.vector=newVec;
    }
    public String toCsv() {
        String out="";
        for(int i=0;i<this.vector.length;i++) {
            out=out+this.vector[i];
            if(i<this.vector.length-1)
                out=out+","; //if not the last column add a seperating comma
        }
        return out;
    }

        /**
     * Finds the dot product of two vectors. Vectors must be the same dimension.
     * @param vec1 first vector 
     * @param vec2 second vector
     * @return V1*V2
     * @throws VectorSizeException if the vectors aren't the same size
     */
    public static double dotProd(Vector vec1,Vector vec2) throws VectorSizeException {
        double ans=0;
        if(vec1.length()!=vec2.length())
            throw new VectorSizeException(vec1.length()+" isn't equal to "+vec2.length());
        for(byte i=0;i<vec1.length();i++) {
            ans+=vec1.getContent(i)*vec2.getContent(i);
        }
        return ans;
    }
    /**
     * Adds two vectors to each other
     * @param V1
     * @param V2
     * @return the addition of the two vectors
     * @throws mgale.VectorSizeException 
     */
    public static Vector add(Vector V1, Vector V2) throws VectorSizeException {
        if(V1.length()!=V2.length())
            throw new VectorSizeException(V1.length()+" isn't equal to "+V2.length());
        double[] buff=new double[V1.length()];
        for(int i=0; i<V1.length();i++) {
            buff[i]=V1.getContent(i)+V2.getContent(i);
        }
        return new Vector(buff);
    }
    /**
     * Squares the requested vector. Really just dots it with itself
     * @param V
     * @return the dot-product VV
     */
    public static double square(Vector V) {
        double out=0;
        try {
            out= Vector.dotProd(V, V);
        } catch (VectorSizeException ex) {
            //will never be thrown
        }
        return out;
    }
    /**
     * subtracts the two vectors.
     * @param V1
     * @param V2
     * @return the difference of the two vectors V1-V2
     * @throws VectorSizeException 
     */
    public static Vector subtract(Vector V1, Vector V2) throws VectorSizeException {
        if(V1.length()!=V2.length())
            throw new VectorSizeException(V1.length()+" isn't equal to "+V2.length());
        double[] buff=new double[V1.length()];
        for(int i=0; i<V1.length();i++) {
            buff[i]=V1.getContent(i)-V2.getContent(i);
        }
        return new Vector(buff);
    }
    /**
     * Multiplies the vector V by a scalar, scal.
     * @param scal
     * @param V
     * @return 
     */
    public static Vector scalarMult(double scal,Vector V) {
        double buff[]=new double[V.length()];
        for(int i=0;i<buff.length;i++) {
            buff[i]=scal*V.getContent(i);
        }
        return new Vector(buff);
    }
    /**
     * Multiplies the two vectors element by element but doesn't sum them. 
     * V=V10*V20,V11*V21.....
     * @param V1
     * @param V2
     * @return
     * @throws VectorSizeException 
     */
    public static Vector pointMultiply(Vector V1, Vector V2) throws VectorSizeException {
      if(V1.length()!=V2.length())
        throw new VectorSizeException(V1.length()+" isn't equal to "+V2.length());
      double[] buff=new double[V1.length()];
      for(int i=0; i<V1.length();i++) {
        buff[i]=V1.getContent(i)*V2.getContent(i);
      }
      return new Vector(buff);
    }
    /**
     * Checks if all components of V are >0.
     * @param V
     * @return 
     */
    public static boolean isAllPos(Vector V) {
        boolean ans=true;
        for(int i=0; i< V.length();i++) {
            if(V.getContent(i)<=0)
                ans=false;
        }
        return ans;
    }
    /**
     * finds the distance between two 3D points.
     * @param pnt1
     * @param pnt2
     * @return 
     * @throws mgale.VectorSizeException 
     */
    public static double getDistance(Vector pnt1, Vector pnt2) throws VectorSizeException {
        if(pnt1.length()!=3||pnt2.length()!=3)
            throw new VectorSizeException(pnt1.length()+" isn't equal to "+pnt2.length());
        double ans = Math.sqrt(Math.pow((pnt1.getContent(0)-pnt2.getContent(0)), 2) + Math.pow((pnt1.getContent(1)-pnt2.getContent(1)), 2)+Math.pow((pnt1.getContent(2)-pnt2.getContent(2)),2));
        return ans;
    }
    
    private static boolean testDotProd() {
        boolean success=true;
        double out;
        double[][] vec1=new double[][]{{1,0},{1,1},{1,0}};
        double[][] vec2=new double[][]{{1,0},{1,1},{0,1}};
        double[] ans=new double[]{1,2,0};
        try {
            for (byte i=0; i<vec1.length;i++) {
                out=dotProd(new Vector(vec1[i]),new Vector(vec2[i]));
                if(Math.abs(out-ans[i])>1e-9)
                    success=false;
            }
        } 
        catch(VectorSizeException ex) {
            
        }
        return success;
    }
    private static boolean testUnitVec() {
        double[][] input= new double[][]{{1,0,0},
            {2,0,0},
            {0,5,0},
            {0,0,20},
            {1,1,1}};
        double[][] answer=new double[][]{{1,0,0},
            {1,0,0},
            {0,1,0},
            {0,0,1},
            {1/Math.sqrt(3),1/Math.sqrt(3),1/Math.sqrt(3)}};
        boolean ans=true;
        for(int i=0;i<input.length;i++) {
            Vector V=new Vector(input[i]);
            V.makeUnit();
            if(!V.isEqual(new Vector(answer[i])))
                ans =false;
        }
        
        return ans;
    }
    private static boolean testDistance() {
        double in1[][]=new double[][]{{0,0,0},
        {1,0,0},
        {0,0,1}};
        double in2[][]=new double[][]{{1,0,0},
            {0,0,0},
            {0,0,-1}};
        double[] ans=new double[]{1,1,2};
        boolean ret=true;
        for (int i=0;i<in1.length;i++) {
            try {
                double buff=getDistance(new Vector(in1[i]),new Vector(in2[i]));
                if(Math.abs(buff-ans[i])>1e-7)
                    ret=false;
            } catch (VectorSizeException ex) {
                ret=false;
            }
        }
        return ret;
    }
    private static boolean testRotation() {
        boolean correct=true;
        double def[]=new double[]{0,0,1};
        Vector V;
        double[][] args= new double[][]{
            {Math.PI/2,0},
            {0,Math.PI},
            {Math.PI/2,Math.PI/2},
            {Math.PI/4,5*Math.PI/4}
        };
        double[][] ans= new double[][] {
            {0,1,0},
            {0,0,1},
            {1,0,0},
            {Math.sin(Math.PI/4)*Math.sin(5*Math.PI/4),Math.sin(Math.PI/4)*Math.cos(5*Math.PI/4),Math.cos(Math.PI/4)}
        };
        
        for(int i=0;i<args.length;i++) {
            V=new Vector(def);
            try {
                V.rotateVector(args[i][0], args[i][1]);
//                System.out.println(V.toString());
                if(!V.isEqual(new Vector(ans[i])))
                    correct=false;
            } catch (VectorSizeException ex) {
                correct=false;
            }
        }
        return correct;
    }
    private static boolean testAngleInit() {
        double[][] input=new double[][]{
            {0,0},
            {0,5},
            {Math.PI/2,0},
            {Math.PI/2,5*Math.PI/2}
        };
        double [][] out=new double[][] {
            {0,0,1},
            {0,0,1},
            {0,1,0},
            {1,0,0}
        };
        boolean ans=true;
        for(int i=0;i<input.length;i++) {
            Vector buff=new Vector(input[i][0],input[i][1]);
            if(!buff.isEqual(new Vector(out[i])))
                ans=false;
        }
        return ans;
    }
}

