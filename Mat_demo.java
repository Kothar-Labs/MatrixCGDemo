import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Properties;
public class Mat_demo{
    static Properties prop=new Properties();
    /**
     * A scanner that refers to the source of the input matadata.
     */
    static Scanner sc;
    /**
     * Graphic frame as an extention of JFrame.
     * @author John_Doe
     */
    public static class GrfFrame extends JFrame{
        private Graphics curG;
        private GeoGraph oprands;
        /**
         * Initialize a graphic frame with a given geometric graph.
         * @param opr Designated geometric graph.
         */
        public GrfFrame(GeoGraph opr){
        super(prop.getProperty("TITLE"));
        // Introduce settings in "Mat_demo.properties".
        this.setSize(Integer.parseInt(prop.getProperty("MAX_X")),Integer.parseInt(prop.getProperty("MAX_Y")));
        this.setVisible(true);
        this.setResizable(Boolean.parseBoolean(prop.getProperty("RESIZABLE")));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        this.oprands=opr;

        this.setLocation(100,100);

        this.setBackground(Color.WHITE);

        curG=this.getGraphics();
        printComponents(curG);

        }
        /**
         * Converts geometric values/fractions of a point or vector into pixelwise notation that is used for CG.
         * @param geoVal A geometric/mathematic value.
         * @return The pixelwise scale resulted.
         * @apiNote Note that such schematic may be altered in later versions to optimize visual effects.
         */
        private static int toPixVal(double geoVal){
            return (int)(geoVal*10)+50;
        }

        @Override
        public void printComponents(Graphics g){
            GrfFrame.render(g,this.oprands);
        }
        /**
         * Render the geometric graph on the frame.
         * @param g Graphics of the frame.
         * @param ori Designated geometric graph.
         */
        public static void render(Graphics g,GeoGraph ori){
            Graphics2D g2d=(Graphics2D)g;
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(3.0f));
            double[][] grfSchem=ori.getSchem();
            try{
                for(int i=0;i<grfSchem.length;++i){
                    g2d.drawLine(
                        toPixVal(grfSchem[i][0]),
                        toPixVal(grfSchem[i][1]),
                        toPixVal(grfSchem[i][2]),
                        toPixVal(grfSchem[i][3])
                    );
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    /**
     * Incapsulation of a 2-D geometric point. It can also be written in the vector form.
     * @param x X coordinate of the point, or X fraction of the vector.
     * @param y Y coordinate of the point, or Y fraction of the vector.
     * @apiNote It is *not equal* to graphic "points", or in another way, pixels.
     */
    public static class point{
        double x;
        double y;
        public point(double x, double y){
            this.x=x;this.y=y;
        }
    }
    /**
     * Graphs in the geometric/mathematic context which is here notated as geo-graph or geometric graph. 
     * @param node List of vertexes of the geo-graph.
     * @param edges edges[x][y] indicates whether the two points node[x] and node[y] are connected directly by an edge, where x>y.
     */
    public static class GeoGraph{
        point[] node;
        int[][] edges;
        private int nodeCount,edgeCount;
        /**
         * Create a geometric graph from a structure and a data list for the metrics.
         * @param nodesize Amount of nodes.
         * @param edgesize Amount of edges.
         * @param nodeDat Positions of the nodes stored in a linear sequence. It should follow the format to function correctly: 
         * <p> {x_1,y_1,x_2,y_2 ... x_n,y_n} for n points. </p>
         * @param struc The structure of the graph.
         * @apiNote A "structure" here is what we commonly refer to as a graph in Graph Theory, which contains no actual metrics.
         * @apiNote For instance, a square and a common quadrilateral has the same "structure". 
         * @apiNote Structures must be fitted with corresponding data such as geometric positions of the nodes, to become geo-graphs.
         * @see point
         */
        public GeoGraph(int nodesize, int edgesize, double[] nodeDat, int[] struc){
            this.nodeCount=nodesize;
            this.edgeCount=edgesize;
            this.node=new point[nodesize];
            this.edges=new int[nodesize][2];
            try{
                int datTop=-1;
                for(int i=0;i<node.length;++i){
                    node[i]=new point(nodeDat[++datTop],nodeDat[++datTop]);
                }
                datTop=-1;
                for(int i=0;i<edges.length;++i){
                    this.edges[i][0]=struc[++datTop];
                    this.edges[i][1]=struc[++datTop];
                }
            }
            catch(ArrayIndexOutOfBoundsException ex){
                ex.printStackTrace();
            }
        }
        /**
         * An interface for the renderer.
         * @return The render schematics for the renderer.
         * @see GrfFrame
         */
        public double[][] getSchem(){
            double[][] res=new double[this.edgeCount][4];
            for(int i=0;i<this.edges.length;++i){
                res[i][0]=this.node[this.edges[i][0]].x;
                res[i][1]=this.node[this.edges[i][0]].y;
                res[i][2]=this.node[this.edges[i][1]].x;
                res[i][3]=this.node[this.edges[i][1]].y;
            }
            return res;
        }
    }
    /**
     * @apiNote Several variables used here:
     * <p> m : The number of nodes. </p>
     * <p> n : The number of edges. </p>
     * @apiNote The matadata contains the following lines in a manner similar to that of Luogu: 
     * <p> The first line contains two integers, m and n.
     * <p> The following n non-empty lines. The (k+1)_th line has two decimals x_k and y_k, indicating that the k_th point is at (x_k, y_k). </p>
     * <p> The next m non-empty lines. The (n+i+1)_th line has two integers a_i and b_i, indicating that there's an edge between the a_th and the b_th point. </p>
     */
    public static GeoGraph readMeta(){
        int nsize=sc.nextInt();
        int esize=sc.nextInt();
        double[] nodeDat=new double[2*nsize];
        int[] struc=new int[2*esize];
        for(int i=0;i<nodeDat.length;++i){
            nodeDat[i]=sc.nextDouble();
        }
        for(int i=0;i<struc.length;++i){
            struc[i]=sc.nextInt();
        }
        sc.close();
        return new Mat_demo.GeoGraph(nsize,esize,nodeDat,struc);
    }
    public static void main(String[] args) throws IOException{
        BufferedReader bfrd=new BufferedReader(new FileReader("Mat_demo.properties"));
        prop.load(bfrd);
        bfrd.close();
        if(prop.getProperty("INPUT_SOURCE").equals("PANEL")){
            sc=new Scanner(System.in);
        }
        else{
            sc=new Scanner(prop.getProperty("INPUT_SOURCE"));
        }
        
        new GrfFrame(readMeta());
    }
}
