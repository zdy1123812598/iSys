<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="../bootstrap/css/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="../bootstrap/css/uniform.css" />
<link rel="stylesheet" href="../bootstrap/css/select2.css" />
<link rel="stylesheet" href="../bootstrap/css/unicorn.main.css" />
<link rel="stylesheet" href="../bootstrap/css/unicorn.grey.css"
	class="skin-color" />
<link rel="stylesheet" href="../css/bootstrap-multiselect.css"
	class="skin-color" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<title>链接编辑</title>
</head>
<body style="background:#ffffff;">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="icon-align-justify"> </i>
						</span>
						<h5>链接添加</h5>
					</div>
					<div class="widget-content nopadding">
						<form class="form-horizontal" method="post" action="linkAdd.htm"
							name="linkForm" id="linkForm" novalidate="novalidate" />
						<div class="control-group">
							<label class="control-label"> 链接名称 </label>
							<div class="controls">
								<input type="text" id="linkname" name="linkname" />
							</div>
						</div>						
						<div class="control-group">
							<label class="control-label"> 上级目录 </label>
							<div class="controls">
								<select id="parentid" style="width: 50%">
								<option value="0">请选择节点</option>
								</select>
							</div>
						</div>								
						<div class="control-group">
							<label class="control-label"> 链接地址 </label>
							<div class="controls">
								<input type="text" id="linktext" name="linktext" readonly/>
							</div>
						</div>
						<div align="center" class="control-group">
							<input type="submit" value="确定" class="btn btn-primary"
								id="btnAdd" /> <input type="reset" value="重置"
								class="btn btn-primary" style="margin: 50px;" id="btnReset" />
						</div>						
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script src="../bootstrap/js/jquery.min.js">
		
	</script>
	<script src="../bootstrap/js/jquery.ui.custom.js">
		
	</script>
	<script src="../bootstrap/js/bootstrap.min.js">
		
	</script>
	<script src="../bootstrap/js/bootstrap-colorpicker.js">
		
	</script>
	<script src="../bootstrap/js/bootstrap-datepicker.js"></script>
	<script src="../bootstrap/js/jquery.uniform.js">
		
	</script>
	<script src="../bootstrap/js/select2.min.js">
		
	</script>
	<script src="../bootstrap/js/jquery.validate.js">
		
	</script>
	<script src="../bootstrap/js/unicorn.js">
		
	</script>
	<script src="../bootstrap/js/unicorn.form_validation.js">
		
	</script>
	<script src="../js/bootstrap-multiselect.js">
		
	</script>
	<script src="../js/jquery.md5.js">
		
	</script>
	<script type="text/javascript">
					$(function() {

						function refreshMultiSelect() {							
							$.ajax({
								type: "POST",
								url: "../link/linkListAll.htm",
								data : {creatorid : $('#unid', window.parent.document).val()},
								dataType: "json",
								success: function(data) {
									$.each(data, function(index, link) {                
						                  $("#parentid").append('<option value="' + link.linkid + '">' + link.linkname + '</option>');
						            });
								}
							});
						}

						refreshMultiSelect();
						
						$('#parentid').change(function(){ 
							if($(this).children('option:selected').val()==0){
								$('#linktext').val("");
								$('#linktext').attr("readonly","readonly");
							} else{
								$('#linktext').removeAttr("readonly");
							}
						});
						
			$("#linkForm").validate(
							{
								rules : {
									linkname : "required"
									
								},
								messages : {
									linkname : "链接名称不能为空!"
								},
								errorClass : "error",
								success : 'valid',
								unhighlight : function(element, errorClass,
										validClass) { //验证通过
									$(element).tooltip('destroy').removeClass(
											errorClass);
								},
								errorPlacement : function(label, element) {
									$(element).tooltip('destroy');
									/*必需*/
									$(element).attr('title', $(label).text())
											.tooltip('show');
								},
								submitHandler : function(form) {
									$.ajax({
												type : 'post',
												url : "linkAdd.htm",
												data : {
													linkname : $("#linkname").val(),
													linktext : $("#linktext").val(),
													parentid : $("#parentid").val(),
													creatorid : $('#unid', window.parent.document).val()
												},
												success : function(result) {
													if (result.code == 1) {
														self.location = "linkList.jsp";
													}
												},
												error : function() {
													alert('保存错误');
												}
											});
								}
							});
		});
	</script>
</body>

</html>