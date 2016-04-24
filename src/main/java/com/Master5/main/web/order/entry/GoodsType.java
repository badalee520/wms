package com.Master5.main.web.order.entry;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * 商品类型
 * 
 * @author Bada Lee
 *
 */
@Entity
@Table(name = "goodsType", uniqueConstraints = { @UniqueConstraint(columnNames = { "name" }) })
public class GoodsType {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(unique = true)
	private String name;

}
