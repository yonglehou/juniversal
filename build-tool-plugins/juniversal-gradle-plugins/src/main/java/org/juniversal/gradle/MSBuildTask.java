package org.juniversal.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskAction;
import org.juniversal.common.MSBuild;
import org.juniversal.gradle.support.GradleProject;

import java.io.File;

/**
 * @author Bret Johnson
 * @since 7/8/2014 7:53 PM
 */
public class MSBuildTask extends DefaultTask {
    /**
     * Path to the directory where MSBuild is installed.  This directory should contain MSBuild.exe.  Defaults to the
     * value of the msbuildDirectory property.
     */
    //@Parameter(property = "msbuildDirectory", defaultValue = "${msbuildDirectory}", required = false)
    public File msbuildDirectory;

    /**
     * Path to project or solution .sln file to build.  Defaults to msbuildProject property.
     */
    //@Parameter(property = "projectFile", defaultValue = "${msbuildProject}", required = false)
    public File projectFile;

    /**
     * Configuration to build (Release, Debug, etc.).  Defaults to Release.
     */
    //@Parameter(property = "configuration", defaultValue = "Release", required = false)
    public String configuration = "Release";

    /**
     * MSBuild output verbosity.  You can specify the following verbosity levels: q[uiet], m[inimal], n[ormal],
     * d[etailed], and diag[nostic].  "normal" is the MSBuild default, but we default to "minimal" instead as that's
     * generally better for batch builds.
     */
    //@Parameter(property = "verbosity", defaultValue = "minimal", required = false)
    public String verbosity = "minimal";

    /**
     * Target(s) to build in the project/solution.  Specify each target separately, or use a semicolon or comma to
     * separate multiple targets (e.g., "Resources;Compile".  Defaults to Rebuild.
     */
    //@Parameter(property = "target", defaultValue = "Rebuild", required = false)
    public String target = "Rebuild";


    @TaskAction
    public void msbuild() {
        MSBuild msBuild = new MSBuild(new GradleProject(getProject()));
        msBuild.build(projectFile, msbuildDirectory, target, configuration, verbosity);
    }

}
