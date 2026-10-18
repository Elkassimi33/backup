import java.util.LinkedList;
import java.util.Map;

public class CustomHashMap <K,V>{

    private final static  int INITIAL_CAPACITY=16;
    private final static  float LOAD_FACTOR=0.75f;

    private LinkedList<Entry<K,V>>[] buckets;
    private int size;
    private int capacity;

    private static class Entry<K,V>{
        private K key;
        private V value;
        public Entry(K key, V value){
            this.key = key;
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    public CustomHashMap(){
        this.capacity=INITIAL_CAPACITY;
        this.buckets=new LinkedList[capacity];
        this.size=0;
    }

    private int getBucketIndex(K key){
        if(key==null)
            return 0;
        int hash=key.hashCode();
        return Math.abs(hash%capacity);
    }

    public void put(K key,V value){
        int idx=getBucketIndex(key);
        if(buckets[idx]==null){
            buckets[idx]=new LinkedList<>();
        }
        for(Entry<K,V> entry:buckets[idx]){
            if((key==null && entry.key==null)||(key!=null && key.equals(entry.key))){
                entry.value=value;
                return;
            }
        }
        buckets[idx].add(new Entry<>(key, value));
        size++;

        if(size>capacity*LOAD_FACTOR){
            resize();
        }
    }

    public void putAll(Map<? extends K, ? extends V> map){}

    public V get(K key){
        int idx=getBucketIndex(key);
        if(buckets[idx]==null)
            return null;
        for(Entry<K,V> entry:buckets[idx]){
            if((key==null && entry.key==null)||(key!=null && key.equals(entry.key))){
                return entry.value;
            }
        }
        return null;
    }
    public V remove(K key){
        int idx=getBucketIndex(key);
        if(buckets[idx]==null)
            return null;
        Entry<K,V> toRemove=null;
        for(Entry<K,V> entry:buckets[idx]){
            if((key==null && entry.key==null)||(key!=null && key.equals(entry.key))){
                toRemove=entry;
                break;
            }
        }

        if(toRemove!=null){
            buckets[idx].remove(toRemove);
            size--;
            return toRemove.value;
        }
        return null;
    }

    public boolean containsKey(K key){
        int idx=getBucketIndex(key);
        if(buckets[idx]==null) return false;

        for(Entry<K,V> entry:buckets[idx]){
            if((key==null && entry.key==null)||(key!=null && key.equals(entry.key))){
                return true;
            }
        }
        return false;
    }

    public int size(){
        return size;
    }

    public  boolean isEmpty(){
        return size==0;
    }

    @SuppressWarnings("unchecked")
    private void resize(){
        capacity*=2;
        LinkedList<Entry<K,V>>[] oldBucket=buckets;
        buckets = new LinkedList[capacity];
        size=0;

        for (LinkedList<Entry<K,V>> bucket:oldBucket) {
            if (bucket != null)
                for (Entry<K,V> entry:bucket) {
                    put(entry.key, entry.value);
                }
        }
    }

    public  void display(){
        System.out.println("HashMap contents:");
        for (int i=0;i<capacity;i++){
            if(buckets[i]!=null && !buckets[i].isEmpty()) {
                System.out.println("Bucket "+i+": ");
                for (Entry<K, V> entry : buckets[i]) {
                    System.out.print("["+entry.key+"="+entry.value+"]");
                }
                System.out.println();
            }
        }
        System.out.println("Size: "+size);
    }

    public static void main(String[] args) {
        CustomHashMap<String,Integer> map=new CustomHashMap<>();

        map.put("one",1);
        map.put("two",2);
        map.put("three",3);
        map.put("four",4);

        System.out.println("Get one: "+map.get("one"));
        System.out.println("Get five: "+map.get("five"));

        map.put("two",22);
        System.out.println("Update two: "+map.get("two"));

        map.remove("three");
        System.out.println("After removing three: "+map.get("three"));

        System.out.println("Contains one: "+map.containsKey("one"));
        System.out.println("Contains three: "+map.containsKey("three"));

        map.display();

    }
}
