package me.stonetrench.reworldedit;

public class WEVector3I {
    public int X;
    public int Y;
    public int Z;

    public WEVector3I(int x, int y, int z) {
        X = x;
        Y = y;
        Z = z;
    }
    public WEVector3I(double x, double y, double z) {
        X = (int)Math.floor(x);
        Y = (int)Math.floor(y);
        Z = (int)Math.floor(z);
    }

    public static WEVector3I FloorToInt(WEVector3F other){
        return new WEVector3I(other.X, other.Y, other.Z);
    }

    public WEVector3I Add(WEVector3I other){
        this.X += other.X;
        this.Y += other.Y;
        this.Z += other.Z;
        return this;
    }
    public WEVector3I Sub(WEVector3I other){
        this.X -= other.X;
        this.Y -= other.Y;
        this.Z -= other.Z;
        return this;
    }
    public WEVector3I Mult(WEVector3I other){
        this.X *= other.X;
        this.Y *= other.Y;
        this.Z *= other.Z;
        return this;
    }
    public WEVector3I Clone(){
        return new WEVector3I(X, Y, Z);
    }
    public WEVector3I Offset(int x, int y, int z){
        this.X += x;
        this.Y += y;
        this.Z += z;
        return this;
    }

    @Override
    public String toString() {
        return "[" + X + ", " + Y + ", " + Z + "]";
    }
}
