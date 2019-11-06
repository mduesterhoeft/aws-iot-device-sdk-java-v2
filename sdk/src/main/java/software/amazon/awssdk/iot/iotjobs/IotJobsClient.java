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

package software.amazon.awssdk.iot.iotjobs;

import java.util.HashMap;
import software.amazon.awssdk.iot.iotjobs.model.DescribeJobExecutionRequest;
import software.amazon.awssdk.iot.iotjobs.model.DescribeJobExecutionResponse;
import software.amazon.awssdk.iot.iotjobs.model.DescribeJobExecutionSubscriptionRequest;
import software.amazon.awssdk.iot.iotjobs.model.GetPendingJobExecutionsRequest;
import software.amazon.awssdk.iot.iotjobs.model.GetPendingJobExecutionsResponse;
import software.amazon.awssdk.iot.iotjobs.model.GetPendingJobExecutionsSubscriptionRequest;
import software.amazon.awssdk.iot.iotjobs.model.JobExecutionData;
import software.amazon.awssdk.iot.iotjobs.model.JobExecutionState;
import software.amazon.awssdk.iot.iotjobs.model.JobExecutionSummary;
import software.amazon.awssdk.iot.iotjobs.model.JobExecutionsChangedEvent;
import software.amazon.awssdk.iot.iotjobs.model.JobExecutionsChangedSubscriptionRequest;
import software.amazon.awssdk.iot.iotjobs.model.JobStatus;
import software.amazon.awssdk.iot.iotjobs.model.NextJobExecutionChangedEvent;
import software.amazon.awssdk.iot.iotjobs.model.NextJobExecutionChangedSubscriptionRequest;
import software.amazon.awssdk.iot.iotjobs.model.RejectedError;
import software.amazon.awssdk.iot.iotjobs.model.RejectedErrorCode;
import software.amazon.awssdk.iot.iotjobs.model.StartNextJobExecutionResponse;
import software.amazon.awssdk.iot.iotjobs.model.StartNextPendingJobExecutionRequest;
import software.amazon.awssdk.iot.iotjobs.model.StartNextPendingJobExecutionSubscriptionRequest;
import software.amazon.awssdk.iot.iotjobs.model.UpdateJobExecutionRequest;
import software.amazon.awssdk.iot.iotjobs.model.UpdateJobExecutionResponse;
import software.amazon.awssdk.iot.iotjobs.model.UpdateJobExecutionSubscriptionRequest;

import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.crt.mqtt.MqttException;
import software.amazon.awssdk.crt.mqtt.MqttMessage;

