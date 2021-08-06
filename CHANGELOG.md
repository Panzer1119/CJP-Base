# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

### [0.16.8](https://github.com/Panzer1119/CJP-Base/compare/v0.16.7...v0.16.8) (2021-08-06)

### [0.16.7](https://github.com/Panzer1119/CJP-Base/compare/v0.16.6...v0.16.7) (2021-08-06)


### Features

* add Console getter to Standard.java ([52a9ed9](https://github.com/Panzer1119/CJP-Base/commit/52a9ed9aff395900b44d840a149dd58e21a5ba57))
* add line separator getter to SystemProperties.java ([bb72b8b](https://github.com/Panzer1119/CJP-Base/commit/bb72b8be5aa0d148c0d6af0f606999ba3b62604a))
* create Formatter.java and Builder.java and edit AbstractFormatBuilder.java ([436149a](https://github.com/Panzer1119/CJP-Base/commit/436149a8bdea2b3e70214ae7bac670b6c1e94dde))
* create JFrameTitleFormatter.java and JFrameTitleFormatterBuilder.java ([1b8cbf4](https://github.com/Panzer1119/CJP-Base/commit/1b8cbf43b4d759aacce3d51c80ec8d89295848d6))
* create log4j2 plugin ConsoleAppender.java ([e350c7a](https://github.com/Panzer1119/CJP-Base/commit/e350c7a9bda3714d30e885ccabe9ea1735cdba4e))
* create LogEventFormatter.java and SourceFormatter.java ([ea4b69c](https://github.com/Panzer1119/CJP-Base/commit/ea4b69cb19ca28219cef56627015c7ed9aaad9f6))
* improve loglevel conversion in GraphicConsoleAppender.java ([3ff210b](https://github.com/Panzer1119/CJP-Base/commit/3ff210b6e517d425480ba5461f75230622ff0ddd))
* rework LogLevel.java, Console.java and DefaultConsole.java and create LogLevelStyle.java ([5e1f739](https://github.com/Panzer1119/CJP-Base/commit/5e1f73936f4162d137674a073f8779fbc80a3076))


### Bug Fixes

* add console reload to GraphicConsoleAppender.java append ([b734402](https://github.com/Panzer1119/CJP-Base/commit/b7344028b26f9eeeb083509f94a3dc4c49e003b0))
* add missing language keys for new LogLevels ([e5e3801](https://github.com/Panzer1119/CJP-Base/commit/e5e38015bf07297489b477edb5538339b01efe78))
* add missing logger variable to base/ ([919fc91](https://github.com/Panzer1119/CJP-Base/commit/919fc917d15f27136b3ff4d7966c9d02f08579ec))
* add missing logger variable to base/action and remove old logger code from CJP.java ([16cfd26](https://github.com/Panzer1119/CJP-Base/commit/16cfd26d995a0429cf133b385f4fa576cf46fc69))
* add missing logger variable to base/util/tough ([0fb60fc](https://github.com/Panzer1119/CJP-Base/commit/0fb60fc8c0b845e3035ab48fb47fd0d524e76d6e))
* add missing logger variable to io/ ([341bce2](https://github.com/Panzer1119/CJP-Base/commit/341bce27adde0d4b4592a272492a22c395d7da83))
* add missing logger variable to lang/ ([4ddc152](https://github.com/Panzer1119/CJP-Base/commit/4ddc1526137d512a4c0559e3a3bf9ffa71721df8))
* add missing logger variable to security/ ([92e3bca](https://github.com/Panzer1119/CJP-Base/commit/92e3bca377152ecc6082bfc773a0a1c745982330))
* add missing logger variable to security/interfaces ([00646d2](https://github.com/Panzer1119/CJP-Base/commit/00646d2538dedea7d2fe97abcad165a1209306ea))
* add missing logger variable to SpeakText.java ([b8cb5f4](https://github.com/Panzer1119/CJP-Base/commit/b8cb5f4f0c0b35ac8aae764b3853a60983be26e5))
* add missing logger variable to util/interfaces and 2 other interfaces ([a64d602](https://github.com/Panzer1119/CJP-Base/commit/a64d602534a68ad3c08961b59315348dcc9f64f8))
* edit Formattable.java ([5cc4bcd](https://github.com/Panzer1119/CJP-Base/commit/5cc4bcd662de969f7b154256958e1f52573b176c))
* implement append for testing purposes in GraphicConsoleAppender.java ([0fdcbfc](https://github.com/Panzer1119/CJP-Base/commit/0fdcbfc59afa3b748d5786c27a2faf8504cccad6))
* remove LogLevel OFF and ALL from LogLevel.LEVELS ([210c546](https://github.com/Panzer1119/CJP-Base/commit/210c546b06865b8578d3fb62aadfefdaa17dd166))
* remove old Logger call from WindowsSystemInfo.java ([afd82d8](https://github.com/Panzer1119/CJP-Base/commit/afd82d898c7c02f66e57554c59a327903152f556))
* use consoleName instead of console in GraphicConsoleAppender.Builder ([7565bed](https://github.com/Panzer1119/CJP-Base/commit/7565bed22aa54201006f245200fc77b82df1fee9))
* use correct class name in toString method in LogEventFormatterBuilder.java and SourceFormatterBuilder.java ([1c5a6b0](https://github.com/Panzer1119/CJP-Base/commit/1c5a6b02f1385d81e595ce2b1129b2d6683a9215))
* use formatted message in append in GraphicConsoleAppender.java ([24c21b6](https://github.com/Panzer1119/CJP-Base/commit/24c21b656abbcfee72d2ea3f0b159fd4b4661f2a))
* use loggerTODO temporary as variable name logger is already in use in Console.java and DefaultConsole.java ([c13a947](https://github.com/Panzer1119/CJP-Base/commit/c13a94782469270d48c9f49244eac2d6a22a53b5))
* use loglevel input for console input in GraphicConsoleAppenderTest.java ([58523c1](https://github.com/Panzer1119/CJP-Base/commit/58523c11d4af631b326ba5e97821f96db099edd1))
* use PluginBuilderFactory instead of PluginFactory Annotation in GraphicConsoleAppender.java ([35607ed](https://github.com/Panzer1119/CJP-Base/commit/35607edbfd824d791103e90b09f24ac272f1aa58))

### [0.16.6](https://github.com/Panzer1119/CJP-Base/compare/v0.16.5...v0.16.6) (2021-08-04)


### Features

* rework ByteSerializable.java ([1b295a5](https://github.com/Panzer1119/CJP-Base/commit/1b295a53db7126853526806848fa391b3cbe8e4e))
* rework JOSFileList.java and JOSFileSet.java ([7acb002](https://github.com/Panzer1119/CJP-Base/commit/7acb002b937a4b832dc90e7916c59e02cae2407d))
* rework KeepingIncrementalData.java ([ae70d9f](https://github.com/Panzer1119/CJP-Base/commit/ae70d9f4fe38e913d108e3afb67eb8d8ebd29a23))
* rework Result.java and ReturningResult.java ([daff380](https://github.com/Panzer1119/CJP-Base/commit/daff3808090c8982804e5f55b6a62efc3c8b59bc))
* rework SerializationUtil.java ([8969f18](https://github.com/Panzer1119/CJP-Base/commit/8969f18dd645dd5f41373dcd0423148c91230d57))


### Bug Fixes

* fix base/entities/data ([5ea835e](https://github.com/Panzer1119/CJP-Base/commit/5ea835e36a3cfe0218c163abdce341f1564605ea))
* fix ComplexDouble.java ([f99bd02](https://github.com/Panzer1119/CJP-Base/commit/f99bd02194a32b08d133fe246282087e366b21c4))
* fix IncrementalObject.java ([dbdf190](https://github.com/Panzer1119/CJP-Base/commit/dbdf190c359af4f6ebe695f64e1ee9b3f0e41346))
* fix io/streams ([2233312](https://github.com/Panzer1119/CJP-Base/commit/22333124b203b126f9599cb1405330b2bae7fa92))
* fix Result.java and ReturningResult.java ([f1dca2e](https://github.com/Panzer1119/CJP-Base/commit/f1dca2ecaa3287b0f68560da7014e45351fd5e2b))

### [0.16.5](https://github.com/Panzer1119/CJP-Base/compare/v0.16.4...v0.16.5) (2021-08-03)


### Features

* add getResourceAsStream methods to IOUtil.java ([042a8ad](https://github.com/Panzer1119/CJP-Base/commit/042a8addbcf33fe910d325c3c750bc78c49dd7d4))

### [0.16.4](https://github.com/Panzer1119/CJP-Base/compare/v0.16.3...v0.16.4) (2021-08-03)


### Features

* create UUIDUtil.java ([4f5f3ba](https://github.com/Panzer1119/CJP-Base/commit/4f5f3bae2e32af6caf9a95eeec5b5e52f2a804f1))

### [0.16.3](https://github.com/Panzer1119/CJP-Base/compare/v0.16.2...v0.16.3) (2021-08-03)


### Features

* create FileUtil.java ([58f34c3](https://github.com/Panzer1119/CJP-Base/commit/58f34c31fcc62707dd95c9bdc52d0fde4278bcbb))

### [0.16.2](https://github.com/Panzer1119/CJP-Base/compare/v0.16.1...v0.16.2) (2021-08-03)


### Features

* create ToughTriConsumer.java ([2754dc5](https://github.com/Panzer1119/CJP-Base/commit/2754dc581401ce882657d56c721724e415eb048a))

### [0.16.1](https://github.com/Panzer1119/CJP-Base/compare/v0.16.0...v0.16.1) (2021-08-03)

## 0.16.0 (2021-08-03)


### Bug Fixes

* replace ClassFormatException with ClassCastException in Require.java ([45d1101](https://github.com/Panzer1119/CJP-Base/commit/45d1101becef998ba5e64231dd3ccc9c97332845))
