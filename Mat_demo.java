import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
public class Mat_demo{
    public static class GrfFrame extends JFrame{
        private Graphics curG;
        public GrfFrame(){
            super(System.getProperty("TITLE"));
        
        this.setSize(Integer.parseInt(System.getProperty("MAX_X")),Integer.parseInt(System.getProperty("MAX_Y")));

        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.setLocation(100,100);

        this.setBackground(Color.WHITE);

        curG=this.getGraphics();
        printComponents(curG);

        }
        public static void paintComponents(Graphics g,GeoGraph ori){
            g.setColor(Color.BLACK);
            int[][] grfSchem=ori.getSchem();
            try{
                for(int i=0;i<grfSchem.length;++i){
                    g.drawLine(grfSchem[i][0],grfSchem[i][1],grfSchem[i][2],grfSchem[i][3]);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    public class point{
        int x;
        int y;
        public point(int x, int y){
            this.x=x;this.y=y;
        }
    }
    public class GeoGraph{
        point[] node;
        boolean[][] edges;
        private int nodeCount,edgeCount;
        public GeoGraph(int nodesize, int edgesize, int[] dat){
            this.nodeCount=nodesize;
            this.edgeCount=edgesize;
            this.node=new point[nodesize];
            this.edges=new boolean[nodesize][nodesize];
            try{
                int datTop=-1;
                for(int i=0;i<node.length;++i){
                    node[i]=new point(dat[++datTop],dat[++datTop]);
                }
                for(int i=0;i<edges.length;++i){
                    edges[++datTop][++datTop]=true;
                }
            }
            catch(ArrayIndexOutOfBoundsException ex){
                ex.printStackTrace();
            }
        }
        public int[][] getSchem(){
            int[][] res=new int[this.edgeCount][4];
            int restop=0;
            for(int i=0;i<this.nodeCount;++i){
                for(int j=0;j<this.nodeCount;++j){
                    if(this.edges[i][j]==true){
                        res[restop][0]=this.node[i].x;
                        res[restop][1]=this.node[i].y;
                        res[restop][2]=this.node[j].x;
                        res[restop][3]=this.node[j].y;
                        restop++;
                    }
                }
            }
            return res;
        }
    }
    
    public static void main(String[] args){
        new GrfFrame();
    }
}
