import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Time;
import java.util.Objects;
import java.util.Random;

public class Gamepanel extends JPanel implements KeyListener, ActionListener {
//create two rectangle for game area
//header snake
    ImageIcon snaketitle=new ImageIcon((getClass().getResource("snakeTitle.jpg")));
//downmouth
    ImageIcon downmouth=new ImageIcon((getClass().getResource("snakeDown.png")));

    ImageIcon upmouth=new ImageIcon((getClass().getResource("snakeUp.png")));
    ImageIcon leftmouth=new ImageIcon((getClass().getResource("snakeLeft.png")));

    ImageIcon rightmouth=new ImageIcon((getClass().getResource("snakeRight.png")));

    ImageIcon enemy=new ImageIcon((getClass().getResource("ball.png")));

    ImageIcon snakeimage=new ImageIcon((getClass().getResource("circle.png")));

    int move=0;
    boolean left=false;
    boolean right=true;   //initially snake is in right direction
    boolean up=false;
    boolean down=false;

    int lengthOfsnake=3;  //initial length is 3
    int[] snakexlength=new int[750];    //for locating snake into 750X750 scr
    int[] snakeylength=new int[750];

    //for making snake movable
    int delay=150;
    Timer time;
    boolean gameover=false;

    int[]xpos={25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,450,475,500,525,550,575,600,625,650,675,700,725,750,775,800,825,850};
   int[]ypos={75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,450,475,500,525,550,575,600,625};

   Random random=new Random();   //using random var to place food in any random position
    int enemyx=150,enemyy=200;
    int score=0;
   Gamepanel()
    {
    addKeyListener(this);
    //whatever key we will press this will listen
        setFocusable(true);
        time=new Timer(delay,this);
        time.start();
    }

    public void newEnemy()
    {
        enemyx=xpos[random.nextInt(34)];
        enemyy=ypos[random.nextInt(23)];
        for(int i=lengthOfsnake-1;i>=0;i--)
        {
            if(snakexlength[i]==enemyx &&snakeylength[i]==enemyy)
            {
                newEnemy();
            }
        }
    }
    //when snake collided with enemy
    public void collidewithenemy()
    {
        //check
        if(snakexlength[0]==enemyx && snakeylength[0]==enemyy)
        {
            newEnemy();
            lengthOfsnake++;
            score++;
            snakexlength[lengthOfsnake-1]=snakexlength[lengthOfsnake-2];   //snake size got inc
            snakeylength[lengthOfsnake-1]=snakeylength[lengthOfsnake-2];
        }
    }
    //when snake body collide game over
    public void collidewithbody()
    {
        for(int i=lengthOfsnake-1;i>0;i--)
        {
            if(snakexlength[i]==snakexlength[0] && snakeylength[i]==snakeylength[0])  //my head is same pos as bodypart
            {
                time.stop();
                gameover=true;
            }
        }
    }
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.white);
        g.drawRect(24,10,851,55);   //create two rectangle top and bottom
        g.drawRect(24,74,851,576);  //x=dist from left scr  y=dist from top

        snaketitle.paintIcon(this,g,25,11);  //snake title jpg insertion

        //color of playing area
        g.setColor(Color.black);   //playing area will be black
        g.fillRect(25,75,851,576);

        if(move==0)   //just started move
        {
            snakexlength[0]=100;  //start
            snakexlength[1]=75;
            snakexlength[2]=50;

            snakeylength[0]=100;
            snakeylength[1]=100;
            snakeylength[2]=100;
            move++;
        }
        //assign head position
        if(left)
        {
            leftmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);
        }
        if(right)
        {
            rightmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);
        }
        if(up)
        {
            upmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);
        }
        if(down)
        {
            downmouth.paintIcon(this,g,snakexlength[0],snakeylength[0]);
        }

        //position to body part
        for(int i=1;i<lengthOfsnake;i++)  //printing body images
        {
            snakeimage.paintIcon(this,g,snakexlength[i],snakeylength[i]);
        }
        enemy.paintIcon(this,g,enemyx,enemyy);
        if(gameover)
        {
            g.setColor(Color.white);
            g.setFont(new Font(Font.SANS_SERIF,Font.BOLD,30));
            g.drawString("Game Over",300,300);
            g.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,10));
            g.drawString("Press the space to retart",330,360);
        }
        //for score and length of snake
        g.setColor(Color.white);
        g.setFont(new Font("Arial",Font.PLAIN,15));
        g.drawString("Score :"+score,750,30);
        g.drawString("Length :"+lengthOfsnake,750,50);
        g.dispose();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
      if(e.getKeyCode()==KeyEvent.VK_SPACE && gameover) //changing head
      {
          restart();
      }
      if(e.getKeyCode()==KeyEvent.VK_LEFT &&(!right))  //!right means snake cannot move opp while moving forward
      {
          left=true;
          right=false;
          up=false;
          down=false;
          move++;
      }
      if(e.getKeyCode()==KeyEvent.VK_RIGHT &&(!left))
      {
          left=false;
          right=true;
          up=false;
          down=false;
          move++;
      }
      if(e.getKeyCode()==KeyEvent.VK_DOWN &&(!up))
      {
          left=false;
          right=false;
          up=false;
          down=true;
          move++;
      }
      if(e.getKeyCode()==KeyEvent.VK_UP &&(!down))
      {
          left=false;
          right=false;
          up=true;
          down=false;
          move++;
      }
    }
    private void restart(){         //restart funtion when snake eat itserlf
        score=0;
        gameover=false;
        move=0;
        lengthOfsnake=3;
        right=true;
        left=false;
        up=false;
        down=false;
        time.start();
        repaint();
    }
    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
       for(int i=lengthOfsnake-1;i>0;i--){
           snakexlength[i]=snakexlength[i-1];  //copying prev values to next in this way snake moves 1 pos ahead
           snakeylength[i]=snakeylength[i-1];
       }                                       //
        if(left)
        {
            snakexlength[0]=snakexlength[0]-25;
        }
        if(right)
        {
            snakexlength[0]=snakexlength[0]+25;
        }
        if(down)
        {
            snakeylength[0]=snakeylength[0]+25;
        }
        if(up)
        {
            snakeylength[0]=snakeylength[0]-25;
        }
        //when snake enters from one end of screen and comes out from opposite end of same axis
        if(snakexlength[0]>850)
            snakexlength[0]=25;
        if(snakexlength[0]<25)
            snakexlength[0]=850;
        if(snakeylength[0]>625)
            snakeylength[0]=75;
        if(snakeylength[0]<75)
            snakeylength[0]=625;

        collidewithenemy();
        collidewithbody();
        repaint();
    }


}
