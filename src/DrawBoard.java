
import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;


public class DrawBoard 
{
    public Graphics g = null;
    public int width = 0;
    public int height = 0;

    int cellsize=8;
    
    TetrisGameCanvas canvas;
    
    public Font font;
    private Image[] images;
   
   
    public Stack stack;
    
    // mark
       public int h_font;
       public int w_stack;
       public int h_stack;
       public int w_preview;
       public int w_next;
       public int w_score;
       public int w_lines;
       public int w_level;
       public int w_gameover;
       public int w_pause;
       public int w_megacool;
       public int w_r;
       public int w_all;
       public int ofsx_stack;
       public int ofsy_stack;
       public int ofsx_level;
       public int ofsy_level;
       public int ofsx_preview;
       public int ofsy_preview;
       public int centerx_r;
       public int fs = 2;
    
    /**
     * Creates a new instance of DrawBoard
     */
    public DrawBoard(TetrisGameCanvas canvas) 
    {
        // init
        this.canvas=canvas;
        this.g = canvas.g;
        this.width = canvas.width;
        this.height = canvas.height;
        
       //font = new Font("/ESFont.png"," 0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&'\".,:;-+*=/\\?()<>[]{}_|¿¡¬√ƒ≈®∆«»… ÀÃÕŒœ–—“”‘’÷◊ÿŸ⁄€‹›ﬁﬂ‡·‚„‰Â∏ÊÁËÈÍÎÏÌÓÔÒÚÛÙıˆ˜¯˘˙˚¸˝˛ˇ");
       font = new Font("/font.png"," !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~π");
        // loading image
       int sz = 4;
       if(height>(6*21)) {sz = 6; fs = 2;}
       if(height>(8*21)) {sz = 8; fs = 3;}
       if(height>(10*21)) {sz = 10; fs = 4;}
       if(height>(12*21)) {sz = 12; fs = 5;}
       Image temp = null;
       images = new Image[12];
       String s="";
       try 
        {
           /*
                  temp =  Image.createImage("/cell10.png");
          images[10] = Image.createImage(temp, 0, 0, sz, sz, Sprite.TRANS_NONE);
                  temp =  Image.createImage("/cell9.png");
          images[9] = Image.createImage(temp, 0, 0, sz, sz, Sprite.TRANS_NONE);
                  temp =  Image.createImage("/cell8.png");
          images[8] = Image.createImage(temp, 0, 0, sz, sz, Sprite.TRANS_NONE);
                  temp =  Image.createImage("/cell7.png");
          images[7] = Image.createImage(temp, 0, 0, sz, sz, Sprite.TRANS_NONE);
                  temp =  Image.createImage("/cell6.png");
          images[6] = Image.createImage(temp, 0, 0, sz, sz, Sprite.TRANS_NONE);
                  temp =  Image.createImage("/cell5.png");
          images[5] = Image.createImage(temp, 0, 0, sz, sz, Sprite.TRANS_NONE);
                  temp =  Image.createImage("/cell4.png");
          images[4] = Image.createImage(temp, 0, 0, sz, sz, Sprite.TRANS_NONE);
                  temp =  Image.createImage("/cell3.png");
          images[3] = Image.createImage(temp, 0, 0, sz, sz, Sprite.TRANS_NONE);
                  temp =  Image.createImage("/cell2.png");
          images[2] = Image.createImage(temp, 0, 0, sz, sz, Sprite.TRANS_NONE);
                  temp =  Image.createImage("/cell1.png");
          images[1] = Image.createImage(temp, 0, 0, sz, sz, Sprite.TRANS_NONE);
                  temp =  Image.createImage("/none.png");
          images[0] =  Image.createImage(temp, 0, 0, sz, sz, Sprite.TRANS_NONE);
                  temp =  Image.createImage("/cell_w.png");
          images[11] =Image.createImage(temp, 0, 0, sz, sz, Sprite.TRANS_NONE); 
          /**/
           temp =  Image.createImage("/blocks.png");
           for(int i = 0;i<12;i++) 
               images[i] =Image.createImage(temp, i*16, 0, sz, sz, Sprite.TRANS_NONE); 
        }
          catch (java.io.IOException e) 
          {
              s = e.getMessage();
          }
       cellsize = sz;     
        
        this.Clear();
    }
    

