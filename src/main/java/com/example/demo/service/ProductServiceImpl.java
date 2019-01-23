package com.example.demo.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.model.*;
import com.example.demo.DAO.*;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductDAO productDAO;
	
	@Override
	public Collection<Product> findAll() {
		return this.productDAO.findAll();
	}

	@Override
	public Product find(int id) {
		return this.productDAO.find(id);
	}

	@Override
	public String deleteProduct(int id) {
		this.productDAO.deleteProduct(id);
		return "deleted";
	}

	@Override
	public Collection<Product> addProduct(Product prod) {
		this.productDAO.addProduct(prod);
		return this.productDAO.findAll();
	}

	
}
