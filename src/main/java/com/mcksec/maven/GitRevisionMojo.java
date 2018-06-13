package com.mcksec.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@Mojo( name = "version" )
public class GitRevisionMojo extends AbstractMojo {

    private final String LONG_PROPERTY = "git.revision.long";
    private final String SHORT_PROPERTY = "git.revision.short";

    @Override
    public void execute(
    ) throws MojoExecutionException, MojoFailureException {
        File file = new File(".git");

        String revisionLong = "none";
        String revisionShort = "none";

        try {
            Git git = Git.open(file);

            try {
                Iterator<RevCommit> it = git.log().call().iterator();

                if (it.hasNext()) {
                    RevCommit commit = it.next();

                    revisionLong = commit.name();
                    revisionShort = commit.abbreviate(8).name();
                }
            } catch (GitAPIException __e) {
                getLog().error(__e.getMessage());
            }
        } catch (IOException __e) {
            getLog().error(__e.getMessage());
        }

        System.setProperty(LONG_PROPERTY, revisionLong);
        System.setProperty(SHORT_PROPERTY, revisionShort);

        getLog().info(LONG_PROPERTY + ": " + System.getProperty(LONG_PROPERTY));
        getLog().info(SHORT_PROPERTY + ": " + System.getProperty(SHORT_PROPERTY));
    }
}
