package com.stech.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.stech.api.dto.ProductDTO;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@Configuration
@RequestMapping("/user-service")
public class UserServiceController {
	@Autowired
	@Lazy
	RestTemplate restTemplate;

	public static final String USER_SERVICE = "userService";
	
	private static final String BASE_URL = "http://localhost:8080/order";

	@GetMapping("/displayOrders")
	@CircuitBreaker(name= USER_SERVICE, fallbackMethod="getDefaultProductList")
	public List<ProductDTO> displayProducts(@RequestParam(value="productType",required = false) String productType) {
		String finalURL = productType == null ? BASE_URL : BASE_URL + "/" + productType;
		System.out.println(finalURL);
		return restTemplate.getForObject(finalURL, ArrayList.class);
	}
	
	public List<ProductDTO> getDefaultProductList(Exception e){
		return Stream.of(
				new ProductDTO(101,"Default LED TV", "Electronics",48000.5),
				new ProductDTO(102,"Default Refigarator", "Electronics",25000),
				new ProductDTO(103,"Default Mobile Phone", "Electronics",18000),
				new ProductDTO(104,"Default Sports Shoes", "Foot wear",4500),
				new ProductDTO(105,"Default ParleG", "Confectionary",25),
				new ProductDTO(106,"Default Alpenlibe", "Confectionary",95),
				new ProductDTO(107,"Default Nike Sliper", "Foot wear",500)
				).collect(Collectors.toList());
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
