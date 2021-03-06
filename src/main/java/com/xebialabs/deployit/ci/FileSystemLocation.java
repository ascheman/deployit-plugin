/**
 * Copyright (c) 2013, XebiaLabs B.V., All rights reserved.
 *
 *
 * The Deployit plugin for Jenkins is licensed under the terms of the GPLv2
 * <http://www.gnu.org/licenses/old-licenses/gpl-2.0.html>, like most XebiaLabs Libraries.
 * There are special exceptions to the terms and conditions of the GPLv2 as it is applied to
 * this software, see the FLOSS License Exception
 * <https://github.com/jenkinsci/deployit-plugin/blob/master/LICENSE>.
 *
 * This program is free software; you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation; version 2
 * of the License.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this
 * program; if not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth
 * Floor, Boston, MA 02110-1301  USA
 */

package com.xebialabs.deployit.ci;

import com.google.common.collect.Lists;
import com.xebialabs.deployit.ci.util.JenkinsDeploymentListener;
import hudson.Extension;
import hudson.FilePath;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.base.Strings.isNullOrEmpty;
import static java.lang.String.format;

public class FileSystemLocation extends ImportLocation {
    public final String location;
    public final String workingDirectory;

    @DataBoundConstructor
    public FileSystemLocation(String location, String workingDirectory) {
        this.location = location;
        this.workingDirectory = workingDirectory;
    }

    @Override
    public String getDarFileLocation(FilePath workspace, JenkinsDeploymentListener deploymentListener) {
        checkNotNull(emptyToNull(location), "location is empty or null");
        FilePath root = (isNullOrEmpty(workingDirectory) ? workspace : new FilePath(new File(workingDirectory)));
        try {
            return ArtifactView.findFileFromPattern(location, root, deploymentListener).getPath();
        } catch (IOException exception) {
            throw new RuntimeException(format("Unable to find DAR from %s in %s", location, root), exception);
        }
    }

    @Extension
    public static final class DescriptorImpl extends ImportLocationDescriptor {
        @Override
        public String getDisplayName() {
            return "FileSystem";
        }
    }

    @Override
    public String toString() {
        return String.format("FileSystemLocation[location: %s, workdir: %s]", location, workingDirectory);
    }
}
