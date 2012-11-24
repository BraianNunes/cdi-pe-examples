package org.jboss.as.quickstart.cdi.extension;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.inject.spi.ProcessBean;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.inject.spi.ProcessObserverMethod;
import javax.enterprise.inject.spi.ProcessProducer;

public class MyExtension implements Extension {
	void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
	    System.out.println("XXX beginning the scanning process");
	}
	<T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
		System.out.println("XXX scanning type: " + pat.getAnnotatedType().getJavaClass().getName());
	}
	
	<T> void processInjectionTarget(@Observes ProcessInjectionTarget<T> pit) {
		System.out.println("XXX processing injection target " + pit.getAnnotatedType().getJavaClass().getName());
		for (InjectionPoint ip : pit.getInjectionTarget().getInjectionPoints()) {
			System.out.println("XXX     " + ip.getMember().toString());
		}
	}
	
	<T, X> void processProducer(@Observes ProcessProducer<T, X> pp) {
		System.out.println("XXX processing producer " + pp.getAnnotatedMember().getJavaMember().getDeclaringClass().getName() + " :: " + pp.getAnnotatedMember().getJavaMember().getName());
	}
	
	<T> void processBean(@Observes ProcessBean<T> pb) {
		System.out.println("XXX processing bean " + pb.getAnnotated().getBaseType().toString());
	}
	
	<T, X> void processObserverMethod(@Observes ProcessObserverMethod<T, X> pom) {
		System.out.println("XXX processing observer method " + pom.getAnnotatedMethod().getJavaMember().getDeclaringClass().getName() + " :: " + pom.getAnnotatedMethod().getJavaMember().getName());
	}
	
	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {
		System.out.println("XXX finished the scanning process");
	}
	
	void afterDeploymentValidation(@Observes AfterDeploymentValidation adv) {
		System.out.println("XXX finished deployment validation");
	}
}
