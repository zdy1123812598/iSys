<%@page import="java.util.ArrayList"%>
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
		<link rel="stylesheet" href="../bootstrap/bootstrap-table/bootstrap-table.min.css" />
<meta http-equiv="Content-Type" content="text/html; charset=charset=UTF-8">
<title>人员列表</title>
</head>
<body style="background:#ffffff;">
<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="widget-box">
					<div class="widget-title">
						<h5>人员信息列表</h5>
					</div>
					<div id="toolbar" class="btn-group">
						<button id="userAdd" type="button" class="btn btn-default">
							<span class="glyphicon glyphicon-plus" aria-hidden="true">
							</span> 新增
						</button>
						<!--  <button id="userDelete" type="button"
							class="btn btn-default">
							<span class="glyphicon glyphicon-remove" aria-hidden="true">
							</span> 删除
						</button> -->
					</div>
					<table id="table"
						class="table table-bordered table-striped with-check">
					</table>
				</div>
			</div>
		</div>
	</div>
	</div>
	</div>
	</div>
	
	<script src="../bootstrap/js/jquery.min.js">
		
	</script>
	<script src="../js/jquery-1.11.1.min.js">
		
	</script>
	<script src="../js/jquery-migrate-1.1.1.js">
		
	</script>
	<script src="../bootstrap/js/jquery.ui.custom.js">
		
	</script>
	<script src="../bootstrap/js/bootstrap.min.js">
		
	</script>
	<script src="../bootstrap/js/jquery.uniform.js">
		
	</script>
	<script src="../bootstrap/js/select2.min.js">
		
	</script>
	<script src="../bootstrap/js/jquery.dataTables.min.js">
		
	</script>
	<script src="../bootstrap/bootstrap-table/bootstrap-table.min.js">
		
	</script>
	<script
		src="../bootstrap/bootstrap-table/locale/bootstrap-table-zh-CN.min.js">
		
	</script>
	<script type="text/javascript">
		$(function() {
		var url = "userListPagination.htm";
		var columns = [{
			field : "state",
			checkbox : true,
			align : 'center',
			formatter : function(value, row, index) {
				if (row.state == true)
					return {
						checked : true
					};
				return value;
			}
		}, {
			field: 'userid',
			title: '编号',
			align: 'center'
		},
		{
			field: 'username',
			title: '姓名',
			align: 'center'
		},
		{
			field: 'logonname',
			title: '登录名',
			align: 'center'
		},
		{
			field: 'gender',
			title: '性别',
			align: 'center',
			formatter : function(value, row, index) {
				if (1==value){
					return "男";
				}else{
					return "女";
				}
			}
		},
		{
			field: 'telephone',
			title: '电话',
			align: 'center'
		
		},
		{
			field: 'status',
			title: '状态',
			align: 'center',
			formatter : function(value, row, index) {
				if (1==value){
					return "<span class='label label-inverse'>启用 </span>";
				}else{
					return "<span class='label'>禁用 </span>";
				}
			}
		}];
		
		$("#userAdd").click(function() {
			self.location = "userAdd.jsp";
		});
		
        var onDblClickRow = function(item, $element) {
			self.location = "userGet.htm?userid="
					+ item.userid;
		};
		
		$("#userDelete").click(function() {
			var currentTableData = $('#table').bootstrapTable('getData');
			var arrStr = [];
			for(i=0;i<currentTableData.length;i++){
				if(currentTableData[i].state==true){
					arrStr.push(currentTableData[i].userid);
				}
			}
			if(arrStr.length>0){
				$.post("userDelete.htm",{ids:arrStr.join(","),creatorid:$('#unid', window.parent.document).val()},function(result){
					if(result.code>0){
						initTable();
						//$('#table').bootstrapTable('refresh');
			        }
			    });
			}
		});
		
		var queryParams = function(params) {
			return {
				page: Math.ceil(params.offset / params.limit) + 1,
				rows: params.limit,
				search: params.search
			};
		}
		
		 
		//调用函数，初始化表格  
		initTable();
		$("#search").bind("click", initTable);

		function initTable() {
			$('#table').bootstrapTable('destroy');
			$('#table').bootstrapTable({
				columns: columns,				
				url: url,   //请求后台的URL（*）
				queryParams: queryParams, //传递参数（*）
				method: 'post',  //请求方式（*）
				toolbar: '#toolbar',  //工具按钮用哪个容器
				cache: false,  //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
				showRefresh: false,  //是否显示刷新按钮
				pagination: true,    //是否显示分页（*）
				singleSelect: false,
				search: true,  //显示搜索框
				contentType: "application/x-www-form-urlencoded",
				pageSize: 10,  //每页的记录行数（*）
				pageNumber: 1, //初始化加载第一页，默认第一页
				pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
				showColumns: false,  //不显示下拉框（选择显示的列	
				sidePagination: "server", //服务端处理分页
				onLoadSuccess: function(data) {
				},
				onDblClickRow : onDblClickRow
			});
		}
		
		
	});	
	 /* function Edit(id){
			self.location = "userEdit.htm?id="+id;
		}   */
	</script>
</body>
</html>