package org.jboss.jdf.princessrescue;

import javax.annotation.PreDestroy;
import javax.inject.Named;

import org.jboss.jdf.princessrescue.scope.GameScoped;

@GameScoped
@Named
public class Counter {
	private int count;
	
	public int getCount() {
		return count;
	}
	
	public void inc() {
		count++;
	}
	
	@PreDestroy
	public void preDestroy() {
		System.out.println("counter destroyed!");
	}
}