import software.amazon.awssdk.iot.Timestamp;
import software.amazon.awssdk.iot.EnumSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class IotJobsClient {
    private MqttClientConnection connection = null;
    private final Gson gson = getGson();

    public IotJobsClient(MqttClientConnection connection) {
        this.connection = connection;
    }

    private Gson getGson() {
        GsonBuilder gson = new GsonBuilder();
        gson.registerTypeAdapter(Timestamp.class, new Timestamp.Serializer());
        gson.registerTypeAdapter(Timestamp.class, new Timestamp.Deserializer());
        addTypeAdapters(gson);
        return gson.create();
    }

    private void addTypeAdapters(GsonBuilder gson) {
        gson.registerTypeAdapter(RejectedErrorCode.class, new EnumSerializer<RejectedErrorCode>());
        gson.registerTypeAdapter(JobStatus.class, new EnumSerializer<JobStatus>());
    }

    public CompletableFuture<Integer> SubscribeToJobExecutionsChangedEvents(
        JobExecutionsChangedSubscriptionRequest request,
        QualityOfService qos,
        Consumer<JobExecutionsChangedEvent> handler,
        Consumer<Exception> exceptionHandler) {
        String topic = "$aws/things/{thingName}/jobs/notify";
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("JobExecutionsChangedSubscriptionRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        Consumer<MqttMessage> messageHandler = (message) -> {
            try {
                String payload = new String(message.getPayload(), "UTF-8");
                JobExecutionsChangedEvent response = gson.fromJson(payload, JobExecutionsChangedEvent.class);
                handler.accept(response);
            } catch (UnsupportedEncodingException ex) {
                if (exceptionHandler != null) {
                    exceptionHandler.accept(ex);
                }
            };
        };
        return connection.subscribe(topic, qos, messageHandler);
    }

    public CompletableFuture<Integer> SubscribeToJobExecutionsChangedEvents(
        JobExecutionsChangedSubscriptionRequest request,
        QualityOfService qos,
        Consumer<JobExecutionsChangedEvent> handler) {
        return SubscribeToJobExecutionsChangedEvents(request, qos, handler, null);
    }

    public CompletableFuture<Integer> SubscribeToStartNextPendingJobExecutionAccepted(
        StartNextPendingJobExecutionSubscriptionRequest request,
        QualityOfService qos,
        Consumer<StartNextJobExecutionResponse> handler,
        Consumer<Exception> exceptionHandler) {
        String topic = "$aws/things/{thingName}/jobs/start-next/accepted";
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("StartNextPendingJobExecutionSubscriptionRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        Consumer<MqttMessage> messageHandler = (message) -> {
            try {
                String payload = new String(message.getPayload(), "UTF-8");
                StartNextJobExecutionResponse response = gson.fromJson(payload, StartNextJobExecutionResponse.class);
                handler.accept(response);
            } catch (UnsupportedEncodingException ex) {
                if (exceptionHandler != null) {
                    exceptionHandler.accept(ex);
                }
            };
        };
        return connection.subscribe(topic, qos, messageHandler);
    }

    public CompletableFuture<Integer> SubscribeToStartNextPendingJobExecutionAccepted(
        StartNextPendingJobExecutionSubscriptionRequest request,
        QualityOfService qos,
        Consumer<StartNextJobExecutionResponse> handler) {
        return SubscribeToStartNextPendingJobExecutionAccepted(request, qos, handler, null);
    }

    public CompletableFuture<Integer> SubscribeToDescribeJobExecutionRejected(
        DescribeJobExecutionSubscriptionRequest request,
        QualityOfService qos,
        Consumer<RejectedError> handler,
        Consumer<Exception> exceptionHandler) {
        String topic = "$aws/things/{thingName}/jobs/{jobId}/get/rejected";
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("DescribeJobExecutionSubscriptionRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        if (request.jobId == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("DescribeJobExecutionSubscriptionRequest must have a non-null jobId"));
            return result;
        }
        topic = topic.replace("{jobId}", request.jobId);
        Consumer<MqttMessage> messageHandler = (message) -> {
            try {
                String payload = new String(message.getPayload(), "UTF-8");
                RejectedError response = gson.fromJson(payload, RejectedError.class);
                handler.accept(response);
            } catch (UnsupportedEncodingException ex) {
                if (exceptionHandler != null) {
                    exceptionHandler.accept(ex);
                }
            };
        };
        return connection.subscribe(topic, qos, messageHandler);
    }

    public CompletableFuture<Integer> SubscribeToDescribeJobExecutionRejected(
        DescribeJobExecutionSubscriptionRequest request,
        QualityOfService qos,
        Consumer<RejectedError> handler) {
        return SubscribeToDescribeJobExecutionRejected(request, qos, handler, null);
    }

    public CompletableFuture<Integer> SubscribeToNextJobExecutionChangedEvents(
        NextJobExecutionChangedSubscriptionRequest request,
        QualityOfService qos,
        Consumer<NextJobExecutionChangedEvent> handler,
        Consumer<Exception> exceptionHandler) {
        String topic = "$aws/things/{thingName}/jobs/notify-next";
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("NextJobExecutionChangedSubscriptionRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        Consumer<MqttMessage> messageHandler = (message) -> {
            try {
                String payload = new String(message.getPayload(), "UTF-8");
                NextJobExecutionChangedEvent response = gson.fromJson(payload, NextJobExecutionChangedEvent.class);
                handler.accept(response);
            } catch (UnsupportedEncodingException ex) {
                if (exceptionHandler != null) {
                    exceptionHandler.accept(ex);
                }
            };
        };
        return connection.subscribe(topic, qos, messageHandler);
    }

    public CompletableFuture<Integer> SubscribeToNextJobExecutionChangedEvents(
        NextJobExecutionChangedSubscriptionRequest request,
        QualityOfService qos,
        Consumer<NextJobExecutionChangedEvent> handler) {
        return SubscribeToNextJobExecutionChangedEvents(request, qos, handler, null);
    }

    public CompletableFuture<Integer> SubscribeToUpdateJobExecutionRejected(
        UpdateJobExecutionSubscriptionRequest request,
        QualityOfService qos,
        Consumer<RejectedError> handler,
        Consumer<Exception> exceptionHandler) {
        String topic = "$aws/things/{thingName}/jobs/{jobId}/update/rejected";
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("UpdateJobExecutionSubscriptionRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        if (request.jobId == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("UpdateJobExecutionSubscriptionRequest must have a non-null jobId"));
            return result;
        }
        topic = topic.replace("{jobId}", request.jobId);
        Consumer<MqttMessage> messageHandler = (message) -> {
            try {
                String payload = new String(message.getPayload(), "UTF-8");
                RejectedError response = gson.fromJson(payload, RejectedError.class);
                handler.accept(response);
            } catch (UnsupportedEncodingException ex) {
                if (exceptionHandler != null) {
                    exceptionHandler.accept(ex);
                }
            };
        };
        return connection.subscribe(topic, qos, messageHandler);
    }

    public CompletableFuture<Integer> SubscribeToUpdateJobExecutionRejected(
        UpdateJobExecutionSubscriptionRequest request,
        QualityOfService qos,
        Consumer<RejectedError> handler) {
        return SubscribeToUpdateJobExecutionRejected(request, qos, handler, null);
    }

    public CompletableFuture<Integer> SubscribeToUpdateJobExecutionAccepted(
        UpdateJobExecutionSubscriptionRequest request,
        QualityOfService qos,
        Consumer<UpdateJobExecutionResponse> handler,
        Consumer<Exception> exceptionHandler) {
        String topic = "$aws/things/{thingName}/jobs/{jobId}/update/accepted";
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("UpdateJobExecutionSubscriptionRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        if (request.jobId == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("UpdateJobExecutionSubscriptionRequest must have a non-null jobId"));
            return result;
        }
        topic = topic.replace("{jobId}", request.jobId);
        Consumer<MqttMessage> messageHandler = (message) -> {
            try {
                String payload = new String(message.getPayload(), "UTF-8");
                UpdateJobExecutionResponse response = gson.fromJson(payload, UpdateJobExecutionResponse.class);
                handler.accept(response);
            } catch (UnsupportedEncodingException ex) {
                if (exceptionHandler != null) {
                    exceptionHandler.accept(ex);
                }
            };
        };
        return connection.subscribe(topic, qos, messageHandler);
    }

    public CompletableFuture<Integer> SubscribeToUpdateJobExecutionAccepted(
        UpdateJobExecutionSubscriptionRequest request,
        QualityOfService qos,
        Consumer<UpdateJobExecutionResponse> handler) {
        return SubscribeToUpdateJobExecutionAccepted(request, qos, handler, null);
    }

    public CompletableFuture<Integer> PublishUpdateJobExecution(
        UpdateJobExecutionRequest request,
        QualityOfService qos) {
        String topic = "$aws/things/{thingName}/jobs/{jobId}/update";
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("UpdateJobExecutionRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        if (request.jobId == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("UpdateJobExecutionRequest must have a non-null jobId"));
            return result;
        }
        topic = topic.replace("{jobId}", request.jobId);
        String payloadJson = gson.toJson(request);
        MqttMessage message = new MqttMessage(topic, payloadJson.getBytes());
        return connection.publish(message, qos, false);
    }

    public CompletableFuture<Integer> SubscribeToDescribeJobExecutionAccepted(
        DescribeJobExecutionSubscriptionRequest request,
        QualityOfService qos,
        Consumer<DescribeJobExecutionResponse> handler,
        Consumer<Exception> exceptionHandler) {
        String topic = "$aws/things/{thingName}/jobs/{jobId}/get/accepted";
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("DescribeJobExecutionSubscriptionRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        if (request.jobId == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("DescribeJobExecutionSubscriptionRequest must have a non-null jobId"));
            return result;
        }
        topic = topic.replace("{jobId}", request.jobId);
        Consumer<MqttMessage> messageHandler = (message) -> {
            try {
                String payload = new String(message.getPayload(), "UTF-8");
                DescribeJobExecutionResponse response = gson.fromJson(payload, DescribeJobExecutionResponse.class);
                handler.accept(response);
            } catch (UnsupportedEncodingException ex) {
                if (exceptionHandler != null) {
                    exceptionHandler.accept(ex);
                }
            };
        };
        return connection.subscribe(topic, qos, messageHandler);
    }

    public CompletableFuture<Integer> SubscribeToDescribeJobExecutionAccepted(
        DescribeJobExecutionSubscriptionRequest request,
        QualityOfService qos,
        Consumer<DescribeJobExecutionResponse> handler) {
        return SubscribeToDescribeJobExecutionAccepted(request, qos, handler, null);
    }

    public CompletableFuture<Integer> PublishGetPendingJobExecutions(
        GetPendingJobExecutionsRequest request,
        QualityOfService qos) {
        String topic = "$aws/things/{thingName}/jobs/get";
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("GetPendingJobExecutionsRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        String payloadJson = gson.toJson(request);
        MqttMessage message = new MqttMessage(topic, payloadJson.getBytes());
        return connection.publish(message, qos, false);
    }

    public CompletableFuture<Integer> SubscribeToGetPendingJobExecutionsAccepted(
        GetPendingJobExecutionsSubscriptionRequest request,
        QualityOfService qos,
        Consumer<GetPendingJobExecutionsResponse> handler,
        Consumer<Exception> exceptionHandler) {
        String topic = "$aws/things/{thingName}/jobs/get/accepted";
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("GetPendingJobExecutionsSubscriptionRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        Consumer<MqttMessage> messageHandler = (message) -> {
            try {
                String payload = new String(message.getPayload(), "UTF-8");
                GetPendingJobExecutionsResponse response = gson.fromJson(payload, GetPendingJobExecutionsResponse.class);
                handler.accept(response);
            } catch (UnsupportedEncodingException ex) {
                if (exceptionHandler != null) {
                    exceptionHandler.accept(ex);
                }
            };
        };
        return connection.subscribe(topic, qos, messageHandler);
    }

    public CompletableFuture<Integer> SubscribeToGetPendingJobExecutionsAccepted(
        GetPendingJobExecutionsSubscriptionRequest request,
        QualityOfService qos,
        Consumer<GetPendingJobExecutionsResponse> handler) {
        return SubscribeToGetPendingJobExecutionsAccepted(request, qos, handler, null);
    }

    public CompletableFuture<Integer> SubscribeToStartNextPendingJobExecutionRejected(
        StartNextPendingJobExecutionSubscriptionRequest request,
        QualityOfService qos,
        Consumer<RejectedError> handler,
        Consumer<Exception> exceptionHandler) {
        String topic = "$aws/things/{thingName}/jobs/start-next/rejected";
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("StartNextPendingJobExecutionSubscriptionRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        Consumer<MqttMessage> messageHandler = (message) -> {
            try {
                String payload = new String(message.getPayload(), "UTF-8");
                RejectedError response = gson.fromJson(payload, RejectedError.class);
                handler.accept(response);
            } catch (UnsupportedEncodingException ex) {
                if (exceptionHandler != null) {
                    exceptionHandler.accept(ex);
                }
            };
        };
        return connection.subscribe(topic, qos, messageHandler);
    }

    public CompletableFuture<Integer> SubscribeToStartNextPendingJobExecutionRejected(
        StartNextPendingJobExecutionSubscriptionRequest request,
        QualityOfService qos,
        Consumer<RejectedError> handler) {
        return SubscribeToStartNextPendingJobExecutionRejected(request, qos, handler, null);
    }

    public CompletableFuture<Integer> SubscribeToGetPendingJobExecutionsRejected(
        GetPendingJobExecutionsSubscriptionRequest request,
        QualityOfService qos,
        Consumer<RejectedError> handler,
        Consumer<Exception> exceptionHandler) {
        String topic = "$aws/things/{thingName}/jobs/get/rejected";
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("GetPendingJobExecutionsSubscriptionRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        Consumer<MqttMessage> messageHandler = (message) -> {
            try {
                String payload = new String(message.getPayload(), "UTF-8");
                RejectedError response = gson.fromJson(payload, RejectedError.class);
                handler.accept(response);
            } catch (UnsupportedEncodingException ex) {
                if (exceptionHandler != null) {
                    exceptionHandler.accept(ex);
                }
            };
        };
        return connection.subscribe(topic, qos, messageHandler);
    }

    public CompletableFuture<Integer> SubscribeToGetPendingJobExecutionsRejected(
        GetPendingJobExecutionsSubscriptionRequest request,
        QualityOfService qos,
        Consumer<RejectedError> handler) {
        return SubscribeToGetPendingJobExecutionsRejected(request, qos, handler, null);
    }

    public CompletableFuture<Integer> PublishStartNextPendingJobExecution(
        StartNextPendingJobExecutionRequest request,
        QualityOfService qos) {
        String topic = "$aws/things/{thingName}/jobs/start-next";
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("StartNextPendingJobExecutionRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        String payloadJson = gson.toJson(request);
        MqttMessage message = new MqttMessage(topic, payloadJson.getBytes());
        return connection.publish(message, qos, false);
    }

    public CompletableFuture<Integer> PublishDescribeJobExecution(
        DescribeJobExecutionRequest request,
        QualityOfService qos) {
        String topic = "$aws/things/{thingName}/jobs/{jobId}/get";
        if (request.jobId == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("DescribeJobExecutionRequest must have a non-null jobId"));
            return result;
        }
        topic = topic.replace("{jobId}", request.jobId);
        if (request.thingName == null) {
            CompletableFuture<Integer> result = new CompletableFuture<Integer>();
            result.completeExceptionally(new MqttException("DescribeJobExecutionRequest must have a non-null thingName"));
            return result;
        }
        topic = topic.replace("{thingName}", request.thingName);
        String payloadJson = gson.toJson(request);
        MqttMessage message = new MqttMessage(topic, payloadJson.getBytes());
        return connection.publish(message, qos, false);
    }

}
