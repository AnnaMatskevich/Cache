import java.util.ArrayList;
import java.util.List;

public class Cache {
    private List<CacheSet> cache;
    private int hit;
    private int requests;
    public Cache(InclusionPolicy policy) {
        cache = new ArrayList<>(Main.CACHE_SETS_COUNT);
        for (int i = 0; i < Main.CACHE_SETS_COUNT; ++i) {
            cache.add(new CacheSet(policy));
        }
        hit = 0;
        requests = 0;
    }
    public void work(Address address, Command command){
        //System.out.println(address.getIndex());
        hit += cache.get(address.getIndex()).work(command, address.getTag());
        requests++;
    }
    public int getHit() {
        return hit;
    }
    public int getRequests(){
        return requests;
    }
}
