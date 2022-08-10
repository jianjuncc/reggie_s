package com.reggie.common;

/** Thread配置
 * @author shu
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static void setThreadLocal(Long id){
       threadLocal.set(id);
    }

    public static long getCurrentId(){
        return threadLocal.get();
    }
}
