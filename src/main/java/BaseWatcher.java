/**
 * @author kdu@instartllogic.com
 * @date 2019-05-06 13:17
 */

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * 在进入watcher之前我们先试想在应用服务器集群中可能存在的两个问题：
 *
 * 因为集群中有很多机器，当某个通用的配置发生变化后，怎么让自动的让所有服务器的配置统一生效？
 * 当集群中某个节点宕机，如何让集群中的其他节点知道？
 * 为了解决这两个问题，zookeeper引入了watcher机制来实现发布/订阅功能，能够让多个订阅者同时监听某一个主题对象，当这个主题对象自身状态发生变化时，会通知所有订阅者。
 *
 *
 * ZooKeeper 允许客户端向服务端注册一个 Watcher 监听，
 * 当服务器的一些特定事件触发了这个 Watcher，那么就会向指定客户端发送一个事件通知来实现分布式的通知功能。
 *
 *  ZooKeeper ：部署在远程主机上的 ZooKeeper 集群，当然，也可能是单机的。
 *  Client ：分布在各处的 ZooKeeper 的 jar 包程序，被引用在各个独立应用程序中。
 *  WatchManager ：一个接口，用于管理各个监听器，只有一个方法 materialize()，返回一个 Watcher 的 set。
 *
 *
 *  Watcher 机制主要包括客户端线程、客户端 WatchManager 和 ZooKeeper 服务器三部分。三个过程：客户端注册 Watcher、服务器处理 Watcher 和客户端回调 Watcher，
 *  客户端在向 ZooKeeper 服务器注册 Watcher 的同时，会将 Watcher 对象存储在客户端的 WatchManager 中。
 *  当ZooKeeper 服务器触发 Watcher 事件后，会向客户端发送通知，
 *  客户端线程从 WatchManager 的实现类中取出对应的 Watcher 对象来执行回调逻辑。
 *
 *
 *
 * 在创建一个 ZooKeeper 客户端对象实例时，可以向构造方法中传入一个默认的 Watcher：
 *     这个 Watcher 将作为整个 ZooKeeper会话期间的默认 Watcher 会一直被保存在客户端 ZKWatchManager 的 defaultWatcher 中。
 *     ZooKeeper 客户端也可以通过 getData、exists 和 getChildren 三个接口来向 ZooKeeper 服务器注册 Watcher
 *''
 *
 *
 * 在创建一个 ZooKeeper 客户端对象实例时，可以向构造方法中传入一个默认的 Watcher：
 *
 * public ZooKeeper(String connectString, int sessionTimeout, Watcher watcher)
 *
 * 这个 Watcher 将作为整个 ZooKeeper会话期间的默认 Watcher，会一直被保存在客户端 ZKWatchManager 的 defaultWatcher 中。另外，ZooKeeper
 * 客户端也可以通过 getData、exists 和 getChildren 三个接口来向 ZooKeeper 服务器注册 Watcher，无论哪种方式，注册 Watcher 的工作原理都是一致的

 *
 * ZooKeeper 的 Watcher 具有以下几个特性。
 *
 * 一次性
 * 无论是服务端还是客户端，一旦一个 Watcher 被触发，ZooKeeper 都会将其从相应的存储中移除。
 * 因此，在 Watcher 的使用上，需要反复注册。这样的设计有效地减轻了服务端的压力。
 * 客户端串行执行
 * 客户端 Watcher 回调的过程是一个串行同步的过程，这为我们保证了顺序，
 * 同时，需要注意的一点是，一定不能因为一个 Watcher 的处理逻辑影响了整个客户端的 Watcher 回调，
 * 所以，我觉得客户端 Watcher 的实现类要另开一个线程进行处理业务逻辑，以便给其他的 Watcher 调用让出时间。
 * 轻量级
 * WatchedEvent 是最小通知单元
 * 仅仅包括：通知状态、事件类型、节点路径
 * 仅仅通知客户端发生了事件，不会带事件具体内容，具体内容需要客户端再次请求获取
 * 客户端向服务端传递的也不是watcher 对象，使用Boolean类型标记属性，服务端保存当前连接的ServerCnxn
 *
 * */

public class BaseWatcher implements Watcher {


    public void process(WatchedEvent event) {

        System.out.println("==========DefaultWatcher start==============");

        System.out.println("DefaultWatcher state: " + event.getState().name());

        System.out.println("DefaultWatcher type: " + event.getType().name());

        System.out.println("DefaultWatcher path: " + event.getPath());

        System.out.println("==========DefaultWatcher end==============");
    }

}