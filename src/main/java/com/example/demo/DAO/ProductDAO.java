package com.example.demo.DAO;

import java.util.*;
import com.example.demo.model.*;

public interface ProductDAO {

	public Collection<Product> findAll();
	
	public Product find(int id);

	public String deleteProduct(int id);
	
	public Collection<Product> addProduct(Product prod);
}
