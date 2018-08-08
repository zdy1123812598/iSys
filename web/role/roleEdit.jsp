<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/include/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="../bootstrap/css/bootstrap-responsive.min.css" />
<link rel="stylesheet" href="../bootstrap/css/uniform.css" />
<link rel="stylesheet" href="../bootstrap/css/select2.css" />
<link rel="stylesheet" href="../bootstrap/css/unicorn.main.css" />
<link rel="stylesheet" href="../bootstrap/css/unicorn.grey.css" class="skin-color" />
<link rel="stylesheet" href="../css/bootstrap.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
</head>
<title>角色编辑</title>
</head>
<body style="background:#ffffff;">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">

				<div class="widget-box">
					<div class="widget-title">
						<span class="icon"> <i class="icon-align-justify"></i>
						</span>
						<h5>组织添加</h5>
					</div>
					<div class="widget-content nopadding">
						<form class="form-horizontal" method="post" action="#"
							name="roleForm" id="roleForm"
							novalidate="novalidate" />
							 <table class="table">
							<tr ><td colspan="2">
							 <div class="control-group">
								<div class="controls">
									<input type="hidden" name="roleid"
										id="roleid" value ="${roleid}") />
								</div>
							</div>
							<label class="control-label" style="margin:10px;">角色名称</label>
							<div class="controls">
								<input type="text" style="height:30px;"  name="rolename" id="rolename" value ="${rolename}"
									placeholder="请输入角色名称" />
							</td></tr>
							<tr><td>
							 <label class="control-label" style="margin:10px;">人员选择</label>
								<div id="treeview-checkable" class="controls"  style="margin: 0 0 0 175px;width:50%;"></div>
							</td><td>
							<label class="control-label" style="margin:10px;">权限列表</label>
								<div id="treeview-checkable1" class="controls" style="margin: 0 220px 0 180px;width:30%;"></div>
							</td></tr>
							<tr><td align="center" colspan="2">
							<div align="center" class="control-group">
							<input type="submit" value="确定" class="btn btn-primary"
								id="btnAdd" /> <input type="reset" value="重置"
								class="btn btn-primary" style="margin: 30px;" id="btnReset" />
						</div>
							</td></tr>
							</table> 
					<%-- 		 <div class="control-group">
								<div class="controls">
									<input type="hidden" name="roleid"
										id="roleid" value ="${roleid}") />
								</div>
							</div>
							<label class="control-label" style="margin:10px;">角色名称</label>
							<div class="controls">
								<input type="text" style="height:30px;" name="rolename" id="rolename" value ="${rolename}"
									placeholder="请输入角色名称" />
							</div>
							</br> <label class="control-label" style="margin:10px;">人员选择</label>
								<div id="treeview-checkable" class="controls" style="margin: 0 220px 0 175px;width:60%;"></div>	
								<label class="control-label" style="margin:10px;">权限列表</label>
								<div id="treeview-checkable1" class="controls" style="margin: 0 220px 0 175px;width:30%;"></div>						
							<div align="center" class="control-group">
								<input type="submit" value="确定" class="btn btn-primary"
									id="btnAdd" /> <input type="reset" value="重置"
									class="btn btn-primary" style="margin: 50px;"
									id="btnReset" />
							</div> --%> 
						</form>
					</div>
				</div>


			</div>
		</div>
	</div>

	<script src="../bootstrap/js/jquery.min.js"></script>
	<script src="../bootstrap/js/jquery.ui.custom.js"></script>
	<script src="../js/jquery-migrate-1.1.1.js"></script>
	<script src="../bootstrap/js/bootstrap.min.js"></script>
	<script src="../bootstrap/js/jquery.uniform.js"></script>
	<script src="../bootstrap/js/select2.min.js"></script>
	<script src="../bootstrap/js/jquery.validate.js"></script>
	<script src="../bootstrap/js/unicorn.js"></script>
	<script src="../bootstrap/js/unicorn.form_validation.js"></script>	
	<script src="../js/bootstrap-treeview.js"></script>
	<script type="text/javascript">		
		
		
		$(function() {			
			
			$("#roleForm").validate({
				rules : {
					rolename : "required"
				},
				messages : {
					rolename : "组织名称不能为空!"
				},
				errorClass : "error",
				success : 'valid',
				unhighlight : function(element, errorClass, validClass) { //验证通过
					$(element).tooltip('destroy').removeClass(errorClass);
				},
				errorPlacement : function(label, element) {
					$(element).tooltip('destroy'); /*必需*/
					$(element).attr('title', $(label).text()).tooltip('show');
				},
				submitHandler : function(form) {
					var arrCheckedAll=$('#treeview-checkable').treeview('getChecked', 0);
					var arrCheckedUsers = new Array();
					for(var i=0;i<arrCheckedAll.length;i++){
						if("user"==arrCheckedAll[i].type){
							arrCheckedUsers.push(arrCheckedAll[i].id);
						}
					}
					
					var arrCheckedPer=$('#treeview-checkable1').treeview('getChecked', 0);
					var arrCheckedPermission = new Array();
					for(var i=0;i<arrCheckedPer.length;i++){
						if("linkTwo"==arrCheckedPer[i].type){
							arrCheckedPermission.push(arrCheckedPer[i].id);
						}
					}
					
					$.ajax({
						type : 'post',
						url : "roleEdit.htm",
						data : {
							roleid: $("#roleid").val(),
							roleName : $("#rolename").val(),
							userids : arrCheckedUsers.join(","),
							linkids : arrCheckedPermission.join(","),
							creatorid : $('#unid', window.parent.document).val()
						},
						success : function(result) {
							if (result.code == 1) {
								self.location = "roleList.jsp";
							}
						}
					});
				}
			});			

			nodeCheckedSilent = false;
			function nodeChecked(event, node) {
				if (nodeCheckedSilent) {
					return;
				}
				nodeCheckedSilent = true;
				checkAllParent(node);
				checkAllSon(node);
				nodeCheckedSilent = false;
			}

			var nodeUncheckedSilent = false;
			function nodeUnchecked(event, node) {
				if (nodeUncheckedSilent)
					return;
				nodeUncheckedSilent = true;
				uncheckAllParent(node);
				uncheckAllSon(node);
				nodeUncheckedSilent = false;
			}

			//选中全部父节点  
			function checkAllParent(node) {
				$('#treeview-checkable').treeview('checkNode', node.nodeId, {
					silent : true
				});
				var parentNode = $('#treeview-checkable').treeview('getParent',
						node.nodeId);
				if (!("id" in parentNode)) {
					return;
				} else {
					checkAllParent(parentNode);
				}
			}
			//取消全部父节点  
			function uncheckAllParent(node) {
				$('#treeview-checkable').treeview('uncheckNode', node.nodeId, {
					silent : true
				});
				var siblings = $('#treeview-checkable').treeview('getSiblings',
						node.nodeId);
				var parentNode = $('#treeview-checkable').treeview('getParent',
						node.nodeId);
				if (!("id" in parentNode)) {
					return;
				}
				var isAllUnchecked = true; //是否全部没选中  
				for ( var i in siblings) {
					if (siblings[i].state.checked) {
						isAllUnchecked = false;
						break;
					}
				}
				if (isAllUnchecked) {
					uncheckAllParent(parentNode);
				}

			}

			//级联选中所有子节点  
			function checkAllSon(node) {
				$('#treeview-checkable').treeview('checkNode', node.nodeId, {
					silent : true
				});
				if (node.nodes != null && node.nodes.length > 0) {
					for ( var i in node.nodes) {
						checkAllSon(node.nodes[i]);
					}
				}
			}
			//级联取消所有子节点  
			function uncheckAllSon(node) {
				$('#treeview-checkable').treeview('uncheckNode', node.nodeId, {
					silent : true
				});
				if (node.nodes != null && node.nodes.length > 0) {
					for ( var i in node.nodes) {
						uncheckAllSon(node.nodes[i]);
					}
				}
			}
			
			var tree;
			$.ajax({
				type : 'post',
				url : '../user/userTreeGet.htm',
				data : {roleid:'${roleid}',creatorid : $('#unid', window.parent.document).val()},
				success : function(data) {
					init(data);
				}
			});

			function init(tree) {
				var $checkableTree = $('#treeview-checkable').treeview({
					data : tree,
					showIcon : false,
					showCheckbox : true,
					showTags : true,
					bootstrap2 : false,
					levels : 5,
					onNodeChecked : nodeChecked,
					onNodeUnchecked : nodeUnchecked
				});
			}
			
			nodeCheckedSilent1 = false;
			function nodeChecked1(event, node) {
				if (nodeCheckedSilent1) {
					return;
				}
				nodeCheckedSilent1 = true;
				checkAllParent1(node);
				checkAllSon1(node);
				nodeCheckedSilent1 = false;
			}

			var nodeUncheckedSilent1 = false;
			function nodeUnchecked1(event, node) {
				if (nodeUncheckedSilent1)
					return;
				nodeUncheckedSilent1 = true;
				uncheckAllParent1(node);
				uncheckAllSon1(node);
				nodeUncheckedSilent1 = false;
			}

			//选中全部父节点  
			function checkAllParent1(node) {
				$('#treeview-checkable1').treeview('checkNode', node.nodeId, {
					silent : true
				});
				var parentNode = $('#treeview-checkable1').treeview('getParent',
						node.nodeId);
				if (!("id" in parentNode)) {
					return;
				} else {
					checkAllParent1(parentNode);
				}
			}
			//取消全部父节点  
			function uncheckAllParent1(node) {
				$('#treeview-checkable1').treeview('uncheckNode', node.nodeId, {
					silent : true
				});
				var siblings = $('#treeview-checkable1').treeview('getSiblings',
						node.nodeId);
				var parentNode = $('#treeview-checkable1').treeview('getParent',
						node.nodeId);
				if (!("id" in parentNode)) {
					return;
				}
				var isAllUnchecked = true; //是否全部没选中  
				for ( var i in siblings) {
					if (siblings[i].state.checked) {
						isAllUnchecked = false;
						break;
					}
				}
				if (isAllUnchecked) {
					uncheckAllParent1(parentNode);
				}

			}

			//级联选中所有子节点  
			function checkAllSon1(node) {
				$('#treeview-checkable1').treeview('checkNode', node.nodeId, {
					silent : true
				});
				if (node.nodes != null && node.nodes.length > 0) {
					for ( var i in node.nodes) {
						checkAllSon1(node.nodes[i]);
					}
				}
			}
			//级联取消所有子节点  
			function uncheckAllSon1(node) {
				$('#treeview-checkable1').treeview('uncheckNode', node.nodeId, {
					silent : true
				});
				if (node.nodes != null && node.nodes.length > 0) {
					for ( var i in node.nodes) {
						uncheckAllSon1(node.nodes[i]);
					}
				}
			}
			var tree1;
			$.ajax({
				type : 'post',
				url : '../role/linkTreeGet.htm',
				data : {roleid : '${roleid}',creatorid : $('#unid', window.parent.document).val()},
				success : function(data) {
					inite(data);
				}
			});

			function inite(tree1) {
				var $checkableTree = $('#treeview-checkable1').treeview({
					data : tree1,
					showIcon : false,
					showCheckbox : true,
					showTags : true,
					bootstrap2 : false,
					levels : 5,
					onNodeChecked : nodeChecked1,
					onNodeUnchecked : nodeUnchecked1
				});
			}
		})
	</script>
</body>
</html>