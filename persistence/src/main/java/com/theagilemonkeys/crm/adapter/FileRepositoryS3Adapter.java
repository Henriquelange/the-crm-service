package com.theagilemonkeys.crm.adapter;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.theagilemonkeys.crm.exception.PersistenceException;
import com.theagilemonkeys.crm.exception.PersistenceExceptionEnum;
import com.theagilemonkeys.crm.repository.FileRepository;
import java.io.InputStream;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@Slf4j
public class FileRepositoryS3Adapter implements FileRepository {

  private final AmazonS3 amazonS3;

  @Override
  public void upload(final String path, final String fileName,
                     final Map<String, String> metadata,
                     InputStream inputStream) throws PersistenceException {
    ObjectMetadata objectMetadata = new ObjectMetadata();
    if (!metadata.isEmpty()) {
      metadata.forEach(objectMetadata::addUserMetadata);
    }
    try {
      amazonS3.putObject(path, fileName, inputStream, objectMetadata);
    } catch (AmazonServiceException e) {
      log.error("Error while uploading file to S3:", e);
      throw PersistenceExceptionEnum.S3_INTEGRATION_ERROR.exception(e.getMessage());
    }
  }

}
