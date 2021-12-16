package com.haafiz.common.util;

import java.io.Serializable;

/**
 * @author Anthony
 * @create 2021/12/16
 * @desc
 */
public class Three<T1, T2, T3> implements Serializable {

	private static final long serialVersionUID = 5189357786039544296L;
	
	private T1 object1;
    private T2 object2;
    private T3 object3;

    public Three(T1 object1, T2 object2, T3 object3) {
        this.object1 = object1;
        this.object2 = object2;
        this.object3 = object3;
    }

    public T1 getObject1() {
        return object1;
    }

    public void setObject1(T1 object1) {
        this.object1 = object1;
    }

    public T2 getObject2() {
        return object2;
    }

    public void setObject2(T2 object2) {
        this.object2 = object2;
    }

	public T3 getObject3() {
		return object3;
	}

	public void setObject3(T3 object3) {
		this.object3 = object3;
	}
    
}
