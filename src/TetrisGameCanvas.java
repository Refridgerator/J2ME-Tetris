import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;
import java.util.Random;


public class TetrisGameCanvas extends GameCanvas implements CommandListener, Runnable
{
        public Midlet midlet;
        private volatile boolean inWork;
        public int width;
        public int height;
        public  Graphics g=null;

        private long time = 0;
        
        private int KL;
        private int KR;
        private int KU;
        private int KD;
        private int KF;
        
        public int LEVEL=1;
        public int SCORE=0;
        public int HISCORE=0;
        public int LINES=0;
        public int FALLS=0;
        public long StartTime=-1;
        public long level_delay=900;

	public static final int
	MODE_MENU=1,
	MODE_GAMEOVER=2,
	MODE_NEWFIGURE=3,
	MODE_FALL=4,
	MODE_FALLFAST=5,
	MODE_FLASHLINES=6,
	MODE_PAUSE=7,
	MODE_MEGACOOL=8;

        private int[] rndp = {1,1,2,3,4,5,6,6,7,7,1,1,2,3,4,5,6,6,7,7,7};

        private int[][] rndc = { 
                                 {1,1,2,2,3,3,6,6,4,2},
                                 {1,1,2,2,3,3,6,6,4,2},
                                 {2,2,3,3,4,6,1,5,7,9},
                                 {7,7,8,8,8,9,9,9,10,10},
                                 {7,7,8,8,8,9,9,9,10,10},
                                 {1,8,8,9,9,10,10,1,5,5},
                                 {10,2,3,4,2,3,4,2,3,4},
        
                                 {1,1,2,2,3,3,6,6,4,4},
                                 {1,1,2,2,3,3,6,6,4,5},
                                 {2,2,3,3,4,6,1,5,5,9},
                                 {7,7,8,8,8,9,9,9,10,10},
                                 {7,7,8,8,8,9,9,9,10,10},
                                 {6,8,8,9,9,10,10,4,4,4},
                                 {10,2,3,4,2,3,4,2,3,4},
        
                                 {1,1,2,2,3,3,6,6,4,5},
                                 {1,1,2,2,3,3,6,6,4,5},
                                 {2,2,3,3,4,6,1,5,7,9},
                                 {7,7,8,8,8,9,9,9,10,10},
                                 {7,7,8,8,8,9,9,9,10,10},
                                 {7,8,8,9,9,10,10,6,6,6},
                                 {10,2,3,4,2,3,4,2,3,4}
        
        };
                                 
          
	public static int mode=MODE_NEWFIGURE;
	public static int mode2=MODE_GAMEOVER;
        Thread t;
                
	public Command cmdExit = new Command("Exit", Command.EXIT, 1);
	public Command cmdPause = new Command("Pause", Command.OK, 2);
	public Command cmdResume = new Command("Resume", Command.OK, 2);
	public Command cmdStart = new Command("Start", Command.OK, 2);
        private Command cmdSet = new Command(" ", Command.OK, 0);
        
        public DrawBoard board; 
        
        private Random _rnd = new Random();    
        public Stack stack = new Stack();
        public Tetramino figure = new Tetramino();
        public Tetramino figure_next = new Tetramino();
        public Tetramino figure_tmp = new Tetramino();
    
    /**
     * Creates a new instance of TetrisGameCanvas
     */
    public TetrisGameCanvas()
    {
        super(true);

        StartTime=System.currentTimeMillis();

        KL = getKeyCode(LEFT);
        KR = getKeyCode(RIGHT);
        KU = getKeyCode(UP);
        KD = getKeyCode(DOWN);
        KF = getKeyCode(FIRE);
        
        if(KL==KEY_NUM4) // нокия...
        {
            KL = -3;
            KR = -4;
            KU = -1;
            KD = -2;
            KF = 0;
            
            addCommand(cmdSet);
        }

        addCommand(cmdExit);
        addCommand(cmdPause);
        setCommandListener(this);

        //setFullScreenMode(true);
        g = getGraphics();
        width = getWidth();
        height = getHeight();

        board = new DrawBoard(this);
        board.stack = stack;        
        //
        clear();
        board.Clear();
        board.DrawHiScore();        
        
    }


