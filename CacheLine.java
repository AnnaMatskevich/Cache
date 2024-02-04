public class CacheLine {
    private FlagStatus flagStatus;
    private int flagModified;
    private int tag;
    public CacheLine() {
        flagStatus = FlagStatus.INVALID;
        flagModified = 0;
        tag = 0;
    }
    public int getTag(){
        return tag;
    }
    public FlagStatus getFlagStatus(){
        return flagStatus;
    }
    public int getFlagModified(){
        return flagModified;
    }
    public void setTag(int tag){
        this.tag = tag;
    }
    public void setFlagModified(int flagModified) {
        this.flagModified = flagModified;
    }
    public void setFlagStatus(FlagStatus flagStatus) {
        this.flagStatus = flagStatus;
    }
}
