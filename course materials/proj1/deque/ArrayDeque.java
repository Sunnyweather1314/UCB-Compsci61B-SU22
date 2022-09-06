package deque;
import deque.Deque;
import java.lang.reflect.Array;

public class ArrayDeque<T> implements Deque<T> {
    private T[] data;
    private int length, capacity;
    private int nextFirst, nextLast;

    @Override
    public void addFirst(T item){
        if(length == capacity){
            resize(length * 2);
        }
        if(nextFirst == -1){
            nextFirst = nextLast = capacity / 2;
            data[nextFirst] = item;
            nextFirst = nextFirst > 0 ? (nextFirst - 1) % capacity : (nextFirst - 1 + capacity) % capacity;

            nextLast = (nextLast + 1) % capacity;
        }else{
            data[nextFirst] = item;
            nextFirst = nextFirst > 0 ? (nextFirst - 1) % capacity : (nextFirst - 1 + capacity) % capacity;
//            nextFirst = (nextFirst - 1) % capacity;
        }
        length++;
    }

    @Override
    public void addLast(T item){
        if(length == capacity){
            resize(length * 2);
        }

        if(nextLast == -1){
            nextFirst = nextLast = capacity / 2;
            data[nextLast] = item;
            nextFirst = nextFirst > 0 ? (nextFirst - 1) % capacity : (nextFirst - 1 + capacity) % capacity;

            nextLast = (nextLast + 1) % capacity;

        }else{
            data[nextLast]= item;
            nextLast = (nextLast + 1) % capacity;
        }
        length++;
    }

//    @Override
//    public boolean isEmpty(){
//        return size() == 0;
//    }

    @Override
    public int size(){
//        System.out.println(capacity);
        return length;
    }


    @Override
    public void printDeque(){
        int index = nextFirst;
        String res = "";
        for(int i = 0; i < length; i++){
            index = (index + 1) % capacity;
            if(data[index] == null){
                res += "null ";
                continue;
            }
            res += data[index].toString();
            if(i < length - 1){
                res += " ";
            }
        }
        System.out.println(res);
    }

    @Override
    public T removeFirst(){
        if(length == 0){
            return null;
        }
        nextFirst = (nextFirst + 1) % capacity;
        T res = data[nextFirst];
        data[nextFirst] = null;
        length--;
        if(length == 0){
            nextFirst = nextLast = -1;
        }
        if(capacity >= 16 && (length * 1.0) / (capacity * 1.0) < 0.25){
            resize(capacity / 2);
        }
        return res;
    }

    @Override
    public T removeLast(){
        if(length == 0){
            return null;
        }

        nextLast = nextLast > 0 ? (nextLast - 1) % capacity : (nextLast - 1 + capacity) % capacity;
//        System.out.println((-1 % 9));
        T res = data[nextLast];
        data[nextLast] = null;
        length--;
        if(length == 0){
            nextFirst = nextLast = -1;
        }
        if(capacity >= 16 && (length * 1.0) / (capacity * 1.0) < 0.25){
            resize(capacity / 2);
        }
        return res;
    }

    @Override
    public T get(int index){
        int first = (nextFirst + 1) % capacity;
        if(index < capacity - first){
            return data[first + index];
        }else{
            return data[index - (capacity - first)];
        }
    }


    //constructor
    public ArrayDeque(){
        capacity = 8;
        data = (T[]) new Object[8];
        nextFirst = -1;
        nextLast = -1;
    }

    private void resize(int cap){
        T[] temp = (T[]) new Object[cap];
//        int index = nextFirst;
        int writeIdx = cap / 2;
        for(int i = 0; i < length; i++){
//            index = (index + 1) % length;

            temp[writeIdx] = this.get(i);
            writeIdx = (writeIdx + 1) % cap;
        }

        capacity = cap;
        data = temp;
        nextFirst = capacity / 2 > 0 ? (capacity / 2 - 1) % capacity : (capacity / 2 - 1 + capacity) % capacity;
        nextLast = writeIdx;
//        System.out.println(nextFirst);
//        System.out.println(nextLast);

    }


    @Override
    public boolean equals(Object o){
        if(o == null || !(o instanceof Deque)){
//            System.out.println("Not a Deque! " + o.getClass().toString() + ", " + Deque.class.toString());
            return false;
        }
        if(this == o){
            return true;
        }

        Deque<T> obj = (Deque)o;
        if(length != obj.size()){
            return false;
        }

        for(int i = 0; i < length; i++){
//            if(this.get(i) != obj.get(i)){
//                return false;
//            }
            T val = this.get(i);
            T mval = obj.get(i);
            if(val == null && mval == null){
                continue;
            }
            if(val == null || mval == null){
                return false;
            }

            if(!val.equals(mval)){
                return false;
            }
        }
        return true;
    }

}
