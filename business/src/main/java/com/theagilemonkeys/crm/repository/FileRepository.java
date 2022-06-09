package com.theagilemonkeys.crm.repository;

import com.theagilemonkeys.crm.exception.PersistenceException;
import java.io.InputStream;
import java.util.Map;

public interface FileRepository {

  void upload(String path, String fileName, Map<String, String> metadata, InputStream inputStream)
      throws PersistenceException;
}
