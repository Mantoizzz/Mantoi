package com.forum.mantoi.sys.services;

import java.util.List;

public interface SearchService {

    List<?> search(String input, Class<?> clazz);
}
