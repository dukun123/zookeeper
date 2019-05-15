/**
 * @author kdu@instartllogic.com
 * @date 2019-05-06 12:13
 */
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * zookeeper 所有的操作基本上有2中模式，一个是同步操作模式，另一个是异步操作模式
 * 同步操作模式如果节点不存在，会throw异常，异步操作都没有返回值，所有的逻辑和返回值都在回调函数里
 * 在同步接口调用过程中，我们需要关注接口抛出异常的可能,处理异常；
 * 但是在异步接口中，接口本身是不会抛出异常的，所有的异常都会在回调函数中通过Result Code（响应码）来体现。详情你可以打开一个callback来详细的看
 *
 * */


public class Main {


     private static CountDownLatch countDownLatch = new CountDownLatch(1);


     public static  void main(String args[])throws Exception{



          //创建一个zookeeper对象
          /**Zookeeper(String connectString,int sessionTimeout,Watcher watcher)
             Zookeeper(String connectString,int sessionTimeout,Watcher watcher,boolean canBeReadOnly)
             Zookeeper(String connectString,int sessionTimeout,Watcher watcher,long sessionId,byte[] sessionPasswd)
             Zookeeper(String connectString,int sessionTimeout,Watcher watcher,long sessionId,byte[] sessionPasswd,boolean canBeReadOnly)
           *参数说明：
           * connectString -- host:port[，host:port][basePath] 指定的服务器列表，多个host:port之间用英文逗号分隔。还可以可选择的指定一个基路径，如果指定了一个基路径，则所有后续操作基于这个及路径进行。
           * sessionTimeOut -- 会话超时时间。以毫秒为单位。客户端和服务器端之间的连接通过心跳包进行维系，如果心跳包超过这个指定时间则认为会话超时失效。
           * watcher -- 指定默认观察者。如果为null表示不需要观察者。
           * canBeReadOnly -- 标识当前会话是否支持只读，默认情况下，在Zookeeper集群中，
           *                  一个机器如果和集群中过半及以上机器失去了网络连接，那么这个机器将不再处理客户端请求，
           *                  但是在某些场景下，当ZooKeeper服务器发生此类故障的时候，我们还是希望ZooKeeper服务器能够提供读服务，
           *                  这就是ZooKeeper的 只读 模式。
           *                  设置当前会话的客户端是否可以在其链接的服务器与集群中多数服务器失去联系，并停止处理客户端的读写请求时，仍然能够获得该服务器只读请求的响应
           * sessionId、SessionPassword -- 分别代表会话ID和会话密钥，用于唯一确定一个会话，
           *                               可以实现客户端会话复用，从而达到恢复会话的效果。具体的使用方法是，第一次连接上ZooKeeper服务器时，
           *                               通过调用ZooKeeper对象实例的以下两个接口，即可获得当前会话的ID和秘钥：
           *
                                           long getSessionId();
           *
           *                               byte[] getSessionPasswd();
           *
           *                              获取到这两个参数值之后，就可以在下次创建ZooKeeper对象实例的时候传入构造方法了
           *
           *
           * **注意，整个创建会话的过程是异步的，构造方法会在初始化连接后即返回，并不代表真正建立好了一个会话，此时会话处于"CONNECTING"状态。
           * **当会话真正创建起来后，服务器会发送事件通知给客户端，只有客户端获取到这个通知后，会话才真正建立。
           *
           * */

          ZooKeeper zookeeper = new ZooKeeper("192.168.56.3:2184",200000,new BaseWatcher());
          Thread.currentThread().sleep(10l);// 因为是异步创建会话，所以等一点时间
          System.out.println("zookeeper connection success");



          /**
           * Watch是ZooKeeper中非常重要的一个机制，
           * 它可以监控ZooKeeper中节点的变化情况，告知客户端。下面，
           * 我们以代码为例来分析Watch在ZooKeeper中是如何实现的。ZooKeeper中一共由三种方法可以实现Watch， watche不会给你任何修改的内容，想要知道什么修改了需要自己手动调用API查询
           * 分别为getData、exists和getChildren, 都是一次性的
           *
           *
           * https://www.cnblogs.com/ggjucheng/p/3370359.html
           * */

          /**
           * getChildren   https://blog.csdn.net/wo541075754/article/details/66472416 同步获取和异步获取
           * （1）可以给指定的节点添加watcher，该watcher的作用范围是：对应节点直接子目录的一次添加的变化，注意是直接，一次和添加
           * （2）可以同步得到对应节点的所有直接子节点，zookeeper.getChildren("/zk-test", true)
           * （3）可以注册回调方法，异步获得直接子节点 zookeeper.getChildren("/zk-test",new childWatcher(),new nodeCallback(),"zhe shi childern de callback");
           * （4）看链接里面有好多
           * 注意：：：当给同一个节点注册多个watcher的时候，每个wachter都是有效的,这里的watcher多个的意思是不同的object,不管同步还是异步
           *         如果同一个object不会增加watcher，watcher的执行的顺序，是后注册的先执行（注册按照代码的顺序）
           * */


        //  zookeeper.create("/zk-test/member_","du".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
          //zookeeper.create("/zk-test/member_","du".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
          //zookeeper.create("/zk-test/member_1","du".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);

         //获取指定 path 下的所有子目录节点，同样 getChildren方法也有一个重载方法可以设置特定的 watcher 监控子节点的状态
         // 在该节点下面添加childern的时候会触发这个default watcher,或者你可以new 一个新的watcher,这个watcher被触发后就会在这个节点上失效

          List<String>  childern= zookeeper.getChildren("/zk-test", true);
//          for (int i = 0; i <childern.size() ; i++) {
//               System.out.println(childern.get(i));
//          }

         //如果你想异步获取，可以点用这个getchildern， 这个方法返回不了children的list， ctx是你传给你回调方法的参数，在你的回调方法对应的var3
          zookeeper.getChildren("/zk-test",new childWatcher(),new nodeCallback(),"zhe shi childern de callback");
         //List<String>  childern3= zookeeper.getChildren("/zk-test", new BaseWatcher());



         /**
          * getData  https://blog.csdn.net/wo541075754/article/details/66967706
          * Zookeeper提供了两个方法来获取节点内容，同步获取和异步获取：
          * （1）public byte[] getData(String path, boolean watch, Stat stat)
          * （2）public void getData(final String path, Watcher watcher,
          *             DataCallback cb, Object ctx)
          * 可以给指定的节点添加数据是否变化的watcher，该watcher的作用范围是：该节点的一次数据值的变化，
          * （3）注册回调方法，异步获取自节点的值
          * 注意：：：当给同一个节点注册多个watcher的时候，每个wachter都是有效的,这里的watcher多个的意思是不同的object,不管同步还是异步
          *         如果同一个object不会增加watcher，watcher的执行的顺序，是后注册的先执行（注册按照代码的顺序）
          * */
         //指定数据节点状态信息。传入旧stat，方法执行过程中会将其替换为新stat对象。
         // 我们吧这个对象传入之后，就会被填充上当前节点的状态信息，就能获取stat了（说白了这个stat就是进去装东西的）
         Stat stat = new Stat();

         //同步获取该节点的值以及更新stat的值，改变该节点的值的时候会触发这个watcher，其他的改变都不会触发这个watcher
         byte[] value = zookeeper.getData("/zk-test",true,stat);
         System.out.println(new String(value));
         //  System.out.println(stat.getCzxid());
         //  System.out.println(stat.getCversion());


         //异步获取节点的值，需要传入回调对象
         zookeeper.getData("/zk-test",new childWatcher(),new dataCallback(),"zhe shi chuan guo qu de canshu");




         /**
          * exists
          * Zookeeper提供了两个方法来判断节点存不存在，同步和异步：
          * （1）public Stat exists(String path, boolean watch) 返回类型的当前节点的stat（基本信息）
          * （2） public void exists(String path, boolean watch, StatCallback cb, Object ctx)
          *  可以给指定的节点添加节点是否存在的watcher，该watcher的作用范围是：当前节点，注意是一次性的，还有如果触发这个watcher，
          *  如果在他的父节点上有getchilder的watcher，这个watcher也会被触发，因为删除当前节点后，父节点的直接子节点的数量发生了变化
          *
          * （3）注册回调方法，异步获取获取节点的stat
          * 注意：：：当给同一个节点注册多个watcher的时候，每个wachter都是有效的,这里的watcher多个的意思是不同的object, 不管同步还是异步
          *         如果同一个object不会增加watcher，watcher的执行的顺序，是后注册的先执行（注册按照代码的顺序）
          * */


         //同步方法判断节点存不存在,注册默认的watcher，当然像getchildern，getData一样，该方法也有一个重载的方法，只不过可以注册新的watcher，可以new childwatcher();
         //该方法的返回值是一个stat对象，包含了当前节点的状态信息

//          Stat stat1 = zookeeper.exists("/zk-test/kp",true);
//          System.out.println(stat1.getCversion());

          //异步判断节点存不存在，watcher同上，有两种重载方法，节点的状态信息会有回调函数所得
          zookeeper.exists("/zk-test/kp",true,new existCallback(),"pan duan jie dian cunbucunzai de de hui diao");


/**
 * 上面介绍了watcher的注册方法，当然每个方法都有同步和异步
 * 接着我们来看看zookeeper的操作方法，当然也有同步和异步，但是也都差不多
 * */



      /**
       * create https://blog.csdn.net/en_joker/article/details/78688140
       *
       *
       * 客户端可以通过ZooKeeper的API来创建一个数据节点，有如下两个接口：
       *
       * 同步的方式  String create(final String path, byte data[], List<ACL> acl, CreateMode createMode) 返回节点的路径
       * 异步的方式  void create(final String path, byte data[], List<ACL> acl, CreateMode createMode, StringCallback cb, Object ctx)
       *
       * path	    需要创建的数据节点的节点路径，例如，/zk-book/foo
       * data[]	    一个字节数组，是节点创建后的初始内容
       * acl	    节点的ACL策略
       * createMode	节点类型，是一个枚举类型，通常有4种可选的节点类型
       *            持久（PERSISTENT）
       *            持久顺序（PERSISTENT_SEQUENTIAL）
       *            临时（EPHEMERAL）
       *            临时顺序（EPHEMERAL_SEQUENTIAL）
       * cb	    注册一个异步回调函数。开发人员需要实现StringCallback接口，主要是对下面这个方法的重写：
       *        void processResult(int rc, String path, Object ctx, String name);
       *        当服务端节点创建完毕后，ZooKeeper客户端就会自动调用这个方法，这样就可以处理相关的业务逻辑了
       * ctx	用于传递一个对象，可以在回调方法执行的时候使用，通常是放一个上下文（Context）信息
       *
       *
       *
       *  需要注意几点，
       *  无论是同步还是异步接口，ZooKeeper都不支持递归创建，即无法在父节点不存在的情况下创建一个子节点。另外
       *  如果一个节点已经存在了，那么创建同名节点的时候，采用同步创建的时候会抛出NodeExistsException异常。
       *
       *  关于权限控制，如果你的应用场景没有太高的权限要求，那么可以不关注这个参数，只需要在acl参数中传入参数Ids.OPEN_ACL_UNSAFE，
       *  这就表明之后对这个节点的任何操作都不受权限控制。
       *
       *  目前，ZooKeeper的节点内容只支持字节数组（byte[]）类型，也就是说，ZooKeeper不负责为节点内容进行序列化，开发人员需要自己使用序列化工具将
       *  节点内容进行序列化和反序列化。对于字符串，可以简单地使用“string”.getBytes()生成一个字节数组；对于其他复杂对象，
       *  可以使用Hessian或是Kryo等专门的序列化工具来进行序列化。
       *
       * */

      //同步创建
      // String path =  zookeeper.create("/zk-test/pph","123".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

       //异步创建
       zookeeper.create("/zk-test/pph/dukun","456".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT,new createCallback(),"wo zai create node");



       /**
        * 同理update节点，这里可以update节点的值，
        *
        * 同步方式  public Stat setData(String path, byte[] data, int version) 返回节点的状态信息
        *
        * 异步方式  public void setData(String path, byte[] data, int version, StatCallback cb, Object ctx)
        *
        * 参数说明，除了version，剩下的都和create一样，下面介绍少一下version（说白了就是一句话，你想跟新节点的值，你得吧节点当前值的版本告诉我，不然你就是不合法，就是做一个检验）
        * 在更新数据时，setData方法存在一个version参数，其用于指定节点的数据版本，表明本次更新操作是针对指定的数据版本进行的，
        * 但是，在getData方法中，并没有提供根据指定数据版本来获取数据的接口，那么，这里为何要指定数据更新版本呢，这里方便理解，
        * 可以等效于CAS（compare and swap），对于值V，每次更新之前都会比较其值是否是预期值A，只有符合预期，才会将V原子化地更新到新值B。
        * Zookeeper的setData接口中的version参数可以对应预期值，表明是针对哪个数据版本进行更新，假如一个客户端试图进行更新操作，
        * 它会携带上次获取到的version值进行更新，而如果这段时间内，Zookeeper服务器上该节点的数据已经被其他客户端更新，那么其数据版本也会相应更新，
        * 而客户端携带的version将无法匹配，无法更新成功，因此可以有效地避免分布式更新的并发问题。
        *
        *  如果携带的数据版本不正确，是无法成功更新节点（同步方式会throw异常）。其中，setData中的version参数设置-1含义为客户端需要基于数据的最新版本进行更新操作。
        *
        * */


        //同步方式修改节点的值，注意version， 你可以先调用getData，得到一个新的stat，然后就能得到当前数据的版本，
        //方法的返回类型是stat 和exists API一样
        Stat stat2 = zookeeper.setData("/zk-test","4456".getBytes(),stat.getVersion());


        //异步方式更新节点的值
       zookeeper.setData("/zk-test","456789".getBytes(),-1,new existCallback(),"zhe shi geng xin shu ju de zhi");

        System.out.println(stat2.getVersion());


        /**
         * delete
         *
         * 只允许删除叶子节点，即一个节点如果有子节点，那么该节点将无法直接删除，必须先删掉其所有子节点。同样也有同步和异步两种方式。　
         * 同步方式 public void delete(String path, int version)
         * 异步方式 public void delete(String path, int version, VoidCallback cb, Object ctx)
         *
         *同样需要一个version参数，详解看update解释里面关于version的解释
         * */

//          zookeeper.delete("/zk-test/lu",-1);
//
//          zookeeper.delete("/zk-test/dkd",-1,new deleteCallback(),"wo zhen zai shan chu shuju");

           TimeUnit.SECONDS.sleep(100000000);



     }


}
