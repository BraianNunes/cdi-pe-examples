package org.jboss.as.quickstart.cdi.pe;

import java.beans.Introspector;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedConstructor;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Named;

public class NamedPackageExtension implements Extension {
	<X> void processAnnotatedType(@Observes ProcessAnnotatedType<X> pat) {
		
        final AnnotatedType<X> at = pat.getAnnotatedType();
        AnnotatedType<X> wrapped = new AnnotatedType<X>() {

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
								
				if ( Named.class.equals(annotationType) ) {
                    class NamedLiteral 
                            extends AnnotationLiteral<Named> 
                            implements Named {
                        @Override
                        public String value() {
                            Package pkg = at.getJavaClass().getPackage();
                            
                            String unqualifiedName = "";
                            if (at.isAnnotationPresent(Named.class)) {
                            	unqualifiedName = at.getAnnotation(Named.class).value();
                            }

                            // If Named is not present, or it ha a default value 
                            if (unqualifiedName.isEmpty()) {
                            	unqualifiedName = Introspector.decapitalize(at.getJavaClass().getSimpleName());
                            }
                            
                            final String qualifiedName;
                            if ( pkg.isAnnotationPresent(Named.class) ) {
                                qualifiedName = pkg.getAnnotation(Named.class).value() 
                                      + '.' + unqualifiedName;
                            }
                            else {
                                qualifiedName = unqualifiedName;
                            }
                                                                                    
                            return qualifiedName;
                        }
                    }
                    return (T) new NamedLiteral();
                }
                else {
                    return at.getAnnotation(annotationType);
                }
			}

			@Override
			public Set<Annotation> getAnnotations() {
				
				Set<Annotation> original = at.getAnnotations();
				Set<Annotation> ret = new HashSet<Annotation>();
				
				boolean hasNamed = false;
				
				for (Annotation annotation : original) {
					if (annotation.annotationType().equals(Named.class)) {
						ret.add(getAnnotation(Named.class));
						hasNamed = true;
					}
					else {
						ret.add(annotation);
					}
				}
				
				if (!hasNamed) {
					Package pkg = at.getJavaClass().getPackage();
					if (pkg.isAnnotationPresent(Named.class)) {
						ret.add(getAnnotation(Named.class));
					}
				}
				
				return ret;
			}

			@Override
			public boolean isAnnotationPresent(
					Class<? extends Annotation> annotationType) {
				
				if (Named.class.equals(annotationType)) {
					Package pkg = at.getJavaClass().getPackage();
					return pkg.isAnnotationPresent(Named.class) || at.isAnnotationPresent(annotationType);
				}
				else {
					return at.isAnnotationPresent(annotationType);
				}
			}

			@Override
			public Class<X> getJavaClass() {
				return at.getJavaClass();
			}

			@Override
			public Set<AnnotatedConstructor<X>> getConstructors() {
				return at.getConstructors();
			}

			@Override
			public Set<AnnotatedMethod<? super X>> getMethods() {
				return at.getMethods();
			}

			@Override
			public Set<AnnotatedField<? super X>> getFields() {
				return at.getFields();
			}        
        };
        
        pat.setAnnotatedType(wrapped);
	}
}
