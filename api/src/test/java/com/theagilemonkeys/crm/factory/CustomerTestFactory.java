package com.theagilemonkeys.crm.factory;

import com.theagilemonkeys.crm.dto.CreateCustomerDTO;
import com.theagilemonkeys.crm.dto.UpdateCustomerDTO;
import com.theagilemonkeys.crm.entity.Customer;
import java.util.Random;
import java.util.UUID;

public abstract class CustomerTestFactory {
  private static String[] firstNames = new String[] {
      "Lord", "Tai", "David", "Henrique", "Flavia",
      "Gabriel", "Max", "Chris", "Marle", "Lucca"
  };
  private static String[] lastNames = new String[] {
      "Daruz", "Lange", "Santos", "Abravanel", "Litt",
      "Specter", "Oliveira", "Nascimento", "Sato", "Wheeler"
  };
  private static String[] photoUrls = new String[] {
      "https://avatars.githubusercontent.com/u/26316855?v=4",
      "https://upload.wikimedia.org/wikipedia/commons/8/89/Portrait_Placeholder.png",
      "https://static.wikia.nocookie.net/deathbattle/images/3/35/Portrait.crash.png",
  };

  public static Customer generateCustomer() {
    Random random = new Random();
    String firstName = firstNames[random.nextInt(firstNames.length)];
    String lastName = lastNames[random.nextInt(lastNames.length)];
    String photoUrl = photoUrls[random.nextInt(photoUrls.length)];
    return Customer.builder()
        .id(UUID.randomUUID())
        .firstName(firstName)
        .lastName(lastName)
        .photoUrl(photoUrl)
        .build();
  }

  public static CreateCustomerDTO generateCreateCustomerDTO() {
    Random random = new Random();
    String firstName = firstNames[random.nextInt(firstNames.length)];
    String lastName = lastNames[random.nextInt(lastNames.length)];
    String photoUrl = photoUrls[random.nextInt(photoUrls.length)];
    return CreateCustomerDTO.builder()
        .firstName(firstName)
        .lastName(lastName)
        .photoUrl(photoUrl)
        .build();
  }

  public static UpdateCustomerDTO generateUpdateCustomerDTO() {
    Random random = new Random();
    String firstName = firstNames[random.nextInt(firstNames.length)];
    String lastName = lastNames[random.nextInt(lastNames.length)];
    String photoUrl = photoUrls[random.nextInt(photoUrls.length)];
    return UpdateCustomerDTO.builder()
        .firstName(firstName)
        .lastName(lastName)
        .photoUrl(photoUrl)
        .build();
  }
}
