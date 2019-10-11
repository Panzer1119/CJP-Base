/*
 *     Copyright 2018 - 2019 Paul Hagedorn (Panzer1119)
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package de.codemakers.base.util.version;

import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;

public class VersionUtilTest {
    
    public static void main(String[] args) {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINE);
        //
        final String version_1_string = "1.0.0.1.5-beta";
        Logger.log("version_1_string=" + version_1_string);
        final Version version_1 = VersionUtil.parseToVersion(version_1_string);
        Logger.log("version_1=" + version_1);
        version_1.getVersionPartsAsList().forEach((versionPart) -> Logger.log("1 versionPart=" + versionPart));
        //
        final String version_2_string = "1.0.0.1.6-beta";
        Logger.log("version_2_string=" + version_2_string);
        final Version version_2 = VersionUtil.parseToVersion(version_2_string);
        Logger.log("version_2=" + version_2);
        version_2.getVersionPartsAsList().forEach((versionPart) -> Logger.log("2 versionPart=" + versionPart));
        //
        Logger.log("");
        Logger.log("");
        Logger.log("");
        //
        Logger.log("");
        Logger.log("version_1=" + version_1);
        Logger.log("");
        Logger.log("version_1=" + version_1.getCompleteString());
        Logger.log("");
        Logger.log("version_2=" + version_2.getCompleteString());
        Logger.log("");
        Logger.log("version_2=" + version_2);
        Logger.log("");
        //
        Logger.log("version_1.isThisHigherThanThat(version_2)     = " + version_1.isThisHigherThanThat(version_2));
        Logger.log("version_1.isThisHigherOrEqualToThat(version_2)= " + version_1.isThisHigherOrEqualToThat(version_2));
        Logger.log("version_1.isThisEqualToThat(version_2)        = " + version_1.isThisEqualToThat(version_2));
        Logger.log("version_1.isThisLowerOrEqualToThat(version_2) = " + version_1.isThisLowerOrEqualToThat(version_2));
        Logger.log("version_1.isThisLowerThanThat(version_2)      = " + version_1.isThisLowerThanThat(version_2));
        //
        Logger.log("");
        Logger.log("");
        Logger.log("");
        //
        Logger.log("version_2.isThisHigherThanThat(version_1)     = " + version_2.isThisHigherThanThat(version_1));
        Logger.log("version_2.isThisHigherOrEqualToThat(version_1)= " + version_2.isThisHigherOrEqualToThat(version_1));
        Logger.log("version_2.isThisEqualToThat(version_1)        = " + version_2.isThisEqualToThat(version_1));
        Logger.log("version_2.isThisLowerOrEqualToThat(version_1) = " + version_2.isThisLowerOrEqualToThat(version_1));
        Logger.log("version_2.isThisLowerThanThat(version_1)      = " + version_2.isThisLowerThanThat(version_1));
        //
        Logger.log("");
        Logger.log("");
        Logger.log("");
        //
        final String version_3_string = "1.0.0.1.10";
        Logger.log("version_3_string=" + version_3_string);
        final Version version_3 = VersionUtil.parseToVersion(version_3_string);
        Logger.log("version_3=" + version_3);
        version_3.getVersionPartsAsList().forEach((versionPart) -> Logger.log("3 versionPart=" + versionPart));
        //
        Logger.log("");
        Logger.log("");
        Logger.log("");
        //
        Logger.log("");
        Logger.log("version_3=" + version_3);
        Logger.log("");
        Logger.log("version_3=" + version_3.getCompleteString());
        Logger.log("");
        Logger.log("version_2=" + version_2.getCompleteString());
        Logger.log("");
        Logger.log("version_2=" + version_2);
        Logger.log("");
        //
        Logger.log("version_3.isThisHigherThanThat(version_2)     = " + version_3.isThisHigherThanThat(version_2));
        Logger.log("version_3.isThisHigherOrEqualToThat(version_2)= " + version_3.isThisHigherOrEqualToThat(version_2));
        Logger.log("version_3.isThisEqualToThat(version_2)        = " + version_3.isThisEqualToThat(version_2));
        Logger.log("version_3.isThisLowerOrEqualToThat(version_2) = " + version_3.isThisLowerOrEqualToThat(version_2));
        Logger.log("version_3.isThisLowerThanThat(version_2)      = " + version_3.isThisLowerThanThat(version_2));
        //
        Logger.log("");
        Logger.log("");
        Logger.log("");
        //
        Logger.log("version_2.isThisHigherThanThat(version_3)     = " + version_2.isThisHigherThanThat(version_3));
        Logger.log("version_2.isThisHigherOrEqualToThat(version_3)= " + version_2.isThisHigherOrEqualToThat(version_3));
        Logger.log("version_2.isThisEqualToThat(version_3)        = " + version_2.isThisEqualToThat(version_3));
        Logger.log("version_2.isThisLowerOrEqualToThat(version_3) = " + version_2.isThisLowerOrEqualToThat(version_3));
        Logger.log("version_2.isThisLowerThanThat(version_3)      = " + version_2.isThisLowerThanThat(version_3));
        //
        Logger.log("");
        Logger.log("");
        Logger.log("");
        //
        final String version_4_string = "1.0.0.1";
        Logger.log("version_4_string=" + version_4_string);
        final Version version_4 = VersionUtil.parseToVersion(version_4_string);
        Logger.log("version_4=" + version_4);
        version_4.getVersionPartsAsList().forEach((versionPart) -> Logger.log("4 versionPart=" + versionPart));
        //
        Logger.log("");
        Logger.log("");
        Logger.log("");
        //
        Logger.log("");
        Logger.log("version_3=" + version_3);
        Logger.log("");
        Logger.log("version_3=" + version_3.getCompleteString());
        Logger.log("");
        Logger.log("version_4=" + version_4.getCompleteString());
        Logger.log("");
        Logger.log("version_4=" + version_4);
        Logger.log("");
        //
        Logger.log("version_3.isThisHigherThanThat(version_4)     = " + version_3.isThisHigherThanThat(version_4));
        Logger.log("version_3.isThisHigherOrEqualToThat(version_4)= " + version_3.isThisHigherOrEqualToThat(version_4));
        Logger.log("version_3.isThisEqualToThat(version_4)        = " + version_3.isThisEqualToThat(version_4));
        Logger.log("version_3.isThisLowerOrEqualToThat(version_4) = " + version_3.isThisLowerOrEqualToThat(version_4));
        Logger.log("version_3.isThisLowerThanThat(version_4)      = " + version_3.isThisLowerThanThat(version_4));
        //
        Logger.log("");
        Logger.log("");
        Logger.log("");
        //
        Logger.log("version_4.isThisHigherThanThat(version_3)     = " + version_4.isThisHigherThanThat(version_3));
        Logger.log("version_4.isThisHigherOrEqualToThat(version_3)= " + version_4.isThisHigherOrEqualToThat(version_3));
        Logger.log("version_4.isThisEqualToThat(version_3)        = " + version_4.isThisEqualToThat(version_3));
        Logger.log("version_4.isThisLowerOrEqualToThat(version_3) = " + version_4.isThisLowerOrEqualToThat(version_3));
        Logger.log("version_4.isThisLowerThanThat(version_3)      = " + version_4.isThisLowerThanThat(version_3));
    }
    
}
