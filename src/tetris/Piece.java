

/**
 *
 * @author ChanghengGu
 */

package tetris;
import java.util.Arrays;
public class Piece  {
    //enumaration of all shapes
    public enum shape{J,Z,L,S,I,O,T,init};
    public String[] shapes={"J","Z","L","S","I","O","T","init"};
    enum color{purple,red,orange,green,blue,yellow,pink,init};
    //piece coordinates
    int pieceCod[][];
    int pieceNumber;
    //initial piece coordinates tabel before possible rotation
    final int initCod[][][] = {
        {{0,-1},{0,0},{0,1},{1,1}},
        {{1,0},{0,0},{0,1},{-1,1}},
        {{0,-1},{0,0},{0,1},{-1,1}},
        {{1,-1},{1,0},{0,0},{0,1}},
        {{0,-1},{0,0},{0,1},{0,2}},
        {{0,0},{1,0},{0,1},{1,1}},
        {{0,-1},{0,0},{1,0},{0,1}},
        {{0,0},{0,0},{0,0},{0,0}}
    };
    private Piece piece;
    
    public void init_Piece(shape in){
        pieceNumber=in.ordinal();
        for(int i=0;i<4;i++){
                pieceCod[i][0]=initCod[pieceNumber][i][0];
                pieceCod[i][1]=initCod[pieceNumber][i][1];
        }
    }
    
    public int findNumber(String s){
        int temp=0;
        temp = Arrays.asList(shapes).indexOf(s);
        return temp;
    }
    
    public void setPiece(int in){
        for(int i=0;i<4;i++){
                pieceCod[i][0]=initCod[in][i][0];
                pieceCod[i][1]=initCod[in][i][1];
        }
    }
    //ctor
    public Piece(){
        pieceCod = new int[4][2];
        init_Piece(shape.init);
    }
    
    public void rotateLeft(){
        for(int i=0;i<4;i++){
            int tempX = pieceCod[i][0];
            int tempY = pieceCod[i][1];
            //x=y;y=-x
            pieceCod[i][0]=tempY*-1;
            pieceCod[i][1]=(tempX);
        }
    }
    public void rotateRight(){
        for(int i=0;i<4;i++){
            int tempX = pieceCod[i][0];
            int tempY = pieceCod[i][1];
            //x=-y;y=x
            pieceCod[i][0]=tempY;
            pieceCod[i][1]=tempX*-1;
        }
    }
        public int getMaxX(){
        int temp=pieceCod[0][0];
        for (int i=1;i<4;i++){
            if(pieceCod[i][0]>temp){
                temp=pieceCod[i][0];
            }
        }
        return temp;
        }
        
        public int getMinX(){
        int temp=pieceCod[0][0];
        for (int i=1;i<4;i++){
            if(pieceCod[i][0]<temp){
                temp=pieceCod[i][0];
            }
        }
        return temp;
        }
        
        public int getMaxY(){
        int temp=pieceCod[0][1];
        for(int i=1;i<4;i++){
            if(pieceCod[i][1]>temp){
                temp=pieceCod[i][1];
            }
        }
        return temp;
        }
        
        public int getMinY(){
        int temp=pieceCod[0][1];
        for (int i=1;i<4;i++){
            if(pieceCod[i][1]<temp){
                temp=pieceCod[i][1];
            }
        }
        return temp;
        }
}
