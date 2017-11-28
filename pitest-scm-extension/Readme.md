#pitest-scm-extension

Extention to the pitest plugin, in order to only mutate the class of code who have been updated between two branches, refs, etc.

The use case is a pull-request: it will only generate the mutation on the class who will have been updated by the pull/merge-request.

Currently only work with **git**.
Also, it will only mutate the class of code who have been updated, and run all the tests.( Not only the modified tests)
##how to use :

```
 <plugin>
    <groupId>org.pitest</groupId>
    <artifactId>pitest-maven</artifactId>
    <version>1.2.4</version>
    <configuration>
        <timestampedReports>false</timestampedReports>
        <failWhenNoMutations>false</failWhenNoMutations>
        <timeoutConstant>4000</timeoutConstant>

        <pluginConfiguration>
            <originReference>origin/sample_branch</originReference>
            <destinationReference>origin/master</destinationReference>
            <scmConnection>scm:git:git@github.com:sarahBuisson/pitest-xebicon-demo.git</scmConnection>
            <scmRootDirectory>${project.basedir}</scmRootDirectory>
        </pluginConfiguration>


    </configuration>
    <dependencies>
        <dependency>
            <groupId>com.github.sarahbuisson</groupId>
            <artifactId>pitest-scm-extension</artifactId>
            <version>LATEST</version>
        </dependency>
    </dependencies>
</plugin>
```

## configuration
**originReference**: reference(branchName, commit, reference) where the modification append. Default: current branch

**destinationReference**: reference where the modification don't append. Default : defaultBranch of the project ( ie origin/master on git)`

**scmConnection** : url of your git repository

