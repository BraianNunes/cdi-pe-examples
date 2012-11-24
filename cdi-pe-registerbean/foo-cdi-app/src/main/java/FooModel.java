import javax.enterprise.inject.Model;
import javax.inject.Inject;

import foo.FooService;


@Model
public class FooModel {
	@Inject
	FooService fooService;
	
	public String getFoo() {
		return fooService.foo();
	}
}
