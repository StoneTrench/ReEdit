package me.stonetrench.reworldedit;

public class WEVector3F {
    public float X;
    public float Y;
    public float Z;

    public WEVector3F(float x, float y, float z) {
        X = x;
        Y = y;
        Z = z;
    }

    public WEVector3F Add(WEVector3F other){
        this.X += other.X;
        this.Y += other.Y;
        this.Z += other.Z;
        return this;
    }
    public WEVector3F Offset(float x, float y, float z){
        return new WEVector3F(X + x, Y + y, Z + z);
    }
}
