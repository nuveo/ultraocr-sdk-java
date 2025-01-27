package com.nuveo.ultraocr;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.nuveo.ultraocr.enums.Resource;
import com.nuveo.ultraocr.exceptions.InvalidStatusCodeException;
import com.nuveo.ultraocr.exceptions.TimeoutException;

class ClientTest {
    @Test
    void createClient() {
        assertDoesNotThrow(() -> new Client());
    }

    @Test
    void createClientHttp() {
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        assertDoesNotThrow(() -> new Client(httpClient));
    }

    @Test
    void createClientAuto() {
        assertDoesNotThrow(() -> new Client("123", "123", 60));
    }

    @Test
    void createClientHttpAndAuto() throws IOException, InterruptedException {
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        assertDoesNotThrow(() -> new Client(httpClient, "123", "123", 60));
    }

    @Test
    void setProperties() {
        Client client = new Client();
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        assertDoesNotThrow(() -> client.setAuthBaseUrl("123"));
        assertDoesNotThrow(() -> client.setBaseUrl("123"));
        assertDoesNotThrow(() -> client.setHttpClient(httpClient));
        assertDoesNotThrow(() -> client.setTimeout(1));
        assertDoesNotThrow(() -> client.setInterval(1));
        assertDoesNotThrow(() -> client.setAutoRefresh("123", "123", 60));
    }

