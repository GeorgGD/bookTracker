package com.bookTracker.BookTracker.api;

import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@SpringBootTest
public class GoogleBooksImpTest {

	private static GoogleBooksImp googleBooksImp;
	private static OkHttpClient client;
	private static Call call;

	private static void setupClient() throws IOException {
		client = mock(OkHttpClient.class);		
		String url = "https://www.googleapis.com/books/v1/volumes?q=courage+is+calling&key=no-key";
		Request bookSearch = setupSearchBookRequest(url);
		when(client.newCall(bookSearch)).thenReturn(call);
		when(client.cache()).thenReturn(new Cache(null, 10));
	}

	private static void setupCall() throws IOException {
		call = mock(Call.class);
		Response response = setupSearchBookResponse("bookSearchJSON.txt", new Request.Builder().url("http://www.notneededurl.com").get().build());
		when(call.execute()).thenReturn(response);
	}
	
	private static Request setupSearchBookRequest(String url) {
		Request request = new Request.Builder()
			.url(url)
			.get()
			.build();
		return request;
	}
	
	private static Response setupSearchBookResponse(String fileName, Request request) {
		Response response = new Response.Builder()
			.request(request)
			.protocol(Protocol.HTTP_1_0)
			.code(200)
			.message("")
			.body(ResponseBody.create(MediaType.get("application/json; charset=utf-8"), readJsonFile(fileName)))
			.build();
		return response;
	}

	private static String readJsonFile(String fileName) {
		fileName = System.getProperty("user.dir") + "/target/test-classes/" + fileName;
		
		File file = new File(fileName);
		String line;
		String text = "";
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while((line = reader.readLine()) != null) {
				text += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
	
	@BeforeAll
    public static void createGoogleBooksImp() {
		try {
			setupCall();
			setupClient();
		} catch (IOException e) {
			e.printStackTrace();
		}
		googleBooksImp = new GoogleBooksImp(client, new ObjectMapper());
	}

	@Test
	public void closeClient_ClosesClient_ClosingCache() {
		googleBooksImp.closeClient();
		verify(client, times(1)).cache();
	}
}
