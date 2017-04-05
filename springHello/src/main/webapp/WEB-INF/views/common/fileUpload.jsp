<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html lang="ko">
	<head>	
    <!-- page common -->
    <meta charset="utf-8">
    <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate">
    <meta http-equiv="Pragma" content="no-cache">
    <META http-equiv="Expires" content="0"> 
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <!-- //page common -->

    <meta name="keywords">
<meta name="description">
<meta content="index,follow" name="robots">
<meta content="True" name="HandheldFriendly">
<meta content="320" name="MobileOptimized">
<meta content="telephone=no" name="format-detection">	
	
		<title>파일업로드</title>
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
		function downLoad(mode, file, name){
			$("#mode").val(mode);
			$("#file").val(file);
			$("#name").val(name);
			$("#down").submit();
		}
		function downLoadHref(mode, file, name){
			$("#mode").val(mode);
			$("#file").val(file);
			$("#name").val(name);
			var hrefUrl = $("#down").attr("action")+"?"+$("#down").serialize();
			alert(hrefUrl)
			location.href = hrefUrl;
			//$("#down").submit();
		}
		
        function fn_openBoardUpdate(){
            var idx = "${map.IDX}";
            var comSubmit = new ComSubmit();
            comSubmit.setUrl("<c:url value='/sample/openBoardUpdate.do' />");
            comSubmit.addParam("IDX", idx);
            comSubmit.submit();
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
	<form id="down" name="down" action="/common/fileDownload.do">
		<input type="hidden" name="mode" id="mode">
		<input type="hidden" name="file" id="file">
		<input type="hidden" name="name" id="name">
	</form>
	<c:set var="name" value="과업지시서-'''20160825.hwp" />

	<p><a href="javascript:;" onclick='downLoad("course","824da37b10f44246939154439560a9c7.hwp","<c:out value="${name }" />")'>${name }</a></p>
	<p><a href="javascript:;" onclick='downLoadHref("course","824da37b10f44246939154439560a9c7.hwp","<c:out value="${name }" />")'>${name }</a></p>
	
	<p><img src="/upload/course/한글.gif"></p>
	</body>
</html>
		