
public class ModNCounter {

    private int myCount;
    public int myN;

    public ModNCounter(int myN) {

        this.myN = myN;
        myCount = 0;
    }

    public void increment() {
        if(myCount<myN){
            myCount++;
        }else{
            myCount = 0;
        }
    }

    public void reset() {
        myCount = 0;
    }

    public int value() {
        return myCount;
    }

}
