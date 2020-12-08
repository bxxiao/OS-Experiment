package entity;

/**
 * 内存的分区结点
 */
public class Zone {
    //始址
    private int start;
    //分区大小
    private int size;
    //空闲状态
    private boolean isLeisure;
    //占用该结点的进程名
    private String pcbName;
    //前向指针
    private Zone pre;
    //后向指针
    private Zone next;

    public Zone(int start, int size, boolean isLeisure) {
        this.start = start;
        this.size = size;
        this.isLeisure = isLeisure;
    }

    @Override
    public String toString() {
        String startBlank = start>=1000?"":(start>=100?" ":(start>=10?"  ":"   "));
        String sizeBlank = size>=1000?"":(size>=100?" ":(size>=10?"  ":"   "));
        return "[" +
                "始址：" + (start+startBlank) +
                ", 大小：" + (size+sizeBlank) +
                ']';
    }

    /**
     * 减少size
     */
    public void reduceSize(int reduce){
        this.size -= reduce;
    }

    /**
     * 增加size
     */
    public void addSize(int add){
        this.size += add;
    }

    /**
     * 始址前移
     */
    public void forwardStart(int add){
        this.start += add;
    }

    /**
     * 在当前结点之前插入新结点
     */
    public void insertBefore(Zone insert){
        insert.setPre(this.pre);
        //若前结点不为空（不为首结点）
        if(this.getPre()!=null) {
            this.getPre().setNext(insert);
        }

        this.pre = insert;
        insert.setNext(this);
    }


    //getter、setter
    public String getPcbName() {
        return pcbName;
    }

    public void setPcbName(String pcbName) {
        this.pcbName = pcbName;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isLeisure() {
        return isLeisure;
    }

    public void setLeisure(boolean leisure) {
        isLeisure = leisure;
    }

    public Zone getPre() {
        return pre;
    }

    public void setPre(Zone pre) {
        this.pre = pre;
    }

    public Zone getNext() {
        return next;
    }

    public void setNext(Zone next) {
        this.next = next;
    }
}
