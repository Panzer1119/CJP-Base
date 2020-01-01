/*
 *     Copyright 2018 - 2020 Paul Hagedorn (Panzer1119)
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

package de.codemakers.io.audio.net;

import de.codemakers.base.Standard;
import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import java.net.InetAddress;
import java.util.Scanner;

public class AudioConnectionEstablisherTest {
    
    public static final int PORT = 5463;
    
    public static void main(String[] args) throws Exception {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINE);
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendThread().appendLogLevel().appendText(": ").appendObject().finishWithoutException();
        final Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixers.length; i++) {
            Logger.log(String.format("%d - %s", i, mixers[i]));
        }
        final Scanner scanner = new Scanner(System.in);
        Mixer.Info mixerInfo_input = null;
        if (args.length == 3) {
            mixerInfo_input = mixers[Integer.parseInt(args[1])];
        } else if (args.length == 4) {
            mixerInfo_input = mixers[Integer.parseInt(args[2])];
        } else {
            Logger.log("Enter the Number you want to use as the Input Device:");
            mixerInfo_input = mixers[scanner.nextInt()];
        }
        Logger.log("You selected \"" + mixerInfo_input + "\" as the Input Device");
        Logger.log("");
        Mixer.Info mixerInfo_output = null;
        if (args.length == 3) {
            mixerInfo_output = mixers[Integer.parseInt(args[2])];
        } else if (args.length == 4) {
            mixerInfo_output = mixers[Integer.parseInt(args[3])];
        } else {
            Logger.log("Enter the Number you want to use as the Output Device:");
            mixerInfo_output = mixers[scanner.nextInt()];
        }
        Logger.log("You selected \"" + mixerInfo_output + "\" as the Output Device");
        Logger.log("");
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendThread().appendLogLevel().appendText(": ").appendObject().appendNewLine().appendSource().finishWithoutException();
        final InetAddress inetAddress = InetAddress.getByName(args[0]);
        int port = PORT;
        if (args.length == 2 || args.length == 4) {
            port = Integer.parseInt(args[1]);
        }
        Logger.log("Connecting");
        final AudioClientTest audioClientTest = new AudioClientTest(inetAddress, port);
        Standard.addShutdownHook(audioClientTest::close);
        audioClientTest.setMixerInfo_input(mixerInfo_input);
        audioClientTest.setMixerInfo_output(mixerInfo_output);
        Logger.log("audioClientTest=" + audioClientTest);
        audioClientTest.init();
        Logger.log("audioClientTest=" + audioClientTest);
        audioClientTest.start();
        Logger.log("audioClientTest=" + audioClientTest);
        Standard.async(() -> {
            Thread.sleep(20000);
            Logger.log("Shutting down");
            Standard.silentError(() -> {
                audioClientTest.stop();
                Thread.sleep(500);
            });
            System.exit(0);
        });
    }
    
}
