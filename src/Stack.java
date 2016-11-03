public class Stack 
{
    public int[][] cells;
    public int w=12;
    public int h=21;
    
    public Stack() 
    {
        cells = new int[12][22]; 
        Clear();
}
    
    public void Clear()
    {
        for (int i=0;i<w;i++)
        for (int j=0;j<(h+1);j++)
        {
            cells[i][j]=0;
        }
        //border
        for (int j=0;j<h;j++)
        {
            cells[0][j]=11;
            cells[11][j]=11;
        }
        for (int i=0;i<w;i++)
        {
            cells[i][20]=11;
        }
    }
    
    public boolean Intersect(Tetramino t)
    {
        int k = 0;
     
        if((t.x[0]>=0)&&(t.x[0]<w))
        {
            if(t.y[0]>=0) k += cells[t.x[0]][t.y[0]]; 
        }   else k+=1;
        
        if((t.x[1]>=0)&&(t.x[1]<w))
        {
            if(t.y[1]>=0) k += cells[t.x[1]][t.y[1]]; 
        }   else k+=1;
        
        if((t.x[2]>=0)&&(t.x[2]<w))
        {
            if(t.y[2]>=0) k += cells[t.x[2]][t.y[2]]; 
        }   else k+=1;
        
        if((t.x[3]>=0)&&(t.x[3]<w))
        {
            if(t.y[3]>=0) k += cells[t.x[3]][t.y[3]]; 
        }   else k+=1;
        
        if(k==0) return false;
        else return true;
    }
    
    public void Fix(Tetramino t)
    {
        if(t.y[0]>=0) cells[t.x[0]][t.y[0]]=t.color;
        if(t.y[1]>=0) cells[t.x[1]][t.y[1]]=t.color;
        if(t.y[2]>=0) cells[t.x[2]][t.y[2]]=t.color;
        if(t.y[3]>=0) cells[t.x[3]][t.y[3]]=t.color;       
    }
    
    public boolean HasFullLines()
    {
        int n=0;
        for(int j=0;j<(h-1);j++)
        {
            int k=0;
            for(int i=1;i<(w-1);i++)
            {
                if(cells[i][j]==0) k++;
            }
            if (k==0) n++;
        }
        if(n>0) return true;
        else return false;
    }
    
    public boolean IsEmpty ()
    {
        int k=0;
        for(int j=0;j<(h-1);j++)
        {            
            for(int i=1;i<(w-1);i++)
            {
                if(cells[i][j]>0) k++;
            }
        }
        if(k==0) return true;
        else return false;
    }
    
    public void InvertFullLines()
    {
        for(int j=0;j<(h-1);j++)
        {
            int k=0;
            for(int i=1;i<(w-1);i++)
            {
                if(cells[i][j]==0) k++;
            }
            if (k==0) 
                for(int i=1;i<(w-1);i++)
                    cells[i][j] = -cells[i][j];
        }
    }
    
    public int RemoveFullLines()
    {
        int n=0;
        for(int j=(h-2);j>=0;j--)
        {
            int k=0;
            for(int i=1;i<(w-1);i++)
            {
                if(cells[i][j]==0) k++;
            }
            if (k==0) 
            {
                n++;
                for(int ii=1;ii<(w-1);ii++)
                {
                    for(int jj = j;jj>0;jj--)
                    {
                        cells[ii][jj]=cells[ii][jj-1];
                    }
                    cells[ii][0]=0;
                }
                j++;
            }
        }
        return n;
    }
        
}
