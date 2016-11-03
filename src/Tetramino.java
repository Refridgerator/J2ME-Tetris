import java.util.*;

public class Tetramino 
{
    public int[] x={0,0,0,0,0};
    public int[] y={0,0,0,0,0};

    private int[] xsav={0,0,0,0,0};
    private int[] ysav={0,0,0,0,0};

    private int[] xsav2={0,0,0,0,0};
    private int[] ysav2={0,0,0,0,0};

    public int color = 0;
    public int n = 0; // Номер тетрамины
    public int r = 0; // номер поворота
    private int rsav = 0; 
    private int rsav2 = 0; 
    
        
    private static int[][] Tetramino1_x = {{0,1,0,1}, {0,1,0,1} , {0,1,0,1} , {0,1,0,1} };
    private static int[][] Tetramino1_y = {{0,0,1,1}, {0,0,1,1} , {0,0,1,1} , {0,0,1,1} };

    private static int[][] Tetramino2_x = {{0,1,2,0}, {0,1,1,1} , {0,1,2,2} , {1,1,1,2} };
    private static int[][] Tetramino2_y = {{0,0,0,1}, {0,0,1,2} , {1,1,1,0} , {0,1,2,2} };

    private static int[][] Tetramino3_x = {{0,1,2,2}, {0,1,1,1} , {0,0,1,2} , {1,1,1,2} };
    private static int[][] Tetramino3_y = {{0,0,0,1}, {2,2,1,0} , {0,1,1,1} , {0,1,2,0} };
   
    private static int[][] Tetramino4_x = {{0,1,1,2}, {1,1,2,2} , {0,1,1,2}, {1,1,2,2} }; // возможно тут 2-ую левее
    private static int[][] Tetramino4_y = {{0,0,1,1}, {2,1,1,0} , {0,0,1,1}, {2,1,1,0} };
   
    private static int[][] Tetramino5_x = {{0,1,1,2}, {1,1,2,2} , {0,1,1,2}, {1,1,2,2} };
    private static int[][] Tetramino5_y = {{1,1,0,0}, {0,1,1,2} , {1,1,0,0}, {0,1,1,2} };
   
    private static int[][] Tetramino6_x = {{0,1,2,1}, {0,1,1,1} , {0,1,2,1} , {1,1,1,2} };
    private static int[][] Tetramino6_y = {{0,0,0,1}, {1,0,1,2} , {1,1,1,0} , {0,1,2,1} };
   
    private static int[][] Tetramino7_x = {{0,1,2,3}, {1,1,1,1} , {0,1,2,3}, {1,1,1,1} };
    private static int[][] Tetramino7_y = {{0,0,0,0}, {-1,0,1,2} , {0,0,0,0}, {-1,0,1,2} };
    
    //--------------------------------------------------------
    public Tetramino() 
    {
    }
    //--------------------------------------------------------
    
    public void Set(int num, int rot, int clr)
    {
        this.color = clr;
        this.n=num; if(n<1) n=6;
        this.r=rot;

        switch(n)
        {
            case 1:
                System.arraycopy(Tetramino1_x[r],0,x,0,4);
                System.arraycopy(Tetramino1_y[r],0,y,0,4);
                break;
            case 2:
                System.arraycopy(Tetramino2_x[r],0,x,0,4);
                System.arraycopy(Tetramino2_y[r],0,y,0,4);
                break;
            case 3:
                System.arraycopy(Tetramino3_x[r],0,x,0,4);
                System.arraycopy(Tetramino3_y[r],0,y,0,4);
                break;
            case 4:
                System.arraycopy(Tetramino4_x[r],0,x,0,4);
                System.arraycopy(Tetramino4_y[r],0,y,0,4);
                break;
            case 5:
                System.arraycopy(Tetramino5_x[r],0,x,0,4);
                System.arraycopy(Tetramino5_y[r],0,y,0,4);
                break;
            case 6:
                System.arraycopy(Tetramino6_x[r],0,x,0,4);
                System.arraycopy(Tetramino6_y[r],0,y,0,4);
                break;
            case 7:
                System.arraycopy(Tetramino7_x[r],0,x,0,4);
                System.arraycopy(Tetramino7_y[r],0,y,0,4);
                break;
        }
                x[4]=0;
        y[4]=0;
    }
    
    //--------------------------------------------------------
    public void SaveCoords()
    {
        System.arraycopy(xsav,0,xsav2,0,5);
        System.arraycopy(ysav,0,ysav2,0,5);        
        System.arraycopy(x,0,xsav,0,5);
        System.arraycopy(y,0,ysav,0,5);
        rsav2 = rsav;  rsav = r;    
    }
    
    public void RestoreCoords()
    {
        System.arraycopy(xsav,0,x,0,5);
        System.arraycopy(ysav,0,y,0,5);
        System.arraycopy(xsav2,0,xsav,0,5);
        System.arraycopy(ysav2,0,ysav,0,5);
        r=rsav;    rsav=rsav2;
    }
    
    //--------------------------------------------------------
    public void Down()
    {
        SaveCoords();
        for(int i=0;i<5;i++) y[i]++;            
    }
    
    public void Up()
    {
        SaveCoords();
        for(int i=0;i<5;i++) y[i]--;            
    }
    
    public void Left()
    {
        SaveCoords();
        for(int i=0;i<5;i++) x[i]--;            
    }
    
    public void Right()
    {
        SaveCoords();
        for(int i=0;i<5;i++) x[i]++;            
    }
    
    public void Offset(int dx, int dy)
    {
        SaveCoords();
        for(int i=0;i<5;i++) x[i]+=dx;            
        for(int i=0;i<5;i++) y[i]+=dy;            
    }
    //--------------------------------------------------------
    public void RotateLeft()
    {
        SaveCoords();
        
        //r=(--r+4)%4;
        r++;
        if(r>3) r = 0;
        
        int x0=x[4];
        int y0=y[4];
        
        this.Set(n,r,color);
        x[0]+=x0;
        x[1]+=x0;
        x[2]+=x0;
        x[3]+=x0;
        x[4]=x0;
        
        y[0]+=y0;
        y[1]+=y0;
        y[2]+=y0;
        y[3]+=y0;
        y[4]=y0;
    }
     
    public void RotateRight()
    {
        SaveCoords();
        
        //r=(--r+4)%4;
        r--;
        if(r<0) r = 3;
        
        int x0=x[4];
        int y0=y[4];
        
        this.Set(n,r,color);
        x[0]+=x0;
        x[1]+=x0;
        x[2]+=x0;
        x[3]+=x0;
        x[4]=x0;
        
        y[0]+=y0;
        y[1]+=y0;
        y[2]+=y0;
        y[3]+=y0;
        y[4]=y0;
    }
    
    //--------------------------------------------------------
    
    public void CopyFrom(Tetramino t)
    {
        System.arraycopy(t.x,0,x,0,5);
        System.arraycopy(t.y,0,y,0,5);

        System.arraycopy(x,0,xsav,0,5);
        System.arraycopy(y,0,ysav,0,5);

        System.arraycopy(x,0,xsav2,0,5);
        System.arraycopy(y,0,ysav2,0,5);

        n=t.n;
        r=t.r;
        rsav=r;
        rsav2=r;

        color = t.color;
    }
}

