public class Address {
    private int tag;
    private int index;
    public Address(int address) {
        address /= 1 << Main.CACHE_OFFSET_LEN;
        this.tag = address / (1 << Main.CACHE_IDX_LEN);
        this.index = address % (1 << Main.CACHE_IDX_LEN);
    }
    public int getTag() {
        return tag;
    }
    public int getIndex() {
        return index;
    }
}
