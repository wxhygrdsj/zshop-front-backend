<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>backend</title>
    <link rel="stylesheet"  href="${pageContext.request.contextPath}/css/bootstrap.css" />
    <link rel="stylesheet"  href="${pageContext.request.contextPath}/css/index.css" />
    <script src="${pageContext.request.contextPath}/js/jquery.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/js/userSetting.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap-paginator.js"></script>
    <script src="${pageContext.request.contextPath}/layer/layer.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/zshop.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrapValidator.min.css"/>
    <script src="${pageContext.request.contextPath}/js/bootstrapValidator.min.js"></script>

    <script>
        $(function () {
            var successMsg='${successMsg}';
            var errorMsg='${errorMsg}';

            if(successMsg!=''){
                layer.msg(
                    successMsg,
                    {
                        time:2000,
                        skin:'successMsg'
                    }
                );
            }
            if(errorMsg!=''){
                layer.msg(
                    errorMsg,
                    {
                        time:2000,
                        skin:'errorMsg'
                    }
                );
            }
            $('#pagination').bootstrapPaginator({
                //主版本号
                bootstrapMajorVersion:3,
                //当前页
                currentPage:${data.pageNum},
                //总页数
                totalPages:${data.pages},
                //点击分页条执行的请求url
               /* pageUrl:function (type,page,current) {
                    return '${pageContext.request.contextPath}/backend/sysuser/findAll?pageNum='+page;

                },*/
                onPageClicked:function(event, originalEvent, type, page){

                    //给隐藏表单域赋值
                    $('#pageNum').val(page);

                    //重新提交表单
                    $('#frmQuery').submit();
                },
                itemTexts: function (type, page, current) {
                    switch (type) {
                        case "first":
                            return "首页";
                        case "prev":
                            return "上一页";
                        case "next":
                            return "下一页";
                        case "last":
                            return "尾页";
                        case "page":
                            return page;
                    }
                }

            });

            //添加表单的初始化校验
            $('#frmAddSysuser').bootstrapValidator({
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',//成功后输出的图标
                    invalid: 'glyphicon glyphicon-remove',//失败后输出的图标
                    validating: 'glyphicon glyphicon-refresh'//长时间加载时输出的图标
                },
                fields:{
                    name:{
                        validators:{
                            notEmpty:{
                                message:'用户姓名不能为空'
                            }
                        }
                    },
                    loginName:{
                        validators:{
                            notEmpty:{
                                message:'登录账号不能为空'
                            },
                            remote:{
                                //校验该名称是否已经在数据库中存在
                                url:'${pageContext.request.contextPath}/backend/sysuser/checkName'
                            }
                        }
                    },
                    password:{
                        validators:{
                            notEmpty:{
                                message:'密码不能为空'
                            }
                        }
                    },
                    phone:{
                        validators:{
                            notEmpty:{
                                message:'电话号码不能为空'
                            }
                        }
                    },
                    email:{
                        validators:{
                            notEmpty:{
                                message:'邮箱不能为空'
                            },
                            emailAddress:{
                                message:'邮箱格式不正确'
                            }
                        }
                    },
                    roleId:{
                        validators:{
                            notEmpty:{
                                message:'请选择角色'
                            }
                        }
                    }

                }
            });


        });

        //添加用户
        function addUser() {
            //alert(1);
            //console.log($('#frmAddSysuser').serialize());
            //进行表单验证
            var bv=$('#frmAddSysuser').data('bootstrapValidator');
            bv.validate();
            //如果校验通过。执行post请求，完成添加动作
            if(bv.isValid()){

            $.post(
                '${pageContext.request.contextPath}/backend/sysuser/add',
                $('#frmAddSysuser').serialize(),
                function (result) {
                    if(result.status==1){
                        layer.msg(
                            result.message,
                            {
                                time:2000,
                                skin:'successMsg'
                            },
                            function () {
                                //返回列表页面
                                location.href='${pageContext.request.contextPath}/backend/sysuser/findAll?pageNum='+${data.pageNum};
                            }
                        );
                    }
                    else{
                        layer.msg(
                            result.message,
                            {
                                time:2000,
                                skin:'errorMsg'
                            }
                        );
                    }
                }

            );
            }
            else{

            }
        }

        //按条件查询
        function doQuery() {
            //将页面重置为1
            $('#pageNum').val(1);
            //提交表单
            $('#frmQuery').submit();
        }

        function showSysuser(id) {
            //alert(id);
            $.post('${pageContext.request.contextPath}/backend/sysuser/findById',
                {"id":id},function (result) {
                    console.log(result);
                    if(result.status==1){
                        //如果查询成功，将值直接写回修改窗口
                        $('#MargerStaffId').val(result.data.id);
                        $('#MargerStaffname').val(result.data.name);
                        $('#MargerPhone').val(result.data.phone);
                        $('#MargerAdrees').val(result.data.email);
                        $('#MargerLoginName').val(result.data.loginName);
                        //下拉列表的值
                        $('#MargerRole').val(result.data.role.id);
                    }
                });
        }

        //修改用户状态
        function  modifyStatus(id,btn) {
            $.post(
                '${pageContext.request.contextPath}/backend/sysuser/modifyStatus',
                {"id":id},
                function (result) {
                    if(result.status==1){
                      var $td= $(btn).parent().parent().children().eq(5);
                      if($td.text().trim()=='有效'){
                          $td.text('无效');
                          $(btn).val('启用').removeClass('btn-danger').addClass('btn-success');
                      }
                      else{
                          $td.text('有效');
                          $(btn).val('禁用').removeClass('btn-success').addClass('btn-danger');

                      }


                    }

                }
            );
        }

    </script>
