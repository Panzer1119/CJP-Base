# CJP-Database

[![Quality Gate Status](https://sonarqube.codemakers.de/api/project_badges/measure?project=de.codemakers%3ACJP-Database&metric=alert_status)](https://sonarqube.codemakers.de/dashboard?id=de.codemakers%3ACJP-Database)

[![Bugs](https://sonarqube.codemakers.de/api/project_badges/measure?project=de.codemakers%3ACJP-Database&metric=bugs)](https://sonarqube.codemakers.de/project/issues?id=de.codemakers%3ACJP-Database&resolved=false&sinceLeakPeriod=false&types=BUG)

[![Vulnerabilities](https://sonarqube.codemakers.de/api/project_badges/measure?project=de.codemakers%3ACJP-Database&metric=vulnerabilities)](https://sonarqube.codemakers.de/project/issues?id=de.codemakers%3ACJP-Database&resolved=false&sinceLeakPeriod=false&types=VULNERABILITY)

[![Code Smells](https://sonarqube.codemakers.de/api/project_badges/measure?project=de.codemakers%3ACJP-Database&metric=code_smells)](https://sonarqube.codemakers.de/dashboard?id=de.codemakers%3ACJP-Database&resolved=false&sinceLeakPeriod=false&types=CODE_SMELL)

[![Duplicated Lines (%)](https://sonarqube.codemakers.de/api/project_badges/measure?project=de.codemakers%3ACJP-Database&metric=duplicated_lines_density)](https://sonarqube.codemakers.de/component_measures?id=de.codemakers%3ACJP-Database&metric=duplicated_lines_density&view=list)

[![Lines of Code](https://sonarqube.codemakers.de/api/project_badges/measure?project=de.codemakers%3ACJP-Database&metric=ncloc)](https://sonarqube.codemakers.de/component_measures?id=de.codemakers%3ACJP-Database&metric=ncloc&view=list)

Base Library of the CJP Library (Codemakers Java Plus)

## Developer Guide

Make sure you have set up your local Git Hooks:

```sh
git config core.hooksPath .githooks
```

This will make sure your commit messages follow the [Conventional Commits Specification](https://www.conventionalcommits.org/en/v1.0.0/).

Here's some handy commands:

| Command | Usage |
|---------|-------|
| `gradle test` | Run the tests. |
| `gradle build` | Run the builds. |
| `gradle shadowJar` | Create the Uber Jar with all Dependencies. |

## Semantic Versioning

This project uses `standard-release` to update the version in the `build.gradle` file from the changes in the history and to create the `CHANGELOG.md` file.

Any time you want to cut a new release, run:

```sh
npx dwmkerr/standard-version --sign
```

This will:

- Update the `CHANGELOG.md`
- Update the version number based on the commit history
- Create a git tag with the new version number

Finally, just push the tag to trigger a deployment of the new version:

```sh
git push --follow-tags
```
