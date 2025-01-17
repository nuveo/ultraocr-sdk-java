package com.nuveo.ultraocr;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import com.nuveo.ultraocr.exceptions.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

class ClientTest {
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
}
