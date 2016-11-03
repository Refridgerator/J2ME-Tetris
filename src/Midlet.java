import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import java.util.Timer;

public class Midlet extends MIDlet 
{    
    private Display display;
    private TetrisGameCanvas canvas;
    public boolean first_start = true;
    public RecordStores rs;

    
    public Midlet()
    {
        rs = new RecordStores("ES.TETRIS",1);
        display = Display.getDisplay(this);

	canvas = new TetrisGameCanvas();
        canvas.midlet = this;        
    }
    
    public void StartGame()
    {
        canvas.start();        
        display.setCurrent(canvas);
    }
    
    public void startApp() 
    {
        if(first_start)
        {
            int nr=rs.getNumRecords();
            if(nr>0) canvas.HISCORE = rs.getRecord(1);         
        StartGame();
        first_start = false;
        }
        else
        {
            canvas.start();            
        }
    }
    
    public void pauseApp() 
    {
        canvas.stop();
    }
    
    public void destroyApp(boolean unconditional) 
    {
        canvas.stop();
        rs.setRecord (1, canvas.HISCORE); 
        rs.closeRecords();
        first_start = true;
    }
    
    public void exitMIDlet()
    {
	destroyApp(true);
	notifyDestroyed();
    }
}
