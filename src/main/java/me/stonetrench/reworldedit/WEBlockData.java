package me.stonetrench.reworldedit;


public class WEBlockData {
    public WEVector3I position;
    public int Id;
    public int Meta;

    public WEBlockData(WEVector3I position, int id, int meta) {
        this.position = position;
        Id = id;
        Meta = meta;
    }
}
