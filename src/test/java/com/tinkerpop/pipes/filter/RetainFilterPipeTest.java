package com.tinkerpop.pipes.filter;

import com.tinkerpop.pipes.Pipe;
import com.tinkerpop.pipes.util.PipesFunction;
import com.tinkerpop.pipes.util.PipesPipeline;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class RetainFilterPipeTest extends TestCase {

    public void testPipeBasic() {
        List<String> names = Arrays.asList("marko", "marko", "peter", "josh", "pavel", "marko");
        Set<String> collection = new HashSet<String>(Arrays.asList("marko", "pavel"));
        Pipe<String, String> pipe1 = new RetainFilterPipe<String>(collection);
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            String name = pipe1.next();
            assertTrue(name.equals("marko") || name.equals("pavel"));
        }
        assertEquals(counter, 4);
    }

    public void testPipeNoRetentions() {
        List<String> names = Arrays.asList("marko", "marko", "peter", "josh", "pavel", "marko");
        Pipe<String, String> pipe1 = new RetainFilterPipe<String>(new HashSet<String>(Arrays.asList("bill")));
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            pipe1.next();
        }
        assertEquals(counter, 0);
    }

    public void testPipeNoRetentions2() {
        List<String> names = Arrays.asList("marko", "marko", "peter", "josh", "pavel", "marko");
        Pipe<String, String> pipe1 = new RetainFilterPipe<String>(new HashSet<String>());
        pipe1.setStarts(names);
        int counter = 0;
        while (pipe1.hasNext()) {
            counter++;
            pipe1.next();
        }
        assertEquals(counter, 0);
    }

    public void testAsPipeRetain() {
        List<String> list = new PipesPipeline<String, String>(Arrays.asList("1", "2", "3"))._().as("x").transform(new PipesFunction<String, String>() {
            @Override
            public String compute(String argument) {
                if (argument.equals("1"))
                    return "1";
                else
                    return argument + argument;
            }
        })._()._().retain("x").toList();
        assertEquals(list.size(), 1);
        assertTrue(list.contains("1"));
    }
}
