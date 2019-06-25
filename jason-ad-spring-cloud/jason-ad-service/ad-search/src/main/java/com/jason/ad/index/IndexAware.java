package com.jason.ad.index;


//实现对检索的增删改查, K代表返回值, V代表index 的键
//注意 不是所有的表都需要创建索引, 也不是所有的字段都需要建立索引
//索引是为了加快速度
public interface IndexAware<K, V> {

    V get(K key);

    void add(K key, V value);

    void update(K key, V value);

    void delete(K key, V value);
}
