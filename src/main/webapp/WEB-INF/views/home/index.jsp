<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Home</title>
	<jsp:include page="../base.jsp"></jsp:include>
</head>
<body> 
	<jsp:include page="../baseHeader.jsp"></jsp:include>
	<jsp:include page="../baseMenu.jsp"></jsp:include>
	<div class="product"> 
		<div class="container">
			<div class="product-top">				
									
			</div>			
			<div style="text-align:center">
				<ul id="pagination" class="pagination pagination-lb"></ul>
			</div>
			<div class="clearfix"></div>
		</div>
	</div>
	<!--product-end-->
	<jsp:include page="../baseFooter.jsp"></jsp:include>
	<script type="text/javascript">
		$(function(){			
			//初始化分页插件
			var defaultItemsOnPage = 4;	
			var status = $('[name="status"]').val();
			var url = "${contextPath}/goods/count?status=" + status;
			$("#pagination").common("initPagination",{
				url:url,
				itemsOnPage:defaultItemsOnPage,
				onChangePage: function (page) {
					changePage(page);
				}
			});
					
		});
		
		function changePage(currentPage) {
			var params = {};
			var currentPage = currentPage == "" ? 1 : $('#pagination').pagination("getCurrentPage");
			params.currentPage = currentPage;
			params.itemsOnPage = $('#pagination').pagination("getItemsOnPage");
			params.status = $('[name="status"]').val();
			params.keywords = encodeURI($('#keywords').val());
			 $.ajax({
				type: "GET",
				dataType: "json",
				data: $.param(params),
				url:"${contextPath}/goods/getAllGoods",
                success: function (data, textStatus) {
                	var list = ""; 
                	for(var i = 0; i < data.length ; i ++) {
                		if( i % 4 == 0) {
                			list += "<div class='product-one'>"
                		} 
                		list += "<div class='col-md-3 product-left'>"
							 + "<div class='product-main simpleCart_shelfItem'>"
							 + "<a href='${contextPath}/goods/showGoodsPage?goodsId=" + data[i].goodsId + "' class='mask'>";
							 if(parseInt(data[i].selledAmount) > 0) {
								 <c:if test="${sessionScope['user']['roleId'] eq 1}">
								 	 list += "<span class='had'><b>已购买</b></span>" ;
								 </c:if>
							 }
							 if(data[i].imgSrc != null && data[i].imgSrc !=''){
								 list += "<img class='img-responsive zoom-img' onerror='javascript:this.src=\"${imagesPath}/noPic.jpg\"' alt='" + data[i].goodsName + "' src='${contextPath}/goods/showGoodsImage/?imgSrc=" + data[i].imgSrc+ "'/>"; 
							 } else {
								 list += "<img class='img-responsive zoom-img' src='${imagesPath}/noPic.jpg\' />";   
							 }	 
						list += "</a>"
							 +	"<div class='product-bottom'>"
							 +	"<h3 style='overflow:hidden;text-overflow:ellipsis;white-space:nowrap;'>" + data[i].goodsName + "</h3>"
							 <c:if test="${sessionScope['user']['roleId'] ne 2}">
							 
							 </c:if>
							 if(parseInt(data[i].selledAmount) > 0) {
								 <c:if test="${sessionScope['user']['roleId'] eq 2}">
								 	list += "<span class='had'><b>已售出</b></span>"
								 </c:if>
							 }
						list +=	"</div>"
							 +  "</div>" 
							 +	"</div>"		 
						 if( i % 4 == 3 || i == data.length - 1) {
							 list += "<div class='clearfix'></div>"
							      + "</div>"
	                	 }  
                	}
                   	$(".product-top").html(list);  	
                }
			});		
		}
	</script>
</body>
</html>