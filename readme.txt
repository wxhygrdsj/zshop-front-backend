1:二进制表单的特征
method="post" enctype="multipart/form-data"
其表单元素的值不能简单的通过request.getParameter方法获取,springMVC可以通过简单的注入获取
表单元素都需要name值

数据层的封装
页面--->controller(vo)----->service(dto)------>dao(pojo)
vo:value object值对象,用于将表单中值注入到对象中

dto:data transport object,在service层，用于处理将vo对象装换成pojo能处理的对象

pojo:plain object and java object,实体bean,用于将对象直接交给mybaits,操作数据库


springmvc的数据绑定一定要注意controller方法中的参数名称一定要和页面表单元素的名称相一致，并且要有对应的值，否则会报
 The request sent by the client was syntactically incorrect.异常

web.xml中配置的字符编码过滤器，只支持post请求
get请求如何解决中文乱码问题
1:如果是tomcat,在tomcat的server.xml中加上URIEncoding="utf-8"
2:如果使用的是tomcat插件：在pom.xml中，配置<uriEncoding>utf-8</uriEncoding>

图片处理
2钟方案：
ftp服务器
nginx服务器

购物车逻辑实现
购物车对象：ShoppingCart.java(service模块)
购物车明细对象：ShoppingCartItem.java(service模块)
购物车工具对象：ShoppingCartUtils.java(front-web模块)
一般实现逻辑
从session作用域获取购物车对象，调用其list方法，获取购物车明细集合
明细对象包含商品对象，数量，总价(数量和总价的计算在明细中处理)







