<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page isELIgnored="false"%>


<html>
<head>
<title>楼盘签约详情</title>


<style>
.div-a {
	float: left;
	width: 49%;
	border: 1px solid #000
}

.div-b {
	float: left;
	width: 49%;
	border: 1px solid #000
}
</style>


<script type="text/javascript">

function do_subscribe()
{
 	var propertyId=document.getElementById("propertyId").attributes["value"].value;

	var xmlhttp;
	if (window.XMLHttpRequest){
  		// code for IE7+, Firefox, Chrome, Opera, Safari
  		xmlhttp=new XMLHttpRequest();
  	}else{
  		// code for IE6, IE5
  		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  	}
  	
	xmlhttp.onreadystatechange=function(){
	
		if (xmlhttp.readyState==4 && xmlhttp.status==200){
			
			//alert(xmlhttp.responseText);
			
			var responseObj = eval('(' + xmlhttp.responseText + ')');
			if(responseObj.success === "true"){
 				alert("关注成功");
 				//document.getElementById("subscribeButton").style.display="none";
				//document.getElementById("unsubscribeButton").style.display="inline";
				
				document.getElementById("subscribeArea").innerHTML="<button  id='unsubscribeButton' type='button' onclick='do_unsubscribe()'>（已关注）取消关注</button>";
			
			}else{
				alert("关注失败");
    		}
    	}
  	}
  	 
	xmlhttp.open("GET","./do_subscribe?propertyId="+propertyId,true);
	xmlhttp.send();
}

function do_unsubscribe()
{
	  	
  	var propertyId=document.getElementById("propertyId").attributes["value"].value;

	var xmlhttp;
	if (window.XMLHttpRequest){
  		// code for IE7+, Firefox, Chrome, Opera, Safari
  		xmlhttp=new XMLHttpRequest();
  	}else{
  		// code for IE6, IE5
  		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
  	}
  	
	xmlhttp.onreadystatechange=function(){
	
		
		if (xmlhttp.readyState==4 && xmlhttp.status==200){
		
    		//alert(xmlhttp.responseText);
    		
    		var responseObj = eval('(' + xmlhttp.responseText + ')');
    		if(responseObj.success === "true"){
    		 	alert("取消关注成功");
    		 	//document.getElementById("subscribeButton").style.display="inline";
				//document.getElementById("unsubscribeButton").style.display="none";
    			document.getElementById("subscribeArea").innerHTML="<button  id='subscribeButton' type='button' onclick='do_subscribe()'>+关注</button>";
    		}else{
    			alert("取消关注失败");
    		}
    	
    	}
    	
  	}

	
	xmlhttp.open("GET","./do_unsubscribe?propertyId="+propertyId,true);
	xmlhttp.send();
}

</script>

</head>
<body>
	<h2>
		<a href="./userIdx" ><c:out value="=>${account}" /></a>
		<a href="./do_logout" >logout</a>
	
	</h2>
	<h3>
	
	
	  	<div  id="subscribeArea" >
	  		<c:if test="${ isSubscripted == true }">	 		  		 	
	  		 	<button  id="unsubscribeButton" type="button" onclick="do_unsubscribe()">（已关注）取消关注</button>	  	  		 	
	  		</c:if>
	  		<c:if test="${ isSubscripted == false }">		
	  		 	<button  id="subscribeButton" type="button" onclick="do_subscribe()">+关注</button>	  		 		  		 	 	  		 	
	  		</c:if>
	  	</div>
	 
	  	
		${property.propertyName}
		${property.city}
		${property.district}		
		${property.propertyAddress}
		${property.developer}
		
		
	</h3>

	
	<form name="houseSaleFind" action="./property_jstl" method="get" >
		
			楼盘：<input type="text" id="propertyId" name="propertyId" value="${property.propertyId}" />
			开始日期：<input type="date" name="dateFrom" value="${dateFrom}"/>
			截止日期：<input type="date" name="dateTo" value="${dateTo}"/>
			
			<input type="submit" value="查询" />
			
	</form>

	
	
	
	<c:if test="${not empty dailyStates}">

		<ul>
			<c:forEach var="dailyState" items="${dailyStates}">
				
				<HR align=center width=100% color=#987cb9 SIZE=1>
				
				<li>
					<h3>
						<c:out value="--------  ${dailyState.date} ----------" />
					</h3> 
					
					<div class="div-a">
					
						<c:forEach var="signed" items="${dailyState.sharpedSigneds}">
							<ul>
								<li>签约套数：${signed.signedNumber}</li>
								<li>签约面积：${signed.signedAreaFormat}</li>
								<li>签约均价：${signed.signedAvgPrice}</li>	
								<li>签约总价：${signed.signed_zongjia}(申报总价：${signed.declare_Total_dePrice_zongjia}) 折扣：${signed.saledDiscount}</li>											
								<li>${signed.signedTime}</li>
								
							</ul>
							<br>
						</c:forEach> 				
					</div>
					
				<!-- 	<c:out value="--------------------------------" />   -->
					<div class="div-b">
					
						<c:forEach var="stateInfo" items="${dailyState.stateInfos}">
							<ul>
								<li>${stateInfo.houseStateName}</li>
								<li>${stateInfo.located}</li>
								<li>${stateInfo.area_builtup}(${stateInfo.area_inside})得房率：${stateInfo.area_Rate}</li>			
								<li>申报总价：${stateInfo.dePrice_zongjia}</li>
								<li>${stateInfo.stateChangeTime}</li>
							</ul>
							<br>
						</c:forEach>
					
					</div>

				</li>
				
			
				
			</c:forEach>
		</ul>

	</c:if>

</body>
</html>