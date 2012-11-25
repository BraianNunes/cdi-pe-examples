package org.jboss.as.quickstart.cdi.pe.app;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Model;
import javax.inject.Named;

@Model
public class Action {
	public String getMessage() {
		return "Hello, world!";
	}
}
