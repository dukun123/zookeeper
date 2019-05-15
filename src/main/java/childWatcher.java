/**
 * @author kdu@instartllogic.com
 * @date 2019-05-08 11:25
 */

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
public class childWatcher implements Watcher{

    public void process(WatchedEvent event) {

        System.out.println("==========childern start==============");

        System.out.println("childern state: " + event.getState().name());

        System.out.println("childern type: " + event.getType().name());

        System.out.println("childern path: " + event.getPath());

        System.out.println("==========childern end==============");
    }
}
