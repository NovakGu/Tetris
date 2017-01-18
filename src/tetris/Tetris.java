package tetris;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.*;
import java.util.Stack;


public class Tetris extends JFrame{
    //JPanel sideBar = new JPanel();
    
    
    
    
    public double Lspeed;
    public String hold="";
    public int holdInt=0;
    public int scoreCur=0;
    public String next="Holder";
    public int Lfps;
    public String Lsequence="LZJSIOT";
    public int sequenceLength = 7;
    public int curLetter=0;
    public JLabel holdPiece;
    public JLabel score;
    public JLabel level;
    public JLabel nextP;
    public JLabel goalText;
    public int goal = 10;
    public int curLevel=1;
    public boolean cooldown=false;
   // public int delay = 7000/24;
    public boolean spamSpace = false;
    Color color[] = {new Color(148,0,211),new Color(220,20,60),new Color(225,140,0),new Color(0,128,0),new Color(65,105,225),
    new Color(225,225,0),new Color(225,105,180),new Color(255,255,255)};
    
    public Tetris(int fps, double speed, String sequence) {
        Lsequence=sequence;
        sequenceLength=sequence.length();
        Lfps=fps;
        Lspeed=speed;
        
        JWindow window = new JWindow();
        ImageIcon iconLogo = new ImageIcon(System.getProperty("user.dir")+"/images.jpeg");
        window.getContentPane().add(new JLabel("",iconLogo , SwingConstants.CENTER));
        window.setBounds(500, 500, 300, 180);
        window.setVisible(true);
        try {
        Thread.sleep(2000);
        } catch (InterruptedException exception) {
        exception.printStackTrace();
        }
        window.setVisible(false);
        window.dispose();
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JLabel instr = new JLabel();
        instr.setFont(new Font("Serif", Font.PLAIN, 17));
        //ImageIcon instruction = new ImageIcon(System.getProperty("user.dir")+"/images2.png");
        instr.setText("<html>Game Action	Keyboard	Numpad\n<br>" +
                "<br>"+
"Move Left	LEFT Arrow	Numpad 4\n<br>" +
"Move Right	RIGHT Arrow	Numpad 6\n<br>" +
"Drop	Space Bar	Numpad 8\n<br>" +
"Rotate Right	UP Arrow, X	Numpad 1, 5, 9\n<br>" +
"Rotate Left	Control, Z	Numpad 3, 7\n<br>" +
"Pause	P	 \n<br>" +
                "<br>"+
                "<br>"+
"Respond to the following mouse events to manipulate the currently falling piece:\n<br>" +
                "<br>"+
"MousePress (on unselected falling piece)	Select the piece for further manipulation\n<br>" +
"MouseMotion	Selected piece follows the mouse from side-to-side.\n<br>" +
"MouseWheel	Rotate the selected piece.\n<br>" +
"MousePress (on selected falling piece)	Drop the selected piece into position.</html><br>");
        JButton start = new JButton("Start");
        //JButton highscore = new JButton("HighScore");
        frame.add(panel,BorderLayout.SOUTH);
        frame.add(instr, BorderLayout.CENTER);
        panel.add(start);
        //panel.add(highscore);
        frame.setTitle("Tetris CS349");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setSize(500,1000);
        frame.setLocationRelativeTo(null);
        
        start.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                panel.remove(instr);
               panel.remove(start);
               
              // panel.remove(highscore);
               frame.remove(panel);
                frame.setSize(300, 900);
                frame.setVisible(true);
                frame.setResizable(true);
                frame.setTitle("Tetris");
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setFocusable(true);
                frame.setLocationRelativeTo(null);
                game game = new game();
                JPanel side = new JPanel();
                JPanel top = new JPanel();
                level = new JLabel();
                level.setText("Level1  |");
                goalText = new JLabel();
                goalText.setText("Level-Goal: " + Integer.toString(goal));
                JButton Restart = new JButton("ReStart");
                //JButton Pause = new JButton("Pause");
                top.add(Restart);
                top.add(level);
                top.add(goalText);
               // top.add(Pause);
                Restart.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent event){
                
                hold="";
                holdInt=0;
                scoreCur=0;
                next="Holder";
                sequenceLength = sequence.length();
                curLetter=0;
                cooldown=false;
                spamSpace = false;
                updateScore();
                updateNext();
                updateHold();
                game.start();
            }
                });
                
                side.setSize(200,300);
                side.setLayout(new GridLayout(2,3,50,50));
                JLabel holdLabel = new JLabel("HOLD");
                JLabel scoreLabel = new JLabel("SCORE");
                JLabel nextLabel = new JLabel("NEXT");
                holdPiece = new JLabel(hold);
                score = new JLabel(Integer.toString(scoreCur));
                nextP = new JLabel(next);
                //JLabel Holdpiece = new 
                holdLabel.setLocation(50,100);
                nextLabel.setLocation(50,200);
                scoreLabel.setLocation(50,300); 
                holdPiece.setLocation(150,100);
                nextP.setLocation(150,200);
                score.setLocation(150,300); 
                side.add(holdLabel);
                side.add(nextLabel);
                side.add(scoreLabel);
                side.add(holdPiece);
                side.add(nextP);
                side.add(score);
                frame.add(game, BorderLayout.CENTER);
                frame.add(side, BorderLayout.SOUTH);
                frame.add(top, BorderLayout.NORTH);
                game.start();
                setFocusable(true);
                requestFocusInWindow();
                
        }
        });
    }
    
    public void updateGoal(){
        goalText.setText("Goal-Level:  "+Integer.toString(goal));
    }
    
    public void updateLevel(String in){
        level.setText(in+"  |");
    }
    
    public void updateScore(){
        score.setText(Integer.toString(scoreCur));
    }
    public void updateNext(){
        nextP.setText(next);
    }
    public void updateHold(){
        holdPiece.setText(hold);
    }
    
    private class game extends JPanel implements ActionListener{
        
        private boolean pause;
        private boolean start;
        private boolean fall;
        private int level;
        private int curPiece;
        private int x;
        private int y;
        Piece piece;
        int[][] board;
        final private int boardLength=10;
        final private int boardHeight=24;
        Timer tFPS;
        Timer speed;
        
        public game(){
            this.addKeyListener(new KeyHandler());
            this.setFocusable(true);
            this.requestFocusInWindow();
            piece = new Piece();
            
            
            addMouseListener(new MouseHandler());
            addMouseMotionListener(new MouseMotionHandler());
            tFPS = new Timer(1000/Lfps,this);
            tFPS.start();
            speed = new Timer(((int)(1000*Lspeed))/24, this);
            speed.start();
            board = new int[boardLength][24];
            for (int[] row: board){
                Arrays.fill(row, 7);
            }
            clean();
        }
        
        public void newLevel(){
            goal=goal+10;
            curLevel++;
            Lspeed=Lspeed*0.9;
            String temp = "Level" + Integer.toString(curLevel);
            updateLevel(temp);
            hold="";
                holdInt=0;
                scoreCur=0;
                next="Holder";
                curLetter=0;
                cooldown=false;
                spamSpace = false;
                updateScore();
                updateNext();
                updateHold();
                updateGoal();
                speed = new Timer(((int)(1000*Lspeed))/24, this);
                speed.start();
                start();
        }
        
        public void nextInLine(){
            String temp;
            if(curLetter!=sequenceLength-1){
                 temp = ""+Lsequence.charAt(curLetter+1);
            }
            else{
                 temp = ""+Lsequence.charAt(0);
            }
                next=temp;
        }
        
        public void detectAndDeleteFullLine(){
            for(int i=0;i<boardHeight;i++){
                for(int j=0;j<boardLength;j++){
                    if(board[j][i]==7){
                        break;
                    }
                    if(j==boardLength-1){
                        cancelLine(i);
                        scoreCur++;
                        updateScore();
                        if(scoreCur==goal){
                            newLevel();
                        }
                    }
                }
            }
        }
        
        public void Hold(){
            if(hold==""){
                
                String temp = ""+Lsequence.charAt(curLetter-1);
                hold=temp;
                //holdPie=piece;
                holdInt=curLetter-1;
                updateHold();
                nextPiece();
                cooldown=true;
            }
            else{
                if(!cooldown){
                      String temp1;
                    if(curLetter==0){
                        temp1= ""+Lsequence.charAt(6);
                    }
                    else{
                        temp1= ""+Lsequence.charAt(curLetter-1); 
                    }
                   
                   hold=temp1;
                   nextPieceHold(holdInt);
                   holdInt=curLetter-1;
                   updateHold();
                   cooldown=true;
                }
                
                //holdInt=curLetter;
            }
        }
        
        public void cancelLine(int lineNumber){
            int dropAbove=lineNumber-1;
            int curLine=lineNumber;
            for(int k=0;k<boardLength;k++){
                board[k][lineNumber]=7;
            }
            for(int k=dropAbove;k>0;k--){
                for(int l=0;l<boardLength;l++){
                    if(curLine<=0){
                    break;
                    }
                    board[l][curLine]=board[l][k]; 
                }
                curLine--;
            }
            //repaint();
        }
        
        
        public boolean moveLeft(Piece p){
            int tempY=piece.pieceCod[0][1];
            int addY=0;
         for(int i=0;i<4;i++){
             if(piece.pieceCod[i][1]>tempY){
                    tempY=piece.pieceCod[i][1];
                    addY++;
                }
                int teX = x + p.pieceCod[i][0] - 1;
                int teY =24-y-addY;
                if(teY<-2){
                return false;
                }
                if(teY>23){
                return false;
                }
                else if(teX>boardLength || teX<0){
                    return false;
                }
                else if(teY>0&&board[teX][teY]!=7){
                    return false; 
                }
                else{} 
            }
            x=x-1;
            piece=p;
          //  repaint();
            return true;
        }
        
        
        
        public boolean moveRight(Piece p){
            int tempY=piece.pieceCod[0][1];
            int addY=0;
         for(int i=0;i<4;i++){
             if(piece.pieceCod[i][1]>tempY){
                    tempY=piece.pieceCod[i][1];
                    addY++;
                }
                int teX = x + p.pieceCod[i][0] + 1;
                int teY =24-y-addY;
                if(teY<-2){
                return false;
                }
                if(teY>23){
                return false;
                }
                else if(teX>=10 || teX<0){
                    return false;
                }
                else if(teY>0&&board[teX][teY]!=7){
                    return false; 
                }
                else{} 
            }
            x=x+1;
            piece=p;
            //repaint();
            return true;
        }
        
        
        public boolean rotateLeft(Piece p){
            p.rotateLeft();
            int tempY=piece.pieceCod[0][1];
            int addY=0;
         for(int i=0;i<4;i++){
             if(piece.pieceCod[i][1]>tempY){
                    tempY=piece.pieceCod[i][1];
                    addY++;
                }
                int teX = x + p.pieceCod[i][0];
                int teY =24-y-addY;
                if(teY<-2){
                return false;
                }
                if(teY>23){
                return false;
                }
                else if(teX>=boardLength || teX<0){
                    p.rotateRight();
                }
                else if(teY>0&&board[teX][teY]!=7){
                    return false; 
                }
                else{} 
            }
            piece=p;
           // repaint();
            return true;
        }
        
         public boolean rotateRight(Piece p){
            p.rotateRight();
            int minYABS=Math.abs(piece.getMinY());
            int tempY;
            for(int i=0;i<4;i++){
               tempY=piece.pieceCod[i][1]+minYABS;
                int teX = x + p.pieceCod[i][0];
                int teY =23-y-tempY;
                if(teX<0){
                 p.rotateLeft();
                }
                else if(teY>23){
                return false;
                }
                else if(teX>=boardLength ){
                    p.rotateLeft();
                }
                else if(teY>0&&board[teX][teY]!=7){
                    return false; 
                }
                else{} 
            }
            piece=p;
            //repaint();
            return true;
        }
       
        
        public void drop(){
            while(moveDown(piece)){
                
                y--;
            }
            stopped();
        }
        
        public boolean moveDown(Piece p){
            int minYABS=Math.abs(piece.getMinY());
            int tempY2;
            int tempY=piece.pieceCod[0][1];
            int addY=0;
         for(int i=0;i<4;i++){
             tempY2=piece.pieceCod[i][1]+minYABS;
             
             if(piece.pieceCod[i][1]>tempY){
                    tempY=piece.pieceCod[i][1];
                    addY++;
                }

             int teX = x + p.pieceCod[i][0];
             int teY =24-y-addY;
             int distance = (24-y-addY)-(23-y-tempY2);
             if(Math.abs(distance)!=1){
                 if(distance>0){
                     teY=teY-distance+1;
                 }
                 else if(distance<0){
                 return false;
                 }
             }
                
                if(teY<-2){
                return false;
                }
                else if(teX>=boardLength || teX<0){
                }
                else if(teY>23){
                return false;
                }
                
                else if(teY>0&&board[teX][teY]!=7){
                    return false; 
                }
                else{} 
            }
            piece=p;
           // repaint();
            return true;
        }
        
        public void fall(){
            y--;
            if(!moveDown(piece)){
                stopped();
            }     
        }
        
        public void stopped(){
            int minYABS=Math.abs(piece.getMinY());
            int tempY;
            for(int i=0;i<4;i++){
               tempY=piece.pieceCod[i][1]+minYABS;
               
               try {
                   if((23-y)-tempY<0){}
                   else{
                board[x+piece.pieceCod[i][0]][(23-y)-tempY]=piece.pieceNumber;
                   }
                    } catch (IndexOutOfBoundsException e) {
                        spamSpace=true;
                    }
                //board[x+piece.pieceCod[i][0]][(23-y)-tempY]=piece.pieceNumber;
            }
            detectAndDeleteFullLine();
            cooldown=false;
            nextPiece();
        }
        
        public void nextPieceHold(int holdint){
            String index = Character.toString(Lsequence.charAt(holdint));
            int a;
            a=piece.findNumber(index);
            piece.setPiece(a);
            piece.pieceNumber=a;
            x = boardLength / 2 + 1;
            y = boardHeight - 1 + piece.getMinY();
            boolean gg;
            
            gg = moveDown(piece);
            
                piece.setPiece(a);
                piece.pieceNumber=a;
                x = boardLength/2 + 1;
                y = boardHeight + 1 + piece.getMinY();
                
                if(!gg){
                //repaint();
                tFPS.stop();
                speed.stop();
                System.out.println("game over");
                start = false;
                }  
        }
        
        public void nextPiece(){
            nextInLine();
            updateNext();
            String index = Character.toString(Lsequence.charAt(curLetter));
            int a;
            a=piece.findNumber(index);
            piece.setPiece(a);
            x = boardLength / 2 + 1;
            y = boardHeight - 1 + piece.getMinY();
            boolean gg;
            
            gg = moveDown(piece);
            
            if(curLetter<=sequenceLength-1){
                piece.setPiece(a);
                piece.pieceNumber=a;
                x = boardLength/2 + 1;
                y = boardHeight + 1 + piece.getMinY();
                curLetter++;
                if(curLetter==sequenceLength){
                curLetter=0;
                }
                
                if(!gg){
               // repaint();
                tFPS.stop();
                speed.stop();
                System.out.println("game over");
                start = false;
                }
            }
            else{
                index = Character.toString(Lsequence.charAt(0));
                a=piece.findNumber(index);
                piece.setPiece(a);
                piece.pieceNumber=a;
                x = boardLength/2 + 1;
                y = boardHeight + 1 + piece.getMinY();
                curLetter=0;
                if(!gg){
                tFPS.stop();
                speed.stop();
                start = false;
                }
            }
            
        }
        
        public void pause(){
            if(!pause){
            pause = true;
            tFPS.stop();
            speed.stop();
          //  repaint();
            }
            else{
            tFPS.start();
            speed.start();
            pause =false;
           // repaint();
            }
        }
        
        public void start(){
            pause=false;
            start=true;
            fall=false;
            clean();
            this.setFocusable(true);
            this.requestFocusInWindow();
            nextPiece();
            tFPS.start();
            speed.start();
            
        }
        
        public void paint(Graphics graph){
            super.paint(graph);
            Dimension panel = getSize(); 
            double limit=panel.getHeight();
            double actualHeightDraw = boardHeight*squareHeight();
            int TopLine = (int)(limit-actualHeightDraw);
            //draw the falling piece
            int pieceAtX;
            for(int i=0;i<boardHeight;i++){
                for(int j=0;j<boardLength;j++){
                  pieceAtX=board[j][i];
                  if(pieceAtX==7){
                      Color c = color[7];
                      graph.setColor(c);
                      graph.fill3DRect(1+j*squareLength(),1+i*squareHeight()+TopLine,squareLength()-2,squareHeight()-2,true);
                  }
                  
                }
            }
            if(piece.pieceNumber!=7){
                int drawX;
                int drawY;
                int squareX;
                int squareY;
                int squareColor;
                for(int i=0;i<4;i++){
                    
                    drawX=x+piece.pieceCod[i][0];
                    drawY=y+piece.pieceCod[i][1];
                    if(drawY>24){
                    continue;
                    }
                    squareX=drawX*squareLength();
                    squareY=TopLine+(squareHeight()*(boardHeight-1-drawY));
                    squareColor=piece.pieceNumber;
                    Color c = color[squareColor];
                    graph.setColor(c);
                    graph.fill3DRect(squareX,squareY, squareLength()-2, squareHeight()-2,true);
                    
                }
            }

            //draw the dropped piece
            for(int i=0;i<boardHeight;i++){
                for(int j=0;j<boardLength;j++){
                  pieceAtX=board[j][i];
                  if(pieceAtX!=7){
                      Color c = color[pieceAtX];
                      graph.setColor(c);
                      graph.fill3DRect(1+j*squareLength(),1+i*squareHeight()+TopLine,squareLength()-2,squareHeight()-2,true);
                  }
                  
                }
            }
        }
         
        public int squareHeight(){
            int squareHeight;
            squareHeight=(int)(getSize().getHeight()/boardHeight);
            return squareHeight;
        }
        
        public int squareLength(){
            int squareWidth;
            squareWidth=(int)(getSize().getWidth()/boardLength);
            return squareWidth;
        }

        public void clean(){
            for (int[] row: board){
                Arrays.fill(row, 7);
            }
        }
        
        public void actionPerformed(ActionEvent e){
             if (e.getSource() == tFPS)
            {
                repaint();
            }
            else if (e.getSource() == speed)
            {
                 piecefallaction();               
            }
        
    
        }
        
        public void piecefallaction(){
            if (fall) {
                fall = false;
                nextPiece();
            } else {
                fall();
            }
        }
        
        
        private class KeyHandler extends KeyAdapter{

            @Override
            public void keyPressed(KeyEvent event){
                int key = event.getKeyCode();
               if(!start){
                    return;
                }
                if(key=='p' || key=='P'){
                    pause();
                    return;
                }
                switch(key){
                    //move left
                    case KeyEvent.VK_LEFT:
                        moveLeft(piece);
                        break;
                    case KeyEvent.VK_NUMPAD4:
                        moveLeft(piece);
                        break;
                    //move right
                    case KeyEvent.VK_RIGHT:
                        moveRight(piece);
                        break;
                    case KeyEvent.VK_NUMPAD6:
                        moveRight(piece);
                        break;
                    //rotate right
                    case KeyEvent.VK_UP:
                        if(piece.pieceNumber==5){}
                        else{rotateRight(piece);}
                        break;
                    case KeyEvent.VK_X:
                        if(piece.pieceNumber==5){}
                        else{rotateRight(piece);}
                        break;
                    case KeyEvent.VK_NUMPAD1:
                        if(piece.pieceNumber==5){}
                        else{rotateRight(piece);}
                        break;
                    case KeyEvent.VK_NUMPAD5:
                        if(piece.pieceNumber==5){}
                        else{rotateRight(piece);}
                        break;
                    case KeyEvent.VK_NUMPAD9:
                        if(piece.pieceNumber==5){}
                        else{rotateRight(piece);}
                        break;
                    //drop down
                    case KeyEvent.VK_SPACE:
                        if(!spamSpace){drop();}
                        break;
                    case KeyEvent.VK_NUMPAD8:
                        if(!spamSpace){drop();}
                        break;
                    //rotate left
                    case KeyEvent.VK_CONTROL:
                        if(piece.pieceNumber==5){}
                        else{rotateLeft(piece);}
                        break;
                    case KeyEvent.VK_Z:
                        if(piece.pieceNumber==5){}
                        else{rotateLeft(piece);}
                        break;
                    case KeyEvent.VK_NUMPAD3:
                        if(piece.pieceNumber==5){}
                        else{rotateLeft(piece);}
                        break;
                    case KeyEvent.VK_NUMPAD7:
                        if(piece.pieceNumber==5){}
                        else{rotateLeft(piece);}
                        break;
                    case KeyEvent.VK_Q:
                        if(!cooldown){
                            Hold();
                        }
                        break;
                    
                }
                
                
            }
        }

        
        private class MouseHandler extends MouseAdapter{
            public void mousePressed(MouseEvent event){
                if (event.getButton() == MouseEvent.BUTTON1) {
                drop();
                }
                
            }
        }
        
        private class MouseMotionHandler implements MouseMotionListener, MouseWheelListener{
            
            public void mouseMoved(MouseEvent event){
            }
            public void mouseDragged(MouseEvent event){
                
            }
            public void mouseWheelMoved(MouseWheelEvent event){
                int notches = event.getWheelRotation();
                if (notches < 0) {
                    rotateLeft(piece);
                } else {
                    rotateRight(piece);
                }
            }
        }
    }
}
