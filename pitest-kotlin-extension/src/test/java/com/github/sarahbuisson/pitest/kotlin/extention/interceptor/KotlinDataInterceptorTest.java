package com.github.sarahbuisson.pitest.kotlin.extention.interceptor;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.pitest.classinfo.ClassByteArraySource;
import org.pitest.classinfo.ClassName;
import org.pitest.functional.Option;
import org.pitest.mutationtest.config.ReportOptions;
import org.pitest.mutationtest.engine.Location;
import org.pitest.mutationtest.engine.MethodName;
import org.pitest.mutationtest.engine.MutationDetails;
import org.pitest.mutationtest.engine.MutationIdentifier;
import org.pitest.mutationtest.tooling.SmartSourceLocator;

import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class KotlinDataInterceptorTest {


    @InjectMocks
    KotlinDataInterceptor interceptor;

    @Mock
    SmartSourceLocator locator;
    @Mock
    ReportOptions data;
    @Mock
    ClassByteArraySource source;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(interceptor);
    }

    @Test
    public void should_be_on_excluded_line() throws Exception {


        MutationDetails mutations = buildMutation(1, "file");

        interceptor.excludedLinesByFile.put("filefile", Arrays.asList(1));
        assertTrue(interceptor.isMutationExcluded(mutations));

    }

    @Test
    public void should_extract_line() throws Exception {
        //Given
        MutationDetails mutations = buildMutation(1, "file");
        Reader reader = new StringReader("data class A{\n\n@Annot\npublic fun someMethod(){}}");
        when(locator.locate(anyCollection(), anyString())).thenReturn(Option.some(reader));

        //When
        Collection<Integer> lines = interceptor.extractExcludedLines(mutations);

        //Then
        assertThat(lines, org.hamcrest.Matchers.contains(Integer.valueOf(1)));
    }

    @Test
    public void should_not_extract_line() throws Exception {
        //Given
        MutationDetails mutations = buildMutation(1, "file");
        Reader reader = new StringReader("class A{\n\n@Annot\npublic fun someMethod(){}}");
        when(locator.locate(anyCollection(), anyString())).thenReturn(Option.some(reader));

        //When
        Collection<Integer> lines = interceptor.extractExcludedLines(mutations);

        //Then
        assertThat(lines, org.hamcrest.Matchers.not(Matchers.contains(0)));
    }


    private MutationDetails buildMutation(int line, String fileName) {
        return new MutationDetails(new MutationIdentifier(new Location(ClassName.fromString(fileName), MethodName.fromString("met"), "desc"), line, "mutatorId"), fileName, "desc", line, 4);
    }

}