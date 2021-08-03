# CJP-Base

[![CodeFactor](https://www.codefactor.io/repository/github/panzer1119/cjp-base/badge)](https://www.codefactor.io/repository/github/panzer1119/cjp-base)

[![Release](https://jitpack.io/v/Panzer1119/CJP-Base.svg)](https://jitpack.io/#Panzer1119/CJP-Base)

[![Quality Gate Status](https://sonarqube.codemakers.de/api/project_badges/measure?project=de.codemakers%3ACJP-Base&metric=alert_status)](https://sonarqube.codemakers.de/dashboard?id=de.codemakers%3ACJP-Base)

[![Bugs](https://sonarqube.codemakers.de/api/project_badges/measure?project=de.codemakers%3ACJP-Base&metric=bugs)](https://sonarqube.codemakers.de/dashboard?id=de.codemakers%3ACJP-Base)

[![Vulnerabilities](https://sonarqube.codemakers.de/api/project_badges/measure?project=de.codemakers%3ACJP-Base&metric=vulnerabilities)](https://sonarqube.codemakers.de/dashboard?id=de.codemakers%3ACJP-Base)

[![Code Smells](https://sonarqube.codemakers.de/api/project_badges/measure?project=de.codemakers%3ACJP-Base&metric=code_smells)](https://sonarqube.codemakers.de/dashboard?id=de.codemakers%3ACJP-Base)

[![Duplicated Lines (%)](https://sonarqube.codemakers.de/api/project_badges/measure?project=de.codemakers%3ACJP-Base&metric=duplicated_lines_density)](https://sonarqube.codemakers.de/dashboard?id=de.codemakers%3ACJP-Base)

[![Lines of Code](https://sonarqube.codemakers.de/api/project_badges/measure?project=de.codemakers%3ACJP-Base&metric=ncloc)](https://sonarqube.codemakers.de/dashboard?id=de.codemakers%3ACJP-Base)

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
