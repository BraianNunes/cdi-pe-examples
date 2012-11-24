package org.jboss.jdf.princessrescue.xmlbeans;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;

public class StringsProducer {
	
	@Produces
	@StringsEntry
	public String getString(InjectionPoint ip) {
		StringsEntry annotation = ip.getAnnotated().getAnnotation(StringsEntry.class);
		return annotation.value();
	}
}
