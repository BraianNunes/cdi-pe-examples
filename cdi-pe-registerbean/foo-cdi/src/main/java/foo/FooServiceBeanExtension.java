package foo;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;

public class FooServiceBeanExtension  implements Extension {
	
	/*
	private static class SessionScopedLiteral extends AnnotationLiteral<SessionScoped> implements SessionScoped {} 
	
	
	void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd, BeanManager bm) {
		
		final AnnotatedType<FooService> at = bm.createAnnotatedType(FooService.class);
		
		AnnotatedType<FooService> wrapped = new AnnotatedType<FooService>() {

			@Override
			public Type getBaseType() {
				return at.getBaseType();
			}

			@Override
			public Set<Type> getTypeClosure() {
				return at.getTypeClosure();
			}

			@Override
			public <T extends Annotation> T getAnnotation(
					Class<T> annotationType) {
				
				if (annotationType.equals(SessionScoped.class)) {
					return (T) new SessionScopedLiteral();
				}
				
				return at.getAnnotation(annotationType);
			}

			@Override
			public Set<Annotation> getAnnotations() {
				Set<Annotation> ret = new HashSet<Annotation> ();
				ret.add(new SessionScopedLiteral());
				ret.addAll(at.getAnnotations());
				return ret;
			}

			@Override
			public boolean isAnnotationPresent(
					Class<? extends Annotation> annotationType) {
				if (annotationType.equals(SessionScoped.class)) {
					return true;
				}
				return at.isAnnotationPresent(annotationType);
			}

			@Override
			public Class<FooService> getJavaClass() {
				return at.getJavaClass();
			}

			@Override
			public Set<AnnotatedConstructor<FooService>> getConstructors() {
				return at.getConstructors();
			}

			@Override
			public Set<AnnotatedMethod<? super FooService>> getMethods() {
				return at.getMethods();
			}

			@Override
			public Set<AnnotatedField<? super FooService>> getFields() {
				return at.getFields();
			}
			
		};
		
		bbd.addAnnotatedType(at);	
	}*/
	
	void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
		
		System.out.println("XXX FooServiceBeanExtension after bean discovery");
		
		AnnotatedType<FooService> at = bm.createAnnotatedType(FooService.class); 
		
        final InjectionTarget<FooService> it = bm.createInjectionTarget(at);
        
        abd.addBean( new Bean<FooService>() {

			@Override
			public FooService create(
					CreationalContext<FooService> creationalContext) {
				FooService instance = it.produce(creationalContext);
                it.inject(instance, creationalContext);
                it.postConstruct(instance);
                return instance;
			}

			@Override
			public void destroy(FooService instance,
					CreationalContext<FooService> creationalContext) {
				 it.preDestroy(instance);
	             it.dispose(instance);
	             creationalContext.release();
			}

			@Override
			public Set<Type> getTypes() {
				Set<Type> types = new HashSet<Type>();
                types.add(FooService.class);
                types.add(Object.class);
                return types;
			}

			@Override
			public Set<Annotation> getQualifiers() {
				Set<Annotation> qualifiers = new HashSet<Annotation>();
                qualifiers.add( new AnnotationLiteral<Default>() {} );
                qualifiers.add( new AnnotationLiteral<Any>() {} );
                return qualifiers;
			}

			@Override
			public Class<? extends Annotation> getScope() {
				return SessionScoped.class;
			}

			@Override
			public String getName() {
				return "fooService";
			}

			@Override
			public Set<Class<? extends Annotation>> getStereotypes() {
				return Collections.emptySet();
			}

			@Override
			public Class<?> getBeanClass() {
				return FooService.class;
			}

			@Override
			public boolean isAlternative() {
				return false;
			}

			@Override
			public boolean isNullable() {
				return false;
			}

			@Override
			public Set<InjectionPoint> getInjectionPoints() {
				return it.getInjectionPoints();
			}        	
        });
	}
}
