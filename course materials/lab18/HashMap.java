import java.util.*;
import java.lang.String;
import java.util.LinkedList;

public class HashMap<K, V> implements Map61BL<K,V> {

    /* TODO: Instance variables here */
    int size;
    LinkedList<Entry<K,V>>[] trackArray;
    double loadFactor;

    /* TODO: Constructors here */
    public HashMap(K key, V value){
        this.trackArray = new LinkedList[16];
        Entry<K,V> arg = new Entry(key, value);
        this.trackArray[this.hash(key)].add(arg);
        this.loadFactor = 0.75;
        size = 1;
    }


    public HashMap(){
        this.trackArray = new LinkedList[16];
        this.loadFactor = 0.75;
        size = 0;
    }

    HashMap(int initialCapacity){
        this.trackArray = new LinkedList[initialCapacity];
        this.loadFactor = 0.75;
        size = 0;
    }

    HashMap(int initialCapacity, double loadFactor){
        this.trackArray = new LinkedList[initialCapacity];
        this.loadFactor = loadFactor;
        size = 0;
    }

    /* TODO: Interface methods here */

    @Override
    public boolean containsKey(K key){
        if(this.trackArray[this.hash(key)] != null){
            LinkedList keys = this.trackArray[this.hash(key)];
            for(Object node: keys){
                if(((Entry)node).key().equals(key)){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    }

    @Override
    public V get(K key){

        LinkedList keys = this.trackArray[this.hash(key)];
        for(Object node: keys){
            if(((Entry)node).key().equals(key)){
                return (V)((Entry)node).value();
            }
        }
        return null;
    }


    @Override
    public int size(){
        return size;
    }

    @Override
    public void put(K key, V value){
        Entry<K,V> arg = new Entry(key, value);
        
        LinkedList keys = this.trackArray[this.hash(key)];
        if(keys != null){
            for(int i = 0; i< keys.size(); i++){
                Entry node = (Entry) keys.get(i);
                if(node.key().equals(key)){
                    if(!node.value().equals(value)){
                        keys.set(i,arg);
                        return;
                    }else{
                        return;
                    }
                }
            }
            keys.add(arg);
            size++;

        }else{
            this.trackArray[this.hash(key)] = new LinkedList<>();
            this.trackArray[this.hash(key)].add(arg);
            size++;
        }

        resize();

    }



    public int hash(K key){
        return Math.floorMod(key.hashCode(), this.trackArray.length);
    }

    @Override
    public void clear(){
        this.trackArray = new LinkedList[16];
        this.size = 0;
    }



    public V remove(K key){
        if(containsKey(key)){
            LinkedList keys = this.trackArray[this.hash(key)];
            V value = null;
            for(int i = 0; i< keys.size(); i++){
                Entry node = (Entry) keys.get(i);
                if(node.key().equals(key)){
                    value = (V)node.value();
                    keys.remove(i);
                }
            }
            size--;
            return value;

        }else{
            return null;
        }


//        removed.key +" "+
    }

    public boolean remove(K key, V value){

        if(containsKey(key)){
            LinkedList keys = this.trackArray[this.hash(key)];

            for(int i = 0; i< keys.size(); i++){
                Entry node = (Entry) keys.get(i);
                if(node.key().equals(key) && node.value().equals(value)){
                    keys.remove(i);
                    return true;
                }
            }
        }else{
            return false;
        }
        return false;

    }



    public void resize(){
        double fact = ((double)this.size()/trackArray.length);
        if( fact > loadFactor){
            LinkedList<Entry<K,V>>[] newArray = new LinkedList[trackArray.length*2];
            System.arraycopy(this.trackArray, 0, newArray, 0, trackArray.length);
            this.trackArray = newArray;
//            this.size = trackArray.length;
        }

    }


    public int capacity(){
        return trackArray.length;
    }

    @Override
    public Iterator<K> iterator(){throw new UnsupportedOperationException();}




    private static class Entry<K,V> {

        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry<K,V>) other).key)
                    && value.equals(((Entry<K,V>) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        public V value(){return value;}
        public K key(){return key;}
    }

    

}