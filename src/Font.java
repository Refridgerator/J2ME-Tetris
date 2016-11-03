import javax.microedition.lcdui.*;
import javax.microedition.lcdui.game.*;

public class Font {

    private int controlPixels[] = null; //массив значений абсцисс для каждого символа
    private int charindex[] = new int[512]; //массив индексов

    private int fontHeight;
    private String symbols;
    private Image imageFont; 

    public Font(String filename, String symbols)
    {
        this.symbols = symbols;         
        imageFont = null;
        try 
        {
          imageFont = Image.createImage(filename);          
        }
          catch (java.io.IOException e) {}

        try 
        {
            if (imageFont == null)  return; 

            fontHeight = imageFont.getHeight() - 1; //исключить контрольную полоску
            int argb[] = new int[imageFont.getWidth()];
            imageFont.getRGB(argb, 0, imageFont.getWidth(), 0, 0, imageFont.getWidth(), 1);

            initByGreenPixel(argb);
            InitChars();
        }

        catch (Throwable t) {
            t.printStackTrace();
            throw new IllegalArgumentException("Specified font is invalid: " + t);
        }
    }

    private void InitChars()
    {
        for (int i = 0; i < 255; i++) charindex[i] = 0;
            
        for (int i = 0; i < symbols.length(); i++) 
        {
            char c = symbols.charAt(i);
            if(c<256)
                charindex[c] = i;
            else
                charindex[256+c%256] = i;
        }
    }


    private void initByGreenPixel(int argb[]) 
    {
        int count = 0;
        for (int i = 0; i < argb.length; ++i) 
        {
            if ((argb[i]&0x0000ff00) > 32768) count++;
        }

        controlPixels = new int[count];

        int c = 0;
        for (int i = 0; i < argb.length; ++i) 
        {
            if ((argb[i]&0x0000ff00) > 32768) controlPixels[c++] = i;
        }
    }

   private int charsWidth(String ch, int offset, int length) {

        int width = 0;
        for (int i = offset; i < length; ++i) 
        {
            width += charWidth(ch.charAt(i));
        }
        return width;
    }

    private int charWidth(char ch) 
    {
        int index = ch - 0x20; //первый символ является пробелом
        if (index < 0 || index + 1 >= controlPixels.length) {

            index = 0;
        }
        return controlPixels[index + 1] - controlPixels[index];
    }
 

    public int getHeight() 
    {
        return fontHeight;
    }
 

    public int stringWidth(String str) 
    {
        return charsWidth(str, 0, str.length());
    }

 

    public int stringWidth(String str, int offset, int len) 
    {
        return charsWidth(str, offset, len);
    }

 
    public void drawCharIndex(Graphics g, int index, int x, int y, int anchor) 
    {   
        int clipX = g.getClipX();
        int clipY = g.getClipY();
        int clipW = g.getClipWidth();
        int clipH = g.getClipHeight(); 
        
        if (index < 0 || index + 1 >= controlPixels.length)  index = 0;
 
        int index1 = controlPixels[index];
        int index2 = controlPixels[index + 1];
        int width = index2 - index1;
        
        g.setClip(x, y, width, fontHeight);
        g.drawImage(imageFont, x - index1, y - 1, anchor); 
           
        g.setClip(clipX, clipY, clipW, clipH);
    }

     public void drawChar(Graphics g, char character, int x, int y, int anchor) 
    {
        int clipX = g.getClipX();
        int clipY = g.getClipY();
        int clipW = g.getClipWidth();
        int clipH = g.getClipHeight(); 
        
        int c = character;
        if(c>256) c = 256+c%256;
        int index = charindex[c];  
        
        int index1 = controlPixels[index];
        int index2 = controlPixels[index + 1];
        int width = index2 - index1;
        
        g.setClip(x, y, width, fontHeight);
        g.drawImage(imageFont, x - index1, y - 1, anchor); 
           
        g.setClip(clipX, clipY, clipW, clipH);
    }

    private int drawCharInternal(Graphics g, char character, int x, int y, int anchor) 
    {
        int c = character;
        if(c>256) c = 256+c%256;
        int index = charindex[c];        
        
        int index1 = controlPixels[index];
        int index2 = controlPixels[index + 1];
        int width = index2 - index1;
        
        g.setClip(x, y, width, fontHeight);
        g.drawImage(imageFont, x - index1, y - 1, anchor); 
         
        return width;
    }
 

    private void drawChars(Graphics g, String data, int offset, int length, int x, int y, int anchor) {

        if ((anchor & Graphics.RIGHT) != 0) 
        {
            x -= charsWidth(data, offset, length);
        } 
        else 
            if ((anchor & Graphics.HCENTER) != 0) 
        {
            x -= (charsWidth(data, offset, length) >> 1);
        }

        if ((anchor & Graphics.BOTTOM) != 0) 
        {
            y -= fontHeight;
        } 
        else 
            if ((anchor & Graphics.VCENTER) != 0) 
        {
            y -= fontHeight >> 1;
        }

        int clipX = g.getClipX();
        int clipY = g.getClipY();
        int clipW = g.getClipWidth();
        int clipH = g.getClipHeight();

        for (int i = 0; i < length; i++) 
        {
            char c = data.charAt(offset + i);
             x += drawCharInternal(g, c, x, y, Graphics.TOP | Graphics.LEFT);
        }

        g.setClip(clipX, clipY, clipW, clipH);
    } 

    public void drawString(Graphics g, String str, int x, int y, int anchor) 
    {
        drawChars(g, str, 0, str.length(), x, y, anchor);
    } 

    public void drawString(Graphics g, String str, int offset, int len, int x, int y, int anchor) 
    {
        drawChars(g, str, offset, len, x, y, anchor);
    }

}

