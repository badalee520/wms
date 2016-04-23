$(document)
		.ready(
				$(function($) {

					$(".update").click(
							function() {
								addRoleBtn();
								$(".errMsg").hide();
								$("#enter").addClass("disabled");
								var id = $(this).parent().siblings(".id") .text();
								var nickName = $(this).parent().siblings( ".nickName").text();
								var name = $(this).parent().siblings(".name") .text();

								$(":input#nickName").val(nickName);
								$(":input#name").val(name);
								//$(".idForUpdate").remove();
								//$("#addForm").append( "<input class='idForUpdate' type='hidden' name='id' value='" + id + "'>");
								$(".idForUpdate").val(id);
								$("#addForm").attr("action","../user/modify");
								$("#enter").removeClass( "disabled");
								$("#enter").attr("id", "update");
								

							});

					// 当用户点击添加的时候异步加载权限列表
					$("#add").click(function() {
						
						$(".errMsg").hide();

						$("#enter").addClass("disabled");
						$("#update").attr("id", "enter");
						$("#addForm").attr("action","../user/add");

						$(":input#name").val("");

						//$(".idForUpdate").remove();

						addRoleBtn();
					});

					
					function addRoleBtn() {// 异步加载权限 并增加按钮到面板
						$.ajax({
									url : "../role/getAllRoles",
									type : 'post',
									data : "",
									dataType : 'html',
									success : function(data, status) {
										if (status == "success") {
											var map = jQuery.parseJSON(data);
											var roleBtns = $(".btn-group.roles") .empty();
											for ( var role in map) {// 循环权限列表 // 并添加权限按钮到页面
												roleBtns .append("<label class='btn alert-info roleIds' id='" + role + "'> " + map[role] + "</label>");
											}

										}
									},
									error : function(xhr, textStatus,
											errorThrown) {
										alert("权限获取错误 请稍后再试" + errorThrown);
									}
								});
					}

					$("#enter").click(// 异步验证添加用户的数据是否正确
					function() {
						var roleIds = "";
						$(".btn.alert-info.roleIds.active").each(function() {
							roleIds += $(this).attr('id') + ",";
						});
						roleIds = roleIds.substring(0, roleIds.length - 1);
						$("#roleIds").val(roleIds);

					});
					
					$(":input.form-control").focusout(
							// 异步验证添加用户的数据是否正确
							function() {
								
								var id=$('.idForUpdate').val();
								if(id==undefined || null==id || ""==id)id=0;
								var nickName = $('#nickName').val();
								var name = $('#name').val();
								var pass = $('#pass').val();
								var pass2 = $('#pass2').val();
								var email = $('#email').val();
								$.ajax({
									url : "../user/checkAdd",
									type : 'post',
									data : "nickName=" + nickName + "&name=" + name + "&pass=" + pass
											+ "&pass2=" + pass2 + "&email=" + email+"&id="+id,
									dataType : 'html',
									success : function(data, status) {
										if (status == "success") {
											var map = jQuery.parseJSON(data);

											if ("" != map['errMsg'])
												$(".errMsg").show("fast").text( map['errMsg']);
											else {
												$(".errMsg").hide();
												$("#enter").removeClass( "disabled");
												$("#update").removeClass( "disabled");
											}
										}
									}, error : function(xhr, textStatus, errorThrown) {
										alert("添加失败" + errorThrown);
									}
								});

							});
				}));