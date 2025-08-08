package com.coderun.compiler.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.PullImageResultCallback;
import com.github.dockerjava.core.DockerClientBuilder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

@Service
public class DockerService {

    public static final String JDK_IMAGE = "openjdk:17-jdk-slim";

    public String compileInContainer(String javaCode) throws Exception {
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        String containerId = null;

        try {

            dockerClient.pullImageCmd(JDK_IMAGE)
                    .exec(new PullImageResultCallback())
                    .awaitCompletion(30, TimeUnit.SECONDS);


            CreateContainerResponse container = dockerClient.createContainerCmd(JDK_IMAGE)
                    .withCmd("sh", "-c", "cat > Main.java && javac Main.java && java Main")
                    .withAttachStdin(true)
                    .withTty(true)
                    .exec();

            containerId = container.getId();

            dockerClient.startContainerCmd(containerId).exec();

            try (InputStream stdin = new ByteArrayInputStream(javaCode.getBytes(StandardCharsets.UTF_8))) {

                dockerClient.attachContainerCmd(containerId)
                        .withStdIn(stdin)
                        .withStdOut(true)
                        .withStdErr(true)
                        .withFollowStream(true)
                        .exec(new LogsCallback())
                        .awaitCompletion();
            }

            return dockerClient.logContainerCmd(containerId)
                    .withStdOut(true)
                    .withStdErr(true)
                    .withFollowStream(true)
                    .exec(new LogsCallback())
                    .awaitCompletion()
                    .toString();

        } finally {

            if (containerId != null) {
                dockerClient.removeContainerCmd(containerId)
                        .withForce(true)
                        .exec();
            }
        }
    }

    private static class LogsCallback extends com.github.dockerjava.api.async.ResultCallbackTemplate<LogsCallback, com.github.dockerjava.api.model.Frame> {
        private final StringBuilder log = new StringBuilder();

        @Override
        public void onNext(com.github.dockerjava.api.model.Frame frame) {
            log.append(new String(frame.getPayload()));
        }

        @Override
        public String toString() {
            return log.toString();
        }
    }
}
