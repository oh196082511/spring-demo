package org.example;

/**
 * 对标spring的ObjectFactory接口
 */
public interface ObjectFactory<T> {

    /**
     * Return an instance (possibly shared or independent)
     * of the object managed by this factory.
     * @return the resulting instance
     */
    T getObject();
}
