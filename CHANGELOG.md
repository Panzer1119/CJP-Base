# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

### [0.1.4](https://github.com/Panzer1119/CJP-Database/compare/v0.1.3...v0.1.4) (2021-08-04)


### Bug Fixes

* edit addOrUpgradeById methods in DatabaseConnector.java and HibernateUtil.java ([934e657](https://github.com/Panzer1119/CJP-Database/commit/934e657ab3c17e6ec1e477147bd52074245c0619))

### [0.1.3](https://github.com/Panzer1119/CJP-Database/compare/v0.1.2...v0.1.3) (2021-08-04)

### [0.1.2](https://github.com/Panzer1119/CJP-Database/compare/v0.1.1...v0.1.2) (2021-08-03)


### Bug Fixes

* create private constructor in HibernateUtil.java to hide the implicit public one ([c18188b](https://github.com/Panzer1119/CJP-Database/commit/c18188b30f970393c436f46a2ef80fddc2b927fe))
* remove unused class parameter from addOrUpgradeById methods ([0297eef](https://github.com/Panzer1119/CJP-Database/commit/0297eef56a215ee01515f2373545a90fcafa73cf))

### [0.1.1](https://github.com/Panzer1119/CJP-Database/compare/v0.1.0...v0.1.1) (2021-08-03)


### Features

* add checkParameter method to ObjectStorageConnector.java ([0b183d6](https://github.com/Panzer1119/CJP-Database/commit/0b183d6116f1e8c49680e47d075dcbf4600f5dea))
* create DatabaseConnector.java ([94f5302](https://github.com/Panzer1119/CJP-Database/commit/94f5302dbc065d41de53926ec1e4f746a8d044e1))
* create HibernateProperties.java, add templates hibernate-mysql.properties and hibernate-postgresql.properties ([2343119](https://github.com/Panzer1119/CJP-Database/commit/23431198ad367a6f8d37c9eb42f335ec3c20084c))
* create HibernateUtil.java ([a5229cb](https://github.com/Panzer1119/CJP-Database/commit/a5229cb99fec344ab00b8d22fee69a872004a567))
* create IEntity.java ([e6febb6](https://github.com/Panzer1119/CJP-Database/commit/e6febb68dfebe06d93e747492a2d91d125656198))
* create JsonBPostgreSQLDialect.java ([19660d8](https://github.com/Panzer1119/CJP-Database/commit/19660d8119787ab4fd2b472ebd930f407e0e9f4c))
* create JsonType.java ([a3dced8](https://github.com/Panzer1119/CJP-Database/commit/a3dced87c8b6cbcd4e59e6c384a8b8cab9ba414d))
* create MinIOConnector.java ([b1a0133](https://github.com/Panzer1119/CJP-Database/commit/b1a0133e879cf69e27decee1a89da8e9df2b1be6))
* create ObjectStorageConnector.java ([fc7c5be](https://github.com/Panzer1119/CJP-Database/commit/fc7c5be5d1d32efcf0533516de7a8dcd2ed70600))
* implement add and set methods in DatabaseConnector.java ([d63ab36](https://github.com/Panzer1119/CJP-Database/commit/d63ab36085a1b5c560eaa8baedeebc670f5c3f93))
* implement add and set methods in HibernateUtil.java ([adf490c](https://github.com/Panzer1119/CJP-Database/commit/adf490cbabfe71a2a6f9e6ee1f434f608bcff904))
* implement addOrSet and delete methods in DatabaseConnector.java ([b345bf5](https://github.com/Panzer1119/CJP-Database/commit/b345bf57d866d4455e19b398ffe8555c13a3e8fe))
* implement addOrSet and delete methods in HibernateUtil.java ([0c13f9c](https://github.com/Panzer1119/CJP-Database/commit/0c13f9ce842efb3787475cc060585cfff9268f7a))
* implement addOrUpgradeById methods in DatabaseConnector.java ([af2e4a2](https://github.com/Panzer1119/CJP-Database/commit/af2e4a225f6a931cc4917e3100177b33721f9d95))
* implement addOrUpgradeById methods in HibernateUtil.java ([d9367ad](https://github.com/Panzer1119/CJP-Database/commit/d9367ad7b6455afd405e8e695b27a465f71d9671))
* implement get/getAll/getWhere/getAllWhere methods in DatabaseConnector.java ([93a4788](https://github.com/Panzer1119/CJP-Database/commit/93a4788c8f252945907bb4b847e5f5738d342e7a))
* implement get/getAll/getWhere/getAllWhere methods in HibernateUtil.java ([1222bc8](https://github.com/Panzer1119/CJP-Database/commit/1222bc8848aa2a1a7a333d453bc85517afeffe54))
* implement useSession and processSession in DatabaseConnector.java ([126be9f](https://github.com/Panzer1119/CJP-Database/commit/126be9f2ecbaf0689fdbefd591a0ab61fa622f66))
* implement useSession and processSession in HibernateUtil.java ([8aefd92](https://github.com/Panzer1119/CJP-Database/commit/8aefd92e9486f458e6b9354f0b0711768140b4c7))


### Bug Fixes

* add shutdown hook back to DatabaseConnector.java ([4b2aa61](https://github.com/Panzer1119/CJP-Database/commit/4b2aa61c0afe1ea2e283642de7f00b56966ded9b))
* close inputStream after writeObject in MinIOConnector.java ([9f563ed](https://github.com/Panzer1119/CJP-Database/commit/9f563eda80e78b7a26beda0f44074c5ac80564b7))
* enable initData again in DatabaseConnector.java ([9338fb0](https://github.com/Panzer1119/CJP-Database/commit/9338fb026d0c9cf9ad4133a4da528f8d2c53f7ab))
* implement both writeObject methods in MinIOConnector.java ([6b34c60](https://github.com/Panzer1119/CJP-Database/commit/6b34c609a4902c3872299076214a223d7fd3c93b))
* implement copyObject and moveObject in MinIOConnector.java ([68a39b1](https://github.com/Panzer1119/CJP-Database/commit/68a39b1bdfe4c1044f989bbede2c1586192d8ed9))
* implement removeObject in MinIOConnector.java ([1ed9fbe](https://github.com/Panzer1119/CJP-Database/commit/1ed9fbec65881d3958417abec2ca00d3b45cbef0))
* improve readObject methods in MinIOConnector.java ([3bbe5ad](https://github.com/Panzer1119/CJP-Database/commit/3bbe5ad9f741936a27cc02f8e80f9665896cb540))
* make type generic of readObject function in ObjectStorageConnector.java ([63c1f79](https://github.com/Panzer1119/CJP-Database/commit/63c1f799926bf0908edf309174e398e99f386f6e))
* make type generic of readObject function in ObjectStorageConnector.java ([36cca1c](https://github.com/Panzer1119/CJP-Database/commit/36cca1cac6e507de6fcb2b4d351079e518d1a610))
* remove another HibernateException from JsonType.java ([0ac3f63](https://github.com/Panzer1119/CJP-Database/commit/0ac3f63944b613f7732fd708b540045a944420b0))
* remove code smells from JsonType.java ([eb672be](https://github.com/Panzer1119/CJP-Database/commit/eb672be6f71b74d34ae3e5ae0511f752b730f970))
* revert useSession returning boolean in HibernateUtil.java ([375b3f0](https://github.com/Panzer1119/CJP-Database/commit/375b3f04a85e8c9c921aeef177fd367284c03318))

## 0.1.0 (2021-08-03)
