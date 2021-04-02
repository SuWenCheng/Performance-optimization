package com.al.po.cache;

public interface BookRepository {

    Book getByIsbn(String isbn);

}