    public void start()
    {
        //figure_next.Set(1, 1, 1+rnd(4));

        inWork = true;
        t = new Thread(this);
        t.start(); 
    }

    public void stop()
    {
        inWork = false;
        try
        {
        t.join();
        }
        catch (InterruptedException ie) {}
    }

    public void clear()
    {
        g.setColor(0x00000000);
        g.fillRect(0, 0, width, height);
        board.MarkUp();
        board.DrawAll();
    }

    public void Sleep(long millis)
    {
        try { Thread.sleep(millis); }
        catch (InterruptedException ie) { stop(); }
    }

    public int rnd(int n)
    {
        return Math.abs(_rnd.nextInt()) % n;
    }

    //-----------------------//
    // T H R E A D   L O O P //
    //-----------------------//

    public void run()
    {
        time = System.currentTimeMillis();
        _rnd.setSeed(System.currentTimeMillis());
        figure_next.Set(rndp[rnd(21)], rnd(4), rndc[(FALLS/20)%21][rnd(10)]);

        Refresh();
        if(mode==MODE_PAUSE) board.DrawPause();

        // цикл
        while (inWork)
        {
            long start = System.currentTimeMillis();

//-------------------------------//
//---------   P A U S E   -------//
//-------------------------------//
            if (mode == MODE_PAUSE)
            {
                //board.DrawPause();
                if((start-time)%4000<2000)
                board.DrawScore();
                else board.DrawHiScore();
                Sleep(100);
                flushGraphics();
                continue;
            }
           

           /*if (mode == MODE_MENU)
            {
                board.DrawHiScore();
                Sleep(100);
                flushGraphics();
                continue;
            }/**/

//-------------------------------//
//---   N E W   F I G U R E   ---//
//-------------------------------//
            if (mode == MODE_NEWFIGURE)
            {
                FALLS++;
                figure.CopyFrom(figure_next);
                figure.Offset(4, 0);
                if(figure.n==1) figure.Offset(1, 0);
                if(figure.n==2 && figure.r==1) figure.Offset(1, 0);
                if(figure.n==3 && figure.r==1) figure.Offset(1, 0);
                if(figure.n==6 && figure.r==1) figure.Offset(1, 0);
                
                    
                //if(figure.n)
                figure.SaveCoords();
                if (stack.Intersect(figure))
                {
                    mode = MODE_GAMEOVER;
                    continue;
                }

                figure_next.Set(rndp[rnd(21)], rnd(4), rndc[(FALLS/20)%21][rnd(10)]);
                //figure_next.Set(1, 1, 1+rnd(4));
                board.DrawPreview(figure_next);
                //figure_next.Set(7,5);
                mode = MODE_FALL;
                time = System.currentTimeMillis();
                Refresh();
                continue;
            }

//-------------------------//
//---   F A L L I N G   ---//
//-------------------------//
            if (mode == MODE_FALL)
            {
                readkeys();
                if ((System.currentTimeMillis() - time) < level_delay)
                {
                    //Sleep(0);
                    continue;
                }
                SCORE++;
                time = System.currentTimeMillis();
                board.DrawTetramino(figure, false);
                figure.Down();
                if (stack.Intersect(figure))
                {
                    figure.RestoreCoords();
                    stack.Fix(figure);
                    if (stack.HasFullLines())
                    {
                        mode = MODE_FLASHLINES;
                        time = System.currentTimeMillis();
                    }
                    else mode = MODE_NEWFIGURE;
                }
                board.DrawTetramino(figure, true);
                board.DrawScoreValueOnly();
                flushGraphics();
                // Refresh();      
                continue;
            }

//---------------------------------//
//---  F A S T    F A L L I N G ---//
//---------------------------------//
            if (mode == MODE_FALLFAST)
            {
                SCORE+=2;
                board.DrawTetramino(figure, false);
                figure.Down();
                if (stack.Intersect(figure))
                {
                    figure.RestoreCoords();
                    stack.Fix(figure);
                    if (stack.HasFullLines())
                    {
                        mode = MODE_FLASHLINES;
                        time = System.currentTimeMillis();
                    }
                    else mode = MODE_NEWFIGURE;
                }
                // Refresh();    
                board.DrawTetramino(figure, true);
                board.DrawScoreValueOnly();
                flushGraphics();
                //Sleep(25);              
                continue;
            }

//-------------------------------//
//---  F L A S H   L I N E S  ---//
//-------------------------------//
            if (mode == MODE_FLASHLINES)
            {
                //if ((System.currentTimeMillis() - time) < 400)
                {
                    stack.InvertFullLines();
                    //Sleep(100);
                    RefreshStackOnly();
                    Sleep(100);
                    //continue;
                }
                int ls = stack.RemoveFullLines();
                //
                LINES += ls;
                SCORE += ls*10*LEVEL*Math.max(1,ls-1);
                
                //LEVEL++;
                LEVEL = Math.min(15,1+LINES/10);
                switch(LEVEL)
                {
                    case 1: level_delay=900; break;
                    case 2: level_delay=750; break;
                    case 3: level_delay=550; break;
                    case 4: level_delay=400; break;
                    case 5: level_delay=350; break;
                    case 6: level_delay=300; break;
                    case 7: level_delay=275; break;
                    case 8: level_delay=250; break;
                    case 9: level_delay=225; break;
                    case 10: level_delay=200; break;
                    case 11: level_delay=180; break;
                    case 12: level_delay=160; break;
                    case 13: level_delay=140; break;
                    case 14: level_delay=120; break;
                    case 15: level_delay=100; break;                    
                }
                //
                if(stack.IsEmpty())
                {
                    SCORE+=5000;
                    board.DrawScore();
                    board.DrawMegaCool();
                    flushGraphics();
                    Sleep(2000);
                }
                //
                //board.Clear();
                board.DrawScore();

                flushGraphics();
                time = System.currentTimeMillis();
                mode = MODE_NEWFIGURE;
                continue;
            }
            
//----------------------------//
//---  G A M E    O V E R  ---//
//----------------------------//
            if (mode == MODE_GAMEOVER)
            {
                time = System.currentTimeMillis();
                board.DrawGameOver();
                removeCommand(cmdPause);
                addCommand(cmdStart);
                mode = MODE_PAUSE;
                if(SCORE>HISCORE) 
                {
                    HISCORE=SCORE;
                }
                
                flushGraphics(); 
                setFullScreenMode(true);
                setFullScreenMode(false);
            }
        }
    }

    
        public void RefreshStackOnly()
    {
        board.DrawStack();
        flushGraphics();
    }