    public void Clear()
    {
        g.setColor(0);
        g.fillRect(0,0,width,height);
    }
    
    
    public void MarkUp()
    {
       h_font = font.getHeight();
       w_stack = cellsize*13;
       h_stack = cellsize*21;
       w_preview = cellsize*4+1;
       //w_next = font.stringWidth("TETRIS");
       w_next = font.stringWidth("next");
       w_score = font.stringWidth("Score");
       w_lines = font.stringWidth("Lines");
       w_level = font.stringWidth("Level");
       w_gameover = font.stringWidth("GAME OVER");
       w_pause = font.stringWidth("PAUSE");
       w_megacool = font.stringWidth("COOL!!!");
       w_r = Math.max(w_preview, Math.max(w_next, Math.max(w_score, Math.max(w_lines,w_level)))) + cellsize*1;
       w_all = w_stack + w_r;
       
       ofsx_stack = (width-w_all)/2;
       if(ofsx_stack<cellsize) ofsx_stack=0;
      

       ofsy_stack = (height-h_stack)/1;
       if(ofsy_stack>(3*cellsize)) ofsy_stack = (height-h_stack)*2/3;
       //ofsx_preview = ofsx_stack +w_stack+(w_r-w_preview)/2;
       ofsx_preview = (width-w_stack-w_preview)/2+w_stack;
       
       ofsy_preview = ofsy_stack+cellsize*1;
       centerx_r =ofsx_preview+w_preview/2;
       //ofsy_level = ofsy_stack + w_preview +cellsize*3;
       
       ofsx_level = centerx_r-w_r/2;
       int h_scores =  h_font*(2*fs+2)+cellsize;
       int t = (height-w_preview-ofsy_preview-cellsize*1);
       ofsy_level = w_preview+ofsy_preview+cellsize*1+(t-h_scores)/2;
       //if((ofsx_level-w_stack)<(2*cellsize))
       {
          // ofsx_stack = (ofsx_level-w_stack)/2; 
       }
       
       /**/
    }

   public void DrawAll()
  {
        DrawStack();
        DrawPreview();
        DrawScore();
   }
   
   public void DrawTetramino(Tetramino t, boolean vis)
   {
       if(vis)
       {
        if(t.y[0]>-1) g.drawImage(images[t.color], cellsize*t.x[0]+ofsx_stack, cellsize*t.y[0]+ofsy_stack, 0); 
        if(t.y[1]>-1) g.drawImage(images[t.color], cellsize*t.x[1]+ofsx_stack, cellsize*t.y[1]+ofsy_stack, 0); 
        if(t.y[2]>-1) g.drawImage(images[t.color], cellsize*t.x[2]+ofsx_stack, cellsize*t.y[2]+ofsy_stack, 0); 
        if(t.y[3]>-1) g.drawImage(images[t.color], cellsize*t.x[3]+ofsx_stack, cellsize*t.y[3]+ofsy_stack, 0); 
       }
       else
       {
        if(t.y[0]>-1) g.drawImage(images[0], cellsize*t.x[0]+ofsx_stack, cellsize*t.y[0]+ofsy_stack, 0); 
        if(t.y[1]>-1) g.drawImage(images[0], cellsize*t.x[1]+ofsx_stack, cellsize*t.y[1]+ofsy_stack, 0); 
        if(t.y[2]>-1) g.drawImage(images[0], cellsize*t.x[2]+ofsx_stack, cellsize*t.y[2]+ofsy_stack, 0); 
        if(t.y[3]>-1) g.drawImage(images[0], cellsize*t.x[3]+ofsx_stack, cellsize*t.y[3]+ofsy_stack, 0); 
       }
           
   }
    
   //----------------------------------
   public void DrawStack()
   {
        for(int j=0;j<stack.h;j++)
        {
            int jj=j*cellsize+ofsy_stack;
            for(int i=0;i<stack.w;i++)
            {
                int ii=i*cellsize;
                if(stack.cells[i][j]<0) 
                    g.drawImage(images[0],ii+ofsx_stack,jj,0); 
                else
                    g.drawImage(images[stack.cells[i][j]],ii+ofsx_stack,jj,0); 
            }
        }
   }
   
   //-----------------------------------
   public void DrawPreview()
   {   
        //font.drawString(g,"next",ofsx_preview+(w_preview-w_next)/2, ofsy_stack, 0);
        //font.drawString(g,"TETRIS",centerx_r-w_next/2, ofsy_stack, 0);

        for(int j=0;j<4;j++)
        {
            int jj=j*cellsize+ofsy_preview;
            for(int i=0;i<4;i++)
            {
                int ii=i*cellsize+ofsx_preview;                    
                g.drawImage(images[0],ii,jj,0); 
            }
        }
        for(int j=0;j<4;j++)
            g.drawRegion(images[0], 0, 0, 1, cellsize, 0, cellsize*4+ofsx_preview, cellsize*j+ofsy_preview, 0);
        for(int i=0;i<4;i++)
            g.drawRegion(images[0], 0, 0, cellsize, 1, 0, cellsize*i+ofsx_preview, cellsize*4+ofsy_preview, 0);
           g.drawRegion(images[0], 0, 0, 1, 1, 0, cellsize*4+ofsx_preview, cellsize*4+ofsy_preview, 0);
         }
      
