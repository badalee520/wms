package com.Master5.main.web.order.Controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.Master5.main.annotation.CheckPermission;
import com.Master5.main.utils.constant.Key;
import com.Master5.main.utils.constant.MsgKey;
import com.Master5.main.utils.constant.OrdersStatus;
import com.Master5.main.utils.constant.SysKey;
import com.Master5.main.web.order.entry.Ingredient;
import com.Master5.main.web.order.entry.IngredientType;
import com.Master5.main.web.order.entry.Orders;
import com.Master5.main.web.order.entry.OrdersIngredient;
import com.Master5.main.web.order.entry.Supplier;
import com.Master5.main.web.order.service.OrderService;
import com.Master5.main.web.user.entry.User;

@Controller
@RequestMapping(value = "order")
public class OrdersController {

	@Autowired
	OrderService orderService;

	@RequiresPermissions(value = "order:listOrders")
	@CheckPermission(name = "订单列表", method = "order:listOrders")
	@RequestMapping(value = { "", "listOrders" })
	public String listOrders(Model model) {

		List<Orders> list = orderService.queryOrders();

		model.addAttribute("list", list);

		return "order/listOrders";
	}

	@RequiresPermissions(value = "order:addOrders")
	@CheckPermission(name = "添加订单", method = "order:addOrders")
	@RequestMapping(value = "addOrders", method = RequestMethod.POST)
	public String addOrders(Orders bean, int[] ingredientId, int[] amount, RedirectAttributes redirectAttributes) {
		List<OrdersIngredient> detail = new ArrayList<>();

		for (int i = 0; i < amount.length; i++) {
			if (amount[i] == 0) {
				continue;
			}

			OrdersIngredient ordersIngredient = new OrdersIngredient();
			ordersIngredient.setAmount(amount[i]);
			ordersIngredient.setIngredientId(orderService.queryIngredient(ingredientId[i]));

			detail.add(ordersIngredient);

		}

		bean.setDetail(detail);
		// bean.setBuyyer((User)
		// SecurityUtils.getSubject().getSession().getAttribute(Key.LOGINED));
		// bean.setBuytime(Calendar.getInstance().getTime());
		bean.setCreatetime(Calendar.getInstance().getTime());

		orderService.saveOrders(bean);
		return "redirect:listOrders";
	}

	@RequiresPermissions(value = "order:buyOrders")
	@CheckPermission(name = "订单采购", method = "order:buyOrders")
	@RequestMapping(value = "buyOrders/{id}")
	public String buy(@PathVariable int id, Model model) {
		Orders orders = orderService.queryOrders(id);
		if (orders.getStatus() != OrdersStatus.NORMAL) {
			return "redirect:../listOrders";
		}
		orders.setStatus(OrdersStatus.BUY);
		orders.setBuyyer((User) SecurityUtils.getSubject().getSession().getAttribute(Key.LOGINED));
		orders.setBuytime(Calendar.getInstance().getTime());
		orderService.saveOrders(orders);
		return "redirect:../listOrders";
	}

	@RequiresPermissions(value = "order:receiveOrders")
	@CheckPermission(name = "订单接收", method = "order:receiveOrders")
	@RequestMapping(value = "receiveOrders/{id}")
	public String receive(@PathVariable int id, Model model) {
		Orders orders = orderService.queryOrders(id);
		if (orders.getStatus() != OrdersStatus.BUY) {
			return "redirect:../listOrders";
		}
		orders.setStatus(OrdersStatus.REVICVE);
		orders.setManager((User) SecurityUtils.getSubject().getSession().getAttribute(Key.LOGINED));
		orders.setIntime(Calendar.getInstance().getTime());
		orderService.saveOrders(orders);
		
		
		return "redirect:../listOrders";
	}

	@RequiresPermissions(value = "order:delOrders")
	@CheckPermission(name = "订单删除", method = "order:delOrders")
	@RequestMapping(value = "delOrders/{id}")
	public String delOrders(@PathVariable int id, Model model) {
		try {
			orderService.deleteOrders(id);
		} catch (JpaSystemException e) {
			List<String> list = new ArrayList<>();
			list.add("删除失败，有相关联的数据未删除。");
			model.addAttribute(MsgKey.msg, list);
		}
		return "redirect:../listOrders";
	}
	
//	@ResponseBody
//	@RequestMapping(value="stock")
//	public List<Object> listStock(){
//		List<OrdersIngredient> list=orderService.queryRecord();
//		list.
//		
//		return list;
//	}
	
	//select 商品名，sum(记录) from 表  where 商品id=商品id  group by 商品id

