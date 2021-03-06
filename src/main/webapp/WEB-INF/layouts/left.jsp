<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<style type="text/css">
/*定义二级菜单样式*/
.secondmenu {
	padding-top: 10px;
	padding-bottom: 1px;
	padding-left: 20%;
	text-align: left;
	background-color: #F3F3F3;
}

.secondmenu a {
	padding-top: 10px;
	padding-bottom: 10px;
	font-size: 14px;
	color: #21B6AE;
}
</style>
<div class="list-group" >
	<div class="list-group-item">
		<a href="${ctx}/user/info" style="color:#666666;"><span class="glyphicon glyphicon-home"></span> &nbsp;&nbsp;首&nbsp;&nbsp;&nbsp;&nbsp;页</a>
	</div>
	<div class="list-group-item" data-toggle="collapse" data-target="#systemMenu"><span class="glyphicon glyphicon-cog"></span> 系统管理</div>
	<div id="systemMenu" class="collapse secondmenu">
		<ul class="list-unstyled">
			<shiro:hasPermission name="user:list">
				<li><a href="${ctx}/user/list"><span class="glyphicon glyphicon-user"></span> 用户管理</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="role:list">
				<li><a href="${ctx}/role/list"><span class="glyphicon glyphicon-link"></span> 角色管理</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="permission:list">
				<li><a href="${ctx}/permission/list"><span class="glyphicon glyphicon-tag"></span> 权限管理</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="record:list">
				<li><a href="${ctx}/record/list"><span class="glyphicon glyphicon-home"></span> 审查管理</a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<div class="list-group-item" data-toggle="collapse" data-target="#orderMenu"><span class="glyphicon glyphicon-leaf"></span> 订单管理</div>
	<div id="orderMenu" class="collapse secondmenu">
		<ul class="list-unstyled right">
			<shiro:hasPermission name="order:listOrders">
				<li><a href="${ctx}/order/listOrders"><span class="glyphicon glyphicon-list"></span> 订单列表</a></li>
			</shiro:hasPermission>

		</ul>
	</div>
	<div class="list-group-item" data-toggle="collapse"
		data-target="#ingredientMenu"><span class="glyphicon glyphicon-send"></span> 食材管理</div>
	<div id="ingredientMenu" class="collapse secondmenu">
		<ul class="list-unstyled right">
			<shiro:hasPermission name="order:listIngredientType">
				<li><a href="${ctx}/order/listIngredientType"><span class="glyphicon glyphicon-barcode"></span> 食材种类列表</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="order:listSupplier">
				<li><a href="${ctx}/order/listSupplier"><span class="glyphicon glyphicon-road"></span> 供应商列表</a></li>
			</shiro:hasPermission>
			<shiro:hasPermission name="order:listIngredient">
				<li><a href="${ctx}/order/listIngredient"><span class="glyphicon glyphicon-th"></span> 食材列表</a></li>
			</shiro:hasPermission>
		</ul>
	</div>
	<div class="list-group-item">
	<a href="${ctx}/order/stock" style="color:#666666"><span class="glyphicon glyphicon-list-alt"></span> 查看内存</a>
	</div>
</div>


