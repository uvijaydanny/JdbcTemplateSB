package com.example.demo.DAO;

import java.util.*;
import com.example.demo.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.*;

@Repository
public class ProductDAOImpl implements ProductDAO {

	@Autowired
	private JdbcTemplate jdbcTemplate; 
	
	@Override
	public Collection<Product> findAll() {
		return this.jdbcTemplate.query("select * from product", 
				new BeanPropertyRowMapper<Product>(Product.class)
				);
	}

	@Override
	public Product find(int id) {
		return this.jdbcTemplate.queryForObject("select * from product where id = ?", 
				new BeanPropertyRowMapper<Product>(Product.class), id);
	}

	@Override
	public String deleteProduct(int id) {
		String del_prod = "delete from product where id = ?";
		this.jdbcTemplate.update(del_prod, id);
		return "deleted";
	}

	@Override
	public Collection<Product> addProduct(Product prod) {

		String add_prod = "insert into Product values(?,?,?,?)";
		this.jdbcTemplate.update(add_prod, prod.getId(),prod.getName(),prod.getPrice(),prod.getDescription());

		return this.jdbcTemplate.query("select * from product", 
				new BeanPropertyRowMapper<Product>(Product.class)
				);
		
	}
}
