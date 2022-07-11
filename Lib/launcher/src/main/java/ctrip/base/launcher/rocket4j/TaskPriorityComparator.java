package ctrip.base.launcher.rocket4j;

import java.util.Comparator;

/**
 * 任务优先级比较器
 */
public class TaskPriorityComparator implements Comparator<Task> {
    @Override
    public int compare(Task o1, Task o2) {
        if(o2.isNeedImmediately() && !o1.isNeedImmediately()){
            return Integer.MAX_VALUE;
        }else if(!o2.isNeedImmediately() && o1.isNeedImmediately()){
            return Integer.MIN_VALUE;
        }
        return o2.getPriority() - o1.getPriority();
    }
}
