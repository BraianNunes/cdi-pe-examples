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
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.util.AnnotationLiteral;

public class FooServiceBeanExtension  implements Extension {
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