   public void DrawPreview(Tetramino t)
   {
         DrawPreview();
         int min_x=Math.min(t.x[0], Math.min(t.x[1],Math.min(t.x[2],t.x[3])));
         int max_x=Math.max(t.x[0], Math.max(t.x[1],Math.max(t.x[2],t.x[3])));
         int min_y=Math.min(t.y[0], Math.min(t.y[1],Math.min(t.y[2],t.y[3])));
         int max_y=Math.max(t.y[0], Math.max(t.y[1],Math.max(t.y[2],t.y[3])));
         
         int wx = max_x-min_x;
         int wy = max_y-min_y;

         int ox = ofsx_preview + cellsize*((4-wx)/2-min_x);
         int oy = ofsy_preview + cellsize*((4-wy)/2-min_y);
        g.drawImage(images[t.color], cellsize*t.x[0]+ox, cellsize*t.y[0]+oy, 0); 
        g.drawImage(images[t.color], cellsize*t.x[1]+ox, cellsize*t.y[1]+oy, 0); 
        g.drawImage(images[t.color], cellsize*t.x[2]+ox, cellsize*t.y[2]+oy, 0); 
        g.drawImage(images[t.color], cellsize*t.x[3]+ox, cellsize*t.y[3]+oy, 0); 
    }
      
   //------------------------------------------
      public void DrawScore()
   {
         DrawTextRect(centerx_r-w_r/2, ofsy_level-cellsize/2, w_r, h_font*(2*fs+2)+cellsize, 0xff001100,0xff222200);
          
         font.drawString(g,"Level", centerx_r, ofsy_level+h_font*0, g.HCENTER);
         font.drawString(g,"Lines", centerx_r, ofsy_level+h_font*fs, g.HCENTER);
         font.drawString(g,"Score", centerx_r, ofsy_level+h_font*2*fs, g.HCENTER);

         font.drawString(g,Integer.toString(canvas.LEVEL), centerx_r, ofsy_level+h_font*1, g.HCENTER);
         font.drawString(g,Integer.toString(canvas.LINES), centerx_r, ofsy_level+h_font*(fs+1), g.HCENTER);
         font.drawString(g,Integer.toString(canvas.SCORE), centerx_r, ofsy_level+h_font*(2*fs+1), g.HCENTER);
      }
      
      public void DrawScoreValueOnly()
   {
          g.setColor(0xff001100);
         g.fillRect(centerx_r-w_r/2, ofsy_level+h_font*(2*fs+1), w_r, h_font);
         
          
         font.drawString(g,Integer.toString(canvas.SCORE), centerx_r, ofsy_level+h_font*(2*fs+1), g.HCENTER);
      }
      
      public void DrawHiScore()
      { 
         int hhs = h_font*(2*fs+2)+cellsize;
         int ohs = ofsy_level +(hhs-h_font*4)/2;  
         DrawTextRect(centerx_r-w_r/2, ofsy_level-cellsize/2, w_r, hhs , 0xff110000,0xff222200);
          
         font.drawString(g,"High", centerx_r, ohs, g.HCENTER);
         font.drawString(g,"Score", centerx_r, ohs+h_font*1, g.HCENTER);

         font.drawString(g,Integer.toString(canvas.HISCORE), centerx_r, ohs+h_font*3, g.HCENTER);
      }
      
      public void DrawGameOver()
      {    
         DrawTextRect(ofsx_stack+(w_stack-w_gameover)/2, ofsy_stack+(h_stack-h_font)/2, w_gameover,h_font,0xff550000,0xff770000);
         font.drawString(g,"GAME OVER", ofsx_stack+(w_stack-w_gameover)/2, ofsy_stack+(h_stack-h_font)/2, 0);
      }      
      
      public void DrawPause()
      {    
         DrawTextRect(ofsx_stack+(w_stack-w_pause)/2, ofsy_stack+(h_stack-h_font)/2, w_pause,h_font,0xff005555,0xff007777);
         font.drawString(g,"PAUSE", ofsx_stack+(w_stack-w_pause)/2, ofsy_stack+(h_stack-h_font)/2, 0);
      }

      public void DrawMegaCool()
      {    
         DrawTextRect(ofsx_stack+(w_stack-w_megacool)/2, ofsy_stack+(h_stack-h_font)/2, w_megacool,h_font,0xff555555,0xff777777);
         font.drawString(g,"COOL!!!", ofsx_stack+(w_stack-w_megacool)/2, ofsy_stack+(h_stack-h_font)/2, 0);
      }

    //-----------------------------------------------------------------------------
   public void DrawTextRect(int x, int y, int w, int h, int color1, int color2)
   {
       g.setColor(color2);
       g.drawRect(x-2,y-1,w+3,h+2);
       g.setColor(color1);
       g.fillRect(x-1,y,w+2,h+1);
   }
    //----------------------------------------------------------------------------

    
    
    
}    

