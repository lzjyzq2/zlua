package cn.settile.lzjyzq2.zlua.api;

public interface LuaVM extends LuaState{

    /**
     * 返回当前PC（仅测试用）
     * @return 返回当前PC
     */
    int getPC();

    /**
     * 修改PC（用于实现跳转指令）
     */
    void addPC(int n);

    /**
     * 取出当前指令；将PC指向下一条指令
     * @return 当前指令
     */
    int fetch();

    /**
     * 将指定常量推入栈顶
     * @param idx 索引
     */
    void getConst(int idx);

    /**
     * 将制定常量或栈值推入栈顶
     * @param rk RK
     */
    void getRK(int rk);

    void loadProto(int idx);

    void loadVararg(int i);

    int registerCount();

    void closeUpvalues(int a);
}
