package com.bookTracker.BookTracker.controllers;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bookTracker.BookTracker.api.GoogleBooks;
import com.bookTracker.BookTracker.dao.BookLibraryImp;
import com.bookTracker.BookTracker.dto.BookSearch;
import com.bookTracker.BookTracker.model.Book;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@WebMvcTest(controllers = BookLibraryController.class)
public class BookLibraryControllerIntegrationTest {

	@Autowired
	private static BookLibraryController bookLibraryController;

	@MockBean
	private static GoogleBooks googleBooks;

	private static Optional<List<BookSearch>> createList() {
		List<BookSearch> list = new ArrayList<>();
		BookSearch bookSearch = new BookSearch();

		bookSearch.setId("isMsEAAAQBAJ");
		bookSearch.setTitle("Courage Is Calling");
		bookSearch.setAuthor("Ryan Holiday");
		bookSearch.setCoverImg("http://books.google.com/books/content?id=isMsEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");

		list.add(bookSearch);
		
		return Optional.ofNullable(list);
	}

	private static Optional<Book> createBook() {
		Book book = new Book(4,
							 "Courage Is Calling",
							 "Ryan Holiday",
							 "Business & Economics",
							 false,
							 null,
							 "http://books.google.com/books/content?id=isMsEAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api");

		return Optional.ofNullable(book);
	}
	
	@BeforeAll
	private static void setupController() {
		googleBooks = mock(GoogleBooks.class);
		when(googleBooks.searchBook(any(String.class))).thenReturn(createList());
		when(googleBooks.bookInfo(any(String.class))).thenReturn(createBook());
	}

}
