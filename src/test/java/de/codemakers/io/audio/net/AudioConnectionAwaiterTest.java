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

package de.codemakers.io.audio.net;

import de.codemakers.base.Standard;
import de.codemakers.base.logger.LogLevel;
import de.codemakers.base.logger.Logger;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Mixer;
import java.net.ServerSocket;
import java.net.Socket;

public class AudioConnectionAwaiterTest {
    
    public static final int PORT = 5463;
    
    public static void main(String[] args) throws Exception {
        Logger.getDefaultAdvancedLeveledLogger().setMinimumLogLevel(LogLevel.FINE);
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendThread().appendLogLevel().appendText(": ").appendObject().finishWithoutException();
        final Mixer.Info[] mixers = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixers.length; i++) {
            Logger.log(String.format("%d - %s", i, mixers[i]));
        }
        Logger.getDefaultAdvancedLeveledLogger().createLogFormatBuilder().appendThread().appendLogLevel().appendText(": ").appendObject().appendNewLine().appendSource().finishWithoutException();
        final Mixer.Info mixerInfo_input = mixers[6];
        final Mixer.Info mixerInfo_output = mixers[1];
        final ServerSocket serverSocket = new ServerSocket(PORT);
        Standard.addShutdownHook(serverSocket::close);
        Logger.log("Awaiting Connection");
        final Socket socket = serverSocket.accept();
        Logger.log(String.format("Accepted Connection from %s:%d", socket.getInetAddress(), socket.getPort()));
        final AudioClientTest audioClientTest = new AudioClientTest(socket);
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
