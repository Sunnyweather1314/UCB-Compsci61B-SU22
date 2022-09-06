package deque;
import deque.Node;

import static org.junit.Assert.assertEquals;

public class LinkedListDeque<T> implements Deque<T> {

    private Node<T> head, tail;
    private int length;

    @Override
    public void addFirst(T item){
        Node<T> newNode = new Node<>(item);
        if(head == null){
            head = newNode;
            tail = newNode;
        }else{
            head.prev = newNode;
            newNode.next = head;
            head = newNode;
        }
        length += 1;
    }

    @Override
    public void addLast(T item){
        Node<T> newNode = new Node<>(item);
        if(tail == null){
            head = newNode;
            tail = newNode;
        }else{
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        length += 1;
    }


//    @Override
//    public boolean isEmpty(){
//        return size() == 0;
//    }

    @Override
    public int size(){
        return length;
    }

    @Override
    public void printDeque(){
        Node<T> p = head;
        String print = "";
        for (int i = 0; i < length; i++) {
            if(i == length- 1){
                print += p.data.toString();
            }else{
                print += p.data.toString() + " ";
            }
            p = p.next;
        }
    }

    @Override
    public T removeFirst(){
        if(head == null){
            return null;
        }else if(head.next == null){
            T res = head.data;
            head = null;
            tail = null;
            length -= 1;
            return res;
        }else{
            Node<T> head_next = head.next;
            head_next.prev = null;
            head.next = null;
            T res = head.data;
            head = null;
            head = head_next;
            length -= 1;
            return res;
        }
    }

    @Override
    public T removeLast(){
        if(head == null){
            return null;
        }else if(head.next == null){
            T res = head.data;
            head = null;
            tail = null;
            length -= 1;
            return res;
        }else{
            Node<T> tail_prev = tail.prev;
            tail.prev = null;
            tail_prev.next = null;
            T res = tail.data;
            tail = null;
            tail = tail_prev;
            length -= 1;
            return res;
        }
    }

    @Override
    public T get(int index) {
        if(index < 0 || index >= length){
            return null;
        }
        Node<T> p = head;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.data;
    }


    @Override
    public boolean equals(Object o){
        if(o == null || !(o instanceof Deque)){
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


    public LinkedListDeque() {
        head = null;
        tail = null;
        length = 0;
    }

    public T getRecursive(int index){
        if(index < 0 || index >= length){
            return null;
        }
        if(index == 0) {
            return head.data;
        }
        LinkedListDeque<T> newlist = new LinkedListDeque<>();
        newlist.head = head.next;
        newlist.tail = tail;
        newlist.length = length - 1;
        return newlist.getRecursive(index - 1);
    }

}