<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <script src="${pageContext.request.contextPath}/js/template.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrapValidator.min.css"/>
    <script src="${pageContext.request.contextPath}/js/bootstrapValidator.min.js"></script>
    <script src="${pageContext.request.contextPath}/layer/layer.js"></script>
    <script>
        function checkForm(){
            var curIndex=${requestScope.index};
            $('ul.nav li').each(function (i) {
                $(this).removeClass('active');
                if(curIndex==i){
                    $(this).addClass('active');
                }
            });
            $('#frmChange').bootstrapValidator({
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',//成功后输出的图标
                    invalid: 'glyphicon glyphicon-remove',//失败后输出的图标
                    validating: 'glyphicon glyphicon-refresh'//长时间加载时输出的图标
                },
                fields:{
                    oldpsw:{
                        validators: {
                            notEmpty: {
                                message: '不能为空'
                            },
                            stringLength: {
                                min: 4,
                                message: '至少为4位'
                            },
                            remote:{
                                //校验该名称是否已经在数据库中存在
                                url:'${pageContext.request.contextPath}/front/customer/checkPsw'
                            }
                        }
                    },
                    newpsw:{
                        validators: {
                            notEmpty: {
                                message: '不能为空'
                            },
                            stringLength: {
                                min: 4,
                                message: '密码至少为4位'
                            },

                        }
                    },
                    reppsw:{
                        validators:{
                            notEmpty:{
                                message:'密码不能为空'
                            },
                            identical:{
                                field: 'newpsw',
                                message:'两次输入密码不一致'
                            }
                        }
                    }
                }
            })
            $('#frmloginin').bootstrapValidator({
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',//成功后输出的图标
                    invalid: 'glyphicon glyphicon-remove',//失败后输出的图标
                    validating: 'glyphicon glyphicon-refresh'//长时间加载时输出的图标
                },
                fields:{
                    name: {
                        validators: {
                            notEmpty: {
                                message: '用户名不能为空'
                            },
                            stringLength:{
                                min:2,
                                max:12,
                                message:'用户名长度必须在2-12位之间'
                            }
                        }
                    },
                    loginName:{
                        validators:{
                            notEmpty:{
                                message:'登录名不能为空'
                            },
                            stringLength:{
                                min:4,
                                max:12,
                                message:'登录名长度必须在4-12位之间'
                            },
                            remote:{
                                //校验该名称是否已经在数据库中存在
                                url:'${pageContext.request.contextPath}/front/customer/checkName'
                            }
                        }
                    },
                    password:{
                        validators: {
                            notEmpty: {
                                message:'密码不能为空'
                            },
                            stringLength: {
                                min:4,
                                message:'密码至少为4位'
                            },
                            different:{
                                field:'loginName',
                                message:'密码不能和登录名相同'
                            }

                        }
                    },
                    confirmpassword:{
                        validators:{
                            notEmpty:{
                                message:'密码不能为空'
                            },
                            identical:{
                                field: 'password',
                                message:'两次输入密码不一致'
                            }
                        }
                    },
                    phone:{
                        validators:{
                            notEmpty:{
                                message:'电话不能为空'
                            },
                            regexp:{
                                regexp: /^1\d{10}$/,
                                message:'手机号格式错误:1开头,11位'
                            }
                        }
                    },
                    address:{
                        validators:{
                            notEmpty:{
                                message:'地址不能为空'
                            }
                        }
                    }

                }
            });
        }
        $(function (){
            checkForm();
        })
        $(function (){
            let customer='${customer.name}'
            if(customer=='') {
                $("#myorders").removeAttr("href");
                $("#myCart").removeAttr("href");
                $("#myCenter").removeAttr("href");
                //$('#loginModal').modal('show');
            }else {
                $("#myorders").attr("href","${pageContext.request.contextPath}/front/product/toMyOrders?id=${customer.id}");
                $("#myCart").attr("href","${pageContext.request.contextPath}/front/product/toCart");
                $("#myCenter").attr("href","${pageContext.request.contextPath}/front/product/toCenter");
            }
        })
        function resetForm(formName){
            document.getElementById(formName).reset();
            $("#"+formName).data('bootstrapValidator').destroy();
            $("#"+formName).data('bootstrapValidator',null);
            checkForm();
        }
        function loginByAccount() {
            //alert(1);
            $.post(
                '${pageContext.request.contextPath}/front/customer/loginByAccount',
                $('#frmLoginByAccount').serialize(),
                function (result) {
                    //登录成功
                    if(result.status==1){
                        //返回到主页面(全局刷新)
                        //location.href='${pageContext.request.contextPath}/front/product/main';
                        //通过template引擎获取整个html片段
                        history.go(0);
                        var content=template('welcome',result.data);
                        //登录窗口关闭
                        $('#loginModal').modal('hide');
                        //在对应节点加上这个片段
                        $('#navInfo').html(content);
                    }

                    //登录失败
                    else{
                        //给登录框一个提示信息
                        $('#loginInfo').html(result.message);
                    }
                }
            );

        }

        //退出
        function loginOut() {
            $.post(
                '${pageContext.request.contextPath}/front/customer/loginOut',
                function (result) {
                    if(result.status==1){
                        //刷新整体页面
                        location.href='${pageContext.request.contextPath}/front/product/main';
                        var content=template('loginOut');
                        $('#navInfo').html(content);
                    }
                }
            );
        }


        //修改密码
        function updatePsw(){
            var bv=$('#frmChange').data('bootstrapValidator');
            bv.validate();
            if(bv.isValid()){
                $.post(
                    '${pageContext.request.contextPath}/front/customer/updatePsw',
                    $('#frmChange').serialize(),
                    function (result) {
                        if(result.status==1){
                            layer.msg(
                                result.message,
                                {
                                    time:2000,
                                    skin:'successMsg'
                                },
                                function () {
                                    history.go(0);

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
                )
            }else {

            }
        }

        //注册
        function addUser() {
            //进行表单验证
            var bv=$('#frmloginin').data('bootstrapValidator');
            bv.validate();
            //console.log($('#frmloginin').serialize());
            //如果校验通过。执行post请求，完成添加动作
            if(bv.isValid()){
                $.post(
                    '${pageContext.request.contextPath}/front/customer/add',
                    $('#frmloginin').serialize(),
                    function (result) {
                        if(result.status==1){
                            layer.msg(
                                result.message,
                                {
                                    time:2000,
                                    skin:'successMsg'
                                },function () {
                                    history.go(0);

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

    </script>
    <script id="welcome" type="text/html">
        <li class="userName">
            您好：{{name}}！
        </li>
        <li class="dropdown">
            <a href="#" class="dropdown-toggle user-active" data-toggle="dropdown" role="button">
                <img class="img-circle" src="${pageContext.request.contextPath}/front/customer/getPic?image={{image}}" height="30" />
                <span class="caret"></span>
            </a>
            <ul class="dropdown-menu">
                <li>
                    <a href="#" data-toggle="modal" data-target="#modifyPasswordModal">
                        <i class="glyphicon glyphicon-cog"></i>修改密码
                    </a>
                </li>
                <li>
                    <a href="#" onclick="loginOut()">
                        <i class="glyphicon glyphicon-off"></i> 退出
                    </a>
                </li>
            </ul>
        </li>

    </script>
    <script id="loginOut" type="text/html">
        <li>
        <a href="#" data-toggle="modal" data-target="#loginModal">登陆</a>
        </li>
        <li>
        <a href="#" data-toggle="modal" data-target="#registModal">注册</a>
        </li>
    </script>
</head>
<body>
<!-- navbar start -->
<div class="navbar navbar-default clear-bottom">
    <div class="container">

        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="active">
                    <a href="${pageContext.request.contextPath}/front/product/main">商城主页</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/front/product/toMyOrders?id=${customer.id}" id="myorders">我的订单</a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/front/product/toCart" id="myCart">购物车</a>
                </li>
                <li class="dropdown">
                    <a href="${pageContext.request.contextPath}/front/product/toCenter" id="myCenter">会员中心</a>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right" id="navInfo">
                <c:choose>
                    <c:when test="${empty customer}">


                        <li>
                            <a href="#" data-toggle="modal" data-target="#loginModal">登陆</a>
                        </li>
                        <li>
                            <a href="#" data-toggle="modal" data-target="#registModal">注册</a>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li class="userName">
                            您好：${customer.name}！
                        </li>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle user-active" data-toggle="dropdown" role="button">
                                <c:if test="${customer.image==''}">
                                <img class="img-circle" src="${pageContext.request.contextPath}/images/user.jpeg" height="30" />
                                </c:if>
                                <c:if test="${customer.image!=null}">
                                    <img class="img-circle" src="${pageContext.request.contextPath}/front/customer/getPic?image=${customer.image}" height="30" />
                                </c:if>
                                <span class="caret"></span>
                            </a>
                            <ul class="dropdown-menu">
                                <li>
                                    <a href="#" data-toggle="modal" data-target="#modifyPasswordModal">
                                        <i class="glyphicon glyphicon-cog"></i>修改密码
                                    </a>
                                </li>
                                <li>
                                    <a href="#" onclick="loginOut()">
                                        <i class="glyphicon glyphicon-off"></i> 退出
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</div>
<!-- navbar end -->

<!-- 修改密码模态框 start -->
<div class="modal fade" id="modifyPasswordModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">修改密码</h4>
            </div>
            <form action="" class="form-horizontal" method="post" id="frmChange">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">原密码：</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="password" name="oldpsw">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">新密码：</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="password" name="newpsw">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">重复密码：</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="password" name="reppsw">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-warning" data-dismiss="modal" aria-label="Close">关&nbsp;&nbsp;闭</button>
                    <button type="button" class="btn btn-warning" onclick="resetForm('frmChange')">重&nbsp;&nbsp;置</button>
                    <button type="button" class="btn btn-warning"  onclick="updatePsw()">修&nbsp;&nbsp;改</button>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- 修改密码模态框 end -->

<!-- 登录模态框 start  -->
<div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <!-- 用户名密码登陆 start -->
        <div class="modal-content" id="login-account">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">用户名密码登录</h4><small class="text-danger" id="loginInfo"></small>
            </div>
            <form action="" class="form-horizontal" method="post" id="frmLoginByAccount">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">用户名：</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" placeholder="请输入用户名" name="loginName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">密&nbsp;&nbsp;&nbsp;&nbsp;码：</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="password" placeholder="请输入密码" name="password">
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="text-align: center">
                    <a class="btn-link">忘记密码？</a> &nbsp;
                    <button type="button" class="btn btn-warning" data-dismiss="modal" aria-label="Close">关&nbsp;&nbsp;闭</button>
                    <button type="button" class="btn btn-warning" onclick="loginByAccount()">登&nbsp;&nbsp;陆</button> &nbsp;&nbsp;
                    <a class="btn-link" id="btn-sms-back">短信快捷登录</a>
                </div>
            </form>
        </div>
        <!-- 用户名密码登陆 end -->
        <!-- 短信快捷登陆 start -->
        <div class="modal-content" id="login-sms" style="display: none;">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">短信快捷登陆</h4>
            </div>
            <form class="form-horizontal" method="post">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">手机号：</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" placeholder="请输入手机号">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">验证码：</label>
                        <div class="col-sm-4">
                            <input class="form-control" type="text" placeholder="请输入验证码">
                        </div>
                        <div class="col-sm-2">
                            <button class="pass-item-timer" type="button">发送验证码</button>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" style="text-align: center">
                    <a class="btn-link">忘记密码？</a> &nbsp;
                    <button type="button" class="btn btn-warning" data-dismiss="modal" aria-label="Close">关&nbsp;&nbsp;闭</button>
                    <button type="submit" class="btn btn-warning">登&nbsp;&nbsp;陆</button> &nbsp;&nbsp;
                    <a class="btn-link" id="btn-account-back">用户名密码登录</a>
                </div>
            </form>
        </div>
        <!-- 短信快捷登陆 end -->
    </div>
</div>
<!-- 登录模态框 end  -->

<!-- 注册模态框 start -->
<div class="modal fade" id="registModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel">会员注册</h4>
            </div>
            <form action="" class="form-horizontal" method="post" id="frmloginin">
                <div class="modal-body">
                    <div class="form-group">
                        <label class="col-sm-3 control-label">用户姓名:</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" name="name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">登陆账号:</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" name="loginName">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">登录密码:</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="password" name="password">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">再次输入登录密码:</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="password" name="confirmpassword">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">联系电话:</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" name="phone">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-3 control-label">联系地址:</label>
                        <div class="col-sm-6">
                            <input class="form-control" type="text" name="address">
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-warning" data-dismiss="modal" aria-label="Close">关&nbsp;&nbsp;闭</button>
                    <button type="button"  id="resetfrm" class="btn btn-warning" onclick="resetForm('frmloginin')">重&nbsp;&nbsp;置</button>
                    <button type="button" class="btn btn-warning" onclick="addUser()">注&nbsp;&nbsp;册</button>
                </div>
            </form>
        </div>
    </div>
</div>
<!-- 注册模态框 end -->

</body>
</html>
