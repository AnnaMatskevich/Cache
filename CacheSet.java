import java.util.ArrayList;
import java.util.List;

public class CacheSet {
    private List<CacheLine> cacheLines;
    private InclusionPolicy policy;

    public CacheSet(InclusionPolicy policy) {
        cacheLines = new ArrayList<>(Main.CACHE_WAY);
        for (int i = 0; i < Main.CACHE_WAY; ++i) {
            cacheLines.add(new CacheLine());
            if (policy == InclusionPolicy.LRU) {
                cacheLines.get(i).setFlagModified(i);
            }
        }
        this.policy = policy;
    }

    private void LRUwrite(int tag, Command command) {
        for (int i = 0; i < Main.CACHE_WAY; ++i) {
            if (cacheLines.get(i).getFlagModified() == 0) {
                if (cacheLines.get(i).getFlagStatus() == FlagStatus.MODIFIED) {
                    Main.time += 102;
                }
                cacheLines.get(i).setTag(tag);
                LRUpdateFlagModified(cacheLines.get(i).getFlagModified());
                cacheLines.get(i).setFlagModified(Main.CACHE_WAY - 1);
                cacheLines.get(i).setFlagStatus(FlagStatus.SHARED);
                if (Main.commandWrite.get(command)) {
                    cacheLines.get(i).setFlagStatus(FlagStatus.MODIFIED);
                }
                return;
            }
        }
        System.out.println("error");
    }

    private void bitPLRUwrite(int tag, Command command) {
        for (int i = 0; i < Main.CACHE_WAY; ++i) {
            if (cacheLines.get(i).getFlagModified() == 0) {
                if (cacheLines.get(i).getFlagStatus() == FlagStatus.MODIFIED) {
                    Main.time += 102;
                }
                cacheLines.get(i).setTag(tag);
                cacheLines.get(i).setFlagModified(1);
                bitPLRUUpdateFlagModified(tag);
                if (Main.commandWrite.get(command)) {
                    cacheLines.get(i).setFlagStatus(FlagStatus.MODIFIED);
                } else {
                    cacheLines.get(i).setFlagStatus(FlagStatus.SHARED);
                }
                return;
            }
        }
        System.out.println("error");
        bitPLRUUpdateFlagModified(tag);
    }

    private void LRUpdateFlagModified(int flag) {
        for (int i = 0; i < Main.CACHE_WAY; ++i) {
            if (cacheLines.get(i).getFlagModified() > flag) {
                cacheLines.get(i).setFlagModified(cacheLines.get(i).getFlagModified() - 1);
            }
        }
    }

    private void bitPLRUUpdateFlagModified(int tag) {
        int ZeroCount = 0;
        for (int i = 0; i < Main.CACHE_WAY; ++i) {
            if (cacheLines.get(i).getFlagModified() == 0) {
                ZeroCount++;
            }
        }
        if (ZeroCount == 0) {
            for (int i = 0; i < Main.CACHE_WAY; ++i) {
                if (cacheLines.get(i).getTag() != tag) {
                    cacheLines.get(i).setFlagModified(0);
                }
            }
        }
    }

    public int work(Command command, int tag) {
        Main.time++;
        for (int i = 0; i < Main.CACHE_WAY; ++i) {
            if (cacheLines.get(i).getTag() == tag && !(cacheLines.get(i).getFlagStatus() == FlagStatus.INVALID)) {
                switch (policy) {
                    case LRU:
                        LRUpdateFlagModified(cacheLines.get(i).getFlagModified());
                        cacheLines.get(i).setFlagModified(Main.CACHE_WAY - 1);
                        break;
                    case bitPLRU:
                        cacheLines.get(i).setFlagModified(1);
                        bitPLRUUpdateFlagModified(tag);
                        break;
                }
                if (Main.commandWrite.get(command)) {
                    cacheLines.get(i).setFlagStatus(FlagStatus.MODIFIED);
                }
                Main.time += 6;
                Main.time += Main.commandTime.get(command);
                return 1;
            }
        }
        Main.time += 4;
        Main.time++;
        Main.time += 100;
        Main.time += Main.CACHE_LINE_SIZE / Main.DATA2_BUS_LEN;
        switch (policy) {
            case LRU:
                LRUwrite(tag, command);
                break;
            case bitPLRU:
                bitPLRUwrite(tag, command);
                break;
        }
        Main.time += Main.commandTime.get(command);
        return 0;
    }

}
