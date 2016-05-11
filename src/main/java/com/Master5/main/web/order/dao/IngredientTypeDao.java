package com.Master5.main.web.order.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Master5.main.web.order.entity.IngredientType;

@Repository
public interface IngredientTypeDao extends JpaRepository<IngredientType, Integer> {

}
