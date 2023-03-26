package springfox.gradlebuild

import org.gradle.api.Project
import springfox.gradlebuild.version.ReleaseType
import springfox.gradlebuild.version.SemanticVersion
import springfox.gradlebuild.version.VersioningStrategy

import static springfox.gradlebuild.plugins.MultiProjectReleasePlugin.*

class BuildInfoFactory {
  VersioningStrategy versioningStrategy

  BuildInfoFactory(VersioningStrategy versioningStrategy) {
    this.versioningStrategy = versioningStrategy
  }

  BuildInfo create(Project project) {
    ReleaseType releaseType = releaseType(project)
    boolean dryRun = dryRun(project)
    def taskNames = project.gradle.startParameter.taskNames;
    def isReleaseBuild = taskNames.contains("release") || "true".equals(System.getenv("SPRINGFOX_RELASE"))

    SemanticVersion buildVersion = versioningStrategy.buildVersion(releaseType, isReleaseBuild)
    project.logger.lifecycle("[RELEASE] current version: ${versioningStrategy.current(project)}, " +
        "build version: $buildVersion, dryRun: $dryRun, releaseBuild: $isReleaseBuild")
    new BuildInfo(
        versioningStrategy.current(project),
        buildVersion,
        releaseType,
        isReleaseBuild,
        dryRun,
        versioningStrategy)
  }


}
