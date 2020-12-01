package zone;

public class Zone{
    //始址
    private int start;
    //分区大小
    private int size;
    //空闲状态
    private boolean isLeisure;
    //前向指针
    private Zone pre;
    //后向指针
    private Zone next;

    public Zone(int start, int size){
        this.start = start;
        this.size =size;
        this.isLeisure = true;

        this.pre = null;
        this.next = null;
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