    public void Refresh()
    {
        board.Clear();
        board.DrawScore();
        board.DrawStack();
        board.DrawPreview(figure_next);
        board.DrawTetramino(figure, true);
        flushGraphics();
    }
    
    public void ResetScores()
    {
        LEVEL=1;
        SCORE=0;
        LINES=0;
        level_delay=900;
        FALLS=0;
    }

    //------------------------------------------------//    
    //------------------- COMMANDS -------------------//
    //------------------------------------------------//    

    public void commandAction(Command c, Displayable d)
    {
        if (c == cmdExit)
        {
            midlet.exitMIDlet();
        }
        else
            if (c == cmdStart)
            {
                stack.Clear();
                mode = MODE_NEWFIGURE;
                ResetScores();
                removeCommand(cmdStart);
                addCommand(cmdPause);
                Refresh();
            }
            if (c == cmdPause)
            {
                time = System.currentTimeMillis();

                removeCommand(cmdPause);
                addCommand(cmdResume);
                mode2=mode;
                mode = MODE_PAUSE;
                board.DrawPause();
                flushGraphics();
            }
            if (c == cmdResume)
            {
                keyTrigger = 0xffffffff;
                removeCommand(cmdResume);
                addCommand(cmdPause);
                mode=mode2;
                Refresh();
            }
    }

    


