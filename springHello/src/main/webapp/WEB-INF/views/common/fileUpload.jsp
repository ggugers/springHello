<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
	<head>	
		<meta name="title" content="Amway Academy">
		<meta name="keywords" content="Amway Academy">
		<meta name="description" content="">
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=Edge">		
	
		<title>Amway Academy 관리자</title>
		<script type="text/javascript" src="/js/jquery-1.10.2.min.js"></script>
		<script type="text/javascript" src="/js/jquery.form.js"></script>
		<script type="text/javascript">
		$(document.body).ready(function(){
			$("#insertBtn").on("click", function(){
				imageSubmit();
			});
		});
		
		var imageSubmit = function(){
			// 저장전 Validation

			alert($("#courseimage").val())
			if($("#courseimagefile").val().length<1 && $("#courseimage").val().length<1){
				alert("대표이미지를 등록해 주세요.");
				return;
			}

			var fileYn = false;
			$( "input:file" ).each(function( index ){
				if($( this ).val().length>0 ){
					fileYn = true;
				}
			});
			if(!fileYn){
				saveSubmit();
				return;
			}
			$("#frm").ajaxForm({
				dataType:"json",
				data:{mode:"course"},
				url:'/common/fileUploadAjax.do',
				beforeSubmit:function(data, form, option){
					return true;
				},
		        success: function(result, textStatus){
		        	for(i=0; i<result.length;i++){
		        		$("#"+result[i].fieldName+"file").val(result[i].FileSavedName);
		        		if(result[i].fieldName == "filedown"){
		        			$("#"+result[i].fieldName+"file").val( result[i].OriginalFilename + "|" + result[i].FileSavedName );
		        		}
		        	}
		        	saveSubmit();
		        },
		        error: function(){
		           	alert("파일업로드 중 오류가 발생하였습니다.");
		           	return;
		        }
			}).submit();
		}
		var saveSubmit = function(){
			alert("저장")
		}
		
		
		/** 이미지 미리보기
		 * 
		 */
		function getThumbnailPrivew(html, $target, w, h) {
			if(html.value == ""){
				return;
			}
			var _lastDot = html.value.lastIndexOf('.');
			var fileExt = html.value.substring(_lastDot+1, html.value.length).toLowerCase();
			/* 
			if( fileExt != "jpg" && fileExt != "gif" && fileExt != "png" ) {
				alert("이미지 파일(jpg,gif,png)만 입력하세요.");
				try{$(html).replaceWith($(html).clone(true))}catch(e){};
				try{html.value = ""}catch(e){};
				return;
			}
		    */
			if (html.files && html.files[0]) {
		        var reader = new FileReader();
		        reader.onload = function (e) {
		            $target.css('display', '');
		            var wStr = "";
		            var hStr = "";
		            if(w !='' && w != null ){
		            	wStr = "width="+w;
		            }
		            if(h !='' && h != null ){
		            	hStr = "height="+h;
		            }
		            $target.html('<img src="' + e.target.result + '" border="0" alt="" '+wStr+' '+hStr+' />');
		        }
		        reader.readAsDataURL(html.files[0]);
		    }
		}
		
		</script>
	</head>
	<body>
	<form id="frm" name="frm" method="post">
		<table>
			<tr>
				<th rowspan="3">대표이미지</th>
				<td rowspan="2">
					<div id="courseimageView" style="width:140px;height:80px;float:left;" class="required">
					<c:if test="${not empty detail.courseimage}"><img src="/manager/lms/common/imageView.do?file=${detail.courseimage}&mode=course" width="120" style="max-height:80px;"></c:if>
					</div>
				</td>
				<td>
					<input type="hidden" id="courseimagefile" name="courseimagefile" value="${detail.courseimage}" title="대표이미지" />
					<input type="file" id="courseimage" name="courseimage" onchange="getThumbnailPrivew(this,$('#courseimageView'), 120 , 80);" title="대표이미지"><br />
				</td>
			</tr>
		</table>
		<a href="javascript:;" id="insertBtn" class="btn_green">저장</a>
	</form>
	</body>
</html>
		