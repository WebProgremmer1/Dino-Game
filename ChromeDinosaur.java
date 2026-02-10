import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class ChromeDinosaur extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 750;
    int boardHeight = 250;

    Image disosaurImg;
    Image dinosaurDeadImg;
    Image dinosaurJumpImg;
    Image cactus1Img;
    Image cactus2Img;
    Image cactus3Img;
    Image birdImg;
    Image dinosaurSprintImg;
    Image gameOverSign;
    Image resetSign;



    class Block{
        int x;
        int y;
        int width;
        int height;
        Image img;

        Block(int x, int y, int width, int height, Image img){
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;

        }
    }
    int duckWidth = 80;
    int dusckHeight = 50;

    // bird sizes 
    int birdWidth = 97;
    int birdHeight = 68;
    int birdDownX = 700;
    int birdDownY = boardHeight - 100;
    int birdUpperX = 700;
    int birdUpperY = boardHeight - 180;

    // dinosaur 
    int dinosaurWidth = 88;
    int dinosaurHeight = 94;
    int dinosaurX = 50;
    int dinosaurY = boardHeight - dinosaurHeight;
    
    // components of playing 
    int velocityX = -12;
    int velocityY = 0;
    int gravity = 1;
    boolean gameOver = false;
    int score = 0;


    // game over sign 
    int SignWidth = 386;
    int SignHeight = 40;
    int SignX = 200;
    int SignY = 20;
    //
    int resetSignW = 50;
    int resetSignH = 40;
    int resetX = 370;
    int resetY = 60;





    Block dinosaur;
    Block gameEnds;
    Block resetImg;
    // Block dinosaur_duck;


    // cactus 
    int cactus1Width = 34;
    int cactus2Width = 69;
    int cactus3Width = 102;
    int cactusHeight = 70;
    int cactusX = 700;
    int cactusY = boardHeight - cactusHeight;
    ArrayList<Block> cactusArray; 
    
    Timer gameLoop;
    Timer placeCactusTimer;

    boolean downHeld = false;


    public ChromeDinosaur(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.lightGray);
        setFocusable(true);
        addKeyListener(this);


        disosaurImg = new ImageIcon(getClass().getResource("./img/dino-run.gif")).getImage();
        dinosaurDeadImg = new ImageIcon(getClass().getResource("./img/dino-dead.png")).getImage();
        dinosaurJumpImg = new ImageIcon(getClass().getResource("./img/dino.png")).getImage();
        dinosaurSprintImg = new ImageIcon(getClass().getResource("./img/dino-duck.gif")).getImage();
        cactus1Img = new ImageIcon(getClass().getResource("./img/cactus1.png")).getImage();
        cactus2Img = new ImageIcon(getClass().getResource("./img/cactus2.png")).getImage();
        cactus3Img = new ImageIcon(getClass().getResource("./img/cactus3.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./img/bird.gif")).getImage();
        gameOverSign = new ImageIcon(getClass().getResource("./img/game-over.png")).getImage();
        resetSign = new ImageIcon(getClass().getResource("./img/reset.png")).getImage();

        dinosaur = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, disosaurImg);
        gameEnds = new Block(SignX, SignY, SignWidth, SignHeight, gameOverSign);
        resetImg = new Block(resetX, resetY, resetSignW, resetSignH, resetSign);
        // dinosaur_duck = new Block(dinosaurX, dinosaurY, dinosaurWidth, dinosaurHeight, dinosaurSprintImg);

        cactusArray = new ArrayList<Block>();

        gameLoop = new Timer(1000/60, this);
        gameLoop.start();

        placeCactusTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placeCactus();
            }
        });
        placeCactusTimer.start();
    }

    void placeCactus(){
        if( gameOver ){
            return;
        }
        double r = Math.random();

        double spawnRate = 0.7; // 70% chance to spawn something each tick
        if (r > spawnRate) return; // no spawn (30%)

        double t = Math.random(); // decide what to spawn

        if (t < 0.15) { // 15% of spawns
            cactusArray.add(new Block(birdDownX, birdDownY, birdWidth, birdHeight, birdImg));
        } else if (t < 0.30) { // next 15%
            cactusArray.add(new Block(birdUpperX, birdUpperY, birdWidth, birdHeight, birdImg));
        } else if (t < 0.45) { // next 15%
            cactusArray.add(new Block(cactusX, cactusY, cactus3Width, cactusHeight, cactus3Img));
        } else if (t < 0.70) { // next 25%
            cactusArray.add(new Block(cactusX, cactusY, cactus2Width, cactusHeight, cactus2Img));
        } else { // remaining 30%
            cactusArray.add(new Block(cactusX, cactusY, cactus1Width, cactusHeight, cactus1Img));
        }

        if(cactusArray.size() > 10){
            cactusArray.remove(0);
        }

    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    
    public void draw(Graphics g){
        g.drawImage(dinosaur.img, dinosaur.x, dinosaur.y, dinosaur.width, dinosaur.height, null);

        for(int i = 0; i < cactusArray.size(); i++){
            Block cactus = cactusArray.get(i);
            g.drawImage(cactus.img, cactus.x, cactus.y, cactus.width, cactus.height, null);
        }
        g.setColor(Color.black);
        g.setFont(new Font("Monaco", Font.PLAIN, 32));
        if (gameOver){
            g.drawString( String.valueOf(score), 10, 35);
            g.drawImage(gameEnds.img, gameEnds.x, gameEnds.y, gameEnds.width, gameEnds.height, null);
            g.drawImage(resetImg.img, resetImg.x, resetImg.y, resetImg.width, resetImg.height, null);
        }
        else{
            g.drawString(String.valueOf(score), 10, 35);
        }
    }
    public void move(){
        dinosaur.y += velocityY;
        velocityY += gravity;

        if(dinosaur.y > dinosaurY){
            dinosaur.y = dinosaurY;
            velocityY = 0;
            dinosaur.img = downHeld ? dinosaurSprintImg : disosaurImg;
        }
        for(int i = 0; i < cactusArray.size(); i++){
            Block cactus = cactusArray.get(i);
            cactus.x += velocityX;

            if(collission(dinosaur, cactus)){
                dinosaur.img = dinosaurDeadImg;
                gameOver = true;
            }
        }

        score++;
    }

    // implement feature with shrinking size of the image 
    // add lable with hame over 


    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameOver){
            placeCactusTimer.stop();
            gameLoop.stop();
        }
    }

    boolean collission(Block a, Block b){
        // check this formula
        return  a.x < b.x + b.width &&
                a.x + a.width > b.x &&
                a.y < b.y + b.height &&
                a.y + a.height > b.y;

    }

    @Override
    public void keyTyped(KeyEvent e) {}


    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            if(dinosaur.y == dinosaurY){
                velocityY = -17;
                dinosaur.img = dinosaurJumpImg;
            }
            if(gameOver){
                dinosaur.y = dinosaurY;
                dinosaur.img = disosaurImg;
                velocityY = 0;
                cactusArray.clear();
                score = 0;
                gameOver = false;
                gameLoop.start();
                placeCactusTimer.start();


                }

            }
        if(e.getKeyCode() == KeyEvent.VK_DOWN){
            downHeld = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            downHeld = false;
        }
    }
       

}
