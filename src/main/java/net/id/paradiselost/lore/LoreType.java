package net.id.paradiselost.lore;

public enum LoreType{
    NORMAL(0, 17, 22, 22, 3, 3),
    RARE(0, 39, 32, 32, 8, 8),
    ;
    
    private final int u;
    private final int v;
    private final int width;
    private final int height;
    private final int itemX;
    private final int itemY;
    
    LoreType(int u, int v, int width, int height, int itemX, int itemY){
        this.u = u;
        this.v = v;
        this.width = width;
        this.height = height;
        this.itemX = itemX;
        this.itemY = itemY;
    }
    
    public int getU(){
        return u;
    }
    
    public int getV(){
        return v;
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
    
    public int getItemX(){
        return itemX;
    }
    
    public int getItemY(){
        return itemY;
    }
}
