package dev.howard.demo.user;

public record Address(
    String street,
    String suite,
    String city,
    String zipCode,
    Geo geo
 
) {

}
