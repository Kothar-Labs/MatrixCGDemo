import java.math.*;
public class Mat_transf {
    public static class linearTrans{
        double[][] mat;
        static final double[][] identity_matrix={
            {1,0},
            {0,1}
        };
        static final double[][] zero_matrix={
            {0,0},
            {0,0},
        };
        /**
         * Construct a linear transformation with the default of an identity matrix.
         */
        public linearTrans(){
            this.mat=identity_matrix.clone();
        }
        /**
         * Construct a linear transformation with a customized matrix.
         * @param dat The matrix provided.
         * @apiNote One can always create customized matrix beyond those defined.
         * @see Scalar
         * @see Shear
         * @see Rotation
         * @see Reflection
         */
        public linearTrans(double[][] dat){
            this();
            for(int i=0;i<2;++i){
                for(int j=0;j<2;++j){
                    this.mat[i][j]=dat[i][j];
                }
            }
        }
        /**
         * Convert, or *map* a 2D vector x through the linear transformation.
         * @param ori The original vector.
         * @return The resulting vector.
         * @apiNote As such in transformation Ax=b, A is implemented as this.mat, x is the original vector and b is the resulting vector.
         * @apiNote Only 2D vectors are accepted currently.
         * @apiNote This method is non-destructive.
         */
        public double[] convert(double[] ori){
            if(ori.length!=2){
                throw new ArithmeticException("Illegal length(s) of target vector(s). Only 2D Vectors are accepted currently.");
            }
            double[] res=new double[2];
            res[0]=ori[0]*this.mat[0][0]+ori[1]*this.mat[0][1];
            res[1]=ori[0]*this.mat[1][0]+ori[1]*this.mat[1][1];
            return res;
        }
        /**
         * Convert, or *map* a point or vector as encapsulated in class Mat_demo.point.
         * @param ori The original point.
         * @return The resulting point.
         * @apiNote Similar to the previous overload, only 2D points are accepted currently.
         * @apiNote This method is non-destructive.
         */
        public Mat_demo.point convert(Mat_demo.point ori){
            double[] intrm={ori.x,ori.y};
            intrm=this.convert(intrm);
            return new Mat_demo.point(intrm[0],intrm[1]);
        }
        /**
         * Convert, or *map an entire geometric graph, as defined in Mat_demo.java, through the linear transformation.
         * @param ori The original geo-graph.
         * @return The resulting geo-graph.
         * @apiNote Did you notice:
         * <p> As seen in the source code, the edge pattern of the original graph, as implemented by ori.edges, is applied directly
         * to the resulting geo-graph, which indicates that matrix transformations do not affect edge patterns. </p>
         * <p> This is pretty much intuitive since that the matrix transformation affects only the metric properties, say, 
         * the lengths of the edges and positions of the vertices, of the graph </p>
         * <p> However, as shown in the textbook, such transformations can be seen as that of a *projection*, so that they maintain
         * some *properties* of the original graph, like the relative positions of points and lines: A point on one side of the line in
         * the original graph must not appear on the other side in the resulting graph; a point that lies on a line in the original graph
         * stays on the line despites the convertion.</p>
         * <p> Such *properties* are mathematically called projective properties, which is often discussed in CG as well as projective
         * geometry. </p>
         * @apiNote This method is non-destructive
         */
        public Mat_demo.GeoGraph convert(Mat_demo.GeoGraph ori){
            Mat_demo.point[] pntlst=ori.node.clone();
            for(int i=0;i<pntlst.length;++i){
                pntlst[i]=this.convert(pntlst[i]);
            }
            return new Mat_demo.GeoGraph(ori.nodeCount, ori.edgeCount, pntlst, ori.edges);
        }
    }
    /**
     * Transformation of a vector (x,y) to (mx,ny).
     * @pattern The matrix is shown as:
     * <p> m 0 </p>
     * <p> 0 n </p>
     * ...where m is the horizontal/x-scalar and n is the vertical/y-scalar of the transformation.
     * <p> In special cases where n==m, the scaling is uniform and thus ratio of x and y coordinates isn't altered. </p>
     */
    public static class Scalar extends linearTrans{
        /**
         * Create a uniform scaling transformation instance.
         * @param Sclr_overall The overall scalar.
         */
        public Scalar(double Sclr_overall){
            super();
            this.mat[0][0]=Sclr_overall;
            this.mat[1][1]=Sclr_overall;
        }
        /**
         * Create a general scalar transformation instance.
         * @param Sclr_x The scalar along the x axis.
         * @param Sclr_y The scalar along the y axis.
         */
        public Scalar(double Sclr_x, double Sclr_y){
            super();
            this.mat[0][0]=Sclr_x;
            this.mat[1][1]=Sclr_y;
        }
    }
    /**
     * Transformation of a vector (x,y) to (x+my,y+nx).
     * @pattern The matrix is shown as:
     * <p> 1 m </p>
     * <p> n 1 </p>
     * @apiNote Note that the definition of general shear transformation here is not exactly the same as that of textbook. 
     * @apiNote The "boost" transformation which is not mentioned in textbook is considered a general case here, whereas
     * the shear transformation is considered a special case where either m or n is zero.
     * @apiNote For those who've taken the relativity course, the Lorentz Transformation, on space-time graph can actually be
     * considered as boost transformation stacked with a scaling. With m and n equal to v/c in the relativity context, such 
     * transformation can be very handy in algorithmatic realization of L.T. 
     */
    public static class Shear extends linearTrans{
        /**
         * Construct a specialized and more intuitive shear-transformation instance with given mode and parameter. 
         * @param mode Token of shear. 3 types of input accepted only: 
         * <p> 'x': Set the transformation along x-axis. </p> 
         * <p> 'y': Set the transformation along y-axis. </p> 
         * <p> '$': Set the transformation along both axises. This is a boost transformation that is symmetric about line y=x or y=-x. </p>
         * @param offset Value of shear offset. 
         * @throws ArithmeticException When illegal tokens are provided. 
         */
        public Shear(char mode,double offset){
            super();
            outer:switch(mode){
                case 'x':{
                    this.mat[0][1]=offset;
                    break outer;
                }
                case 'y':{
                    this.mat[1][0]=offset;
                    break outer;
                }
                case '$':{
                    this.mat[0][1]=offset;
                    this.mat[1][0]=offset;
                    break outer;
                }
                default: {
                    throw new ArithmeticException("Illegal operation token(s).");
                }
            }
        }
        /**
         * Construct a generalized shear-transformation instance with two offset values. 
         * @param x_offset The offset along x_axis. Set to negative to reverse offset direction; set to zero to cancel any offset. 
         * @param y_offset The offset along y_axis. Set to negative to reverse offset direction; set to zero to cancel any offset. 
         * @apiNote This overloaded constructor is publicly called for debugging most of the times for stability reasons, since the 
         * two offset values are directly applied to the matrix algorithm, consequences of misinput may be unpredictable. 
         * @apiNote Algorithmatic Info: 
         * <p> When y_offset is zero, "new Shear(k,0.0d)" is equivalent to "new Shear('x',k)". </p> 
         * <p> When x_offset is zero, "new Shear(0.0d,k)" is equivalent to "new Shear('y',k)". </p> 
         * <p> When x_offset==y_offset, "new Shear(k,k)" is equivalent to "new Shear('$',k)". </p> 
         */
        public Shear(double x_offset, double y_offset){
            super();
            this.mat[0][1]=x_offset;
            this.mat[1][0]=y_offset;
        }
    }
    /**
     * Transformation that rotates the vector around the origin. 
     * @pattern The matrix is shown as: 
     * <p> cos(m)   -sin(m) </p> 
     * <p> sin(m)   cos(m) </p> 
     * ...where m is the angle of rotation, counter-clockwise, in radian measure. 
     */
    public class Rotation extends linearTrans{
        /**
         * Construct a rotation transformation instance with a given angle in a declared notation.
         * @param dat The value of the angle, counter-clockwise. Set to negative for clockwise rotation.
         * @param nota Notation of the said value, 3 possible values accepted: 
         * <p> "rad": Radian measure. </p>
         * <p> "deg": Degree measure. </p>
         * <p> "grad": Gradient measure. </p>
         * @apiNote For instance: (45,"deg") means that the rotation is counter-clockwise, 45 degrees, which is equivalent to
         * (Math.pi/4.0d,"rad") or (50,"grad"). 
         * @throws ArithmeticException When illegal notations are provided. 
         */
        public Rotation(double dat,String nota){
            super();
            double radVal=0.0d;
            outer: switch(nota){
                case "rad":{
                    radVal=dat;
                    break outer;
                }
                case "deg":{
                    radVal=(dat/180.0)*Math.PI;
                    break outer;
                }
                case "grad":{
                    radVal=(dat/100.0)*Math.PI;
                }
                default:{
                    throw new ArithmeticException("Invalid notation(s) of angle(s). Only radient, degree and gradient measures are accepted.");
                }
            }
            this.mat[0][0]=Math.cos(radVal);
            this.mat[1][1]=Math.cos(radVal);
            this.mat[0][1]=0.0d-Math.sin(radVal);
            this.mat[1][0]=Math.sin(radVal);
        }
    }
    /**
     * Transformation that reflects a vector about certain referee. 
     * @pattern See the constructor. Implementation varies along with parameter types. 
     * @apiNote This class is somehow restricted in terms of implementation. 
     */
    public class Reflection extends linearTrans{
        /**
         * Construct a type of pre-defined reflection transformation instance. 
         * @param c Token of reflection. 5 types of input are accepted. 
         * <p> 'x': Reflect about x-axis so that a vector (x,y) is transformed into (x,-y). </p> 
         * <p> 'y': Reflect about y-axis so that a vector (x,y) is transformed into (-x,y). </p> 
         * <p> '+': Reflect about the line y=x, so that a vector (x,y) is transformed into (y,x). </p> 
         * <p> '-': Reflect about the line y=-x, so that a vector(x,y) is transformed into (-y,-x). </p> 
         * <p> 'o': Reflect about the origin, so that a vector (x,y) is transformed into (-x,-y). </p> 
         * @apiNote Note that it is somehow douplicated as "new Reflection('o')" is no difference 
         * from "new Scalar(-1.0)". 
         * @apiNote This interface is kept, however, to make it more intuitive and fit with the notations in the textbook. 
         * The two code implementations are ultimately the same principle with different notation. 
         * @throws ArithmeticException When illegal tokens are provided. 
         */ 
        public Reflection(char c){
            super();
            outer: switch(c){
                case 'x':{
                    this.mat[0][0]=1.0d;
                    this.mat[1][1]=-1.0d;
                    break outer;
                }
                case 'y':{
                    this.mat[0][0]=-1.0d;
                    this.mat[1][1]=1.0d;
                    break outer;
                }
                case '+':{
                    this.mat[1][0]=1.0d;
                    this.mat[0][1]=1.0d;
                    break outer;
                }
                case '-':{
                    this.mat[1][0]=-1.0d;
                    this.mat[0][1]=-1.0d;
                    break outer;
                }
                case 'o':{
                    this.mat[0][0]=-1.0d;
                    this.mat[1][1]=-1.0d;
                    break outer;
                }
                default:{
                    throw new ArithmeticException("Illegal operation token(s).");
                }
            }
        }
    }
    /**
     * @deprecated Unimplemented.
     */
    public class homoTrans extends linearTrans{
        public homoTrans(int xshift, int yshift){
            this.mat=new double[3][3];
            

        }
    }
}
