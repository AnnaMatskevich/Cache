
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static final int MEM_SIZE = 512; // KByte
    public static final int CACHE_SIZE = 2; // KByte
    public static final int CACHE_LINE_SIZE = 32; // Byte
    public static final int CACHE_LINE_COUNT = 64;
    public static final int CACHE_WAY = 4;
    public static final int CACHE_SETS_COUNT = 16;
    public static final int ADDR_LEN = 19;
    public static final int CACHE_TAG_LEN = 10;
    public static final int CACHE_IDX_LEN = 4;
    public static final int CACHE_OFFSET_LEN = 5;
    public final int ADDR1_BUS_LEN = ADDR_LEN;
    public final int ADDR2_BUS_LEN = ADDR_LEN;
    public static final int DATA1_BUS_LEN = 16;
    public static final int DATA2_BUS_LEN = 16;
    public static final int CTR1_BUS_LEN = 3;
    public static final int CTR2_BUS_LEN = 2;

    public static final int M = 64;
    public static final int N = 60;
    public static final int K = 32;
    public static final int aCellSize = 1;
    public static final int bCellSize = 2;
    public static final int cCellSize = 4;
    public static int time;
    public static final Map<Command, Integer> commandTime = Map.of( // recount
            Command.C1_READ8, 1,
            Command.C1_READ16, 1,
            Command.C1_READ32, 2,
            Command.C1_WRITE8, 1,
            Command.C1_WRITE16, 1,
            Command.C1_WRITE32, 1
    );
    public static final Map<Command, Boolean> commandWrite = Map.of(
            Command.C1_READ8, false,
            Command.C1_READ16, false,
            Command.C1_READ32, false,
            Command.C1_WRITE8, true,
            Command.C1_WRITE16, true,
            Command.C1_WRITE32, true
    );
    private static final List<Integer> ans = new ArrayList<>();

    private static void simulateCache(Cache cache) {
        time = 1; // enter to func
        int pa = 0;
        int pb = M * K * aCellSize; // not in code
        int pc = pb + K * N * bCellSize;
        time += 2; // 2 assignments
        time++; // enter for (assignment)
        for (int y = 0; y < M; ++y) {
            time += 2; // new iteration + increment
            if (y == 0) {
                time--;
            } // optimization
            time++; // enter for (assignment)
            for (int x = 0; x < N; ++x) {
                time += 2; // new iteration
                if (x == 0) {
                    time--;
                } // optimization
                pb = M * K * aCellSize;
                time++; // assignment
                // s = 0
                time++; // assignment s
                time++; // enter for (assignment)
                for (int k = 0; k < K; ++k) {
                    time+=2; // new iteration
                    if (k == 0) {
                        time--;
                    } // optimization
                    cache.work(new Address(pa + k * aCellSize), Command.C1_READ8);
                    cache.work(new Address(pb + x * bCellSize), Command.C1_READ16);
                    time += 6;
                    pb += N * bCellSize;
                    time++;
                }
                time++; // exit for
                cache.work(new Address(pc + x * cCellSize), Command.C1_WRITE32);
            }
            time++; // exit for
            pa += K * aCellSize;
            time++; // +
            pc += N * cCellSize;
            time++; // +
        }
        time++; // exit for
        time++; // exit void
        ans.add(cache.getHit());
        ans.add(cache.getRequests());
        ans.add(time);
        /*
        System.out.print(cache.getRequests());
        System.out.print(" ");
        System.out.print(cache.getHit());
        System.out.print(" ");
        System.out.println(time);

         */
    }

    public static void main(String[] args) {
        Cache LRUCache = new Cache(InclusionPolicy.LRU);
        Cache bitPLRUCache = new Cache(InclusionPolicy.bitPLRU);
        simulateCache(LRUCache);
        simulateCache(bitPLRUCache);
        System.out.printf("LRU:\thit perc. %3.4f%%\ttime: %d\npLRU:\thit perc. %3.4f%%\ttime: %d\n", ((double) ans.get(0) * 100) / ans.get(1), ans.get(2), ((double) ans.get(3) * 100) / ans.get(4), ans.get(5));

    }

}
