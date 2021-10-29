import java.math.*;
public class Mat_transf {
    public static class linearTrans{
        double[][] mat;
        public linearTrans(){
            this.mat=new double[2][2];
        }
        public linearTrans(double[][] dat){
            this();
            for(int i=0;i<2;++i){
                for(int j=0;j<2;++j){
                    this.mat[i][j]=dat[i][j];
                }
            }
        }
    }
    public static class Scalar extends linearTrans{
        public Scalar(double Sclr_overall){
            super();
            this.mat[0][0]=Sclr_overall;
            this.mat[1][1]=Sclr_overall;
        }
        public Scalar(double Sclr_x, double Sclr_y){
            super();
            this.mat[0][0]=Sclr_x;
            this.mat[1][1]=Sclr_y;
        }
    }
    public static class Shear extends linearTrans{
        public Shear(char axis,double offset){
            super();

            this.mat[0][0]=1.0d;
            this.mat[1][1]=1.0d;
            outer:switch(axis){
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
    }
    public class Rotation extends linearTrans{
        public Rotation(double dat,String form){
            super();
            double radVal=0.0d;
            outer: switch(form){
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
            }
            this.mat[0][0]=Math.cos(radVal);
            this.mat[1][1]=Math.cos(radVal);
            this.mat[0][1]=0.0d-Math.sin(radVal);
            this.mat[1][0]=Math.sin(radVal);
        }
    }
    public class Reflection extends linearTrans{
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
    public class homoTrans{
        
    }
}
