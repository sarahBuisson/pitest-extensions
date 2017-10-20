package com.github.sarahbuisson.pitest.annotation.generated.extension.interceptor;

import com.github.sarahbuisson.pitest.annotation.generated.extension.interceptor.GeneratedByAnnotationLineInterceptor;
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
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GeneratedByAnnotationLineInterceptorTest {

    @InjectMocks
    GeneratedByAnnotationLineInterceptor interceptor;

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
    public void should_be_on_generated_line() throws Exception {
        //Given
        MutationDetails mutations = buildMutation(1, "file");
        interceptor.getGeneratedLinesByFile().put("filefile", Arrays.asList(1));

        //When //Then
        assertTrue(interceptor.isMutationOnGeneratedLine(mutations));
    }

    @Test
    public void should_extract_excluded_line() throws Exception {
        //Given
        int lineNumber = 3;
        MutationDetails mutations = buildMutation(lineNumber, "file");
        Reader reader = new StringReader("class A{\n\n@Annot\npublic void someMethod(){}}");
        when(locator.locate(anyCollection(), anyString())).thenReturn(Option.some(reader));

        //When
        List<Integer> excludedLines = interceptor.extractExcludedLines(mutations);

        //Then
        assertThat(excludedLines, org.hamcrest.Matchers.contains(lineNumber));
    }

    @Test
    public void should_not_extract_line_if_no_file_found() throws Exception {
        //Given
        MutationDetails mutations = buildMutation(1, "file");
        when(locator.locate(anyCollection(), anyString())).thenReturn(Option.none());

        //When
        List<Integer> lines = interceptor.extractExcludedLines(mutations);

        //Then
        assertTrue(lines.isEmpty());
    }

    private MutationDetails buildMutation(int line, String fileName) {
        return new MutationDetails(new MutationIdentifier(new Location(ClassName.fromString(fileName), MethodName.fromString("met"), "desc"), line, "mutatorId"), fileName, "desc", line, 4);
    }

}