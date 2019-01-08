<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>商品信息</title>
	<link href="${stylesPath}/flexslider.css" rel="stylesheet" type="text/css" media="all" />
	<link href="${stylesPath}/bootstrapSpinner.css" rel="stylesheet" type="text/css" media="all" />
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
					<li class="active">商品信息</li>
				</ol>
			</div>
		</div>
	</div>
	<!--end-breadcrumbs-->
	<!--start-single-->
	<div class="single contact">
		<div class="container">
			<div class="single-main">
				<div class="col-md-9 single-main-left" >
				<form id="dataForm" role="form">
				<input type="hidden" id="goodsId" name="goodsId" value="${goodsId}"/>
				<input type="hidden" id="imgSrc" name="imgSrc" />
				<div class="sngl-top">
					<div class="col-md-5 single-top-left">	
						<img id="image" src="" alt=""/>
					</div>				
				
					<div class="col-md-7 single-top-right">
						<div class="single-para simpleCart_shelfItem">
						<h2 name="goodsName"></h2>
							<div class="star-on">
								<ul class="star-footer">
										<li><a href="#"><i> </i></a></li>
										<li><a href="#"><i> </i></a></li>
										<li><a href="#"><i> </i></a></li>
										<li><a href="#"><i> </i></a></li>
										<li><a href="#"><i> </i></a></li>
									</ul>
								<div class="review">
									<a id="selledAmount" name="selledAmount" href="#">已售数量：</a>	
								</div>
							<div class="clearfix"> </div>
							</div>
							
							<h5 id="unitPrice" name="unitPrice" class="item_price">$</h5>
							<p name="abstractInfo"></p>
							<div class="available">
								<ul>
									<li>数量：
										<div class="spinner">  
					                        <div class="form-group form-group-options">  
					                            <div id="purchasedAmountSpinner" class="input-group input-group-option quantity-wrapper">  
					                                <span  class="input-group-addon input-group-addon-remove quantity-remove btn">  
					                                    <span class="glyphicon glyphicon-minus"></span>  
					                                </span>  							  
					                                <input id="purchasedAmount" type="text" onfocus="this.blur();" value="1" name="option[]" class="form-control quantity-count" placeholder="1">  							  
					                                <span class="input-group-addon input-group-addon-remove quantity-add btn">  
					                                    <span class="glyphicon glyphicon-plus"></span>  
					                                </span>                                      
					                            </div>  							                              
					                        </div> 
							            </div>
					            	</li>
					            	<li>库存：<a id="amount" name="amount" href="#"></a></li>					
								<div class="clearfix"> </div>
							</ul>
						</div>
						<c:if test="${sessionScope['user']['roleId'] eq 2 }">
							<a id="btnEdit" href="${contextPath}/seller/editGoods?goodsId=${goodsId}" class="add-cart item_add">编辑</a>	
						</c:if>		
						<c:if test="${sessionScope['user']['roleId'] ne '2' }">							
							<a id="btnAddToCart" href="javascript:void(0)" class="add-cart item_add ">加入购物车</a>				
						</c:if>	
						</div>
					</div>
					<div class="clearfix"> </div>
				</div>				
				<div class="tabs">
					<ul class="menu_drop">
					<li class="item1"><a href="#">摘要</a>
						<ul>
							<li class="subitem1"><a id="abstractInfo" name="abstractInfo" href="#"></a></li>							
						</ul>
					</li>
					<li class="item2"><a href="#">详细描述</a>
						<ul>
						    <li class="subitem1"><a id="description" name="description" href="#"></a></li>
						</ul>
					</li>				
	 				</ul>
				</div>

				</form>
			</div>
			</div>
		</div>
	</div>
	<!--end-single-->
	<jsp:include page="../baseFooter.jsp"></jsp:include>
	<script type="text/javascript" src="${scriptsPath}/imagezoom.js"></script>
	<script type="text/javascript" src="${scriptsPath}/jquery.flexslider.js"></script>	
	<script type="text/javascript" src="${scriptsPath}/bootstrapSpinner.js"></script>	
	<script type="text/javascript">
		$(function() {			
			//加载商品信息
			loadGoodsInfo();
			
			var menu_ul = $('.menu_drop > li > ul'),
		           menu_a  = $('.menu_drop > li > a');
		    
		    <c:if test="${empty isPurchased or isPurchased eq false }">	
				$('#btnAddToCart').click(function(){
					addToCart();
				});	
			</c:if>
			<c:if test="${not empty isPurchased and isPurchased eq true }">
				$('#btnAddToCart').addClass("btn-disable");
				$('.quantity-add').unbind("click");
				$('.quantity-remove').unbind("click");	
			</c:if>
		
			$('#purchasedAmount').change(function() {
				var purchasedAmount = parseInt($(this).val());
				var amount = parseInt($("#amount").html());
				if(purchasedAmount > amount || amount == 0) {
					$("#btnAddToCart").addClass("btn-disable");
				} else {
					$("#btnAddToCart").removeClass("btn-disable");
				}
			});
			
		});
		
		function loadGoodsInfo() {
			var goodsId = $('#goodsId').val();
			var params = {};
			params.goodsId = goodsId;
			$('#dataForm').common("loadForm", {
				data:$.param(params),
				url:"${contextPath}/goods/getOneGoods",
				onLoadError: function (data) {
					alert('运行超时，请重试！');
				},
				onLoadComplete : function () {
					if(parseInt($('#amount').html()) == 0) {
						$("#btnAddToCart").addClass("btn-disable");
					}	
					loadImage();
				}
			});
		}
		
		//加载图片
		function loadImage() {
			var imgUrl = $('#imgSrc').val();
			if(imgUrl == "") {
				$('#image').attr("src","${imagesPath}/noPic.jpg");
			}
			else {
				$('#image').attr("src","${contextPath}/goods/showGoodsImage/?imgSrc=" + imgUrl);
			}
		}
		
		function addToCart() {
			var confirmMsg = "加入购物车";
			Messager.confirm({ message: "确认" + confirmMsg }).on(function (e) {
                if (!e) {
                    return false;
                }	
				var params = {};
				params.goodsId = $("#goodsId").val();
				params.purchasedAmount = $("#purchasedAmount").val();
				$('#btnAddToCart').addClass("btn-disable");
				$.ajax({
					type: "POST",
					dataType: "json",
					url:"${contextPath}/shoppingCart/addToCart",
					data:JSON.stringify(params),
					success: function(data, textStatus) {
						var res = eval("(" + data + ")");
						Messager.alert(res.msg);					
						if (res.status == 'success') {	
							getCartCount();
						} 
						$('#btnAddToCart').removeClass("btn-disable");						
					}
				});
			});
		}
		
	</script>	
</body>
</html>