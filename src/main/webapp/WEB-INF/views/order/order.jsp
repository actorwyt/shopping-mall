<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>账务</title>
	<jsp:include page="../base.jsp"></jsp:include>
</head>
<body> 
	<jsp:include page="../baseHeader.jsp"></jsp:include>
	<!--start-cart-->
	<!--start-breadcrumbs-->
	<div class="breadcrumbs">
		<div class="container">
			<div class="breadcrumbs-main">
				<ol class="breadcrumb">
					<li><a href="${contextPath}/index">主页</a></li>
					<li class="active">账务</li>
				</ol>
			</div>
		</div>
	</div>
	<!--end-breadcrumbs-->
	<!--start-order-->
	<div class="checkout">
		<div class="container">
			<div class="check-top heading">
				<h2>我的订单</h2>
			</div>
			<div class="check-top">
				<div class="order-items">
			 		<div id="total"></div>	
					<div class="in-order" >
						<ul class="unit">
							<li><span>项目</span></li>
							<li><span>商品名称</span></li>	
							<li><span>购买时间</span>
							<li><span>购买数量</span><li>	
							<li><span>商品单价</span>
							<li><span>总价</span></li>
							<div class="clearfix"> </div>
						</ul>			
					</div> 
					<div id="orderItems" class="in-order">
					
					</div>
					<div class="orderSum" style="text-align:right">
						<span id="orderSum" >总计：</span>
					</div>
					<div style="text-align:center">
							<ul id="pagination" class="pagination pagination-lb"></ul>
					</div> 
				</div>
			</div>	
		</div>
	</div>
	<!--end-checkout-->
	<jsp:include page="../baseFooter.jsp"></jsp:include>
	<script type="text/javascript">
		$(function(){			
			//初始化分页插件
			var defaultItemsOnPage = 6;	
			var url = "${contextPath}/order/count";
			$("#pagination").common("initPagination",{
				url : url,
				itemsOnPage : defaultItemsOnPage,
				onChangePage : function (page) {
					changePage(page);
				}
			});
			
			//获取订单总价格
			$.ajax({
				type: "GET",
				dataType: "json",
				url:"${contextPath}/order/getSum",
                success: function (data, textStatus) {
                	var total = data;
                	$("#orderSum").append(total);
                }
			});		
			
		});
		
		function changePage(currentPage) {
			var params = {};
			var currentPage = currentPage == "" ? 1 : $('#pagination').pagination("getCurrentPage");
			params.currentPage = currentPage;
			params.itemsOnPage = $('#pagination').pagination("getItemsOnPage");
			$.ajax({
				type: "GET",
				dataType: "json",
				url:"${contextPath}/order/getOrderGoods",
				data:$.param(params),
                success: function (data, textStatus) {
                	//alert(data[0].imgSrc)
                	
                	var list = "";
                	for(var i = 0; i < data.length ; i ++) {
                		list += '<ul class="order-header">'
                			 + '<input type="hidden" name="goodsId" value=' + data[i].goodsId + '>'
                			 +  '<li class="ring-in"><a href="${contextPath}/goods/showGoodsPage?goodsId=' + data[i].goodsId + '" >';
						 	 if(data[i].imgSrc != null && data[i].imgSrc !=''){
							 	list += "<img class='img-responsive zoom-img' onerror='javascript:this.src=\"${imagesPath}/noPic.jpg\"' alt='" + data[i].goodsName + "' src='${contextPath}/goods/showGoodsImage/?imgSrc=" + data[i].imgSrc+ "'/>"; 
						 	 } else {
							 	list += "<img class='img-responsive zoom-img' src='${imagesPath}/noPic.jpg\' />";   
						 	 }	
                		list += '</a></li>'	
							 +	'<li><span class="name">' + data[i].goodsName + '</span></li>'
							 +	'<li><span class="time">' + data[i].orderTime + '</span></li>'
							 +  '<li><span class="amount">' + data[i].purchasedAmount + '</span></li>'
							 +	'<li><span class="cost">' + data[i].purchasedUnitPrice + '</span></li>'
 							 +	'<li><span class="cost">' + data[i].priceSum + '</span></li>'
							 +  '<div class="clearfix"> </div>'
							 +  '</ul>';
                	}
                   	$("#orderItems").html(list);  
                   	
                }
			});		
		}

		
	</script>
</body>
</html>