package com.github.sarahbuisson.pitest.scm.extension.prioritiser;

import org.pitest.coverage.TestInfo;
import org.pitest.mutationtest.build.TestPrioritiser;
import org.pitest.mutationtest.engine.MutationDetails;

import java.util.List;

/**
 * Created by sarahbuisson on 09/10/2017.
 */
public class AllTestsPrioritiser implements TestPrioritiser {
    private final List<TestInfo> sorted;

    public AllTestsPrioritiser(List<TestInfo> sorted) {
        this.sorted = sorted;
    }

    @Override
    public List<TestInfo> assignTests(MutationDetails mutation) {
        return sorted;
    }
}
