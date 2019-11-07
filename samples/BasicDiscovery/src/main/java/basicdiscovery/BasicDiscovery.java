/* Copyright 2010-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License").
* You may not use this file except in compliance with the License.
* A copy of the License is located at
*
*  http://aws.amazon.com/apache2.0
*
* or in the "license" file accompanying this file. This file is distributed
* on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
* express or implied. See the License for the specific language governing
* permissions and limitations under the License.

* This file is generated
*/

package basicdiscovery;

import software.amazon.awssdk.crt.io.ClientBootstrap;
import software.amazon.awssdk.crt.io.SocketOptions;
import software.amazon.awssdk.crt.io.TlsContext;
import software.amazon.awssdk.crt.io.TlsContextOptions;
import software.amazon.awssdk.crt.mqtt.MqttClient;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.iot.greengrass.DiscoveryClient;
import software.amazon.awssdk.iot.greengrass.model.DiscoverResponse;
import software.amazon.awssdk.iot.greengrass.model.GGGroup;
import software.amazon.awssdk.iot.greengrass.model.GGCore;
import software.amazon.awssdk.iot.greengrass.model.ConnectivityInfo;

import java.util.concurrent.CompletableFuture;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.nio.charset.StandardCharsets;

public class BasicDiscovery {
    static boolean showHelp = false;
    static String region;
    static String rootCaPath;
    static String certPath;
    static String keyPath;
    static String thingName;
    static int numPublishes = 10;
    static String topic = "/sdk/test/JavaV2";

    static void printUsage() {
        System.out.println(
                "Usage:\n" + "  --help         This message\n" + "  --region       AWS IoT service endpoint hostname\n"
                        + "  -r|--rootca    Path to the root certificate\n"
                        + "  -c|--cert      Path to the IoT thing certificate\n"
                        + "  -k|--key       Path to the IoT thing public key\n"
                        + "  -t|--thingName Name of thing to discover for via greengrass\n"
                        + "  -n|--count     Number of messages to publish (optional)\n"
                        + "  --topic        Topic to pub/sub to (optional)");
    }

    static void parseCommandLine(String[] args) {
        for (int idx = 0; idx < args.length; ++idx) {
            switch (args[idx]) {
            case "--help":
                showHelp = true;
                break;
            case "--region":
                if (idx + 1 < args.length) {
                    region = args[++idx];
                }
                break;
            case "-r":
            case "--rootca":
                if (idx + 1 < args.length) {
                    rootCaPath = args[++idx];
                }
                break;
            case "-c":
            case "--cert":
                if (idx + 1 < args.length) {
                    certPath = args[++idx];
                }
                break;
            case "-k":
            case "--key":
                if (idx + 1 < args.length) {
                    keyPath = args[++idx];
                }
                break;
            case "-t":
            case "--thingname":
            case "--thingName":
            case "--thing_name":
                if (idx + 1 < args.length) {
                    thingName = args[++idx];
                }
                break;
            case "-n":
            case "--count":
                if (idx + 1 < args.length) {
                    numPublishes = Integer.parseInt(args[++idx]);
                }
                break;
            case "--topic":
                if (idx + 1 < args.length) {
                    topic = args[++idx];
                }
                break;
            default:
                System.out.println("Unrecognized argument: " + args[idx]);
            }
        }
    }

    static CompletableFuture<MqttClientConnection> connectToEndpoint(MqttClient client, DiscoverResponse response) {
        CompletableFuture<MqttClientConnection> result = new CompletableFuture<>();
        List<CompletableFuture<MqttClientConnection>> connectionAttempts = new ArrayList<>();
        for (GGGroup group : response.ggGroups) {
            for (GGCore core : group.cores) {
                for (ConnectivityInfo endpoint : core.connectivity) {

                }
            }
        }
        return result;
    }

    static CompletableFuture<Void> executeSession(MqttClientConnection connection) {
        CompletableFuture<Void> result = new CompletableFuture<>();
        try {
            Consumer<MqttMessage> handler = (MqttMessage msg) -> {
                String json = new String(msg.getPayload(), StandardCharsets.UTF_8);
                JsonElement root = new JsonParser().parse(json);
                int sequence = root.getAsJsonObject().get("sequence").getAsInt();
                if (sequence == numPublishes) {
                    result.complete(null);
                }
            };
            connection.subscribe(topic, QualityOfService.AT_LEAST_ONCE, handler).get();
            for (int idx = 0; idx < numPublishes; ++idx) {
                JsonObject message = new JsonObject();
                message.add("message", new JsonPrimitive("This is a test!"));
                message.add("sequence", new JsonPrimitive(idx + 1));
                String json = message.toString();
                connection.publish(new MqttMessage(topic, json.getBytes(StandardCharsets.UTF_8)),
                        QualityOfService.AT_LEAST_ONCE, false);
                Thread.sleep(1000);
            }
        } catch (Exception ex) {
            result.completeExceptionally(ex);
        }
        return result;
    }

    public static void main(String[] args) {
        parseCommandLine(args);

        if (showHelp || rootCaPath == null || certPath == null || keyPath == null || thingName == null
                || region == null) {
            printUsage();
            return;
        }

        try (ClientBootstrap bootstrap = new ClientBootstrap(1)) {
            try (SocketOptions socketOptions = new SocketOptions()) {
                try (TlsContextOptions tlsOptions = TlsContextOptions.createWithMTLSFromPath(certPath, keyPath)) {
                    tlsOptions.overrideDefaultTrustStoreFromPath(null, rootCaPath);
                    if (TlsContextOptions.isAlpnSupported()) {
                        tlsOptions.setAlpnList("x-amzn-http-ca");
                    }
                    try (final TlsContext tlsContext = new TlsContext(tlsOptions)) {
                        final DiscoveryClient discovery = new DiscoveryClient(bootstrap, socketOptions, tlsContext,
                                region);
                        DiscoverResponse response = discovery.discover(thingName).get();
                        System.out.println("Discovery Response:");
                        System.out.println(response.toJson());

                        try (MqttClient client = new MqttClient(bootstrap)) {
                            try (MqttClientConnection connection = connectToEndpoint(client, response).get()) {
                                executeSession(connection).get();
                                System.out.println("Complete!");
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            System.err.println("EXCEPTION: " + ex.toString());
        }
    }
}