
package foo;

public class FooService implements java.io.Serializable {
	private static final long serialVersionUID = -360898171379959738L;
	
	private int count = 0;

    public String foo() {
    	
    	System.out.println("XXX FooService foo called!");
    	
    	int c = ++count;
    	
    	String suffix = "th";
    	if (c % 10 == 1 && c != 11) {
    		suffix = "st";
    	}
    	else if (c % 10 == 2 && c != 12) {
    		suffix = "nd";
    	}
    	else if (c % 10 == 3 && c != 13) {
    		suffix = "rd";
    	}
    	
        return "Hello, world for the " + c + suffix + " time!";
    }
}

