/**
 * @author kdu@instartllogic.com
 * @date 2019-05-14 13:06
 */
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;

import java.util.List;

public class nodeCallback implements  ChildrenCallback{

  /**
   * @param var1  Result Code，服务端响应码。客户端可以从这个响应码中识别出API调用的结果，常见的响应码如下。
   *      *               0（Ok）：接口调用成功。
   *      *               -4（ConnectionLoss）：客户端和服务端连接已断开。
   *      *               -110（NodeExists）：指定节点已存在。
   *      *               -112（SessionExpired）：会话已过期。
   * @param var2  path
   * @param var3  paramter you passed 接口调用时传入API的ctx参数值。
   * @param var4  all the  直接 childern of this node
   * */
   public void processResult(int var1, String var2, Object var3, List<String> var4){


       //实现你回调的业务逻辑
       System.out.println("var1: " + var1);
       System.out.println("var2: " + var2);
       System.out.println("var3: " + var3);
       System.out.println("var4: " + var4);
   }
}
