package org.sbuisson.pitest.extension.intercept.scm;

import org.pitest.coverage.TestInfo;
import org.pitest.mutationtest.build.TestPrioritiser;
import org.pitest.mutationtest.engine.MutationDetails;

import java.util.List;

/**
 * Created by sbuisson on 09/10/2017.
 */
public class AllTestsPrioritiser implements TestPrioritiser {
    private final List<TestInfo> sorted;

    public AllTestsPrioritiser(List<TestInfo> sorted) {
        this.sorted=sorted;
    }

    public List<TestInfo> assignTests(MutationDetails mutation) {
        return sorted;
    }
}
