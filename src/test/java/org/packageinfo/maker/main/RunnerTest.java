package org.packageinfo.maker.main;

import org.junit.Test;


public class RunnerTest {

    @Test
    public void test() {
        final String[] args = new String[] {
            "--D",
            ""
        };
        Runner.main(args);
    }
}
