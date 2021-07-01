<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta http-equiv="X-UA-Compatible" content="ie=edge" />
    <title>我的购物车</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
    <script src="${pageContext.request.contextPath}/layer/layer.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/zshop.css"/>
    <script>
        
        $(function () {
            //使用ajax技术修改单个商品的数量
            $('.text1234').change(function () {
                //alert(1);
                var quantityVal=this.value;
                var reg=/^\d+$/g;
                var quantity=1;
                var flag=false;
                //console.log(reg.test(quantityVal));
                if(reg.test(quantityVal)){
                    quantity=parseInt(quantityVal);
                    if(quantityVal>0){
                        flag=true;

                    }
                }
                if(!flag){
                    alert('输入的商品数量必须大于0');
                    //将该最近的一次正确值写回文本
                    $(this).val($(this).attr('class')[0]);
                    return;
                }
                //重新将该数据写回class属性
                //单击了"确认"按钮
                if(!confirm('确定要修改该数量吗？')){
                    //将该值写回回本
                    $(this).val($(this).attr('class'));
                    return;
                }
                //单击了"取消"按钮
                else{
                    //将值重新写回class属性
                    $(this).attr('class',$(this).val());
                }
                //使用ajax引擎访问数据库，更新商品总价和总计价格
                //1：请求地址
                var url='${pageContext.request.contextPath}/front/product/updateItemQuantity'
                //2：请求参数
                var idVal=$.trim(this.name);
                var args={"id":idVal,"quantity":quantity,"time":new Date()};

                var item_count=$(this).parent().parent().find('td').eq(1).html();

                //3：执行updateItemQuantity方法
                $.post(url,args,function (data) {
                    //console.log(data);
                    //4：传回json数据：itemMoney:XX,totalMoney:YY
                    //5：更新页面中的itemMoney,totalMoney
                    var itemMoney=data.itemMoney;
                    var totalMoney = data.totalMoney;
                    $('#itemMoney_'+item_count).html(itemMoney);
                    $('#totalMoney').html(totalMoney);


                });






            });

        });
        //继续购物
        function shopping() {
            location.href='${pageContext.request.contextPath}/front/product/main';
        }

        //显示删除模态框
        function showDelModal(id) {

            //将id存入隐藏表单域
            $('#deleteItemId').val(id);
            //显示该模态框
            $('#deleteItemModal').modal('show');

        }

        //结算
        function settle(){
            location.href='${pageContext.request.contextPath}/front/product/main';
        }

        //删除商品明细
        function delItem() {
            $.post(
                '${pageContext.request.contextPath}/front/product/removeItemById',
                {"id":$('#deleteItemId').val()},
                function (result) {
                    if(result.status==1){
                        layer.msg('移除成功',
                            {
                                time:2000,
                                skin:'successMsg'
                            },function () {
                                //刷新页面
                                //location.href='${pageContext.request.contextPath}/front/product/toCart';
                                //局部刷新
                                $('#'+$('#deleteItemId').val()).remove();
                                //重新设置总价
                                $('#totalMoney').html(result.data);


                            });
                    }
                    else{
                        layer.msg(
                            result.message,
                            {
                                time:2000,
                                skin:'errorMsg'
                            },function () {
                                //刷新页面
                                //location.href='${pageContext.request.contextPath}/front/product/toCart';
                                //局部刷新
                                $('#'+$('#deleteItemId').val()).remove();
                                //重新设置总价
                                $('#totalMoney').html(0);

                            }
                        );
                    }
                }
            );


        }

    </script>
</head>

<body>
<div class="navbar navbar-default clear-bottom">
    <div class="container">
        <%request.setAttribute("index",2);%>
        <jsp:include page="top.jsp"/>
    </div>
</div>
<!-- content start -->
<div class="container">
    <div class="row">
        <div class="col-xs-12">
            <div class="page-header" style="margin-bottom: 0px;">
                <h3>我的购物车</h3>
            </div>
        </div>
    </div>
    <table class="table table-hover table-striped table-bordered">
        <tr>
            <th>
                <input type="checkbox" id="all">
            </th>
            <th>序号</th>
            <th>商品名称</th>
            <th>商品图片</th>
            <th>商品数量</th>
            <th>商品单价</th>

            <th>商品总价</th>
            <th>操作</th>
        </tr>
        <c:forEach items="${sessionScope.shoppingCart.items}" var="item" varStatus="s">
        <tr id="${item.product.id}">
            <td>
                <input type="checkbox">
            </td>
            <td>${s.count}</td>
            <td>${item.product.name}</td>
            <td> <img src="${pageContext.request.contextPath}/front/product/showPic?image=${item.product.image}" alt="" width="60" height="60"></td>
            <td>
                <input type="text" value="${item.quantity}" size="5" class="${item.quantity} text1234" name="${item.product.id}">
            </td>
            <td>${item.product.price}</td>
            <td id="itemMoney_${s.count}"><fmt:formatNumber value="${item.itemMoney}" pattern="#.##"/></td>
            <td>
                <button class="btn btn-success" type="button"> <span class="glyphicon glyphicon-edit" aria-hidden="true"></span>修改</button>
                <button class="btn btn-danger" type="button" onclick="showDelModal(${item.product.id})">
                    <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
                </button>
            </td>
        </tr>
        </c:forEach>
        <tr>
            <td colspan="8" align="right">
                <button class="btn btn-warning margin-right-15" type="button"> 删除选中项</button>
                <button class="btn btn-warning  margin-right-15" type="button"> 清空购物车</button>
                <button class="btn btn-warning margin-right-15" type="button" onclick="shopping()"> 继续购物</button>
                <button class="btn btn-warning margin-right-15" type="button" onclick="settle()"> 结算</button>
<%--                <a href="">--%>
<%--                    <button class="btn btn-warning " type="button"> 结算</button>--%>
<%--                </a>--%>
            </td>
        </tr>
        <tr>
            <td colspan="8" align="right" class="foot-msg">
                总计： <b><span id="totalMoney">${shoppingCart.totalMoney}</span> </b>元
            </td>
        </tr>
    </table>
</div>
<!-- content end-->
<!-- footers start -->
<div class="footers">
    版权所有：中兴软件技术
</div>
<!-- footers end -->

<!-- 删除购物明细 start -->
<div class="modal fade" tabindex="-1" id="deleteItemModal">
    <input type="hidden" id="deleteItemId"/>
    <!-- 窗口声明 -->
    <div class="modal-dialog">
        <!-- 内容声明 -->
        <div class="modal-content">
            <!-- 头部、主体、脚注 -->
            <div class="modal-header">
                <button class="close" data-dismiss="modal">&times;</button>
                <h4 class="modal-title">提示消息</h4>
            </div>
            <div class="modal-body text-left">
                <h4>确认要删除该商品明细吗？</h4>
            </div>


            <div class="modal-footer">
                <button class="btn btn-primary" onclick="delItem()" data-dismiss="modal">确定</button>
                <button class="btn btn-primary cancel" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>
<!-- 删除购物明细 end -->

</body>

</html>