    @Test
    void shouldAuthenticate() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"token\":\"123\"}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        assertDoesNotThrow(() -> client.authenticate("123", "123", 60));
    }

    @Test
    void shouldFailToAuthenticate() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        assertThrows(InvalidStatusCodeException.class, () -> client.authenticate("123", "123", 60));
    }

    @Test
    void shouldGenerateUrl() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        map.put("1", "2");
        map.put("2", "3");
        assertDoesNotThrow(() -> client.generateSignedUrl("cnh", Resource.JOB, map, map));
    }

    @Test
    void shouldFailToGenerateUrl() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        assertThrows(InvalidStatusCodeException.class, () -> client.generateSignedUrl("cnh", Resource.JOB, map, map));
    }

    @Test
    void shouldGetBatchStatus() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        assertDoesNotThrow(() -> client.getBatchStatus("123"));
    }

    @Test
    void shouldFailToGetBatchStatus() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        assertThrows(InvalidStatusCodeException.class, () -> client.getBatchStatus("123"));
    }

    @Test
    void shouldGetJobResult() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"token\":\"123\"}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient, "123", "123", 60);
        assertDoesNotThrow(() -> client.getJobResult("123", "123"));
    }

    @Test
    void shouldFailToGetJobResult() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        assertThrows(InvalidStatusCodeException.class, () -> client.getJobResult("123", "123"));
    }

    @Test
    void shouldSendJobSingleStep() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"token\":\"123\"}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient, "123", "123", 60);
        HashMap map = new HashMap<>();
        assertDoesNotThrow(() -> client.sendJobSingleStep("rg", "123", map, map));
    }

    @Test
    void shouldFailToSendJobSingleStep() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        assertThrows(InvalidStatusCodeException.class, () -> client.sendJobSingleStep("123", "123", map, map));
    }

    @Test
    void shouldSendJob() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"urls\":{\"document\": \"https://www.example.com\"}}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient, "123", "123", 60);
        HashMap map = new HashMap<>();
        assertDoesNotThrow(() -> client.sendJob("rg", "./pom.xml", map, map));
    }

    @Test
    void shouldFailToSendJob() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        assertThrows(InvalidStatusCodeException.class, () -> client.sendJob("123", "123", map, map));
    }

    @Test
    void shouldSendBatch() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"urls\":{\"document\": \"https://www.example.com\"}}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient, "123", "123", 60);
        HashMap map = new HashMap<>();
        List<Map<String, Object>> metadata = new ArrayList<>();
        assertDoesNotThrow(() -> client.sendBatch("rg", "./pom.xml", metadata, map));
    }

    @Test
    void shouldFailToSendBatch() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        List<Map<String, Object>> metadata = new ArrayList<>();
        assertThrows(InvalidStatusCodeException.class, () -> client.sendBatch("123", "123", metadata, map));
    }

    @Test
    void shouldSendJobSingleStepComplete() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"token\":\"123\"}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient, "123", "123", 60);
        HashMap map = new HashMap<>();
        map.put("extra-document", "true");
        map.put("facematch", "true");
        assertDoesNotThrow(() -> client.sendJobSingleStep("rg", "123", "123", "123", map, map));
    }

    @Test
    void shouldFailToSendJobSingleStepComplete() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        assertThrows(InvalidStatusCodeException.class,
                () -> client.sendJobSingleStep("123", "123", "123", "123", map, map));
    }

    @Test
    void shouldSendJobComplete() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn(
                "{\"urls\":{\"document\": \"https://www.example.com\", \"selfie\": \"https://www.example.com\", \"extra_document\": \"https://www.example.com\"}}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient, "123", "123", 60);
        HashMap map = new HashMap<>();
        map.put("extra-document", "true");
        map.put("facematch", "true");
        assertDoesNotThrow(() -> client.sendJob("rg", "./pom.xml", "./pom.xml", "./pom.xml", map, map));
    }

    @Test
    void shouldFailToSendJobComplete() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        assertThrows(InvalidStatusCodeException.class, () -> client.sendJob("123", "123", map, map));
    }

    @Test
    void shouldSendBatchBase64() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"urls\":{\"document\": \"https://www.example.com\"}}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient, "123", "123", 60);
        HashMap map = new HashMap<>();
        List<Map<String, Object>> metadata = new ArrayList<>();
        assertDoesNotThrow(() -> client.sendBatchBase64("rg", "123", metadata, map));
    }

    @Test
    void shouldFailToSendBatchBase64() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        List<Map<String, Object>> metadata = new ArrayList<>();
        assertThrows(InvalidStatusCodeException.class, () -> client.sendBatchBase64("123", "123", metadata, map));
    }

    @Test
    void shouldSendJobBase64() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"urls\":{\"document\": \"https://www.example.com\"}}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient, "123", "123", 60);
        HashMap map = new HashMap<>();
        assertDoesNotThrow(() -> client.sendJobBase64("rg", "123", map, map));
    }

    @Test
    void shouldFailToSendJobBase64() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        assertThrows(InvalidStatusCodeException.class, () -> client.sendJobBase64("123", "123", map, map));
    }

    @Test
    void shouldSendJobBase64Complete() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn(
                "{\"urls\":{\"document\": \"https://www.example.com\", \"selfie\": \"https://www.example.com\", \"extra_document\": \"https://www.example.com\"}}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient, "123", "123", 60);
        HashMap map = new HashMap<>();
        map.put("extra-document", "true");
        map.put("facematch", "true");
        assertDoesNotThrow(() -> client.sendJobBase64("rg", "123", "123", "123", map, map));
    }

    @Test
    void shouldFailToSendJobBase64Complete() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        assertThrows(InvalidStatusCodeException.class,
                () -> client.sendJobBase64("123", "123", "123", "123", map, map));
    }

    @Test
    void shouldWaitForJobDone() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"status\":\"done\"}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        assertDoesNotThrow(() -> client.waitForJobDone("123", "123"));
    }

    @Test
    void shouldFailToWaitForJobDone() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        assertThrows(InvalidStatusCodeException.class, () -> client.waitForJobDone("123", "123"));
    }

    @Test
    void shouldFailToWaitForJobDoneTimeout() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"status\":\"processing\"}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        client.setTimeout(1);
        assertThrows(TimeoutException.class, () -> client.waitForJobDone("123", "123"));
    }

    @Test
    void shouldWaitForBatchDone() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"status\":\"done\"}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        assertDoesNotThrow(() -> client.waitForBatchDone("123", false));
    }

    @Test
    void shouldWaitForBatchDoneWaitJobs() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"status\":\"done\", \"jobs\":[{\"job_ksuid\":\"1234\"}]}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        assertDoesNotThrow(() -> client.waitForBatchDone("123", true));
    }

    @Test
    void shouldFailToWaitForBatchDone() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        assertThrows(InvalidStatusCodeException.class, () -> client.waitForBatchDone("123", false));
    }

    @Test
    void shouldFailToWaitForBatchDoneTimeout() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"status\":\"processing\"}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        client.setTimeout(1);
        assertThrows(TimeoutException.class, () -> client.waitForBatchDone("123", false));
    }

    @Test
    void shouldCreateAndWaitBatch() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn(
                "{\"urls\":{\"document\": \"https://www.example.com\", \"selfie\": \"https://www.example.com\", \"extra_document\": \"https://www.example.com\"}, \"status\":\"done\"}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        List<Map<String, Object>> metadata = new ArrayList<>();
        assertDoesNotThrow(() -> client.createAndWaitBatch("123", "./pom.xml", metadata, map, false));
    }

    @Test
    void shouldFailToCreateAndWaitBatch() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        List<Map<String, Object>> metadata = new ArrayList<>();
        assertThrows(InvalidStatusCodeException.class,
                () -> client.createAndWaitBatch("123", "./pom.xml", metadata, map, false));
    }

    @Test
    void shouldCreateAndWaitJob() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn(
                "{\"urls\":{\"document\": \"https://www.example.com\", \"selfie\": \"https://www.example.com\", \"extra_document\": \"https://www.example.com\"}, \"status\":\"done\"}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        assertDoesNotThrow(() -> client.createAndWaitJob("123", "./pom.xml", map, map));
    }

    @Test
    void shouldFailToCreateAndWaitJob() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        assertThrows(InvalidStatusCodeException.class, () -> client.createAndWaitJob("123", "./pom.xml", map, map));
    }

    @Test
    void shouldCreateAndWaitJobComplete() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn(
                "{\"urls\":{\"document\": \"https://www.example.com\", \"selfie\": \"https://www.example.com\", \"extra_document\": \"https://www.example.com\"}, \"status\":\"done\"}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        assertDoesNotThrow(() -> client.createAndWaitJob("123", "./pom.xml", map, map));
    }

    @Test
    void shouldFailToCreateAndWaitJobComplete() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        HashMap map = new HashMap<>();
        assertThrows(InvalidStatusCodeException.class, () -> client.createAndWaitJob("123", "./pom.xml", map, map));
    }

    @Test
    void shouldGetJobs() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body()).thenReturn("{\"jobs\":[{\"job_ksuid\":\"1234\"}]}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient, "123", "123", 60);
        assertDoesNotThrow(() -> client.getJobs("123", "123"));
    }

    @Test
    void shouldGetJobsWithToken() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(200);
        Mockito.when(mockResponse.body())
                .thenReturn("{\"jobs\":[{\"job_ksuid\":\"1234\"}], \"nextPageToken\": \"1234\"}")
                .thenReturn("{\"jobs\":[{\"job_ksuid\":\"1234\"}]}");
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient, "123", "123", 60);
        assertDoesNotThrow(() -> client.getJobs("123", "123"));
    }

    @Test
    void shouldFailToGetJobs() throws IOException, InterruptedException {
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        Mockito.when(mockResponse.statusCode()).thenReturn(400);
        HttpClient httpClient = Mockito.mock(HttpClient.class);
        Mockito.when(httpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockResponse);
        Client client = new Client(httpClient);
        assertThrows(InvalidStatusCodeException.class, () -> client.getJobs("123", "123"));
    }
}