    //------------------------------------------------------------
    //----------------------- K E Y S ----------------------------
    //------------------------------------------------------------
    private int keyTrigger = 0;
    private void readkeys()
    {
        int keys = getKeyStates();

        int inv = 0xffffffff - keyTrigger;
        int key = inv & keys;
        keyTrigger &= keys;

        if ((key & RIGHT_PRESSED) != 0)
        {
            OnRight();
            keyTrigger |= RIGHT_PRESSED;
        }
        if ((key & LEFT_PRESSED) != 0)
        {
            OnLeft();
            keyTrigger |= LEFT_PRESSED;
        }
        if ((key & DOWN_PRESSED) != 0)
        {
            OnDown();
            keyTrigger |= DOWN_PRESSED;
        }
        if ((key & UP_PRESSED) != 0)
        {
            OnUp();
            keyTrigger |= UP_PRESSED;
        }
        if ((key & FIRE_PRESSED) != 0)
        {
            //OnFire();
            OnDown();
            keyTrigger |= FIRE_PRESSED;
        }        
        if ((key & GAME_C_PRESSED) != 0)
        {
            OnFire();
            keyTrigger |= GAME_C_PRESSED;
        }
    }
    //------------------------------------------------------------
    //------------------------------------------------------------
    private void OnLeft()
    {
        SCORE-=5;
        board.DrawTetramino(figure, false);
        figure.Left();
        if (stack.Intersect(figure)) figure.RestoreCoords();
        board.DrawTetramino(figure, true);
        board.DrawScoreValueOnly();
        flushGraphics();
    }

    private void OnRight()
    {
        SCORE-=5;
        board.DrawTetramino(figure, false);
        figure.Right();
        if (stack.Intersect(figure)) figure.RestoreCoords();
        board.DrawTetramino(figure, true);
        board.DrawScoreValueOnly();
        flushGraphics();
    }

    private void OnUp()
    {
        SCORE-=5;
        board.DrawTetramino(figure, false);
         figure.RotateLeft();
            do
            {
                if(stack.Intersect(figure))
                {
                    // пробуем подвинуть влево или вправо
                    figure.Left();
                    if(stack.Intersect(figure)) 
                        figure.RestoreCoords();   
                        else break;                        
                    figure.Right();
                    if(stack.Intersect(figure))
                            figure.RestoreCoords();   
                        else break;
                    figure.RestoreCoords();
                }
            } while(false);

        board.DrawTetramino(figure, true);
        board.DrawScoreValueOnly();
        flushGraphics();
    }

    private void OnDown()
    {
        mode = MODE_FALLFAST;
        time = System.currentTimeMillis();
    }

    private void OnFire()
    { 
        // mode = MODE_FALLFAST;
       // time = System.currentTimeMillis();
        
        board.DrawTetramino(figure,false);
        
        figure_tmp.CopyFrom(figure);
        figure.CopyFrom(figure_next);
        
        figure.Offset(figure_tmp.x[4],figure_tmp.y[4]);

        if(!stack.Intersect(figure))
        {
            figure_next.CopyFrom(figure_tmp);
            figure_next.Offset(-figure_tmp.x[4],-figure_tmp.y[4]);

            board.DrawPreview(figure_next);
            board.DrawTetramino(figure,true);
            SCORE-=100;
            board.DrawScoreValueOnly();
            flushGraphics();            
        }
        else
        {
            figure.CopyFrom(figure_tmp);
        }
    }


}