	@ResponseBody
	@RequestMapping(value = "listIngredientTypeJson", method = RequestMethod.POST)
	public List<IngredientType> listIngredientTypeJson(Model model) {
		List<IngredientType> list = orderService.queryIngredientType();
		return list;
	}

	@RequiresPermissions(value = "order:listIngredientType")
	@CheckPermission(name = "商品类型列表", method = "order:listIngredientType")
	@RequestMapping(value = "listIngredientType")
	public String listIngredientType(Model model) {

		List<IngredientType> list = orderService.queryIngredientType();

		model.addAttribute("list", list);

		return "order/listIngredientType";
	}

	@RequiresPermissions(value = "order:addIngredientType")
	@CheckPermission(name = "添加商品类型", method = "order:addIngredientType")
	@RequestMapping(value = "addIngredientType", method = RequestMethod.POST)
	public String addIngredientType(IngredientType type, RedirectAttributes redirectAttributes) {

		List<String> msgList = new ArrayList<String>();

		orderService.addIngredientType(type);
		msgList.add("添加成功");
		redirectAttributes.addFlashAttribute(MsgKey.msg, msgList);
		return "redirect:listIngredientType";
	}

	@RequiresPermissions(value = "order:delIngredientType")
	@CheckPermission(name = "删除商品类型", method = "order:delIngredientType")
	@RequestMapping(value = "delIngredientType/{id}")
	public String delIngredientType(@PathVariable int id, Model model) {
		try {
			orderService.deleteIngredientType(id);
		} catch (JpaSystemException e) {
			List<String> list = new ArrayList<>();
			list.add("删除失败，有相关联的数据未删除。");
			model.addAttribute(MsgKey.msg, list);
		}
		return "redirect:../listIngredientType";
	}

	@RequiresPermissions(value = "order:listSupplier")
	@CheckPermission(name = "供货商列表", method = "order:listSupplier")
	@RequestMapping(value = "listSupplier")
	public String listSupplier(Model model) {

		List<Supplier> list = orderService.querySupplier();

		model.addAttribute("list", list);

		return "order/listSupplier";
	}

	@ResponseBody
	@RequestMapping(value = "listSupplierJson")
	public List<Supplier> listSupplierJson(Model model) {

		return orderService.querySupplier();
	}

	@RequiresPermissions(value = "order:addSupplier")
	@CheckPermission(name = "添加供货商", method = "order:addSupplier")
	@RequestMapping(value = "addSupplier", method = RequestMethod.POST)
	public String addSupplier(Supplier type, RedirectAttributes redirectAttributes) {

		List<String> msgList = new ArrayList<String>();

		orderService.addSupplier(type);
		msgList.add("添加成功");
		redirectAttributes.addFlashAttribute(MsgKey.msg, msgList);
		return "redirect:listSupplier";
	}

	@RequiresPermissions(value = "order:delSupplier")
	@CheckPermission(name = "删除供货商", method = "order:delSupplier")
	@RequestMapping(value = "delSupplier/{id}")
	public String delSupplier(@PathVariable int id) {
		orderService.deleteSupplier(id);
		return "redirect:../listSupplier";
	}

	@RequiresPermissions(value = "order:listIngredient")
	@CheckPermission(name = "商品列表", method = "order:listIngredient")
	@RequestMapping(value = "listIngredient")
	public String listIngredient(Model model) {

		List<Ingredient> list = orderService.queryIngredient();

		model.addAttribute("list", list);

		return "order/listIngredient";
	}

	@ResponseBody
	@RequestMapping(value = "listIngredientJson")
	public List<Ingredient> listIngredientJson(Model model) {
		return orderService.queryIngredient();
	}

	@RequiresPermissions(value = "order:addIngredient")
	@CheckPermission(name = "添加商品", method = "order:addIngredient")
	@RequestMapping(value = "addIngredient", method = RequestMethod.POST)
	public String addIngredient(Ingredient ingredient, RedirectAttributes redirectAttributes) {
		orderService.addIngredient(ingredient);
		return "redirect:listIngredient";
	}

	@RequiresPermissions(value = "order:delIngredient")
	@CheckPermission(name = "删除商品", method = "order:delIngredient")
	@RequestMapping(value = "delIngredient/{id}")
	public String delIngredient(@PathVariable int id, Model model) {
		try {
			orderService.deleteIngredient(id);
		} catch (JpaSystemException e) {
			e.printStackTrace();
			model.addAttribute(MsgKey.msg, "有相关关联数据不可删除");
		}
		return "redirect:../listIngredient";
	}

}
