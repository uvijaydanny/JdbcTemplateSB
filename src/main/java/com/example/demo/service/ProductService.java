package com.example.demo.service;

import java.util.*;
import com.example.demo.model.*;

public interface ProductService {

	public Collection<Product> findAll();
	
	public Product find(int id);
	
	public String deleteProduct(int id);
	
	public Collection<Product> addProduct(Product prod);
}