</head>

<body>
<!-- 系统用户管理 -->
<div class="panel panel-default" id="adminSet">
    <div class="panel-heading">
        <h3 class="panel-title">系统用户管理</h3>
    </div>
    <div class="panel-body">
        <div class="showmargersearch">
            <form class="form-inline" id="frmQuery" action="${pageContext.request.contextPath}/backend/sysuser/findByParams" method="post">
                <input type="hidden" name="pageNum" id="pageNum" value="${data.pageNum}">
                <div class="form-group">
                    <label for="userName">姓名:</label>
                    <input type="text" class="form-control" id="userName" placeholder="请输入姓名" name="name" value="${sysuserParam.name}">
                </div>
                <div class="form-group">
                    <label for="loginName">帐号:</label>
                    <input type="text" class="form-control" id="loginName" placeholder="请输入帐号" name="loginName" value="${sysuserParam.loginName}">
                </div>
                <div class="form-group">
                    <label for="phone">电话:</label>
                    <input type="text" class="form-control" id="phone" placeholder="请输入电话" name="phone" value="${sysuserParam.phone}">
                </div>
                <div class="form-group">
                    <label for="role">角色</label>
                    <select class="form-control" name="roleId" id="role">
                        <option value="-1">全部</option>
                        <c:forEach items="${roles}" var="role">
                            <option value="${role.id}"
                                    <c:if test="${role.id==sysuserParam.roleId}">selected</c:if>
                            >${role.roleName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="form-group">
                    <label for="status">状态</label>
                    <select class="form-control" name="isValid" id="status">
                        <option value="-1">全部</option>
                        <option value="1" <c:if test="${sysuserParam.isValid==1}">selected</c:if>>
                            ---有效---
                        </option>
                        <option value="0" <c:if test="${sysuserParam.isValid==0}">selected</c:if>>
                            ---无效---
                        </option>
                    </select>
                </div>
                <input type="button" value="查询" class="btn btn-primary" id="doSearch" onclick="doQuery()">
            </form>
        </div>
        <br>
        <input type="button" value="添加系统用户" class="btn btn-primary" id="doAddManger">
        <div class="show-list text-center" style="position: relative; top: 10px;">
            <table class="table table-bordered table-hover" style='text-align: center'>
                <thead>
                <tr class="text-danger">
                    <th class="text-center">序号</th>
                    <th class="text-center">姓名</th>
                    <th class="text-center">帐号</th>
                    <th class="text-center">电话</th>
                    <th class="text-center">邮箱</th>
                    <th class="text-center">状态</th>
                    <th class="text-center">注册时间</th>
                    <th class="text-center">角色</th>
                    <th class="text-center">操作</th>
                </tr>
                </thead>
                <tbody id="tb">
                <c:forEach items="${data.list}" var="sysuser">
                <tr>
                    <td>${sysuser.id}</td>
                    <td>${sysuser.name}</td>
                    <td>${sysuser.loginName}</td>
                    <td>${sysuser.phone}</td>
                    <td>${sysuser.email}</td>
                    <td>
                        <c:if test="${sysuser.isValid==1}">有效</c:if>
                        <c:if test="${sysuser.isValid==0}">无效</c:if>
                    </td>
                    <td><fmt:formatDate value="${sysuser.createDate}" pattern="yyyy年MM月dd日"/></td>
                    <td>${sysuser.role.roleName}</td>
                    <td class="text-center">
                        <input type="button" class="btn btn-warning btn-sm doMangerModify" value="修改"
                               onclick="showSysuser(${sysuser.id},this)">
                        <c:if test="${sysuser.isValid==1}">
                        <input type="button" class="btn btn-danger btn-sm doMangerDisable" value="禁用"
                               onclick="modifyStatus(${sysuser.id},this)">
                        </c:if>
                        <c:if test="${sysuser.isValid==0}">
                            <input type="button" class="btn btn-success btn-sm doMangerDisable" value="启用"
                                   onclick="modifyStatus(${sysuser.id},this)">
                        </c:if>
                    </td>
                </tr>
                </c:forEach>
                </tbody>
            </table>
            <ul id="pagination"></ul>
        </div>
    </div>
</div>

<!-- 添加系统用户 start -->
<div class="modal fade" tabindex="-1" id="myMangerUser">
    <!-- 窗口声明 -->
    <div class="modal-dialog">
        <!-- 内容声明 -->
        <div class="modal-content">
            <form id="frmAddSysuser">
            <!-- 头部、主体、脚注 -->
            <div class="modal-header">
                <button class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">添加系统用户</h4>
            </div>
            <div class="modal-body text-center">
                <div class="row text-right">
                    <label for="marger-username" class="col-sm-4 control-label">用户姓名：</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" id="marger-username" name="name">
                    </div>
                </div>
                <br>
                <div class="row text-right">
                    <label for="marger-loginName" class="col-sm-4 control-label">登录帐号：</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" id="marger-loginName" name="loginName">
                    </div>
                </div>
                <br>
                <div class="row text-right">
                    <label for="marger-password" class="col-sm-4 control-label">登录密码：</label>
                    <div class="col-sm-4">
                        <input type="password" class="form-control" id="marger-password" name="password">
                    </div>
                </div>
                <br>
                <div class="row text-right">
                    <label for="marger-phone" class="col-sm-4 control-label">联系电话：</label>
                    <div class="col-sm-4">
                        <input type="text" class="form-control" id="marger-phone" name="phone">
                    </div>
                </div>
                <br>
                <div class="row text-right">
                    <label for="marger-adrees" class="col-sm-4 control-label">联系邮箱：</label>
                    <div class="col-sm-4">
                        <input type="email" class="form-control" id="marger-email" name="email">
                    </div>
                </div>
                <br>
                <div class="row text-right">
                    <label for="role" class="col-sm-4 control-label">角&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;色：</label>
                    <div class=" col-sm-4">
                        <select class="form-control" name="roleId">
                            <option value="">请选择</option>
                            <c:forEach items="${roles}" var="role">
                                <option value="${role.id}">${role.roleName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <br>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary" type="button" onclick="addUser()" >添加</button>
                <button class="btn btn-primary cancel" data-dismiss="modal" type="button">取消</button>
            </div>
            </form>
        </div>

    </div>
</div>
<!-- 添加系统用户 end -->

<!-- 修改系统用户 start -->
<div class="modal fade" tabindex="-1" id="myModal-Manger">
    <!-- 窗口声明 -->
    <div class="modal-dialog">
        <!-- 内容声明 -->
        <div class="modal-content">
            <form  action="${pageContext.request.contextPath}/backend/sysuser/modify"
                   class="form-horizontal" method="post" id="frmModifySysuser">
                <input type="hidden" name="pageNum" value="${data.pageNum}"/>
            <!-- 头部、主体、脚注 -->
                <div class="modal-header">
                    <button class="close" data-dismiss="modal">&times;</button>
                    <h4 class="modal-title">系统用户修改</h4>
                </div>
                <div class="modal-body text-center">
                    <div class="row text-right">
                        <label for="MargerStaffId" class="col-sm-4 control-label">用户编号：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" id="MargerStaffId" readonly="readonly" name="id">
                        </div>
                    </div>
                    <br>
                    <div class="row text-right">
                        <label for="MargerStaffname" class="col-sm-4 control-label">用户姓名：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" id="MargerStaffname" name="name">
                        </div>
                    </div>
                    <br>
                    <div class="row text-right">
                        <label for="MargerLoginName" class="col-sm-4 control-label">登录帐号：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" id="MargerLoginName" readonly="readonly">
                        </div>
                    </div>
                    <br>
                    <div class="row text-right">
                        <label for="MargerPhone" class="col-sm-4 control-label">联系电话：</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" id="MargerPhone" name="phone">
                        </div>
                    </div>
                    <br>
                    <div class="row text-right">
                        <label for="MargerAdrees" class="col-sm-4 control-label">联系邮箱：</label>
                        <div class="col-sm-4">
                            <input type="email" class="form-control" id="MargerAdrees" name="email">
                        </div>
                    </div>
                    <br>
                    <div class="row text-right">
                        <label for="MargerRole" class="col-sm-4 control-label">角&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;色：</label>
                        <div class=" col-sm-4">
                            <select class="form-control" id="MargerRole" name="roleId">
                                <option>请选择</option>
                                <c:forEach items="${roles}" var="role">
                                    <option value="${role.id}">${role.roleName}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                <br>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary doMargerModal" type="submit">修改</button>
                <button class="btn btn-primary cancel" data-dismiss="modal">取消</button>
            </div>
            </form>
        </div>

    </div>
</div>
<!-- 修改系统用户 end -->

</body>

</html>