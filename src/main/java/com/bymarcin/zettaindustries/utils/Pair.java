package com.bymarcin.zettaindustries.utils;

/**
 * Created by marcin212 on 2014-10-03.
 */
public class Pair<K,V> {
    K key;
    V value;
    public Pair(K k,V v){
        key = k;
        value = v;
    }

    public V getValue(){
        return value;
    }

    public K getKey(){
        return key;
    }
    
    public void setValue(V value){
        this.value = value;
    }

    public void setKey(K key){
        this.key = key;
    }
    
}
