package org.jboss.jdf.princessrescue;

import java.io.Serializable;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

@IsAlive
@Interceptor
public class IsAliveInterceptor implements Serializable {
	
	@Inject
	GameControl gameControl;

	@AroundInvoke
	public Object manage(InvocationContext ic) throws Exception {
		
		if (!gameControl.getPlayer().isShot()) {
			// I'am doing science and I'am still alive!	
			return ic.proceed();
		}
		
		return null;
	}

}
