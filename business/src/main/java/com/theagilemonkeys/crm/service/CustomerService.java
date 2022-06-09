package com.theagilemonkeys.crm.service;

import static org.springframework.util.MimeTypeUtils.IMAGE_GIF_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

import com.theagilemonkeys.crm.entity.Customer;
import com.theagilemonkeys.crm.entity.ProfilePhoto;
import com.theagilemonkeys.crm.exception.BusinessException;
import com.theagilemonkeys.crm.exception.BusinessExceptionEnum;
import com.theagilemonkeys.crm.exception.PersistenceException;
import com.theagilemonkeys.crm.repository.CustomerRepository;
import com.theagilemonkeys.crm.repository.FileRepository;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerService {

  private static final String CUSTOMER_BUCKET_NAME = "agile-monkeys-crm-customer";
  private static final List<String> ALLOWED_FILE_MIME_TYPES = Arrays.asList(IMAGE_PNG_VALUE,
      IMAGE_GIF_VALUE,
      IMAGE_JPEG_VALUE);

  private final CustomerRepository customerRepository;

  private final FileRepository fileRepository;

  public Customer saveCustomer(Customer customer) throws PersistenceException {
    return customerRepository.save(customer);
  }

  public Optional<Customer> findCustomerById(UUID customerId) throws PersistenceException {
    return customerRepository.findById(customerId);
  }

  public List<Customer> findAllCustomers() throws PersistenceException {
    return customerRepository.findAll();
  }

  public void deleteCustomer(UUID customerId) throws PersistenceException {
    customerRepository.delete(customerId);
  }

  public String uploadProfilePhoto(ProfilePhoto profilePhoto)
      throws PersistenceException, BusinessException {

    validateFile(profilePhoto.getFile());

    Map<String, String> metadata = new HashMap<>();
    metadata.put("Content-Type", profilePhoto.getFile().getContentType());
    metadata.put("Content-Length", String.valueOf(profilePhoto.getFile().getSize()));

    String path = String.format("%s/%s", CUSTOMER_BUCKET_NAME, profilePhoto.getCustomerId());
    try {
      fileRepository.upload(path, profilePhoto.getName(), metadata,
          profilePhoto.getFile().getInputStream());
      log.info("Uploaded file {}/{} to S3 bucket", path, profilePhoto.getName());
    } catch (IOException e) {
      log.error("Error processing file InputStream", e);
      throw BusinessExceptionEnum.FILE_IO_ERROR.exception(e.getMessage());
    }

    return buildFileUrl(path, profilePhoto.getName());
  }

  private void validateFile(MultipartFile file) throws BusinessException {
    if (file.isEmpty()) {
      throw BusinessExceptionEnum.EMPTY_FILE_ERROR.exception();
    }
    if (!ALLOWED_FILE_MIME_TYPES.contains(file.getContentType())) {
      throw BusinessExceptionEnum.INVALID_FILE_MIME_TYPE_ERROR.exception(file.getContentType());
    }
  }

  private String buildFileUrl(final String path, final String fileName) {
    return String.format("https://%s.s3.us-east-1.amazonaws.com/%s/%s",
        CUSTOMER_BUCKET_NAME,
        path,
        fileName);
  }

}
