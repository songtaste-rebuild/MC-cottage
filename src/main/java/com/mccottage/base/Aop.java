package com.mccottage.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Aop {
	public static void main(String[] args) {
		
	}
}

class Handler implements InvocationHandler {
	private CPU cpu;
	
	Handler(CPU cpu){
		this.cpu = cpu;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		System.out.println("before...");
		Object rest = method.invoke(cpu, args);
		System.out.println("after...");
		return rest;
	}
	
}

interface CPU {
	void produce();
}

class AMD implements CPU {

	public void produce() {
		System.out.println("AMD is produce cpu..");
	}
	
}

class Intel implements CPU {

	public void produce() {
		System.out.println("Intel is produce cpu..");
	}
	
}